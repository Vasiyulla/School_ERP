package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "driver_pre_trip_checks", schema = "operations", indexes = {
        @Index(name = "idx_driver_check_date", columnList = "tenant_id, check_date, vehicle_registration")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverPreTripCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "driver_employee_id", nullable = false)
    private String driverEmployeeId;

    @Column(name = "driver_name", nullable = false)
    private String driverName;

    @Column(name = "vehicle_registration", nullable = false)
    private String vehicleRegistration;

    @Column(name = "check_date", nullable = false)
    private LocalDate checkDate;

    @Column(name = "breathalyzer_reading", precision = 5, scale = 2, nullable = false)
    private BigDecimal breathalyzerReading; // Must be 0.00 BAC

    @Column(name = "is_license_valid", nullable = false)
    private boolean licenseValid;

    @Column(name = "is_tire_pressure_ok", nullable = false)
    private boolean tirePressureOk;

    @Column(name = "is_first_aid_kit_present", nullable = false)
    private boolean firstAidKitPresent;

    @Column(name = "is_fire_extinguisher_present", nullable = false)
    private boolean fireExtinguisherPresent;

    @Column(name = "is_emergency_door_operational", nullable = false)
    private boolean emergencyDoorOperational;

    @Column(name = "trip_cleared", nullable = false)
    private boolean tripCleared;

    @Column(name = "supervisor_signature")
    private String supervisorSignature;

    @CreationTimestamp
    @Column(name = "checked_at", nullable = false, updatable = false)
    private Instant checkedAt;
}
