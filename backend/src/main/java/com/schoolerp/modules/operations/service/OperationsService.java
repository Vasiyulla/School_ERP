package com.schoolerp.modules.operations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.communication.service.CommunicationService;
import com.schoolerp.modules.operations.dto.*;
import com.schoolerp.modules.operations.entity.*;
import com.schoolerp.modules.operations.repository.*;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationsService {

    private final VehicleRepository vehicleRepository;
    private final TransportRouteRepository transportRouteRepository;
    private final GpsPingRepository gpsPingRepository;
    private final RouteStopGeofenceRepository geofenceRepository;
    private final BookRepository bookRepository;
    private final BookIssueRecordRepository bookIssueRecordRepository;
    private final CommunicationService communicationService;
    private final AuditLogService auditLogService;
    private final HostelRoomRepository hostelRoomRepository;
    private final HostelAllocationRepository hostelAllocationRepository;
    private final HostelRollCallRepository hostelRollCallRepository;
    private final HostelExeatPassRepository hostelExeatPassRepository;
    private final CampusVisitorRepository campusVisitorRepository;
    private final VehicleFuelLogRepository vehicleFuelLogRepository;
    private final VehicleComplianceSentinelRepository vehicleComplianceSentinelRepository;
    private final StudentRepository studentRepository;

    // --- Transport Operations & Samsara-Style Telematics ---

    public List<Vehicle> getVehicles() {
        return vehicleRepository.findByTenantId(TenantContext.getTenantId());
    }

    public List<TransportRoute> getRoutes() {
        return transportRouteRepository.findByTenantId(TenantContext.getTenantId());
    }

    @Transactional
    public GpsPing recordGpsPing(String vehicleRegistration, double lat, double lng, double speed, double heading) {
        String tenantId = TenantContext.getTenantId();
        GpsPing ping = GpsPing.builder()
                .tenantId(tenantId)
                .vehicleRegistration(vehicleRegistration)
                .latitude(lat)
                .longitude(lng)
                .speedKmph(speed)
                .headingDegrees(heading)
                .build();
        return gpsPingRepository.save(ping);
    }

    @Transactional
    public GeofenceTelemetryResult processGpsPingWithGeofencing(String vehicleReg, double lat, double lng, double speed, double heading) {
        String tenantId = TenantContext.getTenantId();

        // 1. Record raw GPS ping
        GpsPing ping = recordGpsPing(vehicleReg, lat, lng, speed, heading);

        // 2. Speed Violation Guard (Samsara Telematics safety scorecard)
        boolean isOverspeeding = speed > 50.0; // 50 km/h school zone limit
        if (isOverspeeding) {
            auditLogService.recordEvent(
                    "OVERSPEED_WARNING",
                    "operations.telematics",
                    vehicleReg,
                    String.format("Bus %s exceeded speed threshold: %.1f km/h (Limit: 50 km/h)", vehicleReg, speed)
            );
        }

        // 3. Geofence Proximity Trigger Analysis
        List<TransportRoute> routes = transportRouteRepository.findByTenantId(tenantId);
        TransportRoute vehicleRoute = routes.stream()
                .filter(r -> vehicleReg.equalsIgnoreCase(r.getVehicleRegistration()))
                .findFirst()
                .orElse(null);

        String triggeredStop = null;
        double nearestDistanceMeters = Double.MAX_VALUE;
        boolean geofenceTriggered = false;

        if (vehicleRoute != null) {
            List<RouteStopGeofence> geofences = geofenceRepository
                    .findByTenantIdAndRouteIdOrderByStopSequenceAsc(tenantId, vehicleRoute.getId());

            for (RouteStopGeofence stop : geofences) {
                double distance = calculateHaversineDistanceMeters(lat, lng, stop.getLatitude(), stop.getLongitude());
                if (distance < nearestDistanceMeters) {
                    nearestDistanceMeters = distance;
                    triggeredStop = stop.getStopName();
                }

                if (distance <= stop.getRadiusMeters()) {
                    geofenceTriggered = true;
                    // Trigger automated parent proximity alert
                    int estimatedMinutes = Math.max(1, (int) Math.round((distance / 1000.0) / Math.max(speed, 20.0) * 60.0));
                    String alertBody = String.format("School Bus %s is %d meters away from %s (ETA: ~%d mins).",
                            vehicleReg, (int) Math.round(distance), stop.getStopName(), estimatedMinutes);

                    communicationService.sendPushNotification(
                            UUID.randomUUID(), // Target registered parents linked to this stop
                            "Parents at " + stop.getStopName(),
                            "Bus Proximity Alert",
                            alertBody,
                            "GEOFENCE_PROXIMITY"
                    );

                    auditLogService.recordEvent(
                            "GEOFENCE_ALERT_TRIGGERED",
                            "operations.telematics",
                            vehicleReg,
                            String.format("Geofence triggered for %s at %s. Distance: %.1fm",
                                    vehicleReg, stop.getStopName(), distance)
                    );
                    break;
                }
            }
        }

        return GeofenceTelemetryResult.builder()
                .vehicleRegistration(vehicleReg)
                .currentSpeed(speed)
                .isOverspeeding(isOverspeeding)
                .nearestStop(triggeredStop != null ? triggeredStop : "En Route")
                .distanceToNearestStopMeters(nearestDistanceMeters < Double.MAX_VALUE ? (int) Math.round(nearestDistanceMeters) : 0)
                .geofenceTriggered(geofenceTriggered)
                .latitude(lat)
                .longitude(lng)
                .build();
    }

    public List<RouteStopGeofence> getGeofencesForRoute(UUID routeId) {
        return geofenceRepository.findByTenantIdAndRouteIdOrderByStopSequenceAsc(TenantContext.getTenantId(), routeId);
    }

    @Transactional
    public RouteStopGeofence configureGeofence(RouteStopGeofence geofence) {
        geofence.setTenantId(TenantContext.getTenantId());
        return geofenceRepository.save(geofence);
    }

    public Optional<GpsPing> getLatestVehicleLocation(String vehicleRegistration) {
        return gpsPingRepository.findTop1ByTenantIdAndVehicleRegistrationOrderByRecordedAtDesc(
                TenantContext.getTenantId(), vehicleRegistration);
    }

    public static double calculateHaversineDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // --- Library Operations ---

    public List<Book> getBooks() {
        return bookRepository.findByTenantId(TenantContext.getTenantId());
    }

    public List<BookIssueRecord> getIssueRecords() {
        return bookIssueRecordRepository.findByTenantId(TenantContext.getTenantId());
    }

    @Transactional
    public BookIssueRecord issueBook(UUID bookId, UUID studentId, String studentName, int loanDays) {
        String tenantId = TenantContext.getTenantId();
        Book book = bookRepository.findById(bookId)
                .filter(b -> b.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies remaining for: " + book.getTitle());
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BookIssueRecord record = BookIssueRecord.builder()
                .tenantId(tenantId)
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .studentId(studentId)
                .studentName(studentName)
                .issuedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(loanDays > 0 ? loanDays : 14))
                .status("ISSUED")
                .fineAmount(BigDecimal.ZERO)
                .build();

        BookIssueRecord saved = bookIssueRecordRepository.save(record);

        auditLogService.recordEvent(
                "LIBRARY_ISSUE",
                "operations.library",
                saved.getId().toString(),
                "Book '" + book.getTitle() + "' issued to student " + studentName + " (Due: " + saved.getDueDate() + ")"
        );
        return saved;
    }

    @Transactional
    public BookIssueRecord returnBook(UUID issueRecordId) {
        String tenantId = TenantContext.getTenantId();
        BookIssueRecord record = bookIssueRecordRepository.findById(issueRecordId)
                .filter(r -> r.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Issue record not found: " + issueRecordId));

        if ("RETURNED".equals(record.getStatus())) {
            return record;
        }

        record.setStatus("RETURNED");
        record.setReturnedDate(LocalDate.now());

        bookRepository.findById(record.getBookId()).ifPresent(book -> {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        });

        BookIssueRecord saved = bookIssueRecordRepository.save(record);

        auditLogService.recordEvent(
                "LIBRARY_RETURN",
                "operations.library",
                saved.getId().toString(),
                "Book '" + record.getBookTitle() + "' returned by " + record.getStudentName()
        );
        return saved;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeofenceTelemetryResult {
        private String vehicleRegistration;
        private double currentSpeed;
        private boolean isOverspeeding;
        private String nearestStop;
        private int distanceToNearestStopMeters;
        private boolean geofenceTriggered;
        private double latitude;
        private double longitude;
    }
}
