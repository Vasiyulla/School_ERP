package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hostel_rooms", schema = "operations",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_block_room", columnNames = {"tenantId", "blockName", "roomNumber"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String blockName; // e.g. "Aryabhata Senior Boys Hostel", "Gargi Girls Hostel"

    @Column(nullable = false)
    private int floorNumber; // 1, 2, 3

    @Column(nullable = false)
    private String roomNumber; // e.g. "B-204"

    @Column(nullable = false)
    private String roomType; // AC, NON_AC

    @Builder.Default
    private int totalBeds = 4;

    @Builder.Default
    private int occupiedBeds = 0;

    @Builder.Default
    private BigDecimal annualFee = BigDecimal.valueOf(80000.00);

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

