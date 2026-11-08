package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_webhook_events", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentWebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String gatewayEventId; // e.g. evt_1O0abcXYZ / order_99421_captured

    @Column(nullable = false)
    private String gatewayName; // RAZORPAY, STRIPE, PAYU

    @Column(nullable = false)
    private String eventType; // payment.captured, charge.succeeded, payment.failed

    @Column(nullable = false)
    private String payloadSignature; // HMAC-SHA256 signature from webhook header

    @Column(length = 4000)
    private String rawPayloadJson;

    @Builder.Default
    private String processingStatus = "PROCESSED"; // PROCESSED, DUPLICATE_SKIPPED, SIGNATURE_FAILED

    private String invoiceNumber;

    @Builder.Default
    private Instant receivedAt = Instant.now();
}

