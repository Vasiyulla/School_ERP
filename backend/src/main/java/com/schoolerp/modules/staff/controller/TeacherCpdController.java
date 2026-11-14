package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.dto.CpdSchoolAuditReportDTO;
import com.schoolerp.modules.staff.dto.LogCpdTrainingRequest;
import com.schoolerp.modules.staff.entity.TeacherCpdRecord;
import com.schoolerp.modules.staff.service.TeacherCpdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff/cpd")
@RequiredArgsConstructor
public class TeacherCpdController {

    private final TeacherCpdService cpdService;

    @PostMapping("/log-training")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'HR_MANAGER')")
    public ResponseEntity<ApiResponse<TeacherCpdRecord>> logTraining(
            @RequestBody LogCpdTrainingRequest request,
            Authentication auth) {
        String actor = (auth != null) ? auth.getName() : "HR Department";
        TeacherCpdRecord record = cpdService.logTraining(request, actor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Teacher 50-hour mandatory CPD training logged successfully", record));
    }

    @GetMapping("/compliance-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'HR_MANAGER')")
    public ResponseEntity<ApiResponse<CpdSchoolAuditReportDTO>> getComplianceSummary(
            @RequestParam(required = false) String trainingYear) {
        CpdSchoolAuditReportDTO report = cpdService.getSchoolCpdComplianceSummary(trainingYear);
        return ResponseEntity.ok(ApiResponse.ok("CBSE 50-Hour CPD compliance audit summary retrieved successfully", report));
    }
}
