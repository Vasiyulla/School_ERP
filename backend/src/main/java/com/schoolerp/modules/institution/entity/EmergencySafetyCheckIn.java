package com.schoolerp.modules.institution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emergency_safety_checkins", schema = "institution")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencySafetyCheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userRole; // STUDENT, STAFF, PARENT

    @Column(nullable = false)
    private String safetyStatus; // SAFE_AT_HOME, EVACUATED_TO_SHELTER, NEED_URGENT_ASSISTANCE, MEDICAL_EMERGENCY

    private String currentLocation; // City, Sector, Shelter name

    private String emergencyContactPhone;

    @Column(columnDefinition = "TEXT")
    private String assistanceDetails; // Needs drinking water, power, medical kit, or evacuation support

    @Builder.Default
    private LocalDateTime reportedAt = LocalDateTime.now();
}

