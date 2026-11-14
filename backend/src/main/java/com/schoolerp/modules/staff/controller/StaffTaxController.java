package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.dto.Form16PartBDTO;
import com.schoolerp.modules.staff.dto.SubmitTaxDeclarationRequest;
import com.schoolerp.modules.staff.entity.StaffTaxDeclaration;
import com.schoolerp.modules.staff.service.StaffTaxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/staff/tax")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Faculty Income Tax Section 192 & Form 16", description = "Indian Income Tax compliance: Old vs New Section 115BAC regime, ₹75,000 standard deduction, and Form 16 Part B generator")
public class StaffTaxController {

    private final StaffTaxService staffTaxService;

    @PostMapping("/declaration")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'ACCOUNTANT', 'TEACHER')")
    @Operation(summary = "Submit employee tax regime declaration and compute dynamic monthly TDS deduction")
    public ResponseEntity<ApiResponse<StaffTaxDeclaration>> submitDeclaration(
            @Valid @RequestBody SubmitTaxDeclarationRequest request) {
        StaffTaxDeclaration declaration = staffTaxService.submitDeclaration(request);
        return ResponseEntity.ok(ApiResponse.ok("Tax declaration submitted and TDS projection computed", declaration));
    }

    @GetMapping("/form16/{employeeId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'ACCOUNTANT', 'TEACHER')")
    @Operation(summary = "Generate official Form 16 Part B annual salary and tax deduction certificate")
    public ResponseEntity<ApiResponse<Form16PartBDTO>> getForm16PartB(@PathVariable UUID employeeId) {
        Form16PartBDTO form16 = staffTaxService.generateForm16PartB(employeeId);
        return ResponseEntity.ok(ApiResponse.ok("Form 16 Part B generated successfully", form16));
    }
}

