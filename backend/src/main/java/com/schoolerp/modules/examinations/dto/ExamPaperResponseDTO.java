package com.schoolerp.modules.examinations.dto;

import com.schoolerp.modules.examinations.entity.QuestionBankItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamPaperResponseDTO {

    private String examPaperCode; // e.g. "QP-MAT-10-8942"
    private String examTitle;
    private String subjectName;
    private String gradeLevel;
    private int durationMinutes;
    private int totalMarks;
    private int generatedQuestionsCount;
    private List<QuestionSectionDTO> sections;
    private String markingSchemeSummary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionSectionDTO {
        private String sectionName; // "Section A: Multiple Choice Questions (1 Mark Each)"
        private List<QuestionBankItem> questions;
    }
}

