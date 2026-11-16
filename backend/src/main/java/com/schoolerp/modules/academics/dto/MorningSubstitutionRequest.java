package com.schoolerp.modules.academics.dto;

import java.time.LocalDate;
import java.util.UUID;

public class MorningSubstitutionRequest {
    private LocalDate substitutionDate;
    private UUID absentTeacherId;
    private UUID sectionId;
    private Integer periodNumber;
    private String subjectName;
    private Integer maxDailySubstitutionsPerTeacher;

    public MorningSubstitutionRequest() {}

    public LocalDate getSubstitutionDate() {
        return substitutionDate;
    }

    public void setSubstitutionDate(LocalDate substitutionDate) {
        this.substitutionDate = substitutionDate;
    }

    public UUID getAbsentTeacherId() {
        return absentTeacherId;
    }

    public void setAbsentTeacherId(UUID absentTeacherId) {
        this.absentTeacherId = absentTeacherId;
    }

    public UUID getSectionId() {
        return sectionId;
    }

    public void setSectionId(UUID sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(Integer periodNumber) {
        this.periodNumber = periodNumber;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getMaxDailySubstitutionsPerTeacher() {
        return maxDailySubstitutionsPerTeacher;
    }

    public void setMaxDailySubstitutionsPerTeacher(Integer maxDailySubstitutionsPerTeacher) {
        this.maxDailySubstitutionsPerTeacher = maxDailySubstitutionsPerTeacher;
    }
}
