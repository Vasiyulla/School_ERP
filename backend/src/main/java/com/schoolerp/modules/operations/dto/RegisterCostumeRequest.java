package com.schoolerp.modules.operations.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class RegisterCostumeRequest {
    private UUID eventId;
    private String eventName;
    private String costumeCode;
    private String costumeDescription;
    private String vendorSupplierName;
    private BigDecimal rentalRatePerDay;

    public RegisterCostumeRequest() {}

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

    public BigDecimal getRentalRatePerDay() {
        return rentalRatePerDay;
    }

    public void setRentalRatePerDay(BigDecimal rentalRatePerDay) {
        this.rentalRatePerDay = rentalRatePerDay;
    }
}
