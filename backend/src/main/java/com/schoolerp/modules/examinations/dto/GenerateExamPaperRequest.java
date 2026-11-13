package com.schoolerp.modules.examinations.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateExamPaperRequest {

    @NotBlank(message = "Exam title is mandatory")
    private String examTitle; // e.g. "Periodic Test 2 - Mathematics 2026"

    @NotBlank(message = "Subject name is mandatory")
    private String subjectName; // Mathematics

    @NotBlank(message = "Grade level is mandatory")
    private String gradeLevel; // Class 10

    @Builder.Default
    private int durationMinutes = 180; // 3 hours

    @Min(value = 10, message = "Total marks must be at least 10")
    @Builder.Default
    private int totalMarks = 80;

    @Builder.Default
    private int targetMcqCount = 10;

    @Builder.Default
    private int targetShortCount = 6;

    @Builder.Default
    private int targetLongCount = 4;
}

