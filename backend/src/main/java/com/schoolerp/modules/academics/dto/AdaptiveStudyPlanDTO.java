package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdaptiveStudyPlanDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String className;
    private String subjectName;
    private String topicOrChapter;
    private String planType;
    private double assessmentScoreTrigger;
    private String status;
    private String recommendedMaterialsSummary;
    private String prescribedActionsSummary;
    private double completionPercentage;
    private LocalDate assignedDate;
    private LocalDate targetCompletionDate;
    private String studentNotes;
}

