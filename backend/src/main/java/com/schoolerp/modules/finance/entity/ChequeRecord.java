package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cheque_records", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChequeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String chequeNumber; // e.g. CHQ-448201

    @Column(nullable = false)
    private String bankName; // e.g. State Bank of India / HDFC Bank

    private String branchName; // e.g. Andheri West Branch

    @Column(nullable = false)
    private LocalDate chequeDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String payerName; // e.g. Rajesh Sharma

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String invoiceNumber; // e.g. INV-2026-0842

    @Builder.Default
    private String status = "RECEIVED"; // RECEIVED, DEPOSITED, CLEARED, BOUNCED

    private LocalDate depositedDate;

    private LocalDate clearedDate;

    @Builder.Default
    private BigDecimal bouncePenalty = BigDecimal.ZERO; // e.g. 500.00 if bounced

    private String remarks;

    @Builder.Default
    private Instant receivedAt = Instant.now();
}
