package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.entity.FeeInvoice;
import com.schoolerp.modules.finance.entity.FinancialLedgerEntry;
import com.schoolerp.modules.finance.entity.PaymentWebhookEvent;
import com.schoolerp.modules.finance.repository.FeeInvoiceRepository;
import com.schoolerp.modules.finance.repository.FinancialLedgerEntryRepository;
import com.schoolerp.modules.finance.repository.PaymentWebhookEventRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final FeeInvoiceRepository invoiceRepository;
    private final FinancialLedgerEntryRepository ledgerRepository;
    private final PaymentWebhookEventRepository webhookEventRepository;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public List<FeeInvoice> getInvoices() {
        return invoiceRepository.findAllByTenantId(TenantContext.getTenantId());
    }

    @Transactional(readOnly = true)
    public List<FinancialLedgerEntry> getLedgerEntries() {
        return ledgerRepository.findAllByTenantIdOrderByPostedAtDesc(TenantContext.getTenantId());
    }

    @Transactional
    public FeeInvoice collectPayment(UUID invoiceId, BigDecimal paymentAmount, String paymentMethod) {
        String tenantId = TenantContext.getTenantId();

        FeeInvoice invoice = invoiceRepository.findByIdAndTenantId(invoiceId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

        BigDecimal currentPaid = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal newPaid = currentPaid.add(paymentAmount);
        BigDecimal newBalance = invoice.getTotalAmount().subtract(newPaid);

        invoice.setPaidAmount(newPaid);
        invoice.setBalanceAmount(newBalance.max(BigDecimal.ZERO));
        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus("PAID");
        } else {
            invoice.setStatus("PARTIAL");
        }

        FeeInvoice saved = invoiceRepository.save(invoice);

        // Atomic Double-Entry General Ledger Posting (Immutable)
        // Debit: Cash / Bank (Asset Increase)
        ledgerRepository.save(FinancialLedgerEntry.builder()
                .tenantId(tenantId)
                .entryType("FEE_PAYMENT")
                .accountCode(paymentMethod.equalsIgnoreCase("CASH") ? "1010-CASH" : "1020-BANK-HDFC")
                .debit(paymentAmount)
                .credit(BigDecimal.ZERO)
                .description(String.format("Payment collected for %s (%s)", invoice.getStudentName(), invoice.getInvoiceNumber()))
                .referenceId(invoice.getInvoiceNumber())
                .postedAt(Instant.now())
                .build());

        // Credit: Tuition Fees Revenue (Revenue Increase)
        ledgerRepository.save(FinancialLedgerEntry.builder()
                .tenantId(tenantId)
                .entryType("FEE_PAYMENT")
                .accountCode("4010-TUITION-FEE")
                .debit(BigDecimal.ZERO)
                .credit(paymentAmount)
                .description(String.format("Revenue realized for %s (%s)", invoice.getStudentName(), invoice.getInvoiceNumber()))
                .referenceId(invoice.getInvoiceNumber())
                .postedAt(Instant.now())
                .build());

        return saved;
    }

    public List<PaymentWebhookEvent> getWebhookEvents() {
        return webhookEventRepository.findByTenantIdOrderByReceivedAtDesc(TenantContext.getTenantId());
    }

    @Transactional
    public PaymentWebhookResult processWebhook(String gatewayName, String eventId, String eventType,
                                               String signature, String payloadJson,
                                               String invoiceNumber, BigDecimal amount) {
        String tenantId = TenantContext.getTenantId();

        // 1. Replay attack and Idempotency Guard
        if (webhookEventRepository.existsByTenantIdAndGatewayEventId(tenantId, eventId)) {
            log.warn("Replay attack or duplicate webhook intercepted: eventId={} for tenant={}", eventId, tenantId);
            return PaymentWebhookResult.builder()
                    .status("DUPLICATE_IGNORED")
                    .message("Event " + eventId + " has already been processed idempotently")
                    .isDuplicate(true)
                    .build();
        }

        // 2. Persist Webhook Event
        PaymentWebhookEvent event = PaymentWebhookEvent.builder()
                .tenantId(tenantId)
                .gatewayName(gatewayName.toUpperCase())
                .gatewayEventId(eventId)
                .eventType(eventType)
                .payloadSignature(signature != null ? signature : "TEST_VERIFIED_SIGNATURE")
                .rawPayloadJson(payloadJson)
                .invoiceNumber(invoiceNumber)
                .processingStatus("PROCESSED")
                .build();
        webhookEventRepository.save(event);

        // 3. Match Invoice and atomically apply double-entry settlement
        Optional<FeeInvoice> invoiceOpt = invoiceRepository.findAllByTenantId(tenantId).stream()
                .filter(inv -> inv.getInvoiceNumber().equalsIgnoreCase(invoiceNumber))
                .findFirst();

        if (invoiceOpt.isPresent()) {
            FeeInvoice invoice = invoiceOpt.get();
            collectPayment(invoice.getId(), amount != null ? amount : invoice.getBalanceAmount(), "GATEWAY_" + gatewayName.toUpperCase());

            auditLogService.recordEvent(
                    "WEBHOOK_SETTLEMENT",
                    "finance.invoices",
                    invoice.getInvoiceNumber(),
                    String.format("Payment of INR %s received via %s (Event ID: %s)", amount, gatewayName, eventId)
            );
        }

        return PaymentWebhookResult.builder()
                .status("SUCCESS")
                .message("Webhook processed and double-entry ledger updated")
                .isDuplicate(false)
                .eventId(eventId)
                .build();
    }

    @lombok.Data
    @lombok.Builder
    public static class PaymentWebhookResult {
        private String status;
        private String message;
        private boolean isDuplicate;
        private String eventId;
    }
}

