package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rte_applications", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RteApplicationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String admissionNumber;

    @Column(nullable = false)
    private String gradeApplied;

    @Column(nullable = false, unique = true)
    private String lotteryApplicationNo;

    @Column(nullable = false)
    private String incomeCertificateNo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal annualHouseholdIncome;

    @Column(nullable = false)
    @Builder.Default
    private String category = "EWS"; // EWS, DISADVANTAGED_GROUP

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal stateGovernmentRate = new BigDecimal("17670.00");

    @Column(nullable = false)
    @Builder.Default
    private String reimbursementStatus = "APPROVED_PENDING_DISBURSAL"; // SUBMITTED, APPROVED_PENDING_DISBURSAL, DISBURSED

    @Column(nullable = false)
    @Builder.Default
    private String academicYear = "2026-2027";

    @Builder.Default
    private Instant appliedAt = Instant.now();
}

