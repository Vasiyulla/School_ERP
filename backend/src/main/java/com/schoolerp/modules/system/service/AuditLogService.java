package com.schoolerp.modules.system.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.system.entity.AuditLog;
import com.schoolerp.modules.system.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional(readOnly = true)
    public List<AuditLog> getLogs() {
        return auditLogRepository.findAllByTenantIdOrderByTimestampDesc(TenantContext.getTenantId());
    }

    public void recordEvent(String action, String resourceType, String resourceId, String details) {
        String actor = "System";
        String role = "SYSTEM";
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            actor = SecurityContextHolder.getContext().getAuthentication().getName();
            role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        }
        recordEvent(actor, role, action, resourceType, resourceId, details, "127.0.0.1", "SUCCESS");
    }

    public void logAction(String actor, String action, String resourceType, String resourceId, String details) {
        recordEvent(actor, "USER", action, resourceType, resourceId, details, "127.0.0.1", "SUCCESS");
    }

    @Async
    @Transactional
    public void recordEvent(String actorName, String actorRole, String action,
                            String resourceType, String resourceId, String details,
                            String clientIp, String status) {
        AuditLog log = AuditLog.builder()
                .tenantId(TenantContext.getTenantId())
                .actorName(actorName)
                .actorRole(actorRole)
                .action(action)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .details(details)
                .clientIp(clientIp)
                .status(status)
                .timestamp(Instant.now())
                .build();
        auditLogRepository.save(log);
    }
}

