package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExitTicketHeatmapDTO {
    private UUID sessionId;
    private String sessionPin;
    private String subjectName;
    private String topicName;
    private String questionText;
    private String status;
    private int totalResponses;
    private int understoodCount;
    private int unsureCount;
    private int strugglingCount;
    private double comprehensionRatePercentage;
    private String comprehensionLevel; // EXCELLENT (>=80%), MODERATE (50-79%), NEEDS_RETEACHING (<50%)
    private List<String> strugglingStudentNames;
}

