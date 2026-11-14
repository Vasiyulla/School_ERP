package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cpc_pay_matrices", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpcPayMatrix {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String payLevel; // LEVEL_6_PRT, LEVEL_7_TGT, LEVEL_8_PGT, LEVEL_10_VICE_PRINCIPAL, LEVEL_12_PRINCIPAL

    @Column(nullable = false)
    private String levelName; // e.g. Primary Teacher (PRT) Scale

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal basicPayMin; // e.g. 35400.00

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal basicPayMax; // e.g. 112400.00

    @Column(nullable = false)
    private String standardDesignation; // PRT, TGT, PGT, VP, PRINCIPAL

    @Column(nullable = false)
    @Builder.Default
    private String cityTier = "TIER_X_METRO"; // TIER_X_METRO (27% HRA), TIER_Y (18%), TIER_Z (9%)

    @Column(nullable = false)
    @Builder.Default
    private double hraPercentage = 27.0; // 27% for Mumbai / Delhi

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal transportAllowance = new BigDecimal("3600.00");

    @Builder.Default
    private Instant createdAt = Instant.now();
}

