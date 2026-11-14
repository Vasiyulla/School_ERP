package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.dto.*;
import com.schoolerp.modules.academics.service.LessonMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academics/materials")
@RequiredArgsConstructor
public class LessonMaterialController {

    private final LessonMaterialService materialService;

    @PostMapping("/publish")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<LessonMaterialDTO>> publishMaterial(
            @RequestBody PublishLessonMaterialRequest request,
            Authentication auth) {
        String teacherName = (auth != null) ? auth.getName() : "Faculty Member";
        LessonMaterialDTO published = materialService.publishMaterial(request, teacherName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Digital classroom lesson material published successfully", published));
    }

    @GetMapping("/class-feed")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT', 'STAFF')")
    public ResponseEntity<ApiResponse<List<LessonMaterialDTO>>> getClassFeed(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String sectionName,
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String chapterName) {
        List<LessonMaterialDTO> feed = materialService.getClassFeed(className, sectionName, subjectName, chapterName);
        return ResponseEntity.ok(ApiResponse.ok("Digital study locker class feed retrieved successfully", feed));
    }

    @PostMapping("/log-access")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STUDENT', 'PARENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<String>> logAccess(
            @RequestBody RecordMaterialAccessRequest request) {
        materialService.recordStudentAccess(request);
        return ResponseEntity.ok(ApiResponse.ok("Material study access session recorded successfully", "ACCESS_LOGGED"));
    }

    @GetMapping("/{id}/engagement")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<MaterialEngagementReportDTO>> getEngagement(
            @PathVariable UUID id) {
        MaterialEngagementReportDTO report = materialService.getMaterialEngagement(id);
        return ResponseEntity.ok(ApiResponse.ok("Lesson material student engagement analytics retrieved successfully", report));
    }
}

