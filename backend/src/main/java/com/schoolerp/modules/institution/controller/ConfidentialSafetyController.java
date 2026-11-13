package com.schoolerp.modules.institution.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.institution.dto.LogSafetyIncidentRequest;
import com.schoolerp.modules.institution.entity.ConfidentialSafetyIncident;
import com.schoolerp.modules.institution.service.ConfidentialSafetyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/institution/safety-vault")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Confidential Child Safety & Statutory Committee Vault", description = "Restricted Principal & Board vault for POCSO, POSH, Anti-Bullying, and statutory safety inquiries")
public class ConfidentialSafetyController {

    private final ConfidentialSafetyService safetyService;

    @PostMapping("/incidents")
    @PreAuthorize("hasAnyRole('PRINCIPAL', 'SUPER_ADMIN')")
    @Operation(summary = "Log a confidential safety incident with statutory authority notification audit")
    public ResponseEntity<ApiResponse<ConfidentialSafetyIncident>> logIncident(
            @Valid @RequestBody LogSafetyIncidentRequest request) {
        ConfidentialSafetyIncident incident = safetyService.logIncident(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Confidential safety incident registered in secure vault", incident));
    }

    @GetMapping("/incidents")
    @PreAuthorize("hasAnyRole('PRINCIPAL', 'SUPER_ADMIN')")
    @Operation(summary = "Retrieve confidential safety incidents (restricted to Principal and Super Admin)")
    public ResponseEntity<ApiResponse<List<ConfidentialSafetyIncident>>> getIncidents(
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(required = false, defaultValue = "ALL") String classification) {
        List<ConfidentialSafetyIncident> list = safetyService.getIncidents(status, classification);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PutMapping("/incidents/{incidentId}/resolve")
    @PreAuthorize("hasAnyRole('PRINCIPAL', 'SUPER_ADMIN')")
    @Operation(summary = "Conclude inquiry and record formal disciplinary or statutory disposition")
    public ResponseEntity<ApiResponse<ConfidentialSafetyIncident>> resolveIncident(
            @PathVariable UUID incidentId,
            @RequestParam(required = false, defaultValue = "CLOSED_RESOLVED") String dispositionStatus,
            @RequestParam String closingRemarks) {
        ConfidentialSafetyIncident resolved = safetyService.resolveIncident(incidentId, dispositionStatus, closingRemarks);
        return ResponseEntity.ok(ApiResponse.ok("Safety incident inquiry concluded and disposition sealed", resolved));
    }
}

