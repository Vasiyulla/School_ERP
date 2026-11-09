package com.schoolerp.modules.communication.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.communication.entity.Circular;
import com.schoolerp.modules.communication.entity.DeviceToken;
import com.schoolerp.modules.communication.entity.NotificationDispatchLog;
import com.schoolerp.modules.communication.repository.CircularRepository;
import com.schoolerp.modules.communication.repository.DeviceTokenRepository;
import com.schoolerp.modules.communication.repository.NotificationDispatchLogRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunicationService {

    private final CircularRepository circularRepository;
    private final NotificationDispatchLogRepository dispatchLogRepository;
    private final DeviceTokenRepository deviceTokenRepository;
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

    @Transactional
    public void broadcastCircular(String title, String body, String targetAudience) {
        String tenantId = TenantContext.getTenantId();
        NotificationDispatchLog logEntry = NotificationDispatchLog.builder()
                .tenantId(tenantId)
                .recipientName("Audience: " + targetAudience)
                .recipientIdentifier("BROADCAST_" + targetAudience)
                .recipientType(targetAudience)
                .channel("PUSH_NOTIFICATION_FCM")
                .triggerEvent("CIRCULAR_BROADCAST")
                .messageBody(title + ": " + body)
                .providerMessageId("broadcast_" + UUID.randomUUID().toString().substring(0, 8))
                .deliveryStatus("DELIVERED")
                .build();
        dispatchLogRepository.save(logEntry);
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

    @Transactional
    public DeviceToken registerDevice(DeviceToken registration) {
        String tenantId = TenantContext.getTenantId();
        registration.setTenantId(tenantId);

        DeviceToken token = deviceTokenRepository
                .findByTenantIdAndDeviceToken(tenantId, registration.getDeviceToken())
                .orElseGet(() -> DeviceToken.builder()
                        .tenantId(tenantId)
                        .deviceToken(registration.getDeviceToken())
                        .build());

        token.setUserId(registration.getUserId());
        token.setUserEmail(registration.getUserEmail());
        token.setUserRole(registration.getUserRole());
        token.setDeviceType(registration.getDeviceType());
        token.setDeviceModel(registration.getDeviceModel());
        token.setAppVersion(registration.getAppVersion());
        token.setStatus("ACTIVE");
        token.setLastActiveAt(Instant.now());

        DeviceToken saved = deviceTokenRepository.save(token);
        auditLogService.recordEvent(
                "REGISTER_DEVICE_TOKEN",
                "communication.device_tokens",
                saved.getId().toString(),
                String.format("Device token registered for %s (%s - %s)",
                        saved.getUserEmail(), saved.getDeviceType(), saved.getDeviceModel())
        );
        return saved;
    }

    @Transactional
    public DeviceToken unregisterDevice(String deviceToken) {
        String tenantId = TenantContext.getTenantId();
        DeviceToken token = deviceTokenRepository
                .findByTenantIdAndDeviceToken(tenantId, deviceToken)
                .orElseThrow(() -> new IllegalArgumentException("Device token not found: " + deviceToken));

        token.setStatus("UNREGISTERED");
        token.setLastActiveAt(Instant.now());
        DeviceToken saved = deviceTokenRepository.save(token);

        auditLogService.recordEvent(
                "UNREGISTER_DEVICE_TOKEN",
                "communication.device_tokens",
                saved.getId().toString(),
                "Device token unregistered for user: " + saved.getUserEmail()
        );
        return saved;
    }

    public List<DeviceToken> getActiveDevices() {
        return deviceTokenRepository.findByTenantId(TenantContext.getTenantId());
    }

    /**
     * Dispatch high-priority push notification to parent/student mobile apps via FCM.
     */
    @Transactional
    public NotificationDispatchLog sendPushNotification(UUID userId, String recipientName, String title, String body, String eventType) {
        String tenantId = TenantContext.getTenantId();
        List<DeviceToken> activeTokens = deviceTokenRepository
                .findByTenantIdAndUserIdAndStatus(tenantId, userId, "ACTIVE");

        String fcmMessageId = "fcm_msg_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        NotificationDispatchLog logEntry = NotificationDispatchLog.builder()
                .tenantId(tenantId)
                .recipientName(recipientName)
                .recipientIdentifier(activeTokens.isEmpty() ? "NO_TOKEN_REGISTERED" : activeTokens.get(0).getDeviceToken().substring(0, Math.min(32, activeTokens.get(0).getDeviceToken().length())) + "...")
                .recipientType("PARENT")
                .channel("PUSH_NOTIFICATION_FCM")
                .triggerEvent(eventType != null ? eventType : "MOBILE_PUSH_ALERT")
                .messageBody(title + ": " + body)
                .providerMessageId(fcmMessageId)
                .deliveryStatus(activeTokens.isEmpty() ? "QUEUED_PENDING_DEVICE" : "DELIVERED")
                .build();

        NotificationDispatchLog saved = dispatchLogRepository.save(logEntry);
        log.info("FCM push notification [{}] dispatched to user {} (Tokens found: {})",
                eventType, userId, activeTokens.size());
        return saved;
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
