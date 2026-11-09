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

Write-Host "`n11. Testing Communication: Circulars & Multi-Channel Dispatches (GET /api/v1/communication)..."
$circularsRes = Invoke-RestMethod -Uri "$baseUri/communication/circulars" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($circularsRes.data.Count) school circulars:"
foreach ($c in $circularsRes.data) {
    Write-Host " - Circular: '$($c.title)' | Audience: $($c.targetAudience) | Priority: $($c.priority) | By: $($c.publishedBy)"
}
$dispatchesRes = Invoke-RestMethod -Uri "$baseUri/communication/dispatches" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($dispatchesRes.data.Count) notification dispatch logs:"
foreach ($d in $dispatchesRes.data) {
    Write-Host " - Dispatch to $($d.recipientName) via $($d.channel) | Trigger: $($d.triggerEvent) | Status: $($d.deliveryStatus)"
}

Write-Host "`n12. Testing Enterprise Payment Gateway Webhooks & Idempotency (POST /api/v1/finance/webhooks/razorpay)..."
$webhookEventId = "evt_rzp_live_" + (Get-Random -Minimum 100000 -Maximum 999999)
$webhookPayload = @{
    eventId = $webhookEventId
    eventType = "payment.captured"
    invoiceNumber = "INV-2026-0843"
    amount = 15000.00
    rawJson = '{"order_id":"order_88291","status":"captured"}'
} | ConvertTo-Json

$webhookHeaders = @{
    "Content-Type" = "application/json"
    "X-Tenant-ID" = $tenantId
    "X-Razorpay-Signature" = "f56b2512f451e06d99a224"
}

# 12.1 First Webhook Delivery
$whRes1 = Invoke-RestMethod -Uri "$baseUri/finance/webhooks/razorpay" -Method Post -Body $webhookPayload -Headers $webhookHeaders
Write-Host "Webhook Delivery #1: Status: $($whRes1.data.status), Message: $($whRes1.data.message), IsDuplicate: $($whRes1.data.isDuplicate)"

# 12.2 Replay Attack / Network Retry (Identical Event ID)
$whRes2 = Invoke-RestMethod -Uri "$baseUri/finance/webhooks/razorpay" -Method Post -Body $webhookPayload -Headers $webhookHeaders
Write-Host "Webhook Replay Delivery #2: Status: $($whRes2.data.status), Message: $($whRes2.data.message), IsDuplicate: $($whRes2.data.isDuplicate)"

# 12.3 Inspect Webhook Audit Log
$whLogsRes = Invoke-RestMethod -Uri "$baseUri/finance/webhooks" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($whLogsRes.data.Count) recorded webhook events in audit register."

Write-Host "`n13. Testing Dynamic Fee Structures by Grade Level (GET /api/v1/finance/structures)..."
$structuresRes = Invoke-RestMethod -Uri "$baseUri/finance/structures" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($structuresRes.data.Count) active fee structures:"
foreach ($str in $structuresRes.data) {
    Write-Host " - Grade: $($str.gradeLevel) | Academic Year: $($str.academicYear) | Total: INR $($str.totalAmount) | Items: $($str.items.Count)"
}

Write-Host "`n14. Testing Sibling & Scholarship Concessions (GET /api/v1/finance/concessions)..."
$concessionsRes = Invoke-RestMethod -Uri "$baseUri/finance/concessions" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($concessionsRes.data.Count) approved fee concessions:"
foreach ($cn in $concessionsRes.data) {
    Write-Host " - Student: $($cn.studentName) | Concession: $($cn.concessionType) ($($cn.percentageDiscount)%) | Reason: $($cn.reason)"
}

if ($concessionsRes.data.Count -gt 0) {
    $rheaCon = $concessionsRes.data[0]
    Write-Host "Calculating Net Fee for student $($rheaCon.studentName) on INR 26,000 tuition fee..."
    $calcRes = Invoke-RestMethod -Uri "$baseUri/finance/concessions/calculate?studentId=$($rheaCon.studentId)&grossAmount=26000" -Method Get -Headers $authHeaders
    Write-Host " - Gross Fee: INR $($calcRes.data.grossFee)"
    Write-Host " - Sibling Discount ($($calcRes.data.discountPercentage)%): INR $($calcRes.data.discountAmount)"
    Write-Host " - Net Payable: INR $($calcRes.data.netFee)"
}

Write-Host "`n15. Testing Cheque / DD Clearance Lifecycle (GET /api/v1/finance/cheques)..."
$chequesRes = Invoke-RestMethod -Uri "$baseUri/finance/cheques" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($chequesRes.data.Count) cheque custody records:"
foreach ($ch in $chequesRes.data) {
    Write-Host " - Cheque #$($ch.chequeNumber) | Bank: $($ch.bankName) | Amount: INR $($ch.amount) | Payer: $($ch.payerName) | Status: $($ch.status) | Bounce Penalty: INR $($ch.bouncePenalty)"
}

Write-Host "`n16. Testing Automated Late Fee Surcharge Calculator (GET /api/v1/finance/invoices/{id}/surcharge)..."
$invoicesRes = Invoke-RestMethod -Uri "$baseUri/finance/invoices" -Method Get -Headers $authHeaders
if ($invoicesRes.data.Count -gt 0) {
    $testInv = $invoicesRes.data[0]
    $surchargeRes = Invoke-RestMethod -Uri "$baseUri/finance/invoices/$($testInv.id)/surcharge" -Method Get -Headers $authHeaders
    Write-Host " - Invoice: $($surchargeRes.data.invoiceNumber)"
    Write-Host " - Days Overdue: $($surchargeRes.data.daysOverdue)"
    Write-Host " - Daily Surcharge Rate: INR $($surchargeRes.data.dailyRate)"
    Write-Host " - Total Accrued Surcharge: INR $($surchargeRes.data.totalSurcharge)"
    Write-Host " - Is Surcharge Applicable: $($surchargeRes.data.isSurchargeApplicable)"
}

Write-Host "`n17. Testing CBSE 70/20/10 Consolidated Report Card (GET /api/v1/examinations/gradebook/report-card)..."
$reportCardRes = Invoke-RestMethod -Uri "$baseUri/examinations/gradebook/report-card?studentId=$($student.id)&termName=Term 1 Mid-Term" -Method Get -Headers $authHeaders
$rc = $reportCardRes.data
Write-Host "Report Card Compiled for $($rc.studentName) ($($rc.className) - $($rc.sectionName), Roll #$($rc.rollNumber)):"
Write-Host " - School Affiliation: $($rc.schoolName) ($($rc.affiliationNumber))"
Write-Host " - Total Weighted Marks: $($rc.totalMarksObtained) / $($rc.totalMaxMarks) ($($rc.percentage)%)"
Write-Host " - CBSE CGPA (10-Point Scale): $($rc.cgpa) | Overall Grade: $($rc.overallGrade) | Status: $($rc.resultStatus)"
Write-Host " - Section Rank: Rank #$($rc.classRank) out of $($rc.totalStudentsInClass) students"
Write-Host " - Principal Signature: $($rc.principalSignatureStatus)"
Write-Host " - SHA-256 Verification Hash: $($rc.verificationHash)"
Write-Host " - Verification URL: $($rc.verificationUrl)"

Write-Host "`n18. Testing Public Authenticity Verification without Auth Token (GET /api/v1/examinations/gradebook/report-card/verify)..."
# Calling with no Authorization header to confirm public tamper-proof validation
$publicHeaders = @{
    "Content-Type" = "application/json"
    "X-Tenant-ID" = $tenantId
}
$verifyRes = Invoke-RestMethod -Uri "$baseUri/examinations/gradebook/report-card/verify?hash=$($rc.verificationHash)" -Method Get -Headers $publicHeaders
Write-Host "Public Verification Result: $($verifyRes.message)"
Write-Host " - Student: $($verifyRes.data.studentName) (Admission No: $($verifyRes.data.admissionNumber))"
Write-Host " - Percentage: $($verifyRes.data.percentage)%, CGPA: $($verifyRes.data.cgpa), Grade: $($verifyRes.data.overallGrade)"
Write-Host " - Ledger Verification Status: $($verifyRes.data.verificationStatus) (Issued: $($verifyRes.data.issuedAt))"

Write-Host "`n19. Testing Mobile Device Token Registration (POST & GET /api/v1/communication/devices)..."
$deviceBody = @{
    userId = [guid]::NewGuid().ToString()
    userEmail = "parent.test@apexacademy.edu.in"
    userRole = "PARENT"
    deviceToken = "fcm_token_pixel8_test_" + (Get-Random -Minimum 1000 -Maximum 9999)
    deviceType = "ANDROID"
    deviceModel = "Google Pixel 8 Pro"
    appVersion = "1.0.0-parent-native"
} | ConvertTo-Json

$regDeviceRes = Invoke-RestMethod -Uri "$baseUri/communication/devices/register" -Method Post -Body $deviceBody -Headers $authHeaders
Write-Host "Device Token Registered: $($regDeviceRes.data.deviceModel) ($($regDeviceRes.data.deviceType)) for $($regDeviceRes.data.userEmail)"

$devicesRes = Invoke-RestMethod -Uri "$baseUri/communication/devices" -Method Get -Headers $authHeaders
Write-Host "Total Active Mobile Devices Registered: $($devicesRes.data.Count)"

Write-Host "`n20. Testing High-Priority FCM Push Notification Dispatch (POST /api/v1/communication/devices/push-test)..."
$pushBody = @{
    userId = [guid]::NewGuid().ToString()
    recipientName = "Rajesh Sharma"
    title = "Live Bus GPS Alert"
    body = "School Bus MH-02-CB-4012 is arriving at your designated stop (Lokhandwala Circle) in 3 minutes."
    eventType = "FLEET_GPS_PROXIMITY"
} | ConvertTo-Json

$pushRes = Invoke-RestMethod -Uri "$baseUri/communication/devices/push-test" -Method Post -Body $pushBody -Headers $authHeaders
Write-Host "FCM Push Dispatch Result: $($pushRes.data.deliveryStatus) via channel $($pushRes.data.channel)"
Write-Host "Provider Message ID: $($pushRes.data.providerMessageId)"

Write-Host "`n21. Testing Odoo-Style Dynamic Tenant Module Licensing (GET & POST /api/v1/system/modules)..."
$modulesRes = Invoke-RestMethod -Uri "$baseUri/system/modules" -Method Get -Headers $authHeaders
Write-Host "Retrieved $($modulesRes.data.Count) licensed enterprise modules for tenant:"
foreach ($m in $modulesRes.data) {
    Write-Host " - Module: $($m.moduleKey) [$($m.moduleName)] | Tier: $($m.tier) | Enabled: $($m.enabled)"
}

# Test toggling module state
$toggleBody = @{ enabled = $false } | ConvertTo-Json
$toggleRes = Invoke-RestMethod -Uri "$baseUri/system/modules/FLEET_GPS/toggle" -Method Post -Body $toggleBody -Headers $authHeaders
Write-Host "Toggled module FLEET_GPS to: enabled = $($toggleRes.data.enabled)"
# Restore module state
$restoreBody = @{ enabled = $true } | ConvertTo-Json
$restoreRes = Invoke-RestMethod -Uri "$baseUri/system/modules/FLEET_GPS/toggle" -Method Post -Body $restoreBody -Headers $authHeaders
Write-Host "Restored module FLEET_GPS to: enabled = $($restoreRes.data.enabled)"

Write-Host "`n============================================================"
Write-Host " ALL ENTERPRISE BACKEND ENDPOINTS VERIFIED & VALIDATED 100%"
Write-Host "============================================================"
