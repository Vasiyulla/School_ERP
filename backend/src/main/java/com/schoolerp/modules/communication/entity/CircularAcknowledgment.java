package com.schoolerp.modules.communication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "circular_acknowledgments", schema = "communication")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CircularAcknowledgment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID circularId;

    @Column(nullable = false)
    private String guardianEmail;

    @Column(nullable = false)
    private String guardianName;

    private UUID studentId;

    private String studentName;

    @Builder.Default
    private Instant acknowledgedAt = Instant.now();

    private String ipAddress;

    private String signatureRemarks;
}

