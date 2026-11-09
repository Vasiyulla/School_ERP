package com.schoolerp.modules.students.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.entity.StudentClearanceRequest;
import com.schoolerp.modules.students.entity.TransferCertificate;
import com.schoolerp.modules.students.service.StudentClearanceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentClearanceController {

    private final StudentClearanceService clearanceService;

    @Data
    public static class GenerateTcRequest {
        private String reasonForLeaving;
        private String generalConduct;
    }

    @PostMapping("/{id}/clearance/evaluate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<StudentClearanceRequest>> evaluateClearance(@PathVariable UUID id) {
        StudentClearanceRequest req = clearanceService.initiateOrRefreshClearance(id);
        return ResponseEntity.ok(ApiResponse.ok("Student departure clearance evaluated", req));
    }

    @PostMapping("/{id}/transfer-certificate/generate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL')")
    public ResponseEntity<ApiResponse<TransferCertificate>> generateTransferCertificate(
            @PathVariable UUID id,
            @RequestBody(required = false) GenerateTcRequest req) {
        String reason = req != null ? req.getReasonForLeaving() : "PARENT_RELOCATION";
        String conduct = req != null ? req.getGeneralConduct() : "EXEMPLARY";
        TransferCertificate tc = clearanceService.generateTransferCertificate(id, reason, conduct);
        return ResponseEntity.ok(ApiResponse.ok("Transfer Certificate issued successfully", tc));
    }

    @GetMapping("/transfer-certificates/verify/{hash}")
    public ResponseEntity<ApiResponse<TransferCertificate>> verifyTransferCertificate(@PathVariable String hash) {
        TransferCertificate tc = clearanceService.verifyCertificate(hash);
        return ResponseEntity.ok(ApiResponse.ok("Transfer Certificate verified as authentic", tc));
    }

    @GetMapping("/transfer-certificates")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL')")
    public ResponseEntity<ApiResponse<List<TransferCertificate>>> getAllCertificates() {
        return ResponseEntity.ok(ApiResponse.ok(clearanceService.getAllCertificates()));
    }
}

