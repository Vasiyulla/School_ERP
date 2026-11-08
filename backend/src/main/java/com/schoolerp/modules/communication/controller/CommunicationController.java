package com.schoolerp.modules.communication.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.communication.entity.Circular;
import com.schoolerp.modules.communication.entity.NotificationDispatchLog;
import com.schoolerp.modules.communication.service.CommunicationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/communication")
@RequiredArgsConstructor
public class CommunicationController {

    private final CommunicationService communicationService;

    @GetMapping("/circulars")
    public ResponseEntity<ApiResponse<List<Circular>>> getCirculars() {
        return ResponseEntity.ok(ApiResponse.ok(communicationService.getCirculars()));
    }

    @PostMapping("/circulars")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<Circular>> publishCircular(@RequestBody Circular circular) {
        Circular saved = communicationService.publishCircular(circular);
        return ResponseEntity.ok(ApiResponse.ok("Circular published successfully", saved));
    }

    @GetMapping("/dispatches")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<NotificationDispatchLog>>> getDispatchLogs() {
        return ResponseEntity.ok(ApiResponse.ok(communicationService.getDispatchLogs()));
    }

    @PostMapping("/dispatches/manual")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<NotificationDispatchLog>> dispatchAlert(@RequestBody NotificationDispatchLog entry) {
        NotificationDispatchLog dispatched = communicationService.dispatchNotification(entry);
        return ResponseEntity.ok(ApiResponse.ok("Alert dispatched successfully", dispatched));
    }
}

