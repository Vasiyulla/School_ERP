package com.schoolerp.modules.attendance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.attendance.dto.*;
import com.schoolerp.modules.attendance.service.VirtualAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance/virtual")
@RequiredArgsConstructor
public class VirtualAttendanceController {

    private final VirtualAttendanceService virtualAttendanceService;

    @PostMapping("/log-presence")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STUDENT', 'TEACHER', 'PARENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<VirtualAttendanceTelemetryDTO>> logPresence(
            @RequestBody LogVirtualPresenceRequest request) {
        VirtualAttendanceTelemetryDTO telemetry = virtualAttendanceService.logPresence(request);
        return ResponseEntity.ok(ApiResponse.ok("Virtual classroom attendance presence logged successfully", telemetry));
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<VirtualSessionAttendanceRosterDTO>> getSessionAttendanceRoster(
            @PathVariable UUID sessionId) {
        VirtualSessionAttendanceRosterDTO roster = virtualAttendanceService.getSessionAttendanceRoster(sessionId);
        return ResponseEntity.ok(ApiResponse.ok("Virtual classroom attendance roster retrieved successfully", roster));
    }
}

