package com.schoolerp.modules.examinations.service;

import com.schoolerp.modules.examinations.dto.*;
import com.schoolerp.modules.examinations.entity.ExamInvigilationDuty;
import com.schoolerp.modules.examinations.entity.UfmMalpracticeDocket;
import com.schoolerp.modules.examinations.repository.ExamInvigilationDutyRepository;
import com.schoolerp.modules.examinations.repository.UfmMalpracticeDocketRepository;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamInvigilationService {

    private final ExamInvigilationDutyRepository invigilationDutyRepository;
    private final UfmMalpracticeDocketRepository ufmDocketRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public ExamInvigilationDutyDTO scheduleInvigilation(ScheduleInvigilationRequest req) {
        String subject = req.getSubjectName() != null ? req.getSubjectName() : "Board Mathematics";

        Employee teacher = null;
        if (req.getInvigilatorStaffId() != null) {
            teacher = employeeRepository.findById(req.getInvigilatorStaffId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + req.getInvigilatorStaffId()));

            // Anti-cheating rule: A teacher cannot invigilate their own subject
            if (teacher.getDepartment() != null && subject != null &&
                    teacher.getDepartment().trim().equalsIgnoreCase(subject.trim())) {
                throw new IllegalStateException("Anti-cheating violation: Teacher " + teacher.getFullName() +
                        " belongs to department '" + teacher.getDepartment() + "' and cannot invigilate subject '" + subject + "'");
            }
        } else {
            // Auto-assign first available teacher whose department does not match
            List<Employee> all = employeeRepository.findAll();
            for (Employee emp : all) {
                if (emp.getDepartment() == null || !emp.getDepartment().trim().equalsIgnoreCase(subject.trim())) {
                    teacher = emp;
                    break;
                }
            }
            if (teacher == null && !all.isEmpty()) {
                teacher = all.get(0);
            }
        }

        ExamInvigilationDuty duty = ExamInvigilationDuty.builder()
                .tenantId(teacher != null && teacher.getTenantId() != null ? teacher.getTenantId() : "tenant_apex_mumbai_01")
                .examScheduleId(req.getExamScheduleId() != null ? req.getExamScheduleId() : UUID.randomUUID())
                .examName(req.getExamName() != null ? req.getExamName() : "CBSE Term-2 Board Examination")
                .subjectName(subject)
                .examDate(req.getExamDate() != null ? req.getExamDate() : LocalDate.now())
                .hallRoomNumber(req.getHallRoomNumber() != null ? req.getHallRoomNumber() : "Room 101")
                .invigilatorStaffId(teacher != null ? teacher.getId() : UUID.randomUUID())
                .invigilatorStaffName(teacher != null ? teacher.getFullName() : "Chief Invigilator")
                .dutyRole(req.getDutyRole() != null ? req.getDutyRole() : "PRIMARY_INVIGILATOR")
                .paperSealVerified(true)
                .sealVerificationTimestamp(LocalDateTime.now())
                .witnessStaffName(req.getDutyRole() != null ? "Asst. Invigilator" : null)
                .createdAt(LocalDateTime.now())
                .build();

        ExamInvigilationDuty saved = invigilationDutyRepository.save(duty);
        log.info("Exam invigilation scheduled: Teacher {} for room {}",
                saved.getInvigilatorStaffName(), saved.getHallRoomNumber());

        return mapToDutyDTO(saved);
    }

    @Transactional
    public ExamInvigilationDutyDTO verifySeals(UUID dutyId, VerifySealsRequest req) {
        ExamInvigilationDuty duty = invigilationDutyRepository.findById(dutyId)
                .orElseThrow(() -> new IllegalArgumentException("Invigilation duty record not found: " + dutyId));

        if (req.getPaperSealVerified() != null) {
            duty.setPaperSealVerified(req.getPaperSealVerified());
        }
        if (req.getWitnessStaffName() != null) {
            duty.setWitnessStaffName(req.getWitnessStaffName());
        }
        duty.setSealVerificationTimestamp(LocalDateTime.now());

        ExamInvigilationDuty saved = invigilationDutyRepository.save(duty);
        log.info("Exam seals verified for duty {}: Seal Verified={}", dutyId, saved.isPaperSealVerified());
        return mapToDutyDTO(saved);
    }

    @Transactional
    public UfmMalpracticeDocketDTO reportUfm(ReportUfmRequest req) {
        Student student = studentRepository.findById(req.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + req.getStudentId()));

        String docketNo = "UFM-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        UfmMalpracticeDocket docket = UfmMalpracticeDocket.builder()
                .tenantId(student.getTenantId() != null ? student.getTenantId() : "tenant_apex_mumbai_01")
                .docketNumber(docketNo)
                .examScheduleId(req.getExamScheduleId() != null ? req.getExamScheduleId() : UUID.randomUUID())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .admissionNumber(student.getAdmissionNumber())
                .subjectName(req.getSubjectName() != null ? req.getSubjectName() : "Board Mathematics")
                .incidentTimestamp(LocalDateTime.now())
                .contrabandType(req.getContrabandType() != null ? req.getContrabandType() : "UNAUTHORIZED_NOTES")
                .invigilatorRemarks(req.getInvigilatorRemarks() != null ? req.getInvigilatorRemarks() : "Candidate found with unauthorized notes")
                .studentStatement(req.getStudentStatement())
                .verdictStatus("UNDER_COMMITTEE_REVIEW")
                .createdAt(LocalDateTime.now())
                .build();

        UfmMalpracticeDocket saved = ufmDocketRepository.save(docket);
        log.warn("UFM Malpractice reported! Docket: {}, Student: {}", saved.getDocketNumber(), saved.getStudentName());
        return mapToDocketDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<UfmMalpracticeDocketDTO> getUfmDockets(String status) {
        List<UfmMalpracticeDocket> list;
        if (status != null && !status.isBlank()) {
            list = ufmDocketRepository.findByVerdictStatus(status);
        } else {
            list = ufmDocketRepository.findAll();
        }
        return list.stream().map(this::mapToDocketDTO).collect(Collectors.toList());
    }

    private ExamInvigilationDutyDTO mapToDutyDTO(ExamInvigilationDuty d) {
        ExamInvigilationDutyDTO dto = new ExamInvigilationDutyDTO();
        dto.setId(d.getId());
        dto.setExamScheduleId(d.getExamScheduleId());
        dto.setExamName(d.getExamName());
        dto.setSubjectName(d.getSubjectName());
        dto.setExamDate(d.getExamDate());
        dto.setHallRoomNumber(d.getHallRoomNumber());
        dto.setInvigilatorStaffId(d.getInvigilatorStaffId());
        dto.setInvigilatorStaffName(d.getInvigilatorStaffName());
        dto.setDutyRole(d.getDutyRole());
        dto.setPaperSealVerified(d.isPaperSealVerified());
        dto.setSealVerificationTimestamp(d.getSealVerificationTimestamp());
        dto.setWitnessStaffName(d.getWitnessStaffName());
        dto.setCreatedAt(d.getCreatedAt());
        return dto;
    }

    private UfmMalpracticeDocketDTO mapToDocketDTO(UfmMalpracticeDocket d) {
        UfmMalpracticeDocketDTO dto = new UfmMalpracticeDocketDTO();
        dto.setId(d.getId());
        dto.setDocketNumber(d.getDocketNumber());
        dto.setExamScheduleId(d.getExamScheduleId());
        dto.setStudentId(d.getStudentId());
        dto.setStudentName(d.getStudentName());
        dto.setAdmissionNumber(d.getAdmissionNumber());
        dto.setSubjectName(d.getSubjectName());
        dto.setIncidentTimestamp(d.getIncidentTimestamp());
        dto.setContrabandType(d.getContrabandType());
        dto.setInvigilatorRemarks(d.getInvigilatorRemarks());
        dto.setStudentStatement(d.getStudentStatement());
        dto.setVerdictStatus(d.getVerdictStatus());
        dto.setVerdictDate(d.getVerdictDate());
        dto.setCreatedAt(d.getCreatedAt());
        return dto;
    }
}
