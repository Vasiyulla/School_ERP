package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.dto.*;
import com.schoolerp.modules.operations.entity.*;
import com.schoolerp.modules.operations.service.LibraryService;
import com.schoolerp.modules.operations.service.OperationsService;
import jakarta.validation.Valid;
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
    private final LibraryService libraryService;

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

    @PostMapping("/telemetry/geofence-ping")
    public ResponseEntity<ApiResponse<OperationsService.GeofenceTelemetryResult>> processGeofencePing(@RequestBody GpsPingDto dto) {
        var result = operationsService.processGpsPingWithGeofencing(
                dto.getVehicleRegistration(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getSpeedKmph(),
                dto.getHeadingDegrees()
        );
        return ResponseEntity.ok(ApiResponse.ok("Geofence proximity telemetry processed", result));
    }

    @GetMapping("/routes/{routeId}/geofences")
    public ResponseEntity<ApiResponse<List<RouteStopGeofence>>> getRouteGeofences(@PathVariable UUID routeId) {
        return ResponseEntity.ok(ApiResponse.ok(operationsService.getGeofencesForRoute(routeId)));
    }

    @PostMapping("/routes/{routeId}/geofences")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TRANSPORT_MANAGER')")
    public ResponseEntity<ApiResponse<RouteStopGeofence>> configureGeofence(
            @PathVariable UUID routeId,
            @RequestBody RouteStopGeofence geofence) {
        geofence.setRouteId(routeId);
        return ResponseEntity.ok(ApiResponse.ok("Geofence stop configured", operationsService.configureGeofence(geofence)));
    }

    // --- Library Endpoints ---

    @GetMapping("/library/books")
    public ResponseEntity<ApiResponse<List<Book>>> getBooks() {
        return ResponseEntity.ok(ApiResponse.ok(operationsService.getBooks()));
    }

    @PostMapping("/library/books")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<Book>> registerBook(@RequestBody BookRegisterDto dto) {
        Book book = libraryService.registerBook(
                dto.getTitle(), dto.getAuthor(), dto.getIsbn(), dto.getCategory(),
                dto.getTotalCopies(), dto.getRackLocation(), dto.getBarcodeTag()
        );
        return ResponseEntity.ok(ApiResponse.ok("Book catalog entry created successfully", book));
    }

    @GetMapping("/library/books/search")
    public ResponseEntity<ApiResponse<List<Book>>> searchBooks(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(ApiResponse.ok(libraryService.searchBooks(query)));
    }

    @GetMapping("/library/students/{studentId}/issues")
    public ResponseEntity<ApiResponse<List<BookIssueRecord>>> getStudentIssues(@PathVariable UUID studentId) {
        return ResponseEntity.ok(ApiResponse.ok(libraryService.getStudentIssues(studentId)));
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

    // --- Hostel & Residential Operations ---

    @PostMapping("/hostel/rooms")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('HOSTEL_WARDEN')")
    public ResponseEntity<ApiResponse<HostelRoomDTO>> registerHostelRoom(@Valid @RequestBody RegisterHostelRoomRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Hostel room registered successfully", operationsService.registerHostelRoom(request)));
    }

    @PostMapping("/hostel/allocations")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('HOSTEL_WARDEN')")
    public ResponseEntity<ApiResponse<HostelAllocationDTO>> allocateHostelBed(@Valid @RequestBody AllocateHostelBedRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Hostel bed allocated successfully", operationsService.allocateHostelBed(request)));
    }

    @PostMapping("/hostel/roll-call")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('HOSTEL_WARDEN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<HostelRollCallDTO>> recordHostelRollCall(@Valid @RequestBody HostelRollCallRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Hostel roll call recorded successfully", operationsService.recordHostelRollCall(request)));
    }

    @PostMapping("/hostel/exeat-passes")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('HOSTEL_WARDEN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<HostelExeatPassDTO>> issueHostelExeatPass(@Valid @RequestBody HostelExeatPassRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Hostel exeat pass issued successfully", operationsService.issueHostelExeatPass(request)));
    }

    @GetMapping("/hostel/roster/{block}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('HOSTEL_WARDEN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<HostelRosterDTO>> getHostelRoster(@PathVariable String block) {
        return ResponseEntity.ok(ApiResponse.ok("Hostel occupancy roster retrieved", operationsService.getHostelRoster(block)));
    }

    // --- Campus Visitor Security Management (VMS) ---

    @PostMapping("/visitors/check-in")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SECURITY_GUARD') or hasRole('FRONT_DESK') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<CampusVisitorDTO>> checkInVisitor(@Valid @RequestBody VisitorCheckInRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Visitor check-in logged and badge issued", operationsService.checkInVisitor(request)));
    }

    @PutMapping("/visitors/{id}/host-approval")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('PRINCIPAL') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<CampusVisitorDTO>> updateVisitorHostApproval(
            @PathVariable UUID id,
            @Valid @RequestBody VisitorApprovalRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Visitor host approval status updated", operationsService.updateVisitorHostApproval(id, request)));
    }

    @PutMapping("/visitors/{id}/check-out")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SECURITY_GUARD') or hasRole('FRONT_DESK') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<CampusVisitorDTO>> checkOutVisitor(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok("Visitor check-out confirmed", operationsService.checkOutVisitor(id)));
    }

    @GetMapping("/visitors/active")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SECURITY_GUARD') or hasRole('FRONT_DESK') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<List<CampusVisitorDTO>>> getActiveVisitors() {
        return ResponseEntity.ok(ApiResponse.ok("Active campus visitors retrieved", operationsService.getActiveVisitors()));
    }

    // --- Fleet Fuel & Compliance Logistics ---

    @PostMapping("/fleet/fuel-logs")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TRANSPORT_MANAGER')")
    public ResponseEntity<ApiResponse<VehicleFuelLogDTO>> recordFuelLog(@Valid @RequestBody RecordFuelLogRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Fuel log recorded and mileage analyzed", operationsService.recordFuelLog(request)));
    }

    @GetMapping("/fleet/fitness-sentinel")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TRANSPORT_MANAGER') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<List<VehicleComplianceSentinelDTO>>> getVehicleFitnessCompliance() {
        return ResponseEntity.ok(ApiResponse.ok("Fleet RTO compliance and fitness records retrieved", operationsService.getVehicleFitnessCompliance()));
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

    @Data
    public static class BookRegisterDto {
        private String title;
        private String author;
        private String isbn;
        private String category;
        private int totalCopies;
        private String rackLocation;
        private String barcodeTag;
    }
}

