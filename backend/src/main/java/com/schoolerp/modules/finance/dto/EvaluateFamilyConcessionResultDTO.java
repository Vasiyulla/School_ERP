package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateFamilyConcessionResultDTO {
    private String familyId;
    private int totalChildren;
    private BigDecimal totalOriginalTuitionFee;
    private BigDecimal totalConcessionsGranted;
    private BigDecimal netFamilyTuitionPayable;
    private List<SiblingConcessionLineDTO> siblingConcessions;
}

