package com.schoolerp.modules.students.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.entity.StudentCertificateRecord;
import com.schoolerp.modules.students.entity.StudentUdiseProfile;
import com.schoolerp.modules.students.service.IndianComplianceService;
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
public class IndianComplianceController {

    private final IndianComplianceService complianceService;

    @PostMapping("/{id}/certificates/generate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMINISTRATIVE_OFFICER')")
    @RequireFeature("students.certificates")
    public ResponseEntity<ApiResponse<StudentCertificateRecord>> generateCertificate(
            @PathVariable UUID id,
            @RequestBody GenerateCertificateRequest request) {
        StudentCertificateRecord record = complianceService.generateCertificate(
                id,
                request.getCertificateType(),
                request.getPurpose(),
                request.getIssuedBy()
        );
        return ResponseEntity.ok(ApiResponse.success(record, "Certificate generated with cryptographic verification token"));
    }

    @GetMapping("/certificates/verify/{qrToken}")
    public ResponseEntity<ApiResponse<IndianComplianceService.CertificateVerificationDTO>> verifyCertificate(
            @PathVariable String qrToken) {
        IndianComplianceService.CertificateVerificationDTO verification = complianceService.verifyCertificate(qrToken);
        return ResponseEntity.ok(ApiResponse.success(verification, "Certificate authenticity digitally verified"));
    }

    @GetMapping("/udise/profiles")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @RequireFeature("students.udise_compliance")
    public ResponseEntity<ApiResponse<List<StudentUdiseProfile>>> getUdiseProfiles() {
        return ResponseEntity.ok(ApiResponse.success(complianceService.getUdiseProfiles(), "Student UDISE+ and APAAR ID profiles retrieved"));
    }

    @GetMapping("/udise/export")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL')")
    @RequireFeature("students.udise_compliance")
    public ResponseEntity<ApiResponse<IndianComplianceService.UdiseExportSummary>> exportUdiseData() {
        return ResponseEntity.ok(ApiResponse.success(complianceService.exportUdiseData(), "Ministry of Education UDISE+ export compiled successfully"));
    }

    @Data
    public static class GenerateCertificateRequest {
        private String certificateType; // BONAFIDE, CHARACTER, FEE_ESTIMATION_CEA, STUDY_CONDUCT
        private String purpose;
        private String issuedBy;
    }
}

