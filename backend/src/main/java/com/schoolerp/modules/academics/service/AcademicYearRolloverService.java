package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.entity.AcademicYearRollover;
import com.schoolerp.modules.academics.repository.AcademicYearRolloverRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcademicYearRolloverService {

    private final StudentRepository studentRepository;
    private final AcademicYearRolloverRepository rolloverRepository;
    private final AuditLogService auditLogService;

    private static final Pattern GRADE_NUM_PATTERN = Pattern.compile("(\\d+)");

    @Transactional
    public AcademicYearRollover executeBatchRollover(String fromAcademicYear, String toAcademicYear, String executedBy) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        List<Student> activeStudents = studentRepository.findAllByTenantId(tenantId).stream()
                .filter(s -> "Active".equalsIgnoreCase(s.getStatus()) || "ACTIVE".equalsIgnoreCase(s.getStatus()))
                .toList();

        int promoted = 0;
        int graduated = 0;
        int retained = 0;

        for (Student s : activeStudents) {
            String currentClass = s.getClassName() != null ? s.getClassName() : "Class 10";

            Matcher matcher = GRADE_NUM_PATTERN.matcher(currentClass);
            if (matcher.find()) {
                int currentGrade = Integer.parseInt(matcher.group(1));

                if (currentGrade >= 12) {
                    // Terminal grade transitions to Graduated Alumni
                    s.setStatus("Graduated");
                    graduated++;
                } else {
                    // Promote to next academic grade
                    int nextGrade = currentGrade + 1;
                    s.setClassName("Class " + nextGrade);
                    promoted++;
                }
            } else {
                retained++;
            }
            studentRepository.save(s);
        }

        String notes = String.format("Batch promotion executed from %s to %s: %d students promoted, %d graduated to alumni, %d retained.",
                fromAcademicYear, toAcademicYear, promoted, graduated, retained);

        AcademicYearRollover rollover = AcademicYearRollover.builder()
                .tenantId(tenantId)
                .fromAcademicYear(fromAcademicYear)
                .toAcademicYear(toAcademicYear)
                .totalStudentsEvaluated(activeStudents.size())
                .promotedCount(promoted)
                .graduatedCount(graduated)
                .retainedCount(retained)
                .executedBy(executedBy != null ? executedBy : "Principal Office")
                .executedAt(Instant.now())
                .status("COMPLETED")
                .summaryNotes(notes)
                .build();

        AcademicYearRollover saved = rolloverRepository.save(rollover);

        auditLogService.recordEvent(
                "ACADEMIC_ROLLOVER",
                "academics.rollovers",
                saved.getId().toString(),
                notes
        );

        log.info("Academic year rollover completed for tenant {}: {}", tenantId, notes);
        return saved;
    }

    public List<AcademicYearRollover> getRolloverHistory() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return rolloverRepository.findByTenantIdOrderByExecutedAtDesc(tenantId);
    }
}

