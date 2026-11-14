package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "adaptive_study_plans", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdaptiveStudyPlan {

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
    private String subjectName;

    @Column(nullable = false)
    private String topicOrChapter;

    @Column(nullable = false)
    private String planType; // REMEDIAL (Score < 50%), ENRICHMENT (Score > 85%), TARGETED_PRACTICE (50-85%)

    @Column(nullable = false)
    private double assessmentScoreTrigger;

    @Column(nullable = false)
    private String status; // ASSIGNED, IN_PROGRESS, COMPLETED

    @Column(columnDefinition = "TEXT")
    private String recommendedMaterialsSummary;

    @Column(columnDefinition = "TEXT")
    private String prescribedActionsSummary;

    @Builder.Default
    private double completionPercentage = 0.0;

    @Builder.Default
    private LocalDate assignedDate = LocalDate.now();

    private LocalDate targetCompletionDate;

    @Column(columnDefinition = "TEXT")
    private String studentNotes;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

