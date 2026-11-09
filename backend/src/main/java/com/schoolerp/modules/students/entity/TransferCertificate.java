package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfer_certificates", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String tcNumber; // e.g. TC-2026-0042

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String admissionNumber;

    private String guardianName;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private LocalDate dateOfLeaving;

    @Column(nullable = false)
    private String classAtLeaving;

    @Column(nullable = false)
    private String reasonForLeaving; // PARENT_RELOCATION, HIGHER_STUDIES, WITHDRAWAL

    @Column(nullable = false)
    private String generalConduct; // EXEMPLARY, VERY_GOOD, SATISFACTORY

    @Builder.Default
    private int totalWorkingDays = 220;

    @Builder.Default
    private int daysPresent = 208;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private String issuedBy; // "Principal Office / Dr. Rajesh Sharma"

    @Column(nullable = false, unique = true)
    private String verificationHash; // SHA-256 tamper-proof credential hash for QR validation
}

