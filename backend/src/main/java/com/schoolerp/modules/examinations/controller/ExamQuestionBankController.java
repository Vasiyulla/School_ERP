package com.schoolerp.modules.examinations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.examinations.dto.CreateQuestionRequest;
import com.schoolerp.modules.examinations.dto.ExamPaperResponseDTO;
import com.schoolerp.modules.examinations.dto.GenerateExamPaperRequest;
import com.schoolerp.modules.examinations.entity.QuestionBankItem;
import com.schoolerp.modules.examinations.service.ExamQuestionBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/examinations/question-bank")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Exam Question Bank & Automated Paper Generator", description = "Bloom's taxonomy question tagging, randomized balanced paper generation, and printable exam papers")
public class ExamQuestionBankController {

    private final ExamQuestionBankService questionBankService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Add a new question into the Question Bank with Bloom's taxonomy tags")
    public ResponseEntity<ApiResponse<QuestionBankItem>> addQuestion(
            @Valid @RequestBody CreateQuestionRequest request,
            Authentication auth) {
        String teacher = (auth != null) ? auth.getName() : "Faculty Author";
        QuestionBankItem item = questionBankService.addQuestion(request, teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Question added successfully", item));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "List questions filtered by subject, grade level, and question type")
    public ResponseEntity<ApiResponse<List<QuestionBankItem>>> getQuestions(
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String gradeLevel,
            @RequestParam(required = false) String questionType) {
        List<QuestionBankItem> questions = questionBankService.getQuestions(subjectName, gradeLevel, questionType);
        return ResponseEntity.ok(ApiResponse.ok(questions));
    }

    @PostMapping("/generate-paper")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Generate a randomized, balanced question paper from question bank blueprint")
    public ResponseEntity<ApiResponse<ExamPaperResponseDTO>> generatePaper(
            @Valid @RequestBody GenerateExamPaperRequest request) {
        ExamPaperResponseDTO paper = questionBankService.generateExamPaper(request);
        return ResponseEntity.ok(ApiResponse.ok("Exam paper generated successfully", paper));
    }

    @PostMapping(value = "/generate-paper/print", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Generate a print-ready A4 examination question paper HTML")
    public ResponseEntity<String> printPaper(@Valid @RequestBody GenerateExamPaperRequest request) {
        String htmlPaper = questionBankService.generatePrintableExamPaperHtml(request);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlPaper);
    }
}

