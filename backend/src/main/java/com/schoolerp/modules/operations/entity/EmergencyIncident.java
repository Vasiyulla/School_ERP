package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emergency_incidents", schema = "operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "incident_type", nullable = false)
    private String incidentType; // FIRE_ALARM, SECURITY_LOCKDOWN, MEDICAL_EMERGENCY, WEATHER_HAZARD

    @Column(name = "severity", nullable = false)
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "reported_by", nullable = false)
    private String reportedBy;

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE_LOCKDOWN, CONTAINED, RESOLVED

    @Column(name = "containment_actions", columnDefinition = "TEXT")
    private String containmentActions;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

