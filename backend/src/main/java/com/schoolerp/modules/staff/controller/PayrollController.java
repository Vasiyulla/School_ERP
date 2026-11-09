package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.entity.BiometricPunch;
import com.schoolerp.modules.staff.entity.PayrollRecord;
import com.schoolerp.modules.staff.entity.SalaryStructure;
import com.schoolerp.modules.staff.service.PayrollService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/staff/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping("/biometric/punch")
    public ResponseEntity<ApiResponse<BiometricPunch>> recordPunch(@RequestBody PunchRequest req) {
        BiometricPunch punch = payrollService.recordPunch(
                req.getEmployeeId(),
                req.getPunchType(),
                req.getVerificationMode(),
                req.getDeviceIp()
        );
        return ResponseEntity.ok(ApiResponse.ok("Biometric punch recorded", punch));
    }

    @GetMapping("/biometric/punches")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('HR_MANAGER')")
    public ResponseEntity<ApiResponse<List<BiometricPunch>>> getPunches() {
        return ResponseEntity.ok(ApiResponse.ok(payrollService.getPunches()));
    }

    @PostMapping("/structure")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('HR_MANAGER')")
    public ResponseEntity<ApiResponse<SalaryStructure>> configureStructure(@RequestBody StructureRequest req) {
        SalaryStructure structure = payrollService.configureSalaryStructure(
                req.getEmployeeId(),
                req.getBasicPay(),
                req.getSpecialAllowance()
        );
        return ResponseEntity.ok(ApiResponse.ok("Salary structure configured", structure));
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT') or hasRole('HR_MANAGER')")
    public ResponseEntity<ApiResponse<List<PayrollRecord>>> generateMonthlyPayroll(
            @RequestParam(defaultValue = "2026-11") String payrollMonth) {
        List<PayrollRecord> records = payrollService.generateMonthlyPayroll(payrollMonth);
        return ResponseEntity.ok(ApiResponse.ok("Monthly statutory payroll generated and ledger journals posted", records));
    }

    @GetMapping("/records")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT') or hasRole('HR_MANAGER') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<List<PayrollRecord>>> getPayrollRecords(
            @RequestParam(defaultValue = "2026-11") String payrollMonth) {
        return ResponseEntity.ok(ApiResponse.ok(payrollService.getPayrollRecords(payrollMonth)));
    }

    @GetMapping("/{id}/payslip")
    public ResponseEntity<ApiResponse<PayrollRecord>> getPayslip(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(payrollService.getPayslip(id)));
    }

    @Data
    public static class PunchRequest {
        private UUID employeeId;
        private String punchType; // IN, OUT
        private String verificationMode;
        private String deviceIp;
    }

    @Data
    public static class StructureRequest {
        private UUID employeeId;
        private BigDecimal basicPay;
        private BigDecimal specialAllowance;
    }
}

