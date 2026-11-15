package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicle_fuel_logs", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleFuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID vehicleId;

    @Column(nullable = false)
    private String vehicleRegistration; // e.g. "MH-12-AB-4042"

    @Column(nullable = false)
    private LocalDate fillDate;

    @Column(nullable = false)
    private BigDecimal fuelLitres; // e.g. 65.50

    @Column(nullable = false)
    private BigDecimal fuelRatePerLitre; // e.g. 92.40

    @Column(nullable = false)
    private BigDecimal totalCost; // e.g. 6052.20

    @Column(nullable = false)
    private int odometerReading; // current km e.g. 48520

    @Column(nullable = false)
    private int previousOdometerReading; // previous km e.g. 48240

    @Column(nullable = false)
    private int kilometersRun; // e.g. 280

    @Column(nullable = false)
    private BigDecimal mileageKmPerLitre; // e.g. 4.27 km/L

    @Column(nullable = false)
    private String fuelStationName; // e.g. "Indian Oil - Viman Nagar"

    @Column(nullable = false)
    private String driverName; // e.g. "Ramesh Pawar"

    @Builder.Default
    private boolean pilferageAlert = false; // Flagged if mileage < 3.2 km/L for school bus

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

