package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "learning_portfolio_items", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningPortfolioItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String artifactType; // PROJECT_PHOTO, VIDEO_DEMO, AUDIO_PODCAST, ESSAY_DOCUMENT, CODE_REPO, CERTIFICATE

    @Column(nullable = false)
    private String artifactUrl;

    private String thumbnailUrl;

    @Column(nullable = false)
    private String competencyCategory; // CRITICAL_THINKING, CREATIVITY_INNOVATION, COLLABORATION, SCIENTIFIC_INQUIRY, GLOBAL_CITIZENSHIP

    @Column(columnDefinition = "TEXT")
    private String teacherRemarks;

    private String rubricGradeBand; // EXEMPLARY, PROFICIENT, DEVELOPING

    private String reviewedByTeacherName;

    @Builder.Default
    private int parentClapsCount = 0;

    @Builder.Default
    private boolean isFeaturedShowcase = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime reviewedAt;
}

