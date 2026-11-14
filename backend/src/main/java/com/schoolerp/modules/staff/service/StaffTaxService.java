package com.schoolerp.modules.staff.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.staff.dto.Form16PartBDTO;
import com.schoolerp.modules.staff.dto.SubmitTaxDeclarationRequest;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.entity.SalaryStructure;
import com.schoolerp.modules.staff.entity.StaffTaxDeclaration;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.staff.repository.SalaryStructureRepository;
import com.schoolerp.modules.staff.repository.StaffTaxDeclarationRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffTaxService {

    private final StaffTaxDeclarationRepository taxDeclarationRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryStructureRepository salaryStructureRepository;
    private final AuditLogService auditLogService;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant_apex_mumbai_01";
    }

    @Transactional
    public StaffTaxDeclaration submitDeclaration(SubmitTaxDeclarationRequest request) {
        String tenantId = resolveTenant();
        Employee emp = employeeRepository.findById(request.getEmployeeId())
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + request.getEmployeeId()));

        String fy = (request.getFinancialYear() != null && !request.getFinancialYear().isBlank())
                ? request.getFinancialYear() : "2026-2027";
        String regime = (request.getTaxRegime() != null && !request.getTaxRegime().isBlank())
                ? request.getTaxRegime().toUpperCase() : "NEW_REGIME_115BAC";

        // Estimate annual gross from salary structure
        BigDecimal monthlyGross = salaryStructureRepository
                .findByTenantIdAndEmployeeIdAndStatus(tenantId, emp.getId(), "ACTIVE")
                .map(SalaryStructure::getGrossSalary)
                .orElse(new BigDecimal("75000.00"));

        BigDecimal annualGross = monthlyGross.multiply(new BigDecimal("12.00")).setScale(2, RoundingMode.HALF_UP);

        BigDecimal stdDeduction = "NEW_REGIME_115BAC".equals(regime)
                ? new BigDecimal("75000.00")
                : new BigDecimal("50000.00");

        BigDecimal chapter6Deductions = BigDecimal.ZERO;
        BigDecimal hraExempt = BigDecimal.ZERO;

        if ("OLD_REGIME".equals(regime)) {
            BigDecimal sec80c = (request.getSection80cAmount() != null)
                    ? request.getSection80cAmount().min(new BigDecimal("150000.00"))
                    : BigDecimal.ZERO;
            BigDecimal sec80d = (request.getSection80dMediclaim() != null)
                    ? request.getSection80dMediclaim().min(new BigDecimal("25000.00"))
                    : BigDecimal.ZERO;
            chapter6Deductions = sec80c.add(sec80d);
            hraExempt = (request.getHraRentPaidAnnual() != null)
                    ? request.getHraRentPaidAnnual().multiply(new BigDecimal("0.40")).setScale(2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
        }

        BigDecimal ptAnnual = new BigDecimal("2400.00"); // Standard Professional Tax ₹200/mo
        BigDecimal totalDeductions = stdDeduction.add(chapter6Deductions).add(hraExempt).add(ptAnnual);
        BigDecimal taxableIncome = annualGross.subtract(totalDeductions).max(BigDecimal.ZERO);

        // Tax calculation according to regime
        BigDecimal taxPayable = BigDecimal.ZERO;
        if ("NEW_REGIME_115BAC".equals(regime)) {
            taxPayable = computeNewRegimeTax(taxableIncome);
        } else {
            taxPayable = computeOldRegimeTax(taxableIncome);
        }

        BigDecimal cess = taxPayable.multiply(new BigDecimal("0.04")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAnnualTax = taxPayable.add(cess);
        BigDecimal monthlyTds = totalAnnualTax.divide(new BigDecimal("12.00"), 2, RoundingMode.HALF_UP);

        StaffTaxDeclaration declaration = taxDeclarationRepository
                .findByTenantIdAndEmployeeIdAndFinancialYear(tenantId, emp.getId(), fy)
                .orElseGet(() -> StaffTaxDeclaration.builder()
                        .tenantId(tenantId)
                        .employeeId(emp.getId())
                        .build());

        declaration.setEmployeeCode(emp.getEmployeeCode());
        declaration.setEmployeeName(emp.getFullName());
        declaration.setPanNumber(emp.getPanNumber() != null ? emp.getPanNumber() : "AAEPA9921K");
        declaration.setFinancialYear(fy);
        declaration.setAssessmentYear("2027-2028");
        declaration.setTaxRegime(regime);
        declaration.setStandardDeduction(stdDeduction);
        declaration.setSection80cAmount(request.getSection80cAmount() != null ? request.getSection80cAmount() : BigDecimal.ZERO);
        declaration.setSection80dMediclaim(request.getSection80dMediclaim() != null ? request.getSection80dMediclaim() : BigDecimal.ZERO);
        declaration.setHraExemptionAmount(hraExempt);
        declaration.setProjectedAnnualGross(annualGross);
        declaration.setTotalTaxableIncome(taxableIncome);
        declaration.setProjectedAnnualTax(totalAnnualTax);
        declaration.setMonthlyTdsDeduction(monthlyTds);

        StaffTaxDeclaration saved = taxDeclarationRepository.save(declaration);

        auditLogService.recordEvent(
                "STAFF_TAX_DECLARATION_SUBMITTED",
                "staff.tax_declarations",
                saved.getId().toString(),
                String.format("Tax declaration for %s (%s): Regime %s, Taxable INR %s, Monthly TDS INR %s",
                        emp.getFullName(), emp.getEmployeeCode(), regime, taxableIncome, monthlyTds)
        );

        return saved;
    }

    private BigDecimal computeNewRegimeTax(BigDecimal taxableIncome) {
        // Under Section 115BAC, if taxable income <= 7,00,000, 87A rebate makes tax = 0
        if (taxableIncome.compareTo(new BigDecimal("700000.00")) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal tax = BigDecimal.ZERO;
        double income = taxableIncome.doubleValue();

        // Slab 1: 0 - 3,00,000 -> 0%
        // Slab 2: 3,00,000 - 7,00,000 -> 5%
        if (income > 300000.0) {
            tax = tax.add(BigDecimal.valueOf((Math.min(income, 700000.0) - 300000.0) * 0.05));
        }
        // Slab 3: 7,00,000 - 10,00,000 -> 10%
        if (income > 700000.0) {
            tax = tax.add(BigDecimal.valueOf((Math.min(income, 1000000.0) - 700000.0) * 0.10));
        }
        // Slab 4: 10,00,000 - 12,00,000 -> 15%
        if (income > 1000000.0) {
            tax = tax.add(BigDecimal.valueOf((Math.min(income, 1200000.0) - 1000000.0) * 0.15));
        }
        // Slab 5: 12,00,000 - 15,00,000 -> 20%
        if (income > 1200000.0) {
            tax = tax.add(BigDecimal.valueOf((Math.min(income, 1500000.0) - 1200000.0) * 0.20));
        }
        // Slab 6: Above 15,00,000 -> 30%
        if (income > 1500000.0) {
            tax = tax.add(BigDecimal.valueOf((income - 1500000.0) * 0.30));
        }

        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal computeOldRegimeTax(BigDecimal taxableIncome) {
        if (taxableIncome.compareTo(new BigDecimal("500000.00")) <= 0) {
            return BigDecimal.ZERO; // Section 87A rebate
        }

        BigDecimal tax = BigDecimal.ZERO;
        double income = taxableIncome.doubleValue();

        if (income > 250000.0) {
            tax = tax.add(BigDecimal.valueOf((Math.min(income, 500000.0) - 250000.0) * 0.05));
        }
        if (income > 500000.0) {
            tax = tax.add(BigDecimal.valueOf((Math.min(income, 1000000.0) - 500000.0) * 0.20));
        }
        if (income > 1000000.0) {
            tax = tax.add(BigDecimal.valueOf((income - 1000000.0) * 0.30));
        }

        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public Form16PartBDTO generateForm16PartB(UUID employeeId) {
        String tenantId = resolveTenant();
        Employee emp = employeeRepository.findById(employeeId)
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        StaffTaxDeclaration dec = taxDeclarationRepository
                .findByTenantIdAndEmployeeIdAndFinancialYear(tenantId, emp.getId(), "2026-2027")
                .orElseGet(() -> submitDeclaration(SubmitTaxDeclarationRequest.builder()
                        .employeeId(emp.getId())
                        .taxRegime("NEW_REGIME_115BAC")
                        .financialYear("2026-2027")
                        .build()));

        BigDecimal cess = dec.getProjectedAnnualTax().multiply(new BigDecimal("0.04")).setScale(2, RoundingMode.HALF_UP);

        return Form16PartBDTO.builder()
                .schoolName("Apex Academy (Mumbai)")
                .schoolTanNumber("MUMA10294B")
                .schoolPanNumber("AAETA8819P")
                .employeeName(emp.getFullName())
                .employeePan(dec.getPanNumber())
                .employeeCode(emp.getEmployeeCode())
                .designation(emp.getDesignation())
                .financialYear(dec.getFinancialYear())
                .assessmentYear(dec.getAssessmentYear())
                .chosenTaxRegime(dec.getTaxRegime())
                .grossSalary(dec.getProjectedAnnualGross())
                .allowancesExemptUs10(dec.getHraExemptionAmount())
                .standardDeduction(dec.getStandardDeduction())
                .professionalTax(new BigDecimal("2400.00"))
                .deductionsUnderChapterVIA(dec.getSection80cAmount().add(dec.getSection80dMediclaim()))
                .totalTaxableIncome(dec.getTotalTaxableIncome())
                .calculatedTaxOnIncome(dec.getProjectedAnnualTax().subtract(cess).max(BigDecimal.ZERO))
                .rebateUs87A(dec.getTotalTaxableIncome().compareTo(new BigDecimal("700000.00")) <= 0 ? new BigDecimal("25000.00") : BigDecimal.ZERO)
                .healthAndEducationCess4Percent(cess)
                .totalTaxPayableAnnual(dec.getProjectedAnnualTax())
                .monthlyTdsWithheld(dec.getMonthlyTdsDeduction())
                .issuedDate(LocalDate.now())
                .signatoryOfficer("Bursar & Finance Director, Apex Academy")
                .build();
    }
}

