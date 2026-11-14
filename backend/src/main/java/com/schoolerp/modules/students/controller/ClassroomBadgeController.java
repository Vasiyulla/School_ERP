package com.schoolerp.modules.students.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.dto.*;
import com.schoolerp.modules.students.service.ClassroomBadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students/badges")
@RequiredArgsConstructor
public class ClassroomBadgeController {

    private final ClassroomBadgeService badgeService;

    @PostMapping("/award")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<ClassroomBadgeDTO>> awardBadge(
            @RequestBody AwardBadgeRequest request,
            Authentication auth) {
        String teacherName = (auth != null) ? auth.getName() : "Faculty Member";
        ClassroomBadgeDTO badge = badgeService.awardBadge(request, teacherName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Classroom micro-badge awarded successfully", badge));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT')")
    public ResponseEntity<ApiResponse<StudentBadgeSummaryDTO>> getStudentBadges(
            @PathVariable UUID studentId) {
        StudentBadgeSummaryDTO summary = badgeService.getStudentBadgeSummary(studentId);
        return ResponseEntity.ok(ApiResponse.ok("Student classroom badges and character summary retrieved", summary));
    }

    @GetMapping("/class-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<ClassroomBadgeDTO>>> getClassBadges(
            @RequestParam String className,
            @RequestParam(defaultValue = "Section A") String sectionName) {
        List<ClassroomBadgeDTO> badges = badgeService.getClassBadges(className, sectionName);
        return ResponseEntity.ok(ApiResponse.ok("Classroom badges feed retrieved successfully", badges));
    }
}

