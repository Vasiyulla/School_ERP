package com.schoolerp.modules.communication.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.communication.entity.PtmSession;
import com.schoolerp.modules.communication.entity.PtmSlot;
import com.schoolerp.modules.communication.service.PtmService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/communication/ptm")
@RequiredArgsConstructor
public class PtmController {

    private final PtmService ptmService;

    @Data
    public static class BookSlotRequest {
        private UUID studentId;
        private String guardianName;
        private String guardianPhone;
    }

    @GetMapping("/sessions/active")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'PARENT')")
    public ResponseEntity<ApiResponse<List<PtmSession>>> getActiveSessions() {
        return ResponseEntity.ok(ApiResponse.ok(ptmService.getActiveSessions()));
    }

    @GetMapping("/sessions/{sessionId}/slots")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'PARENT')")
    public ResponseEntity<ApiResponse<List<PtmSlot>>> getAvailableSlots(
            @PathVariable UUID sessionId,
            @RequestParam(required = false) UUID teacherId) {
        return ResponseEntity.ok(ApiResponse.ok(ptmService.getAvailableSlots(sessionId, teacherId)));
    }

    @PostMapping("/slots/{slotId}/book")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'PARENT')")
    public ResponseEntity<ApiResponse<PtmSlot>> bookSlot(
            @PathVariable UUID slotId,
            @RequestBody BookSlotRequest req) {
        PtmSlot booked = ptmService.bookSlot(slotId, req.getStudentId(), req.getGuardianName(), req.getGuardianPhone());
        return ResponseEntity.ok(ApiResponse.ok("PTM conference slot booked successfully", booked));
    }

    @GetMapping("/student/{studentId}/bookings")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'PARENT')")
    public ResponseEntity<ApiResponse<List<PtmSlot>>> getStudentBookings(@PathVariable UUID studentId) {
        return ResponseEntity.ok(ApiResponse.ok(ptmService.getStudentBookings(studentId)));
    }
}

