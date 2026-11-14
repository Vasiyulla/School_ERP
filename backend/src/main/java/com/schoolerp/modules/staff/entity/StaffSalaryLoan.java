package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "staff_salary_loans", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffSalaryLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID employeeId;

    @Column(nullable = false)
    private String employeeCode;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private String loanType; // FESTIVAL_ADVANCE, MEDICAL_EMERGENCY, HOUSING_DEPOSIT, EDUCATION_ASSISTANCE

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal principalAmount; // e.g. INR 50000.00

    @Column(nullable = false)
    private int tenureMonths; // e.g. 10 months

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyEmi; // e.g. INR 5000.00

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal remainingBalance; // e.g. INR 50000.00

    @Column(nullable = false)
    @Builder.Default
    private int emisPaidCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE_RECOVERING"; // ACTIVE_RECOVERING, FULLY_SETTLED, WRITTEN_OFF

    @Column(nullable = false)
    private LocalDate disbursedDate;

    private String approvedBy;

    private String remarks;

    @Builder.Default
    private Instant createdAt = Instant.now();
}

