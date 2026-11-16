package com.schoolerp.modules.academics.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class DailySubstitutionDTO {
    private UUID id;
    private LocalDate substitutionDate;
    private Integer periodNumber;
    private UUID classroomId;
    private String className;
    private String sectionName;
    private UUID absentTeacherId;
    private String absentTeacherName;
    private UUID substituteTeacherId;
    private String substituteTeacherName;
    private String subjectName;
    private String lessonHandoffBrief;
    private String status;
    private LocalDateTime createdAt;

    public DailySubstitutionDTO() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getSubstitutionDate() {
        return substitutionDate;
    }

    public void setSubstitutionDate(LocalDate substitutionDate) {
        this.substitutionDate = substitutionDate;
    }

    public Integer getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(Integer periodNumber) {
        this.periodNumber = periodNumber;
    }

    public UUID getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(UUID classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public UUID getAbsentTeacherId() {
        return absentTeacherId;
    }

    public void setAbsentTeacherId(UUID absentTeacherId) {
        this.absentTeacherId = absentTeacherId;
    }

    public String getAbsentTeacherName() {
        return absentTeacherName;
    }

    public void setAbsentTeacherName(String absentTeacherName) {
        this.absentTeacherName = absentTeacherName;
    }

    public UUID getSubstituteTeacherId() {
        return substituteTeacherId;
    }

    public void setSubstituteTeacherId(UUID substituteTeacherId) {
        this.substituteTeacherId = substituteTeacherId;
    }

    public String getSubstituteTeacherName() {
        return substituteTeacherName;
    }

    public void setSubstituteTeacherName(String substituteTeacherName) {
        this.substituteTeacherName = substituteTeacherName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getLessonHandoffBrief() {
        return lessonHandoffBrief;
    }

    public void setLessonHandoffBrief(String lessonHandoffBrief) {
        this.lessonHandoffBrief = lessonHandoffBrief;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
