package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.dto.FinanceGovernanceDTOs;
import com.schoolerp.modules.finance.entity.ConcessionApprovalRequest;
import com.schoolerp.modules.finance.service.ConcessionWorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finance/concessions/workflow")
@RequiredArgsConstructor
public class ConcessionWorkflowController {

    private final ConcessionWorkflowService concessionService;

    @PostMapping("/submit")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT') or hasRole('ADMISSION_OFFICER')")
    public ResponseEntity<ApiResponse<ConcessionApprovalRequest>> submitConcession(
            @RequestBody FinanceGovernanceDTOs.SubmitConcessionRequest request,
            Principal principal) {
        String maker = request != null && request.getMakerUserId() != null && !request.getMakerUserId().isBlank()
                ? request.getMakerUserId()
                : (principal != null ? principal.getName() : "accountant");
        ConcessionApprovalRequest result = concessionService.submitConcession(request, maker);
        return ResponseEntity.ok(ApiResponse.ok("Concession request docket submitted for review", result));
    }

    @PutMapping("/{id}/authorize")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ConcessionApprovalRequest>> authorizeConcession(
            @PathVariable UUID id,
            @RequestBody FinanceGovernanceDTOs.AuthorizeConcessionRequest request,
            Principal principal) {
        String checker = principal != null ? principal.getName() : "principal";
        ConcessionApprovalRequest result = concessionService.authorizeConcession(id, request, checker);
        return ResponseEntity.ok(ApiResponse.ok("Concession authorization decision recorded", result));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<ConcessionApprovalRequest>>> getPendingConcessions() {
        List<ConcessionApprovalRequest> list = concessionService.getPendingConcessions();
        return ResponseEntity.ok(ApiResponse.ok("Pending concession dockets retrieved", list));
    }
}
