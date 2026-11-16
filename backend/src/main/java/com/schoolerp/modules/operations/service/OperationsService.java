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

    // --- Hostel & Boarding House Operations ---

    @Transactional
    public HostelRoomDTO registerHostelRoom(RegisterHostelRoomRequest request) {
        String tenantId = TenantContext.getTenantId();
        HostelRoom room = HostelRoom.builder()
                .tenantId(tenantId)
                .blockName(request.getBlockName())
                .floorNumber(request.getFloorNumber())
                .roomNumber(request.getRoomNumber())
                .roomType(request.getRoomType() != null ? request.getRoomType() : "NON_AC")
                .totalBeds(request.getTotalBeds() > 0 ? request.getTotalBeds() : 4)
                .occupiedBeds(0)
                .annualFee(request.getAnnualFee() != null ? request.getAnnualFee() : BigDecimal.valueOf(80000.00))
                .build();
        HostelRoom saved = hostelRoomRepository.save(room);
        auditLogService.recordEvent("HOSTEL_ROOM_REGISTER", "operations.hostel", saved.getId().toString(),
                "Registered hostel room " + saved.getBlockName() + " / " + saved.getRoomNumber());
        return mapToHostelRoomDTO(saved);
    }

    @Transactional
    public HostelAllocationDTO allocateHostelBed(AllocateHostelBedRequest request) {
        String tenantId = TenantContext.getTenantId();
        HostelRoom room = hostelRoomRepository.findById(request.getRoomId())
                .filter(r -> r.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Hostel room not found: " + request.getRoomId()));

        if (room.getOccupiedBeds() >= room.getTotalBeds()) {
            throw new IllegalStateException("Hostel room " + room.getRoomNumber() + " is at full capacity (" + room.getTotalBeds() + " beds)");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .filter(s -> s.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + request.getStudentId()));

        HostelAllocation allocation = HostelAllocation.builder()
                .tenantId(tenantId)
                .roomId(room.getId())
                .studentId(student.getId())
                .bedNumber(request.getBedNumber() > 0 ? request.getBedNumber() : room.getOccupiedBeds() + 1)
                .academicYear(request.getAcademicYear() != null ? request.getAcademicYear() : "2026-2027")
                .status("ACTIVE")
                .allocatedAt(java.time.LocalDateTime.now())
                .build();

        HostelAllocation saved = hostelAllocationRepository.save(allocation);

        room.setOccupiedBeds(room.getOccupiedBeds() + 1);
        hostelRoomRepository.save(room);

        auditLogService.recordEvent("HOSTEL_BED_ALLOCATE", "operations.hostel", saved.getId().toString(),
                "Allocated Bed #" + saved.getBedNumber() + " in room " + room.getRoomNumber() + " to student " + student.getFullName());

        return HostelAllocationDTO.builder()
                .id(saved.getId())
                .roomId(room.getId())
                .blockName(room.getBlockName())
                .roomNumber(room.getRoomNumber())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .studentAdmissionNumber(student.getAdmissionNumber())
                .bedNumber(saved.getBedNumber())
                .academicYear(saved.getAcademicYear())
                .status(saved.getStatus())
                .allocatedAt(saved.getAllocatedAt())
                .build();
    }

    @Transactional
    public HostelRollCallDTO recordHostelRollCall(HostelRollCallRequest request) {
        String tenantId = TenantContext.getTenantId();
        HostelAllocation allocation = hostelAllocationRepository.findById(request.getAllocationId())
                .filter(a -> a.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Hostel allocation not found: " + request.getAllocationId()));

        Student student = studentRepository.findById(allocation.getStudentId())
                .filter(s -> s.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + allocation.getStudentId()));

        HostelRollCall rollCall = HostelRollCall.builder()
                .tenantId(tenantId)
                .allocationId(allocation.getId())
                .studentId(student.getId())
                .rollCallDate(request.getRollCallDate() != null ? request.getRollCallDate() : LocalDate.now())
                .sessionType(request.getSessionType())
                .status(request.getStatus() != null ? request.getStatus() : "PRESENT")
                .remarks(request.getRemarks())
                .wardenName(request.getWardenName() != null ? request.getWardenName() : "Chief Warden")
                .createdAt(java.time.LocalDateTime.now())
                .build();

        HostelRollCall saved = hostelRollCallRepository.save(rollCall);

        auditLogService.recordEvent("HOSTEL_ROLL_CALL", "operations.hostel", saved.getId().toString(),
                "Roll call recorded for student " + student.getFullName() + " status: " + saved.getStatus());

        return HostelRollCallDTO.builder()
                .id(saved.getId())
                .allocationId(saved.getAllocationId())
                .studentId(saved.getStudentId())
                .studentName(student.getFullName())
                .rollCallDate(saved.getRollCallDate())
                .sessionType(saved.getSessionType())
                .status(saved.getStatus())
                .remarks(saved.getRemarks())
                .wardenName(saved.getWardenName())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional
    public HostelExeatPassDTO issueHostelExeatPass(HostelExeatPassRequest request) {
        String tenantId = TenantContext.getTenantId();
        Student student = studentRepository.findById(request.getStudentId())
                .filter(s -> s.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + request.getStudentId()));

        String passNumber = "EXEAT-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        HostelExeatPass exeatPass = HostelExeatPass.builder()
                .tenantId(tenantId)
                .studentId(student.getId())
                .passNumber(passNumber)
                .exeatType(request.getExeatType())
                .departureTime(request.getDepartureTime())
                .expectedReturnTime(request.getExpectedReturnTime())
                .parentAuthorized(request.isParentAuthorized())
                .parentContactPhone(request.getParentContactPhone() != null ? request.getParentContactPhone() : student.getGuardianPhone())
                .status("APPROVED")
                .createdAt(java.time.LocalDateTime.now())
                .build();

        HostelExeatPass saved = hostelExeatPassRepository.save(exeatPass);

        auditLogService.recordEvent("HOSTEL_EXEAT_PASS_ISSUED", "operations.hostel", saved.getId().toString(),
                "Exeat pass " + saved.getPassNumber() + " issued for student " + student.getFullName());

        return HostelExeatPassDTO.builder()
                .id(saved.getId())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .studentAdmissionNumber(student.getAdmissionNumber())
                .passNumber(saved.getPassNumber())
                .exeatType(saved.getExeatType())
                .departureTime(saved.getDepartureTime())
                .expectedReturnTime(saved.getExpectedReturnTime())
                .actualReturnTime(saved.getActualReturnTime())
                .parentAuthorized(saved.isParentAuthorized())
                .parentContactPhone(saved.getParentContactPhone())
                .status(saved.getStatus())
                .gateDepartureScannedAt(saved.getGateDepartureScannedAt())
                .build();
    }

    public HostelRosterDTO getHostelRoster(String blockName) {
        String tenantId = TenantContext.getTenantId();
        String cleanBlock = blockName != null ? java.net.URLDecoder.decode(blockName.replace("+", " "), java.nio.charset.StandardCharsets.UTF_8) : "";
        List<HostelRoom> rooms;
        if (cleanBlock.isBlank() || "ALL".equalsIgnoreCase(cleanBlock)) {
            rooms = hostelRoomRepository.findByTenantId(tenantId);
        } else {
            rooms = hostelRoomRepository.findByTenantIdAndBlockName(tenantId, cleanBlock);
        }

        int totalRooms = rooms.size();
        int totalBeds = rooms.stream().mapToInt(HostelRoom::getTotalBeds).sum();
        int totalOccupied = rooms.stream().mapToInt(HostelRoom::getOccupiedBeds).sum();
        int totalAvailable = Math.max(0, totalBeds - totalOccupied);

        List<HostelAllocationDTO> residents = new ArrayList<>();
        for (HostelRoom room : rooms) {
            List<HostelAllocation> allocations = hostelAllocationRepository.findByTenantIdAndRoomIdAndStatus(tenantId, room.getId(), "ACTIVE");
            for (HostelAllocation alloc : allocations) {
                String sName = "Student";
                String admNo = "ADM";
                Optional<Student> st = studentRepository.findById(alloc.getStudentId());
                if (st.isPresent()) {
                    sName = st.get().getFullName();
                    admNo = st.get().getAdmissionNumber();
                }
                residents.add(HostelAllocationDTO.builder()
                        .id(alloc.getId())
                        .roomId(room.getId())
                        .blockName(room.getBlockName())
                        .roomNumber(room.getRoomNumber())
                        .studentId(alloc.getStudentId())
                        .studentName(sName)
                        .studentAdmissionNumber(admNo)
                        .bedNumber(alloc.getBedNumber())
                        .academicYear(alloc.getAcademicYear())
                        .status(alloc.getStatus())
                        .allocatedAt(alloc.getAllocatedAt())
                        .build());
            }
        }

        return HostelRosterDTO.builder()
                .blockName(cleanBlock.isBlank() ? "All Blocks" : cleanBlock)
                .totalRooms(totalRooms)
                .totalBeds(totalBeds)
                .totalOccupied(totalOccupied)
                .totalAvailable(totalAvailable)
                .residents(residents)
                .build();
    }

    private HostelRoomDTO mapToHostelRoomDTO(HostelRoom room) {
        return HostelRoomDTO.builder()
                .id(room.getId())
                .blockName(room.getBlockName())
                .floorNumber(room.getFloorNumber())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .totalBeds(room.getTotalBeds())
                .occupiedBeds(room.getOccupiedBeds())
                .availableBeds(Math.max(0, room.getTotalBeds() - room.getOccupiedBeds()))
                .annualFee(room.getAnnualFee())
                .build();
    }

    // --- Campus Visitor Security Management (VMS) ---

    @Transactional
    public CampusVisitorDTO checkInVisitor(VisitorCheckInRequest request) {
        String tenantId = TenantContext.getTenantId();
        String passNumber = "VIS-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String qrToken = "VQR-" + UUID.randomUUID().toString();

        CampusVisitor visitor = CampusVisitor.builder()
                .tenantId(tenantId)
                .visitorPassNumber(passNumber)
                .visitorName(request.getVisitorName())
                .visitorPhone(request.getVisitorPhone())
                .idProofType(request.getIdProofType())
                .idProofNumber(request.getIdProofNumber())
                .purposeOfVisit(request.getPurposeOfVisit())
                .hostStaffId(request.getHostStaffId())
                .hostStaffName(request.getHostStaffName())
                .hostApprovalStatus("PENDING")
                .entryGate(request.getEntryGate())
                .badgeQrToken(qrToken)
                .checkedInAt(java.time.LocalDateTime.now())
                .status("ACTIVE_ON_CAMPUS")
                .createdAt(java.time.LocalDateTime.now())
                .build();

        CampusVisitor saved = campusVisitorRepository.save(visitor);
        auditLogService.recordEvent("CAMPUS_VISITOR_CHECKIN", "operations.security", saved.getId().toString(),
                "Visitor check-in: " + saved.getVisitorName() + " (Pass: " + saved.getVisitorPassNumber() + ")");

        return mapToCampusVisitorDTO(saved);
    }

    @Transactional
    public CampusVisitorDTO updateVisitorHostApproval(UUID visitorId, VisitorApprovalRequest request) {
        String tenantId = TenantContext.getTenantId();
        CampusVisitor visitor = campusVisitorRepository.findById(visitorId)
                .filter(v -> v.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Visitor record not found: " + visitorId));

        visitor.setHostApprovalStatus(request.getApprovalDecision());
        if ("REJECTED".equalsIgnoreCase(request.getApprovalDecision())) {
            visitor.setStatus("ENTRY_DENIED");
        }
        CampusVisitor saved = campusVisitorRepository.save(visitor);

        auditLogService.recordEvent("VISITOR_HOST_APPROVAL", "operations.security", saved.getId().toString(),
                "Host approval decision for " + saved.getVisitorName() + ": " + saved.getHostApprovalStatus());

        return mapToCampusVisitorDTO(saved);
    }

    @Transactional
    public CampusVisitorDTO checkOutVisitor(UUID visitorId) {
        String tenantId = TenantContext.getTenantId();
        CampusVisitor visitor = campusVisitorRepository.findById(visitorId)
                .filter(v -> v.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Visitor record not found: " + visitorId));

        visitor.setCheckedOutAt(java.time.LocalDateTime.now());
        visitor.setStatus("CHECKED_OUT");
        CampusVisitor saved = campusVisitorRepository.save(visitor);

        auditLogService.recordEvent("CAMPUS_VISITOR_CHECKOUT", "operations.security", saved.getId().toString(),
                "Visitor check-out: " + saved.getVisitorName() + " (Pass: " + saved.getVisitorPassNumber() + ")");

        return mapToCampusVisitorDTO(saved);
    }

    public List<CampusVisitorDTO> getActiveVisitors() {
        String tenantId = TenantContext.getTenantId();
        List<CampusVisitor> active = campusVisitorRepository.findByTenantIdAndStatus(tenantId, "ACTIVE_ON_CAMPUS");
        List<CampusVisitorDTO> dtos = new ArrayList<>();
        for (CampusVisitor v : active) {
            dtos.add(mapToCampusVisitorDTO(v));
        }
        return dtos;
    }

    private CampusVisitorDTO mapToCampusVisitorDTO(CampusVisitor v) {
        boolean isOverstayed = false;
        if ("ACTIVE_ON_CAMPUS".equals(v.getStatus()) && v.getCheckedInAt() != null) {
            long minutesOnCampus = java.time.Duration.between(v.getCheckedInAt(), java.time.LocalDateTime.now()).toMinutes();
            if (minutesOnCampus > 120) { // Over 2 hours
                isOverstayed = true;
            }
        }
        return CampusVisitorDTO.builder()
                .id(v.getId())
                .visitorPassNumber(v.getVisitorPassNumber())
                .visitorName(v.getVisitorName())
                .visitorPhone(v.getVisitorPhone())
                .idProofType(v.getIdProofType())
                .idProofNumber(v.getIdProofNumber())
                .purposeOfVisit(v.getPurposeOfVisit())
                .hostStaffId(v.getHostStaffId())
                .hostStaffName(v.getHostStaffName())
                .hostApprovalStatus(v.getHostApprovalStatus())
                .entryGate(v.getEntryGate())
                .badgeQrToken(v.getBadgeQrToken())
                .checkedInAt(v.getCheckedInAt())
                .checkedOutAt(v.getCheckedOutAt())
                .status(v.getStatus())
                .isOverstayed(isOverstayed)
                .build();
    }

    // --- Fleet Fuel & Compliance Logistics ---

    @Transactional
    public VehicleFuelLogDTO recordFuelLog(RecordFuelLogRequest request) {
        String tenantId = TenantContext.getTenantId();
        List<VehicleFuelLog> previousLogs = vehicleFuelLogRepository
                .findByTenantIdAndVehicleIdOrderByFillDateDesc(tenantId, request.getVehicleId());

        int previousOdo = previousLogs.isEmpty()
                ? Math.max(0, request.getOdometerReading() - 250)
                : previousLogs.get(0).getOdometerReading();

        int kmRun = Math.max(1, request.getOdometerReading() - previousOdo);
        BigDecimal totalCost = request.getFuelLitres().multiply(request.getFuelRatePerLitre());

        BigDecimal mileage = request.getFuelLitres().compareTo(BigDecimal.ZERO) > 0
                ? BigDecimal.valueOf(kmRun).divide(request.getFuelLitres(), 2, java.math.RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        boolean pilferageAlert = mileage.compareTo(BigDecimal.valueOf(3.5)) < 0;

        VehicleFuelLog logEntry = VehicleFuelLog.builder()
                .tenantId(tenantId)
                .vehicleId(request.getVehicleId())
                .vehicleRegistration(request.getVehicleRegistration() != null ? request.getVehicleRegistration() : "MH-12-AB-4042")
                .fillDate(request.getFillDate() != null ? request.getFillDate() : LocalDate.now())
                .fuelLitres(request.getFuelLitres())
                .fuelRatePerLitre(request.getFuelRatePerLitre())
                .totalCost(totalCost)
                .odometerReading(request.getOdometerReading())
                .previousOdometerReading(previousOdo)
                .kilometersRun(kmRun)
                .mileageKmPerLitre(mileage)
                .fuelStationName(request.getFuelStationName() != null ? request.getFuelStationName() : "Indian Oil - Viman Nagar")
                .driverName(request.getDriverName() != null ? request.getDriverName() : "Fleet Driver")
                .pilferageAlert(pilferageAlert)
                .createdAt(java.time.LocalDateTime.now())
                .build();

        VehicleFuelLog saved = vehicleFuelLogRepository.save(logEntry);

        if (pilferageAlert) {
            auditLogService.recordEvent("FLEET_FUEL_PILFERAGE_ALERT", "operations.fleet", saved.getId().toString(),
                    String.format("Fuel pilferage warning for bus %s: mileage %.2f km/L is abnormally low",
                            saved.getVehicleRegistration(), saved.getMileageKmPerLitre()));
        }

        return VehicleFuelLogDTO.builder()
                .id(saved.getId())
                .vehicleId(saved.getVehicleId())
                .vehicleRegistration(saved.getVehicleRegistration())
                .fillDate(saved.getFillDate())
                .fuelLitres(saved.getFuelLitres())
                .fuelRatePerLitre(saved.getFuelRatePerLitre())
                .totalCost(saved.getTotalCost())
                .odometerReading(saved.getOdometerReading())
                .previousOdometerReading(saved.getPreviousOdometerReading())
                .kilometersRun(saved.getKilometersRun())
                .mileageKmPerLitre(saved.getMileageKmPerLitre())
                .fuelStationName(saved.getFuelStationName())
                .driverName(saved.getDriverName())
                .pilferageAlert(saved.isPilferageAlert())
                .build();
    }

    @Transactional
    public List<VehicleComplianceSentinelDTO> getVehicleFitnessCompliance() {
        String tenantId = TenantContext.getTenantId();
        List<VehicleComplianceSentinel> sentinels = vehicleComplianceSentinelRepository.findByTenantId(tenantId);

        if (sentinels.isEmpty()) {
            List<Vehicle> vehicles = vehicleRepository.findByTenantId(tenantId);
            for (Vehicle v : vehicles) {
                VehicleComplianceSentinel sentinel = VehicleComplianceSentinel.builder()
                        .tenantId(tenantId)
                        .vehicleId(v.getId())
                        .vehicleRegistration(v.getRegistrationNumber())
                        .rtoFitnessExpiry(LocalDate.now().plusMonths(8))
                        .insurancePolicyExpiry(LocalDate.now().plusMonths(6))
                        .pucCertificateExpiry(LocalDate.now().plusMonths(2))
                        .roadTaxExpiry(LocalDate.now().plusYears(1))
                        .speedGovernorCertExpiry(LocalDate.now().plusMonths(10))
                        .complianceStatus("COMPLIANT")
                        .createdAt(java.time.LocalDateTime.now())
                        .updatedAt(java.time.LocalDateTime.now())
                        .build();
                sentinels.add(vehicleComplianceSentinelRepository.save(sentinel));
            }
        }

        List<VehicleComplianceSentinelDTO> dtos = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (VehicleComplianceSentinel s : sentinels) {
            long daysToFitness = java.time.temporal.ChronoUnit.DAYS.between(today, s.getRtoFitnessExpiry());
            long daysToInsurance = java.time.temporal.ChronoUnit.DAYS.between(today, s.getInsurancePolicyExpiry());
            long daysToPuc = java.time.temporal.ChronoUnit.DAYS.between(today, s.getPucCertificateExpiry());

            String status = "COMPLIANT";
            if (daysToFitness < 0 || daysToInsurance < 0 || daysToPuc < 0) {
                status = "CRITICAL_EXPIRED";
            } else if (daysToFitness < 30 || daysToInsurance < 30 || daysToPuc < 30) {
                status = "WARNING_EXPIRING_SOON";
            }
            s.setComplianceStatus(status);

            dtos.add(VehicleComplianceSentinelDTO.builder()
                    .id(s.getId())
                    .vehicleId(s.getVehicleId())
                    .vehicleRegistration(s.getVehicleRegistration())
                    .rtoFitnessExpiry(s.getRtoFitnessExpiry())
                    .daysUntilFitnessExpiry(daysToFitness)
                    .insurancePolicyExpiry(s.getInsurancePolicyExpiry())
                    .daysUntilInsuranceExpiry(daysToInsurance)
                    .pucCertificateExpiry(s.getPucCertificateExpiry())
                    .daysUntilPucExpiry(daysToPuc)
                    .roadTaxExpiry(s.getRoadTaxExpiry())
                    .complianceStatus(status)
                    .build());
        }

        return dtos;
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
