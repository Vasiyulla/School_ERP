package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "staff_tax_declarations", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffTaxDeclaration {

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
    private String panNumber;

    @Column(nullable = false)
    @Builder.Default
    private String financialYear = "2026-2027";

    @Column(nullable = false)
    @Builder.Default
    private String assessmentYear = "2027-2028";

    @Column(nullable = false)
    @Builder.Default
    private String taxRegime = "NEW_REGIME_115BAC"; // NEW_REGIME_115BAC or OLD_REGIME

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal standardDeduction = new BigDecimal("75000.00"); // 75k in new regime, 50k in old

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal section80cAmount = BigDecimal.ZERO; // Max 1.5L in Old Regime

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal section80dMediclaim = BigDecimal.ZERO; // Max 25k/50k in Old Regime

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal hraExemptionAmount = BigDecimal.ZERO; // Sec 10(13A)

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal projectedAnnualGross = new BigDecimal("960000.00");

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalTaxableIncome = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal projectedAnnualTax = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal monthlyTdsDeduction = BigDecimal.ZERO;

    @Builder.Default
    private Instant declaredAt = Instant.now();
}

