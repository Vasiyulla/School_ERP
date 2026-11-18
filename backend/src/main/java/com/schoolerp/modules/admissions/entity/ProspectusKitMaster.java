package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "prospectus_kit_masters", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProspectusKitMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String serialBarcode; // e.g. KIT-2026-0001

    @Column(nullable = false)
    private String academicYear; // e.g. 2026-2027

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    @Builder.Default
    private String status = "IN_STOCK"; // IN_STOCK, SOLD, DAMAGED, COMPLIMENTARY

    private String buyerParentName;

    private String buyerPhone;

    private String paymentMode; // CASH, UPI_QR, CARD, WAIVED

    private String transactionRef;

    private UUID soldByUserId;

    private Instant soldAt;

    private UUID linkedInquiryId;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    @Builder.Default
    private Instant updatedAt = Instant.now();
}
