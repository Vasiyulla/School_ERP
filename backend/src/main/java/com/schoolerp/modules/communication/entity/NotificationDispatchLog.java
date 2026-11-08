package com.schoolerp.modules.communication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notification_dispatch_logs", schema = "communication")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDispatchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String recipientIdentifier; // Phone number or Email address

    private String recipientName;

    @Column(nullable = false)
    private String recipientType; // PARENT, STUDENT, TEACHER, STAFF

    @Column(nullable = false)
    private String channel; // WHATSAPP_BUSINESS, SMS_DLT, EMAIL_SES, IN_APP_PUSH

    @Column(nullable = false)
    private String triggerEvent; // ATTENDANCE_ABSENCE, FEE_DUE_REMINDER, BUS_DELAY, CIRCULAR_ALERT

    @Column(length = 2000, nullable = false)
    private String messageBody;

    private String providerMessageId; // e.g. wamid.HBgLMjE... / msg_99412

    @Builder.Default
    private String deliveryStatus = "DELIVERED"; // PENDING, SENT, DELIVERED, FAILED

    private String failureReason;

    @Builder.Default
    private Instant dispatchedAt = Instant.now();
}

