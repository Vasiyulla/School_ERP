package com.schoolerp.modules.communication.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.communication.entity.PtmSession;
import com.schoolerp.modules.communication.entity.PtmSlot;
import com.schoolerp.modules.communication.repository.PtmSessionRepository;
import com.schoolerp.modules.communication.repository.PtmSlotRepository;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PtmService {

    private final PtmSessionRepository sessionRepository;
    private final PtmSlotRepository slotRepository;
    private final StudentRepository studentRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public PtmSession createSessionAndGenerateSlots(String title, LocalDate date, LocalTime startTime,
                                                    LocalTime endTime, int slotDurationMinutes,
                                                    List<Employee> participatingTeachers) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        PtmSession session = PtmSession.builder()
                .tenantId(activeTenantId)
                .sessionTitle(title)
                .sessionDate(date != null ? date : LocalDate.now().plusDays(7))
                .startTime(startTime != null ? startTime : LocalTime.of(9, 0))
                .endTime(endTime != null ? endTime : LocalTime.of(12, 0))
                .slotDurationMinutes(slotDurationMinutes > 0 ? slotDurationMinutes : 15)
                .status("OPEN_FOR_BOOKING")
                .build();

        PtmSession savedSession = sessionRepository.save(session);

        List<PtmSlot> slots = new ArrayList<>();
        if (participatingTeachers != null) {
            for (Employee teacher : participatingTeachers) {
                LocalTime current = session.getStartTime();
                int roomCounter = 201;
                while (current.plusMinutes(session.getSlotDurationMinutes()).compareTo(session.getEndTime()) <= 0) {
                    LocalTime next = current.plusMinutes(session.getSlotDurationMinutes());
                    slots.add(PtmSlot.builder()
                            .tenantId(activeTenantId)
                            .sessionId(savedSession.getId())
                            .teacherId(teacher.getId())
                            .teacherName(teacher.getFullName())
                            .subject(teacher.getDepartment())
                            .roomNumber("Room " + roomCounter)
                            .slotStartTime(current)
                            .slotEndTime(next)
                            .booked(false)
                            .build());
                    current = next;
                    roomCounter++;
                }
            }
        }

        if (!slots.isEmpty()) {
            slotRepository.saveAll(slots);
        }

        auditLogService.recordEvent(
                "CREATE_PTM_SESSION",
                "communication.ptm_sessions",
                savedSession.getId().toString(),
                String.format("Created PTM conference '%s' on %s with %d total booking slots across %d teachers",
                        title, session.getSessionDate(), slots.size(), participatingTeachers != null ? participatingTeachers.size() : 0)
        );

        log.info("PTM session created: {} with {} slots", title, slots.size());
        return savedSession;
    }

    @Transactional
    public PtmSlot bookSlot(UUID slotId, UUID studentId, String guardianName, String guardianPhone) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        PtmSlot slot = slotRepository.findById(slotId)
                .filter(s -> s.getTenantId().equals(activeTenantId))
                .orElseThrow(() -> new IllegalArgumentException("PTM slot not found: " + slotId));

        if (slot.isBooked()) {
            throw new IllegalStateException(String.format(
                    "This appointment slot (%s - %s) with %s is already booked",
                    slot.getSlotStartTime(), slot.getSlotEndTime(), slot.getTeacherName()));
        }

        Student student = studentRepository.findById(studentId)
                .filter(s -> s.getTenantId().equals(activeTenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        slot.setBooked(true);
        slot.setStudentId(studentId);
        slot.setStudentName(student.getFullName());
        slot.setGuardianName(guardianName != null ? guardianName : student.getGuardianName());
        slot.setGuardianPhone(guardianPhone != null ? guardianPhone : student.getGuardianPhone());
        slot.setBookedAt(LocalDateTime.now());

        PtmSlot savedSlot = slotRepository.save(slot);

        auditLogService.recordEvent(
                "BOOK_PTM_SLOT",
                "communication.ptm_slots",
                savedSlot.getId().toString(),
                String.format("PTM Slot booked: %s with %s (%s - %s) for student %s by %s (%s)",
                        slot.getSubject(), slot.getTeacherName(), slot.getSlotStartTime(), slot.getSlotEndTime(),
                        student.getFullName(), slot.getGuardianName(), slot.getGuardianPhone())
        );

        log.info("PTM slot booked: {} with {} for {}", slot.getSlotStartTime(), slot.getTeacherName(), student.getFullName());
        return savedSlot;
    }

    @Transactional(readOnly = true)
    public List<PtmSlot> getAvailableSlots(UUID sessionId, UUID teacherId) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        if (teacherId != null) {
            return slotRepository.findAllByTenantIdAndSessionIdAndTeacherId(activeTenantId, sessionId, teacherId)
                    .stream().filter(s -> !s.isBooked()).toList();
        }
        return slotRepository.findAllByTenantIdAndSessionId(activeTenantId, sessionId)
                .stream().filter(s -> !s.isBooked()).toList();
    }

    @Transactional(readOnly = true)
    public List<PtmSession> getActiveSessions() {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return sessionRepository.findAllByTenantIdAndStatus(activeTenantId, "OPEN_FOR_BOOKING");
    }

    @Transactional(readOnly = true)
    public List<PtmSlot> getStudentBookings(UUID studentId) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return slotRepository.findAllByTenantIdAndStudentId(activeTenantId, studentId);
    }
}

