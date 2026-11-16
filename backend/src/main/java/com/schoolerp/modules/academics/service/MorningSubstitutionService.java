package com.schoolerp.modules.academics.service;

import com.schoolerp.modules.academics.dto.DailySubstitutionDTO;
import com.schoolerp.modules.academics.dto.MorningSubstitutionRequest;
import com.schoolerp.modules.academics.entity.ClassSection;
import com.schoolerp.modules.academics.entity.DailySubstitution;
import com.schoolerp.modules.academics.repository.ClassSectionRepository;
import com.schoolerp.modules.academics.repository.DailySubstitutionRepository;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MorningSubstitutionService {

    private final DailySubstitutionRepository dailySubstitutionRepository;
    private final EmployeeRepository employeeRepository;
    private final ClassSectionRepository classSectionRepository;

    @Transactional
    public DailySubstitutionDTO autoAssignSubstitution(MorningSubstitutionRequest request) {
        LocalDate date = request.getSubstitutionDate() != null ? request.getSubstitutionDate() : LocalDate.now();
        int maxDaily = request.getMaxDailySubstitutionsPerTeacher() != null ? request.getMaxDailySubstitutionsPerTeacher() : 2;

        Employee absentTeacher = employeeRepository.findById(request.getAbsentTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Absent teacher not found: " + request.getAbsentTeacherId()));

        ClassSection section = null;
        if (request.getSectionId() != null) {
            section = classSectionRepository.findById(request.getSectionId()).orElse(null);
        }

        List<DailySubstitution> existingToday = dailySubstitutionRepository.findBySubstitutionDate(date);
        Map<UUID, Long> dutyCounts = existingToday.stream()
                .filter(s -> s.getSubstituteTeacherId() != null)
                .collect(Collectors.groupingBy(DailySubstitution::getSubstituteTeacherId, Collectors.counting()));

        List<Employee> allEmployees = employeeRepository.findAll();
        List<Employee> eligibleTeachers = allEmployees.stream()
                .filter(e -> !e.getId().equals(request.getAbsentTeacherId()))
                .filter(e -> dutyCounts.getOrDefault(e.getId(), 0L) < maxDaily)
                .sorted(Comparator.comparingLong(e -> dutyCounts.getOrDefault(e.getId(), 0L)))
                .toList();

        if (eligibleTeachers.isEmpty()) {
            throw new IllegalStateException("No eligible substitute teacher found within daily equity limit (" + maxDaily + " periods/day)");
        }

        Employee selectedSubstitute = eligibleTeachers.get(0);

        String cName = section != null ? section.getClassName() : "Class 10";
        String sName = section != null ? section.getSectionName() : "A";
        UUID classroomId = request.getSectionId() != null ? request.getSectionId() : UUID.randomUUID();

        DailySubstitution substitution = DailySubstitution.builder()
                .tenantId(absentTeacher.getTenantId() != null ? absentTeacher.getTenantId() : "tenant_apex_mumbai_01")
                .substitutionDate(date)
                .periodNumber(request.getPeriodNumber() != null ? request.getPeriodNumber() : 1)
                .classroomId(classroomId)
                .className(cName)
                .sectionName(sName)
                .absentTeacherId(absentTeacher.getId())
                .absentTeacherName(absentTeacher.getFullName())
                .substituteTeacherId(selectedSubstitute.getId())
                .substituteTeacherName(selectedSubstitute.getFullName())
                .subjectName(request.getSubjectName() != null ? request.getSubjectName() : "General Studies")
                .lessonHandoffBrief("Morning auto-assigned substitution; covers syllabus topic handoff")
                .status("ASSIGNED")
                .createdAt(LocalDateTime.now())
                .build();

        DailySubstitution saved = dailySubstitutionRepository.save(substitution);
        log.info("Morning substitution auto-assigned: {} covering for {} (Period {}, Class {} - {})",
                selectedSubstitute.getFullName(), absentTeacher.getFullName(), saved.getPeriodNumber(), saved.getClassName(), saved.getSectionName());

        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<DailySubstitutionDTO> getDailyRoster(LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        return dailySubstitutionRepository.findBySubstitutionDate(targetDate).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private DailySubstitutionDTO mapToDTO(DailySubstitution s) {
        DailySubstitutionDTO dto = new DailySubstitutionDTO();
        dto.setId(s.getId());
        dto.setSubstitutionDate(s.getSubstitutionDate());
        dto.setPeriodNumber(s.getPeriodNumber());
        dto.setClassroomId(s.getClassroomId());
        dto.setClassName(s.getClassName());
        dto.setSectionName(s.getSectionName());
        dto.setAbsentTeacherId(s.getAbsentTeacherId());
        dto.setAbsentTeacherName(s.getAbsentTeacherName());
        dto.setSubstituteTeacherId(s.getSubstituteTeacherId());
        dto.setSubstituteTeacherName(s.getSubstituteTeacherName());
        dto.setSubjectName(s.getSubjectName());
        dto.setLessonHandoffBrief(s.getLessonHandoffBrief());
        dto.setStatus(s.getStatus());
        dto.setCreatedAt(s.getCreatedAt());
        return dto;
    }
}
