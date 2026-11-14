package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PblProjectDTO {
    private UUID id;
    private String title;
    private String subjectName;
    private String gradeLevel;
    private String description;
    private int totalMilestones;
    private String scoringRubric;
    private String coordinatorTeacherName;
    private LocalDate dueDate;
    private String status;
    private LocalDateTime createdAt;
}

