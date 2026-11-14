package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExitTicketSessionDTO {
    private UUID id;
    private String teacherName;
    private String className;
    private String sectionName;
    private String subjectName;
    private String topicName;
    private String sessionPin;
    private String status;
    private int totalResponses;
    private double comprehensionRate;
    private LocalDateTime createdAt;
}

