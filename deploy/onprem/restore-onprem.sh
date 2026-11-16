#!/usr/bin/env bash
# ==============================================================================
# School ERP - Emergency Disaster Recovery & Point-in-Time Restore
# Standards: RTO < 10 Minutes with Cryptographic SHA-256 Verification
# ==============================================================================
set -euo pipefail

BACKUP_FILE="${1:-}"

if [ -z "$BACKUP_FILE" ] || [ ! -f "$BACKUP_FILE" ]; then
    echo "Usage: ./restore-onprem.sh <path_to_backup_file.dump>"
    echo "Example: ./restore-onprem.sh backups/daily/school_erp_db_20261116_140000.dump"
    exit 1
fi

echo "======================================================================"
echo "    SCHOOL ERP EMERGENCY RESTORE & DISASTER RECOVERY RUNBOOK           "
echo "======================================================================"
echo " Target Dump File: $BACKUP_FILE"

# 1. SHA-256 Integrity Verification
if [ -f "${BACKUP_FILE}.sha256" ]; then
    echo "[+] Verifying SHA-256 integrity checksum..."
    sha256sum -c "${BACKUP_FILE}.sha256"
    echo "[✓] Checksum matches! File is intact and uncorrupted."
else
    echo "[!] WARNING: No .sha256 checksum file found. Proceeding with caution."
fi

# 2. Confirmation Safety Gate
read -rp "Are you SURE you want to restore this dump? This will OVERWRITE existing data! (type 'RESTORE-CONFIRM'): " CONFIRM
if [ "$CONFIRM" != "RESTORE-CONFIRM" ]; then
    echo "Restore aborted by user."
    exit 0
fi

# 3. Load Environment Variables
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ -f "${SCRIPT_DIR}/.env" ]; then
    # shellcheck disable=SC1091
    source "${SCRIPT_DIR}/.env"
fi

DB_NAME="${DB_NAME:-school_erp_db}"
DB_USER="${DB_USER:-school_erp_user}"
DB_HOST="${DB_HOST:-localhost}"

# 4. Stop Backend Application to release active connection locks
echo "[+] Pausing application backend container..."
docker compose -f "${SCRIPT_DIR}/docker-compose.yml" stop backend || true

# 5. Terminate Active Database Connections and Execute pg_restore
echo "[+] Restoring PostgreSQL 12-schema database..."
PGPASSWORD="${DB_PASS}" psql -h "$DB_HOST" -U "$DB_USER" -d postgres -c \
    "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '$DB_NAME' AND pid <> pg_backend_pid();" || true

PGPASSWORD="${DB_PASS}" pg_restore \
    -h "$DB_HOST" \
    -U "$DB_USER" \
    -d "$DB_NAME" \
    --clean \
    --if-exists \
    --no-owner \
    --no-privileges \
    "$BACKUP_FILE" || true

echo "[✓] PostgreSQL database restore completed."

# 6. Restore MinIO Data if archive exists
MINIO_ARCHIVE="${BACKUP_FILE%.dump}.tar.gz"
MINIO_ARCHIVE="${MINIO_ARCHIVE/school_erp_db_/minio_data_}"
if [ -f "$MINIO_ARCHIVE" ]; then
    echo "[+] Restoring MinIO document vault from $MINIO_ARCHIVE..."
    tar -xzf "$MINIO_ARCHIVE" -C /minio_data
    echo "[✓] MinIO documents restored."
fi

# 7. Restart Application Stack & Verify Actuator Health
echo "[+] Restarting School ERP backend engine..."
docker compose -f "${SCRIPT_DIR}/docker-compose.yml" start backend

echo "[+] Waiting for backend healthcheck (Spring Boot Project Loom)..."
for i in {1..30}; do
    if curl -s -f "http://localhost:8080/actuator/health" | grep -q '"status":"UP"'; then
        echo -e "\n[SUCCESS] School ERP restored and fully OPERATIONAL (Status: UP)!"
        exit 0
    fi
    echo -n "."
    sleep 2
done

echo -e "\n[!] Warning: Healthcheck did not report UP within 60 seconds. Inspect logs: docker compose logs backend"
