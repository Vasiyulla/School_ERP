package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.dto.ApplySalaryLoanRequest;
import com.schoolerp.modules.staff.entity.StaffSalaryLoan;
import com.schoolerp.modules.staff.service.StaffSalaryLoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff/loans")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Faculty Salary Advance & Festival Loans", description = "Disbursement of interest-free festival/emergency loans, repayment ledgers, and automated monthly payroll EMI recovery")
public class StaffSalaryLoanController {

    private final StaffSalaryLoanService loanService;

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Disburse staff salary advance or festival loan with automated monthly payroll EMI recovery")
    public ResponseEntity<ApiResponse<StaffSalaryLoan>> applyLoan(
            @Valid @RequestBody ApplySalaryLoanRequest request) {
        String actor = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "Principal Dr. Rajesh Sharma";
        StaffSalaryLoan loan = loanService.disburseLoan(request, actor);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Salary loan disbursed and EMI recovery schedule activated", loan));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get all active staff salary advances and repayment balances")
    public ResponseEntity<ApiResponse<List<StaffSalaryLoan>>> getActiveLoans() {
        return ResponseEntity.ok(ApiResponse.ok(loanService.getActiveLoans()));
    }
}

