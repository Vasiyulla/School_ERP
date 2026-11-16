# ==============================================================================
# School ERP - Enterprise On-Premise & Private Cloud Turnkey Installer (PowerShell)
# Supported OS: Windows Server 2022 / 2025, Windows 11 Pro with Docker Desktop / WSL2
# ==============================================================================

[CmdletBinding()]
param (
    [string]$SchoolCode,
    [string]$SchoolName,
    [string]$SchoolDomain = "localhost",
    [string]$CloudflareTunnelToken = ""
)

$ErrorActionPreference = "Stop"

Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host "       SCHOOL ERP SYSTEM - ENTERPRISE ON-PREMISE WINDOWS INSTALLER     " -ForegroundColor Cyan
Write-Host "======================================================================" -ForegroundColor Cyan

# 1. Administrator Verification
$currentPrincipal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
if (-not $currentPrincipal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Warning "This script should be run from an elevated Administrator PowerShell console."
}

# 2. Hardware Audit
Write-Host "`n[1/5] Auditing Campus Host Hardware Specifications..." -ForegroundColor Yellow
$os = Get-CimInstance Win32_OperatingSystem
$totalRamGb = [math]::Round($os.TotalVisibleMemorySize / 1MB, 2)
$cpu = Get-CimInstance Win32_Processor | Select-Object -First 1
$cores = $cpu.NumberOfCores
$freeDiskGb = [math]::Round((Get-PSDrive -Name C).Free / 1GB, 2)

Write-Host "      - Detected CPU Cores  : $cores ($($cpu.Name))"
Write-Host "      - Detected Total RAM  : $totalRamGb GB"
Write-Host "      - Free Space (C:\)    : $freeDiskGb GB"

if ($totalRamGb -lt 8) {
    Write-Warning "Minimum 8 GB RAM recommended for on-premise production server."
}

# 3. Docker Verification
Write-Host "`n[2/5] Verifying Docker Container Engine..." -ForegroundColor Yellow
try {
    $dockerVer = docker --version
    Write-Host "      - Docker is active: $dockerVer" -ForegroundColor Green
} catch {
    Write-Error "Docker Desktop or Docker CLI is not installed or not in PATH. Please install Docker before running this script."
}

# 4. Interactive Prompts (if not supplied via parameters)
if (-not $SchoolCode) {
    $SchoolCode = Read-Host "Enter School Code (e.g. dps_pune)"
}
$SchoolCode = ($SchoolCode.ToLower() -replace '[^a-z0-9_]', '')

if (-not $SchoolName) {
    $SchoolName = Read-Host "Enter Full School Name (e.g. Delhi Public School Pune)"
}
if ($SchoolDomain -eq "localhost") {
    $promptDomain = Read-Host "Enter Server IP or Hostname (Press Enter for localhost / LAN IP)"
    if ($promptDomain) { $SchoolDomain = $promptDomain }
}

# 5. Cryptographic Secret Generation
Write-Host "`n[3/5] Generating Cryptographic Master Passwords..." -ForegroundColor Yellow
function New-RandomHex([int]$bytes = 20) {
    $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
    $buf = New-Object byte[] $bytes
    $rng.GetBytes($buf)
    return -join ($buf | ForEach-Object { $_.ToString("x2") })
}

$dbPass = New-RandomHex 20
$redisPass = New-RandomHex 20
$minioPass = New-RandomHex 20
$jwtSecret = New-RandomHex 32

$scriptDir = $PSScriptRoot
if (-not $scriptDir) { $scriptDir = (Get-Location).Path }
$envFile = Join-Path $scriptDir ".env"

$envContent = @"
# School ERP On-Premise Environment Variables
TENANT_ID=tenant_${SchoolCode}
SCHOOL_NAME=${SchoolName}
SCHOOL_DOMAIN=${SchoolDomain}
DB_NAME=school_${SchoolCode}_db
DB_USER=school_${SchoolCode}_user
DB_PASS=${dbPass}
REDIS_PASS=${redisPass}
MINIO_ROOT_USER=admin_${SchoolCode}
MINIO_ROOT_PASSWORD=${minioPass}
S3_BUCKET_NAME=school-${SchoolCode}-vault
JWT_SECRET=${jwtSecret}
CLOUDFLARE_TUNNEL_TOKEN=${CloudflareTunnelToken}
"@

Set-Content -Path $envFile -Value $envContent -Encoding UTF8
Write-Host "      - Secrets saved securely to $envFile" -ForegroundColor Green

# 6. Create Backup Directories
Write-Host "`n[4/5] Initializing Local Backup Storage Structure..." -ForegroundColor Yellow
$backupDirs = @("backups\wal", "backups\daily", "backups\archive")
foreach ($dir in $backupDirs) {
    $p = Join-Path $scriptDir $dir
    if (-not (Test-Path $p)) {
        New-Item -ItemType Directory -Path $p -Force | Out-Null
    }
}

# 7. Launch Docker Compose Stack
Write-Host "`n[5/5] Launching ERP Containers via Docker Compose..." -ForegroundColor Yellow
$composeFile = Join-Path $scriptDir "docker-compose.yml"
docker compose -f $composeFile --env-file $envFile up -d

Write-Host "`n======================================================================" -ForegroundColor Green
Write-Host " [SUCCESS] SCHOOL ERP ON-PREMISE STACK IS RUNNING!" -ForegroundColor Green
Write-Host "======================================================================" -ForegroundColor Green
Write-Host " Web Portal URL       : http://${SchoolDomain} (or http://localhost)"
Write-Host " Backend API Gateway  : http://${SchoolDomain}:8080/api/v1"
Write-Host " Actuator Health Probe: http://${SchoolDomain}:8080/actuator/health"
Write-Host " MinIO Object Storage : http://${SchoolDomain}:9001 (User: admin_${SchoolCode})"
Write-Host " Data Directory       : $scriptDir"
Write-Host "======================================================================"
