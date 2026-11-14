package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.dto.CreateVendorInvoiceRequest;
import com.schoolerp.modules.finance.entity.VendorInvoiceRecord;
import com.schoolerp.modules.finance.service.VendorProcurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/vendors/invoices")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vendor Procurement & Statutory TDS Ledger", description = "Vendor procurement bills, TDS withholding u/s 194C (Contractors) and 194J (Professional), and Accounts Payable ledger entries")
public class VendorInvoiceController {

    private final VendorProcurementService vendorProcurementService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ACCOUNTANT')")
    @Operation(summary = "Record vendor procurement invoice with automated Indian TDS withholding and double-entry ledger postings")
    public ResponseEntity<ApiResponse<VendorInvoiceRecord>> recordInvoice(
            @Valid @RequestBody CreateVendorInvoiceRequest request) {
        VendorInvoiceRecord record = vendorProcurementService.recordVendorInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Vendor invoice booked and TDS liability recorded", record));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ACCOUNTANT')")
    @Operation(summary = "Get all vendor procurement bills and TDS withholding status")
    public ResponseEntity<ApiResponse<List<VendorInvoiceRecord>>> getInvoices() {
        return ResponseEntity.ok(ApiResponse.ok(vendorProcurementService.getInvoices()));
    }
}

