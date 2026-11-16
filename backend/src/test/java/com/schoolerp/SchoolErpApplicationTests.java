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
    private com.schoolerp.modules.students.service.StudentService studentService;

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
    private com.schoolerp.modules.admissions.service.AdmissionsCrmService admissionsCrmService;

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

    @Autowired
    private com.schoolerp.modules.institution.service.ConfidentialSafetyService confidentialSafetyService;

    @Autowired
    private com.schoolerp.modules.staff.service.FacultyAppraisalService facultyAppraisalService;

    @Autowired
    private com.schoolerp.modules.institution.service.ExecutiveCockpitService executiveCockpitService;

    @Autowired
    private com.schoolerp.modules.communication.service.GrievanceOmbudsmanService grievanceService;

    @Autowired
    private com.schoolerp.modules.institution.service.InspectionDossierService inspectionDossierService;

    @Autowired
    private com.schoolerp.modules.system.service.DatabaseHaService databaseHaService;

    @Autowired
    private com.schoolerp.modules.staff.service.CpcSalaryService cpcSalaryService;

    @Autowired
    private com.schoolerp.modules.staff.service.StaffTaxService staffTaxService;

    @Autowired
    private com.schoolerp.modules.finance.service.CautionMoneyRefundService cautionMoneyRefundService;

    @Autowired
    private com.schoolerp.modules.staff.service.StaffSalaryLoanService staffSalaryLoanService;

    @Autowired
    private com.schoolerp.modules.finance.service.VendorProcurementService vendorProcurementService;

    @Autowired
    private com.schoolerp.modules.academics.service.TimetableSchedulingService timetableSchedulingService;

    @Autowired
    private com.schoolerp.modules.staff.service.TeacherCpdService teacherCpdService;

    @Autowired
    private com.schoolerp.modules.finance.service.BankReconciliationService bankReconciliationService;

    @Autowired
    private com.schoolerp.modules.finance.service.SiblingConcessionService siblingConcessionService;

    @Autowired
    private com.schoolerp.modules.attendance.service.BiometricSyncService biometricSyncService;

    @Autowired
    private com.schoolerp.modules.staff.service.DesignationHierarchyService designationService;

    @Autowired
    private com.schoolerp.modules.academics.service.LessonMaterialService lessonMaterialService;

    @Autowired
    private com.schoolerp.modules.academics.service.ExitTicketService exitTicketService;

    @Autowired
    private com.schoolerp.modules.students.service.LearningPortfolioService learningPortfolioService;

    @Autowired
    private com.schoolerp.modules.academics.service.AdaptiveStudyPlanService adaptiveStudyPlanService;

    @Autowired
    private com.schoolerp.modules.students.service.ClassroomBadgeService classroomBadgeService;

    @Autowired
    private com.schoolerp.modules.academics.service.PblProjectService pblProjectService;

    @Autowired
    private com.schoolerp.modules.institution.service.EmergencyCampusModeService emergencyCampusModeService;

    @Autowired
    private com.schoolerp.modules.academics.service.VirtualClassroomService virtualClassroomService;

    @Autowired
    private com.schoolerp.modules.attendance.service.VirtualAttendanceService virtualAttendanceService;

    @Autowired
    private com.schoolerp.modules.system.service.CustomEntityExtensibilityService customExtensibilityService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private org.springframework.cache.CacheManager cacheManager;

    @Autowired
    private com.schoolerp.modules.system.service.AuditLogService auditLogService;

    @Autowired
    private com.schoolerp.modules.operations.repository.VehicleRepository vehicleRepository;

    @Autowired
    private com.schoolerp.modules.academics.service.MorningSubstitutionService morningSubstitutionService;

    @Autowired
    private com.schoolerp.modules.examinations.service.ExamInvigilationService examInvigilationService;

    @Autowired
    private com.schoolerp.modules.operations.service.BookstoreService bookstoreService;

    @Autowired
    private com.schoolerp.modules.students.service.HealthPassportService healthPassportService;

    @Autowired
    private com.schoolerp.modules.operations.service.CafeteriaAllergenService cafeteriaAllergenService;

    @Autowired
    private com.schoolerp.modules.operations.service.CostumeRentalService costumeRentalService;

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

    @Test
    void testConfidentialChildSafetyAndPoshPocsoVault() {
        var logReq = com.schoolerp.modules.institution.dto.LogSafetyIncidentRequest.builder()
                .classification("POCSO_STATUTORY")
                .severityLevel("CRITICAL_RED")
                .involvedPartyName("Confidential Inquiry #108")
                .roleInvolved("STUDENT")
                .incidentSummary("Allegation of peer cyber-bullying and unauthorized photographic dissemination")
                .actionTakenInterim("Immediate counseling provided; devices confiscated for forensic audit")
                .statutoryNotificationRequired(true)
                .statutoryAuthorityRef("CWC-MUM-2026-904")
                .inquiryCommitteeMembers("Principal Dr. Rajesh Sharma, Vice Principal Meera Joshi, School Psychologist Dr. Aditi Sen")
                .build();

        var incident = confidentialSafetyService.logIncident(logReq);
        assertNotNull(incident);
        assertNotNull(incident.getIncidentRefCode());
        assertTrue(incident.getIncidentRefCode().startsWith("SAFE-"));
        assertEquals("OPEN_INQUIRY", incident.getStatus());
        assertTrue(incident.isStatutoryNotificationRequired());

        // Conclude inquiry
        var resolved = confidentialSafetyService.resolveIncident(incident.getId(), "CLOSED_RESOLVED", "Internal committee found zero malicious intent; behavioral counseling concluded.");
        assertNotNull(resolved);
        assertEquals("CLOSED_RESOLVED", resolved.getStatus());
        assertNotNull(resolved.getClosedAt());
    }

    @Test
    void testFacultyAnnualAppraisalAndPbasIncrementEngine() {
        var employees = employeeRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(employees.isEmpty());
        var teacher = employees.get(0);

        var evalReq = com.schoolerp.modules.staff.dto.EvaluateFacultyAppraisalRequest.builder()
                .teacherId(teacher.getId())
                .academicYear("2026-2027")
                .selfAppraisalScore(24.0) // out of 25
                .academicResultsScore(34.0) // out of 35
                .institutionalDutyScore(19.0) // out of 20
                .principalObservationScore(18.5) // out of 20
                .principalEvaluationNotes("Exemplary classroom mastery, active robotics mentor, and 100% board pass outcome.")
                .build();

        var appraisal = facultyAppraisalService.evaluateTeacher(evalReq, "Principal Dr. Rajesh Sharma");
        assertNotNull(appraisal);
        assertEquals(95.5, appraisal.getCompositeScore(), 0.01);
        assertEquals("EXEMPLARY_A_PLUS", appraisal.getAppraisalBand());
        assertEquals(10.0, appraisal.getRecommendedIncrementPercentage());

        var list = facultyAppraisalService.getAppraisals("2026-2027");
        assertFalse(list.isEmpty());
        assertTrue(list.stream().anyMatch(a -> a.getTeacherId().equals(teacher.getId())));
    }

    @Test
    void testExecutiveBoardCommandCockpitKpis() {
        var kpis = executiveCockpitService.getExecutiveKpis();
        assertNotNull(kpis);
        assertTrue(kpis.getTotalEnrolledStudents() > 0);
        assertEquals(2000, kpis.getTotalSanctionedCapacity());
        assertTrue(kpis.getOccupancyRatePercentage() > 0.0);
        assertTrue(kpis.getTotalFacultyCount() > 0);
        assertNotNull(kpis.getTeacherStudentRatio());
        assertNotNull(kpis.getTotalFeeBilledInr());
        assertNotNull(kpis.getFeeRealizationRatePercentage());
        assertTrue(kpis.getCampusSafetyComplianceScore() >= 0.0);
        assertNotNull(kpis.getInstitutionalHealthTier());
    }

    @Test
    void testParentGrievanceOmbudsmanDeskAndSlaEscalation() {
        var submitReq = com.schoolerp.modules.communication.dto.SubmitGrievanceRequest.builder()
                .parentName("Suresh Deshmukh")
                .parentPhone("+91 98333 44556")
                .studentName("Ananya Deshmukh")
                .className("Class 10 - Section A")
                .category("TRANSPORT_BUS")
                .subject("Bus Route #07 consistently arriving 25 minutes late at pickup stop")
                .description("Morning pickup time is 07:15 AM but bus arrives at 07:40 AM causing student to miss morning assembly.")
                .build();

        var ticket = grievanceService.submitGrievance(submitReq, "Suresh Deshmukh");
        assertNotNull(ticket);
        assertNotNull(ticket.getTicketNumber());
        assertTrue(ticket.getTicketNumber().startsWith("GRV-"));
        assertEquals("TIER_1_TEACHER", ticket.getCurrentTier());
        assertEquals("OPEN", ticket.getStatus());
        assertNotNull(ticket.getSlaDeadline());

        // Escalate to Principal Desk
        var escReq = com.schoolerp.modules.communication.dto.ReviewGrievanceRequest.builder()
                .action("ESCALATE_TO_PRINCIPAL")
                .resolutionNotes("Driver route delay persists due to road construction; escalating for alternate fleet routing")
                .build();

        var escalated = grievanceService.reviewOrEscalate(ticket.getId(), escReq, "Transport Supervisor");
        assertNotNull(escalated);
        assertEquals("TIER_3_PRINCIPAL_DESK", escalated.getCurrentTier());
        assertEquals("ESCALATED_TO_PRINCIPAL", escalated.getStatus());

        // Principal resolves ticket
        var resReq = com.schoolerp.modules.communication.dto.ReviewGrievanceRequest.builder()
                .action("RESOLVE")
                .resolutionNotes("Principal approved Route #07 diversion via Eastern Expressway. Pickup timing restored to 07:15 AM.")
                .build();

        var resolved = grievanceService.reviewOrEscalate(ticket.getId(), resReq, "Principal Dr. Rajesh Sharma");
        assertNotNull(resolved);
        assertEquals("RESOLVED", resolved.getStatus());
        assertNotNull(resolved.getResolvedAt());
    }

    @Test
    void testCbseInspectionReadinessDossierGenerator() {
        var dossier = inspectionDossierService.generateDossier();
        assertNotNull(dossier);
        assertEquals("Apex Academy (Mumbai)", dossier.getSchoolName());
        assertEquals("CBSE #113042", dossier.getAffiliationNumber());
        assertEquals("FULL_COMPLIANCE", dossier.getInspectionReadinessStatus());
        assertTrue(dossier.isBuildingSafetyNocValid());
        assertTrue(dossier.isFireSafetyNocValid());
        assertTrue(dossier.isCleanWaterAndSanitationValid());
        assertFalse(dossier.getMandatoryDisclosures().isEmpty());

        String html = inspectionDossierService.generatePrintableDossierHtml();
        assertNotNull(html);
        assertTrue(html.contains("Official Accreditation & CBSE Inspection Audit Dossier"));
        assertTrue(html.contains("Apex Academy (Mumbai)"));
        assertTrue(html.contains("Principal & Head of Institution"));
    }

    @Test
    void testDualDatabaseHaClusterStatusAndReplication() {
        var status = databaseHaService.getHaClusterStatus();
        assertNotNull(status);
        assertEquals("tenant_apex_mumbai_01", status.getTenantId());
        assertEquals("school_apex_mumbai_01_db", status.getSchoolDatabaseName());
        assertEquals("db-primary", status.getPrimaryNodeHost());
        assertEquals(5432, status.getPrimaryNodePort());
        assertEquals("ONLINE_ACTIVE", status.getPrimaryNodeStatus());
        assertEquals("db-secondary", status.getSecondaryNodeHost());
        assertEquals(5433, status.getSecondaryNodePort());
        assertEquals("HOT_STANDBY_IN_SYNC", status.getSecondaryNodeStatus());
        assertEquals("db-primary", status.getActivePrimaryHost());
        assertEquals("STREAMING_SYNCHRONOUS", status.getReplicationMode());
        assertEquals(0L, status.getReplicationLagBytes());
        assertEquals(0L, status.getReplicationLagMs());
        assertEquals("HIGH_AVAILABILITY_OPTIMAL", status.getClusterHealthTier());
        assertEquals("MULTI_HOST_JDBC_AUTO_FAILOVER", status.getFailoverDriverMode());
    }

    @Test
    void testSimulatedPrimaryCrashAndInstantFailoverPromotion() {
        var req = com.schoolerp.modules.system.dto.SimulateFailoverRequest.builder()
                .triggerReason("SIMULATED_PRIMARY_NODE_DISK_CORRUPTION")
                .targetPromotionNode("db-secondary")
                .remarks("Disaster recovery live promotion drill")
                .build();

        var result = databaseHaService.simulateFailover(req);
        assertNotNull(result);
        assertEquals("tenant_apex_mumbai_01", result.getTenantId());
        assertEquals("FAILOVER_SUCCESSFUL_PROMOTED", result.getFailoverStatus());
        assertEquals("db-primary", result.getPreviousPrimaryHost());
        assertEquals("db-secondary", result.getNewPromotedPrimaryHost());
        assertEquals(0L, result.getTransactionsLostCount());
        assertEquals("100% ZERO_DATA_LOSS_CONFIRMED", result.getDataIntegrityAudit());
        assertTrue(result.getFailoverDurationMs() < 3000L);

        // Verify state is updated to reflect promoted secondary
        var updated = databaseHaService.getHaClusterStatus();
        assertEquals("db-secondary", updated.getActivePrimaryHost());
        assertEquals("CRASHED_OFFLINE", updated.getPrimaryNodeStatus());
        assertEquals("PROMOTED_PRIMARY", updated.getSecondaryNodeStatus());
        assertEquals("FAILOVER_ACTIVE", updated.getClusterHealthTier());

        // Reset topology back to default for other tests
        var reset = databaseHaService.resetClusterTopology();
        assertEquals("db-primary", reset.getActivePrimaryHost());
        assertEquals("ONLINE_ACTIVE", reset.getPrimaryNodeStatus());
        assertEquals("HOT_STANDBY_IN_SYNC", reset.getSecondaryNodeStatus());
        assertEquals("HIGH_AVAILABILITY_OPTIMAL", reset.getClusterHealthTier());
    }

    @Test
    void testSeventhCpcPayScaleAndDaRevision() {
        var matrices = cpcSalaryService.getOrCreateCpcMatrix();
        assertNotNull(matrices);
        assertFalse(matrices.isEmpty());
        assertTrue(matrices.stream().anyMatch(m -> "LEVEL_6_PRT".equals(m.getPayLevel())));

        var req = com.schoolerp.modules.staff.dto.ApplyDaRevisionRequest.builder()
                .newDaPercentage(50.0)
                .effectiveFromMonth("2026-07")
                .govtOrderNotificationRef("MoE/CBSE/F.No.1/1/2026-E.II(B)")
                .build();

        var result = cpcSalaryService.applySchoolWideDaRevision(req);
        assertNotNull(result);
        assertEquals(50.0, result.getUpdatedDaPercentage());
        assertEquals("2026-07", result.getEffectiveFromMonth());
        assertTrue(result.getAffectedFacultyCount() > 0);
        assertNotNull(result.getSchoolMonthlyPayrollIncreaseInr());
    }

    @Test
    void testStaffTaxDeclarationAndForm16Computation() {
        var employees = employeeRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(employees.isEmpty());
        var teacher = employees.get(0);

        var req = com.schoolerp.modules.staff.dto.SubmitTaxDeclarationRequest.builder()
                .employeeId(teacher.getId())
                .taxRegime("NEW_REGIME_115BAC")
                .financialYear("2026-2027")
                .build();

        var declaration = staffTaxService.submitDeclaration(req);
        assertNotNull(declaration);
        assertEquals("NEW_REGIME_115BAC", declaration.getTaxRegime());
        assertEquals(new java.math.BigDecimal("75000.00"), declaration.getStandardDeduction());
        assertNotNull(declaration.getMonthlyTdsDeduction());

        var form16 = staffTaxService.generateForm16PartB(teacher.getId());
        assertNotNull(form16);
        assertEquals("Apex Academy (Mumbai)", form16.getSchoolName());
        assertEquals(teacher.getFullName(), form16.getEmployeeName());
        assertEquals("NEW_REGIME_115BAC", form16.getChosenTaxRegime());
        assertEquals(new java.math.BigDecimal("75000.00"), form16.getStandardDeduction());
        assertNotNull(form16.getTotalTaxPayableAnnual());
    }

    @Test
    void testStudentCautionMoneyRefundLedgerVoucher() {
        var students = studentRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(students.isEmpty());
        var student = students.get(0);

        var refundReq = com.schoolerp.modules.finance.dto.ProcessCautionMoneyRefundRequest.builder()
                .studentId(student.getId())
                .autoDeductUnpaidTuitionDues(true)
                .autoDeductLabApparatusBreakages(true)
                .refundMode("BANK_NEFT")
                .beneficiaryAccountNo("50100482910482")
                .beneficiaryIfsc("HDFC0001029")
                .reason("Completed Class 12 Senior Secondary Graduation")
                .build();

        var refund = cautionMoneyRefundService.processDepartureRefund(refundReq);
        assertNotNull(refund);
        assertNotNull(refund.getRefundVoucherNumber());
        assertTrue(refund.getRefundVoucherNumber().startsWith("REF-VOUCHER-"));
        assertEquals("REFUNDED_CLOSED", refund.getRefundStatus());
        assertNotNull(refund.getNetRefundPayoutInr());
        assertNotNull(refund.getLedgerJournalEntryId());
    }

    @Test
    void testStaffSalaryLoanDisbursementAndEmiPayrollDeduction() {
        var employees = employeeRepository.findAllByTenantId("tenant_apex_mumbai_01");
        assertFalse(employees.isEmpty());
        var teacher = employees.get(0);

        var loanReq = com.schoolerp.modules.staff.dto.ApplySalaryLoanRequest.builder()
                .employeeId(teacher.getId())
                .loanType("FESTIVAL_ADVANCE")
                .principalAmount(new java.math.BigDecimal("30000.00"))
                .tenureMonths(6)
                .remarks("Diwali festival staff advance")
                .build();

        var loan = staffSalaryLoanService.disburseLoan(loanReq, "Principal Dr. Rajesh Sharma");
        assertNotNull(loan);
        assertEquals("ACTIVE_RECOVERING", loan.getStatus());
        assertEquals(new java.math.BigDecimal("5000.00"), loan.getMonthlyEmi());
        assertEquals(new java.math.BigDecimal("30000.00"), loan.getRemainingBalance());

        var activeLoans = staffSalaryLoanService.getActiveLoans();
        assertFalse(activeLoans.isEmpty());
        assertTrue(activeLoans.stream().anyMatch(l -> l.getId().equals(loan.getId())));
    }

    @Test
    void testVendorBillProcurementWithTds194C() {
        var invReq = com.schoolerp.modules.finance.dto.CreateVendorInvoiceRequest.builder()
                .vendorName("Mahavir Bus Fleet Maintenance Corp")
                .vendorGstin("27AAACM9921D1ZA")
                .vendorPan("AAACM9921D")
                .serviceCategory("FLEET_TRANSPORT")
                .invoiceNumber("INV-BUS-2026-8812")
                .invoiceDate(java.time.LocalDate.now())
                .tdsSection("SECTION_194C_CONTRACTORS")
                .tdsRatePercentage(2.0)
                .grossAmount(new java.math.BigDecimal("150000.00"))
                .purchaseOrderRef("PO-FLEET-2026-0041")
                .remarks("Quarterly bus fitness inspection, brake shoe replacement, and speed governor calibration")
                .build();

        var bill = vendorProcurementService.recordVendorInvoice(invReq);
        assertNotNull(bill);
        assertEquals("Mahavir Bus Fleet Maintenance Corp", bill.getVendorName());
        assertEquals("SECTION_194C_CONTRACTORS", bill.getTdsSection());
        assertEquals(new java.math.BigDecimal("3000.00"), bill.getTdsWithheldAmount()); // 2% of 1.5L
        assertEquals(new java.math.BigDecimal("147000.00"), bill.getNetPayableAmount());
        assertEquals("PENDING_DISBURSEMENT", bill.getPaymentStatus());
    }

    @Test
    void testTimetableAutoSchedulingEngine() {
        var req = com.schoolerp.modules.academics.dto.AutoGenerateTimetableRequest.builder()
                .academicYear("2026-2027")
                .className("Grade 10")
                .sectionName("A")
                .periodsPerDay(7)
                .daysOfWeek(java.util.List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))
                .subjects(java.util.List.of("Mathematics", "Physics", "Chemistry", "English", "Social Science", "Hindi", "Physical Education"))
                .teacherAssignments(java.util.Map.of(
                        "Mathematics", "Mr. Sharma",
                        "Physics", "Dr. Rao",
                        "Chemistry", "Ms. Kapoor",
                        "English", "Ms. Davis",
                        "Social Science", "Mr. Verma",
                        "Hindi", "Ms. Joshi",
                        "Physical Education", "Coach Singh"
                ))
                .build();

        var result = timetableSchedulingService.autoGenerateClassTimetable(req);
        assertNotNull(result);
        assertEquals("Grade 10", result.getClassName());
        assertEquals("A", result.getSectionName());
        assertEquals(35, result.getTotalPeriodsScheduled()); // 5 days * 7 periods
        assertEquals(0, result.getCollisionCount());
        assertEquals("OPTIMAL_COLLISION_FREE", result.getStatus());
        assertFalse(result.getGeneratedSlots().isEmpty());

        var teacherSchedule = timetableSchedulingService.getTeacherWeeklySchedule("Mr. Sharma");
        assertNotNull(teacherSchedule);
        assertFalse(teacherSchedule.isEmpty());
    }

    @Test
    void testTeacherCpd50HourComplianceTracker() {
        var logReq = com.schoolerp.modules.staff.dto.LogCpdTrainingRequest.builder()
                .staffId(java.util.UUID.randomUUID())
                .teacherName("Ms. Pooja Hegde")
                .trainingYear("2026-2027")
                .agency("CBSE_COE")
                .trainingTopic("Competency Based Assessment & Item Writing")
                .domain("PEDAGOGY")
                .durationHours(15)
                .completionDate(java.time.LocalDate.now())
                .certificateRef("CERT-CBSE-COE-9912")
                .evaluationScore(92.0)
                .build();

        var record = teacherCpdService.logTraining(logReq, "Principal Dr. Rajesh Sharma");
        assertNotNull(record);
        assertEquals("Ms. Pooja Hegde", record.getTeacherName());
        assertEquals(15, record.getDurationHours());
        assertEquals("CBSE_COE", record.getAgency());

        var report = teacherCpdService.getSchoolCpdComplianceSummary("2026-2027");
        assertNotNull(report);
        assertTrue(report.getTotalTeachersAudited() >= 1);
        assertFalse(report.getTeacherSummaries().isEmpty());
    }

    @Test
    void testBankStatementAutoReconciliation() {
        // Create an invoice to reconcile
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var testInv = feeInvoiceRepository.save(com.schoolerp.modules.finance.entity.FeeInvoice.builder()
                .tenantId("tenant_apex_mumbai_01")
                .invoiceNumber("INV-2026-9901")
                .studentId(student.getId())
                .studentName(student.getFullName())
                .totalAmount(new java.math.BigDecimal("35000.00"))
                .paidAmount(java.math.BigDecimal.ZERO)
                .balanceAmount(new java.math.BigDecimal("35000.00"))
                .dueDate(java.time.LocalDate.now().plusDays(15))
                .status("OVERDUE")
                .build());

        var reconReq = com.schoolerp.modules.finance.dto.ReconcileBankStatementRequest.builder()
                .bankName("HDFC_BANK")
                .statementEntries(java.util.List.of(
                        com.schoolerp.modules.finance.dto.BankStatementEntryInputDTO.builder()
                                .utrNumber("HDFC20261114001")
                                .amount(new java.math.BigDecimal("35000.00"))
                                .transactionDate(java.time.LocalDate.now())
                                .narration("NEFT/CMS/INV-2026-9901/PARENT FEE TRANSFER")
                                .build(),
                        com.schoolerp.modules.finance.dto.BankStatementEntryInputDTO.builder()
                                .utrNumber("HDFC20261114002")
                                .amount(new java.math.BigDecimal("5000.00"))
                                .transactionDate(java.time.LocalDate.now())
                                .narration("CASH DEPOSIT AT KANDIVALI BRANCH - UNKNOWN REF")
                                .build()
                ))
                .build();

        var result = bankReconciliationService.processStatement(reconReq);
        assertNotNull(result);
        assertEquals(2, result.getTotalProcessed());
        assertEquals(1, result.getAutoReconciledCount());
        assertEquals(1, result.getSuspensePoolCount());
        assertEquals(new java.math.BigDecimal("35000.00"), result.getTotalReconciledAmount());

        var suspense = bankReconciliationService.getSuspensePool();
        assertFalse(suspense.isEmpty());
    }

    @Test
    void testSiblingConcessionPolicyEvaluation() {
        var polReq = com.schoolerp.modules.finance.dto.ConfigureConcessionPolicyRequest.builder()
                .policyCode("SIBLING_TIER_2")
                .siblingRank(2)
                .concessionPercentage(new java.math.BigDecimal("15.00"))
                .applicableFeeHead("TUITION_FEE")
                .isActive(true)
                .build();

        var policy = siblingConcessionService.configurePolicy(polReq);
        assertNotNull(policy);
        assertEquals(new java.math.BigDecimal("15.00"), policy.getConcessionPercentage());

        var famReq = com.schoolerp.modules.finance.dto.EvaluateFamilyConcessionRequest.builder()
                .familyId("FAM-SHARMA-01")
                .primaryGuardianPhone("+91-9820011223")
                .children(java.util.List.of(
                        com.schoolerp.modules.finance.dto.StudentSiblingInputDTO.builder()
                                .studentId(java.util.UUID.randomUUID())
                                .studentName("Aarav Sharma")
                                .dateOfBirth(java.time.LocalDate.of(2010, 5, 12))
                                .standardTuitionFee(new java.math.BigDecimal("40000.00"))
                                .build(),
                        com.schoolerp.modules.finance.dto.StudentSiblingInputDTO.builder()
                                .studentId(java.util.UUID.randomUUID())
                                .studentName("Ananya Sharma")
                                .dateOfBirth(java.time.LocalDate.of(2013, 8, 24))
                                .standardTuitionFee(new java.math.BigDecimal("40000.00"))
                                .build()
                ))
                .build();

        var eval = siblingConcessionService.evaluateFamily(famReq);
        assertNotNull(eval);
        assertEquals(2, eval.getTotalChildren());
        assertEquals(new java.math.BigDecimal("80000.00"), eval.getTotalOriginalTuitionFee());
        assertEquals(new java.math.BigDecimal("6000.00"), eval.getTotalConcessionsGranted()); // 15% of 40,000
        assertEquals(new java.math.BigDecimal("74000.00"), eval.getNetFamilyTuitionPayable());

        // Verify Rank 1 (Aarav, 0% discount) and Rank 2 (Ananya, 15% discount)
        assertEquals(1, eval.getSiblingConcessions().get(0).getSiblingRank());
        assertEquals(new java.math.BigDecimal("0.00"), eval.getSiblingConcessions().get(0).getDiscountAmount());
        assertEquals(2, eval.getSiblingConcessions().get(1).getSiblingRank());
        assertEquals(new java.math.BigDecimal("6000.00"), eval.getSiblingConcessions().get(1).getDiscountAmount());
    }

    @Test
    void testBiometricIoTDevicePunchSync() {
        var webhookReq = com.schoolerp.modules.attendance.dto.BiometricPunchWebhookRequest.builder()
                .deviceSerialNumber("eSSL-GATE-01")
                .rawIpAddress("192.168.1.150")
                .punches(java.util.List.of(
                        com.schoolerp.modules.attendance.dto.BiometricPunchPayloadItemDTO.builder()
                                .staffCode("TCH-2024-001")
                                .staffName("Dr. Rajesh Sharma")
                                .punchTime(java.time.LocalDateTime.of(2026, 11, 14, 8, 5, 0))
                                .punchType("IN")
                                .build(),
                        com.schoolerp.modules.attendance.dto.BiometricPunchPayloadItemDTO.builder()
                                .staffCode("TCH-2024-002")
                                .staffName("Ms. Pooja Hegde")
                                .punchTime(java.time.LocalDateTime.of(2026, 11, 14, 8, 45, 0))
                                .punchType("IN")
                                .build()
                ))
                .build();

        var syncResult = biometricSyncService.ingestBiometricPunches(webhookReq);
        assertNotNull(syncResult);
        assertEquals(2, syncResult.getTotalPunchesReceived());
        assertEquals(1, syncResult.getPresentCount());      // 08:05 is PRESENT
        assertEquals(1, syncResult.getLateArrivalCount());  // 08:45 is LATE_ARRIVAL

        var roster = biometricSyncService.getDailyBiometricSummary(java.time.LocalDate.of(2026, 11, 14));
        assertNotNull(roster);
        assertTrue(roster.getTotalPunches() >= 2);
    }

    @Test
    void testCreateAndToggleCustomDesignations() {
        var createReq = com.schoolerp.modules.staff.dto.CreateDesignationRequest.builder()
                .designationCode("ROBOTICS_COORDINATOR")
                .title("Robotics & AI Lab Coordinator")
                .department("Academics")
                .hierarchyTier(3)
                .reportsToDesignationCode("HOD_SCIENCE")
                .baseRole("ROLE_TEACHER")
                .financialApprovalLimit(new java.math.BigDecimal("75000.00"))
                .defaultCpcLevel("LEVEL_8")
                .build();

        var created = designationService.createCustomDesignation(createReq);
        assertNotNull(created);
        assertEquals("ROBOTICS_COORDINATOR", created.getDesignationCode());
        assertEquals("Robotics & AI Lab Coordinator", created.getTitle());
        assertTrue(created.isActive());

        // Toggle inactive (SaaS school deactivating an unwanted designation)
        var toggled = designationService.toggleDesignationStatus(created.getId(), false);
        assertNotNull(toggled);
        assertFalse(toggled.isActive());

        // Restore active
        var restored = designationService.toggleDesignationStatus(created.getId(), true);
        assertTrue(restored.isActive());

        // List active designations in Academics department
        var academicsList = designationService.getDesignations("Academics", true);
        assertNotNull(academicsList);
        assertTrue(academicsList.stream().anyMatch(d -> d.getDesignationCode().equals("ROBOTICS_COORDINATOR")));
    }

    @Test
    void testOrganizationalHierarchyAndApprovalThresholds() {
        // Retrieve full tree hierarchy
        var hierarchyTree = designationService.getOrganizationalHierarchyTree();
        assertNotNull(hierarchyTree);
        assertFalse(hierarchyTree.isEmpty());

        var root = hierarchyTree.get(0);
        assertEquals("PRINCIPAL", root.getDesignationCode());
        assertFalse(root.getSubordinates().isEmpty(), "Principal should have department heads reporting");

        // Validate approval threshold for a cashier / accountant
        var authReqWithinLimit = com.schoolerp.modules.staff.dto.CheckApprovalAuthorityRequest.builder()
                .employeeCode("EMP-2024-0042")
                .actionType("FEE_REFUND")
                .amount(new java.math.BigDecimal("15000.00"))
                .build();

        var resultWithin = designationService.validateApprovalAuthority(authReqWithinLimit);
        assertNotNull(resultWithin);

        var authReqExceeding = com.schoolerp.modules.staff.dto.CheckApprovalAuthorityRequest.builder()
                .employeeCode("EMP-2024-0042")
                .actionType("FEE_REFUND")
                .amount(new java.math.BigDecimal("250000.00"))
                .build();

        var resultExceeding = designationService.validateApprovalAuthority(authReqExceeding);
        assertNotNull(resultExceeding);
    }

    @Test
    void testPublishClassroomLessonMaterials() {
        var pubReq = com.schoolerp.modules.academics.dto.PublishLessonMaterialRequest.builder()
                .subjectName("Physics")
                .className("Grade 10")
                .sectionName("A")
                .chapterName("Chapter 10: Light - Reflection and Refraction")
                .topicName("Ray Diagrams for Concave & Convex Mirrors")
                .title("Class Slides & NCERT Solved Ray Diagrams")
                .description("Complete presentation slides presented in Period 2 with ray diagrams and mirror formulas")
                .materialType("PRESENTATION_SLIDES")
                .fileUrl("https://storage.apexacademy.edu.in/materials/physics-ch10-slides.pptx")
                .thumbnailUrl("https://storage.apexacademy.edu.in/thumbnails/physics-ch10.png")
                .fileSizeBytes(15728640L) // 15.0 MB
                .fileExtension("pptx")
                .lessonDate(java.time.LocalDate.now())
                .isDownloadable(true)
                .build();

        var published = lessonMaterialService.publishMaterial(pubReq, "Dr. Vikram Seth");
        assertNotNull(published);
        assertNotNull(published.getId());
        assertEquals("Physics", published.getSubjectName());
        assertEquals("Grade 10", published.getClassName());
        assertEquals("PRESENTATION_SLIDES", published.getMaterialType());
        assertEquals("15.0 MB", published.getFormattedFileSize());
        assertTrue(published.isDownloadable());
    }

    @Test
    void testStudentStudyLockerFeedAndEngagementLog() {
        // Publish e-book chapter
        var pubReq = com.schoolerp.modules.academics.dto.PublishLessonMaterialRequest.builder()
                .subjectName("Mathematics")
                .className("Grade 10")
                .sectionName("ALL")
                .chapterName("Chapter 4: Quadratic Equations")
                .topicName("Standard Form & Quadratic Formula Derivation")
                .title("NCERT Exemplar E-Book & Teacher Annotations")
                .description("Digital textbook excerpt with handwritten teacher notes")
                .materialType("E_BOOK_PDF")
                .fileUrl("https://storage.apexacademy.edu.in/materials/math-ch4-ebook.pdf")
                .fileSizeBytes(8388608L) // 8.0 MB
                .fileExtension("pdf")
                .lessonDate(java.time.LocalDate.now())
                .isDownloadable(true)
                .build();

        var material = lessonMaterialService.publishMaterial(pubReq, "Sunita Rao");
        assertNotNull(material);

        // Student fetches study locker class feed
        var feed = lessonMaterialService.getClassFeed("Grade 10", "A", "Mathematics", null);
        assertNotNull(feed);
        assertFalse(feed.isEmpty());
        assertTrue(feed.stream().anyMatch(m -> m.getId().equals(material.getId())));

        // Student accesses/reads the material
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);
        var accessReq = com.schoolerp.modules.academics.dto.RecordMaterialAccessRequest.builder()
                .materialId(material.getId())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .durationSeconds(420) // 7 minutes
                .isCompleted(true)
                .build();

        lessonMaterialService.recordStudentAccess(accessReq);

        // Teacher checks engagement report
        var report = lessonMaterialService.getMaterialEngagement(material.getId());
        assertNotNull(report);
        assertEquals(material.getId(), report.getMaterialId());
        assertTrue(report.getTotalViews() >= 1);
        assertEquals(1, report.getUniqueStudentsViewed());
        assertTrue(report.getAverageDurationSeconds() > 0);
        assertFalse(report.getStudentAccessList().isEmpty());
    }

    @Test
    void testInClassExitTicketsAndComprehensionHeatmap() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);

        var launchReq = com.schoolerp.modules.academics.dto.LaunchExitTicketRequest.builder()
                .className("Grade 10")
                .sectionName("A")
                .subjectName("Physics")
                .topicName("Refraction Index & Snell's Law")
                .questionText("What is the ratio of sin(i) to sin(r) called?")
                .questionType("MCQ")
                .options(java.util.List.of("Refractive Index", "Critical Angle", "Focal Length", "Dispersive Power"))
                .correctOption("Refractive Index")
                .build();

        var session = exitTicketService.launchSession(launchReq, "teacher-01", "Dr. Rao");
        assertNotNull(session);
        assertNotNull(session.getId());
        assertNotNull(session.getSessionPin());
        assertEquals("ACTIVE", session.getStatus());

        // Student submits response
        var submitReq = com.schoolerp.modules.academics.dto.SubmitExitTicketResponseRequest.builder()
                .sessionId(session.getId())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .selectedAnswer("Refractive Index")
                .confidenceScore(3) // Understood
                .studentNotes("Understood Snell's law concept clearly")
                .build();

        var submitResult = exitTicketService.submitResponse(submitReq);
        assertEquals("RESPONSE_SUBMITTED", submitResult);

        // Teacher checks real-time heatmap
        var heatmap = exitTicketService.getHeatmap(session.getId());
        assertNotNull(heatmap);
        assertEquals(1, heatmap.getTotalResponses());
        assertEquals(1, heatmap.getUnderstoodCount());
        assertEquals(100.0, heatmap.getComprehensionRatePercentage());
        assertEquals("EXCELLENT", heatmap.getComprehensionLevel());

        // Close session
        var closed = exitTicketService.closeSession(session.getId());
        assertEquals("CLOSED", closed.getStatus());
    }

    @Test
    void testStudentLearningPortfolioAndParentClap() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);

        var uploadReq = com.schoolerp.modules.students.dto.SubmitPortfolioArtifactRequest.builder()
                .studentId(student.getId())
                .studentName(student.getFullName())
                .className("Grade 10")
                .title("Automated Solar Tracker Working Model")
                .description("Dual-axis LDR servo motorized solar panel tracker for physics science exhibition")
                .artifactType("PROJECT_PHOTO")
                .artifactUrl("https://storage.apexacademy.edu.in/portfolio/solar-tracker-model.jpg")
                .thumbnailUrl("https://storage.apexacademy.edu.in/portfolio/solar-tracker-thumb.jpg")
                .competencyCategory("SCIENTIFIC_INQUIRY")
                .build();

        var artifact = learningPortfolioService.uploadArtifact(uploadReq);
        assertNotNull(artifact);
        assertNotNull(artifact.getId());
        assertEquals("SCIENTIFIC_INQUIRY", artifact.getCompetencyCategory());
        assertEquals(0, artifact.getParentClapsCount());

        // Teacher reviews artifact
        var reviewReq = com.schoolerp.modules.students.dto.ReviewPortfolioArtifactRequest.builder()
                .teacherRemarks("Exemplary circuit design and soldering precision. Demonstrated deep understanding of photovoltaics.")
                .rubricGradeBand("EXEMPLARY")
                .isFeaturedShowcase(true)
                .build();

        var reviewed = learningPortfolioService.reviewArtifact(artifact.getId(), reviewReq, "Dr. Vikram Seth");
        assertNotNull(reviewed);
        assertEquals("EXEMPLARY", reviewed.getRubricGradeBand());
        assertTrue(reviewed.isFeaturedShowcase());

        // Parent claps for the child
        int claps = learningPortfolioService.recordParentClap(artifact.getId());
        assertEquals(1, claps);

        // Verify retrieval
        var studentPortfolio = learningPortfolioService.getStudentPortfolio(student.getId());
        assertFalse(studentPortfolio.isEmpty());

        var featured = learningPortfolioService.getFeaturedShowcase();
        assertFalse(featured.isEmpty());
    }

    @Test
    void testAdaptiveRemedialAndEnrichmentStudyPlans() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);

        // 1. Remedial Plan (Score < 50%)
        var remedialReq = com.schoolerp.modules.academics.dto.GenerateStudyPlanRequest.builder()
                .studentId(student.getId())
                .studentName(student.getFullName())
                .className("Grade 10")
                .subjectName("Mathematics")
                .topicOrChapter("Trigonometric Identities")
                .assessmentScoreTrigger(42.0)
                .build();

        var remedialPlan = adaptiveStudyPlanService.generateAdaptivePlan(remedialReq);
        assertNotNull(remedialPlan);
        assertEquals("REMEDIAL", remedialPlan.getPlanType());
        assertTrue(remedialPlan.getRecommendedMaterialsSummary().contains("Foundational"));
        assertEquals("ASSIGNED", remedialPlan.getStatus());

        // Progress update
        var updateReq = com.schoolerp.modules.academics.dto.UpdateStudyPlanProgressRequest.builder()
                .completionPercentage(100.0)
                .status("COMPLETED")
                .studentNotes("Completed foundational axioms and re-attempted 3 practice proofs.")
                .build();

        var updated = adaptiveStudyPlanService.updateProgress(remedialPlan.getId(), updateReq);
        assertEquals("COMPLETED", updated.getStatus());
        assertEquals(100.0, updated.getCompletionPercentage());

        // 2. Enrichment Plan (Score >= 85%)
        var enrichReq = com.schoolerp.modules.academics.dto.GenerateStudyPlanRequest.builder()
                .studentId(student.getId())
                .studentName(student.getFullName())
                .className("Grade 10")
                .subjectName("Physics")
                .topicOrChapter("Electromagnetic Induction")
                .assessmentScoreTrigger(94.5)
                .build();

        var enrichPlan = adaptiveStudyPlanService.generateAdaptivePlan(enrichReq);
        assertNotNull(enrichPlan);
        assertEquals("ENRICHMENT", enrichPlan.getPlanType());
        assertTrue(enrichPlan.getRecommendedMaterialsSummary().contains("Olympiad"));
    }

    @Test
    void testClassroomMicroBadgesAndHousePointsCredit() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);

        var badgeReq = com.schoolerp.modules.students.dto.AwardBadgeRequest.builder()
                .studentId(student.getId())
                .studentName(student.getFullName())
                .className("Grade 10")
                .sectionName("Section A")
                .badgeCode("DEEP_THINKER")
                .houseName("Mars (Red)")
                .teacherNotes("Asked a penetrating question connecting Faraday's Law to modern maglev trains")
                .build();

        var badge = classroomBadgeService.awardBadge(badgeReq, "Sunita Rao");
        assertNotNull(badge);
        assertNotNull(badge.getId());
        assertEquals("DEEP_THINKER", badge.getBadgeCode());
        assertEquals(20, badge.getHousePointsAwarded());
        assertTrue(badge.getBadgeTitle().contains("Deep Conceptual Inquiry"));

        // Check student summary
        var summary = classroomBadgeService.getStudentBadgeSummary(student.getId());
        assertNotNull(summary);
        assertTrue(summary.getTotalBadgesCount() >= 1);
        assertTrue(summary.getTotalHousePointsContributed() >= 20);
    }

    @Test
    void testProjectBasedLearningMilestoneWorkflow() {
        // Teacher creates PBL Project
        var projReq = com.schoolerp.modules.academics.dto.CreatePblProjectRequest.builder()
                .title("Clean Energy Smart Microgrid Design")
                .subjectName("Physics & Environmental Science")
                .gradeLevel("Grade 10")
                .description("Design a rooftop solar-hybrid microgrid with battery storage for a model rural health clinic")
                .totalMilestones(4)
                .scoringRubric("Proposal (25), Schematic & Math (25), Working Model (25), Viva Defense (25)")
                .dueDate(java.time.LocalDate.now().plusDays(30))
                .build();

        var project = pblProjectService.createProject(projReq, "Dr. Vikram Seth");
        assertNotNull(project);
        assertNotNull(project.getId());
        assertEquals(4, project.getTotalMilestones());

        // Group submits Milestone 1
        var subReq = com.schoolerp.modules.academics.dto.SubmitMilestoneRequest.builder()
                .projectId(project.getId())
                .groupName("Eco Innovators")
                .groupLeaderName("Aarav Sharma")
                .memberNames("Aarav Sharma, Rhea Patel, Neil Sen")
                .milestoneNumber(1)
                .milestoneTitle("Clinic Load Calculation & System Proposal")
                .submissionUrl("https://storage.apexacademy.edu.in/pbl/eco-innovators-m1-proposal.pdf")
                .build();

        var submission = pblProjectService.submitMilestone(subReq);
        assertNotNull(submission);
        assertEquals(1, submission.getMilestoneNumber());
        assertEquals("SUBMITTED", submission.getStatus());

        // Teacher grades Milestone 1
        var gradeReq = com.schoolerp.modules.academics.dto.GradeMilestoneRequest.builder()
                .teacherScore(24.5)
                .maxScore(25.0)
                .peerContributionRating(98.0)
                .status("APPROVED")
                .teacherFeedback("Comprehensive load estimation. Excellent consideration of critical medical equipment backup.")
                .build();

        var graded = pblProjectService.gradeMilestone(submission.getId(), gradeReq, "Dr. Vikram Seth");
        assertNotNull(graded);
        assertEquals(24.5, graded.getTeacherScore());
        assertEquals("APPROVED", graded.getStatus());
        assertEquals(98.0, graded.getPeerContributionRating());

        // Verify retrieval
        var submissions = pblProjectService.getProjectSubmissions(project.getId(), "Eco Innovators");
        assertFalse(submissions.isEmpty());
        assertEquals(1, submissions.get(0).getMilestoneNumber());
    }

    @Test
    void testEmergencyCampusModePivotAndCompleteShutdown() {
        // 1. Pivot to Virtual Emergency Campus (Cyclone / Red Rain alert)
        var pivotReq = com.schoolerp.modules.institution.dto.DeclareEmergencyModeRequest.builder()
                .operatingMode("VIRTUAL_EMERGENCY_CAMPUS")
                .emergencyReason("CYCLONE_STORM_ALERT")
                .effectiveDate(java.time.LocalDate.now())
                .autoNotifyParents(true)
                .suspendTransportAlerts(true)
                .freezeLateFeesAndDeadlines(false)
                .govtOrderReference("DM/MUMBAI/CYCLONE/2026/08")
                .closureNoticeBulletin("All physical classes suspended due to Cyclone alert. Online classes active.")
                .build();

        var declared = emergencyCampusModeService.declareEmergencyMode(pivotReq, "Dr. Rajesh Sharma (Principal)");
        assertNotNull(declared);
        assertEquals("VIRTUAL_EMERGENCY_CAMPUS", declared.getOperatingMode());
        assertTrue(declared.isSuspendTransportAlerts());
        assertTrue(declared.getActiveVirtualRoomsCount() > 0);

        // Verify status
        var status = emergencyCampusModeService.getCurrentStatus();
        assertEquals("VIRTUAL_EMERGENCY_CAMPUS", status.getOperatingMode());

        // 2. Worst-Case Disaster Pivot: Complete Emergency Shutdown
        var shutdownReq = com.schoolerp.modules.institution.dto.DeclareEmergencyModeRequest.builder()
                .operatingMode("COMPLETE_EMERGENCY_SHUTDOWN")
                .emergencyReason("CATASTROPHIC_DISASTER")
                .effectiveDate(java.time.LocalDate.now())
                .autoNotifyParents(true)
                .suspendTransportAlerts(true)
                .freezeLateFeesAndDeadlines(true)
                .govtOrderReference("STATE/DISASTER/MGMT/ACT/2026/01")
                .closureNoticeBulletin("Severe regional earthquake and blackout. All school operations, deadlines and fees frozen. Relief desk active.")
                .build();

        var shutdown = emergencyCampusModeService.declareEmergencyMode(shutdownReq, "Board Chairman");
        assertNotNull(shutdown);
        assertEquals("COMPLETE_EMERGENCY_SHUTDOWN", shutdown.getOperatingMode());
        assertTrue(shutdown.isFreezeLateFeesAndDeadlines());

        // 3. Restore Normal Physical Campus
        var restored = emergencyCampusModeService.restorePhysicalCampus("Dr. Rajesh Sharma (Principal)");
        assertEquals("PHYSICAL_CAMPUS", restored.getOperatingMode());
        assertFalse(restored.isSuspendTransportAlerts());
    }

    @Test
    void testEmergencyWelfareSafetyCheckInRegistry() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);

        // Student Safe at Home
        var checkIn1 = com.schoolerp.modules.institution.dto.SubmitSafetyCheckInRequest.builder()
                .userId(student.getId())
                .userName(student.getFullName())
                .userRole("STUDENT")
                .safetyStatus("SAFE_AT_HOME")
                .currentLocation("Powai, Mumbai")
                .emergencyContactPhone("+91 98201 11223")
                .assistanceDetails("Safe with family; power inverter running")
                .build();

        var res1 = emergencyCampusModeService.submitSafetyCheckIn(checkIn1);
        assertNotNull(res1);
        assertEquals("SAFE_AT_HOME", res1.getSafetyStatus());

        // Staff Needing Urgent Assistance
        var checkIn2 = com.schoolerp.modules.institution.dto.SubmitSafetyCheckInRequest.builder()
                .userId(java.util.UUID.randomUUID())
                .userName("Mr. Ranveer Singh")
                .userRole("STAFF")
                .safetyStatus("NEED_URGENT_ASSISTANCE")
                .currentLocation("Sector 4, Ground Floor Flooded")
                .emergencyContactPhone("+91 98205 99881")
                .assistanceDetails("Water level rising above 3 feet. Require emergency evacuation boat.")
                .build();

        var res2 = emergencyCampusModeService.submitSafetyCheckIn(checkIn2);
        assertNotNull(res2);
        assertEquals("NEED_URGENT_ASSISTANCE", res2.getSafetyStatus());

        // Summary
        var summary = emergencyCampusModeService.getSafetyCheckInSummary();
        assertNotNull(summary);
        assertTrue(summary.getTotalCheckIns() >= 2);
        assertTrue(summary.getNeedUrgentAssistanceCount() >= 1);
        assertFalse(summary.getUrgentCases().isEmpty());
    }

    @Test
    void testAutoGenerateVirtualClassroomsAndSessionLifecycle() {
        var periods = java.util.List.of(
                com.schoolerp.modules.academics.dto.AutoGenerateVirtualSessionsRequest.PeriodScheduleItemDTO.builder()
                        .periodNumber(1)
                        .subjectName("Mathematics")
                        .teacherName("Sunita Rao")
                        .startTime("09:00")
                        .endTime("09:45")
                        .build(),
                com.schoolerp.modules.academics.dto.AutoGenerateVirtualSessionsRequest.PeriodScheduleItemDTO.builder()
                        .periodNumber(2)
                        .subjectName("Physics")
                        .teacherName("Dr. Vikram Seth")
                        .startTime("10:00")
                        .endTime("10:45")
                        .build()
        );

        var req = com.schoolerp.modules.academics.dto.AutoGenerateVirtualSessionsRequest.builder()
                .className("Grade 10")
                .sectionName("A")
                .meetingProvider("JITSI_WEBRTC")
                .sessionDate(java.time.LocalDate.now())
                .periods(periods)
                .build();

        var generated = virtualClassroomService.autoGenerateDailySessions(req);
        assertNotNull(generated);
        assertEquals(2, generated.size());
        assertTrue(generated.get(0).getJoinMeetingUrl().contains("meet.jit.si"));
        assertEquals("SCHEDULED", generated.get(0).getSessionStatus());

        // Start Period 1
        var p1 = generated.get(0);
        var started = virtualClassroomService.startSession(p1.getId());
        assertEquals("LIVE_IN_PROGRESS", started.getSessionStatus());
        assertNotNull(started.getStartedAt());

        // End Period 1 with recording
        var ended = virtualClassroomService.endSession(p1.getId(), "https://storage.apexacademy.edu.in/recordings/math-g10a-p1.mp4");
        assertEquals("CONCLUDED", ended.getSessionStatus());
        assertNotNull(ended.getRecordingUrl());

        // Feed retrieval
        var feed = virtualClassroomService.getStudentDailyFeed("Grade 10", "A", java.time.LocalDate.now());
        assertFalse(feed.isEmpty());
    }

    @Test
    void testVirtualAttendanceTelemetryAndAsyncCatchupNormalization() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);

        // Generate a test virtual session
        var periods = java.util.List.of(
                com.schoolerp.modules.academics.dto.AutoGenerateVirtualSessionsRequest.PeriodScheduleItemDTO.builder()
                        .periodNumber(3)
                        .subjectName("Chemistry")
                        .teacherName("Ms. Kapoor")
                        .startTime("11:00")
                        .endTime("11:45")
                        .build()
        );
        var genReq = com.schoolerp.modules.academics.dto.AutoGenerateVirtualSessionsRequest.builder()
                .className("Grade 10")
                .sectionName("A")
                .meetingProvider("JITSI_WEBRTC")
                .sessionDate(java.time.LocalDate.now())
                .periods(periods)
                .build();
        var sessions = virtualClassroomService.autoGenerateDailySessions(genReq);
        var session = sessions.get(0);

        // 1. Log Virtual Presence >= 70% duration -> PRESENT_VIRTUAL
        var presReq = com.schoolerp.modules.attendance.dto.LogVirtualPresenceRequest.builder()
                .sessionId(session.getId())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .className("Grade 10")
                .sessionScheduledDurationMinutes(45)
                .activeDurationMinutes(38) // >70%
                .connectionHealthScore(95)
                .build();

        var telemetry = virtualAttendanceService.logPresence(presReq);
        assertNotNull(telemetry);
        assertEquals("PRESENT_VIRTUAL", telemetry.getAttendanceStatus());
        assertEquals(38, telemetry.getActiveDurationMinutes());

        // Check Roster
        var roster = virtualAttendanceService.getSessionAttendanceRoster(session.getId());
        assertNotNull(roster);
        assertEquals(1, roster.getTotalAttendees());
        assertEquals(1, roster.getPresentVirtualCount());

        // 2. Student Submits Asynchronous Catch-Up (e.g. for low-bandwidth student)
        var catchupReq = com.schoolerp.modules.academics.dto.SubmitAsyncCatchupRequest.builder()
                .sessionId(session.getId())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .materialReviewedTitle("Class Recording & Chemical Bonding Notes")
                .proofOfLearningResponseText("Watched complete 35-min lecture recording. Answered quiz on ionic vs covalent dipole moments.")
                .quizScore(95.0)
                .build();

        var catchup = virtualClassroomService.submitAsyncCatchup(catchupReq);
        assertNotNull(catchup);
        assertEquals("SUBMITTED", catchup.getStatus());

        // Teacher verifies catchup -> Normalizes attendance
        var verified = virtualClassroomService.verifyAsyncCatchup(catchup.getId(), "Ms. Kapoor");
        assertEquals("VERIFIED_ATTENDANCE_NORMALIZED", verified.getStatus());
        assertNotNull(verified.getVerifiedByTeacherName());
    }

    @Test
    void testDynamicCustomFieldDefinitionsAndValues() {
        var student = studentRepository.findAllByTenantId("tenant_apex_mumbai_01").get(0);

        // 1. Create Custom Field Definition on STUDENT entity
        var fieldDefReq = com.schoolerp.modules.system.dto.CreateCustomFieldDefinitionRequest.builder()
                .targetEntityType("STUDENT")
                .fieldCode("swimming_belt_level")
                .fieldLabel("Swimming Belt Level")
                .fieldType("DROPDOWN")
                .options(java.util.List.of("Beginner White", "Intermediate Blue", "Advanced Gold"))
                .defaultValue("Beginner White")
                .isMandatory(false)
                .displayOrder(1)
                .build();

        var fieldDef = customExtensibilityService.createFieldDefinition(fieldDefReq);
        assertNotNull(fieldDef);
        assertNotNull(fieldDef.getId());
        assertEquals("swimming_belt_level", fieldDef.getFieldCode());
        assertEquals("DROPDOWN", fieldDef.getFieldType());

        // 2. Save Custom Field Values for Student
        var saveValReq = com.schoolerp.modules.system.dto.SaveCustomFieldValueRequest.builder()
                .targetEntityType("STUDENT")
                .targetEntityId(student.getId())
                .fieldValues(java.util.Map.of("swimming_belt_level", "Advanced Gold"))
                .build();

        customExtensibilityService.saveCustomFieldValues(saveValReq);

        // 3. Retrieve Entity Custom Fields
        var response = customExtensibilityService.getEntityCustomFields("STUDENT", student.getId());
        assertNotNull(response);
        assertEquals("Advanced Gold", response.getCustomFieldValues().get("swimming_belt_level"));
        assertTrue(response.getFieldMetadata().containsKey("swimming_belt_level"));
    }

    @Test
    void testUniversalCustomEntityCreationAndDynamicRecordsLifecycle() {
        // 1. Define completely new custom business entity: MUN_DELEGATION
        var schemaFields = java.util.List.<java.util.Map<String, Object>>of(
                java.util.Map.of("name", "countryName", "type", "TEXT", "label", "Country Assigned", "required", true),
                java.util.Map.of("name", "committee", "type", "DROPDOWN", "label", "UN Committee", "options", java.util.List.of("Security Council", "UNHRC", "DISEC")),
                java.util.Map.of("name", "delegateStudentName", "type", "TEXT", "label", "Delegate Name", "required", true),
                java.util.Map.of("name", "positionPaperSubmitted", "type", "BOOLEAN", "label", "Position Paper Submitted", "required", false)
        );

        var entityDefReq = com.schoolerp.modules.system.dto.CreateCustomEntityDefinitionRequest.builder()
                .entityCode("MUN_DELEGATION")
                .displayName("Model UN Country Delegation")
                .pluralName("Model UN Delegations")
                .description("Model United Nations delegation allocations, draft resolutions, and position papers")
                .category("ACTIVITIES")
                .iconName("Globe")
                .fieldsSchema(schemaFields)
                .build();

        var entityDef = customExtensibilityService.createEntityDefinition(entityDefReq);
        assertNotNull(entityDef);
        assertEquals("MUN_DELEGATION", entityDef.getEntityCode());
        assertEquals("ACTIVITIES", entityDef.getCategory());

        // 2. Create Dynamic Record Instance
        var recordData = java.util.Map.<String, Object>of(
                "countryName", "United States of America",
                "committee", "Security Council",
                "delegateStudentName", "Aarav Sharma",
                "positionPaperSubmitted", true
        );

        var createRecReq = com.schoolerp.modules.system.dto.CreateCustomEntityRecordRequest.builder()
                .recordTitle("USA - UN Security Council")
                .data(recordData)
                .build();

        var record = customExtensibilityService.createEntityRecord("MUN_DELEGATION", createRecReq, "Dr. Vikram Seth");
        assertNotNull(record);
        assertNotNull(record.getId());
        assertEquals("USA - UN Security Council", record.getRecordTitle());
        assertEquals("ACTIVE", record.getStatus());
        assertEquals("United States of America", record.getData().get("countryName"));

        // 3. Query records
        var list = customExtensibilityService.listEntityRecords("MUN_DELEGATION", "ACTIVE");
        assertFalse(list.isEmpty());
        assertTrue(list.stream().anyMatch(r -> r.getId().equals(record.getId())));

        // 4. Update record
        var updateReq = com.schoolerp.modules.system.dto.UpdateCustomEntityRecordRequest.builder()
                .recordTitle("USA - UN Security Council (Draft Resolution Sponsor)")
                .status("ACTIVE")
                .data(java.util.Map.of("countryName", "United States of America", "committee", "Security Council", "resolutionDraftNo", "UNSC-RES-2026-04"))
                .build();

        var updated = customExtensibilityService.updateEntityRecord("MUN_DELEGATION", record.getId(), updateReq);
        assertEquals("USA - UN Security Council (Draft Resolution Sponsor)", updated.getRecordTitle());

        // 5. Delete (Archive) record
        customExtensibilityService.deleteEntityRecord("MUN_DELEGATION", record.getId());
        var archivedRec = customExtensibilityService.getEntityRecord("MUN_DELEGATION", record.getId());
        assertEquals("ARCHIVED", archivedRec.getStatus());
    }

    @Test
    void testTenantOutboundWebhookSubscriptionAndDispatch() {
        // 1. Create Webhook Subscription
        var webhookReq = com.schoolerp.modules.system.dto.CreateWebhookSubscriptionRequest.builder()
                .webhookName("Zapier External MUN & Events Pipeline")
                .targetUrl("https://hooks.zapier.com/hooks/catch/98201/schoolerp-events/")
                .eventTopic("CUSTOM_ENTITY_RECORD_CREATED")
                .secretKey("whsec_apex_mun_secret_key_8829")
                .build();

        var webhook = customExtensibilityService.createWebhook(webhookReq);
        assertNotNull(webhook);
        assertNotNull(webhook.getId());
        assertEquals("CUSTOM_ENTITY_RECORD_CREATED", webhook.getEventTopic());
        assertTrue(webhook.isActive());

        // 2. Test Ping Dispatch
        var pingResult = customExtensibilityService.testPingWebhook(webhook.getId());
        assertNotNull(pingResult);
        assertEquals("SUCCESS_200", pingResult.getDeliveryStatus());
        assertEquals(200, pingResult.getHttpStatusCode());
        assertNotNull(pingResult.getHmacSignature());
    }

    @Test
    void testArgon2idCryptographicPasswordEncoder() {
        String rawPassword = "ApexSchoolEnterprise2026!";
        String encodedHash = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedHash);
        assertTrue(encodedHash.startsWith("$argon2id$"), "Password hash must follow Argon2id standard format");
        assertTrue(passwordEncoder.matches(rawPassword, encodedHash), "Valid password must verify against Argon2id hash");
        assertFalse(passwordEncoder.matches("WrongPassword!123", encodedHash), "Invalid password must fail Argon2id check");
    }

    @Test
    void testEnterpriseSpringCachingEngine() {
        assertNotNull(cacheManager);
        var cache = cacheManager.getCache("tenantLicenses");
        assertNotNull(cache, "tenantLicenses cache must be configured");

        // Verify module lookup
        boolean isEnabledFirst = licenseService.isModuleEnabled("tenant_apex_mumbai_01", "CORE_ACADEMICS");
        assertTrue(isEnabledFirst);

        // Verify cache entry is populated
        var cachedVal = cache.get("tenant_apex_mumbai_01_CORE_ACADEMICS");
        assertNotNull(cachedVal, "Cache should store the module license boolean");
        assertEquals(Boolean.TRUE, cachedVal.get());

        // Verify feature flag caching
        var featCache = cacheManager.getCache("tenantFeatures");
        assertNotNull(featCache, "tenantFeatures cache must be configured");
        boolean isFeatEnabled = featureFlagService.isFeatureEnabled("transport.live_gps_telemetry");
        assertNotNull(featCache.get("transport.live_gps_telemetry"));
    }

    @Test
    void testPaginationAuditLogsAndStudentDirectories() {
        // 1. Test Paged Audit Logs
        var pagedLogs = auditLogService.getPagedLogs(0, 5);
        assertNotNull(pagedLogs);
        assertEquals(0, pagedLogs.getNumber());
        assertTrue(pagedLogs.getSize() <= 5);
        assertTrue(pagedLogs.getTotalElements() > 0);
        assertFalse(pagedLogs.getContent().isEmpty());

        // 2. Test Paged Students Directory
        var pagedStudents = studentService.getPagedStudents(0, 3);
        assertNotNull(pagedStudents);
        assertEquals(0, pagedStudents.getNumber());
        assertTrue(pagedStudents.getSize() <= 3);
        assertTrue(pagedStudents.getTotalElements() > 0);
        assertFalse(pagedStudents.getContent().isEmpty());
    }

    @Test
    void testExpressWalkInIntakeAndSpotFeeCollection() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");

        // 1. Rapid Walk-In Desk Registration
        var intakeReq = com.schoolerp.modules.admissions.dto.RapidWalkInIntakeRequest.builder()
                .parentName("Vikram Malhotra")
                .parentPhone("+91-9876543210")
                .parentEmail("vikram.malhotra@apex.edu")
                .studentName("Aarav Malhotra")
                .gradeApplyingFor("Grade 6")
                .academicYear("2026-2027")
                .hasEnrolledSibling(true)
                .proximityUnderFiveKm(true)
                .assignedCounselorName("Pooja Sharma")
                .initialNotes("Arrived at 10 AM. Interested in STEM robotics club.")
                .build();

        var inquiry = admissionsCrmService.rapidWalkInIntake("tenant_apex_mumbai_01", intakeReq);
        assertNotNull(inquiry);
        assertNotNull(inquiry.getId());
        assertTrue(inquiry.getInquiryNumber().startsWith("INQ-2026-W"));
        assertTrue(inquiry.getTokenNumber().startsWith("TOKEN-W"));
        assertEquals("INQUIRY_RECEIVED", inquiry.getPipelineStage());
        assertEquals("WALK_IN", inquiry.getSourceChannel());
        assertTrue(inquiry.getLeadScore() >= 80, "Walk-in lead score with sibling and proximity should be >= 80");
        assertEquals(BigDecimal.ZERO, inquiry.getSpotFeePaidAmount());

        // 2. Spot Prospectus Fee Collection
        var spotFeeReq = com.schoolerp.modules.admissions.dto.CollectSpotFeeRequest.builder()
                .amount(new BigDecimal("1500.00"))
                .paymentMethod("UPI_QR")
                .remarks("Walk-in registration prospectus charge")
                .build();

        var updatedInquiry = admissionsCrmService.collectSpotFee("tenant_apex_mumbai_01", inquiry.getId(), spotFeeReq);
        assertNotNull(updatedInquiry);
        assertEquals(new BigDecimal("1500.00"), updatedInquiry.getSpotFeePaidAmount());
        assertNotNull(updatedInquiry.getSpotFeeReceiptNumber());
        assertTrue(updatedInquiry.getSpotFeeReceiptNumber().startsWith("REC-2026-"));
        assertEquals("APPLICATION_SUBMITTED", updatedInquiry.getPipelineStage());
        assertTrue(updatedInquiry.getCounselorNotes().contains("Spot Fee Paid: Rs.1500.00 via UPI_QR"));
    }

    @Test
    void testAdmissionsInterviewSlotBookingAndEvaluation() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");

        // 1. Create Inquiry
        var inqReq = com.schoolerp.modules.admissions.dto.CreateInquiryRequest.builder()
                .parentName("Anita Sharma")
                .parentPhone("+91-9876500001")
                .parentEmail("anita.sharma@apex.edu")
                .studentName("Rhea Sharma")
                .gradeApplyingFor("Grade 8")
                .academicYear("2026-2027")
                .sourceChannel("WEBSITE_FORM")
                .hasEnrolledSibling(false)
                .proximityUnderFiveKm(true)
                .assignedCounselorName("Deepak Gupta")
                .counselorNotes("Campus tour requested")
                .build();

        var inq = admissionsCrmService.createInquiry("tenant_apex_mumbai_01", inqReq);
        assertNotNull(inq);

        // 2. Book Interview Slot
        var slotReq = com.schoolerp.modules.admissions.dto.BookInterviewSlotRequest.builder()
                .inquiryId(inq.getId())
                .slotType("CAMPUS_TOUR")
                .scheduledAt(java.time.LocalDateTime.now().plusDays(2))
                .interviewerName("Mr. Rajan Verma")
                .venueOrRoom("STEM Labs & Library")
                .build();

        var slot = admissionsCrmService.bookInterviewSlot("tenant_apex_mumbai_01", slotReq);
        assertNotNull(slot);
        assertNotNull(slot.getId());
        assertEquals("CAMPUS_TOUR", slot.getSlotType());
        assertEquals("SCHEDULED", slot.getStatus());

        // Verify inquiry stage advanced
        var inqAfterSlot = admissionsCrmService.getInquiryById("tenant_apex_mumbai_01", inq.getId());
        assertEquals("CAMPUS_TOUR_BOOKED", inqAfterSlot.getPipelineStage());

        // 3. Evaluate Slot Rubric
        var evalReq = com.schoolerp.modules.admissions.dto.EvaluateInterviewSlotRequest.builder()
                .status("COMPLETED")
                .evaluationRating(5)
                .interviewerFeedback("Exceptional curiosity and articulate responses.")
                .recommendedGrade("Grade 8")
                .build();

        var evaluatedSlot = admissionsCrmService.evaluateInterviewSlot("tenant_apex_mumbai_01", slot.getId(), evalReq);
        assertNotNull(evaluatedSlot);
        assertEquals("COMPLETED", evaluatedSlot.getStatus());
        assertEquals(5, evaluatedSlot.getEvaluationRating());
        assertEquals("Exceptional curiosity and articulate responses.", evaluatedSlot.getInterviewerFeedback());
    }

    @Test
    void testMeritOfferIssuanceAndStudentConversion() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");

        // 1. Create Inquiry and Pay Spot Fee
        var walkInReq = com.schoolerp.modules.admissions.dto.RapidWalkInIntakeRequest.builder()
                .parentName("Suresh Nair")
                .parentPhone("+91-9876599999")
                .parentEmail("suresh.nair@apex.edu")
                .studentName("Ananya Nair")
                .gradeApplyingFor("Grade 5")
                .academicYear("2026-2027")
                .hasEnrolledSibling(true)
                .proximityUnderFiveKm(true)
                .assignedCounselorName("Rohan Mehta")
                .initialNotes("Walk-in registration at admission desk")
                .build();

        var inq = admissionsCrmService.rapidWalkInIntake("tenant_apex_mumbai_01", walkInReq);
        admissionsCrmService.collectSpotFee("tenant_apex_mumbai_01", inq.getId(),
                com.schoolerp.modules.admissions.dto.CollectSpotFeeRequest.builder()
                        .amount(new BigDecimal("1500.00"))
                        .paymentMethod("UPI_QR")
                        .remarks("Prospectus fee")
                        .build());

        // 2. Issue Merit Offer
        var offerReq = com.schoolerp.modules.admissions.dto.IssueOfferRequest.builder()
                .inquiryId(inq.getId())
                .tuitionFeeTotal(new BigDecimal("80000.00"))
                .scholarshipOrConcessionAmount(new BigDecimal("10000.00"))
                .validUntilDate(java.time.LocalDate.now().plusDays(14))
                .build();

        var offer = admissionsCrmService.issueMeritOffer("tenant_apex_mumbai_01", offerReq);
        assertNotNull(offer);
        assertNotNull(offer.getId());
        assertTrue(offer.getOfferNumber().startsWith("OFFER-2026-"));
        assertEquals(new BigDecimal("70000.00"), offer.getNetAdmissionPayable());
        assertEquals("ISSUED", offer.getOfferStatus());

        var inqWithOffer = admissionsCrmService.getInquiryById("tenant_apex_mumbai_01", inq.getId());
        assertEquals("OFFER_ISSUED", inqWithOffer.getPipelineStage());

        // 3. 1-Click Convert Offer to Active Student & Invoice
        var converted = admissionsCrmService.convertToActiveStudent("tenant_apex_mumbai_01", offer.getId());
        assertNotNull(converted);
        assertEquals("CONVERTED_TO_STUDENT", converted.getOfferStatus());
        assertNotNull(converted.getConvertedStudentId());
        assertNotNull(converted.getConvertedInvoiceId());
        assertNotNull(converted.getAcceptedAt());

        // Verify Student in Database
        var studentOpt = studentRepository.findById(converted.getConvertedStudentId());
        assertTrue(studentOpt.isPresent());
        assertEquals("Ananya Nair", studentOpt.get().getFullName());
        assertEquals("Grade 5", studentOpt.get().getClassName());
        assertEquals("Active", studentOpt.get().getStatus());

        // Verify Invoice in Database
        var invoiceOpt = feeInvoiceRepository.findById(converted.getConvertedInvoiceId());
        assertTrue(invoiceOpt.isPresent());
        assertEquals(new BigDecimal("70000.00"), invoiceOpt.get().getTotalAmount());
        assertEquals(new BigDecimal("1500.00"), invoiceOpt.get().getPaidAmount());
        assertEquals(new BigDecimal("68500.00"), invoiceOpt.get().getBalanceAmount());

        // Verify Inquiry Enrolled Status
        var finalInquiry = admissionsCrmService.getInquiryById("tenant_apex_mumbai_01", inq.getId());
        assertEquals("ENROLLED", finalInquiry.getPipelineStage());
        assertEquals(100, finalInquiry.getLeadScore());

        // 4. Verify Funnel Analytics
        var funnel = admissionsCrmService.getFunnelAnalytics("tenant_apex_mumbai_01");
        assertNotNull(funnel);
        assertTrue(funnel.getTotalInquiries() >= 2);
        assertTrue(funnel.getWalkInCount() >= 2);
        assertTrue(funnel.getSpotFeesCollectedTotal().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(funnel.getStudentsEnrolledCount() >= 1);
        assertTrue(funnel.getConversionRatePercent() > 0.0);
    }

    @Test
    void testParentDocumentChecklistUploadAndVerificationWorkflow() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");

        // 1. Create Inquiry
        var inq = admissionsCrmService.rapidWalkInIntake("tenant_apex_mumbai_01",
                com.schoolerp.modules.admissions.dto.RapidWalkInIntakeRequest.builder()
                        .parentName("Karan Kapoor")
                        .parentPhone("+91-9822012345")
                        .parentEmail("karan.kapoor@apex.edu")
                        .studentName("Dev Kapoor")
                        .gradeApplyingFor("Grade 1")
                        .academicYear("2026-2027")
                        .build());

        // 2. Upload Checklist Documents
        var birthDoc = admissionsCrmService.uploadChecklistDocument("tenant_apex_mumbai_01", inq.getId(),
                com.schoolerp.modules.admissions.dto.UploadDocumentRequest.builder()
                        .documentType("BIRTH_CERTIFICATE")
                        .documentName("Dev_Kapoor_BirthCert.pdf")
                        .storageUrl("https://s3.ap-south-1.amazonaws.com/apex-admissions/birth_cert.pdf")
                        .build());

        var tcDoc = admissionsCrmService.uploadChecklistDocument("tenant_apex_mumbai_01", inq.getId(),
                com.schoolerp.modules.admissions.dto.UploadDocumentRequest.builder()
                        .documentType("TRANSFER_CERTIFICATE")
                        .documentName("Dev_Kapoor_TC.pdf")
                        .storageUrl("https://s3.ap-south-1.amazonaws.com/apex-admissions/tc.pdf")
                        .build());

        assertEquals("UNDER_REVIEW", birthDoc.getStatus());
        assertEquals("UNDER_REVIEW", tcDoc.getStatus());

        // 3. Officer Verification
        var verifiedBirth = admissionsCrmService.verifyChecklistDocument("tenant_apex_mumbai_01", birthDoc.getId(),
                com.schoolerp.modules.admissions.dto.VerifyDocumentRequest.builder()
                        .status("VERIFIED")
                        .verifiedByName("Officer Siddharth")
                        .build());
        assertEquals("VERIFIED", verifiedBirth.getStatus());

        var rejectedTc = admissionsCrmService.verifyChecklistDocument("tenant_apex_mumbai_01", tcDoc.getId(),
                com.schoolerp.modules.admissions.dto.VerifyDocumentRequest.builder()
                        .status("REJECTED_REUPLOAD_REQUIRED")
                        .rejectionReason("Seal on Page 2 is missing. Please re-upload verified copy.")
                        .verifiedByName("Officer Siddharth")
                        .build());
        assertEquals("REJECTED_REUPLOAD_REQUIRED", rejectedTc.getStatus());
        assertEquals("Seal on Page 2 is missing. Please re-upload verified copy.", rejectedTc.getRejectionReason());

        // 4. Parent Public Tracking Dossier
        var tracker = admissionsCrmService.getPublicTrackingStatus("tenant_apex_mumbai_01", inq.getInquiryNumber(), "9822012345");
        assertNotNull(tracker);
        assertEquals(inq.getInquiryNumber(), tracker.getInquiryNumber());
        assertEquals(1, tracker.getVerifiedDocsCount());
        assertTrue(tracker.getChecklistDocuments().size() >= 2);

        // 5. Parent Re-uploads Corrected TC Document
        var reuploadedTc = admissionsCrmService.uploadChecklistDocument("tenant_apex_mumbai_01", inq.getId(),
                com.schoolerp.modules.admissions.dto.UploadDocumentRequest.builder()
                        .documentType("TRANSFER_CERTIFICATE")
                        .documentName("Dev_Kapoor_TC_Signed.pdf")
                        .storageUrl("https://s3.ap-south-1.amazonaws.com/apex-admissions/tc_signed.pdf")
                        .build());
        assertEquals("UNDER_REVIEW", reuploadedTc.getStatus());
        assertNull(reuploadedTc.getRejectionReason());
    }

    @Test
    void testGradeCapacityAndAutomatedWaitlistPromotion() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");

        // 1. Configure Grade 11-Science Capacity (2 Seats total)
        var capReq = com.schoolerp.modules.admissions.dto.ConfigureCapacityRequest.builder()
                .academicYear("2026-2027")
                .gradeName("Grade 11-Science")
                .totalSanctionedSeats(2)
                .generalQuotaSeats(2)
                .build();

        var cap = admissionsCrmService.configureGradeCapacity("tenant_apex_mumbai_01", capReq);
        assertNotNull(cap);
        assertEquals(2, cap.getTotalSanctionedSeats());
        assertEquals(2, cap.getAvailableSeats());

        // 2. Candidate Inquiries
        var inq1 = admissionsCrmService.createInquiry("tenant_apex_mumbai_01",
                com.schoolerp.modules.admissions.dto.CreateInquiryRequest.builder()
                        .parentName("Aditya Bose")
                        .parentPhone("+91-9833011111")
                        .studentName("Rohit Bose")
                        .gradeApplyingFor("Grade 11-Science")
                        .academicYear("2026-2027")
                        .build());

        var inq2 = admissionsCrmService.createInquiry("tenant_apex_mumbai_01",
                com.schoolerp.modules.admissions.dto.CreateInquiryRequest.builder()
                        .parentName("Manish Dave")
                        .parentPhone("+91-9833022222")
                        .studentName("Tanvi Dave")
                        .gradeApplyingFor("Grade 11-Science")
                        .academicYear("2026-2027")
                        .build());

        // Issue offers to candidate 1 & 2 -> Fills capacity!
        admissionsCrmService.issueMeritOffer("tenant_apex_mumbai_01",
                com.schoolerp.modules.admissions.dto.IssueOfferRequest.builder().inquiryId(inq1.getId()).build());
        admissionsCrmService.issueMeritOffer("tenant_apex_mumbai_01",
                com.schoolerp.modules.admissions.dto.IssueOfferRequest.builder().inquiryId(inq2.getId()).build());

        var updatedCapList = admissionsCrmService.getGradeCapacities("tenant_apex_mumbai_01", "2026-2027");
        var sciCap = updatedCapList.stream().filter(c -> "Grade 11-Science".equals(c.getGradeName())).findFirst().orElseThrow();
        assertEquals(2, sciCap.getActiveOffersCount());

        // 3. 3rd Candidate arrives -> Waitlisted
        var inq3 = admissionsCrmService.createInquiry("tenant_apex_mumbai_01",
                com.schoolerp.modules.admissions.dto.CreateInquiryRequest.builder()
                        .parentName("Vivek Roy")
                        .parentPhone("+91-9833033333")
                        .studentName("Sanya Roy")
                        .gradeApplyingFor("Grade 11-Science")
                        .academicYear("2026-2027")
                        .build());
        admissionsCrmService.updatePipelineStage("tenant_apex_mumbai_01", inq3.getId(), "WAITLISTED", "Waitlisted due to zero open general seats", null);

        var capCheck = admissionsCrmService.getGradeCapacities("tenant_apex_mumbai_01", "2026-2027");
        var checkSci = capCheck.stream().filter(c -> "Grade 11-Science".equals(c.getGradeName())).findFirst().orElseThrow();
        assertEquals(1, checkSci.getWaitlistCount());

        // 4. One-Click Counselor Promotes Waitlisted Candidate
        var promotedOffer = admissionsCrmService.promoteWaitlistApplicant("tenant_apex_mumbai_01", inq3.getId());
        assertNotNull(promotedOffer);
        assertEquals("ISSUED", promotedOffer.getOfferStatus());

        var updatedInq3 = admissionsCrmService.getInquiryById("tenant_apex_mumbai_01", inq3.getId());
        assertEquals("OFFER_ISSUED", updatedInq3.getPipelineStage());
        assertTrue(updatedInq3.getCounselorNotes().contains("Promoted from Waitlist"));
    }

    @Test
    void testCounselorTaskSchedulingAndFollowUpCompletion() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");

        // 1. Create Inquiry
        var inq = admissionsCrmService.createInquiry("tenant_apex_mumbai_01",
                com.schoolerp.modules.admissions.dto.CreateInquiryRequest.builder()
                        .parentName("Meera Sengupta")
                        .parentPhone("+91-9844055555")
                        .studentName("Arjun Sengupta")
                        .gradeApplyingFor("Grade 4")
                        .academicYear("2026-2027")
                        .build());

        // 2. Schedule Follow-Up Task
        var taskReq = com.schoolerp.modules.admissions.dto.CreateCounselorTaskRequest.builder()
                .inquiryId(inq.getId())
                .counselorName("Sunita Verma")
                .taskType("TOUR_REMINDER")
                .title("Call parent to confirm tomorrow's campus tour")
                .description("Inquired about STEM laboratory infrastructure and swimming coaching")
                .dueDate(java.time.LocalDateTime.now().plusDays(1))
                .priority("HIGH")
                .build();

        var task = admissionsCrmService.createCounselorTask("tenant_apex_mumbai_01", taskReq);
        assertNotNull(task);
        assertNotNull(task.getId());
        assertEquals("PENDING", task.getStatus());
        assertEquals("Sunita Verma", task.getCounselorName());

        // 3. Query Tasks by Counselor
        var counselorTasks = admissionsCrmService.getCounselorTasks("tenant_apex_mumbai_01", "Sunita Verma", "PENDING");
        assertFalse(counselorTasks.isEmpty());
        assertTrue(counselorTasks.stream().anyMatch(t -> t.getId().equals(task.getId())));

        // 4. Complete Task with Outcome Notes
        var completeReq = com.schoolerp.modules.admissions.dto.CompleteCounselorTaskRequest.builder()
                .outcomeNotes("Spoke with Mrs. Sengupta. Family arriving at 10:30 AM tomorrow for tour.")
                .build();

        var completedTask = admissionsCrmService.completeCounselorTask("tenant_apex_mumbai_01", task.getId(), completeReq);
        assertEquals("COMPLETED", completedTask.getStatus());
        assertNotNull(completedTask.getCompletedAt());
        assertEquals("Spoke with Mrs. Sengupta. Family arriving at 10:30 AM tomorrow for tour.", completedTask.getOutcomeNotes());

        // 5. Verify Inquiry Counselor Notes Updated
        var inqWithNotes = admissionsCrmService.getInquiryById("tenant_apex_mumbai_01", inq.getId());
        assertTrue(inqWithNotes.getCounselorNotes().contains("Spoke with Mrs. Sengupta"));
    }

    @Test
    void testIndian100PointCriteriaAndStatutoryCertificateVerification() {
        // 1. Create Inquiry for Indian School Admission
        var inqReq = com.schoolerp.modules.admissions.dto.CreateInquiryRequest.builder()
                .studentName("Ananya Deshmukh")
                .parentName("Dr. Nilesh Deshmukh")
                .parentPhone("+919820011223")
                .parentEmail("nilesh.deshmukh@gmail.com")
                .gradeApplyingFor("Grade 1")
                .academicYear("2026-2027")
                .sourceChannel("WEBSITE_FORM")
                .build();

        var inq = admissionsCrmService.createInquiry("tenant_apex_mumbai_01", inqReq);
        assertNotNull(inq);

        // 2. Evaluate 100-Point DoE Criteria
        // Distance: 2.2 km (<=3km -> 40 pts) + Sibling (30 pts) + Alumni (10 pts) + Single Girl Child (5 pts) = 85 pts
        var criteriaReq = com.schoolerp.modules.admissions.dto.EvaluatePointsRequest.builder()
                .inquiryId(inq.getId())
                .distanceKm(new java.math.BigDecimal("2.20"))
                .hasSibling(true)
                .hasAlumniParent(true)
                .alumniPassingYear(2010)
                .isSingleGirlChild(true)
                .isFirstBorn(true)
                .build();

        var criteria = admissionsCrmService.evaluate100PointCriteria("tenant_apex_mumbai_01", criteriaReq);
        assertNotNull(criteria);
        assertEquals(40, criteria.getDistancePoints());
        assertEquals(30, criteria.getSiblingPoints());
        assertEquals(10, criteria.getAlumniPoints());
        assertEquals(5, criteria.getGirlChildPoints());
        assertEquals(5, criteria.getFirstBornPoints());
        assertEquals(90, criteria.getTotalCalculatedPoints());
        assertEquals("TIER_1_DIRECT_CALL", criteria.getCriteriaVerdict());

        // 3. Record Government Caste Certificate (OBC-NCL issued by Tehsildar)
        var certReq = com.schoolerp.modules.admissions.dto.StatutoryCertificateRequest.builder()
                .inquiryId(inq.getId())
                .certificateType("CASTE_OBC_NCL")
                .certificateNumber("OBC-MH-2026-98124")
                .issuingAuthority("Tehsildar & Executive Magistrate, Haveli Pune")
                .issueDate(java.time.LocalDate.now().minusMonths(2))
                .validFinancialYear("2026-2027")
                .verificationRemarks("Original Tehsildar Non-Creamy Layer verified for FY 2026-2027")
                .markVerifiedImmediately(true)
                .build();

        var cert = admissionsCrmService.recordStatutoryCertificate("tenant_apex_mumbai_01", certReq);
        assertNotNull(cert);
        assertEquals("VERIFIED", cert.getVerifiedStatus());
        assertEquals("CASTE_OBC_NCL", cert.getCertificateType());

        // 4. Verify inquiry updated with social category
        var updatedInq = admissionsCrmService.getInquiryById("tenant_apex_mumbai_01", inq.getId());
        assertEquals("OBC_NCL", updatedInq.getSocialCategory());
        assertEquals(90, updatedInq.getLeadScore());

        // 5. Query Category Merit List
        var meritList = admissionsCrmService.getCategoryMeritList("tenant_apex_mumbai_01", "Grade 1");
        assertNotNull(meritList);
        assertEquals("Grade 1", meritList.getGradeName());
        assertTrue(meritList.getTotalApplicants() > 0);
        assertTrue(meritList.getRankedCandidates().stream().anyMatch(c -> c.getInquiryId().equals(inq.getId())));
    }

    @Test
    void testCategoryWiseSeatMatrixAndSegregatedWaitlistPromotion() {
        // 1. Configure Indian Statutory Quota Matrix for Grade 6
        var matrixReq = com.schoolerp.modules.admissions.dto.ConfigureCategoryMatrixRequest.builder()
                .gradeName("Grade 6")
                .academicYear("2026-2027")
                .totalSanctionedSeats(120)
                .generalQuotaSeats(50)
                .rteQuotaSeats(30)
                .obcQuotaSeats(15)
                .scQuotaSeats(8)
                .stQuotaSeats(4)
                .cwsnQuotaSeats(3)
                .siblingQuotaSeats(5)
                .staffWardQuotaSeats(3)
                .singleGirlChildQuotaSeats(2)
                .managementQuotaSeats(10)
                .build();

        var overview = admissionsCrmService.configureCategoryMatrix("tenant_apex_mumbai_01", matrixReq);
        assertNotNull(overview);
        assertEquals(120, overview.getTotalSanctionedSeats());
        assertEquals(15, overview.getObcQuotaSeats());
        assertEquals(8, overview.getScQuotaSeats());
        assertEquals(3, overview.getCwsnQuotaSeats());

        // 2. Create Candidate on Waitlist under OBC_NCL quota
        var inqReq = com.schoolerp.modules.admissions.dto.CreateInquiryRequest.builder()
                .studentName("Kavya Patil")
                .parentName("Sachin Patil")
                .parentPhone("+919765432100")
                .gradeApplyingFor("Grade 6")
                .academicYear("2026-2027")
                .sourceChannel("WALK_IN")
                .build();

        var inq = admissionsCrmService.createInquiry("tenant_apex_mumbai_01", inqReq);
        admissionsCrmService.updatePipelineStage("tenant_apex_mumbai_01", inq.getId(), "WAITLISTED", "Waitlisted in OBC-NCL category", null);

        // 3. Promote Candidate from OBC_NCL Category Waitlist
        var promoted = admissionsCrmService.promoteCategoryWaitlist("tenant_apex_mumbai_01", "OBC_NCL", inq.getId());
        assertEquals("OFFER_ISSUED", promoted.getPipelineStage());
        assertEquals("OBC_NCL", promoted.getAdmissionQuota());
    }

    @Test
    void testEntranceAssessmentAdmitCardAndParentDigitalOfferAcceptance() {
        // 1. Create Inquiry for Grade 9 Entrance Screening
        var inqReq = com.schoolerp.modules.admissions.dto.CreateInquiryRequest.builder()
                .studentName("Rohit Raghavan")
                .parentName("Suresh Raghavan")
                .parentPhone("+919840012345")
                .gradeApplyingFor("Grade 9")
                .academicYear("2026-2027")
                .sourceChannel("WEBSITE_FORM")
                .build();

        var inq = admissionsCrmService.createInquiry("tenant_apex_mumbai_01", inqReq);

        // 2. Schedule Entrance Assessment
        var schedReq = com.schoolerp.modules.admissions.dto.ScheduleEntranceTestRequest.builder()
                .inquiryId(inq.getId())
                .testDate(java.time.LocalDateTime.now().plusDays(5))
                .examHall("Aryabhata Block - Exam Hall B")
                .reportingTime("08:30 AM")
                .qualifyingCutoffPercentage(java.math.BigDecimal.valueOf(60.0))
                .build();

        var testDto = admissionsCrmService.scheduleEntranceTest("tenant_apex_mumbai_01", schedReq);
        assertNotNull(testDto);
        assertNotNull(testDto.getAdmitCardToken());
        assertTrue(testDto.getTestCode().startsWith("TEST-2026-G"));
        assertEquals("PENDING", testDto.getResultStatus());

        // 3. Public Parent Admit Card Retrieval
        var admitCard = admissionsCrmService.getPublicAdmitCard("tenant_apex_mumbai_01", inq.getInquiryNumber());
        assertNotNull(admitCard);
        assertEquals(testDto.getAdmitCardToken(), admitCard.getAdmitCardToken());
        assertEquals("Aryabhata Block - Exam Hall B", admitCard.getExamHall());

        // 4. Evaluate Entrance Test: Math 22, English 23, Science 20, Aptitude 20 = 85/100 (85.0% >= 60% cutoff)
        var evalReq = com.schoolerp.modules.admissions.dto.EvaluateEntranceTestRequest.builder()
                .mathScore(java.math.BigDecimal.valueOf(22.0))
                .englishScore(java.math.BigDecimal.valueOf(23.0))
                .scienceScore(java.math.BigDecimal.valueOf(20.0))
                .aptitudeScore(java.math.BigDecimal.valueOf(20.0))
                .maximumScore(java.math.BigDecimal.valueOf(100.0))
                .evaluatorRemarks("Excellent analytical and scientific problem-solving aptitude.")
                .build();

        var evaluated = admissionsCrmService.evaluateEntranceTest("tenant_apex_mumbai_01", testDto.getId(), evalReq);
        assertEquals("QUALIFIED", evaluated.getResultStatus());
        assertEquals(java.math.BigDecimal.valueOf(85.0).setScale(2), evaluated.getPercentage().setScale(2, java.math.RoundingMode.HALF_UP));

        // 5. Verify Inquiry Advanced to PRINCIPAL_INTERVIEW
        var inqInterview = admissionsCrmService.getInquiryById("tenant_apex_mumbai_01", inq.getId());
        assertEquals("PRINCIPAL_INTERVIEW", inqInterview.getPipelineStage());

        // 6. Link Verified Sibling for Concession Forecast
        var existingStudent = studentRepository.findAll().get(0);
        var linkSiblingReq = com.schoolerp.modules.admissions.dto.LinkSiblingRequest.builder()
                .siblingStudentId(existingStudent.getId())
                .build();

        var concessionPreview = admissionsCrmService.linkVerifiedSibling("tenant_apex_mumbai_01", inq.getId(), linkSiblingReq);
        assertNotNull(concessionPreview);
        assertEquals(java.math.BigDecimal.valueOf(15.0).setScale(2), concessionPreview.getConcessionPercentage().setScale(2, java.math.RoundingMode.HALF_UP));
        assertEquals(java.math.BigDecimal.valueOf(18000.0).setScale(2), concessionPreview.getEstimatedConcessionSavings().setScale(2, java.math.RoundingMode.HALF_UP));

        // 7. Apply for Sports Scholarship
        var scholarshipReq = com.schoolerp.modules.admissions.dto.ApplyScholarshipRequest.builder()
                .inquiryId(inq.getId())
                .scholarshipType("SPORTS_NATIONAL")
                .claimedAchievementOrIncome("Gold Medalist - CBSE National Swimming Championship 2025")
                .requestedConcessionPercentage(java.math.BigDecimal.valueOf(30.0))
                .build();

        var scholarship = admissionsCrmService.applyForScholarship("tenant_apex_mumbai_01", scholarshipReq);
        assertEquals("SUBMITTED", scholarship.getCommitteeStatus());

        var decideReq = com.schoolerp.modules.admissions.dto.DecideScholarshipRequest.builder()
                .decision("APPROVED")
                .approvedConcessionPercentage(java.math.BigDecimal.valueOf(30.0))
                .approvedAmount(java.math.BigDecimal.valueOf(36000.0))
                .committeeReviewerNotes("Approved under CBSE National Sports Excellence Policy")
                .reviewedBy("Director of Physical Education & Admissions Board")
                .build();

        var decided = admissionsCrmService.decideScholarship("tenant_apex_mumbai_01", scholarship.getId(), decideReq);
        assertEquals("APPROVED", decided.getCommitteeStatus());

        // 8. Issue Formal Merit Offer Letter
        var offerReq = com.schoolerp.modules.admissions.dto.IssueOfferRequest.builder()
                .inquiryId(inq.getId())
                .tuitionFeeTotal(java.math.BigDecimal.valueOf(120000.0))
                .scholarshipOrConcessionAmount(java.math.BigDecimal.valueOf(36000.0))
                .validUntilDate(java.time.LocalDate.now().plusDays(14))
                .build();

        var offer = admissionsCrmService.issueMeritOffer("tenant_apex_mumbai_01", offerReq);
        assertNotNull(offer);
        assertEquals(java.math.BigDecimal.valueOf(84000.0).setScale(2), offer.getNetAdmissionPayable().setScale(2, java.math.RoundingMode.HALF_UP));

        // 9. Parent Digital Offer Acceptance & Online Seat Lock Deposit (₹25,000)
        var acceptReq = com.schoolerp.modules.admissions.dto.AcceptOfferAndLockRequest.builder()
                .parentSignatureName("Suresh Raghavan")
                .parentIpAddress("49.207.180.25")
                .seatLockDepositAmount(java.math.BigDecimal.valueOf(25000.0))
                .depositPaymentMethod("UPI")
                .build();

        var acceptResult = admissionsCrmService.acceptOfferAndLockSeat("tenant_apex_mumbai_01", offer.getId(), acceptReq);
        assertNotNull(acceptResult);
        assertEquals("ACCEPTED", acceptResult.getOfferStatus());
        assertEquals("PAID", acceptResult.getDepositStatus());
        assertEquals("Suresh Raghavan", acceptResult.getParentSignatureName());
        assertTrue(acceptResult.getDepositTransactionId().startsWith("TXN-UPI-"));
    }

    @Test
    void testHostelRoomAllocationAndWardenDormRollCall() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");

        // 1. Register Hostel Room
        var roomReq = com.schoolerp.modules.operations.dto.RegisterHostelRoomRequest.builder()
                .blockName("Aryabhata Senior Boys Hostel")
                .floorNumber(2)
                .roomNumber("B-204")
                .roomType("AC")
                .totalBeds(4)
                .annualFee(BigDecimal.valueOf(85000.00))
                .build();

        var room = operationsService.registerHostelRoom(roomReq);
        assertNotNull(room);
        assertEquals("Aryabhata Senior Boys Hostel", room.getBlockName());
        assertEquals("B-204", room.getRoomNumber());
        assertEquals(4, room.getTotalBeds());
        assertEquals(0, room.getOccupiedBeds());
        assertEquals(4, room.getAvailableBeds());

        // 2. Allocate Bed to Student
        var student = studentRepository.findAll().get(0);
        var allocReq = com.schoolerp.modules.operations.dto.AllocateHostelBedRequest.builder()
                .roomId(room.getId())
                .studentId(student.getId())
                .bedNumber(1)
                .academicYear("2026-2027")
                .build();

        var allocation = operationsService.allocateHostelBed(allocReq);
        assertNotNull(allocation);
        assertEquals(1, allocation.getBedNumber());
        assertEquals(student.getId(), allocation.getStudentId());
        assertEquals("ACTIVE", allocation.getStatus());

        // 3. Record Warden Night Roll-Call
        var rollCallReq = com.schoolerp.modules.operations.dto.HostelRollCallRequest.builder()
                .allocationId(allocation.getId())
                .rollCallDate(java.time.LocalDate.now())
                .sessionType("NIGHT_LIGHTS_OUT")
                .status("PRESENT")
                .remarks("Student present in dorm bunk at 22:00 lights out")
                .wardenName("Col. S. Sharma (Retd.)")
                .build();

        var rollCall = operationsService.recordHostelRollCall(rollCallReq);
        assertNotNull(rollCall);
        assertEquals("NIGHT_LIGHTS_OUT", rollCall.getSessionType());
        assertEquals("PRESENT", rollCall.getStatus());
        assertEquals("Col. S. Sharma (Retd.)", rollCall.getWardenName());

        // 4. Issue Weekend Exeat Pass
        var exeatReq = com.schoolerp.modules.operations.dto.HostelExeatPassRequest.builder()
                .studentId(student.getId())
                .exeatType("WEEKEND_HOME")
                .departureTime(java.time.LocalDateTime.now().plusHours(4))
                .expectedReturnTime(java.time.LocalDateTime.now().plusDays(2))
                .parentAuthorized(true)
                .parentContactPhone("+91-98200-11223")
                .build();

        var exeat = operationsService.issueHostelExeatPass(exeatReq);
        assertNotNull(exeat);
        assertTrue(exeat.getPassNumber().startsWith("EXEAT-"));
        assertEquals("WEEKEND_HOME", exeat.getExeatType());
        assertEquals("APPROVED", exeat.getStatus());

        // 5. Query Hostel Roster
        var roster = operationsService.getHostelRoster("Aryabhata Senior Boys Hostel");
        assertNotNull(roster);
        assertTrue(roster.getTotalRooms() >= 1);
        assertTrue(roster.getTotalOccupied() >= 1);
        assertFalse(roster.getResidents().isEmpty());
    }

    @Test
    void testCampusVisitorCheckInHostApprovalAndCheckout() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");

        // 1. Visitor Check-In
        var checkInReq = com.schoolerp.modules.operations.dto.VisitorCheckInRequest.builder()
                .visitorName("Rajesh Sharma")
                .visitorPhone("+91-98765-43210")
                .idProofType("AADHAAR")
                .idProofNumber("XXXX-XXXX-9182")
                .purposeOfVisit("PARENT_MEETING")
                .hostStaffName("Dr. Anita Roy")
                .entryGate("Gate 1 - Main Arch")
                .build();

        var visitor = operationsService.checkInVisitor(checkInReq);
        assertNotNull(visitor);
        assertTrue(visitor.getVisitorPassNumber().startsWith("VIS-"));
        assertTrue(visitor.getBadgeQrToken().startsWith("VQR-"));
        assertEquals("ACTIVE_ON_CAMPUS", visitor.getStatus());
        assertEquals("PENDING", visitor.getHostApprovalStatus());

        // 2. Host Approval Workflow
        var approvalReq = com.schoolerp.modules.operations.dto.VisitorApprovalRequest.builder()
                .approvalDecision("APPROVED")
                .build();

        var approved = operationsService.updateVisitorHostApproval(visitor.getId(), approvalReq);
        assertEquals("APPROVED", approved.getHostApprovalStatus());

        // 3. Query Active Visitors
        var activeVisitors = operationsService.getActiveVisitors();
        assertNotNull(activeVisitors);
        assertTrue(activeVisitors.stream().anyMatch(v -> v.getId().equals(visitor.getId())));

        // 4. Visitor Check-Out
        var checkedOut = operationsService.checkOutVisitor(visitor.getId());
        assertEquals("CHECKED_OUT", checkedOut.getStatus());
        assertNotNull(checkedOut.getCheckedOutAt());
    }

    @Test
    void testFleetFuelEfficiencyAndRtoComplianceSentinel() {
        TenantContext.setTenantId("tenant_apex_mumbai_01");

        // 1. Seed or retrieve Vehicle
        var vehicles = operationsService.getVehicles();
        assertFalse(vehicles.isEmpty(), "Vehicle fleet must be initialized in test seed");
        var vehicle = vehicles.get(0);

        // 2. Record First Fuel Log (Baseline Odometer 42,000)
        var fuelReq1 = com.schoolerp.modules.operations.dto.RecordFuelLogRequest.builder()
                .vehicleId(vehicle.getId())
                .vehicleRegistration(vehicle.getRegistrationNumber())
                .fillDate(java.time.LocalDate.now().minusDays(5))
                .fuelLitres(BigDecimal.valueOf(50.00))
                .fuelRatePerLitre(BigDecimal.valueOf(94.50))
                .odometerReading(42000)
                .fuelStationName("Indian Oil - Viman Nagar")
                .driverName("Ramesh Pawar")
                .build();

        var log1 = operationsService.recordFuelLog(fuelReq1);
        assertNotNull(log1);
        assertEquals(42000, log1.getOdometerReading());

        // 3. Record Second Fuel Log (Odometer 42,225 km -> 225 km run / 45.0 L = 5.00 km/L)
        var fuelReq2 = com.schoolerp.modules.operations.dto.RecordFuelLogRequest.builder()
                .vehicleId(vehicle.getId())
                .vehicleRegistration(vehicle.getRegistrationNumber())
                .fillDate(java.time.LocalDate.now())
                .fuelLitres(BigDecimal.valueOf(45.00))
                .fuelRatePerLitre(BigDecimal.valueOf(94.50))
                .odometerReading(42225)
                .fuelStationName("Indian Oil - Viman Nagar")
                .driverName("Ramesh Pawar")
                .build();

        var log2 = operationsService.recordFuelLog(fuelReq2);
        assertNotNull(log2);
        assertEquals(225, log2.getKilometersRun());
        assertEquals(BigDecimal.valueOf(5.00).setScale(2), log2.getMileageKmPerLitre().setScale(2, java.math.RoundingMode.HALF_UP));
        assertFalse(log2.isPilferageAlert(), "Mileage 5.0 km/L should not trigger pilferage alert");

        // 4. Test RTO Compliance Sentinel
        var complianceList = operationsService.getVehicleFitnessCompliance();
        assertNotNull(complianceList);
        assertFalse(complianceList.isEmpty());
        var sentinel = complianceList.get(0);
        assertNotNull(sentinel.getRtoFitnessExpiry());
        assertNotNull(sentinel.getInsurancePolicyExpiry());
        assertNotNull(sentinel.getPucCertificateExpiry());
        assertTrue(sentinel.getDaysUntilFitnessExpiry() > 0);
        assertEquals("COMPLIANT", sentinel.getComplianceStatus());

        // 5. Test Multi-Year Cumulative Official Transcript Generation
        var student = studentRepository.findAll().get(0);
        var transcriptReq = com.schoolerp.modules.students.dto.GenerateTranscriptRequest.builder()
                .academicStream("Senior Secondary Science")
                .cumulativeGpa(BigDecimal.valueOf(9.60))
                .cumulativePercentage(BigDecimal.valueOf(96.00))
                .totalCredits(42)
                .mediumOfInstruction("English")
                .issuedByPrincipal("Principal Dr. Anita Roy")
                .build();

        var transcript = studentService.generateOfficialTranscript(student.getId(), transcriptReq);
        assertNotNull(transcript);
        assertTrue(transcript.getTranscriptNumber().startsWith("TRANS-2026-"));
        assertEquals(student.getFullName(), transcript.getStudentName());
        assertNotNull(transcript.getVerificationHash());
        assertTrue(transcript.getVerificationHash().startsWith("SHA256:"));
        assertEquals(BigDecimal.valueOf(9.60), transcript.getCumulativeGpa());
        assertEquals("English", transcript.getMediumOfInstruction());
    }

    @Test
    void testMorningTeacherSubstitutionSolverAndEquityLimits() {
        var employees = employeeRepository.findAll();
        assertFalse(employees.isEmpty(), "Staff must be present");
        var absentTeacher = employees.get(0);

        var subReq = new com.schoolerp.modules.academics.dto.MorningSubstitutionRequest();
        subReq.setSubstitutionDate(java.time.LocalDate.now());
        subReq.setAbsentTeacherId(absentTeacher.getId());
        subReq.setPeriodNumber(2);
        subReq.setSubjectName("Advanced Physics");
        subReq.setMaxDailySubstitutionsPerTeacher(2);

        var assigned = morningSubstitutionService.autoAssignSubstitution(subReq);
        assertNotNull(assigned);
        assertEquals("ASSIGNED", assigned.getStatus());
        assertNotEquals(absentTeacher.getId(), assigned.getSubstituteTeacherId(), "Absent teacher cannot substitute for self");
        assertNotNull(assigned.getSubstituteTeacherName());

        var roster = morningSubstitutionService.getDailyRoster(java.time.LocalDate.now());
        assertNotNull(roster);
        assertFalse(roster.isEmpty());
        assertTrue(roster.stream().anyMatch(r -> r.getId().equals(assigned.getId())));
    }

    @Test
    void testExamInvigilationAntiCheatingAndUfmDocket() {
        var employees = employeeRepository.findAll();
        assertFalse(employees.isEmpty());
        var teacher = employees.get(0);

        // 1. Anti-cheating conflict test: if teacher department matches exam subject, scheduling should throw
        var conflictReq = new com.schoolerp.modules.examinations.dto.ScheduleInvigilationRequest();
        conflictReq.setInvigilatorStaffId(teacher.getId());
        conflictReq.setSubjectName(teacher.getDepartment() != null ? teacher.getDepartment() : "Science");
        conflictReq.setExamName("Term 2 Exam");
        conflictReq.setHallRoomNumber("Hall A-1");

        assertThrows(IllegalStateException.class, () -> {
            examInvigilationService.scheduleInvigilation(conflictReq);
        }, "Teacher cannot invigilate an exam of their own department/subject");

        // 2. Schedule invigilation duty with a non-conflicting subject
        var validReq = new com.schoolerp.modules.examinations.dto.ScheduleInvigilationRequest();
        validReq.setInvigilatorStaffId(teacher.getId());
        validReq.setSubjectName("Non-Conflicting Subject XYZ");
        validReq.setExamName("Term 2 Board Exam");
        validReq.setHallRoomNumber("Hall A-1");
        validReq.setExamDate(java.time.LocalDate.now());

        var duty = examInvigilationService.scheduleInvigilation(validReq);
        assertNotNull(duty);
        assertEquals(teacher.getFullName(), duty.getInvigilatorStaffName());
        assertTrue(duty.getPaperSealVerified());

        // 3. Verify paper seals
        var verifyReq = new com.schoolerp.modules.examinations.dto.VerifySealsRequest();
        verifyReq.setPaperSealVerified(true);
        verifyReq.setWitnessStaffName("Witness Staff Officer");
        var verified = examInvigilationService.verifySeals(duty.getId(), verifyReq);
        assertNotNull(verified);
        assertEquals("Witness Staff Officer", verified.getWitnessStaffName());

        // 4. Report UFM Malpractice
        var student = studentRepository.findAll().get(0);
        var ufmReq = new com.schoolerp.modules.examinations.dto.ReportUfmRequest();
        ufmReq.setExamScheduleId(duty.getExamScheduleId());
        ufmReq.setStudentId(student.getId());
        ufmReq.setSubjectName(duty.getSubjectName());
        ufmReq.setContrabandType("SMART_WATCH");
        ufmReq.setInvigilatorRemarks("Student had unauthorized smart watch connected to mobile device");
        ufmReq.setStudentStatement("I forgot to remove it before the exam started");

        var docket = examInvigilationService.reportUfm(ufmReq);
        assertNotNull(docket);
        assertTrue(docket.getDocketNumber().startsWith("UFM-2026-"));
        assertEquals("UNDER_COMMITTEE_REVIEW", docket.getVerdictStatus());
        assertEquals("SMART_WATCH", docket.getContrabandType());

        var dockets = examInvigilationService.getUfmDockets("UNDER_COMMITTEE_REVIEW");
        assertFalse(dockets.isEmpty());
        assertTrue(dockets.stream().anyMatch(d -> d.getId().equals(docket.getId())));
    }

    @Test
    void testBookstorePosCheckoutAndStudentLedgerDebit() {
        var student = studentRepository.findAll().get(0);

        // 1. Register items
        var itemReq = new com.schoolerp.modules.operations.dto.RegisterBookstoreItemRequest();
        itemReq.setSkuCode("SKU-TXT-G10-MATH-" + java.util.UUID.randomUUID().toString().substring(0, 4));
        itemReq.setItemName("NCERT Mathematics Class 10");
        itemReq.setItemCategory("TEXTBOOK_BUNDLE");
        itemReq.setTargetGrade("Grade 10");
        itemReq.setUnitSellingPrice(BigDecimal.valueOf(450.00));
        itemReq.setCostPrice(BigDecimal.valueOf(380.00));
        itemReq.setStockQuantity(50);
        itemReq.setReorderThreshold(10);
        itemReq.setSupplierVendorName("Apex Academic Publishing");

        var item = bookstoreService.registerItem(itemReq);
        assertNotNull(item);
        assertEquals(50, item.getStockQuantity());

        // 2. Checkout POS Order with STUDENT_LEDGER_DEBIT
        var checkoutReq = new com.schoolerp.modules.operations.dto.BookstoreCheckoutRequest();
        checkoutReq.setStudentId(student.getId());
        checkoutReq.setPaymentMode("STUDENT_LEDGER_DEBIT");
        checkoutReq.setItems(java.util.List.of(new com.schoolerp.modules.operations.dto.BookstoreCheckoutRequest.CheckoutItem(item.getId(), 2)));

        var order = bookstoreService.checkout(checkoutReq);
        assertNotNull(order);
        assertTrue(order.getOrderReceiptNumber().startsWith("BK-REC-2026-"));
        assertEquals("STUDENT_LEDGER_DEBIT", order.getPaymentMode());
        assertEquals(BigDecimal.valueOf(900.00).setScale(2), order.getTotalAmount().setScale(2, java.math.RoundingMode.HALF_UP));

        // 3. Verify stock was decremented from 50 to 48
        var updatedItems = bookstoreService.getItems(null, null);
        var updatedItem = updatedItems.stream().filter(i -> i.getId().equals(item.getId())).findFirst().orElseThrow();
        assertEquals(48, updatedItem.getStockQuantity());
    }

    @Test
    void testHealthPassportAndCafeteriaAllergenDefenseShield() {
        var student = studentRepository.findAll().get(0);

        // 1. Save critical health passport with Severe Peanut & Gluten Allergy
        var passportReq = new com.schoolerp.modules.students.dto.SaveHealthPassportRequest();
        passportReq.setBloodGroup("O+");
        passportReq.setHasSevereAllergies(true);
        passportReq.setAllergenList("PEANUTS,GLUTEN");
        passportReq.setChronicConditions("ASTHMA");
        passportReq.setInhalerInInfirmary(true);
        passportReq.setEpipenPrescribed(true);
        passportReq.setEmergencyContactPhone("+91-9876543210");
        passportReq.setTreatingPhysicianName("Dr. Sunita Mehta, Pediatric Allergist");
        passportReq.setParamedicEmergencyProtocol("Administer EpiPen 0.3mg immediately into outer thigh and call 108");

        var passport = healthPassportService.saveHealthPassport(student.getId(), passportReq);
        assertNotNull(passport);
        assertEquals("O+", passport.getBloodGroup());
        assertTrue(passport.getHasSevereAllergies());
        assertTrue(passport.getEpipenPrescribed());

        // 2. Intercept Cafeteria POS for peanut-containing snack
        var hazardCheck = new com.schoolerp.modules.operations.dto.AllergenCheckRequest();
        hazardCheck.setStudentId(student.getId());
        hazardCheck.setItemName("Peanut Chikki Energy Bar");
        hazardCheck.setItemIngredients(java.util.List.of("Jaggery", "Roasted Peanuts", "Cardamom"));

        var hazardResult = cafeteriaAllergenService.checkAllergens(hazardCheck);
        assertNotNull(hazardResult);
        assertFalse(hazardResult.getAllowed(), "Peanut snack must be BLOCKED for peanut-allergic student");
        assertTrue(hazardResult.getMatchedAllergens().contains("Roasted Peanuts"));
        assertTrue(hazardResult.getWarningMessage().contains("CRITICAL ALLERGEN HAZARD"));
        assertTrue(hazardResult.getEpiPenRequired());

        // 3. Cafeteria POS for safe allergen-free food
        var safeCheck = new com.schoolerp.modules.operations.dto.AllergenCheckRequest();
        safeCheck.setStudentId(student.getId());
        safeCheck.setItemName("Steamed Basmati Rice & Moong Dal");
        safeCheck.setItemIngredients(java.util.List.of("Rice", "Moong Dal", "Cumin", "Turmeric"));

        var safeResult = cafeteriaAllergenService.checkAllergens(safeCheck);
        assertNotNull(safeResult);
        assertTrue(safeResult.getAllowed(), "Safe lunch without peanuts or gluten must be permitted");
        assertTrue(safeResult.getMatchedAllergens().isEmpty());

        // 4. Test Event Costume Rental Cycle
        var costumeReq = new com.schoolerp.modules.operations.dto.RegisterCostumeRequest();
        costumeReq.setEventName("Shakespeare Festival 2026");
        costumeReq.setCostumeDescription("Hamlet Royal Velvet Cape - Purple");
        costumeReq.setVendorSupplierName("Theatrical Wardrobe Ltd");
        costumeReq.setRentalRatePerDay(BigDecimal.valueOf(150.00));

        var costume = costumeRentalService.registerCostume(costumeReq);
        assertNotNull(costume);
        assertEquals("IN_WARDROBE", costume.getReturnCondition());

        // Issue costume to student actor
        var issueReq = new com.schoolerp.modules.operations.dto.IssueCostumeRequest();
        issueReq.setCostumeId(costume.getId());
        issueReq.setStudentId(student.getId());
        issueReq.setAction("ISSUE");
        issueReq.setExpectedReturnDate(java.time.LocalDate.now().plusDays(2));

        var issued = costumeRentalService.issueOrReturnCostume(issueReq);
        assertNotNull(issued);
        assertEquals("ISSUED_OUT", issued.getReturnCondition());
        assertEquals(student.getFullName(), issued.getAssignedStudentName());

        // Return costume with damage inspection
        var returnReq = new com.schoolerp.modules.operations.dto.IssueCostumeRequest();
        returnReq.setCostumeId(costume.getId());
        returnReq.setAction("RETURN");
        returnReq.setReturnCondition("RETURNED_DAMAGED");
        returnReq.setDamageFineAmount(BigDecimal.valueOf(350.00));

        var returned = costumeRentalService.issueOrReturnCostume(returnReq);
        assertNotNull(returned);
        assertEquals("RETURNED_DAMAGED", returned.getReturnCondition());
        assertEquals(BigDecimal.valueOf(350.00), returned.getDamageFineAmount());
    }
}



