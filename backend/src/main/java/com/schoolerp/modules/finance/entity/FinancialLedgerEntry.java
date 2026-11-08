package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "financial_ledger_entries", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialLedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String entryType; // FEE_PAYMENT, CONCESSION_WAIVER, REFUND_SETTLEMENT

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal debit = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal credit = BigDecimal.ZERO;

    @Column(nullable = false)
    private String accountCode; // e.g. 1010-CASH, 4010-TUITION-FEE

    @Column(nullable = false)
    private String description;

    private String referenceId; // invoice # or receipt #

    @Builder.Default
    private Instant postedAt = Instant.now();
}

