package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admission_documents_checklist", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionDocumentChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID inquiryId;

    @Column(nullable = false)
    private String documentType; // BIRTH_CERTIFICATE, PREVIOUS_MARKSHEET, TRANSFER_CERTIFICATE, ADDRESS_PROOF, IMMUNIZATION_RECORD, CASTE_OR_INCOME_CERTIFICATE

    @Column(nullable = false)
    private String documentName;

    @Column(columnDefinition = "TEXT")
    private String storageUrl;

    @Builder.Default
    private String status = "PENDING_UPLOAD"; // PENDING_UPLOAD, UNDER_REVIEW, VERIFIED, REJECTED_REUPLOAD_REQUIRED

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    private String verifiedByName;

    private LocalDateTime verifiedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

