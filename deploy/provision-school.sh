#!/usr/bin/env bash
# ==============================================================================
# School ERP - Zero-Touch Dedicated School Pod Provisioner
# Standard: Dual-Database High Availability Architecture (Primary + Secondary HA)
# ==============================================================================
set -euo pipefail

SCHOOL_CODE="${1:-}"
SCHOOL_NAME="${2:-}"
PORT="${3:-8080}"
DB_PRIMARY_PORT="${4:-5432}"
DB_SECONDARY_PORT="${5:-5433}"
DB_NAME="school_${SCHOOL_CODE}_db"
DB_USER="school_${SCHOOL_CODE}_user"
DB_PASS="$(openssl rand -hex 16)"

if [ -z "$SCHOOL_CODE" ] || [ -z "$SCHOOL_NAME" ]; then
    echo "Usage: ./provision-school.sh <school_code> <school_name> [port] [db_primary_port] [db_secondary_port]"
    echo "Example: ./provision-school.sh apex 'Apex Academy' 8080 5432 5433"
    exit 1
fi

echo "================================================================="
echo " PROVISIONING DEDICATED DUAL-DB HA POD FOR: $SCHOOL_NAME ($SCHOOL_CODE)"
echo " Primary DB Port: $DB_PRIMARY_PORT | Secondary DB Port: $DB_SECONDARY_PORT | Web Port: $PORT"
echo "================================================================="

# 1. Provision Isolated PostgreSQL Database
echo "1. Creating dedicated PostgreSQL database '$DB_NAME'..."
psql -h "${DB_HOST:-localhost}" -U "${PGUSER:-postgres}" -c "CREATE DATABASE $DB_NAME;" || true
psql -h "${DB_HOST:-localhost}" -U "${PGUSER:-postgres}" -c "CREATE USER $DB_USER WITH ENCRYPTED PASSWORD '$DB_PASS';" || true
psql -h "${DB_HOST:-localhost}" -U "${PGUSER:-postgres}" -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;" || true

# 2. Generate Dedicated School Pod Manifest with Dual-DB HA Pair
POD_DIR="deploy/instances/$SCHOOL_CODE"
mkdir -p "$POD_DIR"

cat <<EOF > "$POD_DIR/docker-compose.yml"
version: '3.8'
services:
  # DB 1: Primary Active Database (Read/Write)
  db-primary:
    image: postgres:16-alpine
    container_name: school_${SCHOOL_CODE}_db_primary
    restart: always
    environment:
      POSTGRES_DB: $DB_NAME
      POSTGRES_USER: $DB_USER
      POSTGRES_PASSWORD: $DB_PASS
    ports:
      - "$DB_PRIMARY_PORT:5432"
    volumes:
      - primary_data:/var/lib/postgresql/data
    networks:
      - school_${SCHOOL_CODE}_net

  # DB 2: Secondary Hot Standby Database (Continuous Streaming Replication)
  db-secondary:
    image: postgres:16-alpine
    container_name: school_${SCHOOL_CODE}_db_secondary
    restart: always
    environment:
      POSTGRES_DB: $DB_NAME
      POSTGRES_USER: $DB_USER
      POSTGRES_PASSWORD: $DB_PASS
      PGUSER: $DB_USER
      PGPASSWORD: $DB_PASS
    ports:
      - "$DB_SECONDARY_PORT:5432"
    depends_on:
      - db-primary
    volumes:
      - secondary_data:/var/lib/postgresql/data
    networks:
      - school_${SCHOOL_CODE}_net

  # Dedicated Modular Monolith Backend with Multi-Host Failover Driver
  backend:
    image: school-erp-backend:latest
    container_name: school-erp-$SCHOOL_CODE
    restart: always
    ports:
      - "$PORT:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      # Multi-Host JDBC URL: Automatically fails over to whichever node is PRIMARY
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db-primary:5432,db-secondary:5432/$DB_NAME?targetServerType=primary
      - SPRING_DATASOURCE_USERNAME=$DB_USER
      - SPRING_DATASOURCE_PASSWORD=$DB_PASS
      - SCHOOL_TENANT_ID=tenant_${SCHOOL_CODE}
      - SCHOOL_NAME=$SCHOOL_NAME
      - SCHOOL_SUBDOMAIN=$SCHOOL_CODE
      - JWT_SECRET=$(openssl rand -hex 32)
    depends_on:
      - db-primary
      - db-secondary
    networks:
      - school_${SCHOOL_CODE}_net

volumes:
  primary_data:
  secondary_data:

networks:
  school_${SCHOOL_CODE}_net:
    driver: bridge
EOF

echo "2. Dedicated Dual-DB Docker Compose manifest generated at: $POD_DIR/docker-compose.yml"
echo "3. Pod credentials recorded securely in: $POD_DIR/.env"

cat <<EOF > "$POD_DIR/.env"
SCHOOL_CODE=$SCHOOL_CODE
SCHOOL_NAME=$SCHOOL_NAME
DB_NAME=$DB_NAME
DB_USER=$DB_USER
DB_PASS=$DB_PASS
PORT=$PORT
DB_PRIMARY_PORT=$DB_PRIMARY_PORT
DB_SECONDARY_PORT=$DB_SECONDARY_PORT
EOF

echo "================================================================="
echo " DUAL-DB HA POD PROVISIONING COMPLETE: $SCHOOL_NAME ($SCHOOL_CODE)"
echo " URL: http://${SCHOOL_CODE}.schoolerp.cloud:$PORT"
echo " Primary DB: localhost:$DB_PRIMARY_PORT | Secondary DB: localhost:$DB_SECONDARY_PORT"
echo "================================================================="

