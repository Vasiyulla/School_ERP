package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.entity.AcademicYearRollover;
import com.schoolerp.modules.academics.service.AcademicYearRolloverService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academics/rollovers")
@RequiredArgsConstructor
public class AcademicYearRolloverController {

    private final AcademicYearRolloverService rolloverService;

    @PostMapping("/execute")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<AcademicYearRollover>> executeRollover(@RequestBody RolloverRequest request) {
        AcademicYearRollover rollover = rolloverService.executeBatchRollover(
                request.getFromAcademicYear(),
                request.getToAcademicYear(),
                request.getExecutedBy()
        );
        return ResponseEntity.ok(ApiResponse.ok("Academic year rollover and student batch promotion executed successfully", rollover));
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<AcademicYearRollover>>> getHistory() {
        return ResponseEntity.ok(ApiResponse.ok("Retrieved institutional rollover history", rolloverService.getRolloverHistory()));
    }

    @Data
    public static class RolloverRequest {
        private String fromAcademicYear;
        private String toAcademicYear;
        private String executedBy;
    }
}

