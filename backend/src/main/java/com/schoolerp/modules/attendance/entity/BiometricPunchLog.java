package com.schoolerp.modules.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "biometric_punch_logs", schema = "attendance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiometricPunchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String deviceSerialNumber; // e.g. eSSL-IN-GATE-01

    private UUID staffId;

    @Column(nullable = false)
    private String staffCode; // e.g. TCH-2024-042

    @Column(nullable = false)
    private String staffName;

    @Column(nullable = false)
    private LocalDateTime punchTimestamp;

    @Column(nullable = false)
    private String punchType; // IN, OUT

    @Column(nullable = false)
    private String attendanceStatus; // PRESENT, LATE_ARRIVAL, EARLY_DEPARTURE, HALF_DAY

    private String ipAddress;

    @Builder.Default
    private boolean processed = true;
}

