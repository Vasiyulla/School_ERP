package com.schoolerp.modules.examinations.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExamInvigilationDutyDTO {
    private UUID id;
    private UUID examScheduleId;
    private String examName;
    private String subjectName;
    private LocalDate examDate;
    private String hallRoomNumber;
    private UUID invigilatorStaffId;
    private String invigilatorStaffName;
    private String dutyRole;
    private Boolean paperSealVerified;
    private LocalDateTime sealVerificationTimestamp;
    private String witnessStaffName;
    private LocalDateTime createdAt;

    public ExamInvigilationDutyDTO() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getExamScheduleId() {
        return examScheduleId;
    }

    public void setExamScheduleId(UUID examScheduleId) {
        this.examScheduleId = examScheduleId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getHallRoomNumber() {
        return hallRoomNumber;
    }

    public void setHallRoomNumber(String hallRoomNumber) {
        this.hallRoomNumber = hallRoomNumber;
    }

    public UUID getInvigilatorStaffId() {
        return invigilatorStaffId;
    }

    public void setInvigilatorStaffId(UUID invigilatorStaffId) {
        this.invigilatorStaffId = invigilatorStaffId;
    }

    public String getInvigilatorStaffName() {
        return invigilatorStaffName;
    }

    public void setInvigilatorStaffName(String invigilatorStaffName) {
        this.invigilatorStaffName = invigilatorStaffName;
    }

    public String getDutyRole() {
        return dutyRole;
    }

    public void setDutyRole(String dutyRole) {
        this.dutyRole = dutyRole;
    }

    public Boolean getPaperSealVerified() {
        return paperSealVerified;
    }

    public void setPaperSealVerified(Boolean paperSealVerified) {
        this.paperSealVerified = paperSealVerified;
    }

    public LocalDateTime getSealVerificationTimestamp() {
        return sealVerificationTimestamp;
    }

    public void setSealVerificationTimestamp(LocalDateTime sealVerificationTimestamp) {
        this.sealVerificationTimestamp = sealVerificationTimestamp;
    }

    public String getWitnessStaffName() {
        return witnessStaffName;
    }

    public void setWitnessStaffName(String witnessStaffName) {
        this.witnessStaffName = witnessStaffName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
