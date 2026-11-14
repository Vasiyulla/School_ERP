package com.schoolerp.modules.attendance.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogVirtualPresenceRequest {
    private UUID sessionId;
    private UUID studentId;
    private String studentName;
    private String className;
    private int sessionScheduledDurationMinutes; // e.g. 45
    private int activeDurationMinutes; // e.g. 38
    private int connectionHealthScore; // e.g. 95
}

