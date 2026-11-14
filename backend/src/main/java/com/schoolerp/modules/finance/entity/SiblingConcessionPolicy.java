package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "sibling_concession_policies", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiblingConcessionPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String policyCode; // SIBLING_TIER_2, SIBLING_TIER_3, STAFF_WARD

    @Column(nullable = false)
    private Integer siblingRank; // 2 = 2nd child, 3 = 3rd child, etc.

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal concessionPercentage; // 15.00, 25.00, 50.00

    @Column(nullable = false)
    private String applicableFeeHead; // TUITION_FEE

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;
}

