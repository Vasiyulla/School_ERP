package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.dto.*;
import com.schoolerp.modules.academics.entity.PblMilestoneSubmission;
import com.schoolerp.modules.academics.entity.PblProject;
import com.schoolerp.modules.academics.repository.PblMilestoneSubmissionRepository;
import com.schoolerp.modules.academics.repository.PblProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PblProjectService {

    private final PblProjectRepository projectRepository;
    private final PblMilestoneSubmissionRepository submissionRepository;

    @Transactional
    public PblProjectDTO createProject(CreatePblProjectRequest request, String teacherName) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");

        PblProject project = PblProject.builder()
                .tenantId(tenantId)
                .title(request.getTitle())
                .subjectName(request.getSubjectName())
                .gradeLevel(request.getGradeLevel())
                .description(request.getDescription())
                .totalMilestones(request.getTotalMilestones() > 0 ? request.getTotalMilestones() : 4)
                .scoringRubric(request.getScoringRubric() != null ? request.getScoringRubric() : "Default 4-Tier Rubric: Inquiry (25), Prototype (25), Report (25), Oral Viva (25)")
                .coordinatorTeacherName(teacherName)
                .dueDate(request.getDueDate())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        PblProject saved = projectRepository.save(project);
        log.info("Created PBL project: '{}' for grade {} by {}", saved.getTitle(), saved.getGradeLevel(), teacherName);

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PblProjectDTO> listProjects(String gradeLevel) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        List<PblProject> list = (gradeLevel != null && !gradeLevel.isBlank())
                ? projectRepository.findByTenantIdAndGradeLevelOrderByCreatedAtDesc(tenantId, gradeLevel)
                : projectRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);

        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public PblMilestoneSubmissionDTO submitMilestone(SubmitMilestoneRequest request) {
        PblMilestoneSubmission sub = PblMilestoneSubmission.builder()
                .projectId(request.getProjectId())
                .groupName(request.getGroupName())
                .groupLeaderName(request.getGroupLeaderName())
                .memberNames(request.getMemberNames())
                .milestoneNumber(request.getMilestoneNumber())
                .milestoneTitle(request.getMilestoneTitle())
                .submissionUrl(request.getSubmissionUrl())
                .status("SUBMITTED")
                .submittedAt(LocalDateTime.now())
                .build();

        PblMilestoneSubmission saved = submissionRepository.save(sub);
        log.info("Group '{}' submitted Milestone #{} for PBL project {}", request.getGroupName(), request.getMilestoneNumber(), request.getProjectId());

        return toDto(saved);
    }

    @Transactional
    public PblMilestoneSubmissionDTO gradeMilestone(UUID submissionId, GradeMilestoneRequest request, String teacherName) {
        PblMilestoneSubmission sub = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Milestone submission not found with ID: " + submissionId));

        sub.setTeacherScore(request.getTeacherScore());
        sub.setMaxScore(request.getMaxScore() > 0 ? request.getMaxScore() : 25.0);
        sub.setPeerContributionRating(request.getPeerContributionRating() > 0 ? request.getPeerContributionRating() : 100.0);
        sub.setStatus(request.getStatus() != null ? request.getStatus() : "APPROVED");
        sub.setTeacherFeedback(request.getTeacherFeedback());
        sub.setGradedByTeacherName(teacherName);
        sub.setGradedAt(LocalDateTime.now());

        PblMilestoneSubmission saved = submissionRepository.save(sub);
        log.info("Teacher {} graded Milestone #{} for group '{}': score = {}/{}", teacherName, sub.getMilestoneNumber(), sub.getGroupName(), sub.getTeacherScore(), sub.getMaxScore());

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PblMilestoneSubmissionDTO> getProjectSubmissions(UUID projectId, String groupName) {
        List<PblMilestoneSubmission> subs = (groupName != null && !groupName.isBlank())
                ? submissionRepository.findByProjectIdAndGroupNameOrderByMilestoneNumberAsc(projectId, groupName)
                : submissionRepository.findByProjectIdOrderByMilestoneNumberAsc(projectId);

        return subs.stream().map(this::toDto).collect(Collectors.toList());
    }

    private PblProjectDTO toDto(PblProject p) {
        return PblProjectDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .subjectName(p.getSubjectName())
                .gradeLevel(p.getGradeLevel())
                .description(p.getDescription())
                .totalMilestones(p.getTotalMilestones())
                .scoringRubric(p.getScoringRubric())
                .coordinatorTeacherName(p.getCoordinatorTeacherName())
                .dueDate(p.getDueDate())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .build();
    }

    private PblMilestoneSubmissionDTO toDto(PblMilestoneSubmission s) {
        return PblMilestoneSubmissionDTO.builder()
                .id(s.getId())
                .projectId(s.getProjectId())
                .groupName(s.getGroupName())
                .groupLeaderName(s.getGroupLeaderName())
                .memberNames(s.getMemberNames())
                .milestoneNumber(s.getMilestoneNumber())
                .milestoneTitle(s.getMilestoneTitle())
                .submissionUrl(s.getSubmissionUrl())
                .teacherScore(s.getTeacherScore())
                .maxScore(s.getMaxScore())
                .peerContributionRating(s.getPeerContributionRating())
                .status(s.getStatus())
                .teacherFeedback(s.getTeacherFeedback())
                .gradedByTeacherName(s.getGradedByTeacherName())
                .submittedAt(s.getSubmittedAt())
                .gradedAt(s.getGradedAt())
                .build();
    }
}

