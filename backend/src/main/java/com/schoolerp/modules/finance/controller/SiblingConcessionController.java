package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.dto.ConfigureConcessionPolicyRequest;
import com.schoolerp.modules.finance.dto.EvaluateFamilyConcessionRequest;
import com.schoolerp.modules.finance.dto.EvaluateFamilyConcessionResultDTO;
import com.schoolerp.modules.finance.entity.SiblingConcessionPolicy;
import com.schoolerp.modules.finance.service.SiblingConcessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance/concessions")
@RequiredArgsConstructor
public class SiblingConcessionController {

    private final SiblingConcessionService concessionService;

    @PostMapping("/configure-policy")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SiblingConcessionPolicy>> configurePolicy(
            @RequestBody ConfigureConcessionPolicyRequest request) {
        SiblingConcessionPolicy policy = concessionService.configurePolicy(request);
        return ResponseEntity.ok(ApiResponse.ok("Sibling concession tier policy configured successfully", policy));
    }

    @PostMapping("/evaluate-family")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ACCOUNTANT', 'ADMISSIONS')")
    public ResponseEntity<ApiResponse<EvaluateFamilyConcessionResultDTO>> evaluateFamily(
            @RequestBody EvaluateFamilyConcessionRequest request) {
        EvaluateFamilyConcessionResultDTO result = concessionService.evaluateFamily(request);
        return ResponseEntity.ok(ApiResponse.ok("Family sibling concessions evaluated successfully", result));
    }
}
