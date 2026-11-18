package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "admission_welcome_dossiers", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionWelcomeDossier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false, unique = true)
    private String admissionNumber; // e.g. ADM-2026-0812

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String gradeName;

    @Column(nullable = false)
    private String sectionName;

    @Column(nullable = false)
    private String rollNumber;

    @Column(nullable = false)
    private String houseName; // e.g. "Tigers", "Falcons", "Blue House"

    private String smartCardRfidUid; // NFC/RFID chip UID: e.g. "04:5A:82:1F:B9:6C:80"

    @Column(nullable = false)
    @Builder.Default
    private String smartCardStatus = "ACTIVE"; // ACTIVE, ISSUED, INACTIVE

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal canteenInitialBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean welcomePackGenerated = true;

    @Column(nullable = false)
    private UUID mintedByUserId;

    @Column(nullable = false)
    @Builder.Default
    private Instant mintedAt = Instant.now();

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    @Builder.Default
    private Instant updatedAt = Instant.now();
}
