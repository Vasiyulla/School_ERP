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
    private com.schoolerp.modules.finance.repository.FeeInvoiceRepository feeInvoiceRepository;

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
}
