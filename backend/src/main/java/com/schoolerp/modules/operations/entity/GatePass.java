package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "gate_passes", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatePass {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String passNumber; // e.g., GP-202611-001

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String guardianName;

    @Column(nullable = false)
    private String guardianPhone;

    @Column(nullable = false)
    private String guardianRelationship; // FATHER, MOTHER, GUARDIAN, AUTHORIZED_DRIVER

    @Column(nullable = false)
    private String reason; // MEDICAL_EMERGENCY, FAMILY_EVENT, ILLNESS, APPROVED_EXEMPTION

    @Column(nullable = false)
    private String passStatus; // APPROVED, SCANNED_EXIT, EXPIRED, CANCELLED

    private String approvedByStaffName;

    private String securityGuardName;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    private LocalDateTime exitTimestamp;

    @Column(nullable = false, unique = true)
    private String qrToken; // Cryptographic UUID verification token for turnstile scanner
}

