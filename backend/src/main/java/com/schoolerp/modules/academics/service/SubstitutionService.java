package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.dto.SubstitutionRecommendationDTO;
import com.schoolerp.modules.academics.dto.SubstitutionRecommendationDTO.AvailableSubstituteDTO;
import com.schoolerp.modules.academics.entity.TeacherSubstitution;
import com.schoolerp.modules.academics.entity.TimetablePeriod;
import com.schoolerp.modules.academics.repository.TeacherSubstitutionRepository;
import com.schoolerp.modules.academics.repository.TimetablePeriodRepository;
import com.schoolerp.modules.communication.service.CommunicationService;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubstitutionService {

    private final TimetablePeriodRepository timetableRepository;
    private final TeacherSubstitutionRepository substitutionRepository;
    private final EmployeeRepository employeeRepository;
    private final CommunicationService communicationService;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public List<SubstitutionRecommendationDTO> getRecommendations(LocalDate date, UUID absentTeacherId) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        Employee absentTeacher = employeeRepository.findById(absentTeacherId)
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + absentTeacherId));

        // Find periods assigned to absent teacher on this day of week
        List<TimetablePeriod> periods = timetableRepository
                .findAllByTenantIdAndTeacherNameAndDayOfWeek(tenantId, absentTeacher.getFullName(), dayOfWeek);

        if (periods.isEmpty()) {
            // Check fallback periods on that day
            periods = timetableRepository.findAllByTenantIdAndDayOfWeekOrderByPeriodNumberAsc(tenantId, dayOfWeek)
                    .stream()
                    .filter(p -> p.getTeacherName() != null && p.getTeacherName().contains(absentTeacher.getFullName().split(" ")[1]))
                    .toList();
        }

        // Fetch all active faculty members in the school
        List<Employee> allFaculty = employeeRepository.findAllByTenantId(tenantId).stream()
                .filter(e -> "ACTIVE".equalsIgnoreCase(e.getStatus()) && !e.getId().equals(absentTeacherId))
                .toList();

        List<TimetablePeriod> allDayPeriods = timetableRepository
                .findAllByTenantIdAndDayOfWeekOrderByPeriodNumberAsc(tenantId, dayOfWeek);

        List<SubstitutionRecommendationDTO> recommendations = new ArrayList<>();

        for (TimetablePeriod period : periods) {
            List<AvailableSubstituteDTO> candidates = new ArrayList<>();

            for (Employee faculty : allFaculty) {
                // Check if candidate faculty is busy during this specific period
                boolean isBusy = allDayPeriods.stream().anyMatch(p ->
                        p.getPeriodNumber() == period.getPeriodNumber() &&
                        p.getTeacherName() != null &&
                        p.getTeacherName().equalsIgnoreCase(faculty.getFullName()) &&
                        !p.isFree()
                );

                if (!isBusy) {
                    boolean isSameDept = faculty.getDepartment().equalsIgnoreCase(absentTeacher.getDepartment());
                    String suitability = isSameDept ? "SAME_DEPARTMENT_EXPERT" : "FREE_SENIOR_FACULTY";

                    candidates.add(AvailableSubstituteDTO.builder()
                            .teacherId(faculty.getId())
                            .teacherName(faculty.getFullName())
                            .department(faculty.getDepartment())
                            .designation(faculty.getDesignation())
                            .matchSuitability(suitability)
                            .currentDailyPeriods(3)
                            .build());
                }
            }

            // Sort candidates: Same department experts first
            candidates.sort(Comparator.comparing(AvailableSubstituteDTO::getMatchSuitability));

            recommendations.add(SubstitutionRecommendationDTO.builder()
                    .periodNumber(period.getPeriodNumber())
                    .timeSlot(period.getTimeSlot())
                    .className(period.getClassName())
                    .sectionName(period.getSectionName())
                    .subject(period.getSubject())
                    .roomNumber(period.getRoomNumber())
                    .absentTeacherName(absentTeacher.getFullName())
                    .absentTeacherId(absentTeacher.getId())
                    .candidateSubstitutes(candidates)
                    .build());
        }

        return recommendations;
    }

    @Transactional
    public TeacherSubstitution assignSubstitution(LocalDate date, int periodNumber, String timeSlot,
                                                 String className, String sectionName, String subject,
                                                 String roomNumber, UUID absentTeacherId, String absentTeacherName,
                                                 UUID substituteTeacherId, String substituteTeacherName,
                                                 String assignedBy) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        TeacherSubstitution substitution = TeacherSubstitution.builder()
                .tenantId(tenantId)
                .substitutionDate(date)
                .dayOfWeek(dayOfWeek)
                .periodNumber(periodNumber)
                .timeSlot(timeSlot)
                .className(className)
                .sectionName(sectionName)
                .subject(subject)
                .roomNumber(roomNumber)
                .absentTeacherId(absentTeacherId)
                .absentTeacherName(absentTeacherName)
                .substituteTeacherId(substituteTeacherId)
                .substituteTeacherName(substituteTeacherName)
                .status("ASSIGNED")
                .assignedBy(assignedBy)
                .assignedAt(Instant.now())
                .build();

        TeacherSubstitution saved = substitutionRepository.save(substitution);

        // Instant notification to the substitute teacher via FCM Push / Alert
        String alertMsg = String.format("Substitution Notice: You have been assigned %s Period %d (%s, %s - %s) for %s on %s.",
                subject, periodNumber, timeSlot, className, sectionName, absentTeacherName, date);

        communicationService.sendPushNotification(
                substituteTeacherId,
                substituteTeacherName,
                "Period Substitution Assigned",
                alertMsg,
                "TEACHER_SUBSTITUTION"
        );

        auditLogService.recordEvent(
                "ASSIGN_TEACHER_SUBSTITUTION",
                "academics.substitutions",
                saved.getId().toString(),
                String.format("Assigned %s to substitute for %s (Period %d, %s %s) on %s",
                        substituteTeacherName, absentTeacherName, periodNumber, className, sectionName, date)
        );

        return saved;
    }

    public List<TeacherSubstitution> getSubstitutionsForDate(LocalDate date) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return substitutionRepository
                .findByTenantIdAndSubstitutionDateOrderByPeriodNumberAsc(tenantId, date);
    }
}
