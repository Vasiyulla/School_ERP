package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "official_transcripts", schema = "students",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_transcript_number", columnNames = {"tenantId", "transcriptNumber"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficialTranscript {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String transcriptNumber; // e.g. "TRANS-2026-0814"

    @Column(nullable = false)
    private String academicStream; // e.g. "Science (PCMB)", "Commerce with Mathematics"

    @Column(nullable = false)
    private BigDecimal cumulativeGpa; // e.g. 9.40

    @Column(nullable = false)
    private BigDecimal cumulativePercentage; // e.g. 94.20%

    @Builder.Default
    private int totalCredits = 40;

    @Builder.Default
    private String mediumOfInstruction = "English";

    @Column(nullable = false)
    private String verificationHash; // SHA-256 digital verification hash

    @Column(nullable = false)
    private LocalDate issuedDate;

    @Column(nullable = false)
    private String issuedByPrincipal;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

