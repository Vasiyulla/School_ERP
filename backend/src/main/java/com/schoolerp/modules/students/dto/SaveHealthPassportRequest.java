package com.schoolerp.modules.students.dto;

public class SaveHealthPassportRequest {
    private String bloodGroup;
    private Boolean hasSevereAllergies;
    private String allergenList; // Comma-separated: "PEANUTS,GLUTEN,DAIRY"
    private String chronicConditions; // "ASTHMA,TYPE_1_DIABETES"
    private Boolean inhalerInInfirmary;
    private Boolean epipenPrescribed;
    private String emergencyContactPhone;
    private String treatingPhysicianName;
    private String paramedicEmergencyProtocol;

    public SaveHealthPassportRequest() {}

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
}
