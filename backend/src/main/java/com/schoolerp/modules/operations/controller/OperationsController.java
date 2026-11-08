package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.entity.*;
import com.schoolerp.modules.operations.service.OperationsService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operations")
@RequiredArgsConstructor
public class OperationsController {

    private final OperationsService operationsService;

    // --- Transport Endpoints ---

    @GetMapping("/vehicles")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getVehicles() {
        return ResponseEntity.ok(ApiResponse.ok(operationsService.getVehicles()));
    }

    @GetMapping("/routes")
    public ResponseEntity<ApiResponse<List<TransportRoute>>> getRoutes() {
        return ResponseEntity.ok(ApiResponse.ok(operationsService.getRoutes()));
    }

    @PostMapping("/telemetry/ping")
    public ResponseEntity<ApiResponse<GpsPing>> recordPing(@RequestBody GpsPingDto dto) {
        GpsPing ping = operationsService.recordGpsPing(
                dto.getVehicleRegistration(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getSpeedKmph(),
                dto.getHeadingDegrees()
        );
        return ResponseEntity.ok(ApiResponse.ok("Telemetry ping recorded", ping));
    }

    @GetMapping("/telemetry/{registration}")
    public ResponseEntity<ApiResponse<GpsPing>> getVehicleLocation(@PathVariable String registration) {
        return operationsService.getLatestVehicleLocation(registration)
                .map(p -> ResponseEntity.ok(ApiResponse.ok(p)))
                .orElse(ResponseEntity.ok(ApiResponse.ok("No telemetry received yet", null)));
    }

    // --- Library Endpoints ---

    @GetMapping("/library/books")
    public ResponseEntity<ApiResponse<List<Book>>> getBooks() {
        return ResponseEntity.ok(ApiResponse.ok(operationsService.getBooks()));
    }

    @GetMapping("/library/issues")
    public ResponseEntity<ApiResponse<List<BookIssueRecord>>> getIssueRecords() {
        return ResponseEntity.ok(ApiResponse.ok(operationsService.getIssueRecords()));
    }

    @PostMapping("/library/issues")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<BookIssueRecord>> issueBook(@RequestBody IssueBookDto dto) {
        BookIssueRecord record = operationsService.issueBook(
                dto.getBookId(),
                dto.getStudentId(),
                dto.getStudentName(),
                dto.getLoanDays()
        );
        return ResponseEntity.ok(ApiResponse.ok("Book issued successfully", record));
    }

    @PostMapping("/library/issues/{id}/return")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<BookIssueRecord>> returnBook(@PathVariable UUID id) {
        BookIssueRecord record = operationsService.returnBook(id);
        return ResponseEntity.ok(ApiResponse.ok("Book returned successfully", record));
    }

    @Data
    public static class GpsPingDto {
        private String vehicleRegistration;
        private double latitude;
        private double longitude;
        private double speedKmph;
        private double headingDegrees;
    }

    @Data
    public static class IssueBookDto {
        private UUID bookId;
        private UUID studentId;
        private String studentName;
        private int loanDays;
    }
}

