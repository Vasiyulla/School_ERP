package com.schoolerp.modules.identity.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.identity.dto.MfaDTOs;
import com.schoolerp.modules.identity.service.MfaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/identity/mfa")
@RequiredArgsConstructor
public class MfaController {

    private final MfaService mfaService;

    @PostMapping("/setup")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MfaDTOs.SetupResponse>> setupMfa(
            @RequestBody(required = false) MfaDTOs.VerifyRequest request,
            Principal principal) {
        String email = request != null && request.getEmail() != null && !request.getEmail().isBlank()
                ? request.getEmail()
                : (principal != null ? principal.getName() : "admin@apex.edu.in");

        MfaDTOs.SetupResponse response = mfaService.setupMfa(email);
        return ResponseEntity.ok(ApiResponse.ok("MFA setup profile generated", response));
    }

    @PostMapping("/verify")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MfaDTOs.VerifyResponse>> verifyMfa(
            @RequestBody MfaDTOs.VerifyRequest request,
            Principal principal) {
        String email = request.getEmail() != null && !request.getEmail().isBlank()
                ? request.getEmail()
                : (principal != null ? principal.getName() : "admin@apex.edu.in");

        MfaDTOs.VerifyResponse response = mfaService.verifyMfa(email, request.getCode());
        return ResponseEntity.ok(ApiResponse.ok("MFA verification processed", response));
    }
}
