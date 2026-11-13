package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.dto.EvaluateFacultyAppraisalRequest;
import com.schoolerp.modules.staff.entity.FacultyAppraisal;
import com.schoolerp.modules.staff.service.FacultyAppraisalService;
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

@RestController
@RequestMapping("/api/v1/staff/appraisals")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Faculty 360° Appraisal & PBAS Evaluation Engine", description = "Performance Based Appraisal System for faculty evaluation, performance banding, and salary increment recommendations")
public class FacultyAppraisalController {

    private final FacultyAppraisalService appraisalService;

    @PostMapping("/evaluate")
    @PreAuthorize("hasAnyRole('PRINCIPAL', 'SUPER_ADMIN')")
    @Operation(summary = "Perform 360-degree annual appraisal evaluation and increment recommendation for a faculty member")
    public ResponseEntity<ApiResponse<FacultyAppraisal>> evaluateFaculty(
            @Valid @RequestBody EvaluateFacultyAppraisalRequest request,
            Authentication auth) {
        String principalName = (auth != null) ? auth.getName() : "Head of School";
        FacultyAppraisal appraisal = appraisalService.evaluateTeacher(request, principalName);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Faculty appraisal evaluated and recorded", appraisal));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PRINCIPAL', 'SUPER_ADMIN')")
    @Operation(summary = "View faculty performance appraisal leaderboard and increment roster")
    public ResponseEntity<ApiResponse<List<FacultyAppraisal>>> getAppraisals(
            @RequestParam(required = false, defaultValue = "ALL") String academicYear) {
        List<FacultyAppraisal> list = appraisalService.getAppraisals(academicYear);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}

