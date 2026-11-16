package com.schoolerp.modules.examinations.dto;

public class VerifySealsRequest {
    private Boolean paperSealVerified;
    private String witnessStaffName;

    public VerifySealsRequest() {}

    public Boolean getPaperSealVerified() {
        return paperSealVerified;
    }

    public void setPaperSealVerified(Boolean paperSealVerified) {
        this.paperSealVerified = paperSealVerified;
    }

    public String getWitnessStaffName() {
        return witnessStaffName;
    }

    public void setWitnessStaffName(String witnessStaffName) {
        this.witnessStaffName = witnessStaffName;
    }
}
