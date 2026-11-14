package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.dto.ApplyDaRevisionRequest;
import com.schoolerp.modules.staff.dto.DaRevisionResultDTO;
import com.schoolerp.modules.staff.entity.CpcPayMatrix;
import com.schoolerp.modules.staff.service.CpcSalaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff/payroll/cpc")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "7th CPC Faculty Pay Matrix & DA Revisions", description = "CBSE Bye-Law 5.3 compliant 7th Central Pay Commission scales, bi-annual DA % updates, and Metro HRA calculator")
public class CpcSalaryController {

    private final CpcSalaryService cpcSalaryService;

    @GetMapping("/matrix")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Get official 7th CPC Pay Matrix bands (PRT, TGT, PGT, VP, Principal)")
    public ResponseEntity<ApiResponse<List<CpcPayMatrix>>> getPayMatrix() {
        List<CpcPayMatrix> list = cpcSalaryService.getOrCreateCpcMatrix();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping("/apply-da-revision")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Apply school-wide bi-annual 7th CPC Dearness Allowance (DA %) revision")
    public ResponseEntity<ApiResponse<DaRevisionResultDTO>> applyDaRevision(
            @Valid @RequestBody(required = false) ApplyDaRevisionRequest request) {
        DaRevisionResultDTO result = cpcSalaryService.applySchoolWideDaRevision(request);
        return ResponseEntity.ok(ApiResponse.ok("School-wide 7th CPC DA revision applied successfully", result));
    }
}

