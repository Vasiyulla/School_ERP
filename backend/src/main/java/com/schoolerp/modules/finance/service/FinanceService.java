package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.entity.*;
import com.schoolerp.modules.finance.repository.*;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private final FeeStructureRepository feeStructureRepository;
    private final FeeConcessionRepository feeConcessionRepository;
    private final ChequeRecordRepository chequeRecordRepository;
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

    // --- 1. Dynamic Fee Structure by Grade Level ---

    public List<FeeStructure> getFeeStructures() {
        return feeStructureRepository.findByTenantId(TenantContext.getTenantId());
    }

    @Transactional
    public FeeStructure createFeeStructure(FeeStructure structure) {
        String tenantId = TenantContext.getTenantId();
        structure.setTenantId(tenantId);

        BigDecimal sum = structure.getItems().stream()
                .map(FeeStructureItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        structure.setTotalAmount(sum);

        FeeStructure saved = feeStructureRepository.save(structure);
        auditLogService.recordEvent(
                "CREATE_FEE_STRUCTURE",
                "finance.fee_structures",
                saved.getId().toString(),
                "Fee structure created for " + saved.getGradeLevel() + " (" + saved.getAcademicYear() + "): Total INR " + saved.getTotalAmount()
        );
        return saved;
    }

    // --- 2. Sibling & Scholarship Concession Engine ---

    public List<FeeConcession> getConcessions() {
        return feeConcessionRepository.findByTenantId(TenantContext.getTenantId());
    }

    @Transactional
    public FeeConcession applyConcession(FeeConcession concession) {
        String tenantId = TenantContext.getTenantId();
        concession.setTenantId(tenantId);
        FeeConcession saved = feeConcessionRepository.save(concession);

        auditLogService.recordEvent(
                "APPLY_CONCESSION",
                "finance.concessions",
                saved.getId().toString(),
                String.format("%s of %s%% applied to student %s (%s)",
                        saved.getConcessionType(), saved.getPercentageDiscount(), saved.getStudentName(), saved.getReason())
        );
        return saved;
    }

    public ConcessionCalculationResult calculateNetFee(UUID studentId, BigDecimal grossTuitionFee) {
        String tenantId = TenantContext.getTenantId();
        Optional<FeeConcession> concessionOpt = feeConcessionRepository
                .findByTenantIdAndStudentIdAndStatus(tenantId, studentId, "ACTIVE");

        if (concessionOpt.isEmpty()) {
            return ConcessionCalculationResult.builder()
                    .grossFee(grossTuitionFee)
                    .discountAmount(BigDecimal.ZERO)
                    .netFee(grossTuitionFee)
                    .concessionType("NONE")
                    .discountPercentage(BigDecimal.ZERO)
                    .build();
        }

        FeeConcession c = concessionOpt.get();
        BigDecimal discount = BigDecimal.ZERO;
        if (c.getPercentageDiscount().compareTo(BigDecimal.ZERO) > 0) {
            discount = grossTuitionFee.multiply(c.getPercentageDiscount())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (c.getFlatDiscountAmount() != null) {
            discount = c.getFlatDiscountAmount();
        }

        BigDecimal net = grossTuitionFee.subtract(discount).max(BigDecimal.ZERO);

        return ConcessionCalculationResult.builder()
                .grossFee(grossTuitionFee)
                .discountAmount(discount)
                .netFee(net)
                .concessionType(c.getConcessionType())
                .discountPercentage(c.getPercentageDiscount())
                .reason(c.getReason())
                .build();
    }

    // --- 3. Cheque / DD Clearance Lifecycle ---

    public List<ChequeRecord> getChequeRecords() {
        return chequeRecordRepository.findByTenantIdOrderByReceivedAtDesc(TenantContext.getTenantId());
    }

    @Transactional
    public ChequeRecord receiveCheque(ChequeRecord cheque) {
        String tenantId = TenantContext.getTenantId();
        cheque.setTenantId(tenantId);
        cheque.setStatus("RECEIVED");
        ChequeRecord saved = chequeRecordRepository.save(cheque);

        auditLogService.recordEvent(
                "RECEIVE_CHEQUE",
                "finance.cheques",
                saved.getChequeNumber(),
                String.format("Cheque #%s of INR %s received from %s (Bank: %s)",
                        saved.getChequeNumber(), saved.getAmount(), saved.getPayerName(), saved.getBankName())
        );
        return saved;
    }

    @Transactional
    public ChequeRecord updateChequeStatus(UUID chequeId, String newStatus, String remarks) {
        String tenantId = TenantContext.getTenantId();
        ChequeRecord cheque = chequeRecordRepository.findById(chequeId)
                .filter(c -> c.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Cheque record not found: " + chequeId));

        cheque.setStatus(newStatus.toUpperCase());
        cheque.setRemarks(remarks);

        if ("DEPOSITED".equalsIgnoreCase(newStatus)) {
            cheque.setDepositedDate(LocalDate.now());
        } else if ("CLEARED".equalsIgnoreCase(newStatus)) {
            cheque.setClearedDate(LocalDate.now());

            // Match invoice and collect payment atomically with double-entry ledger posting
            invoiceRepository.findAllByTenantId(tenantId).stream()
                    .filter(inv -> inv.getInvoiceNumber().equalsIgnoreCase(cheque.getInvoiceNumber()))
                    .findFirst()
                    .ifPresent(inv -> collectPayment(inv.getId(), cheque.getAmount(), "CHEQUE_" + cheque.getChequeNumber()));

            auditLogService.recordEvent(
                    "CHEQUE_CLEARED",
                    "finance.cheques",
                    cheque.getChequeNumber(),
                    String.format("Cheque #%s CLEARED. Deposited into 1020-BANK-HDFC. Invoice %s settled.",
                            cheque.getChequeNumber(), cheque.getInvoiceNumber())
            );
        } else if ("BOUNCED".equalsIgnoreCase(newStatus)) {
            cheque.setBouncePenalty(BigDecimal.valueOf(500.00)); // ₹500 standard cheque bounce penalty

            // Post penalty debit to double-entry ledger
            ledgerRepository.save(FinancialLedgerEntry.builder()
                    .tenantId(tenantId)
                    .entryType("CHEQUE_BOUNCE_PENALTY")
                    .accountCode("5020-BANK-CHARGES")
                    .debit(BigDecimal.valueOf(500.00))
                    .credit(BigDecimal.ZERO)
                    .description(String.format("Dishonour penalty for Cheque #%s (%s)", cheque.getChequeNumber(), cheque.getPayerName()))
                    .referenceId(cheque.getChequeNumber())
                    .postedAt(Instant.now())
                    .build());

            auditLogService.recordEvent(
                    "CHEQUE_BOUNCED",
                    "finance.cheques",
                    cheque.getChequeNumber(),
                    String.format("Cheque #%s BOUNCED! Applied INR 500 penalty. Payer: %s. Reason: %s",
                            cheque.getChequeNumber(), cheque.getPayerName(), remarks)
            );
        }

        return chequeRecordRepository.save(cheque);
    }

    // --- 4. Automated Late Fee Surcharge Calculator ---

    public SurchargeCalculationResult calculateLateFeeSurcharge(UUID invoiceId, LocalDate asOfDate) {
        String tenantId = TenantContext.getTenantId();
        FeeInvoice invoice = invoiceRepository.findByIdAndTenantId(invoiceId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceId));

        if (invoice.getDueDate() == null || !invoice.getStatus().equalsIgnoreCase("OVERDUE") && !invoice.getStatus().equalsIgnoreCase("PENDING")) {
            return SurchargeCalculationResult.builder()
                    .invoiceNumber(invoice.getInvoiceNumber())
                    .daysOverdue(0)
                    .dailyRate(BigDecimal.valueOf(50.00))
                    .totalSurcharge(BigDecimal.ZERO)
                    .isSurchargeApplicable(false)
                    .build();
        }

        long daysOverdue = ChronoUnit.DAYS.between(invoice.getDueDate(), asOfDate != null ? asOfDate : LocalDate.now());
        if (daysOverdue <= 0) {
            return SurchargeCalculationResult.builder()
                    .invoiceNumber(invoice.getInvoiceNumber())
                    .daysOverdue(0)
                    .dailyRate(BigDecimal.valueOf(50.00))
                    .totalSurcharge(BigDecimal.ZERO)
                    .isSurchargeApplicable(false)
                    .build();
        }

        BigDecimal dailyRate = BigDecimal.valueOf(50.00); // ₹50/day standard school late fee
        BigDecimal totalSurcharge = dailyRate.multiply(BigDecimal.valueOf(daysOverdue));

        return SurchargeCalculationResult.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .daysOverdue(daysOverdue)
                .dailyRate(dailyRate)
                .totalSurcharge(totalSurcharge)
                .isSurchargeApplicable(true)
                .build();
    }

    @lombok.Data
    @lombok.Builder
    public static class ConcessionCalculationResult {
        private BigDecimal grossFee;
        private BigDecimal discountAmount;
        private BigDecimal netFee;
        private String concessionType;
        private BigDecimal discountPercentage;
        private String reason;
    }

    @lombok.Data
    @lombok.Builder
    public static class SurchargeCalculationResult {
        private String invoiceNumber;
        private long daysOverdue;
        private BigDecimal dailyRate;
        private BigDecimal totalSurcharge;
        private boolean isSurchargeApplicable;
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

