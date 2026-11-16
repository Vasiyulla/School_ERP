package com.schoolerp.modules.examinations.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UfmMalpracticeDocketDTO {
    private UUID id;
    private String docketNumber;
    private UUID examScheduleId;
    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private String subjectName;
    private LocalDateTime incidentTimestamp;
    private String contrabandType;
    private String invigilatorRemarks;
    private String studentStatement;
    private String verdictStatus;
    private LocalDate verdictDate;
    private LocalDateTime createdAt;

    public UfmMalpracticeDocketDTO() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocketNumber() {
        return docketNumber;
    }

    public void setDocketNumber(String docketNumber) {
        this.docketNumber = docketNumber;
    }

    public UUID getExamScheduleId() {
        return examScheduleId;
    }

    public void setExamScheduleId(UUID examScheduleId) {
        this.examScheduleId = examScheduleId;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public LocalDateTime getIncidentTimestamp() {
        return incidentTimestamp;
    }

    public void setIncidentTimestamp(LocalDateTime incidentTimestamp) {
        this.incidentTimestamp = incidentTimestamp;
    }

    public String getContrabandType() {
        return contrabandType;
    }

    public void setContrabandType(String contrabandType) {
        this.contrabandType = contrabandType;
    }

    public String getInvigilatorRemarks() {
        return invigilatorRemarks;
    }

    public void setInvigilatorRemarks(String invigilatorRemarks) {
        this.invigilatorRemarks = invigilatorRemarks;
    }

    public String getStudentStatement() {
        return studentStatement;
    }

    public void setStudentStatement(String studentStatement) {
        this.studentStatement = studentStatement;
    }

    public String getVerdictStatus() {
        return verdictStatus;
    }

    public void setVerdictStatus(String verdictStatus) {
        this.verdictStatus = verdictStatus;
    }

    public LocalDate getVerdictDate() {
        return verdictDate;
    }

    public void setVerdictDate(LocalDate verdictDate) {
        this.verdictDate = verdictDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
