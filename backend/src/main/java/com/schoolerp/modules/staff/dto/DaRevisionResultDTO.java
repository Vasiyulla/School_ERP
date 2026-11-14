package com.schoolerp.modules.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaRevisionResultDTO {

    private String tenantId;
    private double previousDaPercentage;
    private double updatedDaPercentage;
    private String effectiveFromMonth;
    private int affectedFacultyCount;
    private BigDecimal schoolMonthlyPayrollIncreaseInr;
    private BigDecimal newTotalMonthlyFacultyBudgetInr;
    private Instant executedAt;
    private String complianceNotes;
}

