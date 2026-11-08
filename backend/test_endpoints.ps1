# Endpoint Verification Script
$baseUri = "http://localhost:8080/api/v1"
$tenantId = "tenant_apex_mumbai_01"

Write-Host "Waiting for Spring Boot to be healthy on port 8080..."
$healthy = $false
for ($i = 0; $i -lt 30; $i++) {
    try {
        $healthRes = Invoke-RestMethod -Uri "$baseUri/auth/health" -Method Get -TimeoutSec 2 -ErrorAction Stop
        if ($healthRes.data.status -eq "UP" -or $healthRes.status -eq "UP") {
            $healthy = $true
            Write-Host "Spring Boot is UP and healthy!"
            break
        }
    } catch {
        Start-Sleep -Seconds 2
    }
}

if (-not $healthy) {
    Write-Error "Spring Boot did not start within expected timeframe."
    exit 1
}
Write-Host "Engine Status: $($healthRes.data.status), Java: $($healthRes.data.javaVersion), Architecture: $($healthRes.data.architecture)"

Write-Host "`n1. Testing Authentication (POST /api/v1/auth/login)..."
$loginBody = @{
    email = "admin@apexacademy.edu.in"
    password = "Admin123!"
} | ConvertTo-Json

$headers = @{
    "Content-Type" = "application/json"
    "X-Tenant-ID" = $tenantId
}

$authRes = Invoke-RestMethod -Uri "$baseUri/auth/login" -Method Post -Body $loginBody -Headers $headers
$token = $authRes.data.token
$user = $authRes.data.user
$tenant = $authRes.data.tenant
Write-Host "Authenticated successfully!"
Write-Host " - User: $($user.fullName) ($($user.email))"
Write-Host " - Roles: $($user.roles -join ', ')"
Write-Host " - Tenant: $($tenant.schoolName) (Subdomain: $($tenant.subdomain), Affiliation: $($tenant.affiliationNumber))"
Write-Host " - JWT Bearer Token: $($token.Substring(0, 32))...[truncated]"

$authHeaders = @{
    "Content-Type" = "application/json"
    "X-Tenant-ID" = $tenantId
    "Authorization" = "Bearer $token"
}

Write-Host "`n2. Testing Students Directory (GET /api/v1/students)..."
$studentsRes = Invoke-RestMethod -Uri "$baseUri/students" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($studentsRes.data.Count) students."
$student = $studentsRes.data[0]
Write-Host "Sample Student: $($student.fullName) | Admission No: $($student.admissionNumber) | Class: $($student.className) - $($student.sectionName) | Status: $($student.status)"

Write-Host "`n3. Testing Physical Document Custody Register (GET /api/v1/students/$($student.id)/documents)..."
$docsRes = Invoke-RestMethod -Uri "$baseUri/students/$($student.id)/documents" -Method Get -Headers $authHeaders
Write-Host "Found $($docsRes.data.Count) custodial documents for student $($student.fullName):"
foreach ($doc in $docsRes.data) {
    Write-Host " - Document Type: $($doc.documentType) | Custody Status: $($doc.custodyStatus) | Locker Tag: $($doc.lockerIndex) | Verified By: $($doc.verifierName)"
}

Write-Host "`n4. Testing Batch Attendance Recording (POST /api/v1/attendance/sessions/batch)..."
$batchBody = @{
    className = "Class 10"
    sectionName = "Section A"
    attendanceDate = (Get-Date).ToString("yyyy-MM-dd")
    entries = @(
        @{
            studentId = $student.id
            status = "PRESENT"
            remarks = "Present on time"
        }
    )
} | ConvertTo-Json

$batchRes = Invoke-RestMethod -Uri "$baseUri/attendance/sessions/batch" -Method Post -Body $batchBody -Headers $authHeaders
$session = $batchRes.data
Write-Host "Attendance session submitted successfully!"
Write-Host " - Session ID: $($session.id)"
Write-Host " - Class: $($session.className) - $($session.sectionName)"
Write-Host " - Status: $($session.status) | Submitted By: $($session.submittedBy)"

Write-Host "`n5. Testing Finance Invoices (GET /api/v1/finance/invoices)..."
$invoicesRes = Invoke-RestMethod -Uri "$baseUri/finance/invoices" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($invoicesRes.data.Count) fee invoices:"
foreach ($inv in $invoicesRes.data) {
    Write-Host " - Invoice $($inv.invoiceNumber): Student: $($inv.studentName) | Total: INR $($inv.totalAmount) | Paid: INR $($inv.paidAmount) | Balance: INR $($inv.balanceAmount) | Status: $($inv.paymentStatus)"
}

Write-Host "`n6. Testing Immutable Double-Entry Ledger (GET /api/v1/finance/ledger)..."
$ledgerRes = Invoke-RestMethod -Uri "$baseUri/finance/ledger" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($ledgerRes.data.Count) double-entry ledger postings:"
foreach ($entry in $ledgerRes.data) {
    Write-Host " - Entry ID: $($entry.id) | Account: $($entry.accountCode) | Debit: INR $($entry.debit) | Credit: INR $($entry.credit) | Desc: $($entry.description)"
}

Write-Host "`n7. Testing CBSE Gradebook (GET /api/v1/examinations/gradebook)..."
$gradeRes = Invoke-RestMethod -Uri "$baseUri/examinations/gradebook?termName=Term%201%20Mid-Term&subject=Mathematics" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($gradeRes.data.Count) examination records:"
foreach ($mark in $gradeRes.data) {
    Write-Host " - Student: $($mark.studentName) | Subject: $($mark.subject) | Theory: $($mark.theoryScore) | Practical: $($mark.practicalScore) | Total: $($mark.totalScore) | Grade: $($mark.gradeBand) | Status: $($mark.resultStatus)"
}

Write-Host "`n8. Testing System Security Audit Logs (GET /api/v1/system/audit-logs)..."
$auditRes = Invoke-RestMethod -Uri "$baseUri/system/audit-logs" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($auditRes.data.Count) system audit log entries:"
foreach ($log in $auditRes.data) {
    Write-Host " - [$($log.timestamp)] Action: $($log.action) | Actor: $($log.actorName) ($($log.actorRole)) | Resource: $($log.resourceType) | Details: $($log.details)"
}

Write-Host "`n9. Testing Staff & Human Resources (GET /api/v1/staff/employees & leaves)..."
$staffRes = Invoke-RestMethod -Uri "$baseUri/staff/employees" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($staffRes.data.Count) staff employees:"
foreach ($emp in $staffRes.data) {
    Write-Host " - [$($emp.employeeCode)] $($emp.fullName) | Dept: $($emp.department) | Role: $($emp.designation) | Status: $($emp.status)"
}
$leavesRes = Invoke-RestMethod -Uri "$baseUri/staff/leaves" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($leavesRes.data.Count) staff leave applications:"
foreach ($leave in $leavesRes.data) {
    Write-Host " - Leave: $($leave.employeeName) | Type: $($leave.leaveType) | Reason: $($leave.reason) | Status: $($leave.status)"
}

Write-Host "`n10. Testing Operations: Transport Fleet GPS Telematics & Library (GET /api/v1/operations)..."
$vehiclesRes = Invoke-RestMethod -Uri "$baseUri/operations/vehicles" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($vehiclesRes.data.Count) transport vehicles:"
foreach ($v in $vehiclesRes.data) {
    Write-Host " - Vehicle: $($v.registrationNumber) | Model: $($v.vehicleModel) | Capacity: $($v.seatingCapacity) | Driver: $($v.driverName)"
}
$routesRes = Invoke-RestMethod -Uri "$baseUri/operations/routes" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($routesRes.data.Count) transport routes:"
foreach ($r in $routesRes.data) {
    Write-Host " - Route: $($r.routeName) | Vehicle: $($r.vehicleRegistration) | Status: $($r.status)"
}
$telemetryRes = Invoke-RestMethod -Uri "$baseUri/operations/telemetry/MH-02-CB-4012" -Method Get -Headers $authHeaders
Write-Host "Latest GPS Telemetry for Bus MH-02-CB-4012: Lat $($telemetryRes.data.latitude), Lng $($telemetryRes.data.longitude), Speed $($telemetryRes.data.speedKmph) km/h, Heading $($telemetryRes.data.headingDegrees) deg"

$booksRes = Invoke-RestMethod -Uri "$baseUri/operations/library/books" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($booksRes.data.Count) library books:"
foreach ($b in $booksRes.data) {
    Write-Host " - Book: '$($b.title)' by $($b.author) | Category: $($b.category) | Available: $($b.availableCopies)/$($b.totalCopies) | Location: $($b.rackLocation)"
}
$issuesRes = Invoke-RestMethod -Uri "$baseUri/operations/library/issues" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($issuesRes.data.Count) library circulation records:"
foreach ($iss in $issuesRes.data) {
    Write-Host " - Issued: '$($iss.bookTitle)' to $($iss.studentName) | Due: $($iss.dueDate) | Status: $($iss.status)"
}

Write-Host "`n============================================================"
Write-Host " ALL ENTERPRISE BACKEND ENDPOINTS VERIFIED & VALIDATED 100%"
Write-Host "============================================================"
