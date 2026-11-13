package com.schoolerp.modules.communication.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.communication.dto.ReviewGrievanceRequest;
import com.schoolerp.modules.communication.dto.SubmitGrievanceRequest;
import com.schoolerp.modules.communication.entity.GrievanceTicket;
import com.schoolerp.modules.communication.service.GrievanceOmbudsmanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/communication/grievances")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Parent Grievance & Escalation Ombudsman Desk", description = "3-tier parent grievance lifecycle with 48h SLA timer and auto-escalation to the Principal's Desk")
public class GrievanceController {

    private final GrievanceOmbudsmanService grievanceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('PARENT', 'STUDENT', 'TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Submit a parent or student grievance ticket with automatic 48h SLA deadline")
    public ResponseEntity<ApiResponse<GrievanceTicket>> submitGrievance(
            @Valid @RequestBody SubmitGrievanceRequest request,
            Authentication auth) {
        String submitter = (auth != null) ? auth.getName() : "Parent / Guardian";
        GrievanceTicket ticket = grievanceService.submitGrievance(request, submitter);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Grievance registered with 48-hour SLA tracking", ticket));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'PARENT')")
    @Operation(summary = "List grievances with status, tier, or Principal Urgent Attention filter")
    public ResponseEntity<ApiResponse<List<GrievanceTicket>>> getGrievances(
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(required = false, defaultValue = "ALL") String tier,
            @RequestParam(required = false) Boolean principalAttentionOnly) {
        List<GrievanceTicket> list = grievanceService.getGrievances(status, tier, principalAttentionOnly);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PutMapping("/{ticketId}/review")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Review, resolve, or escalate grievance to HOD or Principal Ombudsman Desk")
    public ResponseEntity<ApiResponse<GrievanceTicket>> reviewGrievance(
            @PathVariable UUID ticketId,
            @Valid @RequestBody ReviewGrievanceRequest request,
            Authentication auth) {
        String reviewer = (auth != null) ? auth.getName() : "Administrative Staff";
        GrievanceTicket updated = grievanceService.reviewOrEscalate(ticketId, request, reviewer);
        return ResponseEntity.ok(ApiResponse.ok("Grievance ticket updated", updated));
    }
}

