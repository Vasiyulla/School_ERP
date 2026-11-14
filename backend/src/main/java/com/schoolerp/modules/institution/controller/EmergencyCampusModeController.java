package com.schoolerp.modules.institution.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.institution.dto.*;
import com.schoolerp.modules.institution.service.EmergencyCampusModeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/institution/emergency-mode")
@RequiredArgsConstructor
public class EmergencyCampusModeController {

    private final EmergencyCampusModeService emergencyService;

    @PostMapping("/pivot")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL')")
    public ResponseEntity<ApiResponse<EmergencyCampusModeDTO>> declareEmergencyMode(
            @RequestBody DeclareEmergencyModeRequest request,
            Authentication auth) {
        String staffName = (auth != null) ? auth.getName() : "Dr. Rajesh Sharma (Principal)";
        EmergencyCampusModeDTO declared = emergencyService.declareEmergencyMode(request, staffName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Campus emergency operating mode declared successfully", declared));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT', 'STAFF')")
    public ResponseEntity<ApiResponse<EmergencyCampusModeDTO>> getCurrentStatus() {
        EmergencyCampusModeDTO status = emergencyService.getCurrentStatus();
        return ResponseEntity.ok(ApiResponse.ok("Current campus operational mode retrieved", status));
    }

    @PostMapping("/restore-physical")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL')")
    public ResponseEntity<ApiResponse<EmergencyCampusModeDTO>> restorePhysical(Authentication auth) {
        String staffName = (auth != null) ? auth.getName() : "Dr. Rajesh Sharma (Principal)";
        EmergencyCampusModeDTO restored = emergencyService.restorePhysicalCampus(staffName);
        return ResponseEntity.ok(ApiResponse.ok("Normal physical campus operations restored", restored));
    }

    @PostMapping("/safety-checkin")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT', 'STAFF')")
    public ResponseEntity<ApiResponse<EmergencySafetyCheckInDTO>> submitSafetyCheckIn(
            @RequestBody SubmitSafetyCheckInRequest request) {
        EmergencySafetyCheckInDTO checkIn = emergencyService.submitSafetyCheckIn(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Emergency welfare safety check-in recorded successfully", checkIn));
    }

    @GetMapping("/safety-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL')")
    public ResponseEntity<ApiResponse<SafetyCheckInSummaryDTO>> getSafetySummary() {
        SafetyCheckInSummaryDTO summary = emergencyService.getSafetyCheckInSummary();
        return ResponseEntity.ok(ApiResponse.ok("Campus emergency safety and welfare summary retrieved", summary));
    }
}

