package com.schoolerp.modules.system.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.system.dto.BatchJobDTO;
import com.schoolerp.modules.system.service.BatchJobService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/system/jobs")
@RequiredArgsConstructor
public class BatchJobController {

    private final BatchJobService batchJobService;

    @GetMapping("/{jobId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<BatchJobDTO>> getJobStatus(@PathVariable String jobId) {
        BatchJobDTO dto = batchJobService.getJobStatus(jobId);
        return ResponseEntity.ok(ApiResponse.ok("Batch job status retrieved", dto));
    }

    @PostMapping("/sample-bulk-run")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<BatchJobDTO>> runSampleBulkJob(
            @RequestBody(required = false) SampleJobRequest request,
            Principal principal) {
        String jobType = request != null && request.getJobType() != null ? request.getJobType() : "FEE_INVOICE_GENERATION";
        int totalItems = request != null && request.getTotalItems() > 0 ? request.getTotalItems() : 500;
        String initiatedBy = principal != null ? principal.getName() : "admin";

        BatchJobDTO dto = batchJobService.runSimulatedBulkJob(jobType, totalItems, initiatedBy);
        return ResponseEntity.ok(ApiResponse.ok("Batch job executed successfully", dto));
    }

    @Data
    public static class SampleJobRequest {
        private String jobType;
        private int totalItems;
    }
}
