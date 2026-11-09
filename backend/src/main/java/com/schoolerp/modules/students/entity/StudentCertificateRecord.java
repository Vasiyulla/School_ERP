package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "student_certificates", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCertificateRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String certificateType; // BONAFIDE, CHARACTER, FEE_ESTIMATION_CEA, STUDY_CONDUCT

    @Column(nullable = false, unique = true)
    private String certificateNumber;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private String issuedTo;

    @Column(nullable = false)
    private String qrVerificationToken;

    @Builder.Default
    private Instant issuedAt = Instant.now();

    @Column(nullable = false)
    private String issuedBy;
}

