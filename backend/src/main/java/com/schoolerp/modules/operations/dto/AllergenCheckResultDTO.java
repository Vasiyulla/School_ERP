package com.schoolerp.modules.operations.dto;

import java.util.List;
import java.util.UUID;

public class AllergenCheckResultDTO {
    private Boolean allowed;
    private UUID studentId;
    private String studentName;
    private String bloodGroup;
    private Boolean epiPenRequired;
    private List<String> matchedAllergens;
    private String warningMessage;
    private String emergencyProtocol;

    public AllergenCheckResultDTO() {}

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
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

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Boolean getEpiPenRequired() {
        return epiPenRequired;
    }

    public void setEpiPenRequired(Boolean epiPenRequired) {
        this.epiPenRequired = epiPenRequired;
    }

    public List<String> getMatchedAllergens() {
        return matchedAllergens;
    }

    public void setMatchedAllergens(List<String> matchedAllergens) {
        this.matchedAllergens = matchedAllergens;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public String getEmergencyProtocol() {
        return emergencyProtocol;
    }

    public void setEmergencyProtocol(String emergencyProtocol) {
        this.emergencyProtocol = emergencyProtocol;
    }
}
