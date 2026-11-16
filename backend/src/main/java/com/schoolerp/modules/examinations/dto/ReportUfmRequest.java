package com.schoolerp.modules.examinations.dto;

import java.util.UUID;

public class ReportUfmRequest {
    private UUID examScheduleId;
    private UUID studentId;
    private String subjectName;
    private String contrabandType; // UNAUTHORIZED_NOTES, MOBILE_DEVICE, SMART_WATCH, IMPERSONATION
    private String invigilatorRemarks;
    private String studentStatement;

    public ReportUfmRequest() {}

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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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
}
