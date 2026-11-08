package com.schoolerp.modules.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "attendance_sessions", schema = "attendance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String sectionName;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Builder.Default
    private String status = "SUBMITTED"; // OPEN, SUBMITTED, LOCKED

    private String submittedBy;

    @Builder.Default
    private Instant submittedAt = Instant.now();
}

