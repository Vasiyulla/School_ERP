package com.schoolerp.modules.students.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.dto.*;
import com.schoolerp.modules.students.entity.LearningPortfolioItem;
import com.schoolerp.modules.students.repository.LearningPortfolioItemRepository;
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
public class LearningPortfolioService {

    private final LearningPortfolioItemRepository repository;

    @Transactional
    public PortfolioItemDTO uploadArtifact(SubmitPortfolioArtifactRequest request) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");

        LearningPortfolioItem item = LearningPortfolioItem.builder()
                .tenantId(tenantId)
                .studentId(request.getStudentId())
                .studentName(request.getStudentName())
                .className(request.getClassName())
                .title(request.getTitle())
                .description(request.getDescription())
                .artifactType(request.getArtifactType())
                .artifactUrl(request.getArtifactUrl())
                .thumbnailUrl(request.getThumbnailUrl())
                .competencyCategory(request.getCompetencyCategory())
                .createdAt(LocalDateTime.now())
                .build();

        LearningPortfolioItem saved = repository.save(item);
        log.info("Student {} uploaded portfolio artifact: {} ({})", request.getStudentName(), request.getTitle(), request.getArtifactType());

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PortfolioItemDTO> getStudentPortfolio(UUID studentId) {
        return repository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PortfolioItemDTO> getFeaturedShowcase() {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        return repository.findByTenantIdAndIsFeaturedShowcaseTrueOrderByCreatedAtDesc(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PortfolioItemDTO reviewArtifact(UUID itemId, ReviewPortfolioArtifactRequest request, String teacherName) {
        LearningPortfolioItem item = repository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio artifact not found: " + itemId));

        item.setTeacherRemarks(request.getTeacherRemarks());
        item.setRubricGradeBand(request.getRubricGradeBand());
        item.setReviewedByTeacherName(teacherName);
        item.setReviewedAt(LocalDateTime.now());
        if (request.getIsFeaturedShowcase() != null) {
            item.setFeaturedShowcase(request.getIsFeaturedShowcase());
        }

        LearningPortfolioItem saved = repository.save(item);
        log.info("Teacher {} reviewed portfolio artifact: {} with band {}", teacherName, item.getTitle(), item.getRubricGradeBand());

        return toDto(saved);
    }

    @Transactional
    public int recordParentClap(UUID itemId) {
        LearningPortfolioItem item = repository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio artifact not found: " + itemId));

        item.setParentClapsCount(item.getParentClapsCount() + 1);
        repository.save(item);

        return item.getParentClapsCount();
    }

    private PortfolioItemDTO toDto(LearningPortfolioItem i) {
        return PortfolioItemDTO.builder()
                .id(i.getId())
                .studentId(i.getStudentId())
                .studentName(i.getStudentName())
                .className(i.getClassName())
                .title(i.getTitle())
                .description(i.getDescription())
                .artifactType(i.getArtifactType())
                .artifactUrl(i.getArtifactUrl())
                .thumbnailUrl(i.getThumbnailUrl())
                .competencyCategory(i.getCompetencyCategory())
                .teacherRemarks(i.getTeacherRemarks())
                .rubricGradeBand(i.getRubricGradeBand())
                .reviewedByTeacherName(i.getReviewedByTeacherName())
                .parentClapsCount(i.getParentClapsCount())
                .isFeaturedShowcase(i.isFeaturedShowcase())
                .createdAt(i.getCreatedAt())
                .reviewedAt(i.getReviewedAt())
                .build();
    }
}

