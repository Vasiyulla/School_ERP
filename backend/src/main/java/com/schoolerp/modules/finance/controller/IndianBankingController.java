package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.entity.CashierDayScroll;
import com.schoolerp.modules.finance.service.IndianBankingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finance")
@RequiredArgsConstructor
public class IndianBankingController {

    private final IndianBankingService bankingService;

    @GetMapping("/invoices/{id}/upi-qr")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ACCOUNTANT','PARENT')")
    @RequireFeature("finance.indian_banking")
    public ResponseEntity<ApiResponse<IndianBankingService.UpiQrPayloadDTO>> getInvoiceUpiQr(@PathVariable UUID id) {
        IndianBankingService.UpiQrPayloadDTO payload = bankingService.generateUpiDynamicQr(id);
        return ResponseEntity.ok(ApiResponse.success(payload, "NPCI Dynamic UPI Intent QR generated successfully"));
    }

    @PostMapping("/cashier/day-scroll/close")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ACCOUNTANT')")
    @RequireFeature("finance.indian_banking")
    public ResponseEntity<ApiResponse<CashierDayScroll>> closeDayScroll(@RequestBody CloseScrollRequest request) {
        CashierDayScroll scroll = bankingService.closeCashierDayScroll(
                request.getCashierName(),
                request.getCounterDate(),
                request.getCount500Notes(),
                request.getCount200Notes(),
                request.getCount100Notes(),
                request.getCount50Notes(),
                request.getCount20Notes(),
                request.getCount10Notes(),
                request.getTotalSystemReceipts(),
                request.getNotes()
        );
        return ResponseEntity.ok(ApiResponse.success(scroll, "Cashier counter day-end scroll closed & verified"));
    }

    @GetMapping("/cashier/day-scroll/history")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ACCOUNTANT')")
    @RequireFeature("finance.indian_banking")
    public ResponseEntity<ApiResponse<List<CashierDayScroll>>> getScrollHistory() {
        return ResponseEntity.ok(ApiResponse.success(bankingService.getCashierScrolls(), "Cashier day-end scroll history retrieved"));
    }

    @GetMapping("/cheques/pdc-vault")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ACCOUNTANT')")
    @RequireFeature("finance.indian_banking")
    public ResponseEntity<ApiResponse<IndianBankingService.PdcVaultSummaryDTO>> getPdcVault() {
        return ResponseEntity.ok(ApiResponse.success(bankingService.getPdcVaultStatus(), "Post-Dated Cheque (PDC) vault status retrieved"));
    }

    @Data
    public static class CloseScrollRequest {
        private String cashierName;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate counterDate;
        private int count500Notes;
        private int count200Notes;
        private int count100Notes;
        private int count50Notes;
        private int count20Notes;
        private int count10Notes;
        private BigDecimal totalSystemReceipts;
        private String notes;
    }
}

