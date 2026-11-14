package com.schoolerp.modules.attendance.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualSessionAttendanceRosterDTO {
    private UUID sessionId;
    private int totalAttendees;
    private int presentVirtualCount;
    private int partialAttendanceCount;
    private int absentCount;
    private double classAttendancePercentage;
    private List<VirtualAttendanceTelemetryDTO> studentLogs;
}

