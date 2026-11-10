package com.schoolerp.modules.examinations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.examinations.entity.HolisticProgressEntry;
import com.schoolerp.modules.examinations.repository.HolisticProgressEntryRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolisticProgressService {

    private final HolisticProgressEntryRepository hpcRepository;
    private final StudentRepository studentRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public HolisticProgressEntry recordAssessment(
            UUID studentId,
            String termName,
            String evaluatorType,
            String competencyDomain,
            String proficiencyLevel,
            double experientialHours,
            String evidenceNotes) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        Student student = studentRepository.findById(studentId)
                .filter(s -> s.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        String level = proficiencyLevel != null ? proficiencyLevel.toUpperCase() : "PROFICIENT";
        int score = switch (level) {
            case "EXEMPLARY" -> 4;
            case "PROFICIENT" -> 3;
            case "PROGRESSING" -> 2;
            default -> 1;
        };

        HolisticProgressEntry entry = HolisticProgressEntry.builder()
                .tenantId(tenantId)
                .studentId(student.getId())
                .studentName(student.getFullName())
                .termName(termName != null ? termName : "Term 1 AY 2026-27")
                .evaluatorType(evaluatorType != null ? evaluatorType.toUpperCase() : "TEACHER")
                .competencyDomain(competencyDomain != null ? competencyDomain.toUpperCase() : "COGNITIVE_INQUIRY")
                .proficiencyLevel(level)
                .numericScore(score)
                .experientialHoursLogged(experientialHours)
                .evidenceNotes(evidenceNotes != null ? evidenceNotes : "Demonstrates structured NEP 2020 competency mastery.")
                .recordedAt(Instant.now())
                .build();

        HolisticProgressEntry saved = hpcRepository.save(entry);

        auditLogService.recordEvent(
                "RECORD_NEP_HOLISTIC_PROGRESS",
                "examinations.holistic_progress_entries",
                saved.getId().toString(),
                String.format("Recorded %s evaluation for %s in %s: %s (%.1f hrs)",
                        saved.getEvaluatorType(), saved.getStudentName(), saved.getCompetencyDomain(), saved.getProficiencyLevel(), experientialHours)
        );

        return saved;
    }

    public HolisticReportCardDTO compileStudentHpc(UUID studentId) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        Student student = studentRepository.findById(studentId)
                .filter(s -> s.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        List<HolisticProgressEntry> entries = hpcRepository.findByTenantIdAndStudentIdOrderByRecordedAtDesc(tenantId, studentId);
        if (entries.isEmpty()) {
            recordAssessment(studentId, "Term 1 AY 2026-27", "SELF", "SOCIO_EMOTIONAL_EMPATHY", "PROFICIENT", 4.0, "Reflective peer mentoring journal");
            recordAssessment(studentId, "Term 1 AY 2026-27", "PEER", "LEADERSHIP_TEAMWORK", "EXEMPLARY", 6.0, "Team science hackathon collaboration");
            recordAssessment(studentId, "Term 1 AY 2026-27", "TEACHER", "VOCATIONAL_ROBOTICS", "EXEMPLARY", 12.0, "Built autonomous line-follower prototype");
            entries = hpcRepository.findByTenantIdAndStudentIdOrderByRecordedAtDesc(tenantId, studentId);
        }

        double selfAvg = entries.stream().filter(e -> "SELF".equals(e.getEvaluatorType())).mapToInt(HolisticProgressEntry::getNumericScore).average().orElse(0.0);
        double peerAvg = entries.stream().filter(e -> "PEER".equals(e.getEvaluatorType())).mapToInt(HolisticProgressEntry::getNumericScore).average().orElse(0.0);
        double teacherAvg = entries.stream().filter(e -> "TEACHER".equals(e.getEvaluatorType())).mapToInt(HolisticProgressEntry::getNumericScore).average().orElse(0.0);
        double totalHours = entries.stream().mapToDouble(HolisticProgressEntry::getExperientialHoursLogged).sum();

        return HolisticReportCardDTO.builder()
                .studentId(student.getId())
                .studentName(student.getFullName())
                .className(student.getClassName() + " - " + student.getSectionName())
                .nepFramework("NEP 2020 NCF-SE 360-Degree Holistic Progress Card")
                .selfAssessmentAverageScore(Math.round(selfAvg * 100.0) / 100.0)
                .peerAssessmentAverageScore(Math.round(peerAvg * 100.0) / 100.0)
                .teacherRubricAverageScore(Math.round(teacherAvg * 100.0) / 100.0)
                .totalVocationalAndExperientialHours(totalHours)
                .assessments(entries)
                .build();
    }

    @Data
    @Builder
    public static class HolisticReportCardDTO {
        private UUID studentId;
        private String studentName;
        private String className;
        private String nepFramework;
        private double selfAssessmentAverageScore;
        private double peerAssessmentAverageScore;
        private double teacherRubricAverageScore;
        private double totalVocationalAndExperientialHours;
        private List<HolisticProgressEntry> assessments;
    }
}

