package com.schoolerp.modules.attendance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.attendance.dto.BatchAttendanceRequest;
import com.schoolerp.modules.attendance.dto.CorrectionRequest;
import com.schoolerp.modules.attendance.entity.AttendanceCorrection;
import com.schoolerp.modules.attendance.entity.AttendanceSession;
import com.schoolerp.modules.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/sessions/batch")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<AttendanceSession>> submitBatch(
            @Valid @RequestBody BatchAttendanceRequest request) {
        AttendanceSession session = attendanceService.submitBatchAttendance(request);
        return ResponseEntity.ok(ApiResponse.ok("Batch attendance submitted and locked", session));
    }

    @PostMapping("/records/{recordId}/correct")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<AttendanceCorrection>> correctAttendance(
            @PathVariable UUID recordId,
            @Valid @RequestBody CorrectionRequest request) {
        AttendanceCorrection correction = attendanceService.correctAttendance(recordId, request);
        return ResponseEntity.ok(ApiResponse.ok("Attendance corrected and audited", correction));
    }

    @GetMapping("/corrections")
    public ResponseEntity<ApiResponse<List<AttendanceCorrection>>> getCorrections() {
        List<AttendanceCorrection> corrections = attendanceService.getCorrections();
        return ResponseEntity.ok(ApiResponse.ok(corrections));
    }
}

