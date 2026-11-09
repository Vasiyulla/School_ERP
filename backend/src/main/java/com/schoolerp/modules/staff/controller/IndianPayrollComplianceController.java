package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.service.IndianPayrollComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/staff/payroll")
@RequiredArgsConstructor
public class IndianPayrollComplianceController {

    private final IndianPayrollComplianceService complianceService;

    @GetMapping("/epfo-ecr/export")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ACCOUNTANT')")
    @RequireFeature("staff.indian_payroll_compliance")
    public ResponseEntity<ApiResponse<IndianPayrollComplianceService.EpfoEcrExportDTO>> exportEpfoEcr(
            @RequestParam(required = false, defaultValue = "2026-11") String monthYear) {
        return ResponseEntity.ok(ApiResponse.success(
                complianceService.generateEpfoEcrExport(monthYear),
                "EPFO Electronic Challan cum Return (ECR) compiled for Unified Portal"
        ));
    }

    @GetMapping("/esic-return/export")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ACCOUNTANT')")
    @RequireFeature("staff.indian_payroll_compliance")
    public ResponseEntity<ApiResponse<IndianPayrollComplianceService.EsicMonthlyReturnDTO>> exportEsicReturn(
            @RequestParam(required = false, defaultValue = "2026-11") String monthYear) {
        return ResponseEntity.ok(ApiResponse.success(
                complianceService.generateEsicReturnExport(monthYear),
                "ESIC Monthly Contribution Return compiled successfully"
        ));
    }
}

