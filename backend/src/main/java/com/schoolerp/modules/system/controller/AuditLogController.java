package com.schoolerp.modules.system.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.system.entity.AuditLog;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/system/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs() {
        List<AuditLog> logs = auditLogService.getLogs();
        return ResponseEntity.ok(ApiResponse.ok(logs));
    }

    @GetMapping("/paged")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getPagedAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLog> paged = auditLogService.getPagedLogs(page, size);
        return ResponseEntity.ok(ApiResponse.ok(paged));
    }
}

