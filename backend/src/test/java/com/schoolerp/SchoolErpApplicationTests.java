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
}
