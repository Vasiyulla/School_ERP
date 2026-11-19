package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.dto.CurriculumPlanningDTOs;
import com.schoolerp.modules.academics.entity.CurriculumUnit;
import com.schoolerp.modules.academics.entity.WeeklyLessonPlan;
import com.schoolerp.modules.academics.service.CurriculumPlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academics/curriculum")
@RequiredArgsConstructor
public class CurriculumPlanningController {

    private final CurriculumPlanningService curriculumPlanningService;

    @PostMapping("/units")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<CurriculumUnit>> createUnit(
            @RequestBody CurriculumPlanningDTOs.CreateUnitRequest request,
            Principal principal) {
        String teacher = principal != null ? principal.getName() : "Teacher";
        CurriculumUnit unit = curriculumPlanningService.createUnit(request, teacher);
        return ResponseEntity.ok(ApiResponse.ok("Curriculum unit plan created successfully", unit));
    }

    @GetMapping("/units/grade/{grade}/subject/{subject}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<CurriculumUnit>>> getUnits(
            @PathVariable String grade,
            @PathVariable String subject) {
        List<CurriculumUnit> list = curriculumPlanningService.getUnits(grade, subject);
        return ResponseEntity.ok(ApiResponse.ok("Curriculum units retrieved", list));
    }

    @PutMapping("/units/{id}/approve")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<CurriculumUnit>> approveUnit(
            @PathVariable UUID id,
            @RequestBody(required = false) CurriculumPlanningDTOs.ApproveUnitRequest request,
            Principal principal) {
        String reviewer = principal != null ? principal.getName() : "Principal";
        CurriculumUnit unit = curriculumPlanningService.approveUnit(id, request, reviewer);
        return ResponseEntity.ok(ApiResponse.ok("Curriculum unit review processed", unit));
    }

    @GetMapping("/pacing-radar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CurriculumPlanningDTOs.PacingRadarDTO>> getPacingRadar(
            @RequestParam(defaultValue = "10") String grade,
            @RequestParam(defaultValue = "Mathematics") String subject) {
        CurriculumPlanningDTOs.PacingRadarDTO radar = curriculumPlanningService.getPacingRadar(grade, subject);
        return ResponseEntity.ok(ApiResponse.ok("Curriculum pacing radar calculated", radar));
    }

    @PostMapping("/lesson-plans")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<WeeklyLessonPlan>> submitLessonPlan(
            @RequestBody CurriculumPlanningDTOs.SubmitLessonPlanRequest request,
            Principal principal) {
        String teacher = principal != null ? principal.getName() : "Teacher";
        WeeklyLessonPlan plan = curriculumPlanningService.submitLessonPlan(request, teacher);
        return ResponseEntity.ok(ApiResponse.ok("Weekly lesson plan submitted successfully", plan));
    }
}
