package com.schoolerp.modules.examinations.dto;

import java.time.LocalDate;
import java.util.UUID;

public class ScheduleInvigilationRequest {
    private UUID examScheduleId;
    private String examName;
    private String subjectName;
    private String hallRoomNumber;
    private UUID invigilatorStaffId;
    private String dutyRole;
    private LocalDate examDate;

    public ScheduleInvigilationRequest() {}

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

    public String getDutyRole() {
        return dutyRole;
    }

    public void setDutyRole(String dutyRole) {
        this.dutyRole = dutyRole;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }
}
