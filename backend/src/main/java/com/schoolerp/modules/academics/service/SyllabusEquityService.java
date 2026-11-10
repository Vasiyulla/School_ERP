package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.entity.SyllabusTopicProgress;
import com.schoolerp.modules.academics.repository.SyllabusTopicProgressRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SyllabusEquityService {

    private final SyllabusTopicProgressRepository syllabusRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public SyllabusTopicProgress updateSyllabusProgress(
            String className,
            String subject,
            String teacherName,
            String currentChapter,
            String lastCompletedTopic,
            String nextTopicObjective,
            String resourceAttachmentUrl,
            double completionPercentage) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        SyllabusTopicProgress progress = SyllabusTopicProgress.builder()
                .tenantId(tenantId)
                .className(className != null ? className : "Class 10 - Section A")
                .subject(subject != null ? subject : "Mathematics")
                .teacherName(teacherName != null ? teacherName : "Sunita Rao")
                .currentChapter(currentChapter)
                .lastCompletedTopic(lastCompletedTopic)
                .nextTopicObjective(nextTopicObjective)
                .resourceAttachmentUrl(resourceAttachmentUrl != null ? resourceAttachmentUrl : "s3://apex-lms/worksheets/default.pdf")
                .completionPercentage(completionPercentage)
                .updatedAt(Instant.now())
                .build();

        SyllabusTopicProgress saved = syllabusRepository.save(progress);

        auditLogService.recordEvent(
                "UPDATE_SYLLABUS_PROGRESS",
                "academics.syllabus_topic_progress",
                saved.getId().toString(),
                String.format("%s (%s) updated pacing to '%s' (%.1f%% complete)",
                        saved.getClassName(), saved.getSubject(), saved.getCurrentChapter(), saved.getCompletionPercentage())
        );

        return saved;
    }

    public SubstituteHandoffBriefDTO getSubstituteHandoffBrief(String className, String subject) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        String targetClass = className != null ? className : "Class 10 - Section A";
        String targetSubject = subject != null ? subject : "Mathematics";

        SyllabusTopicProgress latest = syllabusRepository
                .findTopByTenantIdAndClassNameAndSubjectOrderByUpdatedAtDesc(tenantId, targetClass, targetSubject)
                .orElseGet(() -> updateSyllabusProgress(
                        targetClass,
                        targetSubject,
                        "Sunita Rao",
                        "Chapter 4: Quadratic Equations",
                        "Discriminant Derivation & Formula Application (Ex 4.2)",
                        "Begin Exercise 4.3: Nature of Roots + Distribute Printed Practice Sheet #4",
                        "s3://apex-lms/class10/math/quadratic-ex4-3-handoff.pdf",
                        62.5
                ));

        return SubstituteHandoffBriefDTO.builder()
                .className(latest.getClassName())
                .subject(latest.getSubject())
                .regularFaculty(latest.getTeacherName())
                .currentChapter(latest.getCurrentChapter())
                .lastCompletedTopic(latest.getLastCompletedTopic())
                .immediateSubstituteInstruction(latest.getNextTopicObjective())
                .attachedWorksheetUrl(latest.getResourceAttachmentUrl())
                .syllabusCompletionPercentage(latest.getCompletionPercentage())
                .build();
    }

    public List<SyllabusTopicProgress> getAllSyllabusProgress() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return syllabusRepository.findByTenantIdOrderByUpdatedAtDesc(tenantId);
    }

    @Data
    @Builder
    public static class SubstituteHandoffBriefDTO {
        private String className;
        private String subject;
        private String regularFaculty;
        private String currentChapter;
        private String lastCompletedTopic;
        private String immediateSubstituteInstruction;
        private String attachedWorksheetUrl;
        private double syllabusCompletionPercentage;
    }
}

