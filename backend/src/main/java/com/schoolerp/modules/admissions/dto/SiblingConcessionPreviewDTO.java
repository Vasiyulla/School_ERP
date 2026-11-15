package com.schoolerp.modules.admissions.dto;

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
public class SiblingConcessionPreviewDTO {
    private UUID inquiryId;
    private String applicantStudentName;
    private UUID siblingStudentId;
    private String siblingStudentName;
    private String siblingGrade;
    private String siblingAdmissionNumber;
    private BigDecimal standardAnnualTuitionFee;
    private BigDecimal concessionPercentage; // e.g. 15.00%
    private BigDecimal estimatedConcessionSavings; // e.g. ₹18,000
    private BigDecimal netEstimatedTuitionFee;
    private int updatedLeadScore; // boosted with sibling priority points
}

