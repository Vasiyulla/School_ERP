package com.schoolerp.modules.staff.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.entity.FinancialLedgerEntry;
import com.schoolerp.modules.finance.repository.FinancialLedgerEntryRepository;
import com.schoolerp.modules.staff.entity.BiometricPunch;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.entity.PayrollRecord;
import com.schoolerp.modules.staff.entity.SalaryStructure;
import com.schoolerp.modules.staff.repository.BiometricPunchRepository;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.staff.repository.PayrollRecordRepository;
import com.schoolerp.modules.staff.repository.SalaryStructureRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayrollService {

    private final EmployeeRepository employeeRepository;
    private final SalaryStructureRepository salaryStructureRepository;
    private final PayrollRecordRepository payrollRecordRepository;
    private final BiometricPunchRepository biometricPunchRepository;
    private final FinancialLedgerEntryRepository ledgerRepository;
    private final AuditLogService auditLogService;

    // --- 1. Biometric Punch Ingestion ---

    @Transactional
    public BiometricPunch recordPunch(UUID employeeId, String punchType, String verificationMode, String deviceIp) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        Employee emp = employeeRepository.findById(employeeId)
                .filter(e -> e.getTenantId().equals(activeTenantId))
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        Instant now = Instant.now();
        LocalTime time = now.atZone(ZoneId.systemDefault()).toLocalTime();

        // School shift rule: 08:00 AM cutoff for teachers
        String punchStatus = "ON_TIME";
        if ("IN".equalsIgnoreCase(punchType)) {
            if (time.isAfter(LocalTime.of(8, 15))) {
                punchStatus = "LATE_MARK";
            }
        } else if ("OUT".equalsIgnoreCase(punchType)) {
            if (time.isBefore(LocalTime.of(15, 30))) {
                punchStatus = "EARLY_DEPARTURE";
            }
        }

        BiometricPunch punch = BiometricPunch.builder()
                .tenantId(activeTenantId)
                .employeeId(emp.getId())
                .employeeCode(emp.getEmployeeCode())
                .employeeName(emp.getFullName())
                .punchTime(now)
                .punchType(punchType.toUpperCase())
                .verificationMode(verificationMode != null ? verificationMode : "FACIAL_RECOGNITION")
                .deviceIp(deviceIp != null ? deviceIp : "10.0.8.21")
                .punchStatus(punchStatus)
                .build();

        BiometricPunch saved = biometricPunchRepository.save(punch);
        auditLogService.recordEvent(
                "BIOMETRIC_PUNCH",
                "staff.biometric_punches",
                saved.getId().toString(),
                String.format("Biometric %s punch for %s (%s) status: %s",
                        punchType, emp.getFullName(), emp.getEmployeeCode(), punchStatus)
        );
        return saved;
    }

    public List<BiometricPunch> getPunches() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return biometricPunchRepository.findByTenantIdOrderByPunchTimeDesc(tenantId);
    }

    // --- 2. Salary Structure Setup ---

    @Transactional
    public SalaryStructure configureSalaryStructure(UUID employeeId, BigDecimal basicPay, BigDecimal specialAllowance) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        Employee emp = employeeRepository.findById(employeeId)
                .filter(e -> e.getTenantId().equals(activeTenantId))
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        // HRA standard 40% of basic pay
        BigDecimal hra = basicPay.multiply(BigDecimal.valueOf(0.40)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal gross = basicPay.add(hra).add(specialAllowance);

        SalaryStructure structure = salaryStructureRepository
                .findByTenantIdAndEmployeeIdAndStatus(activeTenantId, employeeId, "ACTIVE")
                .orElseGet(() -> SalaryStructure.builder()
                        .tenantId(activeTenantId)
                        .employeeId(employeeId)
                        .build());

        structure.setBasicPay(basicPay);
        structure.setHra(hra);
        structure.setSpecialAllowance(specialAllowance);
        structure.setGrossSalary(gross);
        structure.setEffectiveFrom(LocalDate.now());
        structure.setStatus("ACTIVE");

        return salaryStructureRepository.save(structure);
    }

    // --- 3. Monthly Payroll Generation Engine with Statutory Deductions ---

    @Transactional
    public List<PayrollRecord> generateMonthlyPayroll(String payrollMonth) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<Employee> activeEmployees = employeeRepository.findAllByTenantId(tenantId).stream()
                .filter(e -> "ACTIVE".equalsIgnoreCase(e.getStatus()))
                .toList();

        List<PayrollRecord> generatedRecords = new ArrayList<>();
        BigDecimal totalPayrollDisbursed = BigDecimal.ZERO;
        BigDecimal totalStatutoryDeductions = BigDecimal.ZERO;

        int slipSeq = 1;
        for (Employee emp : activeEmployees) {
            SalaryStructure structure = salaryStructureRepository
                    .findByTenantIdAndEmployeeIdAndStatus(tenantId, emp.getId(), "ACTIVE")
                    .orElseGet(() -> {
                        // Fallback default salary band for teaching faculty (Basic ₹45,000)
                        BigDecimal defaultBasic = new BigDecimal("45000.00");
                        BigDecimal defaultHra = defaultBasic.multiply(BigDecimal.valueOf(0.40));
                        BigDecimal defaultAllowance = new BigDecimal("15000.00");
                        return SalaryStructure.builder()
                                .tenantId(tenantId)
                                .employeeId(emp.getId())
                                .basicPay(defaultBasic)
                                .hra(defaultHra)
                                .specialAllowance(defaultAllowance)
                                .grossSalary(defaultBasic.add(defaultHra).add(defaultAllowance))
                                .effectiveFrom(LocalDate.now())
                                .status("ACTIVE")
                                .build();
                    });

            BigDecimal basic = structure.getBasicPay();
            BigDecimal hra = structure.getHra();
            BigDecimal allowance = structure.getSpecialAllowance();
            BigDecimal gross = basic.add(hra).add(allowance);

            // Statutory deductions (Indian Labor / EPFO regulations):
            // 1. Employee Provident Fund (PF): 12% of basic
            BigDecimal pf = basic.multiply(BigDecimal.valueOf(0.12)).setScale(2, RoundingMode.HALF_UP);

            // 2. Employee State Insurance (ESI): 0.75% of gross
            BigDecimal esi = gross.multiply(BigDecimal.valueOf(0.0075)).setScale(2, RoundingMode.HALF_UP);

            // 3. Professional Tax (PT): Statutory ₹200.00 per month
            BigDecimal pt = new BigDecimal("200.00");

            // 4. Income Tax TDS: 5% flat withholding
            BigDecimal tds = gross.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);

            // 5. Loss of pay (LOP)
            int lopDays = 0;
            BigDecimal lopDeduction = BigDecimal.ZERO;

            BigDecimal deductions = pf.add(esi).add(pt).add(tds).add(lopDeduction);
            BigDecimal net = gross.subtract(deductions);

            String payslipNumber = String.format("PAY-%s-%04d", payrollMonth.replace("-", ""), slipSeq++);

            PayrollRecord record = payrollRecordRepository
                    .findByTenantIdAndEmployeeIdAndPayrollMonth(tenantId, emp.getId(), payrollMonth)
                    .orElseGet(() -> PayrollRecord.builder()
                            .tenantId(tenantId)
                            .employeeId(emp.getId())
                            .payrollMonth(payrollMonth)
                            .build());

            record.setPayslipNumber(payslipNumber);
            record.setEmployeeCode(emp.getEmployeeCode());
            record.setEmployeeName(emp.getFullName());
            record.setDepartment(emp.getDepartment());
            record.setDesignation(emp.getDesignation());
            record.setBankAccountNumber(emp.getBankAccountNumber() != null ? emp.getBankAccountNumber() : "HDFC0001029384");
            record.setPanNumber(emp.getPanNumber() != null ? emp.getPanNumber() : "AAEPA9921K");
            record.setBasicPay(basic);
            record.setHra(hra);
            record.setSpecialAllowance(allowance);
            record.setGrossEarnings(gross);
            record.setProvidentFund(pf);
            record.setEmployeeStateInsurance(esi);
            record.setProfessionalTax(pt);
            record.setIncomeTaxTds(tds);
            record.setLossOfPayDays(lopDays);
            record.setLossOfPayDeduction(lopDeduction);
            record.setTotalDeductions(deductions);
            record.setNetSalary(net);
            record.setPaymentStatus("DISBURSED");
            record.setDisbursedAt(Instant.now());

            PayrollRecord savedRecord = payrollRecordRepository.save(record);
            generatedRecords.add(savedRecord);

            totalPayrollDisbursed = totalPayrollDisbursed.add(net);
            totalStatutoryDeductions = totalStatutoryDeductions.add(deductions);
        }

        // Immutable Double-Entry Ledger Postings for Monthly Staff Payroll
        // Debit: 5010-STAFF-SALARIES (Expense)
        ledgerRepository.save(FinancialLedgerEntry.builder()
                .tenantId(tenantId)
                .entryType("PAYROLL_EXPENSE")
                .accountCode("5010-STAFF-SALARIES")
                .debit(totalPayrollDisbursed.add(totalStatutoryDeductions))
                .credit(BigDecimal.ZERO)
                .description(String.format("Monthly faculty and staff payroll expense for %s (%d employees)", payrollMonth, generatedRecords.size()))
                .referenceId("PAYROLL-" + payrollMonth)
                .postedAt(Instant.now())
                .build());

        // Credit: 1020-BANK-HDFC (Asset deduction for net disbursements)
        ledgerRepository.save(FinancialLedgerEntry.builder()
                .tenantId(tenantId)
                .entryType("PAYROLL_DISBURSEMENT")
                .accountCode("1020-BANK-HDFC")
                .debit(BigDecimal.ZERO)
                .credit(totalPayrollDisbursed)
                .description(String.format("Net salary disbursements via NEFT for %s", payrollMonth))
                .referenceId("PAYROLL-" + payrollMonth)
                .postedAt(Instant.now())
                .build());

        // Credit: 2030-STATUTORY-PAYABLES (Liability held for PF, ESI, TDS remittance)
        ledgerRepository.save(FinancialLedgerEntry.builder()
                .tenantId(tenantId)
                .entryType("STATUTORY_WITHHOLDING")
                .accountCode("2030-STATUTORY-PAYABLES")
                .debit(BigDecimal.ZERO)
                .credit(totalStatutoryDeductions)
                .description(String.format("Statutory withholdings (PF, ESI, PT, TDS) for %s", payrollMonth))
                .referenceId("PAYROLL-" + payrollMonth)
                .postedAt(Instant.now())
                .build());

        auditLogService.recordEvent(
                "GENERATE_PAYROLL",
                "staff.payroll",
                "PAYROLL-" + payrollMonth,
                String.format("Generated payroll for %s: Total Gross INR %s, Net INR %s across %d employees. Ledger journal posted.",
                        payrollMonth, totalPayrollDisbursed.add(totalStatutoryDeductions), totalPayrollDisbursed, generatedRecords.size())
        );

        return generatedRecords;
    }

    public List<PayrollRecord> getPayrollRecords(String payrollMonth) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return payrollRecordRepository.findByTenantIdAndPayrollMonthOrderByEmployeeNameAsc(tenantId, payrollMonth);
    }

    public PayrollRecord getPayslip(UUID payrollId) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return payrollRecordRepository.findById(payrollId)
                .filter(p -> p.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Payslip not found: " + payrollId));
    }
}
