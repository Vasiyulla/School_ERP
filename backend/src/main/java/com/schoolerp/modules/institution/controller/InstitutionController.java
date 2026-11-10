package com.schoolerp.modules.institution.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.institution.entity.AcademicCalendarEvent;
import com.schoolerp.modules.institution.service.InstitutionCalendarService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionCalendarService calendarService;

    @PostMapping("/calendar/events")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL')")
    @RequireFeature("institution.calendar")
    public ResponseEntity<ApiResponse<AcademicCalendarEvent>> declareEvent(@RequestBody DeclareEventRequest request) {
        AcademicCalendarEvent event = calendarService.declareEvent(
                request.getEventTitle(),
                request.getEventType(),
                request.getStartDate(),
                request.getEndDate(),
                request.isWorkingDay(),
                request.getBellSchedulePreset(),
                request.getDescription()
        );
        return ResponseEntity.ok(ApiResponse.success(event, "Institutional calendar event saved"));
    }

    @GetMapping("/calendar/events")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','PARENT','STUDENT')")
    @RequireFeature("institution.calendar")
    public ResponseEntity<ApiResponse<List<AcademicCalendarEvent>>> getEvents() {
        return ResponseEntity.ok(ApiResponse.success(calendarService.getCalendarEvents(), "Academic calendar retrieved"));
    }

    @GetMapping("/calendar/is-working-day")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','PARENT','STUDENT')")
    @RequireFeature("institution.calendar")
    public ResponseEntity<ApiResponse<InstitutionCalendarService.WorkingDayStatusDTO>> isWorkingDay(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(calendarService.evaluateWorkingDay(date), "Working day status evaluated"));
    }

    @Data
    public static class DeclareEventRequest {
        private String eventTitle;
        private String eventType;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startDate;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate endDate;
        private boolean workingDay;
        private String bellSchedulePreset;
        private String description;
    }
}

