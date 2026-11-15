package com.schoolerp.modules.system.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantWebhookSubscriptionDTO {
    private UUID id;
    private String webhookName;
    private String targetUrl;
    private String eventTopic;
    private boolean isActive;
    private int totalDispatchesCount;
    private String lastDispatchStatus;
    private LocalDateTime lastDispatchedAt;
    private LocalDateTime createdAt;
}

