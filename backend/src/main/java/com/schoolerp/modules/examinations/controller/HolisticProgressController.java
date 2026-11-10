package com.schoolerp.modules.examinations.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.examinations.entity.HolisticProgressEntry;
import com.schoolerp.modules.examinations.service.HolisticProgressService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/examinations/hpc")
@RequiredArgsConstructor
public class HolisticProgressController {

    private final HolisticProgressService hpcService;

    @PostMapping("/assessments")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','STUDENT')")
    @RequireFeature("examinations.nep_hpc")
    public ResponseEntity<ApiResponse<HolisticProgressEntry>> recordAssessment(@RequestBody RecordHpcRequest request) {
        HolisticProgressEntry entry = hpcService.recordAssessment(
                request.getStudentId(),
                request.getTermName(),
                request.getEvaluatorType(),
                request.getCompetencyDomain(),
                request.getProficiencyLevel(),
                request.getExperientialHoursLogged(),
                request.getEvidenceNotes()
        );
        return ResponseEntity.ok(ApiResponse.success(entry, "360-Degree NEP Holistic Progress competency recorded"));
    }

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','PARENT','STUDENT')")
    @RequireFeature("examinations.nep_hpc")
    public ResponseEntity<ApiResponse<HolisticProgressService.HolisticReportCardDTO>> getStudentHpc(
            @PathVariable UUID studentId) {
        return ResponseEntity.ok(ApiResponse.success(hpcService.compileStudentHpc(studentId), "NEP 2020 Holistic Progress Card compiled"));
    }

    @Data
    public static class RecordHpcRequest {
        private UUID studentId;
        private String termName;
        private String evaluatorType; // SELF, PEER, TEACHER
        private String competencyDomain;
        private String proficiencyLevel; // BEGINNER, PROGRESSING, PROFICIENT, EXEMPLARY
        private double experientialHoursLogged;
        private String evidenceNotes;
    }
}

