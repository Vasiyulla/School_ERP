package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.entity.FeeInvoice;
import com.schoolerp.modules.finance.entity.FinancialLedgerEntry;
import com.schoolerp.modules.finance.entity.PaymentWebhookEvent;
import com.schoolerp.modules.finance.service.FinanceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/invoices")
    public ResponseEntity<ApiResponse<List<FeeInvoice>>> getInvoices() {
        List<FeeInvoice> invoices = financeService.getInvoices();
        return ResponseEntity.ok(ApiResponse.ok(invoices));
    }

    @GetMapping("/ledger")
    public ResponseEntity<ApiResponse<List<FinancialLedgerEntry>>> getLedger() {
        List<FinancialLedgerEntry> entries = financeService.getLedgerEntries();
        return ResponseEntity.ok(ApiResponse.ok(entries));
    }

    @GetMapping("/webhooks")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<PaymentWebhookEvent>>> getWebhookEvents() {
        return ResponseEntity.ok(ApiResponse.ok(financeService.getWebhookEvents()));
    }

    @PostMapping("/webhooks/{gateway}")
    public ResponseEntity<ApiResponse<FinanceService.PaymentWebhookResult>> handleWebhook(
            @PathVariable String gateway,
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String razorpaySig,
            @RequestHeader(value = "Stripe-Signature", required = false) String stripeSig,
            @RequestBody WebhookPayload payload) {

        String signature = razorpaySig != null ? razorpaySig : (stripeSig != null ? stripeSig : "TEST_HMAC_SIG");
        FinanceService.PaymentWebhookResult result = financeService.processWebhook(
                gateway,
                payload.getEventId(),
                payload.getEventType(),
                signature,
                payload.getRawJson(),
                payload.getInvoiceNumber(),
                payload.getAmount()
        );

        return ResponseEntity.ok(ApiResponse.ok("Webhook processed", result));
    }

    @PostMapping("/invoices/{invoiceId}/collect")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<FeeInvoice>> collectPayment(
            @PathVariable UUID invoiceId,
            @RequestBody PaymentRequest req) {
        FeeInvoice invoice = financeService.collectPayment(invoiceId, req.getAmount(), req.getPaymentMethod());
        return ResponseEntity.ok(ApiResponse.ok("Payment collected and double-entry ledger posted", invoice));
    }

    @Data
    public static class PaymentRequest {
        private BigDecimal amount;
        private String paymentMethod; // CASH, POS, CHEQUE, ONLINE
    }

    @Data
    public static class WebhookPayload {
        private String eventId;
        private String eventType;
        private String invoiceNumber;
        private BigDecimal amount;
        private String rawJson;
    }
}

