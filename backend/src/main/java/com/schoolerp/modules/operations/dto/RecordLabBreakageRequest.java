package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordLabBreakageRequest {

    @NotNull(message = "Lab item ID is mandatory")
    private UUID labItemId;

    @NotNull(message = "Student ID is mandatory")
    private UUID studentId;

    @NotNull(message = "Incident date is mandatory")
    private LocalDate incidentDate;

    @Builder.Default
    private String faultType = "ACCIDENTAL"; // ACCIDENTAL, NEGLIGENCE

    @NotNull(message = "Assessed damage cost is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Damage cost must be greater than zero")
    private BigDecimal assessedDamageCost;

    private String remarks;
}

