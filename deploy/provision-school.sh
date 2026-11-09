#!/usr/bin/env bash
# ==============================================================================
# School ERP - Zero-Touch Dedicated School Pod Provisioner
# Standard: Single-Tenant Pod Architecture (Database-Per-School & Isolated Host)
# ==============================================================================
set -euo pipefail

SCHOOL_CODE="${1:-}"
SCHOOL_NAME="${2:-}"
PORT="${3:-8080}"
DB_NAME="school_${SCHOOL_CODE}_db"
DB_USER="school_${SCHOOL_CODE}_user"
DB_PASS="$(openssl rand -hex 16)"

if [ -z "$SCHOOL_CODE" ] || [ -z "$SCHOOL_NAME" ]; then
    echo "Usage: ./provision-school.sh <school_code> <school_name> [port]"
    echo "Example: ./provision-school.sh apex 'Apex Academy' 8080"
    exit 1
fi

echo "================================================================="
echo " PROVISIONING DEDICATED POD FOR: $SCHOOL_NAME ($SCHOOL_CODE)"
echo " Database: $DB_NAME | Port: $PORT"
echo "================================================================="

# 1. Provision Isolated PostgreSQL Database
echo "1. Creating dedicated PostgreSQL database '$DB_NAME'..."
psql -h "${DB_HOST:-localhost}" -U "${PGUSER:-postgres}" -c "CREATE DATABASE $DB_NAME;" || true
psql -h "${DB_HOST:-localhost}" -U "${PGUSER:-postgres}" -c "CREATE USER $DB_USER WITH ENCRYPTED PASSWORD '$DB_PASS';" || true
psql -h "${DB_HOST:-localhost}" -U "${PGUSER:-postgres}" -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;" || true

# 2. Generate Dedicated School Pod Manifest
POD_DIR="deploy/instances/$SCHOOL_CODE"
mkdir -p "$POD_DIR"

cat <<EOF > "$POD_DIR/docker-compose.yml"
version: '3.8'
services:
  backend:
    image: school-erp-backend:latest
    container_name: school-erp-$SCHOOL_CODE
    restart: always
    ports:
      - "$PORT:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:postgresql://${DB_HOST:-localhost}:5432/$DB_NAME
      - SPRING_DATASOURCE_USERNAME=$DB_USER
      - SPRING_DATASOURCE_PASSWORD=$DB_PASS
      - SCHOOL_TENANT_ID=tenant_${SCHOOL_CODE}
      - SCHOOL_NAME=$SCHOOL_NAME
      - SCHOOL_SUBDOMAIN=$SCHOOL_CODE
      - JWT_SECRET=$(openssl rand -hex 32)
    networks:
      - school-erp-network

networks:
  school-erp-network:
    external: true
EOF

echo "2. Dedicated Docker Compose manifest generated at: $POD_DIR/docker-compose.yml"
echo "3. Pod credentials recorded securely in: $POD_DIR/.env"

cat <<EOF > "$POD_DIR/.env"
SCHOOL_CODE=$SCHOOL_CODE
SCHOOL_NAME=$SCHOOL_NAME
DB_NAME=$DB_NAME
DB_USER=$DB_USER
DB_PASS=$DB_PASS
PORT=$PORT
EOF

echo "================================================================="
echo " POD PROVISIONING COMPLETE: $SCHOOL_NAME ($SCHOOL_CODE)"
echo " URL: http://${SCHOOL_CODE}.schoolerp.cloud:$PORT"
echo "================================================================="

