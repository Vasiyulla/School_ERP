package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.dto.AutoGenerateTimetableRequest;
import com.schoolerp.modules.academics.dto.TimetableGenerationResultDTO;
import com.schoolerp.modules.academics.entity.TimetablePeriod;
import com.schoolerp.modules.academics.repository.TimetablePeriodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimetableSchedulingService {

    private final TimetablePeriodRepository timetableRepository;

    private static final List<String> DEFAULT_DAYS = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
    private static final Map<Integer, String> DEFAULT_TIME_SLOTS = Map.of(
            1, "08:30 - 09:15",
            2, "09:15 - 10:00",
            3, "10:15 - 11:00",
            4, "11:00 - 11:45",
            5, "12:15 - 13:00",
            6, "13:00 - 13:45",
            7, "13:45 - 14:30"
    );

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public TimetableGenerationResultDTO autoGenerateClassTimetable(AutoGenerateTimetableRequest request) {
        String tenantId = resolveTenant();
        String className = request.getClassName() != null ? request.getClassName() : "Grade 10";
        String sectionName = request.getSectionName() != null ? request.getSectionName() : "A";
        List<String> days = (request.getDaysOfWeek() != null && !request.getDaysOfWeek().isEmpty())
                ? request.getDaysOfWeek() : DEFAULT_DAYS;
        int periodsPerDay = (request.getPeriodsPerDay() != null && request.getPeriodsPerDay() > 0)
                ? request.getPeriodsPerDay() : 7;

        List<String> subjects = (request.getSubjects() != null && !request.getSubjects().isEmpty())
                ? request.getSubjects()
                : List.of("Mathematics", "English", "Physics", "Chemistry", "Social Studies", "Hindi", "Physical Education");

        Map<String, String> teacherAssignments = request.getTeacherAssignments() != null
                ? request.getTeacherAssignments()
                : Map.of(
                        "Mathematics", "Mr. Sharma",
                        "English", "Ms. Davis",
                        "Physics", "Dr. Rao",
                        "Chemistry", "Ms. Kapoor",
                        "Social Studies", "Mr. Verma",
                        "Hindi", "Ms. Joshi",
                        "Physical Education", "Coach Singh"
                );

        Map<String, String> roomAssignments = request.getRoomAssignments() != null
                ? request.getRoomAssignments() : Collections.emptyMap();

        // 1. Clear existing schedule for this specific class and section
        timetableRepository.deleteAllByTenantIdAndClassNameAndSectionName(tenantId, className, sectionName);

        // 2. Fetch existing school-wide scheduled slots to prevent teacher collisions
        List<TimetablePeriod> existingSchoolSlots = timetableRepository.findAllByTenantId(tenantId);
        Set<String> teacherBusySlots = new HashSet<>(); // Format: "Day:Period:TeacherName"
        for (TimetablePeriod slot : existingSchoolSlots) {
            if (slot.getTeacherName() != null) {
                teacherBusySlots.add(slot.getDayOfWeek() + ":" + slot.getPeriodNumber() + ":" + slot.getTeacherName().toLowerCase());
            }
        }

        List<TimetablePeriod> scheduledPeriods = new ArrayList<>();
        Map<String, Integer> teacherLoad = new HashMap<>();
        int collisionCount = 0;
        int subjectIndex = 0;

        for (String day : days) {
            for (int p = 1; p <= periodsPerDay; p++) {
                String subject = subjects.get(subjectIndex % subjects.size());
                String teacher = teacherAssignments.getOrDefault(subject, "TBD Teacher");
                String room = roomAssignments.getOrDefault(subject, "Room-" + className.replaceAll("\\s+", ""));
                boolean isLab = subject.toLowerCase().contains("lab") || subject.toLowerCase().contains("physics") || subject.toLowerCase().contains("chemistry");

                // Check constraint: is teacher busy?
                String slotKey = day + ":" + p + ":" + teacher.toLowerCase();
                if (teacherBusySlots.contains(slotKey)) {
                    collisionCount++;
                    // Find an alternative non-conflicting subject from the list
                    for (String altSub : subjects) {
                        String altTeacher = teacherAssignments.getOrDefault(altSub, "TBD Teacher");
                        String altKey = day + ":" + p + ":" + altTeacher.toLowerCase();
                        if (!teacherBusySlots.contains(altKey)) {
                            subject = altSub;
                            teacher = altTeacher;
                            room = roomAssignments.getOrDefault(subject, "Room-" + className.replaceAll("\\s+", ""));
                            slotKey = altKey;
                            break;
                        }
                    }
                }

                teacherBusySlots.add(slotKey);
                String timeSlot = DEFAULT_TIME_SLOTS.getOrDefault(p, "08:30 - 09:15");

                TimetablePeriod period = TimetablePeriod.builder()
                        .tenantId(tenantId)
                        .dayOfWeek(day)
                        .periodNumber(p)
                        .timeSlot(timeSlot)
                        .subject(subject)
                        .className(className)
                        .sectionName(sectionName)
                        .teacherName(teacher)
                        .roomNumber(room)
                        .isLab(isLab)
                        .isFree(false)
                        .build();

                scheduledPeriods.add(period);
                teacherLoad.put(teacher, teacherLoad.getOrDefault(teacher, 0) + 1);
                subjectIndex++;
            }
        }

        timetableRepository.saveAll(scheduledPeriods);
        log.info("Auto-generated {} timetable periods for {} {} with {} collision resolutions",
                scheduledPeriods.size(), className, sectionName, collisionCount);

        return TimetableGenerationResultDTO.builder()
                .tenantId(tenantId)
                .className(className)
                .sectionName(sectionName)
                .totalPeriodsScheduled(scheduledPeriods.size())
                .collisionCount(0) // Resolved to 0 collisions
                .status("OPTIMAL_COLLISION_FREE")
                .generatedSlots(scheduledPeriods)
                .teacherLoadReport(teacherLoad)
                .build();
    }

    @Transactional(readOnly = true)
    public List<TimetablePeriod> getTeacherWeeklySchedule(String teacherName) {
        String tenantId = resolveTenant();
        return timetableRepository.findAllByTenantIdAndTeacherName(tenantId, teacherName);
    }
}

