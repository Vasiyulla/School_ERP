package com.schoolerp.modules.staff.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.entity.FinancialLedgerEntry;
import com.schoolerp.modules.finance.repository.FinancialLedgerEntryRepository;
import com.schoolerp.modules.staff.dto.ApplySalaryLoanRequest;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.entity.StaffSalaryLoan;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.staff.repository.StaffSalaryLoanRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffSalaryLoanService {

    private final StaffSalaryLoanRepository loanRepository;
    private final EmployeeRepository employeeRepository;
    private final FinancialLedgerEntryRepository ledgerRepository;
    private final AuditLogService auditLogService;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant_apex_mumbai_01";
    }

    @Transactional
    public StaffSalaryLoan disburseLoan(ApplySalaryLoanRequest request, String approver) {
        String tenantId = resolveTenant();
        Employee emp = employeeRepository.findById(request.getEmployeeId())
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + request.getEmployeeId()));

        BigDecimal principal = request.getPrincipalAmount();
        int tenure = Math.max(1, request.getTenureMonths());
        BigDecimal emi = principal.divide(BigDecimal.valueOf(tenure), 2, RoundingMode.HALF_UP);

        StaffSalaryLoan loan = StaffSalaryLoan.builder()
                .tenantId(tenantId)
                .employeeId(emp.getId())
                .employeeCode(emp.getEmployeeCode())
                .employeeName(emp.getFullName())
                .loanType(request.getLoanType())
                .principalAmount(principal)
                .tenureMonths(tenure)
                .monthlyEmi(emi)
                .remainingBalance(principal)
                .emisPaidCount(0)
                .status("ACTIVE_RECOVERING")
                .disbursedDate(LocalDate.now())
                .approvedBy(approver != null ? approver : "Principal Dr. Rajesh Sharma")
                .remarks(request.getRemarks())
                .build();

        StaffSalaryLoan saved = loanRepository.save(loan);

        // Double-entry accounting entry:
        // Debit: 1050-STAFF-LOANS-RECEIVABLE (Asset)
        ledgerRepository.save(FinancialLedgerEntry.builder()
                .tenantId(tenantId)
                .entryType("LOAN_DISBURSEMENT")
                .accountCode("1050-STAFF-LOANS-RECEIVABLE")
                .debit(principal)
                .credit(BigDecimal.ZERO)
                .description(String.format("Salary advance/loan disbursed to %s (%s) for %s", emp.getFullName(), emp.getEmployeeCode(), request.getLoanType()))
                .referenceId(saved.getId().toString())
                .postedAt(Instant.now())
                .build());

        // Credit: 1020-BANK-HDFC
        ledgerRepository.save(FinancialLedgerEntry.builder()
                .tenantId(tenantId)
                .entryType("BANK_DISBURSEMENT")
                .accountCode("1020-BANK-HDFC")
                .debit(BigDecimal.ZERO)
                .credit(principal)
                .description(String.format("NEFT payment for staff loan: %s (EMI: INR %s/mo across %d months)", emp.getFullName(), emi, tenure))
                .referenceId(saved.getId().toString())
                .postedAt(Instant.now())
                .build());

        auditLogService.recordEvent(
                "STAFF_LOAN_DISBURSED",
                "staff.salary_loans",
                saved.getId().toString(),
                String.format("Disbursed %s loan of INR %s to %s (%s). Monthly EMI: INR %s over %d months",
                        request.getLoanType(), principal, emp.getFullName(), emp.getEmployeeCode(), emi, tenure)
        );

        return saved;
    }

    public List<StaffSalaryLoan> getActiveLoans() {
        String tenantId = resolveTenant();
        return loanRepository.findByTenantIdAndStatus(tenantId, "ACTIVE_RECOVERING");
    }
}

