package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.entity.*;
import com.schoolerp.modules.finance.service.FinanceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
public class FinanceController {

    private final FinanceService financeService;

    // --- Invoices & Ledger ---

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

    // --- 1. Dynamic Fee Structures ---

    @GetMapping("/structures")
    public ResponseEntity<ApiResponse<List<FeeStructure>>> getFeeStructures() {
        return ResponseEntity.ok(ApiResponse.ok(financeService.getFeeStructures()));
    }

    @PostMapping("/structures")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<FeeStructure>> createFeeStructure(@RequestBody FeeStructure structure) {
        FeeStructure created = financeService.createFeeStructure(structure);
        return ResponseEntity.ok(ApiResponse.ok("Fee structure created successfully", created));
    }

    // --- 2. Sibling & Scholarship Concessions ---

    @GetMapping("/concessions")
    public ResponseEntity<ApiResponse<List<FeeConcession>>> getConcessions() {
        return ResponseEntity.ok(ApiResponse.ok(financeService.getConcessions()));
    }

    @PostMapping("/concessions")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<FeeConcession>> applyConcession(@RequestBody FeeConcession concession) {
        FeeConcession applied = financeService.applyConcession(concession);
        return ResponseEntity.ok(ApiResponse.ok("Concession applied successfully", applied));
    }

    @GetMapping("/concessions/calculate")
    public ResponseEntity<ApiResponse<FinanceService.ConcessionCalculationResult>> calculateNetFee(
            @RequestParam UUID studentId,
            @RequestParam BigDecimal grossAmount) {
        var result = financeService.calculateNetFee(studentId, grossAmount);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    // --- 3. Cheque / DD Clearing Lifecycle ---

    @GetMapping("/cheques")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<ChequeRecord>>> getCheques() {
        return ResponseEntity.ok(ApiResponse.ok(financeService.getChequeRecords()));
    }

    @PostMapping("/cheques")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChequeRecord>> receiveCheque(@RequestBody ChequeRecord cheque) {
        ChequeRecord received = financeService.receiveCheque(cheque);
        return ResponseEntity.ok(ApiResponse.ok("Cheque logged into clearance register", received));
    }

    @PostMapping("/cheques/{id}/status")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChequeRecord>> updateChequeStatus(
            @PathVariable UUID id,
            @RequestBody ChequeStatusUpdateDto dto) {
        ChequeRecord updated = financeService.updateChequeStatus(id, dto.getStatus(), dto.getRemarks());
        return ResponseEntity.ok(ApiResponse.ok("Cheque status updated to " + dto.getStatus(), updated));
    }

    // --- 4. Late Fee Surcharge Calculator ---

    @GetMapping("/invoices/{id}/surcharge")
    public ResponseEntity<ApiResponse<FinanceService.SurchargeCalculationResult>> calculateSurcharge(
            @PathVariable UUID id,
            @RequestParam(required = false) String asOfDate) {
        LocalDate date = asOfDate != null ? LocalDate.parse(asOfDate) : LocalDate.now();
        var result = financeService.calculateLateFeeSurcharge(id, date);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    // --- Webhook & Collection Endpoints ---

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
    public static class ChequeStatusUpdateDto {
        private String status; // DEPOSITED, CLEARED, BOUNCED
        private String remarks;
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

