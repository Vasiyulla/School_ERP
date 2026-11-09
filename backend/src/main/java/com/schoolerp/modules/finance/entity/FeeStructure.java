package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "fee_structures", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String structureName; // e.g. Class 10 Standard Annual Fee 2026-27

    @Column(nullable = false)
    private String gradeLevel; // e.g. Class 10, Class 6

    @Column(nullable = false)
    private String academicYear; // e.g. 2026-27

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, ARCHIVED

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fee_structure_id")
    @Builder.Default
    private List<FeeStructureItem> items = new ArrayList<>();

    @Builder.Default
    private Instant createdAt = Instant.now();
}

