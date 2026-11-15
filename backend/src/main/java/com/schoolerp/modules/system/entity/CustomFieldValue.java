package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "custom_field_values", schema = "system")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomFieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID fieldDefinitionId;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String targetEntityType; // STUDENT, STAFF, etc.

    @Column(nullable = false)
    private UUID targetEntityId; // The ID of the specific student/staff/etc.

    @Column(nullable = false)
    private String fieldCode;

    @Column(columnDefinition = "TEXT")
    private String fieldValue;

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

