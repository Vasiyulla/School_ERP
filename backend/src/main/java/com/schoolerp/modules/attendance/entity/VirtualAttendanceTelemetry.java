package com.schoolerp.modules.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "virtual_attendance_telemetry", schema = "attendance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAttendanceTelemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID sessionId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime leftAt;

    @Builder.Default
    private int activeDurationMinutes = 0;

    @Column(nullable = false)
    private String attendanceStatus; // PRESENT_VIRTUAL (>=70% duration), PARTIAL_ATTENDANCE_VIRTUAL (30-69%), ABSENT_VIRTUAL (<30%)

    @Builder.Default
    private int connectionHealthScore = 100; // 0-100% network stability

    @Builder.Default
    private LocalDateTime loggedAt = LocalDateTime.now();
}

