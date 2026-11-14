package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePblProjectRequest {
    private String title;
    private String subjectName;
    private String gradeLevel;
    private String description;
    private int totalMilestones;
    private String scoringRubric;
    private LocalDate dueDate;
}

