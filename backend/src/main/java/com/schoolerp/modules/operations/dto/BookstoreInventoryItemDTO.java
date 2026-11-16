package com.schoolerp.modules.operations.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class BookstoreInventoryItemDTO {
    private UUID id;
    private String skuCode;
    private String itemCategory;
    private String itemName;
    private String targetGrade;
    private String sizeSpecification;
    private BigDecimal unitSellingPrice;
    private BigDecimal costPrice;
    private Integer stockQuantity;
    private Integer reorderThreshold;
    private String supplierVendorName;
    private LocalDateTime createdAt;

    public BookstoreInventoryItemDTO() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public void setTargetGrade(String targetGrade) {
        this.targetGrade = targetGrade;
    }

    public String getSizeSpecification() {
        return sizeSpecification;
    }

    public void setSizeSpecification(String sizeSpecification) {
        this.sizeSpecification = sizeSpecification;
    }

    public BigDecimal getUnitSellingPrice() {
        return unitSellingPrice;
    }

    public void setUnitSellingPrice(BigDecimal unitSellingPrice) {
        this.unitSellingPrice = unitSellingPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(Integer reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    public String getSupplierVendorName() {
        return supplierVendorName;
    }

    public void setSupplierVendorName(String supplierVendorName) {
        this.supplierVendorName = supplierVendorName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
