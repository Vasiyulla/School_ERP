package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.dto.FinanceGovernanceDTOs;
import com.schoolerp.modules.finance.entity.PostDatedCheque;
import com.schoolerp.modules.finance.service.PdcVaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finance/pdc")
@RequiredArgsConstructor
public class PdcVaultController {

    private final PdcVaultService pdcVaultService;

    @PostMapping("/deposit-vault")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<PostDatedCheque>> depositPdc(
            @RequestBody FinanceGovernanceDTOs.DepositPdcRequest request) {
        PostDatedCheque pdc = pdcVaultService.depositPdc(request);
        return ResponseEntity.ok(ApiResponse.ok("Post-dated cheque registered in vault", pdc));
    }

    @GetMapping("/maturity-schedule")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<FinanceGovernanceDTOs.PdcMaturityScheduleDTO>> getMaturitySchedule() {
        FinanceGovernanceDTOs.PdcMaturityScheduleDTO schedule = pdcVaultService.getMaturitySchedule();
        return ResponseEntity.ok(ApiResponse.ok("PDC maturity schedule retrieved", schedule));
    }

    @PutMapping("/{id}/clear")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<PostDatedCheque>> clearPdc(@PathVariable UUID id) {
        PostDatedCheque pdc = pdcVaultService.clearPdc(id);
        return ResponseEntity.ok(ApiResponse.ok("Post-dated cheque cleared and deposited", pdc));
    }
}
