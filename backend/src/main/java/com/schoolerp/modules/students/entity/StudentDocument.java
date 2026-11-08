package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "student_documents", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String documentType; // TC_ORIGINAL, BIRTH_CERTIFICATE, AADHAAR_CARD, MARKSHEET, MEDICAL_CERTIFICATE

    @Column(nullable = false)
    @Builder.Default
    private String custodyStatus = "ORIGINAL_IN_CUSTODY"; // ORIGINAL_IN_CUSTODY, VERIFIED_COPY, PENDING, RETURNED

    private String lockerIndex; // e.g. Locker B-12 / File #2026-0842

    private String verifierName;

    @Builder.Default
    private Instant verifiedAt = Instant.now();
}

