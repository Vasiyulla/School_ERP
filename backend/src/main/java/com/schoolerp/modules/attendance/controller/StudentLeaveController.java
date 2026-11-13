package com.schoolerp.modules.attendance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.attendance.dto.ApplyStudentLeaveRequest;
import com.schoolerp.modules.attendance.dto.ReviewStudentLeaveRequest;
import com.schoolerp.modules.attendance.entity.StudentLeaveApplication;
import com.schoolerp.modules.attendance.service.StudentLeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance/leaves")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Leave & Attendance Normalization Engine", description = "Parent student leave applications and automated attendance normalization to preserve CBSE 75% compliance")
public class StudentLeaveController {

    private final StudentLeaveService studentLeaveService;

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('PARENT', 'STUDENT', 'ADMIN', 'SUPER_ADMIN', 'TEACHER')")
    @Operation(summary = "Submit a student leave application with medical/family reasons")
    public ResponseEntity<ApiResponse<StudentLeaveApplication>> applyLeave(
            @Valid @RequestBody ApplyStudentLeaveRequest request,
            Authentication auth) {
        String parentName = (auth != null) ? auth.getName() : "Authorized Guardian";
        StudentLeaveApplication app = studentLeaveService.applyLeave(request, null, parentName, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Leave application submitted successfully", app));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'PARENT', 'STUDENT')")
    @Operation(summary = "List student leave applications with filtering")
    public ResponseEntity<ApiResponse<List<StudentLeaveApplication>>> getLeaves(
            @RequestParam(required = false) UUID studentId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String sectionName) {
        List<StudentLeaveApplication> leaves = studentLeaveService.getLeaves(studentId, status, className, sectionName);
        return ResponseEntity.ok(ApiResponse.ok(leaves));
    }

    @PutMapping("/{applicationId}/review")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Review and approve/reject leave application with automatic attendance normalization")
    public ResponseEntity<ApiResponse<StudentLeaveApplication>> reviewLeave(
            @PathVariable UUID applicationId,
            @Valid @RequestBody ReviewStudentLeaveRequest request,
            Authentication auth) {
        String reviewerName = (auth != null) ? auth.getName() : "Faculty In-Charge";
        StudentLeaveApplication reviewed = studentLeaveService.reviewLeave(applicationId, request, reviewerName);
        return ResponseEntity.ok(ApiResponse.ok("Leave application reviewed successfully", reviewed));
    }
}

