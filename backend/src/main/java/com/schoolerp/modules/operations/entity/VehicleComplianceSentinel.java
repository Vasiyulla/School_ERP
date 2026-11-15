package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicle_compliance_sentinels", schema = "operations",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_vehicle_compliance", columnNames = {"tenantId", "vehicleId"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleComplianceSentinel {

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
    private LocalDate rtoFitnessExpiry;

    @Column(nullable = false)
    private LocalDate insurancePolicyExpiry;

    @Column(nullable = false)
    private LocalDate pucCertificateExpiry;

    @Column(nullable = false)
    private LocalDate roadTaxExpiry;

    private LocalDate speedGovernorCertExpiry;

    @Builder.Default
    private String complianceStatus = "COMPLIANT"; // COMPLIANT, WARNING_EXPIRING_SOON, CRITICAL_EXPIRED

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

