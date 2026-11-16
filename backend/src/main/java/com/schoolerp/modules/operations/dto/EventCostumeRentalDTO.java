package com.schoolerp.modules.operations.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class EventCostumeRentalDTO {
    private UUID id;
    private UUID eventId;
    private String eventName;
    private String costumeCode;
    private String costumeDescription;
    private String vendorSupplierName;
    private UUID assignedStudentId;
    private String assignedStudentName;
    private BigDecimal rentalRatePerDay;
    private LocalDate issuedDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private String returnCondition;
    private BigDecimal damageFineAmount;
    private LocalDateTime createdAt;

    public EventCostumeRentalDTO() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCostumeCode() {
        return costumeCode;
    }

    public void setCostumeCode(String costumeCode) {
        this.costumeCode = costumeCode;
    }

    public String getCostumeDescription() {
        return costumeDescription;
    }

    public void setCostumeDescription(String costumeDescription) {
        this.costumeDescription = costumeDescription;
    }

    public String getVendorSupplierName() {
        return vendorSupplierName;
    }

    public void setVendorSupplierName(String vendorSupplierName) {
        this.vendorSupplierName = vendorSupplierName;
    }

    public UUID getAssignedStudentId() {
        return assignedStudentId;
    }

    public void setAssignedStudentId(UUID assignedStudentId) {
        this.assignedStudentId = assignedStudentId;
    }

    public String getAssignedStudentName() {
        return assignedStudentName;
    }

    public void setAssignedStudentName(String assignedStudentName) {
        this.assignedStudentName = assignedStudentName;
    }

    public BigDecimal getRentalRatePerDay() {
        return rentalRatePerDay;
    }

    public void setRentalRatePerDay(BigDecimal rentalRatePerDay) {
        this.rentalRatePerDay = rentalRatePerDay;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getReturnCondition() {
        return returnCondition;
    }

    public void setReturnCondition(String returnCondition) {
        this.returnCondition = returnCondition;
    }

    public BigDecimal getDamageFineAmount() {
        return damageFineAmount;
    }

    public void setDamageFineAmount(BigDecimal damageFineAmount) {
        this.damageFineAmount = damageFineAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
