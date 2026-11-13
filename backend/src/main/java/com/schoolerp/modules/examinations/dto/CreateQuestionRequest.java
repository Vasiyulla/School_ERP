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
public class CreateQuestionRequest {

    @NotBlank(message = "Subject name is mandatory")
    private String subjectName;

    @NotBlank(message = "Grade level is mandatory")
    private String gradeLevel;

    @NotBlank(message = "Chapter name is mandatory")
    private String chapterName;

    private String topicName;

    @NotBlank(message = "Question type is mandatory (MCQ, VERY_SHORT, SHORT_ANSWER, LONG_ANSWER, CASE_STUDY)")
    private String questionType;

    @Builder.Default
    private String bloomsTaxonomy = "UNDERSTAND";

    @Builder.Default
    private String difficultyLevel = "MEDIUM";

    @Min(value = 1, message = "Marks must be at least 1")
    private int marks;

    @NotBlank(message = "Question text is mandatory")
    private String questionText;

    private String optionsJson;

    @NotBlank(message = "Marking scheme / Answer key is mandatory")
    private String markingSchemeAnswerKey;
}

