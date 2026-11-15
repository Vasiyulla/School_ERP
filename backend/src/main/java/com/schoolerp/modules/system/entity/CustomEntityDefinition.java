package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "custom_entities", schema = "system")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomEntityDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String entityCode; // e.g. "MUN_DELEGATION", "EQUESTRIAN_HORSE", "ROBOTICS_PARTS"

    @Column(nullable = false)
    private String displayName; // "Model UN Country Delegation", "Equestrian Horse Registry"

    @Column(nullable = false)
    private String pluralName; // "MUN Delegations", "Horses"

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category; // ACADEMICS, OPERATIONS, ACTIVITIES, SPECIALTY

    private String iconName; // e.g. "Flag", "Activity", "Cpu"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String fieldsSchemaJson; // JSON array of field definitions with names, types, labels, required flags

    @Builder.Default
    private boolean isPublished = true;

    @Builder.Default
    private int recordCount = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

