package com.schoolerp.modules.staff.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyDaRevisionRequest {

    @DecimalMin(value = "0.0", message = "DA percentage must be >= 0")
    @DecimalMax(value = "100.0", message = "DA percentage cannot exceed 100")
    @Builder.Default
    private double newDaPercentage = 50.0; // Central / State Govt 50% DA

    @NotBlank(message = "Effective month is mandatory (e.g. 2026-07)")
    @Builder.Default
    private String effectiveFromMonth = "2026-07";

    private String govtOrderNotificationRef;

    private String remarks;
}

