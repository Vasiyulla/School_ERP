package com.schoolerp.modules.staff.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class ApplySalaryLoanRequest {

    @NotNull(message = "Employee ID is mandatory")
    private UUID employeeId;

    @NotBlank(message = "Loan type is mandatory (FESTIVAL_ADVANCE, MEDICAL_EMERGENCY, HOUSING_DEPOSIT, EDUCATION_ASSISTANCE)")
    @Builder.Default
    private String loanType = "FESTIVAL_ADVANCE";

    @NotNull(message = "Principal amount is mandatory")
    @DecimalMin(value = "1000.00", message = "Minimum loan amount is INR 1,000.00")
    @Builder.Default
    private BigDecimal principalAmount = new BigDecimal("30000.00");

    @Min(value = 1, message = "Tenure must be at least 1 month")
    @Builder.Default
    private int tenureMonths = 6;

    private String remarks;
}

