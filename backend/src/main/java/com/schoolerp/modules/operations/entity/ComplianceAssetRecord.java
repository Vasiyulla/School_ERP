package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "compliance_assets", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceAssetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String assetName; // e.g. School Bus MH-02-CB-4012 RTO Fitness, State Fire Safety NOC

    @Column(nullable = false)
    private String category; // SCHOOL_STATUTORY, FLEET_RTO, LAB_SAFETY, BUILDING_NOC

    @Column(nullable = false)
    private String certificateNumber;

    @Column(nullable = false)
    private String issuingAuthority; // e.g. CBSE New Delhi, Mumbai Fire Brigade, RTO Andheri

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private String custodianOfficer;

    @Builder.Default
    private Instant updatedAt = Instant.now();
}

