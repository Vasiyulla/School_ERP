package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_webhooks", schema = "system")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantWebhookSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String webhookName; // e.g. "Zapier MUN Sync", "RFID Library Gate Hook"

    @Column(nullable = false)
    private String targetUrl; // https://hooks.zapier.com/hooks/catch/...

    @Column(nullable = false)
    private String eventTopic; // CUSTOM_ENTITY_RECORD_CREATED, STUDENT_ENROLLED, FEE_PAID, ATTENDANCE_LOCKED

    private String secretKey; // HMAC signature secret

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private int totalDispatchesCount = 0;

    private String lastDispatchStatus; // SUCCESS_200, FAILED_TIMEOUT, etc.

    private LocalDateTime lastDispatchedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

