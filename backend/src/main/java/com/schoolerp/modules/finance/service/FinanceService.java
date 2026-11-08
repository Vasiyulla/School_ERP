package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.entity.FeeInvoice;
import com.schoolerp.modules.finance.entity.FinancialLedgerEntry;
import com.schoolerp.modules.finance.repository.FeeInvoiceRepository;
import com.schoolerp.modules.finance.repository.FinancialLedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final FeeInvoiceRepository invoiceRepository;
    private final FinancialLedgerEntryRepository ledgerRepository;

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
}

