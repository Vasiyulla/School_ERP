package com.schoolerp.modules.communication.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.communication.entity.Circular;
import com.schoolerp.modules.communication.entity.DeviceToken;
import com.schoolerp.modules.communication.entity.NotificationDispatchLog;
import com.schoolerp.modules.communication.service.CommunicationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping("/devices/register")
    public ResponseEntity<ApiResponse<DeviceToken>> registerDevice(@RequestBody DeviceToken registration) {
        DeviceToken token = communicationService.registerDevice(registration);
        return ResponseEntity.ok(ApiResponse.ok("Device token registered for mobile push notifications", token));
    }

    @PostMapping("/devices/unregister")
    public ResponseEntity<ApiResponse<DeviceToken>> unregisterDevice(@RequestParam String deviceToken) {
        DeviceToken token = communicationService.unregisterDevice(deviceToken);
        return ResponseEntity.ok(ApiResponse.ok("Device token unregistered successfully", token));
    }

    @GetMapping("/devices")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<List<DeviceToken>>> getActiveDevices() {
        return ResponseEntity.ok(ApiResponse.ok(communicationService.getActiveDevices()));
    }

    @PostMapping("/devices/push-test")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<NotificationDispatchLog>> sendPushNotification(@RequestBody PushNotificationTestRequest req) {
        NotificationDispatchLog log = communicationService.sendPushNotification(
                req.getUserId(),
                req.getRecipientName(),
                req.getTitle(),
                req.getBody(),
                req.getEventType()
        );
        return ResponseEntity.ok(ApiResponse.ok("Mobile push notification dispatched", log));
    }

    @Data
    public static class PushNotificationTestRequest {
        private UUID userId;
        private String recipientName;
        private String title;
        private String body;
        private String eventType;
    }
}
