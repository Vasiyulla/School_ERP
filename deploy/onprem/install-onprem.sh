#!/usr/bin/env bash
# ==============================================================================
# School ERP - Enterprise On-Premise & Private Cloud Turnkey Installer
# Standard: GitLab Omnibus / Odoo Enterprise Appliance Standards
# Supported OS: Ubuntu 22.04/24.04 LTS, Debian 12, Rocky Linux 9, RHEL 9
# ==============================================================================
set -euo pipefail

RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${BLUE}======================================================================${NC}"
echo -e "${BLUE}       SCHOOL ERP SYSTEM - ENTERPRISE ON-PREMISE INSTALLER            ${NC}"
echo -e "${BLUE}======================================================================${NC}"

# 1. Root / Sudo Check
if [ "$(id -u)" -ne 0 ]; then
    echo -e "${RED}[ERROR] This installation script must be run as root or with sudo.${NC}"
    exit 1
fi

# 2. Hardware Audit & Verification
echo -e "${YELLOW}[1/6] Auditing Campus Server Hardware Specifications...${NC}"
TOTAL_RAM_KB=$(grep MemTotal /proc/meminfo | awk '{print $2}')
TOTAL_RAM_GB=$((TOTAL_RAM_KB / 1024 / 1024))
CPU_CORES=$(nproc)
AVAILABLE_DISK_GB=$(df -BG /var/lib | awk 'NR==2 {print $4}' | sed 's/G//')

echo "      - Detected CPU Cores  : ${CPU_CORES}"
echo "      - Detected Total RAM  : ${TOTAL_RAM_GB} GB"
echo "      - Available Disk Space: ${AVAILABLE_DISK_GB} GB"

if [ "$TOTAL_RAM_GB" -lt 8 ]; then
    echo -e "${RED}[!] WARNING: Minimum 8 GB RAM recommended for production on-premise operations.${NC}"
    read -rp "Do you wish to continue anyway? (y/N): " FORCE_INSTALL
    if [[ ! "$FORCE_INSTALL" =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

if [ "$AVAILABLE_DISK_GB" -lt 20 ]; then
    echo -e "${RED}[ERROR] At least 20 GB free disk space required. Current: ${AVAILABLE_DISK_GB} GB.${NC}"
    exit 1
fi
echo -e "${GREEN}[✓] Hardware specifications verified.${NC}"

# 3. Docker & Docker Compose Dependency Installation
echo -e "${YELLOW}[2/6] Verifying Container Runtime Engine...${NC}"
if ! command -v docker &> /dev/null; then
    echo "      Docker not found. Installing Docker Engine..."
    curl -fsSL https://get.docker.com | sh
    systemctl enable --now docker
fi

if ! docker compose version &> /dev/null; then
    echo "      Installing Docker Compose plugin..."
    apt-get update && apt-get install -y docker-compose-plugin || yum install -y docker-compose-plugin
fi
echo -e "${GREEN}[✓] Docker Engine $(docker --version | awk '{print $3}' | tr -d ',') ready.${NC}"

# 4. Interactive Configuration
echo -e "${YELLOW}[3/6] Configuring Institution Parameters...${NC}"
read -rp "Enter School Short Code (lowercase, no spaces, e.g., dps_mumbai): " SCHOOL_CODE
SCHOOL_CODE=$(echo "$SCHOOL_CODE" | tr '[:upper:]' '[:lower:]' | tr -cd '[:alnum:]_')
read -rp "Enter Full Institution Name: " SCHOOL_NAME
read -rp "Enter Primary Server Hostname or Local IP (e.g., erp.school.edu.in or 192.168.1.50): " SCHOOL_DOMAIN
read -rp "Enter Cloudflare Zero Trust Tunnel Token (press Enter to skip if using Local LAN only): " CF_TOKEN

# 5. Cryptographic Secret Generation & Environment Preparation
echo -e "${YELLOW}[4/6] Generating Cryptographically Secure Secrets...${NC}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env"

DB_PASS_GEN=$(openssl rand -hex 20)
REDIS_PASS_GEN=$(openssl rand -hex 20)
MINIO_PASS_GEN=$(openssl rand -hex 20)
JWT_SECRET_GEN=$(openssl rand -hex 32)

cat <<EOF > "$ENV_FILE"
# School ERP On-Premise Environment Variables
TENANT_ID=tenant_${SCHOOL_CODE}
SCHOOL_NAME=${SCHOOL_NAME}
SCHOOL_DOMAIN=${SCHOOL_DOMAIN}
DB_NAME=school_${SCHOOL_CODE}_db
DB_USER=school_${SCHOOL_CODE}_user
DB_PASS=${DB_PASS_GEN}
REDIS_PASS=${REDIS_PASS_GEN}
MINIO_ROOT_USER=admin_${SCHOOL_CODE}
MINIO_ROOT_PASSWORD=${MINIO_PASS_GEN}
S3_BUCKET_NAME=school-${SCHOOL_CODE}-vault
JWT_SECRET=${JWT_SECRET_GEN}
CLOUDFLARE_TUNNEL_TOKEN=${CF_TOKEN}
EOF
chmod 600 "$ENV_FILE"
echo -e "${GREEN}[✓] Environment configured securely at ${ENV_FILE}.${NC}"

# 6. Create Backup & Data Directories
echo -e "${YELLOW}[5/6] Creating Persistent Storage & Backup Directories...${NC}"
mkdir -p "${SCRIPT_DIR}/backups/wal"
mkdir -p "${SCRIPT_DIR}/backups/daily"
mkdir -p "${SCRIPT_DIR}/backups/archive"
chmod 700 "${SCRIPT_DIR}/backups"

# 7. Launch Container Stack
echo -e "${YELLOW}[6/6] Launching Dedicated ERP Container Pod...${NC}"
docker compose -f "${SCRIPT_DIR}/docker-compose.yml" --env-file "$ENV_FILE" up -d

echo "      Waiting for PostgreSQL primary database readiness..."
RETRY_COUNT=0
until docker compose -f "${SCRIPT_DIR}/docker-compose.yml" exec -T db-primary pg_isready -U "school_${SCHOOL_CODE}_user" -d "school_${SCHOOL_CODE}_db" &> /dev/null; do
    RETRY_COUNT=$((RETRY_COUNT + 1))
    if [ "$RETRY_COUNT" -gt 30 ]; then
        echo -e "${RED}[ERROR] Database startup timed out after 60 seconds.${NC}"
        docker compose -f "${SCRIPT_DIR}/docker-compose.yml" logs db-primary
        exit 1
    fi
    sleep 2
done

echo -e "${GREEN}======================================================================${NC}"
echo -e "${GREEN} [SUCCESS] SCHOOL ERP ON-PREMISE PLATFORM INSTALLED SUCCESSFULLY!     ${NC}"
echo -e "${GREEN}======================================================================${NC}"
echo " Institutional Portal : http://${SCHOOL_DOMAIN} (or https://${SCHOOL_DOMAIN})"
echo " Backend REST API     : http://${SCHOOL_DOMAIN}:8080/api/v1"
echo " Health Status Check  : http://${SCHOOL_DOMAIN}:8080/actuator/health"
echo " Local MinIO Console  : http://${SCHOOL_DOMAIN}:9001 (User: admin_${SCHOOL_CODE})"
echo " Backup Storage Path  : ${SCRIPT_DIR}/backups"
echo " Configuration File   : ${ENV_FILE}"
echo "======================================================================"
