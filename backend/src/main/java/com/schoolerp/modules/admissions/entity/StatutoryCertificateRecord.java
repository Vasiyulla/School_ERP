package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "statutory_certificate_records", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatutoryCertificateRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID inquiryId;

    @Column(nullable = false)
    private String certificateType; // CASTE_SC, CASTE_ST, CASTE_OBC_NCL, EWS_INCOME, CWSN_DISABILITY, SINGLE_GIRL_AFFIDAVIT, RESIDENCE_PROOF

    @Column(nullable = false)
    private String certificateNumber;

    @Column(nullable = false)
    private String issuingAuthority; // e.g. "Tehsildar, Delhi", "SDM, Mumbai", "District Medical Board"

    private LocalDate issueDate;

    private String validFinancialYear; // e.g. "2026-2027"

    @Builder.Default
    private String verifiedStatus = "PENDING"; // PENDING, VERIFIED, REJECTED, EXPIRED

    @Column(columnDefinition = "TEXT")
    private String verificationRemarks;

    private String verifiedBy;

    private LocalDateTime verifiedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

