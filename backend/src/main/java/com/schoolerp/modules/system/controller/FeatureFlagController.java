package com.schoolerp.modules.system.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.system.entity.TenantFeatureFlag;
import com.schoolerp.modules.system.service.TenantFeatureFlagService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/system/features")
@RequiredArgsConstructor
public class FeatureFlagController {

    private final TenantFeatureFlagService featureFlagService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TenantFeatureFlag>>> getAllFeatures() {
        return ResponseEntity.ok(ApiResponse.ok("Retrieved institutional sub-service feature registry", featureFlagService.getAllFeatures()));
    }

    @PostMapping("/toggle")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<TenantFeatureFlag>> toggleFeature(@RequestBody ToggleFeatureRequest request) {
        TenantFeatureFlag flag = featureFlagService.toggleFeature(request.getFeatureKey(), request.isEnabled(), request.getTier());
        return ResponseEntity.ok(ApiResponse.ok("Sub-service feature flag updated", flag));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkFeature(@RequestParam String key) {
        boolean enabled = featureFlagService.isFeatureEnabled(key);
        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "featureKey", key,
                "enabled", enabled
        )));
    }

    @Data
    public static class ToggleFeatureRequest {
        private String featureKey;
        private boolean enabled;
        private String tier;
    }
}

