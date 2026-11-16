package com.schoolerp.modules.operations.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class IssueCostumeRequest {
    private UUID costumeId;
    private UUID studentId;
    private String action; // ISSUE or RETURN
    private LocalDate expectedReturnDate;
    private String returnCondition; // RETURNED_PRISTINE, RETURNED_DAMAGED, LOST
    private BigDecimal damageFineAmount;

    public IssueCostumeRequest() {}

    public UUID getCostumeId() {
        return costumeId;
    }

    public void setCostumeId(UUID costumeId) {
        this.costumeId = costumeId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
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
}
