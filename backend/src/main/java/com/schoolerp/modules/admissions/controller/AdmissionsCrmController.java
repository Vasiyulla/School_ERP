package com.schoolerp.modules.admissions.controller;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.admissions.dto.*;
import com.schoolerp.modules.admissions.service.AdmissionsCrmService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admissions/crm")
@RequiredArgsConstructor
public class AdmissionsCrmController {

    private final AdmissionsCrmService crmService;

    private String getEffectiveTenantId() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "default";
    }

    @PostMapping("/walk-in/rapid-intake")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','FRONT_DESK')")
    public ResponseEntity<ApiResponse<AdmissionInquiryDTO>> rapidWalkInIntake(
            @RequestBody RapidWalkInIntakeRequest request) {
        AdmissionInquiryDTO inquiry = crmService.rapidWalkInIntake(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Physical walk-in inquiry recorded and counselor queue token issued", inquiry));
    }

    @PostMapping("/walk-in/{id}/spot-fee")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','ACCOUNTANT','FRONT_DESK')")
    public ResponseEntity<ApiResponse<AdmissionInquiryDTO>> collectSpotFee(
            @PathVariable UUID id,
            @RequestBody CollectSpotFeeRequest request) {
        AdmissionInquiryDTO inquiry = crmService.collectSpotFee(getEffectiveTenantId(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Spot prospectus registration fee collected successfully", inquiry));
    }

    @PostMapping("/inquiries")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionInquiryDTO>> createInquiry(
            @RequestBody CreateInquiryRequest request) {
        AdmissionInquiryDTO inquiry = crmService.createInquiry(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Admission inquiry created successfully", inquiry));
    }

    @GetMapping("/inquiries")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','FRONT_DESK')")
    public ResponseEntity<ApiResponse<List<AdmissionInquiryDTO>>> getInquiries(
            @RequestParam(required = false) String pipelineStage) {
        List<AdmissionInquiryDTO> list = crmService.getInquiries(getEffectiveTenantId(), pipelineStage);
        return ResponseEntity.ok(ApiResponse.ok("Admission inquiries pipeline retrieved", list));
    }

    @GetMapping("/inquiries/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','FRONT_DESK')")
    public ResponseEntity<ApiResponse<AdmissionInquiryDTO>> getInquiryById(
            @PathVariable UUID id) {
        AdmissionInquiryDTO inquiry = crmService.getInquiryById(getEffectiveTenantId(), id);
        return ResponseEntity.ok(ApiResponse.ok("Admission inquiry details retrieved", inquiry));
    }

    @PatchMapping("/inquiries/{id}/stage")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionInquiryDTO>> updatePipelineStage(
            @PathVariable UUID id,
            @RequestBody UpdateStageRequest request) {
        AdmissionInquiryDTO inquiry = crmService.updatePipelineStage(
                getEffectiveTenantId(),
                id,
                request.getNewStage(),
                request.getNotes(),
                request.getDropReason()
        );
        return ResponseEntity.ok(ApiResponse.ok("Inquiry pipeline stage transitioned", inquiry));
    }

    @PostMapping("/interview-slots")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionInterviewSlotDTO>> bookInterviewSlot(
            @RequestBody BookInterviewSlotRequest request) {
        AdmissionInterviewSlotDTO slot = crmService.bookInterviewSlot(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Admissions interview or campus tour slot booked", slot));
    }

    @PutMapping("/interview-slots/{id}/evaluate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','TEACHER')")
    public ResponseEntity<ApiResponse<AdmissionInterviewSlotDTO>> evaluateInterviewSlot(
            @PathVariable UUID id,
            @RequestBody EvaluateInterviewSlotRequest request) {
        AdmissionInterviewSlotDTO slot = crmService.evaluateInterviewSlot(getEffectiveTenantId(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Admissions interview rubric evaluated successfully", slot));
    }

    @PostMapping("/offers/issue")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionMeritOfferDTO>> issueMeritOffer(
            @RequestBody IssueOfferRequest request) {
        AdmissionMeritOfferDTO offer = crmService.issueMeritOffer(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Formal admission merit offer letter issued", offer));
    }

    @PostMapping("/offers/{id}/convert-to-student")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionMeritOfferDTO>> convertOfferToStudent(
            @PathVariable UUID id) {
        AdmissionMeritOfferDTO offer = crmService.convertToActiveStudent(getEffectiveTenantId(), id);
        return ResponseEntity.ok(ApiResponse.ok("Candidate converted to active student with initial invoice generated", offer));
    }

    @GetMapping("/analytics/funnel")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionsFunnelAnalyticsDTO>> getFunnelAnalytics() {
        AdmissionsFunnelAnalyticsDTO analytics = crmService.getFunnelAnalytics(getEffectiveTenantId());
        return ResponseEntity.ok(ApiResponse.ok("Admissions conversion funnel metrics compiled", analytics));
    }

    @GetMapping("/portal/tracking/{inquiryNumber}")
    public ResponseEntity<ApiResponse<ParentApplicationTrackingDTO>> getPublicTrackingStatus(
            @PathVariable String inquiryNumber,
            @RequestParam(required = false) String parentPhone) {
        ParentApplicationTrackingDTO tracking = crmService.getPublicTrackingStatus(getEffectiveTenantId(), inquiryNumber, parentPhone);
        return ResponseEntity.ok(ApiResponse.ok("Parent self-service application tracking details retrieved", tracking));
    }

    @PostMapping("/documents/{inquiryId}/upload")
    public ResponseEntity<ApiResponse<AdmissionDocumentChecklistDTO>> uploadChecklistDocument(
            @PathVariable UUID inquiryId,
            @RequestBody UploadDocumentRequest request) {
        AdmissionDocumentChecklistDTO doc = crmService.uploadChecklistDocument(getEffectiveTenantId(), inquiryId, request);
        return ResponseEntity.ok(ApiResponse.ok("Application checklist document uploaded successfully", doc));
    }

    @PutMapping("/documents/{documentId}/verify")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionDocumentChecklistDTO>> verifyChecklistDocument(
            @PathVariable UUID documentId,
            @RequestBody VerifyDocumentRequest request) {
        AdmissionDocumentChecklistDTO doc = crmService.verifyChecklistDocument(getEffectiveTenantId(), documentId, request);
        return ResponseEntity.ok(ApiResponse.ok("Application checklist document status verified", doc));
    }

    @GetMapping("/documents/{inquiryId}")
    public ResponseEntity<ApiResponse<List<AdmissionDocumentChecklistDTO>>> getChecklistDocuments(
            @PathVariable UUID inquiryId) {
        List<AdmissionDocumentChecklistDTO> list = crmService.getChecklistDocuments(getEffectiveTenantId(), inquiryId);
        return ResponseEntity.ok(ApiResponse.ok("Application checklist documents retrieved", list));
    }

    @PostMapping("/capacity/configure")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<GradeSeatCapacityDTO>> configureGradeCapacity(
            @RequestBody ConfigureCapacityRequest request) {
        GradeSeatCapacityDTO capacity = crmService.configureGradeCapacity(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Grade seat capacity and category quotas configured", capacity));
    }

    @GetMapping("/capacity/overview")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<GradeSeatCapacityDTO>>> getGradeCapacities(
            @RequestParam(required = false) String academicYear) {
        List<GradeSeatCapacityDTO> list = crmService.getGradeCapacities(getEffectiveTenantId(), academicYear);
        return ResponseEntity.ok(ApiResponse.ok("Admissions seat capacity matrix and waitlist metrics retrieved", list));
    }

    @PostMapping("/capacity/waitlist/{inquiryId}/promote")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionMeritOfferDTO>> promoteWaitlistApplicant(
            @PathVariable UUID inquiryId) {
        AdmissionMeritOfferDTO offer = crmService.promoteWaitlistApplicant(getEffectiveTenantId(), inquiryId);
        return ResponseEntity.ok(ApiResponse.ok("Waitlisted candidate promoted and merit offer letter issued", offer));
    }

    @PostMapping("/tasks")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<CounselorTaskDTO>> createCounselorTask(
            @RequestBody CreateCounselorTaskRequest request) {
        CounselorTaskDTO task = crmService.createCounselorTask(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Counselor follow-up task scheduled successfully", task));
    }

    @GetMapping("/tasks")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<List<CounselorTaskDTO>>> getCounselorTasks(
            @RequestParam(required = false) String counselorName,
            @RequestParam(required = false) String status) {
        List<CounselorTaskDTO> list = crmService.getCounselorTasks(getEffectiveTenantId(), counselorName, status);
        return ResponseEntity.ok(ApiResponse.ok("Admissions counselor tasks queue retrieved", list));
    }

    @PutMapping("/tasks/{id}/complete")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<CounselorTaskDTO>> completeCounselorTask(
            @PathVariable UUID id,
            @RequestBody CompleteCounselorTaskRequest request) {
        CounselorTaskDTO task = crmService.completeCounselorTask(getEffectiveTenantId(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Counselor follow-up task marked completed with outcome notes", task));
    }

    // =========================================================================
    // Indian Quota, 100-Point Criteria, Entrance Tests & Scholarship Subsystems (197-208)
    // =========================================================================

    @PostMapping("/criteria/evaluate-points")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionPointCriteriaDTO>> evaluate100PointCriteria(
            @RequestBody EvaluatePointsRequest request) {
        AdmissionPointCriteriaDTO criteria = crmService.evaluate100PointCriteria(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("100-Point DoE criteria evaluated and score calculated", criteria));
    }

    @PostMapping("/criteria/statutory-certificates")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<StatutoryCertificateDTO>> recordStatutoryCertificate(
            @RequestBody StatutoryCertificateRequest request) {
        StatutoryCertificateDTO cert = crmService.recordStatutoryCertificate(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Statutory caste/EWS/CWSN government certificate recorded", cert));
    }

    @GetMapping("/criteria/merit-list/{grade}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<MeritListResponseDTO>> getCategoryMeritList(
            @PathVariable String grade) {
        MeritListResponseDTO list = crmService.getCategoryMeritList(getEffectiveTenantId(), grade);
        return ResponseEntity.ok(ApiResponse.ok("Point-based criteria merit list generated successfully", list));
    }

    @PostMapping("/capacity/category-matrix")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL')")
    public ResponseEntity<ApiResponse<CategorySeatOverviewDTO>> configureCategoryMatrix(
            @RequestBody ConfigureCategoryMatrixRequest request) {
        CategorySeatOverviewDTO matrix = crmService.configureCategoryMatrix(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Statutory Indian quota seat matrix configured successfully", matrix));
    }

    @GetMapping("/capacity/category-overview/{grade}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<CategorySeatOverviewDTO>> getCategorySeatOverview(
            @PathVariable String grade) {
        CategorySeatOverviewDTO overview = crmService.getCategorySeatOverview(getEffectiveTenantId(), grade);
        return ResponseEntity.ok(ApiResponse.ok("Category-wise seat capacity and quota waitlist overview retrieved", overview));
    }

    @PostMapping("/capacity/category-waitlist/{quota}/promote")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionInquiryDTO>> promoteCategoryWaitlist(
            @PathVariable String quota,
            @RequestParam UUID inquiryId) {
        AdmissionInquiryDTO inquiry = crmService.promoteCategoryWaitlist(getEffectiveTenantId(), quota, inquiryId);
        return ResponseEntity.ok(ApiResponse.ok("Candidate promoted from " + quota + " category waitlist to merit offer stage", inquiry));
    }

    @PostMapping("/entrance-tests/schedule")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionEntranceTestDTO>> scheduleEntranceTest(
            @RequestBody ScheduleEntranceTestRequest request) {
        AdmissionEntranceTestDTO test = crmService.scheduleEntranceTest(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Entrance diagnostic assessment scheduled and admit card generated", test));
    }

    @GetMapping("/entrance-tests/admit-card/{inquiryNumber}")
    public ResponseEntity<ApiResponse<AdmitCardDTO>> getPublicAdmitCard(
            @PathVariable String inquiryNumber) {
        AdmitCardDTO admitCard = crmService.getPublicAdmitCard(getEffectiveTenantId(), inquiryNumber);
        return ResponseEntity.ok(ApiResponse.ok("Official entrance test hall ticket admit card retrieved", admitCard));
    }

    @PutMapping("/entrance-tests/{testId}/evaluate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','TEACHER')")
    public ResponseEntity<ApiResponse<AdmissionEntranceTestDTO>> evaluateEntranceTest(
            @PathVariable UUID testId,
            @RequestBody EvaluateEntranceTestRequest request) {
        AdmissionEntranceTestDTO test = crmService.evaluateEntranceTest(getEffectiveTenantId(), testId, request);
        return ResponseEntity.ok(ApiResponse.ok("Entrance test evaluated and qualification cut-off determined", test));
    }

    @PostMapping("/inquiries/{id}/link-sibling")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<SiblingConcessionPreviewDTO>> linkVerifiedSibling(
            @PathVariable UUID id,
            @RequestBody LinkSiblingRequest request) {
        SiblingConcessionPreviewDTO preview = crmService.linkVerifiedSibling(getEffectiveTenantId(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Verified student sibling linked with priority score boost and fee concession forecast", preview));
    }

    @PostMapping("/scholarships/apply")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<ScholarshipApplicationDTO>> applyForScholarship(
            @RequestBody ApplyScholarshipRequest request) {
        ScholarshipApplicationDTO app = crmService.applyForScholarship(getEffectiveTenantId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Scholarship and financial aid committee application submitted", app));
    }

    @PostMapping("/portal/offer/{offerId}/accept-and-lock")
    public ResponseEntity<ApiResponse<OfferAcceptanceResultDTO>> acceptOfferAndLockSeat(
            @PathVariable UUID offerId,
            @RequestBody AcceptOfferAndLockRequest request) {
        OfferAcceptanceResultDTO result = crmService.acceptOfferAndLockSeat(getEffectiveTenantId(), offerId, request);
        return ResponseEntity.ok(ApiResponse.ok("Admission merit offer formally accepted and seat locked with token deposit", result));
    }

    @Data
    public static class UpdateStageRequest {
        private String newStage;
        private String notes;
        private String dropReason;
    }
}

