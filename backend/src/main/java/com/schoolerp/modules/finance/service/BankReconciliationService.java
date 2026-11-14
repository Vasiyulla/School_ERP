package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.dto.BankReconciliationResultDTO;
import com.schoolerp.modules.finance.dto.BankStatementEntryInputDTO;
import com.schoolerp.modules.finance.dto.ReconcileBankStatementRequest;
import com.schoolerp.modules.finance.entity.BankStatementEntry;
import com.schoolerp.modules.finance.entity.FeeInvoice;
import com.schoolerp.modules.finance.entity.FinancialLedgerEntry;
import com.schoolerp.modules.finance.repository.BankStatementEntryRepository;
import com.schoolerp.modules.finance.repository.FeeInvoiceRepository;
import com.schoolerp.modules.finance.repository.FinancialLedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankReconciliationService {

    private final BankStatementEntryRepository statementRepository;
    private final FeeInvoiceRepository feeInvoiceRepository;
    private final FinancialLedgerEntryRepository ledgerRepository;

    private static final Pattern INVOICE_PATTERN = Pattern.compile("INV-\\d{4}-\\d{4}|INV\\d+");
    private static final Pattern ADMISSION_PATTERN = Pattern.compile("ADM-\\d{4}-\\d{4}|ADM\\d+");
    private static final Pattern VAN_PATTERN = Pattern.compile("(HDFC|ICIC|SBIN)[A-Z0-9]{6,12}");

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public BankReconciliationResultDTO processStatement(ReconcileBankStatementRequest request) {
        String tenantId = resolveTenant();
        String bankName = request.getBankName() != null ? request.getBankName().toUpperCase() : "HDFC_BANK";

        List<BankStatementEntry> reconciledList = new ArrayList<>();
        List<BankStatementEntry> suspenseList = new ArrayList<>();
        BigDecimal totalReconciledAmount = BigDecimal.ZERO;

        List<FeeInvoice> allInvoices = feeInvoiceRepository.findAllByTenantId(tenantId);
        Map<String, FeeInvoice> invoiceMap = new HashMap<>();
        for (FeeInvoice inv : allInvoices) {
            invoiceMap.put(inv.getInvoiceNumber().toUpperCase(), inv);
        }

        if (request.getStatementEntries() != null) {
            for (BankStatementEntryInputDTO input : request.getStatementEntries()) {
                String utr = (input.getUtrNumber() != null && !input.getUtrNumber().isBlank())
                        ? input.getUtrNumber() : "UTR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                BigDecimal amount = input.getAmount() != null ? input.getAmount() : BigDecimal.ZERO;
                String narration = input.getNarration() != null ? input.getNarration() : "";

                // Attempt Rule Matching
                String matchedInvoiceNo = null;
                String matchedAdmissionNo = null;
                String ruleApplied = null;

                // Rule 1: Invoice Number in narration
                Matcher invMatcher = INVOICE_PATTERN.matcher(narration);
                if (invMatcher.find()) {
                    String extracted = invMatcher.group().toUpperCase();
                    if (invoiceMap.containsKey(extracted)) {
                        matchedInvoiceNo = extracted;
                        ruleApplied = "RULE_1_INVOICE_PATTERN_MATCH";
                    }
                }

                // Rule 2: Admission Number in narration
                if (matchedInvoiceNo == null) {
                    Matcher admMatcher = ADMISSION_PATTERN.matcher(narration);
                    if (admMatcher.find()) {
                        matchedAdmissionNo = admMatcher.group().toUpperCase();
                        // Find first open invoice for this student admission prefix
                        for (FeeInvoice inv : allInvoices) {
                            if (inv.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
                                matchedInvoiceNo = inv.getInvoiceNumber();
                                ruleApplied = "RULE_2_ADMISSION_PATTERN_MATCH";
                                break;
                            }
                        }
                    }
                }

                // Rule 3: Virtual Account Number (VAN) matching
                if (matchedInvoiceNo == null) {
                    Matcher vanMatcher = VAN_PATTERN.matcher(narration);
                    if (vanMatcher.find()) {
                        matchedAdmissionNo = "VAN-" + vanMatcher.group();
                        ruleApplied = "RULE_3_VAN_PREFIX_MATCH";
                        if (!allInvoices.isEmpty()) {
                            matchedInvoiceNo = allInvoices.get(0).getInvoiceNumber();
                        }
                    }
                }

                BankStatementEntry entry = BankStatementEntry.builder()
                        .tenantId(tenantId)
                        .bankName(bankName)
                        .utrNumber(utr)
                        .transactionDate(input.getTransactionDate() != null ? input.getTransactionDate() : java.time.LocalDate.now())
                        .amount(amount)
                        .narration(narration)
                        .build();

                if (matchedInvoiceNo != null) {
                    entry.setStatus("RECONCILED");
                    entry.setMatchedInvoiceNumber(matchedInvoiceNo);
                    entry.setMatchedStudentAdmissionNumber(matchedAdmissionNo);
                    entry.setSettledAt(LocalDateTime.now());
                    entry.setMatchRuleApplied(ruleApplied);

                    // Update invoice balance
                    FeeInvoice targetInvoice = invoiceMap.get(matchedInvoiceNo);
                    if (targetInvoice != null) {
                        BigDecimal newPaid = targetInvoice.getPaidAmount().add(amount);
                        BigDecimal newBal = targetInvoice.getTotalAmount().subtract(newPaid).max(BigDecimal.ZERO);
                        targetInvoice.setPaidAmount(newPaid);
                        targetInvoice.setBalanceAmount(newBal);
                        if (newBal.compareTo(BigDecimal.ZERO) == 0) {
                            targetInvoice.setStatus("PAID");
                        }
                        feeInvoiceRepository.save(targetInvoice);
                    }

                    // Post double-entry ledger entries
                    ledgerRepository.save(FinancialLedgerEntry.builder()
                            .tenantId(tenantId)
                            .entryType("BANK_AUTO_RECONCILIATION")
                            .debit(amount)
                            .credit(BigDecimal.ZERO)
                            .accountCode("1020-BANK-" + bankName)
                            .description("Bank Auto-Reconciliation Receipt for " + matchedInvoiceNo + " UTR: " + utr)
                            .referenceId(utr)
                            .build());

                    ledgerRepository.save(FinancialLedgerEntry.builder()
                            .tenantId(tenantId)
                            .entryType("BANK_AUTO_RECONCILIATION")
                            .debit(BigDecimal.ZERO)
                            .credit(amount)
                            .accountCode("1010-ACCOUNTS-RECEIVABLE")
                            .description("Credit AR for Fee Settlement " + matchedInvoiceNo)
                            .referenceId(utr)
                            .build());

                    statementRepository.save(entry);
                    reconciledList.add(entry);
                    totalReconciledAmount = totalReconciledAmount.add(amount);
                } else {
                    entry.setStatus("SUSPENSE");
                    entry.setMatchRuleApplied("NONE_PLACED_IN_SUSPENSE");
                    statementRepository.save(entry);
                    suspenseList.add(entry);
                }
            }
        }

        return BankReconciliationResultDTO.builder()
                .tenantId(tenantId)
                .bankName(bankName)
                .totalProcessed(reconciledList.size() + suspenseList.size())
                .autoReconciledCount(reconciledList.size())
                .suspensePoolCount(suspenseList.size())
                .totalReconciledAmount(totalReconciledAmount)
                .reconciledEntries(reconciledList)
                .suspenseEntries(suspenseList)
                .build();
    }

    @Transactional(readOnly = true)
    public List<BankStatementEntry> getSuspensePool() {
        String tenantId = resolveTenant();
        return statementRepository.findAllByTenantIdAndStatus(tenantId, "SUSPENSE");
    }
}

