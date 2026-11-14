package com.schoolerp.modules.attendance.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAttendanceTelemetryDTO {
    private UUID id;
    private UUID sessionId;
    private UUID studentId;
    private String studentName;
    private String className;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
    private int activeDurationMinutes;
    private String attendanceStatus;
    private int connectionHealthScore;
    private LocalDateTime loggedAt;
}

