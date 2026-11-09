package com.schoolerp.config;

import com.schoolerp.common.context.TenantContext;

import com.schoolerp.modules.academics.entity.ClassSection;
import com.schoolerp.modules.academics.entity.TimetablePeriod;
import com.schoolerp.modules.academics.repository.ClassSectionRepository;
import com.schoolerp.modules.academics.repository.TimetablePeriodRepository;
import com.schoolerp.modules.admissions.entity.AdmissionApplication;
import com.schoolerp.modules.admissions.repository.AdmissionRepository;
import com.schoolerp.modules.examinations.entity.StudentMark;
import com.schoolerp.modules.examinations.repository.StudentMarkRepository;
import com.schoolerp.modules.examinations.service.GradebookService;
import com.schoolerp.modules.finance.entity.*;
import com.schoolerp.modules.finance.repository.*;
import com.schoolerp.modules.communication.entity.*;
import com.schoolerp.modules.communication.repository.*;
import com.schoolerp.modules.identity.entity.Role;
import com.schoolerp.modules.identity.entity.User;
import com.schoolerp.modules.identity.repository.RoleRepository;
import com.schoolerp.modules.identity.repository.UserRepository;
import com.schoolerp.modules.institution.entity.Tenant;
import com.schoolerp.modules.institution.repository.TenantRepository;
import com.schoolerp.modules.operations.entity.*;
import com.schoolerp.modules.operations.repository.*;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.entity.StaffLeaveRequest;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.staff.repository.StaffLeaveRequestRepository;
import com.schoolerp.modules.staff.service.PayrollService;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.entity.StudentDocument;
import com.schoolerp.modules.students.repository.StudentDocumentRepository;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.entity.AuditLog;
import com.schoolerp.modules.system.repository.AuditLogRepository;
import com.schoolerp.modules.system.service.TenantModuleLicenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseDataInitializer implements CommandLineRunner {

    private final TenantRepository tenantRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;
    private final StudentDocumentRepository documentRepository;
    private final AdmissionRepository admissionRepository;
    private final ClassSectionRepository classSectionRepository;
    private final TimetablePeriodRepository timetableRepository;
    private final StudentMarkRepository markRepository;
    private final FeeInvoiceRepository invoiceRepository;
    private final FinancialLedgerEntryRepository ledgerRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final FeeConcessionRepository feeConcessionRepository;
    private final ChequeRecordRepository chequeRecordRepository;
    private final AuditLogRepository auditLogRepository;

    private final EmployeeRepository employeeRepository;
    private final StaffLeaveRequestRepository staffLeaveRequestRepository;
    private final VehicleRepository vehicleRepository;
    private final TransportRouteRepository transportRouteRepository;
    private final GpsPingRepository gpsPingRepository;
    private final BookRepository bookRepository;
    private final BookIssueRecordRepository bookIssueRecordRepository;
    private final CircularRepository circularRepository;
    private final NotificationDispatchLogRepository dispatchLogRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final TenantModuleLicenseService tenantModuleLicenseService;
    private final PayrollService payrollService;
    private final RouteStopGeofenceRepository geofenceRepository;
    private final com.schoolerp.modules.operations.service.GatePassService gatePassService;
    private final com.schoolerp.modules.operations.repository.GatePassRepository gatePassRepository;
    private final com.schoolerp.modules.students.service.InfirmaryService infirmaryService;
    private final com.schoolerp.modules.students.repository.InfirmaryVisitRepository infirmaryVisitRepository;
    private final com.schoolerp.modules.academics.service.HomeworkService homeworkService;
    private final com.schoolerp.modules.academics.repository.HomeworkAssignmentRepository homeworkAssignmentRepository;

    @Override
    public void run(String... args) {
        log.info("Starting enterprise multi-tenant seed sequence across 12 schemas...");

        String tenantId = "tenant_apex_mumbai_01";
        TenantContext.setTenantId(tenantId);

        // 1. Seed Tenant
        Tenant apexTenant = tenantRepository.findByTenantIdentifier(tenantId)
                .orElseGet(() -> tenantRepository.save(Tenant.builder()
                        .tenantIdentifier(tenantId)
                        .schoolName("Apex Academy (Mumbai)")
                        .subdomain("apex")
                        .affiliationNumber("CBSE #113042")
                        .status("ACTIVE")
                        .subscriptionTier("ENTERPRISE")
                        .studentCapacity(20000)
                        .build()));

        // Provision Odoo-style Dynamic Tenant Module Licenses
        tenantModuleLicenseService.seedDefaultModulesForTenant(tenantId);

        // 2. Seed Roles
        Role superAdminRole = roleRepository.findByName("SUPER_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("SUPER_ADMIN")
                        .description("Full enterprise access across all schemas")
                        .build()));

        Role teacherRole = roleRepository.findByName("TEACHER")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("TEACHER")
                        .description("Faculty with roll call and gradebook access")
                        .build()));

        Role accountantRole = roleRepository.findByName("ACCOUNTANT")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("ACCOUNTANT")
                        .description("Bursar with fee ledger access")
                        .build()));

        roleRepository.findByName("PRINCIPAL")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("PRINCIPAL")
                        .description("Director with lock and publish authorization")
                        .build()));

        // 3. Seed Users
        userRepository.findByEmail("admin@apexacademy.edu.in")
                .orElseGet(() -> userRepository.save(User.builder()
                        .tenantId(tenantId)
                        .email("admin@apexacademy.edu.in")
                        .passwordHash(passwordEncoder.encode("Admin123!"))
                        .fullName("Dr. Rajesh Sharma")
                        .phone("+91 98200 11223")
                        .roles(Set.of(superAdminRole))
                        .build()));

        userRepository.findByEmail("teacher@apexacademy.edu.in")
                .orElseGet(() -> userRepository.save(User.builder()
                        .tenantId(tenantId)
                        .email("teacher@apexacademy.edu.in")
                        .passwordHash(passwordEncoder.encode("Teacher123!"))
                        .fullName("Dr. Vikram Seth")
                        .phone("+91 98201 54321")
                        .roles(Set.of(teacherRole))
                        .build()));

        userRepository.findByEmail("accountant@apexacademy.edu.in")
                .orElseGet(() -> userRepository.save(User.builder()
                        .tenantId(tenantId)
                        .email("accountant@apexacademy.edu.in")
                        .passwordHash(passwordEncoder.encode("Accountant123!"))
                        .fullName("Suresh Mehta")
                        .phone("+91 98202 33445")
                        .roles(Set.of(accountantRole))
                        .build()));

        // 4. Seed Class Sections (Academics)
        if (classSectionRepository.count() == 0) {
            classSectionRepository.save(ClassSection.builder()
                    .tenantId(tenantId)
                    .className("Class 10")
                    .sectionName("Section A")
                    .classTeacherName("Dr. Vikram Seth")
                    .capacity(40)
                    .enrolledCount(38)
                    .build());

            classSectionRepository.save(ClassSection.builder()
                    .tenantId(tenantId)
                    .className("Class 10")
                    .sectionName("Section B")
                    .classTeacherName("Meera Nair")
                    .capacity(40)
                    .enrolledCount(40)
                    .build());

            classSectionRepository.save(ClassSection.builder()
                    .tenantId(tenantId)
                    .className("Class 11")
                    .sectionName("Section A")
                    .classTeacherName("Rajesh Kulkarni")
                    .capacity(45)
                    .enrolledCount(42)
                    .build());
        }

        // 5. Seed Students with Physical Document Custody
        if (studentRepository.count() == 0) {
            Student aarav = studentRepository.save(Student.builder()
                    .tenantId(tenantId)
                    .admissionNumber("ADM-2024-0012")
                    .fullName("Aarav Sharma")
                    .gender("Male")
                    .dateOfBirth(LocalDate.of(2010, 4, 12))
                    .className("Class 10")
                    .sectionName("Section A")
                    .rollNumber(1)
                    .guardianName("Rajesh Sharma")
                    .guardianPhone("+91 98765 43210")
                    .guardianEmail("rajesh.sharma@example.com")
                    .lockerIndex("Locker B-12")
                    .attendancePercentage(96.5)
                    .feeStatus("Paid")
                    .balanceAmount(0.0)
                    .build());

            Student ananya = studentRepository.save(Student.builder()
                    .tenantId(tenantId)
                    .admissionNumber("ADM-2024-0045")
                    .fullName("Ananya Deshmukh")
                    .gender("Female")
                    .dateOfBirth(LocalDate.of(2010, 8, 22))
                    .className("Class 10")
                    .sectionName("Section A")
                    .rollNumber(2)
                    .guardianName("Sunil Deshmukh")
                    .guardianPhone("+91 98111 22334")
                    .guardianEmail("sunil.deshmukh@example.com")
                    .lockerIndex("Locker B-14")
                    .attendancePercentage(98.2)
                    .feeStatus("Paid")
                    .balanceAmount(0.0)
                    .build());

            Student diya = studentRepository.save(Student.builder()
                    .tenantId(tenantId)
                    .admissionNumber("ADM-2024-0089")
                    .fullName("Diya Patel")
                    .gender("Female")
                    .dateOfBirth(LocalDate.of(2010, 11, 5))
                    .className("Class 10")
                    .sectionName("Section A")
                    .rollNumber(3)
                    .guardianName("Bhavesh Patel")
                    .guardianPhone("+91 98222 33445")
                    .guardianEmail("bhavesh.patel@example.com")
                    .lockerIndex("Locker B-15")
                    .attendancePercentage(95.0)
                    .feeStatus("Paid")
                    .balanceAmount(0.0)
                    .build());

            Student ishaan = studentRepository.save(Student.builder()
                    .tenantId(tenantId)
                    .admissionNumber("ADM-2024-0102")
                    .fullName("Ishaan Verma")
                    .gender("Male")
                    .dateOfBirth(LocalDate.of(2010, 2, 18))
                    .className("Class 10")
                    .sectionName("Section A")
                    .rollNumber(4)
                    .guardianName("Alok Verma")
                    .guardianPhone("+91 98333 44556")
                    .guardianEmail("alok.verma@example.com")
                    .lockerIndex("Locker B-18")
                    .attendancePercentage(88.4)
                    .feeStatus("Partial")
                    .balanceAmount(15000.0)
                    .build());

            Student rhea = studentRepository.save(Student.builder()
                    .tenantId(tenantId)
                    .admissionNumber("ADM-2025-0318")
                    .fullName("Rhea Sharma")
                    .gender("Female")
                    .dateOfBirth(LocalDate.of(2014, 5, 14))
                    .className("Class 6")
                    .sectionName("Section B")
                    .rollNumber(14)
                    .guardianName("Rajesh Sharma")
                    .guardianPhone("+91 98765 43210")
                    .guardianEmail("rajesh.sharma@example.com")
                    .lockerIndex("Locker C-08")
                    .attendancePercentage(98.0)
                    .feeStatus("Paid")
                    .balanceAmount(0.0)
                    .build());

            // Physical Document Custody records
            documentRepository.save(StudentDocument.builder()
                    .tenantId(tenantId)
                    .studentId(aarav.getId())
                    .documentType("TC_ORIGINAL")
                    .custodyStatus("ORIGINAL_IN_CUSTODY")
                    .lockerIndex("Locker B-12 / File #0842")
                    .verifierName("Admission Officer (Siddharth Rao)")
                    .build());

            documentRepository.save(StudentDocument.builder()
                    .tenantId(tenantId)
                    .studentId(aarav.getId())
                    .documentType("BIRTH_CERTIFICATE")
                    .custodyStatus("VERIFIED_COPY")
                    .lockerIndex("Locker B-12 / File #0842")
                    .verifierName("Admission Officer (Siddharth Rao)")
                    .build());

            documentRepository.save(StudentDocument.builder()
                    .tenantId(tenantId)
                    .studentId(ishaan.getId())
                    .documentType("TC_ORIGINAL")
                    .custodyStatus("PENDING")
                    .lockerIndex("Pending Deposit")
                    .verifierName("Pending")
                    .build());

            // 6. Seed Gradebook Marks for Aarav and Ananya
            markRepository.save(StudentMark.builder()
                    .tenantId(tenantId)
                    .termName("Term 1 Mid-Term")
                    .studentId(aarav.getId())
                    .studentName("Aarav Sharma")
                    .subject("Mathematics")
                    .theoryScore(74)
                    .practicalScore(19)
                    .internalScore(10)
                    .totalScore(94)
                    .gradeBand(GradebookService.calculateGradeBand(94))
                    .remarks("Exceptional mastery of quadratic equations")
                    .status("DRAFT")
                    .build());

            markRepository.save(StudentMark.builder()
                    .tenantId(tenantId)
                    .termName("Term 1 Mid-Term")
                    .studentId(ananya.getId())
                    .studentName("Ananya Deshmukh")
                    .subject("Mathematics")
                    .theoryScore(78)
                    .practicalScore(20)
                    .internalScore(10)
                    .totalScore(98)
                    .gradeBand(GradebookService.calculateGradeBand(98))
                    .remarks("Flawless geometric proofs and lab records")
                    .status("DRAFT")
                    .build());

            // 7. Seed Fee Invoices & Double-Entry Ledger
            FeeInvoice inv1 = invoiceRepository.save(FeeInvoice.builder()
                    .tenantId(tenantId)
                    .invoiceNumber("INV-2026-0842")
                    .studentId(aarav.getId())
                    .studentName("Aarav Sharma")
                    .totalAmount(new BigDecimal("45000.00"))
                    .paidAmount(new BigDecimal("45000.00"))
                    .balanceAmount(BigDecimal.ZERO)
                    .dueDate(LocalDate.of(2026, 11, 15))
                    .status("PAID")
                    .build());

            FeeInvoice inv2 = invoiceRepository.save(FeeInvoice.builder()
                    .tenantId(tenantId)
                    .invoiceNumber("INV-2026-0843")
                    .studentId(ishaan.getId())
                    .studentName("Ishaan Verma")
                    .totalAmount(new BigDecimal("45000.00"))
                    .paidAmount(new BigDecimal("30000.00"))
                    .balanceAmount(new BigDecimal("15000.00"))
                    .dueDate(LocalDate.of(2026, 11, 15))
                    .status("PARTIAL")
                    .build());

            // Immutable Double-Entry Ledger Postings
            ledgerRepository.save(FinancialLedgerEntry.builder()
                    .tenantId(tenantId)
                    .entryType("FEE_PAYMENT")
                    .accountCode("1020-BANK-HDFC")
                    .debit(new BigDecimal("45000.00"))
                    .credit(BigDecimal.ZERO)
                    .description("Full fee receipt collected for Aarav Sharma (INV-2026-0842)")
                    .referenceId(inv1.getInvoiceNumber())
                    .postedAt(Instant.now())
                    .build());

            ledgerRepository.save(FinancialLedgerEntry.builder()
                    .tenantId(tenantId)
                    .entryType("FEE_PAYMENT")
                    .accountCode("4010-TUITION-FEE")
                    .debit(BigDecimal.ZERO)
                    .credit(new BigDecimal("45000.00"))
                    .description("Tuition revenue recognized for Aarav Sharma (INV-2026-0842)")
                    .referenceId(inv1.getInvoiceNumber())
                    .postedAt(Instant.now())
                    .build());

            // Seed Fee Structures (Class 10 and Class 6)
            if (feeStructureRepository.count() == 0) {
                FeeStructure class10Structure = FeeStructure.builder()
                        .tenantId(tenantId)
                        .structureName("Class 10 Standard Annual Fee Structure 2026-27")
                        .gradeLevel("Class 10")
                        .academicYear("2026-27")
                        .totalAmount(new BigDecimal("45000.00"))
                        .status("ACTIVE")
                        .items(List.of(
                                FeeStructureItem.builder().categoryName("TUITION").amount(new BigDecimal("30000.00")).isOptional(false).frequency("TERMLY").build(),
                                FeeStructureItem.builder().categoryName("SCIENCE_LAB").amount(new BigDecimal("5000.00")).isOptional(false).frequency("TERMLY").build(),
                                FeeStructureItem.builder().categoryName("COMPUTER_LAB").amount(new BigDecimal("4000.00")).isOptional(false).frequency("TERMLY").build(),
                                FeeStructureItem.builder().categoryName("TRANSPORT").amount(new BigDecimal("6000.00")).isOptional(true).frequency("TERMLY").build()
                        ))
                        .build();
                feeStructureRepository.save(class10Structure);

                FeeStructure class6Structure = FeeStructure.builder()
                        .tenantId(tenantId)
                        .structureName("Class 6 Standard Annual Fee Structure 2026-27")
                        .gradeLevel("Class 6")
                        .academicYear("2026-27")
                        .totalAmount(new BigDecimal("38000.00"))
                        .status("ACTIVE")
                        .items(List.of(
                                FeeStructureItem.builder().categoryName("TUITION").amount(new BigDecimal("26000.00")).isOptional(false).frequency("TERMLY").build(),
                                FeeStructureItem.builder().categoryName("COMPUTER_LAB").amount(new BigDecimal("4000.00")).isOptional(false).frequency("TERMLY").build(),
                                FeeStructureItem.builder().categoryName("SPORTS_LIBRARY").amount(new BigDecimal("2000.00")).isOptional(false).frequency("TERMLY").build(),
                                FeeStructureItem.builder().categoryName("TRANSPORT").amount(new BigDecimal("6000.00")).isOptional(true).frequency("TERMLY").build()
                        ))
                        .build();
                feeStructureRepository.save(class6Structure);
            }

            // Seed Sibling Concession for Rhea Sharma (15% discount on Tuition fee)
            if (feeConcessionRepository.count() == 0) {
                feeConcessionRepository.save(FeeConcession.builder()
                        .tenantId(tenantId)
                        .studentId(rhea.getId())
                        .studentName("Rhea Sharma")
                        .concessionType("SIBLING_DISCOUNT")
                        .percentageDiscount(new BigDecimal("15.00"))
                        .flatDiscountAmount(BigDecimal.ZERO)
                        .applicableFeeHead("TUITION")
                        .reason("2nd child sibling concession (Elder sibling: Aarav Sharma ADM-2024-0012)")
                        .approvedBy("Executive Trustee (Dr. Rajesh Sharma)")
                        .status("ACTIVE")
                        .build());
            }

            // Seed Cheque Records
            if (chequeRecordRepository.count() == 0) {
                chequeRecordRepository.save(ChequeRecord.builder()
                        .tenantId(tenantId)
                        .chequeNumber("CHQ-448201")
                        .bankName("HDFC Bank")
                        .branchName("Bandra West, Mumbai")
                        .chequeDate(LocalDate.of(2026, 11, 2))
                        .amount(new BigDecimal("45000.00"))
                        .payerName("Rajesh Sharma")
                        .studentId(aarav.getId())
                        .invoiceNumber("INV-2026-0842")
                        .status("CLEARED")
                        .depositedDate(LocalDate.of(2026, 11, 3))
                        .clearedDate(LocalDate.of(2026, 11, 5))
                        .bouncePenalty(BigDecimal.ZERO)
                        .remarks("Term 1 full tuition clearance")
                        .build());

                chequeRecordRepository.save(ChequeRecord.builder()
                        .tenantId(tenantId)
                        .chequeNumber("CHQ-991204")
                        .bankName("State Bank of India")
                        .branchName("Andheri East, Mumbai")
                        .chequeDate(LocalDate.of(2026, 11, 7))
                        .amount(new BigDecimal("15000.00"))
                        .payerName("Alok Verma")
                        .studentId(ishaan.getId())
                        .invoiceNumber("INV-2026-0843")
                        .status("RECEIVED")
                        .depositedDate(null)
                        .clearedDate(null)
                        .bouncePenalty(BigDecimal.ZERO)
                        .remarks("Term 1 balance installment cheque under processing")
                        .build());
            }
        }

        // 8. Seed Admissions Pipeline
        if (admissionRepository.count() == 0) {
            admissionRepository.save(AdmissionApplication.builder()
                    .tenantId(tenantId)
                    .applicationNumber("APP-2026-0891")
                    .studentName("Reyansh Singhania")
                    .targetClass("Class 11 - Science")
                    .parentName("Vikram Singhania")
                    .parentPhone("+91 98200 44556")
                    .status("INTERVIEW")
                    .documentStatus("COMPLETE")
                    .build());

            admissionRepository.save(AdmissionApplication.builder()
                    .tenantId(tenantId)
                    .applicationNumber("APP-2026-0904")
                    .studentName("Tara Mukherjee")
                    .targetClass("Class 1 - Daisy")
                    .parentName("Amit Mukherjee")
                    .parentPhone("+91 98201 77889")
                    .status("UNDER_REVIEW")
                    .documentStatus("PENDING_DOCS")
                    .build());
        }

        // 9. Seed Timetable Periods
        if (timetableRepository.count() == 0) {
            timetableRepository.save(TimetablePeriod.builder()
                    .tenantId(tenantId)
                    .dayOfWeek("Monday")
                    .periodNumber(1)
                    .timeSlot("08:30 – 09:15")
                    .subject("Homeroom & Roll Call")
                    .className("Class 10")
                    .sectionName("Section A")
                    .teacherName("Dr. Vikram Seth")
                    .roomNumber("Room 301")
                    .build());

            timetableRepository.save(TimetablePeriod.builder()
                    .tenantId(tenantId)
                    .dayOfWeek("Monday")
                    .periodNumber(2)
                    .timeSlot("09:15 – 10:00")
                    .subject("Advanced Mathematics")
                    .className("Class 10")
                    .sectionName("Section A")
                    .teacherName("Dr. Vikram Seth")
                    .roomNumber("Room 301")
                    .build());

            timetableRepository.save(TimetablePeriod.builder()
                    .tenantId(tenantId)
                    .dayOfWeek("Monday")
                    .periodNumber(4)
                    .timeSlot("11:00 – 11:45")
                    .subject("Calculus & Algebra")
                    .className("Class 11")
                    .sectionName("Section A")
                    .teacherName("Dr. Vikram Seth")
                    .roomNumber("Room 402")
                    .build());
        }

        // 10. Seed Security Audit Logs
        if (auditLogRepository.count() == 0) {
            auditLogRepository.save(AuditLog.builder()
                    .tenantId(tenantId)
                    .actorName("Dr. Rajesh Sharma")
                    .actorRole("SUPER_ADMIN")
                    .action("LOGIN")
                    .resourceType("identity.auth_token")
                    .resourceId("AUTH-SESSION-001")
                    .details("Super admin login from enterprise intranet")
                    .clientIp("10.0.4.12")
                    .status("SUCCESS")
                    .build());

            auditLogRepository.save(AuditLog.builder()
                    .tenantId(tenantId)
                    .actorName("Siddharth Rao")
                    .actorRole("ADMISSIONS_OFFICER")
                    .action("UPDATE")
                    .resourceType("admissions.document_custody")
                    .resourceId("DOC-2026-0842")
                    .details("Marked Original TC and Birth Certificate as deposited in Locker B-12")
                    .clientIp("10.0.4.15")
                    .status("SUCCESS")
                    .build());
        }

        // 11. Seed Staff & Human Resources
        if (employeeRepository.count() == 0) {
            Employee emp1 = employeeRepository.save(Employee.builder()
                    .tenantId(tenantId)
                    .employeeCode("EMP-2024-0010")
                    .fullName("Dr. Vikram Seth")
                    .email("teacher@apexacademy.edu.in")
                    .phone("+91 98201 54321")
                    .department("Science")
                    .designation("PGT Physics & Senior Faculty")
                    .employmentType("FULL_TIME")
                    .joiningDate(LocalDate.of(2021, 6, 1))
                    .panNumber("ABCDE1234F")
                    .bankAccountNumber("HDFC000123456789")
                    .status("ACTIVE")
                    .build());

            employeeRepository.save(Employee.builder()
                    .tenantId(tenantId)
                    .employeeCode("EMP-2024-0011")
                    .fullName("Sunita Rao")
                    .email("sunita.rao@apexacademy.edu.in")
                    .phone("+91 98201 67890")
                    .department("Mathematics")
                    .designation("TGT Mathematics")
                    .employmentType("FULL_TIME")
                    .joiningDate(LocalDate.of(2022, 7, 15))
                    .status("ACTIVE")
                    .build());

            employeeRepository.save(Employee.builder()
                    .tenantId(tenantId)
                    .employeeCode("EMP-2024-0012")
                    .fullName("Meera Joshi")
                    .email("meera.joshi@apexacademy.edu.in")
                    .phone("+91 98202 77889")
                    .department("Administration")
                    .designation("Chief Librarian")
                    .employmentType("FULL_TIME")
                    .joiningDate(LocalDate.of(2019, 4, 10))
                    .status("ACTIVE")
                    .build());

            staffLeaveRequestRepository.save(StaffLeaveRequest.builder()
                    .tenantId(tenantId)
                    .employeeId(emp1.getId())
                    .employeeName(emp1.getFullName())
                    .leaveType("CASUAL")
                    .startDate(LocalDate.now().plusDays(2))
                    .endDate(LocalDate.now().plusDays(3))
                    .reason("Attending CBSE Regional Academic Pedagogy Conference")
                    .status("PENDING")
                    .build());
        }

        // 12. Seed Operations (Transport Fleet & Library)
        if (vehicleRepository.count() == 0) {
            vehicleRepository.save(Vehicle.builder()
                    .tenantId(tenantId)
                    .registrationNumber("MH-02-CB-4012")
                    .vehicleModel("Tata Starbus 42-Seater AC")
                    .seatingCapacity(42)
                    .driverName("Ramesh Yadav")
                    .driverPhone("+91 98334 11223")
                    .gpsDeviceId("GPS-TK103-994")
                    .status("ACTIVE")
                    .build());

            vehicleRepository.save(Vehicle.builder()
                    .tenantId(tenantId)
                    .registrationNumber("MH-02-CB-4015")
                    .vehicleModel("Ashok Leyland 36-Seater")
                    .seatingCapacity(36)
                    .driverName("Suresh Patil")
                    .driverPhone("+91 98334 44556")
                    .gpsDeviceId("GPS-TK103-995")
                    .status("ACTIVE")
                    .build());

            transportRouteRepository.save(TransportRoute.builder()
                    .tenantId(tenantId)
                    .routeName("Route 7 - Andheri West to Campus")
                    .vehicleRegistration("MH-02-CB-4012")
                    .driverName("Ramesh Yadav")
                    .driverPhone("+91 98334 11223")
                    .stopsList("Lokhandwala Complex (06:45 AM) -> Infinity Mall (07:05 AM) -> DN Nagar Metro (07:20 AM) -> Campus Gate 1 (07:45 AM)")
                    .morningStartTime("06:45 AM")
                    .eveningStartTime("03:15 PM")
                    .status("OPERATIONAL")
                    .build());

            gpsPingRepository.save(GpsPing.builder()
                    .tenantId(tenantId)
                    .vehicleRegistration("MH-02-CB-4012")
                    .latitude(19.1415)
                    .longitude(72.8312)
                    .speedKmph(38.5)
                    .headingDegrees(145.0)
                    .build());
        }

        if (bookRepository.count() == 0) {
            Book book1 = bookRepository.save(Book.builder()
                    .tenantId(tenantId)
                    .title("NCERT Physics for Class 10")
                    .author("NCERT Editorial Board")
                    .isbn("978-81-7450-492-0")
                    .category("Science")
                    .totalCopies(25)
                    .availableCopies(24)
                    .rackLocation("Shelf C-4 / Science")
                    .barcodeTag("BC-SCI-1042")
                    .build());

            bookRepository.save(Book.builder()
                    .tenantId(tenantId)
                    .title("RD Sharma Mathematics Class 10")
                    .author("Dr. R.D. Sharma")
                    .isbn("978-93-5144-884-6")
                    .category("Mathematics")
                    .totalCopies(30)
                    .availableCopies(30)
                    .rackLocation("Shelf M-2 / Mathematics")
                    .barcodeTag("BC-MATH-2015")
                    .build());

            Book book3 = bookRepository.save(Book.builder()
                    .tenantId(tenantId)
                    .title("Wings of Fire")
                    .author("Dr. A.P.J. Abdul Kalam")
                    .isbn("978-81-7371-146-6")
                    .category("Biography")
                    .totalCopies(15)
                    .availableCopies(14)
                    .rackLocation("Shelf BIO-1")
                    .barcodeTag("BC-BIO-3001")
                    .build());

            if (studentRepository.count() > 0) {
                Student sampleStudent = studentRepository.findAll().get(0);
                bookIssueRecordRepository.save(BookIssueRecord.builder()
                        .tenantId(tenantId)
                        .bookId(book3.getId())
                        .bookTitle(book3.getTitle())
                        .studentId(sampleStudent.getId())
                        .studentName(sampleStudent.getFullName())
                        .issuedDate(LocalDate.now().minusDays(3))
                        .dueDate(LocalDate.now().plusDays(11))
                        .status("ISSUED")
                        .fineAmount(BigDecimal.ZERO)
                        .build());
            }
        }

        // 13. Seed Communication (Circulars & Notification Dispatches)
        if (circularRepository.count() == 0) {
            circularRepository.save(Circular.builder()
                    .tenantId(tenantId)
                    .title("Annual Inter-School CBSE Science Olympiad 2026")
                    .content("Students of Classes 9, 10, and 11 are invited to register for the national qualifiers by November 20. Preliminary rounds will be conducted in the Physics & Chemistry Senior Labs.")
                    .targetAudience("ALL")
                    .priority("IMPORTANT")
                    .attachmentUrl("s3://apex-docs/circulars/olympiad-guidelines-2026.pdf")
                    .publishedBy("Dr. Vikram Seth (Head of Science)")
                    .publishedDate(LocalDate.now().minusDays(1))
                    .status("PUBLISHED")
                    .build());

            circularRepository.save(Circular.builder()
                    .tenantId(tenantId)
                    .title("Term 1 Parent-Teacher Meeting (PTM) Schedule")
                    .content("The mandatory Term 1 PTM for Class 10 Sections A & B will be held this Saturday between 09:00 AM and 01:00 PM. Teachers will review continuous assessment portfolios.")
                    .targetAudience("PARENTS")
                    .targetGradeLevel("Class 10")
                    .priority("URGENT")
                    .publishedBy("Principal Office")
                    .publishedDate(LocalDate.now())
                    .status("PUBLISHED")
                    .build());

            dispatchLogRepository.save(NotificationDispatchLog.builder()
                    .tenantId(tenantId)
                    .recipientName("Rajesh Sharma (Father of Aarav)")
                    .recipientIdentifier("+91 98765 43210")
                    .recipientType("PARENT")
                    .channel("WHATSAPP_BUSINESS")
                    .triggerEvent("CIRCULAR_ALERT")
                    .messageBody("Apex Academy Alert: PTM Schedule for Class 10 has been published on parent portal. Date: Saturday 09:00 AM.")
                    .providerMessageId("wamid.HBgLMTc4NDQ2")
                    .deliveryStatus("DELIVERED")
                    .build());

            dispatchLogRepository.save(NotificationDispatchLog.builder()
                    .tenantId(tenantId)
                    .recipientName("Alok Verma (Father of Ishaan)")
                    .recipientIdentifier("+91 98333 44556")
                    .recipientType("PARENT")
                    .channel("SMS_DLT")
                    .triggerEvent("FEE_DUE_REMINDER")
                    .messageBody("Reminder: Outstanding fee invoice INV-2026-0843 of INR 15,000.00 is due on Nov 15. Please pay online via parent portal.")
                    .providerMessageId("sms_msg_994812")
                    .deliveryStatus("DELIVERED")
                    .build());
        }

        // 14. Seed Mobile Parent App Device Tokens
        if (deviceTokenRepository.count() == 0) {
            deviceTokenRepository.save(DeviceToken.builder()
                    .tenantId(tenantId)
                    .userId(UUID.randomUUID())
                    .userEmail("rajesh.sharma@example.com")
                    .userRole("PARENT")
                    .deviceToken("fcm_token_samsung_s24_apex_9921_alpha_secret")
                    .deviceType("ANDROID")
                    .deviceModel("Samsung Galaxy S24 Ultra")
                    .appVersion("1.0.0-parent-native")
                    .status("ACTIVE")
                    .build());
        }

        // 15. Seed Route Stop Geofences for Samsara Telematics
        if (geofenceRepository.count() == 0 && transportRouteRepository.count() > 0) {
            TransportRoute route7 = transportRouteRepository.findByTenantId(tenantId).get(0);
            geofenceRepository.save(RouteStopGeofence.builder()
                    .tenantId(tenantId)
                    .routeId(route7.getId())
                    .stopName("Lokhandwala Circle Stop")
                    .stopSequence(1)
                    .latitude(19.1415)
                    .longitude(72.8312)
                    .radiusMeters(800.0)
                    .scheduledMorningTime("06:45 AM")
                    .scheduledEveningTime("03:30 PM")
                    .build());

            geofenceRepository.save(RouteStopGeofence.builder()
                    .tenantId(tenantId)
                    .routeId(route7.getId())
                    .stopName("DN Nagar Metro Stop")
                    .stopSequence(2)
                    .latitude(19.1308)
                    .longitude(72.8340)
                    .radiusMeters(800.0)
                    .scheduledMorningTime("07:15 AM")
                    .scheduledEveningTime("03:50 PM")
                    .build());
        }

        // 16. Seed Staff Payroll & Biometric Turnstile Punches
        if (employeeRepository.count() > 0) {
            Employee vikram = employeeRepository.findAllByTenantId(tenantId).stream()
                    .filter(e -> e.getFullName().contains("Vikram"))
                    .findFirst()
                    .orElse(null);

            if (vikram != null) {
                payrollService.recordPunch(vikram.getId(), "IN", "FACIAL_RECOGNITION", "10.0.8.21");
                payrollService.configureSalaryStructure(vikram.getId(), new BigDecimal("60000.00"), new BigDecimal("20000.00"));
                payrollService.generateMonthlyPayroll("2026-11");
            }
        }

        // 17. Seed QR Security Gate Pass
        if (gatePassRepository.count() == 0 && studentRepository.count() > 0) {
            Student aarav = studentRepository.findAllByTenantId(tenantId).stream()
                    .filter(s -> s.getFullName().contains("Aarav"))
                    .findFirst()
                    .orElse(null);

            if (aarav != null) {
                gatePassService.issueGatePass(
                        aarav.getId(),
                        aarav.getFullName(),
                        aarav.getClassName() + " - " + aarav.getSectionName(),
                        aarav.getGuardianName(),
                        aarav.getGuardianPhone(),
                        "FATHER",
                        "DOCTOR_APPOINTMENT",
                        "Dr. Rajesh Sharma (Principal Desk)"
                );
            }
        }

        // 18. Seed Infirmary Clinical Triage
        if (infirmaryVisitRepository.count() == 0 && studentRepository.count() > 0) {
            Student ishaan = studentRepository.findAllByTenantId(tenantId).stream()
                    .filter(s -> s.getFullName().contains("Ishaan"))
                    .findFirst()
                    .orElse(null);

            if (ishaan != null) {
                infirmaryService.recordVisit(
                        ishaan.getId(),
                        "FEVER",
                        101.4,
                        "115/75",
                        82,
                        "Paracetamol 250mg oral, cold compress sponge, oral hydration",
                        45,
                        "SENT_HOME_WITH_PARENT",
                        "Sister Mary D'Souza, RN"
                );
            }
        }

        // 19. Seed Homework & Submissions
        if (homeworkAssignmentRepository.count() == 0) {
            var hw = homeworkService.createAssignment(
                    "Class 10",
                    "Section A",
                    "Mathematics",
                    "Quadratic Equations & Roots Working",
                    "Solve Exercise 4.2 Questions 1-12. Show complete discriminant steps.",
                    "Sunita Rao (TGT Mathematics)",
                    LocalDate.now().plusDays(2),
                    20.0,
                    "https://cdn.apexacademy.edu.in/assignments/math-ch4.pdf"
            );

            Student aarav = studentRepository.findAllByTenantId(tenantId).stream()
                    .filter(s -> s.getFullName().contains("Aarav"))
                    .findFirst()
                    .orElse(null);

            if (aarav != null) {
                var sub = homeworkService.submitHomework(hw.getId(), aarav.getId(), "https://cdn.apexacademy.edu.in/submissions/aarav_math_hw.pdf");
                homeworkService.gradeSubmission(sub.getId(), 19.5, "Flawless discriminant derivation and quadratic formula application.", "Sunita Rao");
            }
        }

        log.info("Multi-tenant enterprise seed sequence completed successfully.");
    }
}
