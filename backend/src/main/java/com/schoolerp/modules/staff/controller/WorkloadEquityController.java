package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.service.WorkloadEquityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff/workload-equity")
@RequiredArgsConstructor
public class WorkloadEquityController {

    private final WorkloadEquityService workloadEquityService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @RequireFeature("staff.workload_equity")
    public ResponseEntity<ApiResponse<List<WorkloadEquityService.FacultyWorkloadDTO>>> getWorkloadEquity() {
        return ResponseEntity.ok(ApiResponse.success(
                workloadEquityService.analyzeFacultyWorkloadEquity(),
                "Faculty burnout prevention & workload equity index computed"
        ));
    }
}

