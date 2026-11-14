package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.dto.*;
import com.schoolerp.modules.academics.entity.AdaptiveStudyPlan;
import com.schoolerp.modules.academics.repository.AdaptiveStudyPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdaptiveStudyPlanService {

    private final AdaptiveStudyPlanRepository repository;

    @Transactional
    public AdaptiveStudyPlanDTO generateAdaptivePlan(GenerateStudyPlanRequest request) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");

        String planType;
        String materials;
        String actions;
        LocalDate targetDate;

        if (request.getAssessmentScoreTrigger() < 50.0) {
            planType = "REMEDIAL";
            materials = "Foundational Concept Summary Video (8 mins), NCERT Solved Examples 1-5, Formula Flashcard Deck";
            actions = "1. Watch video on fundamental axioms. 2. Solve 3 guided worksheet problems with step-by-step hints. 3. Attend peer mentor clinic.";
            targetDate = LocalDate.now().plusDays(5);
        } else if (request.getAssessmentScoreTrigger() >= 85.0) {
            planType = "ENRICHMENT";
            materials = "National Science Olympiad (NSO) Previous Year HOTS Problem Set, Advanced Concept Application Brief";
            actions = "1. Attempt 3 multi-concept Olympiad synthesis questions. 2. Formulate real-world application case study. 3. Lead a 5-minute peer study group recap.";
            targetDate = LocalDate.now().plusDays(10);
        } else {
            planType = "TARGETED_PRACTICE";
            materials = "Standard Question Bank Exercise 4.2 & 4.3, Mid-level concept clarification notes";
            actions = "1. Review marked misconceptions in recent assignment. 2. Complete 5 practice questions.";
            targetDate = LocalDate.now().plusDays(7);
        }

        AdaptiveStudyPlan plan = AdaptiveStudyPlan.builder()
                .tenantId(tenantId)
                .studentId(request.getStudentId())
                .studentName(request.getStudentName())
                .className(request.getClassName())
                .subjectName(request.getSubjectName())
                .topicOrChapter(request.getTopicOrChapter())
                .planType(planType)
                .assessmentScoreTrigger(request.getAssessmentScoreTrigger())
                .status("ASSIGNED")
                .recommendedMaterialsSummary(materials)
                .prescribedActionsSummary(actions)
                .completionPercentage(0.0)
                .assignedDate(LocalDate.now())
                .targetCompletionDate(targetDate)
                .createdAt(LocalDateTime.now())
                .build();

        AdaptiveStudyPlan saved = repository.save(plan);
        log.info("Generated {} study plan for student {} (Score: {}%) in {}", planType, request.getStudentName(), request.getAssessmentScoreTrigger(), request.getTopicOrChapter());

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<AdaptiveStudyPlanDTO> getStudentPlans(UUID studentId) {
        return repository.findByStudentIdOrderByAssignedDateDesc(studentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdaptiveStudyPlanDTO updateProgress(UUID planId, UpdateStudyPlanProgressRequest request) {
        AdaptiveStudyPlan plan = repository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Study plan not found with ID: " + planId));

        double progress = Math.max(0.0, Math.min(100.0, request.getCompletionPercentage()));
        plan.setCompletionPercentage(progress);

        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        } else if (progress >= 100.0) {
            plan.setStatus("COMPLETED");
        } else if (progress > 0.0) {
            plan.setStatus("IN_PROGRESS");
        }

        if (request.getStudentNotes() != null) {
            plan.setStudentNotes(request.getStudentNotes());
        }

        AdaptiveStudyPlan saved = repository.save(plan);
        log.info("Updated study plan {}: progress = {}%, status = {}", planId, saved.getCompletionPercentage(), saved.getStatus());

        return toDto(saved);
    }

    private AdaptiveStudyPlanDTO toDto(AdaptiveStudyPlan p) {
        return AdaptiveStudyPlanDTO.builder()
                .id(p.getId())
                .studentId(p.getStudentId())
                .studentName(p.getStudentName())
                .className(p.getClassName())
                .subjectName(p.getSubjectName())
                .topicOrChapter(p.getTopicOrChapter())
                .planType(p.getPlanType())
                .assessmentScoreTrigger(p.getAssessmentScoreTrigger())
                .status(p.getStatus())
                .recommendedMaterialsSummary(p.getRecommendedMaterialsSummary())
                .prescribedActionsSummary(p.getPrescribedActionsSummary())
                .completionPercentage(p.getCompletionPercentage())
                .assignedDate(p.getAssignedDate())
                .targetCompletionDate(p.getTargetCompletionDate())
                .studentNotes(p.getStudentNotes())
                .build();
    }
}

