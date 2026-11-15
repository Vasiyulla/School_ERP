package com.schoolerp.modules.operations.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleFuelLogDTO {
    private UUID id;
    private UUID vehicleId;
    private String vehicleRegistration;
    private LocalDate fillDate;
    private BigDecimal fuelLitres;
    private BigDecimal fuelRatePerLitre;
    private BigDecimal totalCost;
    private int odometerReading;
    private int previousOdometerReading;
    private int kilometersRun;
    private BigDecimal mileageKmPerLitre;
    private String fuelStationName;
    private String driverName;
    private boolean pilferageAlert;
}

