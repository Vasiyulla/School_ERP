package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payroll_records", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String payslipNumber; // e.g. PAY-2026-11-0010

    @Column(nullable = false)
    private UUID employeeId;

    @Column(nullable = false)
    private String employeeCode;

    @Column(nullable = false)
    private String employeeName;

    private String department;

    private String designation;

    private String bankAccountNumber;

    private String panNumber;

    @Column(nullable = false)
    private String payrollMonth; // e.g. 2026-11

    // Earnings
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal basicPay;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal hra;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal specialAllowance;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal grossEarnings;

    // Statutory Deductions (Indian Labor Law / CBSE compliance)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal providentFund; // 12% of basic

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal employeeStateInsurance; // 0.75% of gross

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal professionalTax; // Fixed ₹200.00 / month

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal incomeTaxTds;

    @Builder.Default
    private int lossOfPayDays = 0;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal lossOfPayDeduction = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalDeductions;

    // Net Take-Home Salary
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal netSalary;

    @Column(nullable = false)
    @Builder.Default
    private String paymentStatus = "GENERATED"; // GENERATED, APPROVED, DISBURSED

    private Instant disbursedAt;

    @Builder.Default
    private Instant createdAt = Instant.now();
}

