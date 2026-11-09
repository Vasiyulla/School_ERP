package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.entity.EmergencyIncident;
import com.schoolerp.modules.operations.service.EmergencyIncidentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operations/incidents")
@RequiredArgsConstructor
public class EmergencyIncidentController {

    private final EmergencyIncidentService incidentService;

    @PostMapping("/sos")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','CAMPUS_WARDEN','SECURITY_HEAD')")
    @RequireFeature("operations.emergency_sos")
    public ResponseEntity<ApiResponse<EmergencyIncident>> triggerSos(@RequestBody TriggerSosRequest request) {
        EmergencyIncident incident = incidentService.triggerEmergencySos(
                request.getIncidentType(),
                request.getSeverity(),
                request.getLocation(),
                request.getDescription(),
                request.getReportedBy()
        );
        return ResponseEntity.ok(ApiResponse.success(incident, "Emergency SOS lockdown initiated successfully"));
    }

    @PostMapping("/{id}/resolve")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','CAMPUS_WARDEN')")
    @RequireFeature("operations.emergency_sos")
    public ResponseEntity<ApiResponse<EmergencyIncident>> resolveIncident(
            @PathVariable UUID id,
            @RequestBody(required = false) ResolveIncidentRequest request) {
        String notes = request != null ? request.getResolutionNotes() : "Resolved by campus safety team.";
        EmergencyIncident resolved = incidentService.resolveIncident(id, notes);
        return ResponseEntity.ok(ApiResponse.success(resolved, "Incident resolved and campus restored to normal status"));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','CAMPUS_WARDEN','SECURITY_HEAD')")
    @RequireFeature("operations.emergency_sos")
    public ResponseEntity<ApiResponse<List<EmergencyIncident>>> getActiveIncidents() {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getActiveIncidents(), "Active emergency incidents retrieved"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','CAMPUS_WARDEN','SECURITY_HEAD')")
    @RequireFeature("operations.emergency_sos")
    public ResponseEntity<ApiResponse<List<EmergencyIncident>>> getAllIncidents() {
        return ResponseEntity.ok(ApiResponse.success(incidentService.getAllIncidents(), "All emergency incidents retrieved"));
    }

    @Data
    public static class TriggerSosRequest {
        private String incidentType;
        private String severity;
        private String location;
        private String description;
        private String reportedBy;
    }

    @Data
    public static class ResolveIncidentRequest {
        private String resolutionNotes;
    }
}

