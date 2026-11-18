package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "admission_seat_surrenders", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionSeatSurrender {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String surrenderRefNumber; // e.g. SURR-2026-0031

    @Column(nullable = false)
    private UUID applicationId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String gradeName;

    @Column(nullable = false)
    private String categoryName; // GENERAL, RTE, DEFENCE, SIBLING, ALUMNI

    @Column(nullable = false)
    private String surrenderReason;

    @Column(nullable = false)
    private LocalDate surrenderDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalFeesPaid;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal deductionPercentage;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal deductionAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal refundableAmount;

    @Column(nullable = false)
    @Builder.Default
    private String settlementStatus = "PENDING"; // PENDING, APPROVED, SETTLED, REJECTED

    private UUID waitlistStudentPromotedId;

    private UUID generalLedgerVoucherId;

    @Column(nullable = false)
    private UUID authorizedByUserId;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    @Builder.Default
    private Instant updatedAt = Instant.now();
}
