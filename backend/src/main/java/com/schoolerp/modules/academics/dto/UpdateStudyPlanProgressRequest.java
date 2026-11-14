package com.schoolerp.modules.academics.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudyPlanProgressRequest {
    private double completionPercentage;
    private String status; // ASSIGNED, IN_PROGRESS, COMPLETED
    private String studentNotes;
}

