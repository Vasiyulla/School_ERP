package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.dto.*;
import com.schoolerp.modules.academics.service.AdaptiveStudyPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academics/adaptive-plans")
@RequiredArgsConstructor
public class AdaptiveStudyPlanController {

    private final AdaptiveStudyPlanService studyPlanService;

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<AdaptiveStudyPlanDTO>> generatePlan(
            @RequestBody GenerateStudyPlanRequest request) {
        AdaptiveStudyPlanDTO plan = studyPlanService.generateAdaptivePlan(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Personalized adaptive study plan generated successfully", plan));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT')")
    public ResponseEntity<ApiResponse<List<AdaptiveStudyPlanDTO>>> getStudentPlans(
            @PathVariable UUID studentId) {
        List<AdaptiveStudyPlanDTO> plans = studyPlanService.getStudentPlans(studentId);
        return ResponseEntity.ok(ApiResponse.ok("Student personalized study plans retrieved successfully", plans));
    }

    @PutMapping("/{id}/progress")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'STUDENT', 'TEACHER', 'PARENT')")
    public ResponseEntity<ApiResponse<AdaptiveStudyPlanDTO>> updateProgress(
            @PathVariable UUID id,
            @RequestBody UpdateStudyPlanProgressRequest request) {
        AdaptiveStudyPlanDTO updated = studyPlanService.updateProgress(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Study plan progress updated successfully", updated));
    }
}

