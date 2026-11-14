package com.schoolerp.modules.finance.dto;

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
public class SiblingConcessionLineDTO {
    private UUID studentId;
    private String studentName;
    private int siblingRank; // 1 = elder, 2 = second, etc.
    private BigDecimal standardTuitionFee;
    private BigDecimal concessionPercentage;
    private BigDecimal discountAmount;
    private BigDecimal netPayableTuitionFee;
}

