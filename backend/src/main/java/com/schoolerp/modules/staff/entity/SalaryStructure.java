package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "salary_structures", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID employeeId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal basicPay;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal hra; // 40% of basic pay

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal specialAllowance;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal grossSalary;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate effectiveFrom = LocalDate.now();

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, REVISED
}

