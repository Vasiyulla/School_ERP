package com.schoolerp.modules.students.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class StudentHealthPassportDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String studentAdmissionNumber;
    private String bloodGroup;
    private Boolean hasSevereAllergies;
    private String allergenList;
    private String chronicConditions;
    private Boolean inhalerInInfirmary;
    private Boolean epipenPrescribed;
    private String emergencyContactPhone;
    private String treatingPhysicianName;
    private String paramedicEmergencyProtocol;
    private LocalDateTime createdAt;

    public StudentHealthPassportDTO() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getStudentAdmissionNumber() {
        return studentAdmissionNumber;
    }

    public void setStudentAdmissionNumber(String studentAdmissionNumber) {
        this.studentAdmissionNumber = studentAdmissionNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Boolean getHasSevereAllergies() {
        return hasSevereAllergies;
    }

    public void setHasSevereAllergies(Boolean hasSevereAllergies) {
        this.hasSevereAllergies = hasSevereAllergies;
    }

    public String getAllergenList() {
        return allergenList;
    }

    public void setAllergenList(String allergenList) {
        this.allergenList = allergenList;
    }

    public String getChronicConditions() {
        return chronicConditions;
    }

    public void setChronicConditions(String chronicConditions) {
        this.chronicConditions = chronicConditions;
    }

    public Boolean getInhalerInInfirmary() {
        return inhalerInInfirmary;
    }

    public void setInhalerInInfirmary(Boolean inhalerInInfirmary) {
        this.inhalerInInfirmary = inhalerInInfirmary;
    }

    public Boolean getEpipenPrescribed() {
        return epipenPrescribed;
    }

    public void setEpipenPrescribed(Boolean epipenPrescribed) {
        this.epipenPrescribed = epipenPrescribed;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getTreatingPhysicianName() {
        return treatingPhysicianName;
    }

    public void setTreatingPhysicianName(String treatingPhysicianName) {
        this.treatingPhysicianName = treatingPhysicianName;
    }

    public String getParamedicEmergencyProtocol() {
        return paramedicEmergencyProtocol;
    }

    public void setParamedicEmergencyProtocol(String paramedicEmergencyProtocol) {
        this.paramedicEmergencyProtocol = paramedicEmergencyProtocol;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
