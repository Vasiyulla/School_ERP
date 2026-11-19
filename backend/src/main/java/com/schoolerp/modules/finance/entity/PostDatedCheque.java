package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "post_dated_cheques", schema = "finance", indexes = {
        @Index(name = "idx_pdc_maturity", columnList = "tenant_id, maturity_date, status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDatedCheque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "cheque_number", nullable = false)
    private String chequeNumber;

    @Column(name = "drawer_bank", nullable = false)
    private String drawerBank;

    @Column(name = "drawer_account_name", nullable = false)
    private String drawerAccountName;

    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "vault_locker_location", nullable = false)
    private String vaultLockerLocation; // e.g. LOCKER-A-04

    @Column(name = "status", nullable = false)
    private String status; // IN_VAULT, PRESENTED_TO_BANK, CLEARED, DISHONOURED, RETURNED_TO_PARENT

    @Column(name = "deposit_batch_ref")
    private String depositBatchRef;

    @Column(name = "cleared_at")
    private Instant clearedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
