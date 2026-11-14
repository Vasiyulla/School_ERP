package com.schoolerp.modules.finance.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.finance.dto.CautionMoneyRefundResultDTO;
import com.schoolerp.modules.finance.dto.ProcessCautionMoneyRefundRequest;
import com.schoolerp.modules.finance.service.CautionMoneyRefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/finance/caution-money")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Caution Money & Refund Clearance Desk", description = "Refundable security deposit custody, fee/damage deductions, and statutory TC departure refund vouchers")
public class CautionMoneyController {

    private final CautionMoneyRefundService refundService;

    @PostMapping("/refund")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ACCOUNTANT')")
    @Operation(summary = "Process student departure caution money refund voucher with breakage and fee offset adjustments")
    public ResponseEntity<ApiResponse<CautionMoneyRefundResultDTO>> processRefund(
            @Valid @RequestBody ProcessCautionMoneyRefundRequest request) {
        CautionMoneyRefundResultDTO result = refundService.processDepartureRefund(request);
        return ResponseEntity.ok(ApiResponse.ok("Caution money refund voucher processed successfully", result));
    }
}

