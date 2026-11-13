package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lab_inventory", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String labName; // Physics Lab, Chemistry Lab, Biology Lab, Robotics STEM Lab

    @Column(nullable = false)
    private String itemName; // e.g. Borosilicate Burette 50ml, Compound Microscope 1000x

    private String specification;

    @Column(nullable = false)
    @Builder.Default
    private int quantityOnHand = 10;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal unitCost = BigDecimal.ZERO;

    @Builder.Default
    private int reorderThreshold = 3;

    @Builder.Default
    private boolean isHazardous = false;

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

