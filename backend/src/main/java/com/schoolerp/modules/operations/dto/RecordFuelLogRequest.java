package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordFuelLogRequest {
    @NotNull
    private UUID vehicleId;

    private String vehicleRegistration;
    private LocalDate fillDate;

    @NotNull
    private BigDecimal fuelLitres;

    @NotNull
    private BigDecimal fuelRatePerLitre;

    @NotNull
    private int odometerReading;

    private String fuelStationName;
    private String driverName;
}

