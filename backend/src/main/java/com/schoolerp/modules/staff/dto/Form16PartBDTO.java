package com.schoolerp.modules.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Form16PartBDTO {

    private String schoolName;
    private String schoolTanNumber;
    private String schoolPanNumber;
    private String employeeName;
    private String employeePan;
    private String employeeCode;
    private String designation;
    private String financialYear; // 2026-2027
    private String assessmentYear; // 2027-2028
    private String chosenTaxRegime; // Section 115BAC (New) or Old
    private BigDecimal grossSalary;
    private BigDecimal allowancesExemptUs10; // HRA
    private BigDecimal standardDeduction; // ₹75,000 (New) / ₹50,000 (Old)
    private BigDecimal professionalTax; // ₹2,400
    private BigDecimal deductionsUnderChapterVIA; // 80C + 80D
    private BigDecimal totalTaxableIncome;
    private BigDecimal calculatedTaxOnIncome;
    private BigDecimal rebateUs87A;
    private BigDecimal healthAndEducationCess4Percent;
    private BigDecimal totalTaxPayableAnnual;
    private BigDecimal monthlyTdsWithheld;
    private LocalDate issuedDate;
    private String signatoryOfficer;
}

