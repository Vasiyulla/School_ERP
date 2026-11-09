package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "biometric_punches", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiometricPunch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID employeeId;

    @Column(nullable = false)
    private String employeeCode;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private Instant punchTime;

    @Column(nullable = false)
    private String punchType; // IN, OUT

    private String deviceIp; // e.g. 10.0.8.21 (Biometric Turnstile Gate 1)

    @Builder.Default
    private String verificationMode = "FACIAL_RECOGNITION"; // FACIAL_RECOGNITION, BIOMETRIC_FINGERPRINT, RFID_SMARTCARD

    @Builder.Default
    private String punchStatus = "ON_TIME"; // ON_TIME, LATE_MARK, EARLY_DEPARTURE, OVERTIME
}

