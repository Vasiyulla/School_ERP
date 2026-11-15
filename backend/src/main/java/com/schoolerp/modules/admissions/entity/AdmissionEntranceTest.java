package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admission_entrance_tests", schema = "admissions",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_test_code", columnNames = {"tenantId", "testCode"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionEntranceTest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID inquiryId;

    @Column(nullable = false)
    private String testCode; // e.g. "TEST-2026-G09-042"

    @Column(nullable = false)
    private LocalDateTime testDate;

    @Column(nullable = false)
    private String examHall; // e.g. "Exam Hall B / Room 204"

    @Column(nullable = false)
    private String reportingTime; // e.g. "08:30 AM"

    @Builder.Default
    private BigDecimal mathScore = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal englishScore = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal scienceScore = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal aptitudeScore = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalScore = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal maximumScore = BigDecimal.valueOf(100.0);

    @Builder.Default
    private BigDecimal percentage = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal qualifyingCutoffPercentage = BigDecimal.valueOf(60.0); // e.g. 60% for General, 50% for SC/ST/OBC/CWSN

    @Builder.Default
    private String resultStatus = "PENDING"; // PENDING, QUALIFIED, NOT_QUALIFIED, ABSENT

    @Column(columnDefinition = "TEXT")
    private String evaluatorRemarks;

    @Column(nullable = false)
    private String admitCardToken; // e.g. "ADMIT-2026-XXXX"

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

