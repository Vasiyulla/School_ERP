package com.schoolerp.modules.students.service;

import com.schoolerp.modules.students.dto.SaveHealthPassportRequest;
import com.schoolerp.modules.students.dto.StudentHealthPassportDTO;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.entity.StudentHealthPassport;
import com.schoolerp.modules.students.repository.StudentHealthPassportRepository;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthPassportService {

    private final StudentHealthPassportRepository healthPassportRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public StudentHealthPassportDTO saveHealthPassport(java.util.UUID studentId, SaveHealthPassportRequest req) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        StudentHealthPassport passport = healthPassportRepository.findByStudentId(studentId)
                .orElseGet(() -> StudentHealthPassport.builder()
                        .tenantId(student.getTenantId() != null ? student.getTenantId() : "tenant_apex_mumbai_01")
                        .studentId(studentId)
                        .build());

        passport.setBloodGroup(req.getBloodGroup() != null ? req.getBloodGroup() : "B+");
        passport.setHasSevereAllergies(Boolean.TRUE.equals(req.getHasSevereAllergies()));
        passport.setAllergenList(req.getAllergenList());
        passport.setChronicConditions(req.getChronicConditions());
        passport.setInhalerInInfirmary(Boolean.TRUE.equals(req.getInhalerInInfirmary()));
        passport.setEpipenPrescribed(Boolean.TRUE.equals(req.getEpipenPrescribed()));
        passport.setEmergencyContactPhone(req.getEmergencyContactPhone() != null ? req.getEmergencyContactPhone() : student.getGuardianPhone());
        passport.setTreatingPhysicianName(req.getTreatingPhysicianName());
        passport.setParamedicEmergencyProtocol(req.getParamedicEmergencyProtocol() != null ? req.getParamedicEmergencyProtocol() : "Immediate Paramedic Evaluation");
        passport.setCreatedAt(LocalDateTime.now());

        StudentHealthPassport saved = healthPassportRepository.save(passport);
        log.info("Health Passport saved for student: {} ({})", student.getFullName(), saved.getBloodGroup());
        return mapToDTO(saved, student);
    }

    @Transactional(readOnly = true)
    public StudentHealthPassportDTO getHealthPassport(java.util.UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        StudentHealthPassport passport = healthPassportRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("No Health Passport recorded for student: " + studentId));

        return mapToDTO(passport, student);
    }

    private StudentHealthPassportDTO mapToDTO(StudentHealthPassport p, Student student) {
        StudentHealthPassportDTO dto = new StudentHealthPassportDTO();
        dto.setId(p.getId());
        dto.setStudentId(p.getStudentId());
        dto.setStudentName(student != null ? student.getFullName() : null);
        dto.setStudentAdmissionNumber(student != null ? student.getAdmissionNumber() : null);
        dto.setBloodGroup(p.getBloodGroup());
        dto.setHasSevereAllergies(p.isHasSevereAllergies());
        dto.setAllergenList(p.getAllergenList());
        dto.setChronicConditions(p.getChronicConditions());
        dto.setInhalerInInfirmary(p.isInhalerInInfirmary());
        dto.setEpipenPrescribed(p.isEpipenPrescribed());
        dto.setEmergencyContactPhone(p.getEmergencyContactPhone());
        dto.setTreatingPhysicianName(p.getTreatingPhysicianName());
        dto.setParamedicEmergencyProtocol(p.getParamedicEmergencyProtocol());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}
