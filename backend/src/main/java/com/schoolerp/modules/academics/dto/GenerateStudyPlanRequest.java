package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateStudyPlanRequest {
    private UUID studentId;
    private String studentName;
    private String className;
    private String subjectName;
    private String topicOrChapter;
    private double assessmentScoreTrigger; // e.g., 42.5 for Remedial, 92.0 for Enrichment
}

