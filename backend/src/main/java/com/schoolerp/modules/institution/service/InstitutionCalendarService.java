package com.schoolerp.modules.institution.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.institution.entity.AcademicCalendarEvent;
import com.schoolerp.modules.institution.repository.AcademicCalendarEventRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionCalendarService {

    private final AcademicCalendarEventRepository calendarRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public AcademicCalendarEvent declareEvent(
            String eventTitle,
            String eventType,
            LocalDate startDate,
            LocalDate endDate,
            boolean workingDay,
            String bellSchedulePreset,
            String description) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        LocalDate start = startDate != null ? startDate : LocalDate.now();
        LocalDate end = (endDate != null && !endDate.isBefore(start)) ? endDate : start;

        AcademicCalendarEvent event = AcademicCalendarEvent.builder()
                .tenantId(tenantId)
                .eventTitle(eventTitle)
                .eventType(eventType != null ? eventType.toUpperCase() : "HOLIDAY")
                .startDate(start)
                .endDate(end)
                .workingDay(workingDay)
                .bellSchedulePreset(bellSchedulePreset != null ? bellSchedulePreset : (workingDay ? "STANDARD_8_PERIODS" : "CLOSED"))
                .description(description)
                .createdAt(Instant.now())
                .build();

        AcademicCalendarEvent saved = calendarRepository.save(event);

        auditLogService.recordEvent(
                "DECLARE_INSTITUTION_CALENDAR_EVENT",
                "institution.academic_calendar_events",
                saved.getId().toString(),
                String.format("Declared %s '%s' (%s to %s), workingDay=%s",
                        saved.getEventType(), saved.getEventTitle(), saved.getStartDate(), saved.getEndDate(), saved.isWorkingDay())
        );

        return saved;
    }

    public List<AcademicCalendarEvent> getCalendarEvents() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<AcademicCalendarEvent> events = calendarRepository.findByTenantIdOrderByStartDateAsc(tenantId);
        if (events.isEmpty()) {
            declareEvent("Republic Day National Holiday", "HOLIDAY",
                    LocalDate.of(LocalDate.now().getYear(), 1, 26),
                    LocalDate.of(LocalDate.now().getYear(), 1, 26),
                    false, "CLOSED", "National Gazetted Holiday");
            return calendarRepository.findByTenantIdOrderByStartDateAsc(tenantId);
        }
        return events;
    }

    public WorkingDayStatusDTO evaluateWorkingDay(LocalDate targetDate) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        LocalDate date = targetDate != null ? targetDate : LocalDate.now();

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return WorkingDayStatusDTO.builder()
                    .date(date)
                    .workingDay(false)
                    .reason("Weekly Off (Sunday)")
                    .bellSchedulePreset("CLOSED")
                    .attendanceRollCallRequired(false)
                    .build();
        }

        List<AcademicCalendarEvent> matching = calendarRepository
                .findByTenantIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(tenantId, date, date);

        for (AcademicCalendarEvent ev : matching) {
            if (!ev.isWorkingDay()) {
                return WorkingDayStatusDTO.builder()
                        .date(date)
                        .workingDay(false)
                        .reason(ev.getEventType() + ": " + ev.getEventTitle())
                        .bellSchedulePreset(ev.getBellSchedulePreset())
                        .attendanceRollCallRequired(false)
                        .build();
            }
        }

        String preset = matching.isEmpty() ? "STANDARD_8_PERIODS" : matching.get(0).getBellSchedulePreset();
        return WorkingDayStatusDTO.builder()
                .date(date)
                .workingDay(true)
                .reason(matching.isEmpty() ? "Regular Instructional Day" : matching.get(0).getEventTitle())
                .bellSchedulePreset(preset)
                .attendanceRollCallRequired(true)
                .build();
    }

    @Data
    @Builder
    public static class WorkingDayStatusDTO {
        private LocalDate date;
        private boolean workingDay;
        private String reason;
        private String bellSchedulePreset;
        private boolean attendanceRollCallRequired;
    }
}

