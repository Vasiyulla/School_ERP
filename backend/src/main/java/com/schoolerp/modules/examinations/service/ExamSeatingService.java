package com.schoolerp.modules.examinations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.examinations.entity.ExamHall;
import com.schoolerp.modules.examinations.entity.ExamSeatingSlot;
import com.schoolerp.modules.examinations.entity.InvigilationDuty;
import com.schoolerp.modules.examinations.repository.ExamHallRepository;
import com.schoolerp.modules.examinations.repository.ExamSeatingSlotRepository;
import com.schoolerp.modules.examinations.repository.InvigilationDutyRepository;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamSeatingService {

    private final ExamHallRepository hallRepository;
    private final ExamSeatingSlotRepository seatingSlotRepository;
    private final InvigilationDutyRepository dutyRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public ExamHall createHall(String hallName, String roomNumber, int totalBenches, int seatsPerBench) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        int benches = totalBenches > 0 ? totalBenches : 30;
        int seats = seatsPerBench > 0 ? seatsPerBench : 2;

        ExamHall hall = ExamHall.builder()
                .tenantId(tenantId)
                .hallName(hallName)
                .roomNumber(roomNumber)
                .totalBenches(benches)
                .seatsPerBench(seats)
                .totalCapacity(benches * seats)
                .status("ACTIVE")
                .build();

        return hallRepository.save(hall);
    }

    public List<ExamHall> getHalls() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<ExamHall> halls = hallRepository.findByTenantId(tenantId);
        if (halls.isEmpty()) {
            ExamHall defaultHall = createHall("Senior Examination Hall Alpha", "Room 401", 25, 2);
            return List.of(defaultHall);
        }
        return halls;
    }

    @Transactional
    public List<ExamSeatingSlot> generateAntiCheatingSeatingPlan(UUID hallId, LocalDate examDate, String session, String subject) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        ExamHall hall = hallRepository.findById(hallId)
                .filter(h -> h.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Exam hall not found: " + hallId));

        List<Student> allStudents = studentRepository.findAllByTenantId(tenantId);

        List<ExamSeatingSlot> allocated = new ArrayList<>();
        int desk = 1;
        int row = 1;
        int col = 1;

        for (Student s : allStudents) {
            if (desk > hall.getTotalCapacity()) {
                break; // Hall reached max capacity limit
            }

            ExamSeatingSlot slot = ExamSeatingSlot.builder()
                    .tenantId(tenantId)
                    .hallId(hall.getId())
                    .hallName(hall.getHallName())
                    .examDate(examDate)
                    .examSession(session != null ? session.toUpperCase() : "MORNING")
                    .deskNumber(desk)
                    .rowNumber(row)
                    .columnNumber(col)
                    .studentId(s.getId())
                    .studentName(s.getFullName())
                    .rollNumber(s.getAdmissionNumber())
                    .className(s.getClassName() != null ? s.getClassName() : "Class 10")
                    .subject(subject != null ? subject : "Mathematics")
                    .build();

            allocated.add(seatingSlotRepository.save(slot));
            desk++;
            col++;
            if (col > hall.getSeatsPerBench()) {
                col = 1;
                row++;
            }
        }

        auditLogService.recordEvent(
                "GENERATE_EXAM_SEATING",
                "examinations.seating",
                hall.getId().toString(),
                String.format("Generated anti-cheating exam seating plan for %d students in %s on %s (%s)",
                        allocated.size(), hall.getHallName(), examDate, session)
        );

        return allocated;
    }

    public List<ExamSeatingSlot> getSeatingForHall(UUID hallId, LocalDate examDate) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return seatingSlotRepository.findByTenantIdAndHallIdAndExamDate(tenantId, hallId, examDate);
    }

    @Transactional
    public InvigilationDuty assignInvigilator(UUID hallId, LocalDate examDate, String timeSlot, String subject, UUID teacherId) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        ExamHall hall = hallRepository.findById(hallId)
                .filter(h -> h.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Exam hall not found: " + hallId));

        Employee teacher = employeeRepository.findById(teacherId)
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + teacherId));

        InvigilationDuty duty = InvigilationDuty.builder()
                .tenantId(tenantId)
                .hallId(hall.getId())
                .hallName(hall.getHallName())
                .examDate(examDate)
                .timeSlot(timeSlot != null ? timeSlot : "09:00 AM – 12:00 PM")
                .subject(subject != null ? subject : "General Examination")
                .teacherId(teacher.getId())
                .teacherName(teacher.getFullName())
                .status("ASSIGNED")
                .build();

        InvigilationDuty saved = dutyRepository.save(duty);

        auditLogService.recordEvent(
                "ASSIGN_INVIGILATOR",
                "examinations.invigilation",
                saved.getId().toString(),
                String.format("Assigned %s as exam invigilator for %s (%s, %s)",
                        teacher.getFullName(), hall.getHallName(), examDate, timeSlot)
        );

        return saved;
    }
}

