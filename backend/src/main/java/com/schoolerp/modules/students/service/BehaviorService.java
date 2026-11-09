package com.schoolerp.modules.students.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.entity.BehaviorIncident;
import com.schoolerp.modules.students.entity.SchoolHouse;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.BehaviorIncidentRepository;
import com.schoolerp.modules.students.repository.SchoolHouseRepository;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BehaviorService {

    private final SchoolHouseRepository houseRepository;
    private final BehaviorIncidentRepository incidentRepository;
    private final StudentRepository studentRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public BehaviorIncident recordBehaviorIncident(UUID studentId, String incidentType, String category,
                                                   int rawPoints, String description, String reportedByStaff) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        Student student = studentRepository.findById(studentId)
                .filter(s -> s.getTenantId().equals(activeTenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        // Assign to a house based on student class/roll
        String houseName = (student.getRollNumber() % 4 == 0) ? "Mars (Red)"
                : (student.getRollNumber() % 4 == 1) ? "Neptune (Blue)"
                : (student.getRollNumber() % 4 == 2) ? "Terra (Green)" : "Apollo (Yellow)";

        boolean isMerit = "MERIT".equalsIgnoreCase(incidentType);
        int netPoints = isMerit ? Math.abs(rawPoints) : -Math.abs(rawPoints);

        boolean notifyParent = (!isMerit && Math.abs(netPoints) >= 10);

        BehaviorIncident incident = BehaviorIncident.builder()
                .tenantId(activeTenantId)
                .studentId(studentId)
                .studentName(student.getFullName())
                .className(student.getClassName() + " - " + student.getSectionName())
                .houseName(houseName)
                .incidentType(isMerit ? "MERIT" : "DEMERIT")
                .category(category != null ? category : "GENERAL_CONDUCT")
                .points(netPoints)
                .description(description)
                .reportedByStaffName(reportedByStaff != null ? reportedByStaff : "Faculty In-Charge")
                .incidentDate(LocalDate.now())
                .parentNotified(notifyParent)
                .recordedAt(LocalDateTime.now())
                .build();

        BehaviorIncident saved = incidentRepository.save(incident);

        // Update house points
        SchoolHouse house = houseRepository.findByTenantIdAndHouseName(activeTenantId, houseName)
                .orElseGet(() -> houseRepository.save(SchoolHouse.builder()
                        .tenantId(activeTenantId)
                        .houseName(houseName)
                        .houseColorHex("#EF4444")
                        .totalPoints(0)
                        .build()));

        house.setTotalPoints(house.getTotalPoints() + netPoints);
        houseRepository.save(house);

        auditLogService.recordEvent(
                "RECORD_BEHAVIOR_INCIDENT",
                "students.behavior_incidents",
                saved.getId().toString(),
                String.format("%s: %s points (%+d) to %s (%s) in House %s. Description: '%s'",
                        isMerit ? "Merit Awarded" : "Demerit Logged", category, netPoints,
                        student.getFullName(), student.getClassName(), houseName, description)
        );

        log.info("Behavior incident recorded: {} - {:+d} pts for {}", incidentType, netPoints, student.getFullName());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<SchoolHouse> getHouseLeaderboard() {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return houseRepository.findAllByTenantIdOrderByTotalPointsDesc(activeTenantId);
    }

    @Transactional(readOnly = true)
    public List<BehaviorIncident> getStudentIncidents(UUID studentId) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return incidentRepository.findAllByTenantIdAndStudentIdOrderByRecordedAtDesc(activeTenantId, studentId);
    }

    @Transactional(readOnly = true)
    public List<BehaviorIncident> getAllIncidents() {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return incidentRepository.findAllByTenantIdOrderByRecordedAtDesc(activeTenantId);
    }
}

