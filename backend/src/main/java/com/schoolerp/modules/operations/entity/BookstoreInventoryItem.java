package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookstore_inventory_items", schema = "operations",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_bookstore_sku", columnNames = {"tenantId", "skuCode"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookstoreInventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String skuCode; // e.g. "SKU-TXT-G10-MATH", "SKU-UNI-BLAZER-34"

    @Column(nullable = false)
    private String itemCategory; // TEXTBOOK_BUNDLE, NOTEBOOK_PACK, UNIFORM_SUMMER, UNIFORM_WINTER, UNIFORM_SPORTS, ACCESSORY

    @Column(nullable = false)
    private String itemName;

    private String targetGrade; // e.g. "Grade 10", "Grade 6"

    private String sizeSpecification; // e.g. "Size 34", "Standard Pack"

    @Column(nullable = false)
    private BigDecimal unitSellingPrice;

    @Column(nullable = false)
    private BigDecimal costPrice;

    @Builder.Default
    private int stockQuantity = 0;

    @Builder.Default
    private int reorderThreshold = 10;

    private String supplierVendorName;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Version
    private Long version;
}
