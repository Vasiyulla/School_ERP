package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admission_scholarship_applications", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionScholarshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID inquiryId;

    @Column(nullable = false)
    private String scholarshipType; // MERIT_ACADEMIC, SPORTS_NATIONAL, DEFENSE_PERSONNEL, NEED_BASED_EWS, SIBLING_SPECIAL

    @Column(columnDefinition = "TEXT")
    private String claimedAchievementOrIncome;

    @Builder.Default
    private BigDecimal requestedConcessionPercentage = BigDecimal.ZERO;

    @Builder.Default
    private String committeeStatus = "SUBMITTED"; // SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED

    @Builder.Default
    private BigDecimal approvedConcessionPercentage = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal approvedAmount = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String committeeReviewerNotes;

    private String reviewedBy;

    private LocalDateTime decidedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

