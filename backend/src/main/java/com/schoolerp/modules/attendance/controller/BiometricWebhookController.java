package com.schoolerp.modules.attendance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.attendance.dto.BiometricDailyRosterDTO;
import com.schoolerp.modules.attendance.dto.BiometricPunchWebhookRequest;
import com.schoolerp.modules.attendance.dto.BiometricSyncResultDTO;
import com.schoolerp.modules.attendance.service.BiometricSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/attendance/biometric")
@RequiredArgsConstructor
public class BiometricWebhookController {

    private final BiometricSyncService biometricService;

    @PostMapping("/webhook")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HR_MANAGER', 'SYSTEM')")
    public ResponseEntity<ApiResponse<BiometricSyncResultDTO>> receiveBiometricWebhook(
            @RequestBody BiometricPunchWebhookRequest request) {
        BiometricSyncResultDTO result = biometricService.ingestBiometricPunches(request);
        return ResponseEntity.ok(ApiResponse.ok("Biometric punches received and processed successfully", result));
    }

    @GetMapping("/daily-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'HR_MANAGER', 'TEACHER')")
    public ResponseEntity<ApiResponse<BiometricDailyRosterDTO>> getDailySummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BiometricDailyRosterDTO roster = biometricService.getDailyBiometricSummary(date);
        return ResponseEntity.ok(ApiResponse.ok("Daily biometric attendance summary retrieved successfully", roster));
    }
}
