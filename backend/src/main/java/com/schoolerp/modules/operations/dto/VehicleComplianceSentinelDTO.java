package com.schoolerp.modules.operations.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleComplianceSentinelDTO {
    private UUID id;
    private UUID vehicleId;
    private String vehicleRegistration;
    private LocalDate rtoFitnessExpiry;
    private long daysUntilFitnessExpiry;
    private LocalDate insurancePolicyExpiry;
    private long daysUntilInsuranceExpiry;
    private LocalDate pucCertificateExpiry;
    private long daysUntilPucExpiry;
    private LocalDate roadTaxExpiry;
    private String complianceStatus;
}

