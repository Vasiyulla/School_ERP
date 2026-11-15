package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyScholarshipRequest {
    @NotNull
    private UUID inquiryId;

    @NotBlank
    private String scholarshipType; // MERIT_ACADEMIC, SPORTS_NATIONAL, DEFENSE_PERSONNEL, NEED_BASED_EWS, SIBLING_SPECIAL

    private String claimedAchievementOrIncome;
    private BigDecimal requestedConcessionPercentage; // e.g. 50.0%
}

