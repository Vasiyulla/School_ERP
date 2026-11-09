package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.dto.SubstitutionRecommendationDTO;
import com.schoolerp.modules.academics.entity.TeacherSubstitution;
import com.schoolerp.modules.academics.service.SubstitutionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academics/substitutions")
@RequiredArgsConstructor
public class SubstitutionController {

    private final SubstitutionService substitutionService;

    @GetMapping("/recommend")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<SubstitutionRecommendationDTO>>> getRecommendations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam UUID absentTeacherId) {
        List<SubstitutionRecommendationDTO> recommendations = substitutionService.getRecommendations(date, absentTeacherId);
        return ResponseEntity.ok(ApiResponse.ok("Teacher substitution recommendations generated", recommendations));
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<TeacherSubstitution>> assignSubstitution(@RequestBody AssignSubstitutionRequest req) {
        String actor = SecurityContextHolder.getContext().getAuthentication().getName();
        TeacherSubstitution substitution = substitutionService.assignSubstitution(
                req.getDate(),
                req.getPeriodNumber(),
                req.getTimeSlot(),
                req.getClassName(),
                req.getSectionName(),
                req.getSubject(),
                req.getRoomNumber(),
                req.getAbsentTeacherId(),
                req.getAbsentTeacherName(),
                req.getSubstituteTeacherId(),
                req.getSubstituteTeacherName(),
                actor
        );
        return ResponseEntity.ok(ApiResponse.ok("Period substitution confirmed and notification dispatched", substitution));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherSubstitution>>> getSubstitutions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate queryDate = date != null ? date : LocalDate.now();
        List<TeacherSubstitution> list = substitutionService.getSubstitutionsForDate(queryDate);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @Data
    public static class AssignSubstitutionRequest {
        private LocalDate date;
        private int periodNumber;
        private String timeSlot;
        private String className;
        private String sectionName;
        private String subject;
        private String roomNumber;
        private UUID absentTeacherId;
        private String absentTeacherName;
        private UUID substituteTeacherId;
        private String substituteTeacherName;
    }
}

