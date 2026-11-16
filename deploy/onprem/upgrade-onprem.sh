#!/usr/bin/env bash
# ==============================================================================
# School ERP - Zero-Downtime Safe Upgrade & Automatic Rollback Engine
# Standards: Pre-Upgrade DB Snapshot + Health Probe Auto-Rollback Guard
# ==============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="${SCRIPT_DIR}/docker-compose.yml"
ENV_FILE="${SCRIPT_DIR}/.env"

if [ -f "$ENV_FILE" ]; then
    # shellcheck disable=SC1091
    source "$ENV_FILE"
fi

echo "======================================================================"
echo "    SCHOOL ERP - ZERO-DOWNTIME SAFE UPGRADE & HEALTH SENTINEL         "
echo "======================================================================"

# 1. Pre-Upgrade Safety Database Snapshot
SNAPSHOT_TIME=$(date +"%Y%m%d_%H%M%S")
SNAPSHOT_FILE="${SCRIPT_DIR}/backups/daily/pre_upgrade_snapshot_${SNAPSHOT_TIME}.dump"
mkdir -p "${SCRIPT_DIR}/backups/daily"

echo "[+] Creating pre-upgrade database safety snapshot: $SNAPSHOT_FILE..."
PGPASSWORD="${DB_PASS:-secure_db_pass}" pg_dump \
    -h localhost \
    -U "${DB_USER:-school_erp_user}" \
    -d "${DB_NAME:-school_erp_db}" \
    --format=custom \
    --compress=9 \
    --file="$SNAPSHOT_FILE"
echo "[✓] Pre-upgrade snapshot created successfully."

# 2. Pull Latest Signed Container Images
echo "[+] Pulling latest release container images..."
docker compose -f "$COMPOSE_FILE" --env-file "$ENV_FILE" pull backend frontend || true

# 3. Rolling Restart of Backend Container (Flyway executes pending migrations)
echo "[+] Restarting backend container with new release..."
docker compose -f "$COMPOSE_FILE" --env-file "$ENV_FILE" up -d --no-deps backend

# 4. Observability & Healthcheck Sentinel (60-second window)
echo "[+] Awaiting Spring Boot Actuator liveness & readiness probes..."
UPGRADE_SUCCESS=false
for i in {1..30}; do
    if curl -s -f "http://localhost:8080/actuator/health" | grep -q '"status":"UP"'; then
        UPGRADE_SUCCESS=true
        break
    fi
    echo -n "."
    sleep 2
done

# 5. Evaluation & Automatic Rollback Guard
if [ "$UPGRADE_SUCCESS" = true ]; then
    echo -e "\n[SUCCESS] Release upgraded successfully! Backend health probe is UP."
    # Refresh frontend container
    docker compose -f "$COMPOSE_FILE" --env-file "$ENV_FILE" up -d --no-deps frontend
    echo "[SUCCESS] School ERP update sequence completed cleanly."
else
    echo -e "\n[!] CRITICAL: Backend failed to achieve HEALTHY state within 60 seconds!"
    echo "[!] Triggering automated emergency rollback sequence..."
    
    # Emergency rollback to pre-upgrade state
    "${SCRIPT_DIR}/restore-onprem.sh" "$SNAPSHOT_FILE"
    echo "[ROLLBACK COMPLETE] Application restored to pre-upgrade snapshot."
    exit 1
fi
