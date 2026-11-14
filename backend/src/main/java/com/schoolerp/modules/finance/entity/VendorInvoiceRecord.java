package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "vendor_invoices", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorInvoiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String vendorName; // e.g. Mahavir Bus Transport Corp, Sharp Security Services, Oxford University Press

    @Column(nullable = false)
    private String vendorGstin;

    @Column(nullable = false)
    private String vendorPan;

    @Column(nullable = false)
    private String serviceCategory; // FLEET_TRANSPORT, CAMPUS_SECURITY, IT_INFRASTRUCTURE, UNIFORMS, BOOKS_PRINTING, LAB_CHEMICALS

    @Column(nullable = false)
    private String invoiceNumber; // Vendor's invoice number

    @Column(nullable = false)
    private LocalDate invoiceDate;

    @Column(nullable = false)
    private String tdsSection; // SECTION_194C_CONTRACTORS (1% or 2%), SECTION_194J_PROFESSIONAL (10%)

    @Column(nullable = false)
    private double tdsRatePercentage; // 1.0, 2.0, 10.0

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal grossAmount; // e.g. INR 125000.00

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal tdsWithheldAmount; // e.g. 2% = INR 2500.00

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal netPayableAmount; // e.g. INR 122500.00

    @Column(nullable = false)
    @Builder.Default
    private String paymentStatus = "PENDING_DISBURSEMENT"; // PENDING_DISBURSEMENT, PAID_SETTLED

    private String paymentReference;

    private String purchaseOrderRef;

    private String remarks;

    @Builder.Default
    private Instant recordedAt = Instant.now();
}

