package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipApplicationDTO {
    private UUID id;
    private UUID inquiryId;
    private String studentName;
    private String scholarshipType;
    private String claimedAchievementOrIncome;
    private BigDecimal requestedConcessionPercentage;
    private String committeeStatus;
    private BigDecimal approvedConcessionPercentage;
    private BigDecimal approvedAmount;
    private String committeeReviewerNotes;
    private String reviewedBy;
    private LocalDateTime decidedAt;
    private LocalDateTime createdAt;
}

