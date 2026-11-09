package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.entity.HomeworkAssignment;
import com.schoolerp.modules.academics.entity.HomeworkSubmission;
import com.schoolerp.modules.academics.service.HomeworkService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academics/homework")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;

    @Data
    public static class CreateHomeworkRequest {
        private String className;
        private String sectionName;
        private String subject;
        private String title;
        private String instructions;
        private String teacherName;
        private LocalDate dueDate;
        private double maxMarks;
        private String attachmentUrl;
    }

    @Data
    public static class SubmitHomeworkRequest {
        private UUID studentId;
        private String submissionUrl;
    }

    @Data
    public static class GradeHomeworkRequest {
        private double marksAwarded;
        private String feedback;
        private String gradedByTeacherName;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<HomeworkAssignment>> createAssignment(@RequestBody CreateHomeworkRequest req) {
        HomeworkAssignment assignment = homeworkService.createAssignment(
                req.getClassName(),
                req.getSectionName(),
                req.getSubject(),
                req.getTitle(),
                req.getInstructions(),
                req.getTeacherName(),
                req.getDueDate(),
                req.getMaxMarks(),
                req.getAttachmentUrl()
        );
        return ResponseEntity.ok(ApiResponse.ok("Homework assigned successfully", assignment));
    }

    @GetMapping("/class/{className}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<HomeworkAssignment>>> getAssignments(
            @PathVariable String className,
            @RequestParam(required = false) String sectionName) {
        return ResponseEntity.ok(ApiResponse.ok(homeworkService.getAssignmentsForClass(className, sectionName)));
    }

    @PostMapping("/{assignmentId}/submit")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<ApiResponse<HomeworkSubmission>> submitHomework(
            @PathVariable UUID assignmentId,
            @RequestBody SubmitHomeworkRequest req) {
        HomeworkSubmission sub = homeworkService.submitHomework(assignmentId, req.getStudentId(), req.getSubmissionUrl());
        return ResponseEntity.ok(ApiResponse.ok("Homework submitted successfully", sub));
    }

    @PostMapping("/submissions/{submissionId}/grade")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<HomeworkSubmission>> gradeHomework(
            @PathVariable UUID submissionId,
            @RequestBody GradeHomeworkRequest req) {
        HomeworkSubmission graded = homeworkService.gradeSubmission(
                submissionId,
                req.getMarksAwarded(),
                req.getFeedback(),
                req.getGradedByTeacherName()
        );
        return ResponseEntity.ok(ApiResponse.ok("Homework submission graded successfully", graded));
    }

    @GetMapping("/student/{studentId}/submissions")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<HomeworkSubmission>>> getStudentSubmissions(@PathVariable UUID studentId) {
        return ResponseEntity.ok(ApiResponse.ok(homeworkService.getStudentSubmissions(studentId)));
    }
}

