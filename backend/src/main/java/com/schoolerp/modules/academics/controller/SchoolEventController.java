package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.dto.CreateSchoolEventRequest;
import com.schoolerp.modules.academics.dto.EnrollParticipantRequest;
import com.schoolerp.modules.academics.dto.RecordParticipantResultRequest;
import com.schoolerp.modules.academics.entity.EventParticipant;
import com.schoolerp.modules.academics.entity.SchoolEvent;
import com.schoolerp.modules.academics.service.SchoolEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academics/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Teacher School Event & Student Participation Engine", description = "Organize school co-curricular events, enroll class cohorts, record achievements, issue certificates, and award house points")
public class SchoolEventController {

    private final SchoolEventService schoolEventService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Create a new school co-curricular or sports event")
    public ResponseEntity<ApiResponse<SchoolEvent>> createEvent(
            @Valid @RequestBody CreateSchoolEventRequest request,
            Authentication auth) {
        String teacherName = (auth != null) ? auth.getName() : "Faculty Coordinator";
        SchoolEvent event = schoolEventService.createEvent(request, teacherName);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Event created successfully", event));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'STUDENT', 'PARENT')")
    @Operation(summary = "List school events with optional category and status filtering")
    public ResponseEntity<ApiResponse<List<SchoolEvent>>> getEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean upcomingOnly) {
        List<SchoolEvent> events = schoolEventService.getEvents(category, status, upcomingOnly);
        return ResponseEntity.ok(ApiResponse.ok(events));
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'STUDENT', 'PARENT')")
    @Operation(summary = "Get detailed information about a school event")
    public ResponseEntity<ApiResponse<SchoolEvent>> getEventById(@PathVariable UUID eventId) {
        SchoolEvent event = schoolEventService.getEventById(eventId);
        return ResponseEntity.ok(ApiResponse.ok(event));
    }

    @PutMapping("/{eventId}/status")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Update school event lifecycle status (PUBLISHED, IN_PROGRESS, CONCLUDED, CANCELLED)")
    public ResponseEntity<ApiResponse<SchoolEvent>> updateEventStatus(
            @PathVariable UUID eventId,
            @RequestParam String status) {
        SchoolEvent event = schoolEventService.updateEventStatus(eventId, status);
        return ResponseEntity.ok(ApiResponse.ok(event));
    }

    @PostMapping("/{eventId}/participants")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Enroll participating students into a school event with roles")
    public ResponseEntity<ApiResponse<List<EventParticipant>>> enrollParticipants(
            @PathVariable UUID eventId,
            @Valid @RequestBody EnrollParticipantRequest request) {
        List<EventParticipant> participants = schoolEventService.enrollParticipants(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Participants enrolled successfully", participants));
    }

    @PostMapping("/{eventId}/enroll-class")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Bulk enroll an entire class and section cohort into an event")
    public ResponseEntity<ApiResponse<List<EventParticipant>>> enrollClassCohort(
            @PathVariable UUID eventId,
            @RequestParam String className,
            @RequestParam(required = false, defaultValue = "ALL") String sectionName,
            @RequestParam(required = false, defaultValue = "PARTICIPANT") String role) {
        List<EventParticipant> participants = schoolEventService.enrollClassSection(eventId, className, sectionName, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Class cohort enrolled successfully", participants));
    }

    @GetMapping("/{eventId}/participants")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'STUDENT', 'PARENT')")
    @Operation(summary = "List all enrolled student participants for a school event")
    public ResponseEntity<ApiResponse<List<EventParticipant>>> getEventParticipants(@PathVariable UUID eventId) {
        List<EventParticipant> participants = schoolEventService.getEventParticipants(eventId);
        return ResponseEntity.ok(ApiResponse.ok(participants));
    }

    @PutMapping("/{eventId}/participants/{participantId}/results")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Record participation results, achievement rank, house points, and digital certificate")
    public ResponseEntity<ApiResponse<EventParticipant>> recordParticipantResult(
            @PathVariable UUID eventId,
            @PathVariable UUID participantId,
            @RequestBody RecordParticipantResultRequest request,
            Authentication auth) {
        String teacherName = (auth != null) ? auth.getName() : "Evaluating Faculty";
        EventParticipant updated = schoolEventService.recordParticipantResult(eventId, participantId, request, teacherName);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @GetMapping(value = "/{eventId}/roster/print", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Render a high-contrast print-ready call sheet for event day")
    public ResponseEntity<String> getEventCallSheet(@PathVariable UUID eventId) {
        String htmlRoster = schoolEventService.generateEventCallSheetHtml(eventId);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlRoster);
    }
}

