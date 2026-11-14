package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualClassroomSessionDTO {
    private UUID id;
    private String className;
    private String sectionName;
    private String subjectName;
    private String teacherName;
    private LocalDate sessionDate;
    private int periodNumber;
    private String startTime;
    private String endTime;
    private String meetingProvider;
    private String joinMeetingUrl;
    private String meetingRoomCode;
    private String sessionStatus;
    private int totalStudentsJoined;
    private String recordingUrl;
    private LocalDateTime startedAt;
    private LocalDateTime concludedAt;
}

