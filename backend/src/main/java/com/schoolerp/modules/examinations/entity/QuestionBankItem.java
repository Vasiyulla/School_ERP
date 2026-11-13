package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "question_banks", schema = "examinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String subjectName; // e.g. Mathematics, Science, English

    @Column(nullable = false)
    private String gradeLevel; // e.g. Class 10, Class 11

    @Column(nullable = false)
    private String chapterName; // e.g. Chapter 4: Quadratic Equations

    private String topicName;

    @Column(nullable = false)
    private String questionType; // MCQ, VERY_SHORT, SHORT_ANSWER, LONG_ANSWER, CASE_STUDY

    @Column(nullable = false)
    @Builder.Default
    private String bloomsTaxonomy = "UNDERSTAND"; // REMEMBER, UNDERSTAND, APPLY, ANALYZE, EVALUATE, CREATE

    @Column(nullable = false)
    @Builder.Default
    private String difficultyLevel = "MEDIUM"; // EASY, MEDIUM, HARD

    @Column(nullable = false)
    private int marks; // 1, 2, 3, 4, 5

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(columnDefinition = "TEXT")
    private String optionsJson; // for MCQs e.g. ["A) Option 1", "B) Option 2", ...]

    @Column(columnDefinition = "TEXT", nullable = false)
    private String markingSchemeAnswerKey; // step-by-step answer key & marking rubric

    private String authorTeacherName;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

