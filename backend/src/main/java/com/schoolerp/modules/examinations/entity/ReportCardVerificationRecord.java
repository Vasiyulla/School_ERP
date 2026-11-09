package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "report_card_verifications", schema = "examinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportCardVerificationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true, length = 64)
    private String verificationHash;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String admissionNumber;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String academicYear;

    @Column(nullable = false)
    private String termName;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal cgpa;

    @Column(nullable = false)
    private String overallGrade;

    @Column(nullable = false)
    private String resultStatus;

    @Builder.Default
    private String verificationStatus = "AUTHENTIC_VALID"; // AUTHENTIC_VALID, REVOKED

    @Builder.Default
    private Instant issuedAt = Instant.now();
}
