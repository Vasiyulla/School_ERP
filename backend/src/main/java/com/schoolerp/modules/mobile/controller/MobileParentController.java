package com.schoolerp.modules.mobile.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.mobile.dto.ParentHomeDashboardDTO;
import com.schoolerp.modules.mobile.service.MobileParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mobile/parent")
@RequiredArgsConstructor
public class MobileParentController {

    private final MobileParentService mobileParentService;

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<ParentHomeDashboardDTO>> getParentHome(
            @RequestParam(required = false) String guardianEmail) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String effectiveEmail = guardianEmail;

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String principalEmail = auth.getName();
            boolean isStaffOrAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().contains("ADMIN") || a.getAuthority().contains("PRINCIPAL") || a.getAuthority().contains("TEACHER"));

            // If caller is not staff/admin, strictly bind query to their authenticated email (IDOR Defense)
            if (!isStaffOrAdmin || effectiveEmail == null || effectiveEmail.isBlank()) {
                effectiveEmail = principalEmail;
            }
        }

        ParentHomeDashboardDTO dashboard = mobileParentService.getParentDashboard(effectiveEmail);
        return ResponseEntity.ok(ApiResponse.ok("Mobile parent home dashboard aggregated successfully", dashboard));
    }
}
