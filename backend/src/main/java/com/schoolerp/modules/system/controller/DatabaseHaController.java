package com.schoolerp.modules.system.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.system.dto.DatabaseHaClusterStatusDTO;
import com.schoolerp.modules.system.dto.SimulateFailoverRequest;
import com.schoolerp.modules.system.dto.SimulateFailoverResult;
import com.schoolerp.modules.system.service.DatabaseHaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/system/database")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Database High Availability & Failover Cluster", description = "Dedicated Dual-DB (Primary + Secondary) HA Pair management with real-time streaming replication and instant failover")
public class DatabaseHaController {

    private final DatabaseHaService databaseHaService;

    @GetMapping("/ha-status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PRINCIPAL')")
    @RequireFeature("system.backup_management")
    @Operation(summary = "Get dedicated Dual-DB Primary and Secondary cluster health and replication status")
    public ResponseEntity<ApiResponse<DatabaseHaClusterStatusDTO>> getHaStatus() {
        DatabaseHaClusterStatusDTO status = databaseHaService.getHaClusterStatus();
        return ResponseEntity.ok(ApiResponse.ok(status));
    }

    @PostMapping("/simulate-failover")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @RequireFeature("system.backup_management")
    @Operation(summary = "Simulate active Primary crash and trigger instant automated promotion of Secondary to Primary")
    public ResponseEntity<ApiResponse<SimulateFailoverResult>> simulateFailover(
            @RequestBody(required = false) SimulateFailoverRequest request) {
        SimulateFailoverResult result = databaseHaService.simulateFailover(request);
        return ResponseEntity.ok(ApiResponse.ok("Automated failover drill executed successfully", result));
    }

    @PostMapping("/reset-topology")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @RequireFeature("system.backup_management")
    @Operation(summary = "Reset cluster topology back to default db-primary active and db-secondary standby")
    public ResponseEntity<ApiResponse<DatabaseHaClusterStatusDTO>> resetTopology() {
        DatabaseHaClusterStatusDTO status = databaseHaService.resetClusterTopology();
        return ResponseEntity.ok(ApiResponse.ok("Cluster topology reset to normal HA state", status));
    }
}
