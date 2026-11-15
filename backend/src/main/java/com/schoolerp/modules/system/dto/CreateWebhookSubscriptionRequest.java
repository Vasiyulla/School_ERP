package com.schoolerp.modules.system.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWebhookSubscriptionRequest {
    private String webhookName;
    private String targetUrl;
    private String eventTopic; // CUSTOM_ENTITY_RECORD_CREATED, STUDENT_ENROLLED, FEE_PAID, ATTENDANCE_LOCKED
    private String secretKey;
}

