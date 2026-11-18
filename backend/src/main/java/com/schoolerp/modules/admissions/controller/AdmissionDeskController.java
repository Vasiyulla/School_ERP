package com.schoolerp.modules.admissions.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.admissions.dto.*;
import com.schoolerp.modules.admissions.service.AdmissionDeskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admissions/desk")
@RequiredArgsConstructor
public class AdmissionDeskController {

    private final AdmissionDeskService deskService;

    // =========================================================================
    // 1. FRONT-DESK SMART QUEUE & COUNTER CALLING DESK
    // =========================================================================

    @PostMapping("/queue/dispense")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','FRONT_DESK')")
    public ResponseEntity<ApiResponse<AdmissionQueueTokenDTO>> dispenseQueueToken(
            @Valid @RequestBody DispenseQueueTokenRequest request) {
        AdmissionQueueTokenDTO token = deskService.dispenseQueueToken(request);
        return ResponseEntity.ok(ApiResponse.ok("Front-desk queue token dispensed successfully", token));
    }

    @PostMapping("/queue/call-next")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','FRONT_DESK')")
    public ResponseEntity<ApiResponse<AdmissionQueueTokenDTO>> callNextToken(
            @Valid @RequestBody CallNextTokenRequest request) {
        AdmissionQueueTokenDTO token = deskService.callNextToken(request);
        return ResponseEntity.ok(ApiResponse.ok("Next queue token called to counter", token));
    }

    @PutMapping("/queue/{id}/complete")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','FRONT_DESK')")
    public ResponseEntity<ApiResponse<AdmissionQueueTokenDTO>> completeToken(
            @PathVariable UUID id,
            @RequestBody(required = false) CompleteTokenRequest request) {
        AdmissionQueueTokenDTO token = deskService.completeToken(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Queue token service completed", token));
    }

    @GetMapping("/queue/live-board")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','FRONT_DESK')")
    public ResponseEntity<ApiResponse<LiveQueueBoardDTO>> getLiveQueueBoard() {
        LiveQueueBoardDTO board = deskService.getLiveQueueBoard();
        return ResponseEntity.ok(ApiResponse.ok("Live queue display board retrieved", board));
    }

    // =========================================================================
    // 2. SERIALIZED PROSPECTUS KIT INVENTORY & SALES REGISTER
    // =========================================================================

    @PostMapping("/prospectus/add-stock")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<ProspectusKitMasterDTO>>> addProspectusStock(
            @Valid @RequestBody AddProspectusStockRequest request) {
        List<ProspectusKitMasterDTO> kits = deskService.addProspectusStock(request);
        return ResponseEntity.ok(ApiResponse.ok("Prospectus kit inventory batch ingested successfully", kits));
    }

    @PostMapping("/prospectus/sell-kit")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','ACCOUNTANT','FRONT_DESK')")
    public ResponseEntity<ApiResponse<ProspectusKitMasterDTO>> sellProspectusKit(
            @Valid @RequestBody SellProspectusKitRequest request) {
        ProspectusKitMasterDTO kit = deskService.sellProspectusKit(request);
        return ResponseEntity.ok(ApiResponse.ok("Prospectus kit point-of-sale recorded and inquiry linked", kit));
    }

    @GetMapping("/prospectus/inventory-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','ACCOUNTANT','FRONT_DESK')")
    public ResponseEntity<ApiResponse<ProspectusInventorySummaryDTO>> getProspectusInventorySummary() {
        ProspectusInventorySummaryDTO summary = deskService.getProspectusInventorySummary();
        return ResponseEntity.ok(ApiResponse.ok("Prospectus kit inventory and revenue summary retrieved", summary));
    }

    // =========================================================================
    // 3. DOCUMENT VERIFICATION & DEFICIENT DOCUMENT UNDERTAKING DESK
    // =========================================================================

    @PostMapping("/undertakings/issue")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionDocumentUndertakingDTO>> issueUndertaking(
            @Valid @RequestBody IssueUndertakingRequest request) {
        AdmissionDocumentUndertakingDTO undertaking = deskService.issueUndertaking(request);
        return ResponseEntity.ok(ApiResponse.ok("Deficient document undertaking bond issued successfully", undertaking));
    }

    @PutMapping("/undertakings/{id}/fulfill")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionDocumentUndertakingDTO>> fulfillUndertaking(
            @PathVariable UUID id,
            @RequestBody(required = false) FulfillUndertakingRequest request) {
        AdmissionDocumentUndertakingDTO undertaking = deskService.fulfillUndertaking(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Document undertaking fulfilled and status verified", undertaking));
    }

    @GetMapping("/undertakings/overdue-sentinel")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<List<AdmissionDocumentUndertakingDTO>>> getOverdueSentinels() {
        List<AdmissionDocumentUndertakingDTO> sentinels = deskService.getOverdueSentinels();
        return ResponseEntity.ok(ApiResponse.ok("Pending document undertakings and overdue sentinels retrieved", sentinels));
    }

    // =========================================================================
    // 4. INSTANT STUDENT IDENTITY MINTING & WELCOME DOSSIER
    // =========================================================================

    @PostMapping("/onboarding/mint-identity")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionWelcomeDossierDTO>> mintIdentity(
            @Valid @RequestBody MintIdentityRequest request) {
        AdmissionWelcomeDossierDTO dossier = deskService.mintStudentIdentity(request);
        return ResponseEntity.ok(ApiResponse.ok("Permanent student admission number and section identity minted", dossier));
    }

    @GetMapping("/onboarding/welcome-dossier/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','FRONT_DESK')")
    public ResponseEntity<ApiResponse<AdmissionWelcomeDossierDTO>> getWelcomeDossier(
            @PathVariable UUID studentId) {
        AdmissionWelcomeDossierDTO dossier = deskService.getWelcomeDossierByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.ok("Student welcome pack dossier retrieved", dossier));
    }

    @PostMapping("/onboarding/smart-card/serialize")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<AdmissionWelcomeDossierDTO>> serializeSmartCard(
            @RequestParam UUID studentId,
            @Valid @RequestBody SerializeSmartCardRequest request) {
        AdmissionWelcomeDossierDTO dossier = deskService.serializeSmartCard(studentId, request);
        return ResponseEntity.ok(ApiResponse.ok("Campus smart RFID/NFC card serialized successfully", dossier));
    }

    // =========================================================================
    // 5. SEAT SURRENDER, CANCELLATION & STATUTORY FEE REFUND ENGINE
    // =========================================================================

    @PostMapping("/surrender/calculate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SurrenderRefundCalculationDTO>> calculateSurrenderRefund(
            @Valid @RequestBody CalculateSurrenderRefundRequest request) {
        SurrenderRefundCalculationDTO calc = deskService.calculateSurrenderRefund(request);
        return ResponseEntity.ok(ApiResponse.ok("Statutory seat surrender refund schedule calculated", calc));
    }

    @PostMapping("/surrender/process")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ACCOUNTANT')")
    public ResponseEntity<ApiResponse<AdmissionSeatSurrenderDTO>> processSeatSurrender(
            @Valid @RequestBody ProcessSeatSurrenderRequest request) {
        AdmissionSeatSurrenderDTO surrender = deskService.processSeatSurrender(request);
        return ResponseEntity.ok(ApiResponse.ok("Seat surrender processed, GL voucher issued, and waitlist promoted", surrender));
    }

    @GetMapping("/surrender/audit-ledger")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<AdmissionSeatSurrenderDTO>>> getSurrenderAuditLedger() {
        List<AdmissionSeatSurrenderDTO> ledger = deskService.getSurrenderAuditLedger();
        return ResponseEntity.ok(ApiResponse.ok("Surrendered seats audit ledger retrieved", ledger));
    }
}
