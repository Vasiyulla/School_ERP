package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "admission_document_undertakings", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionDocumentUndertaking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String undertakingNumber; // e.g. UNDERTAKING-2026-0042

    @Column(nullable = false)
    private UUID applicationId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String parentName;

    @Column(nullable = false)
    private String missingDocumentName; // e.g. "Previous School TC with DEO Counter-Signature"

    @Column(nullable = false)
    private String reasonCode; // PENDING_PREVIOUS_SCHOOL_CLEARANCE, INTERSTATE_TRANSFER_PROCESSING, OTHER

    @Column(nullable = false)
    private LocalDate deadlineDate;

    @Column(nullable = false)
    @Builder.Default
    private String status = "PENDING"; // PENDING, FULFILLED, OVERDUE, FORFEITED

    @Column(nullable = false)
    private UUID verifiedByOfficerId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean parentSignatureAck = true;

    private LocalDate fulfillmentDate;

    @Column(columnDefinition = "TEXT")
    private String fulfillmentRemarks;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    @Builder.Default
    private Instant updatedAt = Instant.now();
}
