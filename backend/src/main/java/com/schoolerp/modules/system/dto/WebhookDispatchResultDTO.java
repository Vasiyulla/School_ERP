package com.schoolerp.modules.system.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookDispatchResultDTO {
    private UUID webhookId;
    private String webhookName;
    private String targetUrl;
    private String eventTopic;
    private String deliveryStatus; // SUCCESS_200, SIMULATED_DELIVERY
    private int httpStatusCode;
    private String hmacSignature;
    private LocalDateTime dispatchedAt;
}

