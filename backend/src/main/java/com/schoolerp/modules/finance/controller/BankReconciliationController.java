package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.dto.BankReconciliationResultDTO;
import com.schoolerp.modules.finance.dto.ReconcileBankStatementRequest;
import com.schoolerp.modules.finance.entity.BankStatementEntry;
import com.schoolerp.modules.finance.service.BankReconciliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/bank-reconciliation")
@RequiredArgsConstructor
public class BankReconciliationController {

    private final BankReconciliationService reconciliationService;

    @PostMapping("/process-statement")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<BankReconciliationResultDTO>> processStatement(
            @RequestBody ReconcileBankStatementRequest request) {
        BankReconciliationResultDTO result = reconciliationService.processStatement(request);
        return ResponseEntity.ok(ApiResponse.ok("Bank statement processed and auto-reconciled successfully", result));
    }

    @GetMapping("/suspense-pool")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<BankStatementEntry>>> getSuspensePool() {
        List<BankStatementEntry> suspense = reconciliationService.getSuspensePool();
        return ResponseEntity.ok(ApiResponse.ok("Unallocated bank deposits suspense pool retrieved successfully", suspense));
    }
}
