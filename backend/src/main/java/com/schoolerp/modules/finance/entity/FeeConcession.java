package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fee_concessions", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeConcession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String concessionType; // SIBLING_DISCOUNT, STAFF_WARD, MERIT_SCHOLARSHIP, RTE_GOVERNMENT

    @Column(nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal percentageDiscount = BigDecimal.ZERO; // e.g. 15.00 for 15%

    @Column(precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal flatDiscountAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private String applicableFeeHead; // TUITION, TOTAL_FEE

    @Column(nullable = false)
    private String reason; // e.g. "2nd child sibling concession (Elder sibling: Aarav Sharma ADM-2024-0012)"

    @Column(nullable = false)
    private String approvedBy;

    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, EXPIRED, REVOKED

    @Builder.Default
    private Instant createdAt = Instant.now();
}

