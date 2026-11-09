package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cashier_day_scrolls", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashierDayScroll {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String cashierName;

    @Column(nullable = false)
    private LocalDate counterDate;

    @Builder.Default
    private int count500Notes = 0;

    @Builder.Default
    private int count200Notes = 0;

    @Builder.Default
    private int count100Notes = 0;

    @Builder.Default
    private int count50Notes = 0;

    @Builder.Default
    private int count20Notes = 0;

    @Builder.Default
    private int count10Notes = 0;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCashCalculated;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalSystemReceipts;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal discrepancyAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private String status = "BALANCED"; // BALANCED, OVERAGE, SHORTAGE

    private String notes;

    @Builder.Default
    private Instant closedAt = Instant.now();
}

