package com.schoolerp.modules.admissions.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.admissions.entity.RteApplicationRecord;
import com.schoolerp.modules.admissions.service.RteQuotaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admissions/rte")
@RequiredArgsConstructor
public class RteQuotaController {

    private final RteQuotaService rteQuotaService;

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER')")
    @RequireFeature("admissions.rte_quota")
    public ResponseEntity<ApiResponse<RteApplicationRecord>> registerCandidate(@RequestBody RegisterRteRequest request) {
        RteApplicationRecord record = rteQuotaService.registerRteCandidate(
                request.getStudentName(),
                request.getAdmissionNumber(),
                request.getGradeApplied(),
                request.getLotteryApplicationNo(),
                request.getIncomeCertificateNo(),
                request.getAnnualHouseholdIncome(),
                request.getCategory()
        );
        return ResponseEntity.ok(ApiResponse.success(record, "RTE 25% Quota student registered successfully with fee waiver"));
    }

    @GetMapping("/records")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ADMISSION_OFFICER','ACCOUNTANT')")
    @RequireFeature("admissions.rte_quota")
    public ResponseEntity<ApiResponse<List<RteApplicationRecord>>> getRecords() {
        return ResponseEntity.ok(ApiResponse.success(rteQuotaService.getRteRecords(), "RTE 25% Quota enrollment roster retrieved"));
    }

    @GetMapping("/claims/annual")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','ACCOUNTANT')")
    @RequireFeature("admissions.rte_quota")
    public ResponseEntity<ApiResponse<RteQuotaService.RteAnnualClaimReport>> getAnnualClaim(
            @RequestParam(required = false, defaultValue = "2026-2027") String academicYear) {
        return ResponseEntity.ok(ApiResponse.success(rteQuotaService.generateAnnualReimbursementClaim(academicYear), "State Government RTE reimbursement claim compiled"));
    }

    @Data
    public static class RegisterRteRequest {
        private String studentName;
        private String admissionNumber;
        private String gradeApplied;
        private String lotteryApplicationNo;
        private String incomeCertificateNo;
        private BigDecimal annualHouseholdIncome;
        private String category;
    }
}

