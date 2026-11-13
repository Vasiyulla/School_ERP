package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.dto.RecordLabBreakageRequest;
import com.schoolerp.modules.operations.entity.ApparatusBreakage;
import com.schoolerp.modules.operations.entity.LabItem;
import com.schoolerp.modules.operations.service.LabInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operations/lab")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Science & STEM Lab Inventory & Breakage Engine", description = "Track laboratory apparatus, log practical exam breakages, and auto-assess replacement fines on student fee ledger")
public class LabInventoryController {

    private final LabInventoryService labInventoryService;

    @GetMapping("/inventory")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Get lab inventory items and current stock levels across science and STEM labs")
    public ResponseEntity<ApiResponse<List<LabItem>>> getInventory(
            @RequestParam(required = false, defaultValue = "ALL") String labName) {
        List<LabItem> items = labInventoryService.getInventory(labName);
        return ResponseEntity.ok(ApiResponse.ok(items));
    }

    @PostMapping("/breakages")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Record apparatus breakage, decrement inventory, and post auxiliary damage fine to student fee ledger")
    public ResponseEntity<ApiResponse<ApparatusBreakage>> recordBreakage(
            @Valid @RequestBody RecordLabBreakageRequest request,
            Authentication auth) {
        String technician = (auth != null) ? auth.getName() : "Lab Demonstrator";
        ApparatusBreakage record = labInventoryService.recordBreakage(request, technician);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Lab breakage assessed and auxiliary fee invoice generated", record));
    }

    @GetMapping("/breakages")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "List apparatus breakage incidents and damage fee audits")
    public ResponseEntity<ApiResponse<List<ApparatusBreakage>>> getBreakages(
            @RequestParam(required = false, defaultValue = "ALL") String labName) {
        List<ApparatusBreakage> list = labInventoryService.getBreakages(labName);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}

