package com.schoolerp.modules.communication.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.communication.entity.Circular;
import com.schoolerp.modules.communication.entity.CircularAcknowledgment;
import com.schoolerp.modules.communication.repository.CircularAcknowledgmentRepository;
import com.schoolerp.modules.communication.repository.CircularRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CircularService {

    private final CircularRepository circularRepository;
    private final CircularAcknowledgmentRepository acknowledgmentRepository;
    private final CommunicationService communicationService;
    private final AuditLogService auditLogService;

    @Transactional
    public Circular publishCircular(String title, String content, String targetAudience, String targetGradeLevel, String priority, String attachmentUrl, String publishedBy) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        Circular circular = Circular.builder()
                .tenantId(tenantId)
                .title(title)
                .content(content)
                .targetAudience(targetAudience != null ? targetAudience.toUpperCase() : "ALL")
                .targetGradeLevel(targetGradeLevel)
                .priority(priority != null ? priority.toUpperCase() : "NORMAL")
                .attachmentUrl(attachmentUrl)
                .publishedBy(publishedBy != null ? publishedBy : "Principal Office")
                .publishedDate(LocalDate.now())
                .status("PUBLISHED")
                .createdAt(Instant.now())
                .build();

        Circular saved = circularRepository.save(circular);

        // Broadcast notification alert
        String notifMsg = String.format("Notice: %s - %s", circular.getTitle(), circular.getContent().length() > 80 ? circular.getContent().substring(0, 80) + "..." : circular.getContent());
        communicationService.broadcastCircular(saved.getTitle(), notifMsg, saved.getTargetAudience());

        auditLogService.logAction(publishedBy != null ? publishedBy : "PRINCIPAL",
                "PUBLISH_CIRCULAR", "communication.circulars", saved.getId().toString(),
                "Published circular: '" + saved.getTitle() + "' to audience " + saved.getTargetAudience());

        return saved;
    }

    public List<Circular> getCirculars(String audience, String gradeLevel) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<Circular> all = circularRepository.findByTenantIdOrderByPublishedDateDesc(tenantId);
        if (audience == null || audience.isBlank() || "ALL".equalsIgnoreCase(audience)) {
            return all;
        }

        return all.stream()
                .filter(c -> "ALL".equalsIgnoreCase(c.getTargetAudience()) ||
                             audience.equalsIgnoreCase(c.getTargetAudience()) ||
                             (gradeLevel != null && gradeLevel.equalsIgnoreCase(c.getTargetGradeLevel())))
                .collect(Collectors.toList());
    }

    @Transactional
    public CircularAcknowledgment acknowledgeCircular(UUID circularId, String guardianEmail, String guardianName, UUID studentId, String studentName, String ipAddress, String remarks) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        Circular circular = circularRepository.findById(circularId)
                .filter(c -> c.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Circular not found: " + circularId));

        return acknowledgmentRepository.findByTenantIdAndCircularIdAndGuardianEmail(tenantId, circularId, guardianEmail)
                .orElseGet(() -> {
                    CircularAcknowledgment ack = CircularAcknowledgment.builder()
                            .tenantId(tenantId)
                            .circularId(circular.getId())
                            .guardianEmail(guardianEmail)
                            .guardianName(guardianName != null ? guardianName : "Guardian")
                            .studentId(studentId)
                            .studentName(studentName)
                            .acknowledgedAt(Instant.now())
                            .ipAddress(ipAddress != null ? ipAddress : "127.0.0.1")
                            .signatureRemarks(remarks != null ? remarks : "Read and acknowledged digitally via Parent Portal")
                            .build();

                    CircularAcknowledgment saved = acknowledgmentRepository.save(ack);
                    auditLogService.logAction(guardianEmail, "ACKNOWLEDGE_CIRCULAR", "communication.circular_acknowledgments", saved.getId().toString(),
                            "Parent " + guardianName + " digitally signed circular '" + circular.getTitle() + "'");
                    return saved;
                });
    }

    public List<CircularAcknowledgment> getAcknowledgments(UUID circularId) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return acknowledgmentRepository.findByTenantIdAndCircularId(tenantId, circularId);
    }
}

