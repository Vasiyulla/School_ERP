package com.schoolerp.modules.students.controller;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.service.StudentExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/students/export")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Export & Printable Roster Engine", description = "Admin class data exports in Excel (.xlsx), CSV, and Print-Ready Roster formats")
public class StudentExportController {

    private final StudentExportService studentExportService;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @GetMapping("/excel")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'TEACHER')")
    @Operation(summary = "Export class student records in Microsoft Excel (.xlsx) format")
    public ResponseEntity<byte[]> exportClassExcel(
            @RequestParam(required = false, defaultValue = "ALL") String className,
            @RequestParam(required = false, defaultValue = "ALL") String sectionName) throws IOException {

        String tenantId = resolveTenant();
        byte[] excelBytes = studentExportService.generateExcelExport(tenantId, className, sectionName);

        String safeClass = className.replaceAll("[^a-zA-Z0-9_-]", "_");
        String safeSection = sectionName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String filename = String.format("Class_%s_%s_%s.xlsx", safeClass, safeSection, LocalDate.now());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

    @GetMapping("/csv")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'TEACHER')")
    @Operation(summary = "Stream class student records in RFC 4180 CSV format with UTF-8 BOM")
    public ResponseEntity<String> exportClassCsv(
            @RequestParam(required = false, defaultValue = "ALL") String className,
            @RequestParam(required = false, defaultValue = "ALL") String sectionName) {

        String tenantId = resolveTenant();
        String csvData = studentExportService.generateCsvExport(tenantId, className, sectionName);

        String safeClass = className.replaceAll("[^a-zA-Z0-9_-]", "_");
        String safeSection = sectionName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String filename = String.format("Class_%s_%s_%s.csv", safeClass, safeSection, LocalDate.now());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvData);
    }

    @GetMapping(value = "/printable-roster", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'TEACHER')")
    @Operation(summary = "Render a high-contrast print-ready classroom roster with signature blocks")
    public ResponseEntity<String> getPrintableRoster(
            @RequestParam(required = false, defaultValue = "ALL") String className,
            @RequestParam(required = false, defaultValue = "ALL") String sectionName) {

        String tenantId = resolveTenant();
        String htmlRoster = studentExportService.generatePrintableRosterHtml(tenantId, className, sectionName);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlRoster);
    }
}

