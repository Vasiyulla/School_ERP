package com.schoolerp.modules.students.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.entity.StudentCertificateRecord;
import com.schoolerp.modules.students.entity.StudentUdiseProfile;
import com.schoolerp.modules.students.repository.StudentCertificateRecordRepository;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.students.repository.StudentUdiseProfileRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.Year;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndianComplianceService {

    private final StudentUdiseProfileRepository udiseRepository;
    private final StudentCertificateRecordRepository certificateRepository;
    private final StudentRepository studentRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public List<StudentUdiseProfile> getUdiseProfiles() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<StudentUdiseProfile> profiles = udiseRepository.findByTenantId(tenantId);
        if (profiles.isEmpty()) {
            List<Student> students = studentRepository.findAllByTenantId(tenantId);
            long penSeq = 20261109001L;
            long apaarSeq = 984210842901L;

            for (Student s : students) {
                StudentUdiseProfile p = StudentUdiseProfile.builder()
                        .tenantId(tenantId)
                        .studentId(s.getId())
                        .studentName(s.getFullName())
                        .admissionNumber(s.getAdmissionNumber())
                        .apaarId(formatApaar(apaarSeq++))
                        .penNumber(String.valueOf(penSeq++))
                        .socialCategory("GENERAL")
                        .religion("HINDU")
                        .motherTongue("HINDI")
                        .bplStatus(false)
                        .cwsnDisabilityCode("NONE")
                        .rteAdmission(false)
                        .build();
                udiseRepository.save(p);
            }
            return udiseRepository.findByTenantId(tenantId);
        }
        return profiles;
    }

    private String formatApaar(long raw) {
        String s = String.valueOf(raw);
        if (s.length() >= 12) {
            return s.substring(0, 4) + "-" + s.substring(4, 8) + "-" + s.substring(8, 12);
        }
        return "9842-1084-2901";
    }

    @Transactional
    public StudentCertificateRecord generateCertificate(UUID studentId, String certificateType, String purpose, String issuedBy) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        Student student = studentRepository.findById(studentId)
                .filter(s -> s.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        String certType = certificateType != null ? certificateType.toUpperCase() : "BONAFIDE";
        String currentYear = String.valueOf(Year.now().getValue());
        String certNumber = String.format("APEX/CERT/%s/%04d", currentYear, (int)(Math.random() * 9000) + 1000);

        String rawToken = tenantId + ":" + student.getId() + ":" + certType + ":" + certNumber + ":" + System.currentTimeMillis();
        String qrToken;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            qrToken = HexFormat.of().formatHex(md.digest(rawToken.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            qrToken = UUID.randomUUID().toString().replace("-", "");
        }

        StudentCertificateRecord record = StudentCertificateRecord.builder()
                .tenantId(tenantId)
                .certificateType(certType)
                .certificateNumber(certNumber)
                .studentId(student.getId())
                .studentName(student.getFullName())
                .className(student.getClassName() != null ? student.getClassName() : "Class 10")
                .purpose(purpose != null ? purpose : "Official institutional verification")
                .issuedTo(student.getFullName())
                .qrVerificationToken(qrToken)
                .issuedAt(Instant.now())
                .issuedBy(issuedBy != null ? issuedBy : "Office of the Principal")
                .build();

        StudentCertificateRecord saved = certificateRepository.save(record);

        auditLogService.recordEvent(
                "ISSUE_STUDENT_CERTIFICATE",
                "students.certificates",
                saved.getId().toString(),
                String.format("Issued %s certificate (%s) for %s. Purpose: %s",
                        certType, certNumber, student.getFullName(), saved.getPurpose())
        );

        return saved;
    }

    public CertificateVerificationDTO verifyCertificate(String qrToken) {
        StudentCertificateRecord record = certificateRepository.findByQrVerificationToken(qrToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid certificate verification token"));

        return CertificateVerificationDTO.builder()
                .isValid(true)
                .certificateNumber(record.getCertificateNumber())
                .certificateType(record.getCertificateType())
                .studentName(record.getStudentName())
                .className(record.getClassName())
                .purpose(record.getPurpose())
                .issuedAt(record.getIssuedAt())
                .issuedBy(record.getIssuedBy())
                .verificationStatus("AUTHENTIC_DIGITALLY_VERIFIED")
                .build();
    }

    public UdiseExportSummary exportUdiseData() {
        List<StudentUdiseProfile> profiles = getUdiseProfiles();
        return UdiseExportSummary.builder()
                .academicYear("2026-2027")
                .schoolUdiseCode("27240801242")
                .totalEnrolledStudents(profiles.size())
                .apaarRegisteredStudents((int) profiles.stream().filter(p -> p.getApaarId() != null).count())
                .cwsnStudents((int) profiles.stream().filter(p -> !"NONE".equalsIgnoreCase(p.getCwsnDisabilityCode())).count())
                .rteStudents((int) profiles.stream().filter(StudentUdiseProfile::isRteAdmission).count())
                .profiles(profiles)
                .build();
    }

    @Data
    @Builder
    public static class CertificateVerificationDTO {
        private boolean isValid;
        private String certificateNumber;
        private String certificateType;
        private String studentName;
        private String className;
        private String purpose;
        private Instant issuedAt;
        private String issuedBy;
        private String verificationStatus;
    }

    @Data
    @Builder
    public static class UdiseExportSummary {
        private String academicYear;
        private String schoolUdiseCode;
        private int totalEnrolledStudents;
        private int apaarRegisteredStudents;
        private int cwsnStudents;
        private int rteStudents;
        private List<StudentUdiseProfile> profiles;
    }
}

