package com.schoolerp.modules.communication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "device_tokens", schema = "communication")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String userRole; // PARENT, TEACHER, ADMIN

    @Column(nullable = false, length = 512)
    private String deviceToken; // Firebase FCM / APNs Token

    @Column(nullable = false)
    private String deviceType; // ANDROID, IOS

    private String deviceModel; // e.g. Pixel 8 Pro, Samsung Galaxy S24

    private String appVersion; // e.g. 1.0.0-parent-native

    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, UNREGISTERED

    @Builder.Default
    private Instant lastActiveAt = Instant.now();

    @Builder.Default
    private Instant createdAt = Instant.now();
}
