package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecideScholarshipRequest {
    @NotBlank
    private String decision; // APPROVED, REJECTED, UNDER_REVIEW

    private BigDecimal approvedConcessionPercentage;
    private BigDecimal approvedAmount;
    private String committeeReviewerNotes;
    private String reviewedBy;
}

