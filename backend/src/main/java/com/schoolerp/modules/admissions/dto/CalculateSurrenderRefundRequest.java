package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateSurrenderRefundRequest {

    @NotNull(message = "Surrender notice date is required")
    private LocalDate surrenderDate;

    @NotNull(message = "Session academic commencement date is required")
    private LocalDate academicSessionStartDate;

    @NotNull(message = "Total fees paid is required")
    @DecimalMin(value = "0.00", message = "Total fees paid cannot be negative")
    private BigDecimal totalFeesPaid;
}
