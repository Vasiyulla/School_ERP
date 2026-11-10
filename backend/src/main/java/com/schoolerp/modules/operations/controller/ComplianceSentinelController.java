package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.entity.ComplianceAssetRecord;
import com.schoolerp.modules.operations.service.ComplianceSentinelService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/operations/compliance-sentinel")
@RequiredArgsConstructor
public class ComplianceSentinelController {

    private final ComplianceSentinelService sentinelService;

    @PostMapping("/assets")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TRANSPORT_MANAGER')")
    @RequireFeature("operations.compliance_sentinel")
    public ResponseEntity<ApiResponse<ComplianceAssetRecord>> registerAsset(@RequestBody RegisterComplianceAssetRequest request) {
        ComplianceAssetRecord record = sentinelService.registerComplianceAsset(
                request.getAssetName(),
                request.getCategory(),
                request.getCertificateNumber(),
                request.getIssuingAuthority(),
                request.getExpiryDate(),
                request.getCustodianOfficer()
        );
        return ResponseEntity.ok(ApiResponse.success(record, "Statutory licence / safety compliance registered cleanly"));
    }

    @GetMapping("/expiring")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TRANSPORT_MANAGER')")
    @RequireFeature("operations.compliance_sentinel")
    public ResponseEntity<ApiResponse<List<ComplianceSentinelService.ComplianceAlertDTO>>> getExpiringAssets(
            @RequestParam(required = false, defaultValue = "60") int withinDays) {
        return ResponseEntity.ok(ApiResponse.success(
                sentinelService.scanExpiringCompliance(withinDays),
                "Campus & fleet expiry countdown telemetry scanned"
        ));
    }

    @Data
    public static class RegisterComplianceAssetRequest {
        private String assetName;
        private String category;
        private String certificateNumber;
        private String issuingAuthority;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate expiryDate;
        private String custodianOfficer;
    }
}

