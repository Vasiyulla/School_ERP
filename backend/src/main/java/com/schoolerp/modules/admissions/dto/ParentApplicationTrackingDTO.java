package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentApplicationTrackingDTO {
    private UUID inquiryId;
    private String inquiryNumber;
    private String tokenNumber;
    private String studentName;
    private String parentName;
    private String parentPhone;
    private String gradeApplyingFor;
    private String academicYear;
    private String pipelineStage;
    private int stageProgressPercent;
    private int totalRequiredDocs;
    private int verifiedDocsCount;
    private List<AdmissionDocumentChecklistDTO> checklistDocuments;
    private AdmissionMeritOfferDTO offerDetails;
    private List<AdmissionInterviewSlotDTO> interviewSlots;
}

