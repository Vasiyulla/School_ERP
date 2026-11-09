package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "fee_structure_items", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeStructureItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String categoryName; // TUITION, SCIENCE_LAB, COMPUTER_LAB, TRANSPORT, SPORTS_LIBRARY

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Builder.Default
    private boolean isOptional = false; // e.g. Transport is optional, Tuition is mandatory

    @Builder.Default
    private String frequency = "TERMLY"; // ANNUAL, TERMLY, MONTHLY
}

