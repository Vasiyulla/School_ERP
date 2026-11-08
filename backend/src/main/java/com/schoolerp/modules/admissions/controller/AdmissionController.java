package com.schoolerp.modules.admissions.controller;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.admissions.entity.AdmissionApplication;
import com.schoolerp.modules.admissions.repository.AdmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admissions")
@RequiredArgsConstructor
public class AdmissionController {

    private final AdmissionRepository admissionRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdmissionApplication>>> getPipeline() {
        List<AdmissionApplication> applications = admissionRepository.findAllByTenantId(TenantContext.getTenantId());
        return ResponseEntity.ok(ApiResponse.ok(applications));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<AdmissionApplication>> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        AdmissionApplication app = admissionRepository.findByIdAndTenantId(id, TenantContext.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Admission application not found"));
        app.setStatus(status.toUpperCase());
        AdmissionApplication saved = admissionRepository.save(app);
        return ResponseEntity.ok(ApiResponse.ok("Status updated", saved));
    }
}

