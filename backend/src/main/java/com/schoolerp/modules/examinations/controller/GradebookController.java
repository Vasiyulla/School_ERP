package com.schoolerp.modules.examinations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.examinations.dto.ReportCardDTO;
import com.schoolerp.modules.examinations.entity.ReportCardVerificationRecord;
import com.schoolerp.modules.examinations.entity.StudentMark;
import com.schoolerp.modules.examinations.service.GradebookService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/examinations/gradebook")
@RequiredArgsConstructor
public class GradebookController {

    private final GradebookService gradebookService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentMark>>> getMarks(
            @RequestParam(defaultValue = "Term 1 Mid-Term") String termName,
            @RequestParam(defaultValue = "Mathematics") String subject) {
        List<StudentMark> marks = gradebookService.getMarks(termName, subject);
        return ResponseEntity.ok(ApiResponse.ok(marks));
    }

    @PostMapping("/entry")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<StudentMark>> saveMark(@RequestBody MarkEntryRequest req) {
        StudentMark mark = gradebookService.saveMark(
                req.getTermName(),
                req.getStudentId(),
                req.getStudentName(),
                req.getSubject(),
                req.getTheoryScore(),
                req.getPracticalScore(),
                req.getInternalScore(),
                req.getRemarks()
        );
        return ResponseEntity.ok(ApiResponse.ok("Mark entry saved", mark));
    }

    @PostMapping("/lock")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<List<StudentMark>>> lockGradebook(
            @RequestParam String termName,
            @RequestParam String subject) {
        List<StudentMark> locked = gradebookService.lockGradebook(termName, subject);
        return ResponseEntity.ok(ApiResponse.ok("Gradebook officially locked", locked));
    }

    @GetMapping("/report-card")
    public ResponseEntity<ApiResponse<ReportCardDTO>> getReportCard(
            @RequestParam UUID studentId,
            @RequestParam(defaultValue = "Term 1 Mid-Term") String termName) {
        ReportCardDTO reportCard = gradebookService.compileReportCard(studentId, termName);
        return ResponseEntity.ok(ApiResponse.ok(reportCard));
    }

    @GetMapping("/report-card/verify")
    public ResponseEntity<ApiResponse<ReportCardVerificationRecord>> verifyReportCard(
            @RequestParam String hash) {
        ReportCardVerificationRecord record = gradebookService.verifyReportCard(hash);
        return ResponseEntity.ok(ApiResponse.ok("Report Card Cryptographically Verified as Authentic", record));
    }

    @Data
    public static class MarkEntryRequest {
        private String termName;
        private UUID studentId;
        private String studentName;
        private String subject;
        private int theoryScore;
        private int practicalScore;
        private int internalScore;
        private String remarks;
    }
}

