package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.dto.CreateVendorInvoiceRequest;
import com.schoolerp.modules.finance.entity.FinancialLedgerEntry;
import com.schoolerp.modules.finance.entity.VendorInvoiceRecord;
import com.schoolerp.modules.finance.repository.FinancialLedgerEntryRepository;
import com.schoolerp.modules.finance.repository.VendorInvoiceRecordRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorProcurementService {

    private final VendorInvoiceRecordRepository vendorInvoiceRepository;
    private final FinancialLedgerEntryRepository ledgerRepository;
    private final AuditLogService auditLogService;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant_apex_mumbai_01";
    }

    @Transactional
    public VendorInvoiceRecord recordVendorInvoice(CreateVendorInvoiceRequest request) {
        String tenantId = resolveTenant();
        BigDecimal gross = request.getGrossAmount();
        double rate = request.getTdsRatePercentage() > 0 ? request.getTdsRatePercentage() : 2.0;

        BigDecimal tds = gross.multiply(BigDecimal.valueOf(rate / 100.0)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal net = gross.subtract(tds);

        VendorInvoiceRecord record = VendorInvoiceRecord.builder()
                .tenantId(tenantId)
                .vendorName(request.getVendorName())
                .vendorGstin(request.getVendorGstin())
                .vendorPan(request.getVendorPan())
                .serviceCategory(request.getServiceCategory())
                .invoiceNumber(request.getInvoiceNumber())
                .invoiceDate(request.getInvoiceDate())
                .tdsSection(request.getTdsSection())
                .tdsRatePercentage(rate)
                .grossAmount(gross)
                .tdsWithheldAmount(tds)
                .netPayableAmount(net)
                .paymentStatus("PENDING_DISBURSEMENT")
                .purchaseOrderRef(request.getPurchaseOrderRef() != null ? request.getPurchaseOrderRef() : "PO-2026-0812")
                .remarks(request.getRemarks())
                .build();

        VendorInvoiceRecord saved = vendorInvoiceRepository.save(record);

        // Double-entry general ledger entries:
        // 1. Debit: 5020-CAMPUS-OPERATIONS-EXPENSE for gross amount
        ledgerRepository.save(FinancialLedgerEntry.builder()
                .tenantId(tenantId)
                .entryType("OPERATIONAL_EXPENSE")
                .accountCode("5020-CAMPUS-OPERATIONS-EXPENSE")
                .debit(gross)
                .credit(BigDecimal.ZERO)
                .description(String.format("Vendor invoice booked: %s (Inv: %s) for %s", request.getVendorName(), request.getInvoiceNumber(), request.getServiceCategory()))
                .referenceId(saved.getId().toString())
                .postedAt(Instant.now())
                .build());

        // 2. Credit: 2050-TDS-PAYABLE-STATUTORY for withheld TDS
        if (tds.compareTo(BigDecimal.ZERO) > 0) {
            ledgerRepository.save(FinancialLedgerEntry.builder()
                    .tenantId(tenantId)
                    .entryType("TDS_WITHHOLDING")
                    .accountCode("2050-TDS-PAYABLE-STATUTORY")
                    .debit(BigDecimal.ZERO)
                    .credit(tds)
                    .description(String.format("TDS u/s %s withheld at %.1f%% on %s (PAN: %s)", request.getTdsSection(), rate, request.getVendorName(), request.getVendorPan()))
                    .referenceId(saved.getId().toString())
                    .postedAt(Instant.now())
                    .build());
        }

        // 3. Credit: 2010-ACCOUNTS-PAYABLE-VENDOR for net amount
        ledgerRepository.save(FinancialLedgerEntry.builder()
                .tenantId(tenantId)
                .entryType("ACCOUNTS_PAYABLE")
                .accountCode("2010-ACCOUNTS-PAYABLE-VENDOR")
                .debit(BigDecimal.ZERO)
                .credit(net)
                .description(String.format("Net payable liability booked for vendor %s", request.getVendorName()))
                .referenceId(saved.getId().toString())
                .postedAt(Instant.now())
                .build());

        auditLogService.recordEvent(
                "VENDOR_INVOICE_BOOKED",
                "finance.vendor_invoices",
                saved.getId().toString(),
                String.format("Vendor invoice #%s from %s: Gross INR %s, TDS %s withheld (INR %s), Net Payable INR %s",
                        request.getInvoiceNumber(), request.getVendorName(), gross, request.getTdsSection(), tds, net)
        );

        return saved;
    }

    public List<VendorInvoiceRecord> getInvoices() {
        String tenantId = resolveTenant();
        return vendorInvoiceRepository.findByTenantId(tenantId);
    }
}
