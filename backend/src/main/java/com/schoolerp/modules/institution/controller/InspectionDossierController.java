package com.schoolerp.modules.institution.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.institution.dto.InspectionDossierDTO;
import com.schoolerp.modules.institution.service.InspectionDossierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/institution/inspection-dossier")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CBSE & Accreditation Inspection Audit Dossier", description = "1-click statutory compliance compilation: Fire, Building, Water, RTE, Teacher Qualifications, and Lab Apparatus readiness")
public class InspectionDossierController {

    private final InspectionDossierService dossierService;

    @GetMapping("/generate")
    @PreAuthorize("hasAnyRole('PRINCIPAL', 'SUPER_ADMIN')")
    @Operation(summary = "Generate comprehensive CBSE and Accreditation Inspection Readiness Dossier")
    public ResponseEntity<ApiResponse<InspectionDossierDTO>> generateDossier() {
        InspectionDossierDTO dossier = dossierService.generateDossier();
        return ResponseEntity.ok(ApiResponse.ok(dossier));
    }

    @GetMapping(value = "/print", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyRole('PRINCIPAL', 'SUPER_ADMIN')")
    @Operation(summary = "Render official print-ready CBSE Inspection Readiness Audit Report HTML")
    public ResponseEntity<String> printDossier() {
        String htmlDossier = dossierService.generatePrintableDossierHtml();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlDossier);
    }
}

