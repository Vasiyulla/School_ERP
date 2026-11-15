package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "custom_field_definitions", schema = "system")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomFieldDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String targetEntityType; // STUDENT, STAFF, ADMISSION, INVOICE, BOOK, VEHICLE

    @Column(nullable = false)
    private String fieldCode; // e.g., "swimming_belt_level", "bus_landmark"

    @Column(nullable = false)
    private String fieldLabel; // "Swimming Belt Level", "Bus Pickup Landmark"

    @Column(nullable = false)
    private String fieldType; // TEXT, NUMBER, DROPDOWN, BOOLEAN, DATE, FILE_URL

    @Column(columnDefinition = "TEXT")
    private String optionsJson; // JSON array of dropdown choices, e.g. ["Beginner", "Intermediate", "Advanced"]

    private String defaultValue;

    private String validationRegex; // Optional regex for formatting e.g. "^[A-Z]{2}[0-9]{4}$"

    @Builder.Default
    private boolean isMandatory = false;

    @Builder.Default
    private int displayOrder = 1;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

