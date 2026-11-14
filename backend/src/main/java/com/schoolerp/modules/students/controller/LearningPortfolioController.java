package com.schoolerp.modules.students.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.dto.*;
import com.schoolerp.modules.students.service.LearningPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students/portfolio")
@RequiredArgsConstructor
public class LearningPortfolioController {

    private final LearningPortfolioService portfolioService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'STUDENT', 'TEACHER', 'PARENT')")
    public ResponseEntity<ApiResponse<PortfolioItemDTO>> uploadArtifact(
            @RequestBody SubmitPortfolioArtifactRequest request) {
        PortfolioItemDTO item = portfolioService.uploadArtifact(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Learning portfolio artifact uploaded successfully", item));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT')")
    public ResponseEntity<ApiResponse<List<PortfolioItemDTO>>> getStudentPortfolio(
            @PathVariable UUID studentId) {
        List<PortfolioItemDTO> portfolio = portfolioService.getStudentPortfolio(studentId);
        return ResponseEntity.ok(ApiResponse.ok("Student learning portfolio retrieved successfully", portfolio));
    }

    @GetMapping("/showcase")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT')")
    public ResponseEntity<ApiResponse<List<PortfolioItemDTO>>> getFeaturedShowcase() {
        List<PortfolioItemDTO> showcase = portfolioService.getFeaturedShowcase();
        return ResponseEntity.ok(ApiResponse.ok("Featured school learning showcase retrieved successfully", showcase));
    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<PortfolioItemDTO>> reviewArtifact(
            @PathVariable UUID id,
            @RequestBody ReviewPortfolioArtifactRequest request,
            Authentication auth) {
        String teacherName = (auth != null) ? auth.getName() : "Faculty Reviewer";
        PortfolioItemDTO reviewed = portfolioService.reviewArtifact(id, request, teacherName);
        return ResponseEntity.ok(ApiResponse.ok("Portfolio artifact reviewed and graded successfully", reviewed));
    }

    @PostMapping("/{id}/parent-clap")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PARENT', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<Integer>> parentClap(@PathVariable UUID id) {
        int totalClaps = portfolioService.recordParentClap(id);
        return ResponseEntity.ok(ApiResponse.ok("Parent applause recorded successfully", totalClaps));
    }
}

