package com.schoolerp.modules.attendance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.attendance.entity.AttendanceRecord;
import com.schoolerp.modules.attendance.repository.AttendanceRecordRepository;
import com.schoolerp.modules.attendance.repository.AttendanceSessionRepository;
import com.schoolerp.modules.communication.service.CommunicationService;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceComplianceService {

    private final AttendanceRecordRepository recordRepository;
    private final AttendanceSessionRepository sessionRepository;
    private final StudentRepository studentRepository;
    private final CommunicationService communicationService;
    private final AuditLogService auditLogService;

    public static final double CBSE_MINIMUM_ATTENDANCE_PERCENT = 75.0;

    public StudentComplianceReport evaluateStudentCompliance(UUID studentId) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        Student student = studentRepository.findById(studentId)
                .filter(s -> s.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        List<AttendanceRecord> records = recordRepository.findByTenantIdAndStudentId(tenantId, studentId);
        int totalSessions = records.size();

        if (totalSessions == 0) {
            // Default baseline if no sessions yet recorded
            return StudentComplianceReport.builder()
                    .studentId(student.getId())
                    .studentName(student.getFullName())
                    .admissionNumber(student.getAdmissionNumber())
                    .className(student.getClassName())
                    .totalInstructionalDays(0)
                    .presentDays(0)
                    .absentDays(0)
                    .lateDays(0)
                    .attendancePercentage(100.0)
                    .isEligibleForBoardExams(true)
                    .warningStatus("NORMAL")
                    .requiredConsecutiveDaysToRecover(0)
                    .build();
        }

        int present = 0;
        int absent = 0;
        int late = 0;

        for (AttendanceRecord r : records) {
            String status = r.getStatus() != null ? r.getStatus().toUpperCase() : "ABSENT";
            switch (status) {
                case "PRESENT" -> present++;
                case "LATE" -> {
                    late++;
                    present++; // CBSE counts late as attended
                }
                case "HALF_DAY" -> present++;
                default -> absent++;
            }
        }

        double pct = (double) present / totalSessions * 100.0;
        double roundedPct = BigDecimal.valueOf(pct).setScale(1, RoundingMode.HALF_UP).doubleValue();
        boolean isEligible = roundedPct >= CBSE_MINIMUM_ATTENDANCE_PERCENT;

        // Calculate days to recover: (present + x) / (totalSessions + x) >= 0.75
        // present + x >= 0.75*totalSessions + 0.75*x
        // 0.25*x >= 0.75*totalSessions - present
        // x >= 3*totalSessions - 4*present
        int daysToRecover = 0;
        if (!isEligible) {
            daysToRecover = Math.max(1, (3 * totalSessions) - (4 * present));
        }

        String warningStatus = "NORMAL";
        if (roundedPct < 65.0) {
            warningStatus = "CRITICAL_DEBARMENT_RISK";
        } else if (roundedPct < 75.0) {
            warningStatus = "CBSE_DEFICIT_WARNING";
        }

        return StudentComplianceReport.builder()
                .studentId(student.getId())
                .studentName(student.getFullName())
                .admissionNumber(student.getAdmissionNumber())
                .className(student.getClassName())
                .totalInstructionalDays(totalSessions)
                .presentDays(present)
                .absentDays(absent)
                .lateDays(late)
                .attendancePercentage(roundedPct)
                .isEligibleForBoardExams(isEligible)
                .warningStatus(warningStatus)
                .requiredConsecutiveDaysToRecover(daysToRecover)
                .build();
    }

    public List<StudentComplianceReport> getDefaulters(String className, Double threshold) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        double targetThreshold = threshold != null ? threshold : CBSE_MINIMUM_ATTENDANCE_PERCENT;

        List<Student> students = studentRepository.findAllByTenantId(tenantId);
        if (className != null && !className.isBlank()) {
            students = students.stream().filter(s -> className.equalsIgnoreCase(s.getClassName())).toList();
        }

        List<StudentComplianceReport> defaulters = new ArrayList<>();
        for (Student s : students) {
            StudentComplianceReport report = evaluateStudentCompliance(s.getId());
            if (report.getAttendancePercentage() < targetThreshold) {
                defaulters.add(report);
            }
        }

        return defaulters;
    }

    @Transactional
    public List<StudentComplianceReport> dispatchDebarmentWarnings(String className, Double threshold) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<StudentComplianceReport> defaulters = getDefaulters(className, threshold);

        for (StudentComplianceReport d : defaulters) {
            String warningNotice = String.format(
                    "CBSE Attendance Debarment Notice: %s (Adm: %s, Class: %s) has an attendance of %.1f%% (Mandatory: 75.0%%). Requires %d consecutive school days to regain eligibility. Please contact Principal.",
                    d.getStudentName(), d.getAdmissionNumber(), d.getClassName(), d.getAttendancePercentage(), d.getRequiredConsecutiveDaysToRecover()
            );

            // Send Push / WhatsApp Notice
            communicationService.sendPushNotification(
                    d.getStudentId(),
                    d.getStudentName(),
                    "CBSE 75% Attendance Warning",
                    warningNotice,
                    "ATTENDANCE_DEBARMENT_WARNING"
            );

            auditLogService.logAction("ATTENDANCE_OFFICER", "DISPATCH_DEBARMENT_WARNING", "attendance.compliance", d.getStudentId().toString(),
                    "Dispatched CBSE attendance debarment warning to guardian of " + d.getStudentName() + " (" + d.getAttendancePercentage() + "%)");
        }

        return defaulters;
    }

    @Data
    @Builder
    public static class StudentComplianceReport {
        private UUID studentId;
        private String studentName;
        private String admissionNumber;
        private String className;
        private int totalInstructionalDays;
        private int presentDays;
        private int absentDays;
        private int lateDays;
        private double attendancePercentage;
        private boolean isEligibleForBoardExams;
        private String warningStatus;
        private int requiredConsecutiveDaysToRecover;
    }
}

