package com.schoolerp.modules.operations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.operations.entity.EmergencyIncident;
import com.schoolerp.modules.operations.repository.EmergencyIncidentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyIncidentService {

    private final EmergencyIncidentRepository incidentRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public EmergencyIncident triggerEmergencySos(String incidentType, String severity, String location, String description, String reportedBy) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        EmergencyIncident incident = EmergencyIncident.builder()
                .tenantId(tenantId)
                .incidentType(incidentType != null ? incidentType.toUpperCase() : "GENERAL_EMERGENCY")
                .severity(severity != null ? severity.toUpperCase() : "CRITICAL")
                .location(location != null ? location : "Entire School Campus")
                .description(description != null ? description : "Emergency lockdown triggered via Campus SOS protocol.")
                .reportedBy(reportedBy != null ? reportedBy : "System / Security Operations")
                .status("ACTIVE_LOCKDOWN")
                .containmentActions("Perimeter gates locked; visitor access revoked; emergency broadcast dispatched to safety response team")
                .build();

        EmergencyIncident saved = incidentRepository.save(incident);

        auditLogService.recordEvent(
                "CAMPUS_EMERGENCY_LOCKDOWN_TRIGGERED",
                "operations.incidents",
                saved.getId().toString(),
                String.format("HIGH-PRIORITY SOS LOCKDOWN: [%s] at %s (Severity: %s, Reported by: %s)",
                        saved.getIncidentType(), saved.getLocation(), saved.getSeverity(), saved.getReportedBy())
        );

        log.warn("CAMPUS LOCKDOWN ACTIVATED for tenant {}: Incident ID {}", tenantId, saved.getId());
        return saved;
    }

    @Transactional
    public EmergencyIncident resolveIncident(UUID incidentId, String resolutionNotes) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        EmergencyIncident incident = incidentRepository.findById(incidentId)
                .filter(i -> i.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Incident not found: " + incidentId));

        incident.setStatus("RESOLVED");
        incident.setResolutionNotes(resolutionNotes != null ? resolutionNotes : "Incident contained and normal operations restored.");
        incident.setResolvedAt(LocalDateTime.now());

        EmergencyIncident updated = incidentRepository.save(incident);

        auditLogService.recordEvent(
                "CAMPUS_EMERGENCY_LOCKDOWN_RESOLVED",
                "operations.incidents",
                updated.getId().toString(),
                String.format("Emergency lockdown resolved for [%s]. Notes: %s", updated.getIncidentType(), updated.getResolutionNotes())
        );

        return updated;
    }

    public List<EmergencyIncident> getActiveIncidents() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return incidentRepository.findByTenantIdAndStatusOrderByCreatedAtDesc(tenantId, "ACTIVE_LOCKDOWN");
    }

    public List<EmergencyIncident> getAllIncidents() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return incidentRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }
}

