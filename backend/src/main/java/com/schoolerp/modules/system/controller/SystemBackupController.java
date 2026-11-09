package com.schoolerp.modules.system.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.system.entity.SystemBackupRecord;
import com.schoolerp.modules.system.service.SystemBackupService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
public class SystemBackupController {

    private final SystemBackupService backupService;

    @PostMapping("/backups/create")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @RequireFeature("system.backup_management")
    public ResponseEntity<ApiResponse<SystemBackupRecord>> createBackup(@RequestBody(required = false) CreateBackupRequest request) {
        String type = (request != null && request.getBackupType() != null) ? request.getBackupType() : "FULL_POD_SNAPSHOT";
        String actor = (request != null && request.getInitiatedBy() != null) ? request.getInitiatedBy() : "SUPER_ADMIN";

        SystemBackupRecord record = backupService.createPodBackup(type, actor);
        return ResponseEntity.ok(ApiResponse.success(record, "Pod backup snapshot generated successfully"));
    }

    @GetMapping("/backups")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL')")
    @RequireFeature("system.backup_management")
    public ResponseEntity<ApiResponse<List<SystemBackupRecord>>> getBackupHistory() {
        return ResponseEntity.ok(ApiResponse.success(backupService.getBackupHistory(), "Pod backup history retrieved"));
    }

    @GetMapping("/health/telemetry")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL')")
    @RequireFeature("system.backup_management")
    public ResponseEntity<ApiResponse<SystemBackupService.PodHealthTelemetry>> getHealthTelemetry() {
        return ResponseEntity.ok(ApiResponse.success(backupService.getPodHealthTelemetry(), "Pod telemetry retrieved"));
    }

    @Data
    public static class CreateBackupRequest {
        private String backupType;
        private String initiatedBy;
    }
}

