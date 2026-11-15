package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hostel_allocations", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID roomId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private int bedNumber;

    @Column(nullable = false)
    private String academicYear; // "2026-2027"

    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, VACATED

    @Builder.Default
    private LocalDateTime allocatedAt = LocalDateTime.now();

    private LocalDateTime vacatedAt;
}

