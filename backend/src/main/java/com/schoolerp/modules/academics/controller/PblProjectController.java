package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.dto.*;
import com.schoolerp.modules.academics.service.PblProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academics/pbl")
@RequiredArgsConstructor
public class PblProjectController {

    private final PblProjectService pblProjectService;

    @PostMapping("/projects")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<PblProjectDTO>> createProject(
            @RequestBody CreatePblProjectRequest request,
            Authentication auth) {
        String teacherName = (auth != null) ? auth.getName() : "Faculty Coordinator";
        PblProjectDTO project = pblProjectService.createProject(request, teacherName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Project-Based Learning (PBL) project created successfully", project));
    }

    @GetMapping("/projects")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT')")
    public ResponseEntity<ApiResponse<List<PblProjectDTO>>> listProjects(
            @RequestParam(required = false) String gradeLevel) {
        List<PblProjectDTO> projects = pblProjectService.listProjects(gradeLevel);
        return ResponseEntity.ok(ApiResponse.ok("PBL projects retrieved successfully", projects));
    }

    @PostMapping("/submissions")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<PblMilestoneSubmissionDTO>> submitMilestone(
            @RequestBody SubmitMilestoneRequest request) {
        PblMilestoneSubmissionDTO sub = pblProjectService.submitMilestone(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("PBL group milestone artifact submitted successfully", sub));
    }

    @PutMapping("/submissions/{id}/grade")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<PblMilestoneSubmissionDTO>> gradeMilestone(
            @PathVariable UUID id,
            @RequestBody GradeMilestoneRequest request,
            Authentication auth) {
        String teacherName = (auth != null) ? auth.getName() : "Faculty Evaluator";
        PblMilestoneSubmissionDTO graded = pblProjectService.gradeMilestone(id, request, teacherName);
        return ResponseEntity.ok(ApiResponse.ok("PBL milestone graded and viva scores recorded", graded));
    }

    @GetMapping("/projects/{id}/submissions")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT')")
    public ResponseEntity<ApiResponse<List<PblMilestoneSubmissionDTO>>> getSubmissions(
            @PathVariable UUID id,
            @RequestParam(required = false) String groupName) {
        List<PblMilestoneSubmissionDTO> subs = pblProjectService.getProjectSubmissions(id, groupName);
        return ResponseEntity.ok(ApiResponse.ok("PBL project milestone submissions retrieved", subs));
    }
}

