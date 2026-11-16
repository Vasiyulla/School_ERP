package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookstore_sales_orders", schema = "operations",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_bookstore_receipt", columnNames = {"tenantId", "orderReceiptNumber"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookstoreSalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderReceiptNumber; // e.g. "BK-REC-2026-0812"

    private UUID studentId;
    private String studentName;
    private String admissionNumber;

    @Column(nullable = false)
    private String paymentMode; // CASH, UPI, STUDENT_LEDGER_DEBIT

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private BigDecimal tenderedAmount;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String itemsJson;

    @Column(nullable = false)
    private String cashierStaffName;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
