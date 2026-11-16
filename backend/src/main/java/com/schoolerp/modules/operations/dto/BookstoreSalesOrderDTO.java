package com.schoolerp.modules.operations.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class BookstoreSalesOrderDTO {
    private UUID id;
    private String orderReceiptNumber;
    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private String paymentMode;
    private BigDecimal totalAmount;
    private BigDecimal tenderedAmount;
    private String itemsJson;
    private String cashierStaffName;
    private LocalDateTime createdAt;

    public BookstoreSalesOrderDTO() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderReceiptNumber() {
        return orderReceiptNumber;
    }

    public void setOrderReceiptNumber(String orderReceiptNumber) {
        this.orderReceiptNumber = orderReceiptNumber;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTenderedAmount() {
        return tenderedAmount;
    }

    public void setTenderedAmount(BigDecimal tenderedAmount) {
        this.tenderedAmount = tenderedAmount;
    }

    public String getItemsJson() {
        return itemsJson;
    }

    public void setItemsJson(String itemsJson) {
        this.itemsJson = itemsJson;
    }

    public String getCashierStaffName() {
        return cashierStaffName;
    }

    public void setCashierStaffName(String cashierStaffName) {
        this.cashierStaffName = cashierStaffName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
