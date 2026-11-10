package com.schoolerp.modules.system.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.system.entity.BulkImportJob;
import com.schoolerp.modules.system.entity.OutboxEvent;
import com.schoolerp.modules.system.service.BulkImportService;
import com.schoolerp.modules.system.service.EnterpriseSchedulerService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
public class PlatformAutomationController {

    private final BulkImportService bulkImportService;
    private final EnterpriseSchedulerService schedulerService;

    @PostMapping("/imports/students-csv")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @RequireFeature("system.bulk_onboarding")
    public ResponseEntity<ApiResponse<BulkImportJob>> importStudentsCsv(@RequestBody CsvImportRequest request) {
        BulkImportJob job = bulkImportService.importStudentsFromCsv(
                request.getFileName(),
                request.getCsvContent(),
                request.getInitiatedBy()
        );
        return ResponseEntity.ok(ApiResponse.success(job, "Bulk CSV onboarding job processed"));
    }

    @GetMapping("/imports/{jobId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @RequireFeature("system.bulk_onboarding")
    public ResponseEntity<ApiResponse<BulkImportJob>> getImportJob(@PathVariable UUID jobId) {
        return ResponseEntity.ok(ApiResponse.success(bulkImportService.getJob(jobId), "Import job details retrieved"));
    }

    @GetMapping("/imports")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @RequireFeature("system.bulk_onboarding")
    public ResponseEntity<ApiResponse<List<BulkImportJob>>> getAllImportJobs() {
        return ResponseEntity.ok(ApiResponse.success(bulkImportService.getJobs(), "Bulk import jobs retrieved"));
    }

    @PostMapping("/outbox/enqueue")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<OutboxEvent>> enqueueOutbox(@RequestBody EnqueueOutboxRequest request) {
        OutboxEvent ev = schedulerService.enqueueEvent(
                request.getEventType(),
                request.getAggregateId(),
                request.getPayload(),
                request.isSimulateTransientFailure()
        );
        return ResponseEntity.ok(ApiResponse.success(ev, "Event reliably persisted to transactional outbox"));
    }

    @GetMapping("/outbox/status")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<EnterpriseSchedulerService.OutboxStatusSummaryDTO>> getOutboxStatus() {
        return ResponseEntity.ok(ApiResponse.success(schedulerService.getOutboxSummary(), "Transactional outbox telemetry retrieved"));
    }

    @PostMapping("/outbox/retry-failed")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> retryFailed() {
        int count = schedulerService.retryFailedOrPendingForTenant();
        return ResponseEntity.ok(ApiResponse.success(
                Map.of("dispatchedCount", count),
                "Queued & dead-letter outbox events retried and dispatched"
        ));
    }

    @Data
    public static class CsvImportRequest {
        private String fileName;
        private String csvContent;
        private String initiatedBy;
    }

    @Data
    public static class EnqueueOutboxRequest {
        private String eventType;
        private String aggregateId;
        private String payload;
        private boolean simulateTransientFailure;
    }
}

