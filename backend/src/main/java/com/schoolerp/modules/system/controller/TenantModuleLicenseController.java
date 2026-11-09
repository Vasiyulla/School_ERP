package com.schoolerp.modules.system.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.system.entity.TenantModuleLicense;
import com.schoolerp.modules.system.service.TenantModuleLicenseService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/system/modules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class TenantModuleLicenseController {

    private final TenantModuleLicenseService licenseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TenantModuleLicense>>> getTenantModules() {
        return ResponseEntity.ok(ApiResponse.ok(licenseService.getTenantModules()));
    }

    @PostMapping("/{moduleKey}/toggle")
    public ResponseEntity<ApiResponse<TenantModuleLicense>> toggleModule(
            @PathVariable String moduleKey,
            @RequestBody ToggleModuleRequest req) {
        String actor = SecurityContextHolder.getContext().getAuthentication().getName();
        TenantModuleLicense license = licenseService.toggleModule(moduleKey, req.isEnabled(), actor);
        return ResponseEntity.ok(ApiResponse.ok("Module license updated successfully", license));
    }

    @Data
    public static class ToggleModuleRequest {
        private boolean enabled;
    }
}

