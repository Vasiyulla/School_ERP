package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.entity.StudentWallet;
import com.schoolerp.modules.finance.entity.WalletTransaction;
import com.schoolerp.modules.finance.service.StudentWalletService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finance/wallet")
@RequiredArgsConstructor
public class StudentWalletController {

    private final StudentWalletService walletService;

    @Data
    public static class TopupRequest {
        private UUID studentId;
        private BigDecimal amount;
        private String paymentMethod;
    }

    @Data
    public static class CanteenDebitRequest {
        private String rfidCardNumber;
        private BigDecimal amount;
        private String itemDescription;
        private String terminalId;
        private String cashierName;
    }

    @PostMapping("/topup")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ACCOUNTANT', 'PARENT')")
    public ResponseEntity<ApiResponse<StudentWallet>> topupWallet(@RequestBody TopupRequest req) {
        StudentWallet wallet = walletService.topupWallet(req.getStudentId(), req.getAmount(), req.getPaymentMethod());
        return ResponseEntity.ok(ApiResponse.ok("Student wallet topped up successfully", wallet));
    }

    @PostMapping("/transact")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ACCOUNTANT', 'STAFF')")
    public ResponseEntity<ApiResponse<WalletTransaction>> transactCanteen(@RequestBody CanteenDebitRequest req) {
        WalletTransaction tx = walletService.transactCanteen(
                req.getRfidCardNumber(),
                req.getAmount(),
                req.getItemDescription(),
                req.getTerminalId(),
                req.getCashierName()
        );
        return ResponseEntity.ok(ApiResponse.ok("Cafeteria POS debit successful", tx));
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ACCOUNTANT', 'PARENT', 'STUDENT')")
    public ResponseEntity<ApiResponse<StudentWallet>> getWallet(@PathVariable UUID studentId) {
        return ResponseEntity.ok(ApiResponse.ok(walletService.getWallet(studentId)));
    }

    @GetMapping("/{studentId}/transactions")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ACCOUNTANT', 'PARENT', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<WalletTransaction>>> getTransactions(@PathVariable UUID studentId) {
        return ResponseEntity.ok(ApiResponse.ok(walletService.getStudentTransactions(studentId)));
    }
}

