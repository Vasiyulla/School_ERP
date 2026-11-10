package com.schoolerp.modules.identity.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.identity.dto.AuthResponse;
import com.schoolerp.modules.identity.entity.UserSession;
import com.schoolerp.modules.identity.service.SessionSecurityService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SessionSecurityController {

    private final SessionSecurityService sessionSecurityService;

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest request) {
        AuthResponse response = sessionSecurityService.rotateRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.ok("Session refreshed via cryptographic token rotation", response));
    }

    @GetMapping("/identity/sessions/active")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','ACCOUNTANT','PARENT')")
    public ResponseEntity<ApiResponse<List<UserSession>>> getActiveSessions(
            @RequestParam(required = false) String userEmail) {
        return ResponseEntity.ok(ApiResponse.success(
                sessionSecurityService.getActiveSessions(userEmail),
                "Active user sessions retrieved"
        ));
    }

    @PostMapping("/identity/sessions/revoke-all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> revokeAll(@RequestBody RevokeRequest request) {
        int count = sessionSecurityService.revokeAllSessionsForUser(request.getUserEmail(), request.getReason());
        return ResponseEntity.ok(ApiResponse.success(
                Map.of("userEmail", request.getUserEmail(), "revokedCount", count),
                "All active sessions revoked cleanly"
        ));
    }

    @Data
    public static class RefreshRequest {
        private String refreshToken;
    }

    @Data
    public static class RevokeRequest {
        private String userEmail;
        private String reason;
    }
}

