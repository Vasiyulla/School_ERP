package com.schoolerp.modules.admissions.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurrenderRefundCalculationDTO {
    private LocalDate surrenderDate;
    private LocalDate academicSessionStartDate;
    private long daysUntilSessionStart;
    private BigDecimal totalFeesPaid;
    private BigDecimal deductionPercentage;
    private BigDecimal deductionAmount;
    private BigDecimal refundableAmount;
    private String refundRuleApplied;
}
