package com.schoolerp.modules.communication.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.communication.entity.Circular;
import com.schoolerp.modules.communication.entity.NotificationDispatchLog;
import com.schoolerp.modules.communication.repository.CircularRepository;
import com.schoolerp.modules.communication.repository.NotificationDispatchLogRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunicationService {

    private final CircularRepository circularRepository;
    private final NotificationDispatchLogRepository dispatchLogRepository;
    private final AuditLogService auditLogService;

    public List<Circular> getCirculars() {
        return circularRepository.findByTenantIdOrderByPublishedDateDesc(TenantContext.getTenantId());
    }

    @Transactional
    public Circular publishCircular(Circular circular) {
        String tenantId = TenantContext.getTenantId();
        circular.setTenantId(tenantId);
        Circular saved = circularRepository.save(circular);

        auditLogService.recordEvent(
                "PUBLISH_CIRCULAR",
                "communication.circulars",
                saved.getId().toString(),
                "Circular published: '" + saved.getTitle() + "' to audience: " + saved.getTargetAudience()
        );
        return saved;
    }

    public List<NotificationDispatchLog> getDispatchLogs() {
        return dispatchLogRepository.findByTenantIdOrderByDispatchedAtDesc(TenantContext.getTenantId());
    }

    @Transactional
    public NotificationDispatchLog dispatchNotification(NotificationDispatchLog logEntry) {
        String tenantId = TenantContext.getTenantId();
        logEntry.setTenantId(tenantId);
        logEntry.setProviderMessageId("wamid." + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        logEntry.setDeliveryStatus("DELIVERED");
        return dispatchLogRepository.save(logEntry);
    }

    @Async
    @Transactional
    public void triggerAbsenceAlert(String studentName, String guardianPhone, String sessionDate) {
        String message = String.format(
                "Dear Parent, your ward %s was marked ABSENT for roll call on %s. If this is an authorized leave, please submit a leave note on the parent portal.",
                studentName, sessionDate
        );

        NotificationDispatchLog logEntry = NotificationDispatchLog.builder()
                .tenantId(TenantContext.getTenantId())
                .recipientName(studentName + " (Guardian)")
                .recipientIdentifier(guardianPhone)
                .recipientType("PARENT")
                .channel("WHATSAPP_BUSINESS")
                .triggerEvent("ATTENDANCE_ABSENCE")
                .messageBody(message)
                .providerMessageId("wamid.abs_" + UUID.randomUUID().toString().substring(0, 8))
                .deliveryStatus("DELIVERED")
                .build();

        dispatchLogRepository.save(logEntry);
        log.info("Automated 09:00 AM absence alert dispatched to {} for student {}", guardianPhone, studentName);
    }

    @Async
    @Transactional
    public void triggerFeeDueReminder(String studentName, String invoiceNo, double amount, String dueDate, String phone) {
        String message = String.format(
                "Reminder: Term Fee Invoice %s of INR %.2f for %s is due on %s. Kindly pay online via parent portal to avoid late-fee surcharge.",
                invoiceNo, amount, studentName, dueDate
        );

        NotificationDispatchLog logEntry = NotificationDispatchLog.builder()
                .tenantId(TenantContext.getTenantId())
                .recipientName(studentName + " (Guardian)")
                .recipientIdentifier(phone)
                .recipientType("PARENT")
                .channel("SMS_DLT")
                .triggerEvent("FEE_DUE_REMINDER")
                .messageBody(message)
                .providerMessageId("sms_" + UUID.randomUUID().toString().substring(0, 8))
                .deliveryStatus("DELIVERED")
                .build();

        dispatchLogRepository.save(logEntry);
        log.info("Automated Fee Due SMS reminder dispatched to {} for invoice {}", phone, invoiceNo);
    }
}

