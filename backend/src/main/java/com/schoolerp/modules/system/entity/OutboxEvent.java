package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events", schema = "system")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String eventType; // WHATSAPP_DISPATCH, LATE_FEE_ASSESSMENT, EXPIRY_ALERT, TRUANCY_NOTICE

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false, length = 2000)
    private String payload;

    @Column(nullable = false)
    @Builder.Default
    private String status = "PENDING"; // PENDING, DISPATCHED, DEAD_LETTER

    @Builder.Default
    private int retryCount = 0;

    private String lastError;

    @Builder.Default
    private Instant createdAt = Instant.now();

    private Instant dispatchedAt;
}

