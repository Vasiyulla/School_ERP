package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.entity.StudentRiskAssessment;
import com.schoolerp.modules.academics.entity.SyllabusTopicProgress;
import com.schoolerp.modules.academics.service.StudentRiskRadarService;
import com.schoolerp.modules.academics.service.SyllabusEquityService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academics")
@RequiredArgsConstructor
public class AcademicIntelligenceController {

    private final StudentRiskRadarService riskRadarService;
    private final SyllabusEquityService syllabusEquityService;

    @PostMapping("/risk-radar/evaluate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @RequireFeature("academics.risk_radar")
    public ResponseEntity<ApiResponse<List<StudentRiskAssessment>>> evaluateRiskRadar() {
        return ResponseEntity.ok(ApiResponse.success(
                riskRadarService.runSchoolWideEvaluation(),
                "Multi-signal Student Early-Warning Risk Radar executed"
        ));
    }

    @GetMapping("/risk-radar/high-risk")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @RequireFeature("academics.risk_radar")
    public ResponseEntity<ApiResponse<List<StudentRiskAssessment>>> getHighRiskStudents() {
        return ResponseEntity.ok(ApiResponse.success(
                riskRadarService.getHighRiskStudents(),
                "High-risk and watchlist students retrieved"
        ));
    }

    @PostMapping("/syllabus/progress")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @RequireFeature("academics.syllabus_handoff")
    public ResponseEntity<ApiResponse<SyllabusTopicProgress>> logSyllabusProgress(@RequestBody SyllabusProgressRequest request) {
        SyllabusTopicProgress saved = syllabusEquityService.updateSyllabusProgress(
                request.getClassName(),
                request.getSubject(),
                request.getTeacherName(),
                request.getCurrentChapter(),
                request.getLastCompletedTopic(),
                request.getNextTopicObjective(),
                request.getResourceAttachmentUrl(),
                request.getCompletionPercentage()
        );
        return ResponseEntity.ok(ApiResponse.success(saved, "Curriculum pacing progress logged cleanly"));
    }

    @GetMapping("/substitutions/syllabus-handoff")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @RequireFeature("academics.syllabus_handoff")
    public ResponseEntity<ApiResponse<SyllabusEquityService.SubstituteHandoffBriefDTO>> getSubstituteHandoff(
            @RequestParam(required = false, defaultValue = "Class 10 - Section A") String className,
            @RequestParam(required = false, defaultValue = "Mathematics") String subject) {
        return ResponseEntity.ok(ApiResponse.success(
                syllabusEquityService.getSubstituteHandoffBrief(className, subject),
                "Syllabus-aware substitute instructional handoff generated"
        ));
    }

    @Data
    public static class SyllabusProgressRequest {
        private String className;
        private String subject;
        private String teacherName;
        private String currentChapter;
        private String lastCompletedTopic;
        private String nextTopicObjective;
        private String resourceAttachmentUrl;
        private double completionPercentage;
    }
}

