package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "caution_money_deposits", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CautionMoneyDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String admissionNumber;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal depositAmount; // e.g. INR 15000.00

    @Column(nullable = false, unique = true)
    private String receiptNumber; // e.g. SEC-DEP-2024-0842

    @Column(nullable = false)
    @Builder.Default
    private String status = "HELD_IN_CUSTODY"; // HELD_IN_CUSTODY, PARTIALLY_ADJUSTED, REFUNDED_CLOSED

    private String refundVoucherNumber;

    @Column(precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal feeDuesDeduction = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal labBreakageDeduction = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal netRefundedAmount = BigDecimal.ZERO;

    private Instant refundedAt;

    private String paymentReference;

    @Builder.Default
    private Instant depositedAt = Instant.now();
}

