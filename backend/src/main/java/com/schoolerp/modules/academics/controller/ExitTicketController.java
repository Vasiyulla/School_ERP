package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.dto.*;
import com.schoolerp.modules.academics.service.ExitTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academics/exit-tickets")
@RequiredArgsConstructor
public class ExitTicketController {

    private final ExitTicketService exitTicketService;

    @PostMapping("/launch")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<ExitTicketSessionDTO>> launchSession(
            @RequestBody LaunchExitTicketRequest request,
            Authentication auth) {
        String teacherName = (auth != null) ? auth.getName() : "Faculty Member";
        String teacherId = (auth != null) ? auth.getName() : "teacher-01";
        ExitTicketSessionDTO session = exitTicketService.launchSession(request, teacherId, teacherName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("In-class exit ticket session launched successfully", session));
    }

    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'STUDENT', 'PARENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<String>> submitResponse(
            @RequestBody SubmitExitTicketResponseRequest request) {
        String result = exitTicketService.submitResponse(request);
        return ResponseEntity.ok(ApiResponse.ok("Student exit ticket response logged successfully", result));
    }

    @GetMapping("/{id}/heatmap")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<ExitTicketHeatmapDTO>> getHeatmap(@PathVariable UUID id) {
        ExitTicketHeatmapDTO heatmap = exitTicketService.getHeatmap(id);
        return ResponseEntity.ok(ApiResponse.ok("Real-time exit ticket comprehension heatmap retrieved", heatmap));
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<ApiResponse<ExitTicketSessionDTO>> closeSession(@PathVariable UUID id) {
        ExitTicketSessionDTO closed = exitTicketService.closeSession(id);
        return ResponseEntity.ok(ApiResponse.ok("Exit ticket session closed and aggregated successfully", closed));
    }
}

