package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.entity.CashierDayScroll;
import com.schoolerp.modules.finance.entity.ChequeRecord;
import com.schoolerp.modules.finance.entity.FeeInvoice;
import com.schoolerp.modules.finance.repository.CashierDayScrollRepository;
import com.schoolerp.modules.finance.repository.ChequeRecordRepository;
import com.schoolerp.modules.finance.repository.FeeInvoiceRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndianBankingService {

    private final CashierDayScrollRepository scrollRepository;
    private final FeeInvoiceRepository invoiceRepository;
    private final ChequeRecordRepository chequeRepository;
    private final AuditLogService auditLogService;

    public UpiQrPayloadDTO generateUpiDynamicQr(UUID invoiceId) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        FeeInvoice invoice = invoiceRepository.findById(invoiceId)
                .filter(i -> i.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Fee Invoice not found: " + invoiceId));

        BigDecimal payable = invoice.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0 ? invoice.getBalanceAmount() : invoice.getTotalAmount();
        String vpa = "apexacademy.fees@sbi";
        String payeeName = "Apex Academy Educational Trust";
        String mcc = "8211"; // NPCI Merchant Category Code: Educational Services & Schools

        String encodedName = URLEncoder.encode(payeeName, StandardCharsets.UTF_8);
        String note = URLEncoder.encode("Tuition Fee " + invoice.getInvoiceNumber(), StandardCharsets.UTF_8);

        String upiIntentUri = String.format("upi://pay?pa=%s&pn=%s&mc=%s&tr=%s&am=%.2f&cu=INR&tn=%s",
                vpa, encodedName, mcc, invoice.getInvoiceNumber(), payable, note);

        String rawMockQr = "MOCK_NPCI_QR_MATRIX:" + upiIntentUri;
        String qrBase64 = Base64.getEncoder().encodeToString(rawMockQr.getBytes(StandardCharsets.UTF_8));

        return UpiQrPayloadDTO.builder()
                .invoiceId(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .studentName(invoice.getStudentName())
                .amountPayable(payable)
                .merchantVpa(vpa)
                .merchantName(payeeName)
                .merchantCategoryCode(mcc)
                .upiIntentUri(upiIntentUri)
                .qrBase64Payload(qrBase64)
                .build();
    }

    @Transactional
    public CashierDayScroll closeCashierDayScroll(
            String cashierName,
            LocalDate counterDate,
            int c500, int c200, int c100, int c50, int c20, int c10,
            BigDecimal totalSystemReceipts,
            String notes) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        LocalDate date = counterDate != null ? counterDate : LocalDate.now();

        BigDecimal physicalTotal = BigDecimal.ZERO
                .add(BigDecimal.valueOf(500L * c500))
                .add(BigDecimal.valueOf(200L * c200))
                .add(BigDecimal.valueOf(100L * c100))
                .add(BigDecimal.valueOf(50L * c50))
                .add(BigDecimal.valueOf(20L * c20))
                .add(BigDecimal.valueOf(10L * c10));

        BigDecimal sysReceipts = totalSystemReceipts != null ? totalSystemReceipts : physicalTotal;
        BigDecimal diff = physicalTotal.subtract(sysReceipts);

        String status = diff.compareTo(BigDecimal.ZERO) == 0 ? "BALANCED" : (diff.compareTo(BigDecimal.ZERO) > 0 ? "OVERAGE" : "SHORTAGE");

        CashierDayScroll scroll = CashierDayScroll.builder()
                .tenantId(tenantId)
                .cashierName(cashierName != null ? cashierName : "Chief Cashier (Mr. Rakesh Gupta)")
                .counterDate(date)
                .count500Notes(c500)
                .count200Notes(c200)
                .count100Notes(c100)
                .count50Notes(c50)
                .count20Notes(c20)
                .count10Notes(c10)
                .totalCashCalculated(physicalTotal)
                .totalSystemReceipts(sysReceipts)
                .discrepancyAmount(diff.abs())
                .status(status)
                .notes(notes != null ? notes : "Counter day-end denomination balanced for bank deposit slip")
                .closedAt(Instant.now())
                .build();

        CashierDayScroll saved = scrollRepository.save(scroll);

        auditLogService.recordEvent(
                "CLOSE_CASHIER_DAY_SCROLL",
                "finance.cashier_day_scrolls",
                saved.getId().toString(),
                String.format("Cashier %s closed counter on %s. Physical Cash: INR %s, Receipts: INR %s, Status: %s",
                        saved.getCashierName(), saved.getCounterDate(), saved.getTotalCashCalculated(), saved.getTotalSystemReceipts(), saved.getStatus())
        );

        return saved;
    }

    public List<CashierDayScroll> getCashierScrolls() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<CashierDayScroll> list = scrollRepository.findByTenantIdOrderByCounterDateDesc(tenantId);
        if (list.isEmpty()) {
            closeCashierDayScroll("Chief Cashier (Mr. Rakesh Gupta)", LocalDate.now(), 40, 30, 80, 20, 50, 40, new BigDecimal("35800.00"), "Day-end cash collection balanced");
            return scrollRepository.findByTenantIdOrderByCounterDateDesc(tenantId);
        }
        return list;
    }

    public PdcVaultSummaryDTO getPdcVaultStatus() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<ChequeRecord> allCheques = chequeRepository.findByTenantIdOrderByReceivedAtDesc(tenantId);

        BigDecimal totalPending = allCheques.stream()
                .filter(c -> "RECEIVED".equalsIgnoreCase(c.getStatus()) || "PENDING".equalsIgnoreCase(c.getStatus()))
                .map(ChequeRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return PdcVaultSummaryDTO.builder()
                .tenantId(tenantId)
                .vaultLocation("Bursar Office Fireproof Safe #4")
                .totalPdcInCustody(allCheques.size())
                .totalVaultValue(totalPending)
                .upcomingMaturityAlertsCount((int) allCheques.stream().filter(c -> "RECEIVED".equalsIgnoreCase(c.getStatus())).count())
                .cheques(allCheques)
                .build();
    }

    @Data
    @Builder
    public static class UpiQrPayloadDTO {
        private UUID invoiceId;
        private String invoiceNumber;
        private String studentName;
        private BigDecimal amountPayable;
        private String merchantVpa;
        private String merchantName;
        private String merchantCategoryCode;
        private String upiIntentUri;
        private String qrBase64Payload;
    }

    @Data
    @Builder
    public static class PdcVaultSummaryDTO {
        private String tenantId;
        private String vaultLocation;
        private int totalPdcInCustody;
        private BigDecimal totalVaultValue;
        private int upcomingMaturityAlertsCount;
        private List<ChequeRecord> cheques;
    }
}

