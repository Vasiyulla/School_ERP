package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "concession_approval_requests", schema = "finance", indexes = {
        @Index(name = "idx_concession_status", columnList = "tenant_id, status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcessionApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "docket_number", nullable = false, unique = true)
    private String docketNumber;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Column(name = "reason_category", nullable = false)
    private String reasonCategory; // FINANCIAL_HARDSHIP, MERIT_SCHOLARSHIP, STAFF_CHILD, SPORTS_EXCELLENCE

    @Column(name = "requested_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal requestedAmount;

    @Column(name = "approved_amount", precision = 12, scale = 2)
    private BigDecimal approvedAmount;

    @Column(name = "status", nullable = false)
    private String status; // PENDING_CHECKER_REVIEW, APPROVED, REJECTED

    @Column(name = "maker_user_id", nullable = false)
    private String makerUserId;

    @Column(name = "checker_user_id")
    private String checkerUserId;

    @Column(name = "required_checker_role", nullable = false)
    private String requiredCheckerRole; // PRINCIPAL, BURSAR, SUPER_ADMIN

    @Column(name = "justification", length = 2000, nullable = false)
    private String justification;

    @Column(name = "general_ledger_voucher_id")
    private UUID generalLedgerVoucherId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "approved_at")
    private Instant approvedAt;
}
