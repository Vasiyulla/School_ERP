package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "custom_entity_records", schema = "system")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomEntityRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID customEntityDefinitionId;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String entityCode;

    @Column(nullable = false)
    private String recordTitle; // Primary searchable title e.g. "USA - Security Council" or "Horse Thunder (Bay 3)"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String recordDataJson; // JSON string storing all key-values for the defined schema

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, ARCHIVED

    private String createdByName;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

