package com.schoolerp;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.communication.service.CommunicationService;
import com.schoolerp.modules.finance.service.FinanceService;
import com.schoolerp.modules.identity.dto.AuthResponse;
import com.schoolerp.modules.identity.dto.LoginRequest;
import com.schoolerp.modules.identity.service.AuthService;
import com.schoolerp.modules.operations.service.OperationsService;
import com.schoolerp.modules.staff.service.StaffService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SchoolErpApplicationTests {

    @Autowired
    private AuthService authService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private OperationsService operationsService;

    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private com.schoolerp.modules.examinations.service.GradebookService gradebookService;

    @Autowired
    private com.schoolerp.modules.system.service.TenantModuleLicenseService licenseService;

    @Autowired
    private com.schoolerp.modules.students.repository.StudentRepository studentRepository;

    @Autowired
    private com.schoolerp.modules.operations.repository.BookIssueRecordRepository bookIssueRecordRepository;

    @Autowired
    private com.schoolerp.modules.staff.service.PayrollService payrollService;

    @Autowired
    private com.schoolerp.modules.operations.service.GatePassService gatePassService;

    @Autowired
    private com.schoolerp.modules.students.service.StudentClearanceService clearanceService;

    @Autowired
    private com.schoolerp.modules.students.service.InfirmaryService infirmaryService;

    @Autowired
    private com.schoolerp.modules.academics.service.HomeworkService homeworkService;

    @Autowired
    private com.schoolerp.modules.mobile.service.MobileParentService mobileParentService;

    @Autowired
    private com.schoolerp.modules.academics.service.SubstitutionService substitutionService;

    @Autowired
    private com.schoolerp.modules.staff.repository.EmployeeRepository employeeRepository;

    @Autowired
    private com.schoolerp.modules.finance.service.StudentWalletService studentWalletService;

    @Autowired
    private com.schoolerp.modules.students.service.BehaviorService behaviorService;

    @Autowired
    private com.schoolerp.modules.communication.service.PtmService ptmService;

    @Autowired
    private com.schoolerp.modules.operations.service.LibraryService libraryService;

    @Autowired
    private com.schoolerp.modules.staff.service.StaffLeaveService staffLeaveService;

    @Autowired
    private com.schoolerp.modules.communication.service.CircularService circularService;

    @Autowired
    private com.schoolerp.modules.attendance.service.AttendanceComplianceService complianceService;

    @Autowired
    private com.schoolerp.modules.system.service.TenantFeatureFlagService featureFlagService;

    @Autowired
    private com.schoolerp.modules.academics.service.AcademicYearRolloverService rolloverService;

    @Autowired
    private com.schoolerp.modules.examinations.service.ExamSeatingService seatingService;

    @Autowired
    private com.schoolerp.modules.operations.service.EmergencyIncidentService incidentService;

    @Autowired
    private com.schoolerp.modules.system.service.SystemBackupService backupService;

    @Autowired
    private com.schoolerp.modules.finance.repository.FeeInvoiceRepository feeInvoiceRepository;

    @Autowired
    private com.schoolerp.modules.students.service.IndianComplianceService indianComplianceService;

    @Autowired
    private com.schoolerp.modules.admissions.service.RteQuotaService rteQuotaService;

    @Autowired
    private com.schoolerp.modules.examinations.service.CbseLocService cbseLocService;

    @Autowired
    private com.schoolerp.modules.finance.service.IndianBankingService indianBankingService;

    @Autowired
    private com.schoolerp.modules.staff.service.IndianPayrollComplianceService indianPayrollComplianceService;

    @Autowired
    private com.schoolerp.modules.academics.service.MorningAssemblyService morningAssemblyService;

    @Autowired
    private com.schoolerp.modules.institution.service.InstitutionCalendarService institutionCalendarService;

    @Autowired
    private com.schoolerp.modules.identity.service.SessionSecurityService sessionSecurityService;

    @Autowired
    private com.schoolerp.modules.system.service.BulkImportService bulkImportService;

    @Autowired
    private com.schoolerp.modules.system.service.EnterpriseSchedulerService enterpriseSchedulerService;

    @Autowired
    private com.schoolerp.modules.academics.service.StudentRiskRadarService studentRiskRadarService;

    @Autowired
    private com.schoolerp.modules.examinations.service.HolisticProgressService holisticProgressService;

    @Autowired
    private com.schoolerp.modules.operations.service.ComplianceSentinelService complianceSentinelService;

    @Autowired
    private com.schoolerp.modules.academics.service.SyllabusEquityService syllabusEquityService;

    @Autowired
    private com.schoolerp.modules.staff.service.WorkloadEquityService workloadEquityService;

    @Autowired
    private com.schoolerp.modules.students.service.StudentExportService studentExportService;

    @Autowired
    private com.schoolerp.modules.academics.service.SchoolEventService schoolEventService;

    @Autowired
    private com.schoolerp.modules.attendance.service.StudentLeaveService studentLeaveService;

    @Autowired
    private com.schoolerp.modules.examinations.service.ExamQuestionBankService questionBankService;

    @Autowired
    private com.schoolerp.modules.operations.service.LabInventoryService labInventoryService;

    @Autowired
    private com.schoolerp.modules.operations.service.LostAndFoundService lostAndFoundService;

    @Autowired
    private com.schoolerp.modules.students.service.AlumniPlacementService alumniPlacementService;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void contextLoads() {
        assertNotNull(authService, "AuthService should be injected");
    }

    @Test
    void testAdminLogin() {
        LoginRequest request = LoginRequest.builder()
                .email("admin@apexacademy.edu.in")
                .password("Admin123!")
                .build();

        AuthResponse response = authService.login(request);
        assertNotNull(response);
        assertNotNull(response.getToken(), "JWT token should be generated");
        assertEquals("Dr. Rajesh Sharma", response.getUser().getFullName());
        assertTrue(response.getUser().getRoles().contains("SUPER_ADMIN"));
        assertEquals("tenant_apex_mumbai_01", response.getTenant().getTenantIdentifier());
    }

    @Test
    void testTeacherLogin() {
        LoginRequest request = LoginRequest.builder()
                .email("teacher@apexacademy.edu.in")
                .password("Teacher123!")
                .build();

        AuthResponse response = authService.login(request);
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("Dr. Vikram Seth", response.getUser().getFullName());
        assertTrue(response.getUser().getRoles().contains("TEACHER"));
    }

    @Test
    void testStaffModule() {
        var employees = staffService.getEmployees();
        assertNotNull(employees);
        assertTrue(employees.size() >= 3, "Should have seeded at least 3 staff employees");
        assertTrue(employees.stream().anyMatch(e -> e.getFullName().equals("Dr. Vikram Seth")));

        var leaves = staffService.getLeaveRequests();
        assertNotNull(leaves);
        assertTrue(leaves.size() >= 1, "Should have seeded at least 1 leave request");
    }

    @Test
    void testOperationsModule() {
        var vehicles = operationsService.getVehicles();
        assertNotNull(vehicles);
        assertTrue(vehicles.size() >= 2, "Should have seeded at least 2 transport vehicles");

        var routes = operationsService.getRoutes();
        assertNotNull(routes);
        assertTrue(routes.size() >= 1, "Should have seeded at least 1 transport route");

        var books = operationsService.getBooks();
        assertNotNull(books);
        assertTrue(books.size() >= 3, "Should have seeded at least 3 library books");

        var issues = operationsService.getIssueRecords();
        assertNotNull(issues);
        assertTrue(issues.size() >= 1, "Should have seeded at least 1 active book issue");
    }

    @Test
    void testCommunicationModule() {
        var circulars = communicationService.getCirculars();
        assertNotNull(circulars);
        assertTrue(circulars.size() >= 2, "Should have seeded at least 2 circulars");
        assertTrue(circulars.stream().anyMatch(c -> c.getTitle().contains("Science Olympiad")));

        var dispatches = communicationService.getDispatchLogs();
        assertNotNull(dispatches);
        assertTrue(dispatches.size() >= 2, "Should have seeded at least 2 dispatch logs");
    }

    @Test
    void testIdempotentWebhookProcessing() {
        String eventId = "evt_razorpay_test_0099";
        var firstResult = financeService.processWebhook(
                "RAZORPAY",
                eventId,
                "payment.captured",
                "sig_valid_test_hash",
                "{}",
                "INV-2026-0843",
                new BigDecimal("15000.00")
        );

        assertNotNull(firstResult);
        assertEquals("SUCCESS", firstResult.getStatus());
        assertFalse(firstResult.isDuplicate(), "First webhook processing should not be duplicate");

        // Replay same webhook event to test idempotency and replay-attack defense
        var replayResult = financeService.processWebhook(
                "RAZORPAY",
                eventId,
                "payment.captured",
                "sig_valid_test_hash",
                "{}",
                "INV-2026-0843",
                new BigDecimal("15000.00")
        );

        assertNotNull(replayResult);
        assertEquals("DUPLICATE_IGNORED", replayResult.getStatus());
        assertTrue(replayResult.isDuplicate(), "Replay webhook should be marked duplicate and safely ignored");
    }

    @Test
    void testFeeStructuresAndConcessions() {
        var structures = financeService.getFeeStructures();
        assertNotNull(structures);
        assertTrue(structures.size() >= 2, "Should have seeded at least 2 fee structures");
        assertTrue(structures.stream().anyMatch(s -> s.getGradeLevel().equals("Class 10")));

        var concessions = financeService.getConcessions();
        assertNotNull(concessions);
        assertTrue(concessions.size() >= 1, "Should have seeded at least 1 sibling concession");
        var rheaConcession = concessions.stream()
                .filter(c -> c.getStudentName().equals("Rhea Sharma"))
                .findFirst()
                .orElse(null);
        assertNotNull(rheaConcession);
        assertEquals("SIBLING_DISCOUNT", rheaConcession.getConcessionType());
        assertEquals(0, new BigDecimal("15.00").compareTo(rheaConcession.getPercentageDiscount()));

        // Calculate net fee for Rhea on gross tuition of ₹26,000 (15% discount = ₹3,900 => Net ₹22,100)
        var netResult = financeService.calculateNetFee(rheaConcession.getStudentId(), new BigDecimal("26000.00"));
        assertNotNull(netResult);
        assertEquals(0, new BigDecimal("3900.00").compareTo(netResult.getDiscountAmount()));
        assertEquals(0, new BigDecimal("22100.00").compareTo(netResult.getNetFee()));
    }

    @Test
    void testChequeClearanceAndBounceLifecycle() {
        var cheques = financeService.getChequeRecords();
        assertNotNull(cheques);
        assertTrue(cheques.size() >= 2, "Should have seeded at least 2 cheque records");

        // Find the RECEIVED cheque and test status change to BOUNCED
        var receivedCheque = cheques.stream()
                .filter(c -> "RECEIVED".equalsIgnoreCase(c.getStatus()))
                .findFirst()
                .orElse(null);
        assertNotNull(receivedCheque);

        var bounced = financeService.updateChequeStatus(receivedCheque.getId(), "BOUNCED", "Insufficient funds (Return memo #9901)");
        assertEquals("BOUNCED", bounced.getStatus());
        assertEquals(0, new BigDecimal("500.00").compareTo(bounced.getBouncePenalty()));
    }

    @Test
    void testReportCardCompilationAndQrVerification() {
        var students = studentRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(students.isEmpty(), "Students must be seeded");
        var aarav = students.stream().filter(s -> s.getFullName().equals("Aarav Sharma")).findFirst().orElse(null);
        assertNotNull(aarav);

        // Compile CBSE 70/20/10 weighted report card
        var reportCard = gradebookService.compileReportCard(aarav.getId(), "Term 1 Mid-Term");
        assertNotNull(reportCard);
        assertNotNull(reportCard.getVerificationHash(), "Cryptographic verification hash must be present");
        assertEquals("Aarav Sharma", reportCard.getStudentName());
        assertTrue(reportCard.getPercentage().compareTo(BigDecimal.valueOf(80.0)) >= 0);
        assertTrue(reportCard.getCgpa().compareTo(BigDecimal.valueOf(8.0)) >= 0);
        assertEquals(1, reportCard.getClassRank(), "Aarav should rank #1 in Section A");
        assertEquals("DIGITALLY_SIGNED_SHA256", reportCard.getPrincipalSignatureStatus());

        // Verify the report card using public hash
        var verification = gradebookService.verifyReportCard(reportCard.getVerificationHash());
        assertNotNull(verification);
        assertEquals("AUTHENTIC_VALID", verification.getVerificationStatus());
        assertEquals(aarav.getAdmissionNumber(), verification.getAdmissionNumber());
    }

    @Test
    void testMobileDeviceTokenAndPushNotification() {
        var devices = communicationService.getActiveDevices();
        assertNotNull(devices);
        assertTrue(devices.size() >= 1, "Should have seeded at least 1 parent mobile device token");

        // Send high-priority mobile push
        var pushLog = communicationService.sendPushNotification(
                devices.get(0).getUserId(),
                "Rajesh Sharma",
                "Bus Approaching Stop",
                "Tata Starbus #MH-02-CB-4012 is 3 minutes away from Lokhandwala Circle.",
                "BUS_TRACKING"
        );
        assertNotNull(pushLog);
        assertEquals("PUSH_NOTIFICATION_FCM", pushLog.getChannel());
        assertTrue(pushLog.getProviderMessageId().startsWith("fcm_msg_"));
    }

    @Test
    void testTenantDynamicModuleLicensing() {
        var modules = licenseService.getTenantModules();
        assertNotNull(modules);
        assertEquals(9, modules.size(), "Should have seeded 9 enterprise modules");

        // Toggle module license state
        var toggled = licenseService.toggleModule("FLEET_GPS", false, "SUPER_ADMIN");
        assertFalse(toggled.isEnabled(), "Module should be toggled to false");

        // Restore to true
        var restored = licenseService.toggleModule("FLEET_GPS", true, "SUPER_ADMIN");
        assertTrue(restored.isEnabled(), "Module should be restored to true");
    }

    @Test
    void testBiometricPunchAndStaffPayroll() {
        var employees = employeeRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(employees.isEmpty());
        var vikram = employees.stream().filter(e -> e.getFullName().contains("Vikram")).findFirst().orElse(null);
        assertNotNull(vikram);

        // Biometric turnstile punch
        var punch = payrollService.recordPunch(vikram.getId(), "IN", "FACIAL_RECOGNITION", "10.0.8.21");
        assertNotNull(punch);
        assertEquals("Dr. Vikram Seth", punch.getEmployeeName());

        // Monthly payroll generation & statutory deduction math
        var payrollRecords = payrollService.generateMonthlyPayroll("2026-11");
        assertFalse(payrollRecords.isEmpty());
        var vikramPayroll = payrollRecords.stream().filter(p -> p.getEmployeeId().equals(vikram.getId())).findFirst().orElse(null);
        assertNotNull(vikramPayroll);

        // Verify PF (12% of Basic) and statutory professional tax
        assertTrue(vikramPayroll.getProvidentFund().compareTo(BigDecimal.ZERO) > 0);
        assertEquals(0, new BigDecimal("200.00").compareTo(vikramPayroll.getProfessionalTax()));
        assertTrue(vikramPayroll.getNetSalary().compareTo(BigDecimal.ZERO) > 0);
        assertEquals("DISBURSED", vikramPayroll.getPaymentStatus());
    }

    @Test
    void testTeacherSubstitutionAlgorithm() {
        var employees = employeeRepository.findAllByTenantId("tenant_apex_mumbai_01");
        var vikram = employees.stream().filter(e -> e.getFullName().contains("Vikram")).findFirst().orElse(null);
        assertNotNull(vikram);

        // Monday recommendations
        var nextMonday = java.time.LocalDate.now().with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.MONDAY));
        var recommendations = substitutionService.getRecommendations(nextMonday, vikram.getId());
        assertNotNull(recommendations);

        // Assign substitute for Period 1
        var substitution = substitutionService.assignSubstitution(
                nextMonday, 1, "08:30 – 09:15", "Class 10", "Section A", "Homeroom & Roll Call", "Room 301",
                vikram.getId(), vikram.getFullName(),
                employees.get(1).getId(), employees.get(1).getFullName(),
                "Principal Office"
        );
        assertNotNull(substitution);
        assertEquals("ASSIGNED", substitution.getStatus());
        assertEquals("Class 10", substitution.getClassName());
    }

    @Test
    void testFamilyBillingCheckoutAndTrialBalance() {
        // Unified Family Billing Cart for Rajesh Sharma (Father of Aarav & Rhea)
        var familyBilling = financeService.getFamilyBillingSummary("rajesh.sharma@example.com");
        assertNotNull(familyBilling);
        assertEquals("Rajesh Sharma", familyBilling.getGuardianName());
        assertTrue(familyBilling.getChildrenLines().size() >= 2, "Should group Aarav and Rhea under single family billing");
        assertTrue(familyBilling.getTotalSiblingConcessions().compareTo(BigDecimal.ZERO) > 0, "15% sibling concession must be reflected");
        assertNotNull(familyBilling.getUnifiedOrderId());

        // Double-Entry General Ledger Trial Balance
        var trialBalance = financeService.generateTrialBalance();
        assertNotNull(trialBalance);
        assertFalse(trialBalance.getAccounts().isEmpty());
        assertTrue(trialBalance.getTotalDebitSum().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(trialBalance.getTotalCreditSum().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(trialBalance.isBalanced(), "Total debits must strictly equal total credits across all ledger accounts");
    }

    @Test
    void testSamsaraGeofencingTelematics() {
        // Telemetry ping right near Lokhandwala Circle Stop (Lat 19.1415, Lng 72.8312)
        var result = operationsService.processGpsPingWithGeofencing(
                "MH-02-CB-4012",
                19.1416, 72.8313,
                35.0, 140.0
        );
        assertNotNull(result);
        assertEquals("MH-02-CB-4012", result.getVehicleRegistration());
        assertTrue(result.isGeofenceTriggered(), "Geofence proximity alert must trigger when bus is within 800m of stop");
        assertTrue(result.getDistanceToNearestStopMeters() < 100, "Distance should be under 100 meters");
        assertFalse(result.isOverspeeding(), "35 km/h is within 50 km/h threshold");
    }

    @Test
    void testGatePassIssuanceAndTurnstileScan() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var pass = gatePassService.issueGatePass(
                student.getId(),
                student.getFullName(),
                student.getClassName(),
                student.getGuardianName(),
                student.getGuardianPhone(),
                "FATHER",
                "EARLY_DEPARTURE",
                "Principal Office"
        );
        assertNotNull(pass);
        assertNotNull(pass.getQrToken());
        assertEquals("APPROVED", pass.getPassStatus());

        // Test security turnstile scan
        var scanned = gatePassService.verifyAndScanGatePass(pass.getQrToken(), "Officer Deshmukh - Gate 1");
        assertNotNull(scanned);
        assertEquals("SCANNED_EXIT", scanned.getPassStatus());
        assertNotNull(scanned.getExitTimestamp());
    }

    @Test
    void testStudentClearanceAndTransferCertificate() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").stream()
                .filter(s -> s.getFullName().contains("Aarav"))
                .findFirst()
                .orElseThrow();

        // 1. Initial audit should detect unreturned library book
        var initialClearance = clearanceService.initiateOrRefreshClearance(student.getId());
        assertNotNull(initialClearance);
        assertFalse(initialClearance.isLibraryCleared(), "Must catch that Aarav has an unreturned library book");
        assertEquals("BLOCKED", initialClearance.getClearanceStatus());

        // 2. Return book to librarian
        var books = bookIssueRecordRepository.findByTenantIdAndStudentId("tenant_apex_mumbai_01", student.getId());
        for (var b : books) {
            b.setStatus("RETURNED");
            bookIssueRecordRepository.save(b);
        }

        // 3. Re-evaluate clearance -> All departments cleared
        var clearance = clearanceService.initiateOrRefreshClearance(student.getId());
        assertNotNull(clearance);
        assertTrue(clearance.isLibraryCleared(), "Library must now be cleared after return");
        assertTrue(clearance.isBursarCleared(), "Aarav Sharma has full fee clearance");
        assertEquals("CLEARED", clearance.getClearanceStatus());

        var tc = clearanceService.generateTransferCertificate(student.getId(), "PARENT_RELOCATION", "EXEMPLARY");
        assertNotNull(tc);
        assertNotNull(tc.getTcNumber());
        assertNotNull(tc.getVerificationHash());
        assertEquals("Transferred", studentRepository.findById(student.getId()).orElseThrow().getStatus());

        // Verify public QR hash
        var verifiedTc = clearanceService.verifyCertificate(tc.getVerificationHash());
        assertNotNull(verifiedTc);
        assertEquals(tc.getTcNumber(), verifiedTc.getTcNumber());
    }

    @Test
    void testInfirmaryClinicalVisitAndAlert() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var visit = infirmaryService.recordVisit(
                student.getId(),
                "HIGH_FEVER",
                102.1,
                "118/76",
                86,
                "Paracetamol 500mg, ice compress sponge",
                60,
                "SENT_HOME_WITH_PARENT",
                "Sister Mary D'Souza, RN"
        );
        assertNotNull(visit);
        assertTrue(visit.isParentNotified(), "High temperature >= 100 F must trigger parent notification");
        assertEquals("WHATSAPP_HEALTH_ALERT", visit.getParentAlertChannel());
    }

    @Test
    void testHomeworkAssignmentAndGradingLifecycle() {
        var hw = homeworkService.createAssignment(
                "Class 10",
                "Section A",
                "Physics",
                "Ray Optics & Refraction Index Lab",
                "Complete Ray Diagram 5.1 and compute focal lengths.",
                "Dr. Vikram Seth",
                java.time.LocalDate.now().plusDays(3),
                25.0,
                "https://cdn.apexacademy.edu.in/assignments/physics-optics.pdf"
        );
        assertNotNull(hw);
        assertEquals("Physics", hw.getSubject());

        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var sub = homeworkService.submitHomework(hw.getId(), student.getId(), "https://cdn.apexacademy.edu.in/submissions/optics_lab.pdf");
        assertNotNull(sub);
        assertEquals("SUBMITTED", sub.getStatus());

        var graded = homeworkService.gradeSubmission(sub.getId(), 24.5, "Excellent ray diagram precision", "Dr. Vikram Seth");
        assertNotNull(graded);
        assertEquals("GRADED", graded.getStatus());
        assertEquals(24.5, graded.getMarksAwarded());
    }

    @Test
    void testMobileParentHomeDashboardAggregation() {
        var dashboard = mobileParentService.getParentDashboard("rajesh.sharma@example.com");
        assertNotNull(dashboard);
        assertEquals("Rajesh Sharma", dashboard.getGuardianName());
        assertFalse(dashboard.getChildren().isEmpty(), "Must aggregate children linked to guardian");

        var firstChild = dashboard.getChildren().get(0);
        assertNotNull(firstChild.getFullName());
        assertNotNull(firstChild.getPendingFeeBalance());
        assertNotNull(firstChild.getGatePassStatus());
    }

    @Test
    void testStudentRfidWalletTopupAndDailyLimit() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var wallet = studentWalletService.registerOrGetWallet(student.getId(), "RFID-TEST-9921", new BigDecimal("150.00"));
        assertNotNull(wallet);

        // Top-up
        var toppedUp = studentWalletService.topupWallet(student.getId(), new BigDecimal("500.00"), "UPI");
        assertTrue(toppedUp.getBalance().compareTo(new BigDecimal("500.00")) >= 0);

        // Debit within limit (INR 80 <= INR 150 daily limit)
        var tx = studentWalletService.transactCanteen(wallet.getRfidCardNumber(), new BigDecimal("80.00"),
                "Fruit Salad + Sandwich", "POS-CANTEEN-01", "Cashier A");
        assertNotNull(tx);
        assertEquals("DEBIT_CANTEEN", tx.getTransactionType());

        // Attempt debit exceeding daily limit (Attempting INR 100 more, but limit is 150 and already spent 80)
        assertThrows(IllegalStateException.class, () -> {
            studentWalletService.transactCanteen(wallet.getRfidCardNumber(), new BigDecimal("100.00"),
                    "Extra Snack", "POS-CANTEEN-01", "Cashier A");
        }, "Must decline transaction exceeding daily spending cap");
    }

    @Test
    void testSchoolHouseMeritDemeritLeaderboard() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);

        var merit = behaviorService.recordBehaviorIncident(student.getId(), "MERIT", "SPORTSMANSHIP", 10,
                "Inter-school relay race silver medal", "Sports Coach");
        assertNotNull(merit);
        assertEquals("MERIT", merit.getIncidentType());
        assertEquals(10, merit.getPoints());

        var demerit = behaviorService.recordBehaviorIncident(student.getId(), "DEMERIT", "TARDINESS", 5,
                "Late reporting to assembly", "Discipline Head");
        assertNotNull(demerit);
        assertEquals("DEMERIT", demerit.getIncidentType());
        assertEquals(-5, demerit.getPoints());

        var leaderboard = behaviorService.getHouseLeaderboard();
        assertFalse(leaderboard.isEmpty(), "House leaderboard must return houses sorted by points");
        assertTrue(leaderboard.get(0).getTotalPoints() >= leaderboard.get(leaderboard.size() - 1).getTotalPoints());
    }

    @Test
    void testPtmConferenceSlotBooking() {
        var sessions = ptmService.getActiveSessions();
        assertFalse(sessions.isEmpty(), "Active PTM session must exist from seed");
        var session = sessions.get(0);

        var availableSlots = ptmService.getAvailableSlots(session.getId(), null);
        assertFalse(availableSlots.isEmpty(), "Unbooked conference slots must be available");
        var slotToBook = availableSlots.get(0);

        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var bookedSlot = ptmService.bookSlot(slotToBook.getId(), student.getId(), "Mrs. Sunita Sharma", "+91 98201 12345");
        assertNotNull(bookedSlot);
        assertTrue(bookedSlot.isBooked());
        assertEquals(student.getId(), bookedSlot.getStudentId());

        // Second booking attempt on same slot must fail with collision exception
        assertThrows(IllegalStateException.class, () -> {
            ptmService.bookSlot(slotToBook.getId(), student.getId(), "Another Parent", "+91 98201 99999");
        }, "Double booking same PTM slot must be strictly prevented");
    }

    @Test
    void testLibraryCirculationAndOverdueFine() {
        var book = libraryService.registerBook(
                "Concepts of Modern Physics", "Arthur Beiser", "978-0070151550",
                "Science", 10, "Shelf SCI-9", "TAG-BEISER-01"
        );
        assertNotNull(book);
        assertEquals(10, book.getAvailableCopies());

        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var issueRecord = libraryService.issueBook(book.getId(), student.getId(), 14);
        assertNotNull(issueRecord);
        assertEquals("ISSUED", issueRecord.getStatus());

        var updatedBook = libraryService.searchBooks(book.getIsbn()).get(0);
        assertEquals(9, updatedBook.getAvailableCopies(), "Available copies must decrement on checkout");

        // Return book
        var returnRecord = libraryService.returnBook(issueRecord.getId());
        assertNotNull(returnRecord);
        assertEquals("RETURNED", returnRecord.getStatus());
        assertNotNull(returnRecord.getReturnedDate());

        var restoredBook = libraryService.searchBooks(book.getIsbn()).get(0);
        assertEquals(10, restoredBook.getAvailableCopies(), "Available copies must restore on return");
    }

    @Test
    void testStaffLeaveApprovalAndAutoSubstitutionTrigger() {
        var teachers = employeeRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(teachers.isEmpty());
        var teacher = teachers.get(0);

        var leaveReq = staffLeaveService.applyLeave(
                teacher.getId(), "CASUAL",
                java.time.LocalDate.now().plusDays(5),
                java.time.LocalDate.now().plusDays(5),
                "CBSE Pedagogy Workshop"
        );
        assertNotNull(leaveReq);
        assertEquals("PENDING", leaveReq.getStatus());

        // Review & Approve
        var reviewResult = staffLeaveService.reviewLeave(
                leaveReq.getId(), true, "Approved by Principal", "principal@apexacademy.edu.in"
        );
        assertNotNull(reviewResult);
        assertEquals("APPROVED", reviewResult.leaveRequest().getStatus());
        assertNotNull(reviewResult.generatedSubstitutions());
    }

    @Test
    void testSchoolCircularPublicationAndDigitalParentAcknowledgment() {
        var circular = circularService.publishCircular(
                "Winter Uniform Guidelines AY 2026-27",
                "All students must adhere to the navy blazer and formal tie policy starting Dec 1.",
                "ALL", null, "IMPORTANT", "s3://apex/circulars/winter-uniform.pdf", "Executive Principal"
        );
        assertNotNull(circular);
        assertEquals("PUBLISHED", circular.getStatus());

        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var ack = circularService.acknowledgeCircular(
                circular.getId(), "parent.rajesh@example.com", "Rajesh Sharma",
                student.getId(), student.getFullName(), "192.168.1.50", "Understood and agreed"
        );
        assertNotNull(ack);
        assertEquals("parent.rajesh@example.com", ack.getGuardianEmail());

        var acks = circularService.getAcknowledgments(circular.getId());
        assertEquals(1, acks.size());
    }

    @Test
    void testDailyLateFeeSurchargeBatchProcessor() {
        var asOfDate = java.time.LocalDate.now().plusDays(30);
        var surcharges = financeService.processDailyLateFees(asOfDate, new BigDecimal("50.00"));
        assertNotNull(surcharges);

        var trialBalance = financeService.generateTrialBalance();
        assertNotNull(trialBalance);
        assertTrue(trialBalance.isBalanced(), "Double-entry general ledger must balance after late fee assessment");
    }

    @Test
    void testCbseAttendanceComplianceWatchdog() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var report = complianceService.evaluateStudentCompliance(student.getId());
        assertNotNull(report);
        assertEquals(student.getId(), report.getStudentId());
        assertTrue(report.getAttendancePercentage() >= 0.0 && report.getAttendancePercentage() <= 100.0);

        var defaulters = complianceService.getDefaulters(null, 75.0);
        assertNotNull(defaulters);
    }

    @Test
    void testGranularFeatureFlagEntitlementEngine() {
        var flags = featureFlagService.getAllFeatures();
        assertNotNull(flags);
        assertFalse(flags.isEmpty());

        // Toggle a flag
        var toggled = featureFlagService.toggleFeature("transport.live_gps_telemetry", false, "PREMIUM");
        assertNotNull(toggled);
        assertFalse(toggled.isEnabled());
        assertFalse(featureFlagService.isFeatureEnabled("transport.live_gps_telemetry"));

        // Re-enable
        var reEnabled = featureFlagService.toggleFeature("transport.live_gps_telemetry", true, "PREMIUM");
        assertTrue(reEnabled.isEnabled());
        assertTrue(featureFlagService.isFeatureEnabled("transport.live_gps_telemetry"));
    }

    @Test
    void testAcademicYearBatchPromotionRolloverEngine() {
        var rollover = rolloverService.executeBatchRollover(
                "2025-2026",
                "2026-2027",
                "Executive Principal"
        );
        assertNotNull(rollover);
        assertEquals("2025-2026", rollover.getFromAcademicYear());
        assertEquals("2026-2027", rollover.getToAcademicYear());
        assertTrue(rollover.getTotalStudentsEvaluated() >= 0);

        var history = rolloverService.getRolloverHistory();
        assertNotNull(history);
        assertFalse(history.isEmpty());
    }

    @Test
    void testExamHallSeatingMatrixAndInvigilationDuty() {
        var halls = seatingService.getHalls();
        assertNotNull(halls);
        assertFalse(halls.isEmpty());
        var hall = halls.get(0);

        var seatingPlan = seatingService.generateAntiCheatingSeatingPlan(
                hall.getId(),
                java.time.LocalDate.now().plusDays(2),
                "MORNING",
                "Mathematics & Physics"
        );
        assertNotNull(seatingPlan);
        assertFalse(seatingPlan.isEmpty());

        var teachers = employeeRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(teachers.isEmpty());

        var duty = seatingService.assignInvigilator(
                hall.getId(),
                java.time.LocalDate.now().plusDays(2),
                "09:00 AM – 12:00 PM",
                "Mathematics & Physics",
                teachers.get(0).getId()
        );
        assertNotNull(duty);
        assertEquals("ASSIGNED", duty.getStatus());
    }

    @Test
    void testCampusEmergencySosLockdownLifecycle() {
        var incident = incidentService.triggerEmergencySos(
                "SECURITY_LOCKDOWN",
                "CRITICAL",
                "Perimeter North Gate",
                "Unauthorized security breach detected near perimeter.",
                "Chief Security Officer"
        );
        assertNotNull(incident);
        assertEquals("ACTIVE_LOCKDOWN", incident.getStatus());
        assertEquals("CRITICAL", incident.getSeverity());

        var activeIncidents = incidentService.getActiveIncidents();
        assertFalse(activeIncidents.isEmpty());

        var resolved = incidentService.resolveIncident(
                incident.getId(),
                "Perimeter cleared and secured. All perimeter sensors recalibrated."
        );
        assertNotNull(resolved);
        assertEquals("RESOLVED", resolved.getStatus());
        assertNotNull(resolved.getResolvedAt());
    }

    @Test
    void testPodBackupSnapshotAndHealthTelemetry() {
        var backup = backupService.createPodBackup("FULL_POD_SNAPSHOT", "TEST_RUNNER");
        assertNotNull(backup);
        assertEquals("COMPLETED", backup.getStatus());
        assertNotNull(backup.getSha256Checksum());
        assertEquals(64, backup.getSha256Checksum().length(), "SHA-256 must be 64 hexadecimal characters");
        assertTrue(backup.getFileSizeBytes() > 0);

        var telemetry = backupService.getPodHealthTelemetry();
        assertNotNull(telemetry);
        assertEquals("UP", telemetry.getHealthStatus());
        assertEquals("DEDICATED_DATABASE_POD", telemetry.getPodIsolationStatus());
        assertTrue(telemetry.getHeapTotalMb() > 0);
    }

    @Test
    void testUdisePlusAndStudentCertificatesDesk() {
        var udiseExport = indianComplianceService.exportUdiseData();
        assertNotNull(udiseExport);
        assertEquals("2026-2027", udiseExport.getAcademicYear());
        assertTrue(udiseExport.getTotalEnrolledStudents() > 0);
        assertTrue(udiseExport.getApaarRegisteredStudents() > 0);

        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var cert = indianComplianceService.generateCertificate(
                student.getId(),
                "BONAFIDE",
                "Regional Passport Office Verification",
                "Principal Office"
        );
        assertNotNull(cert);
        assertEquals("BONAFIDE", cert.getCertificateType());
        assertNotNull(cert.getQrVerificationToken());

        var verify = indianComplianceService.verifyCertificate(cert.getQrVerificationToken());
        assertNotNull(verify);
        assertTrue(verify.isValid());
        assertEquals("AUTHENTIC_DIGITALLY_VERIFIED", verify.getVerificationStatus());
    }

    @Test
    void testRteAct25PercentQuotaAndGovernmentReimbursementClaims() {
        var records = rteQuotaService.getRteRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());

        var claim = rteQuotaService.generateAnnualReimbursementClaim("2026-2027");
        assertNotNull(claim);
        assertEquals("2026-2027", claim.getAcademicYear());
        assertTrue(claim.getTotalRteEnrolledStudents() > 0);
        assertTrue(claim.getTotalReimbursementClaimAmount().compareTo(java.math.BigDecimal.ZERO) > 0);
    }

    @Test
    void testCbseListCandidatesLocExaminationRegistration() {
        var locExport = cbseLocService.generateLocExport("Class 10");
        assertNotNull(locExport);
        assertEquals("Central Board of Secondary Education (CBSE)", locExport.getBoardName());
        assertTrue(locExport.getTotalRegisteredCandidates() >= 2);
        assertTrue(locExport.getStandardMathCandidateCount() > 0);
        assertTrue(locExport.getBasicMathCandidateCount() > 0);
        assertTrue(locExport.getTotalRegistrationFeePayable().compareTo(java.math.BigDecimal.ZERO) > 0);
    }

    @Test
    void testNpciDynamicUpiQrAndCashierDenominationDayEndScroll() {
        var invoices = feeInvoiceRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(invoices.isEmpty());

        var upiPayload = indianBankingService.generateUpiDynamicQr(invoices.get(0).getId());
        assertNotNull(upiPayload);
        assertTrue(upiPayload.getUpiIntentUri().startsWith("upi://pay?pa=apexacademy.fees@sbi"));
        assertEquals("8211", upiPayload.getMerchantCategoryCode());
        assertNotNull(upiPayload.getQrBase64Payload());

        var scroll = indianBankingService.closeCashierDayScroll(
                "Senior Cashier",
                java.time.LocalDate.now(),
                50, 40, 100, 50, 20, 10,
                new BigDecimal("46000.00"),
                "Cash counter day-end balance complete"
        );
        assertNotNull(scroll);
        assertEquals(new BigDecimal("46000"), scroll.getTotalCashCalculated());
        assertEquals("BALANCED", scroll.getStatus());
    }

    @Test
    void testEpfoEcrAndEsicStatutoryPayrollExports() {
        var ecr = indianPayrollComplianceService.generateEpfoEcrExport("2026-11");
        assertNotNull(ecr);
        assertEquals("MH/BAN/0048291/000", ecr.getEstablishmentId());
        assertTrue(ecr.getTotalMembers() > 0);
        assertTrue(ecr.getTotalEeShare().compareTo(java.math.BigDecimal.ZERO) > 0);
        assertTrue(ecr.getTotalEpsShare().compareTo(java.math.BigDecimal.ZERO) > 0);

        var esic = indianPayrollComplianceService.generateEsicReturnExport("2026-11");
        assertNotNull(esic);
        assertTrue(esic.getTotalInsuredPersons() > 0);
        assertTrue(esic.getTotalContributionPayable().compareTo(java.math.BigDecimal.ZERO) > 0);
    }

    @Test
    void testMorningAssemblyHouseDutyAndQuadrangleRoster() {
        var roster = morningAssemblyService.getActiveWeekRoster();
        assertNotNull(roster);
        assertEquals("Tagore House (Blue)", roster.getHouseOnDuty());
        assertNotNull(roster.getThoughtForTheDay());
        assertNotNull(roster.getTeacherInChargeName());

        var allRosters = morningAssemblyService.getAllRosters();
        assertFalse(allRosters.isEmpty());
    }

    @Test
    void testInstitutionHolidayCalendarAndWorkingDayEvaluation() {
        var diwaliDate = java.time.LocalDate.of(2026, 11, 18);
        var holiday = institutionCalendarService.declareEvent(
                "Diwali Vacation Break",
                "VACATION",
                diwaliDate,
                diwaliDate.plusDays(2),
                false,
                "CLOSED",
                "Festival closure across all branches"
        );
        assertNotNull(holiday);
        assertFalse(holiday.isWorkingDay());

        var evalHoliday = institutionCalendarService.evaluateWorkingDay(diwaliDate);
        assertFalse(evalHoliday.isWorkingDay());
        assertFalse(evalHoliday.isAttendanceRollCallRequired());
    }

    @Test
    void testRefreshTokenRotationAndEmergencySessionRevoke() {
        var loginReq = new LoginRequest();
        loginReq.setEmail("admin@apexacademy.edu.in");
        loginReq.setPassword("Admin123!");
        AuthResponse loginRes = authService.login(loginReq);
        assertNotNull(loginRes.getRefreshToken());

        AuthResponse rotated = sessionSecurityService.rotateRefreshToken(loginRes.getRefreshToken());
        assertNotNull(rotated.getToken());
        assertNotNull(rotated.getRefreshToken());
        assertNotEquals(loginRes.getRefreshToken(), rotated.getRefreshToken(), "Refresh token must rotate cleanly");

        // Replaying already-rotated token must be rejected
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class,
                () -> sessionSecurityService.rotateRefreshToken(loginRes.getRefreshToken()));

        int revokedCount = sessionSecurityService.revokeAllSessionsForUser("admin@apexacademy.edu.in", "TEST_LOCKDOWN");
        assertTrue(revokedCount >= 1);
    }

    @Test
    void testBulkStudentCsvOnboardingWithRowErrorIsolation() {
        String csv = "admissionNumber,fullName,gender,className,sectionName,guardianName,guardianPhone,guardianEmail\n" +
                "ADM-CSV-9001,Meera Kulkarni,Female,Class 8,Section A,Suresh Kulkarni,+91 98201 11223,suresh@example.com\n" +
                "ADM-CSV-9001,Duplicate Row,Male,Class 8,Section A,Test,+91 98000 00000,dup@example.com\n" +
                "INVALID_SHORT_ROW\n";

        var job = bulkImportService.importStudentsFromCsv("batch_onboarding_2026.csv", csv, "SUPER_ADMIN");
        assertNotNull(job);
        assertEquals(3, job.getTotalRows());
        assertEquals(1, job.getSuccessCount());
        assertEquals(2, job.getFailedCount());
        assertEquals("COMPLETED_WITH_ERRORS", job.getStatus());
    }

    @Test
    void testTransactionalOutboxRelayAndDeadLetterRetry() {
        var failedEv = enterpriseSchedulerService.enqueueEvent(
                "WHATSAPP_TRUANCY_ALERT",
                "ADM-2024-0012",
                "{\"student\":\"Aarav Sharma\"}",
                true
        );
        assertEquals("DEAD_LETTER", failedEv.getStatus());

        var pendingEv = enterpriseSchedulerService.enqueueEvent(
                "LATE_FEE_ASSESSMENT",
                "INV-2026-0843",
                "{\"invoice\":\"INV-2026-0843\"}",
                false
        );
        assertEquals("PENDING", pendingEv.getStatus());

        int relayed = enterpriseSchedulerService.relayPendingOutboxEvents();
        assertTrue(relayed >= 1);

        int retried = enterpriseSchedulerService.retryFailedOrPendingForTenant();
        assertTrue(retried >= 1);
    }

    @Test
    void testMultiSignalStudentEarlyWarningRiskRadar() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").stream()
                .filter(s -> s.getAdmissionNumber().startsWith("ADM-"))
                .findFirst().orElseThrow();
        student.setAttendancePercentage(68.0);
        student.setFeeStatus("Overdue");
        student.setBalanceAmount(25000.0);
        studentRepository.save(student);

        // Add frequent infirmary visits to elevate clinical signal
        for (int i = 0; i < 3; i++) {
            infirmaryService.recordVisit(student.getId(), "Headache / fatigue", 99.4, "110/70", 76,
                    "Rest & hydration", 20, "RETURNED_TO_CLASS", "Nurse Priya");
        }

        var assessments = studentRiskRadarService.runSchoolWideEvaluation();
        assertFalse(assessments.isEmpty());
        var highRisk = studentRiskRadarService.getHighRiskStudents();
        assertFalse(highRisk.isEmpty(), "Expected student with low attendance + overdue fee + clinic visits to trigger Risk Radar");
        assertTrue(highRisk.get(0).getCompositeRiskScore() >= 35);
    }

    @Test
    void testNep2020HolisticProgressCardCompilation() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var entry = holisticProgressService.recordAssessment(
                student.getId(),
                "Term 1 AY 2026-27",
                "TEACHER",
                "COGNITIVE_INQUIRY",
                "EXEMPLARY",
                8.5,
                "Designed working solar water purifier model"
        );
        assertNotNull(entry);
        assertEquals(4, entry.getNumericScore());

        var report = holisticProgressService.compileStudentHpc(student.getId());
        assertNotNull(report);
        assertTrue(report.getTotalVocationalAndExperientialHours() > 0);
        assertFalse(report.getAssessments().isEmpty());
    }

    @Test
    void testCampusSafetyAndBusRtoComplianceExpirySentinel() {
        var alerts = complianceSentinelService.scanExpiringCompliance(60);
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());
        assertTrue(alerts.stream().anyMatch(a -> "EXPIRING_IN_30_DAYS".equals(a.getAlertSeverity())));
    }

    @Test
    void testSyllabusAwareSubstituteHandoffAndWorkloadEquity() {
        var progress = syllabusEquityService.updateSyllabusProgress(
                "Class 10 - Section A",
                "Mathematics",
                "Sunita Rao",
                "Chapter 5: Arithmetic Progressions",
                "General Term of AP (Ex 5.2)",
                "Solve Sum of N Terms (Ex 5.3) Questions 1 to 6",
                "s3://apex-lms/math/ap-ex5-3.pdf",
                71.0
        );
        assertNotNull(progress);

        var handoff = syllabusEquityService.getSubstituteHandoffBrief("Class 10 - Section A", "Mathematics");
        assertEquals("Chapter 5: Arithmetic Progressions", handoff.getCurrentChapter());
        assertTrue(handoff.getImmediateSubstituteInstruction().contains("Ex 5.3"));

        var equity = workloadEquityService.analyzeFacultyWorkloadEquity();
        assertFalse(equity.isEmpty());
        for (int i = 1; i < equity.size(); i++) {
            assertTrue(equity.get(i).getCompositeWorkloadIndex() >= equity.get(i - 1).getCompositeWorkloadIndex(),
                    "Workload equity list must sort least-burdened teachers first");
        }
    }

    @Test
    void testClassDataExportExcelCsvAndPrintableRoster() throws Exception {
        var students = studentRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(students.isEmpty(), "Seeded students must exist");
        String sampleClass = students.get(0).getClassName();
        String sampleSection = students.get(0).getSectionName();

        // 1. Test Excel (.xlsx) generation
        byte[] excelBytes = studentExportService.generateExcelExport("tenant_apex_mumbai_01", sampleClass, sampleSection);
        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 500, "Generated Excel workbook must contain valid binary XLSX payload");

        // Validate Excel structure using POI
        try (org.apache.poi.ss.usermodel.Workbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook(new java.io.ByteArrayInputStream(excelBytes))) {
            org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheet("Class Roster");
            assertNotNull(sheet, "Sheet 'Class Roster' must exist in workbook");
            assertTrue(sheet.getPhysicalNumberOfRows() >= 5, "Sheet must contain header banner and student rows");
        }

        // 2. Test Streamed CSV generation
        String csvData = studentExportService.generateCsvExport("tenant_apex_mumbai_01", sampleClass, sampleSection);
        assertNotNull(csvData);
        assertTrue(csvData.startsWith("\uFEFF"), "CSV must begin with UTF-8 BOM for Microsoft Excel compatibility");
        assertTrue(csvData.contains("Admission No,Roll No,Full Name"), "CSV must contain standard student header columns");
        assertTrue(csvData.lines().count() >= 2, "CSV must contain header line and student data lines");

        // 3. Test Printable Roster HTML generation
        String htmlRoster = studentExportService.generatePrintableRosterHtml("tenant_apex_mumbai_01", sampleClass, sampleSection);
        assertNotNull(htmlRoster);
        assertTrue(htmlRoster.contains("<!DOCTYPE html>"));
        assertTrue(htmlRoster.contains("@media print"));
        assertTrue(htmlRoster.contains("Official Classroom Attendance & Verification Roster"));
        assertTrue(htmlRoster.contains("window.print()"));
        assertTrue(htmlRoster.contains("Principal Seal & Signature"));
    }

    @Test
    void testTeacherSchoolEventManagementAndParticipationHousePoints() {
        // 1. Create a teacher-led school event
        var eventReq = com.schoolerp.modules.academics.dto.CreateSchoolEventRequest.builder()
                .title("Inter-School Annual Science & Robotics Olympiad 2026")
                .description("Regional STEM championship featuring autonomous robotics and innovation models")
                .category("SCIENCE_EXHIBITION")
                .eventDate(java.time.LocalDate.now().plusDays(10))
                .endDate(java.time.LocalDate.now().plusDays(11))
                .venue("APJ Abdul Kalam Science Auditorium")
                .coordinatorTeacherName("Dr. Vikram Sarabhai")
                .targetClasses("Class 9, Class 10")
                .maxParticipants(50)
                .budgetAllocated(new BigDecimal("25000.00"))
                .build();

        var event = schoolEventService.createEvent(eventReq, "Dr. Vikram Sarabhai");
        assertNotNull(event);
        assertNotNull(event.getId());
        assertEquals("PUBLISHED", event.getStatus());
        assertEquals("SCIENCE_EXHIBITION", event.getCategory());

        // 2. Fetch seeded students to enroll
        var students = studentRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(students.isEmpty(), "Seeded students must exist");
        var leadStudent = students.get(0);

        // 3. Enroll individual participant with role
        var enrollReq = com.schoolerp.modules.academics.dto.EnrollParticipantRequest.builder()
                .participants(java.util.List.of(
                        com.schoolerp.modules.academics.dto.EnrollParticipantRequest.ParticipantItem.builder()
                                .studentId(leadStudent.getId())
                                .role("TEAM_CAPTAIN")
                                .build()
                ))
                .build();

        var enrolled = schoolEventService.enrollParticipants(event.getId(), enrollReq);
        assertEquals(1, enrolled.size());
        assertEquals("TEAM_CAPTAIN", enrolled.get(0).getRole());
        assertEquals(leadStudent.getFullName(), enrolled.get(0).getStudentName());

        // 4. Bulk enroll class cohort
        var cohort = schoolEventService.enrollClassSection(event.getId(), "Class 10", "Section A", "PARTICIPANT");
        assertNotNull(cohort);
        assertFalse(cohort.isEmpty());

        // 5. Record participant result & awards with House Points
        var resultReq = com.schoolerp.modules.academics.dto.RecordParticipantResultRequest.builder()
                .participationStatus("ATTENDED")
                .achievementTier("FIRST_PLACE")
                .housePointsAwarded(50)
                .issueCertificate(true)
                .teacherRemarks("Designed first-prize winning dual-axis solar tracking robotics arm")
                .build();

        var updated = schoolEventService.recordParticipantResult(event.getId(), enrolled.get(0).getId(), resultReq, "Dr. Vikram Sarabhai");
        assertNotNull(updated);
        assertEquals("FIRST_PLACE", updated.getAchievementTier());
        assertEquals(50, updated.getHousePointsAwarded());
        assertTrue(updated.isCertificateIssued());
        assertNotNull(updated.getCertificateNumber());
        assertTrue(updated.getCertificateNumber().startsWith("CERT-EVT-"));

        // 6. Test printable event call sheet
        String callSheet = schoolEventService.generateEventCallSheetHtml(event.getId());
        assertNotNull(callSheet);
        assertTrue(callSheet.contains("Official School Event Call Sheet"));
        assertTrue(callSheet.contains(event.getTitle()));
        assertTrue(callSheet.contains("TEAM_CAPTAIN"));
        assertTrue(callSheet.contains("FIRST_PLACE"));
        assertTrue(callSheet.contains("Event Coordinator Teacher"));
    }

    @Test
    void testStudentLeaveApplicationAndAttendanceNormalization() {
        var students = studentRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(students.isEmpty());
        var student = students.get(0);

        // 1. Submit leave application
        var applyReq = com.schoolerp.modules.attendance.dto.ApplyStudentLeaveRequest.builder()
                .studentId(student.getId())
                .leaveType("SICK_LEAVE")
                .startDate(java.time.LocalDate.now())
                .endDate(java.time.LocalDate.now().plusDays(2))
                .reason("Viral fever and doctor prescribed bed rest")
                .medicalCertificateUrl("s3://apex-health/leaves/med-cert-0842.pdf")
                .build();

        var app = studentLeaveService.applyLeave(applyReq, null, "Rajesh Sharma", "+91 98765 43210");
        assertNotNull(app);
        assertNotNull(app.getId());
        assertEquals("PENDING", app.getStatus());
        assertEquals(3, app.getTotalDays());

        // 2. Review and approve leave with attendance normalization
        var reviewReq = com.schoolerp.modules.attendance.dto.ReviewStudentLeaveRequest.builder()
                .status("APPROVED")
                .reviewRemarks("Medical certificate verified. Approved for 3 days.")
                .build();

        var reviewed = studentLeaveService.reviewLeave(app.getId(), reviewReq, "Dr. Vikram Seth");
        assertNotNull(reviewed);
        assertEquals("APPROVED", reviewed.getStatus());
        assertEquals("Dr. Vikram Seth", reviewed.getReviewedByStaffName());
        assertNotNull(reviewed.getReviewedAt());

        // 3. Verify querying by status
        var approvedList = studentLeaveService.getLeaves(null, "APPROVED", null, null);
        assertFalse(approvedList.isEmpty());
        assertTrue(approvedList.stream().anyMatch(l -> l.getId().equals(app.getId())));
    }

    @Test
    void testExamQuestionBankAndBlueprintPaperGenerator() {
        // 1. Add question with Bloom's taxonomy
        var qReq = com.schoolerp.modules.examinations.dto.CreateQuestionRequest.builder()
                .subjectName("Mathematics")
                .gradeLevel("Class 10")
                .chapterName("Chapter 4: Quadratic Equations")
                .topicName("Nature of Roots")
                .questionType("SHORT_ANSWER")
                .bloomsTaxonomy("APPLY")
                .difficultyLevel("MEDIUM")
                .marks(3)
                .questionText("Find the value of k for which the quadratic equation 2x^2 + kx + 3 = 0 has two real equal roots.")
                .markingSchemeAnswerKey("For equal roots, D = b^2 - 4ac = 0. k^2 - 24 = 0 => k = +/- 2*sqrt(6). (3 marks)")
                .build();

        var question = questionBankService.addQuestion(qReq, "Sunita Rao");
        assertNotNull(question);
        assertNotNull(question.getId());
        assertEquals("APPLY", question.getBloomsTaxonomy());

        // 2. Generate exam paper from blueprint
        var paperReq = com.schoolerp.modules.examinations.dto.GenerateExamPaperRequest.builder()
                .examTitle("Periodic Assessment II - Mathematics 2026")
                .subjectName("Mathematics")
                .gradeLevel("Class 10")
                .totalMarks(80)
                .durationMinutes(180)
                .targetMcqCount(5)
                .targetShortCount(3)
                .targetLongCount(2)
                .build();

        var paper = questionBankService.generateExamPaper(paperReq);
        assertNotNull(paper);
        assertNotNull(paper.getExamPaperCode());
        assertTrue(paper.getExamPaperCode().startsWith("QP-"));
        assertEquals("Mathematics", paper.getSubjectName());
        assertFalse(paper.getSections().isEmpty());
        assertTrue(paper.getGeneratedQuestionsCount() > 0);

        // 3. Generate printable HTML exam paper
        String htmlPaper = questionBankService.generatePrintableExamPaperHtml(paperReq);
        assertNotNull(htmlPaper);
        assertTrue(htmlPaper.contains("NATIONAL PUBLIC ACADEMY"));
        assertTrue(htmlPaper.contains("Periodic Assessment II"));
        assertTrue(htmlPaper.contains("Candidate Roll No"));
    }

    @Test
    void testScienceLabInventoryAndApparatusBreakageFeeAssessment() {
        var students = studentRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(students.isEmpty());
        var student = students.get(0);
        double initialBalance = student.getBalanceAmount();

        // 1. Fetch lab inventory
        var inventory = labInventoryService.getInventory("ALL");
        assertNotNull(inventory);
        assertFalse(inventory.isEmpty());
        var burette = inventory.stream()
                .filter(i -> i.getItemName().contains("Burette"))
                .findFirst()
                .orElse(inventory.get(0));

        int initialQty = burette.getQuantityOnHand();

        // 2. Record apparatus breakage
        var breakReq = com.schoolerp.modules.operations.dto.RecordLabBreakageRequest.builder()
                .labItemId(burette.getId())
                .studentId(student.getId())
                .incidentDate(java.time.LocalDate.now())
                .faultType("ACCIDENTAL")
                .assessedDamageCost(new BigDecimal("850.00"))
                .remarks("Burette tip fractured while clamping during titration practical")
                .build();

        var breakage = labInventoryService.recordBreakage(breakReq, "Lab Assistant Shinde");
        assertNotNull(breakage);
        assertNotNull(breakage.getInvoiceNumberGenerated());
        assertTrue(breakage.getInvoiceNumberGenerated().startsWith("INV-BRK-"));
        assertEquals("INVOICED_TO_STUDENT", breakage.getFineStatus());

        // 3. Verify student fee ledger was debited
        var updatedStudent = studentRepository.findById(student.getId()).orElseThrow();
        assertEquals(initialBalance + 850.0, updatedStudent.getBalanceAmount(), 0.01);

        // 4. Verify physical inventory was decremented
        var updatedBurette = labInventoryService.getInventory("Chemistry Lab").stream()
                .filter(i -> i.getId().equals(burette.getId()))
                .findFirst()
                .orElseThrow();
        assertEquals(initialQty - 1, updatedBurette.getQuantityOnHand());
    }

    @Test
    void testCampusLostAndFoundPropertyVault() {
        var students = studentRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(students.isEmpty());
        var student = students.get(0);

        // 1. Log found item
        var logReq = com.schoolerp.modules.operations.dto.LogLostFoundRequest.builder()
                .itemCategory("ELECTRONICS_CALCULATOR")
                .itemDescription("Casio fx-991EX ClassWiz Scientific Calculator with initials AS engraved on cover")
                .foundLocation("Physics Lab Station 4")
                .foundDate(java.time.LocalDate.now())
                .storageLockerRef("Security Vault Locker E-3")
                .build();

        var item = lostAndFoundService.logFoundItem(logReq, "Janitor Ramesh");
        assertNotNull(item);
        assertNotNull(item.getTrackingCode());
        assertTrue(item.getTrackingCode().startsWith("LF-"));
        assertEquals("FOUND_IN_CUSTODY", item.getStatus());

        // 2. Claim item
        var claimReq = com.schoolerp.modules.operations.dto.ClaimLostFoundRequest.builder()
                .claimedByStudentId(student.getId())
                .claimedByName(student.getFullName())
                .claimVerificationNote("Matched engraving initials 'AS' and serial number verified")
                .build();

        var claimed = lostAndFoundService.processClaim(item.getId(), claimReq);
        assertNotNull(claimed);
        assertEquals("CLAIMED_RETURNED", claimed.getStatus());
        assertEquals(student.getFullName(), claimed.getClaimedByName());
        assertNotNull(claimed.getReturnedAt());
    }

    @Test
    void testClass12AlumniAndHigherEducationPlacementTracker() {
        var students = studentRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(students.isEmpty());
        var student = students.get(0);

        // 1. Record university admission & national entrance rank
        var placeReq = com.schoolerp.modules.students.dto.RecordPlacementRequest.builder()
                .studentId(student.getId())
                .universityName("IIT Bombay")
                .country("India")
                .degreeProgram("B.Tech Computer Science & Engineering")
                .admissionType("ENTRANCE_RANK")
                .entranceExamName("JEE Advanced")
                .entranceScoreOrRank("AIR 142")
                .scholarshipAmountInr(new BigDecimal("150000.00"))
                .build();

        var placement = alumniPlacementService.recordPlacement(placeReq);
        assertNotNull(placement);
        assertNotNull(placement.getId());
        assertEquals("IIT Bombay", placement.getUniversityName());
        assertEquals("AIR 142", placement.getEntranceScoreOrRank());

        // 2. Verify alumni directory
        var placements = alumniPlacementService.getPlacements("IIT");
        assertFalse(placements.isEmpty());
        assertTrue(placements.stream().anyMatch(p -> p.getUniversityName().contains("IIT Bombay")));

        var profiles = alumniPlacementService.getAlumniProfiles();
        assertFalse(profiles.isEmpty());
        assertTrue(profiles.stream().anyMatch(p -> p.getStudentName().equals(student.getFullName())));
    }
}



