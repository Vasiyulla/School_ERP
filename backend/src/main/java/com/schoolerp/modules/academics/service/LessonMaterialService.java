package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.dto.*;
import com.schoolerp.modules.academics.entity.LessonMaterial;
import com.schoolerp.modules.academics.entity.MaterialAccessLog;
import com.schoolerp.modules.academics.repository.LessonMaterialRepository;
import com.schoolerp.modules.academics.repository.MaterialAccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LessonMaterialService {

    private final LessonMaterialRepository materialRepository;
    private final MaterialAccessLogRepository accessLogRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    private String formatFileSize(Long bytes) {
        if (bytes == null || bytes <= 0) return "0 KB";
        if (bytes < 1024 * 1024) {
            return (bytes / 1024) + " KB";
        }
        double mb = (double) bytes / (1024 * 1024);
        return String.format(Locale.US, "%.1f MB", mb);
    }

    private LessonMaterialDTO toDTO(LessonMaterial m) {
        return LessonMaterialDTO.builder()
                .id(m.getId())
                .teacherName(m.getTeacherName())
                .subjectName(m.getSubjectName())
                .className(m.getClassName())
                .sectionName(m.getSectionName())
                .chapterName(m.getChapterName())
                .topicName(m.getTopicName())
                .title(m.getTitle())
                .description(m.getDescription())
                .materialType(m.getMaterialType())
                .fileUrl(m.getFileUrl())
                .thumbnailUrl(m.getThumbnailUrl())
                .fileSizeBytes(m.getFileSizeBytes())
                .formattedFileSize(formatFileSize(m.getFileSizeBytes()))
                .fileExtension(m.getFileExtension())
                .lessonDate(m.getLessonDate())
                .isDownloadable(m.isDownloadable())
                .viewCount(m.getViewCount())
                .status(m.getStatus())
                .createdAt(m.getCreatedAt())
                .build();
    }

    @Transactional
    public LessonMaterialDTO publishMaterial(PublishLessonMaterialRequest request, String teacherName) {
        String tenantId = resolveTenant();

        String ext = request.getFileExtension();
        if (ext == null && request.getFileUrl() != null && request.getFileUrl().contains(".")) {
            ext = request.getFileUrl().substring(request.getFileUrl().lastIndexOf(".") + 1);
        }

        LessonMaterial material = LessonMaterial.builder()
                .tenantId(tenantId)
                .teacherStaffId(UUID.randomUUID())
                .teacherName((teacherName != null && !teacherName.isBlank()) ? teacherName : "Faculty Member")
                .subjectName(request.getSubjectName() != null ? request.getSubjectName() : "General Studies")
                .className(request.getClassName() != null ? request.getClassName() : "Grade 10")
                .sectionName(request.getSectionName() != null ? request.getSectionName() : "ALL")
                .chapterName(request.getChapterName() != null ? request.getChapterName() : "Chapter 1")
                .topicName(request.getTopicName() != null ? request.getTopicName() : "Core Principles")
                .title(request.getTitle() != null ? request.getTitle() : "Lesson Notes & Lecture Slides")
                .description(request.getDescription() != null ? request.getDescription() : "Class presentation & reference material")
                .materialType(request.getMaterialType() != null ? request.getMaterialType().toUpperCase() : "PRESENTATION_SLIDES")
                .fileUrl(request.getFileUrl() != null ? request.getFileUrl() : "https://storage.schoolerp.com/materials/sample.pdf")
                .thumbnailUrl(request.getThumbnailUrl())
                .fileSizeBytes(request.getFileSizeBytes() != null ? request.getFileSizeBytes() : 12500000L) // Default 12.5 MB
                .fileExtension(ext != null ? ext.toLowerCase() : "pdf")
                .lessonDate(request.getLessonDate() != null ? request.getLessonDate() : LocalDate.now())
                .isDownloadable(request.getIsDownloadable() != null ? request.getIsDownloadable() : true)
                .viewCount(0)
                .status("PUBLISHED")
                .build();

        LessonMaterial saved = materialRepository.save(material);
        log.info("Published lesson material [{}] '{}' for {} ({}) by {}",
                saved.getMaterialType(), saved.getTitle(), saved.getClassName(), saved.getSubjectName(), saved.getTeacherName());
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<LessonMaterialDTO> getClassFeed(String className, String sectionName, String subjectName, String chapterName) {
        String tenantId = resolveTenant();
        String cName = (className != null) ? className : "Grade 10";

        List<LessonMaterial> list = (subjectName != null && !subjectName.isBlank())
                ? materialRepository.findAllByTenantIdAndClassNameAndSubjectName(tenantId, cName, subjectName)
                : materialRepository.findAllByTenantIdAndClassName(tenantId, cName);

        return list.stream()
                .filter(m -> "PUBLISHED".equalsIgnoreCase(m.getStatus()))
                .filter(m -> sectionName == null || "ALL".equalsIgnoreCase(m.getSectionName()) || m.getSectionName().equalsIgnoreCase(sectionName))
                .filter(m -> chapterName == null || m.getChapterName().equalsIgnoreCase(chapterName))
                .sorted(Comparator.comparing(LessonMaterial::getLessonDate).reversed())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void recordStudentAccess(RecordMaterialAccessRequest request) {
        String tenantId = resolveTenant();
        if (request.getMaterialId() == null) return;

        materialRepository.findByIdAndTenantId(request.getMaterialId(), tenantId).ifPresent(m -> {
            m.setViewCount(m.getViewCount() + 1);
            materialRepository.save(m);

            MaterialAccessLog accessLog = MaterialAccessLog.builder()
                    .tenantId(tenantId)
                    .materialId(m.getId())
                    .studentId(request.getStudentId() != null ? request.getStudentId() : UUID.randomUUID())
                    .studentName(request.getStudentName() != null ? request.getStudentName() : "Student")
                    .durationSeconds(request.getDurationSeconds() != null ? request.getDurationSeconds() : 180)
                    .isCompleted(request.getIsCompleted() != null ? request.getIsCompleted() : false)
                    .build();

            accessLogRepository.save(accessLog);
        });
    }

    @Transactional(readOnly = true)
    public MaterialEngagementReportDTO getMaterialEngagement(UUID materialId) {
        String tenantId = resolveTenant();
        LessonMaterial material = materialRepository.findByIdAndTenantId(materialId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson material not found: " + materialId));

        List<MaterialAccessLog> logs = accessLogRepository.findAllByTenantIdAndMaterialId(tenantId, materialId);

        List<StudentAccessDetailDTO> details = logs.stream()
                .map(l -> StudentAccessDetailDTO.builder()
                        .studentId(l.getStudentId())
                        .studentName(l.getStudentName())
                        .accessedAt(l.getAccessedAt())
                        .durationSeconds(l.getDurationSeconds())
                        .isCompleted(l.isCompleted())
                        .build())
                .collect(Collectors.toList());

        Set<UUID> uniqueStudents = logs.stream().map(MaterialAccessLog::getStudentId).collect(Collectors.toSet());
        double avgDuration = logs.isEmpty() ? 0.0 : logs.stream().mapToInt(MaterialAccessLog::getDurationSeconds).average().orElse(0.0);

        return MaterialEngagementReportDTO.builder()
                .materialId(material.getId())
                .title(material.getTitle())
                .subjectName(material.getSubjectName())
                .className(material.getClassName())
                .totalViews(material.getViewCount())
                .uniqueStudentsViewed(uniqueStudents.size())
                .averageDurationSeconds(Math.round(avgDuration * 10.0) / 10.0)
                .studentAccessList(details)
                .build();
    }
}

