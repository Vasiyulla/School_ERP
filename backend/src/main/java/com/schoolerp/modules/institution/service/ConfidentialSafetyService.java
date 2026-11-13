package com.schoolerp.modules.institution.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.institution.dto.LogSafetyIncidentRequest;
import com.schoolerp.modules.institution.entity.ConfidentialSafetyIncident;
import com.schoolerp.modules.institution.repository.ConfidentialSafetyIncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfidentialSafetyService {

    private final ConfidentialSafetyIncidentRepository safetyRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public ConfidentialSafetyIncident logIncident(LogSafetyIncidentRequest request) {
        String tenantId = resolveTenant();

        long count = safetyRepository.countByTenantId(tenantId);
        String refCode = String.format("SAFE-%d-%04d", LocalDate.now().getYear(), count + 1);

        ConfidentialSafetyIncident incident = ConfidentialSafetyIncident.builder()
                .tenantId(tenantId)
                .incidentRefCode(refCode)
                .classification(request.getClassification().toUpperCase())
                .severityLevel(request.getSeverityLevel() != null ? request.getSeverityLevel().toUpperCase() : "HIGH_AMBER")
                .involvedStudentOrStaffId(request.getInvolvedStudentOrStaffId())
                .involvedPartyName(request.getInvolvedPartyName())
                .roleInvolved(request.getRoleInvolved() != null ? request.getRoleInvolved().toUpperCase() : "STUDENT")
                .incidentSummary(request.getIncidentSummary())
                .actionTakenInterim(request.getActionTakenInterim())
                .statutoryNotificationRequired(request.isStatutoryNotificationRequired())
                .statutoryAuthorityRef(request.getStatutoryAuthorityRef())
                .statutoryNotificationDate(request.isStatutoryNotificationRequired() ? LocalDateTime.now() : null)
                .inquiryCommitteeMembers(request.getInquiryCommitteeMembers() != null ? request.getInquiryCommitteeMembers() : "Internal Complaints Committee (ICC)")
                .status("OPEN_INQUIRY")
                .build();

        log.warn("CONFIDENTIAL SAFETY INCIDENT LOGGED: {} [{}] involving {}", refCode, incident.getClassification(), incident.getInvolvedPartyName());
        return safetyRepository.save(incident);
    }

    @Transactional(readOnly = true)
    public List<ConfidentialSafetyIncident> getIncidents(String status, String classification) {
        String tenantId = resolveTenant();

        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            return safetyRepository.findAllByTenantIdAndStatusOrderByReportedAtDesc(tenantId, status.toUpperCase());
        }
        if (classification != null && !classification.isBlank() && !"ALL".equalsIgnoreCase(classification)) {
            return safetyRepository.findAllByTenantIdAndClassificationOrderByReportedAtDesc(tenantId, classification.toUpperCase());
        }
        return safetyRepository.findAllByTenantIdOrderByReportedAtDesc(tenantId);
    }

    @Transactional
    public ConfidentialSafetyIncident resolveIncident(UUID incidentId, String dispositionStatus, String closingRemarks) {
        String tenantId = resolveTenant();

        ConfidentialSafetyIncident incident = safetyRepository.findByIdAndTenantId(incidentId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Safety incident not found with ID: " + incidentId));

        incident.setStatus(dispositionStatus != null ? dispositionStatus.toUpperCase() : "CLOSED_RESOLVED");
        incident.setClosingRemarks(closingRemarks);
        incident.setClosedAt(LocalDateTime.now());

        log.info("Confidential safety incident {} closed with status {}", incident.getIncidentRefCode(), incident.getStatus());
        return safetyRepository.save(incident);
    }
}

