package com.schoolerp.modules.institution.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.institution.dto.ExecutiveCockpitKpiDTO;
import com.schoolerp.modules.institution.service.ExecutiveCockpitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/institution/executive-cockpit")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Executive Strategic Command Cockpit", description = "High-level board KPIs: Capacity, Fee Realization Rate, Teacher-Student Ratio, Safety Index, and Institutional Health Tier")
public class ExecutiveCockpitController {

    private final ExecutiveCockpitService cockpitService;

    @GetMapping("/kpis")
    @PreAuthorize("hasAnyRole('PRINCIPAL', 'SUPER_ADMIN')")
    @Operation(summary = "Get real-time Board of Directors and Principal executive KPI health snapshot")
    public ResponseEntity<ApiResponse<ExecutiveCockpitKpiDTO>> getExecutiveKpis() {
        ExecutiveCockpitKpiDTO kpis = cockpitService.getExecutiveKpis();
        return ResponseEntity.ok(ApiResponse.ok(kpis));
    }
}

