package com.schoolerp.modules.students.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioItemDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String className;
    private String title;
    private String description;
    private String artifactType;
    private String artifactUrl;
    private String thumbnailUrl;
    private String competencyCategory;
    private String teacherRemarks;
    private String rubricGradeBand;
    private String reviewedByTeacherName;
    private int parentClapsCount;
    private boolean isFeaturedShowcase;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
}

