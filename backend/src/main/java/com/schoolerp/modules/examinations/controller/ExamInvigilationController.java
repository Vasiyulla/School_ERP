package com.schoolerp.modules.examinations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.examinations.dto.*;
import com.schoolerp.modules.examinations.service.ExamInvigilationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/examinations/invigilation")
@RequiredArgsConstructor
public class ExamInvigilationController {

    private final ExamInvigilationService examInvigilationService;

    @PostMapping("/auto-schedule")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('EXAM_CONTROLLER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExamInvigilationDutyDTO>> autoScheduleDuty(
            @RequestBody ScheduleInvigilationRequest request) {
        ExamInvigilationDutyDTO duty = examInvigilationService.scheduleInvigilation(request);
        return ResponseEntity.ok(ApiResponse.ok("Exam invigilation duty scheduled with anti-cheating conflict check", duty));
    }

    @PutMapping("/{id}/verify-seals")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('EXAM_CONTROLLER') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExamInvigilationDutyDTO>> verifySeals(
            @PathVariable UUID id,
            @RequestBody VerifySealsRequest request) {
        ExamInvigilationDutyDTO duty = examInvigilationService.verifySeals(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Exam packet security tape barcode and seals verified", duty));
    }

    @PostMapping("/ufm/report")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('EXAM_CONTROLLER') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UfmMalpracticeDocketDTO>> reportMalpractice(
            @RequestBody ReportUfmRequest request) {
        UfmMalpracticeDocketDTO docket = examInvigilationService.reportUfm(request);
        return ResponseEntity.ok(ApiResponse.ok("Unfair Means (UFM) Malpractice Docket logged and queued for committee hearing", docket));
    }

    @GetMapping("/ufm/dockets")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('EXAM_CONTROLLER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UfmMalpracticeDocketDTO>>> getUfmDockets(
            @RequestParam(required = false) String status) {
        List<UfmMalpracticeDocketDTO> dockets = examInvigilationService.getUfmDockets(status);
        return ResponseEntity.ok(ApiResponse.ok("UFM Malpractice Dockets retrieved", dockets));
    }
}
