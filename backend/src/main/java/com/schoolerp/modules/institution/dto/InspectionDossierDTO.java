package com.schoolerp.modules.institution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionDossierDTO {

    private String schoolName;
    private String affiliationNumber;
    private String boardName;
    private String academicYear;
    private String inspectionReadinessStatus; // FULL_COMPLIANCE, SUBSTANTIAL_COMPLIANCE, DEFICIENCIES_DETECTED

    private long totalEnrolledStudents;
    private long totalQualifiedTeachers;
    private String teacherPupilRatio;

    private boolean buildingSafetyNocValid;
    private boolean fireSafetyNocValid;
    private boolean cleanWaterAndSanitationValid;

    private int totalLibraryBookTitles;
    private int totalScienceAndStemLabApparatusCount;
    private long rteQuotaEnrollmentCount;

    private List<ComplianceCheckItem> mandatoryDisclosures;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComplianceCheckItem {
        private String statutoryParameter; // e.g. "Fire Safety NOC Certificate", "Building Structural Stability"
        private String issuingAuthority;
        private String status; // COMPLIANT, EXPIRING_SOON, NON_COMPLIANT
        private String validityNotes;
    }
}

