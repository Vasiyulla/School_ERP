package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cbse_candidate_loc", schema = "examinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CbseCandidateLoc {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String motherName;

    @Column(nullable = false)
    private String fatherName;

    @Column(nullable = false)
    private String admissionNumber;

    @Column(nullable = false)
    private String cbseRollNumber;

    @Column(nullable = false)
    @Builder.Default
    private String classStandard = "Class 10"; // Class 10, Class 12

    @Column(nullable = false)
    @Builder.Default
    private String stream = "GENERAL"; // GENERAL, SCIENCE, COMMERCE, HUMANITIES

    @Column(nullable = false, length = 500)
    private String subjectCodes; // Comma-separated: 301,041,086,087,402

    @Column(nullable = false, length = 500)
    private String subjectNames;

    @Builder.Default
    private boolean isBasicMath = false; // Code 241 vs Code 041

    @Builder.Default
    private boolean cwsnScribeRequired = false;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal feeAmount = new BigDecimal("1500.00");

    @Column(nullable = false)
    @Builder.Default
    private String registrationYear = "2026-2027";

    @Column(nullable = false)
    @Builder.Default
    private String status = "VALIDATED_AND_LOCKED";

    @Builder.Default
    private Instant createdAt = Instant.now();
}

