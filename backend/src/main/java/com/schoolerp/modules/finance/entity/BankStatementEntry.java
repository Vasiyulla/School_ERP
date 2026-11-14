package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bank_statement_entries", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String bankName; // HDFC_BANK, ICICI_BANK, SBI, AXIS_BANK

    @Column(nullable = false)
    private String utrNumber; // Bank unique transaction reference

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 1000)
    private String narration;

    @Column(nullable = false)
    private String status; // RECONCILED, SUSPENSE, MANUALLY_ALLOCATED

    private String matchedStudentAdmissionNumber;
    private String matchedInvoiceNumber;
    private LocalDateTime settledAt;
    private String matchRuleApplied;
}

