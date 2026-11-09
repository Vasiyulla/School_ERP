package com.schoolerp.modules.examinations.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.examinations.entity.ExamHall;
import com.schoolerp.modules.examinations.entity.ExamSeatingSlot;
import com.schoolerp.modules.examinations.entity.InvigilationDuty;
import com.schoolerp.modules.examinations.service.ExamSeatingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/examinations")
@RequiredArgsConstructor
public class ExamSeatingController {

    private final ExamSeatingService seatingService;

    @PostMapping("/halls")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','EXAM_CONTROLLER')")
    @RequireFeature("examinations.seating_matrix")
    public ResponseEntity<ApiResponse<ExamHall>> createHall(@RequestBody CreateHallRequest request) {
        ExamHall hall = seatingService.createHall(
                request.getHallName(),
                request.getRoomNumber(),
                request.getTotalBenches(),
                request.getSeatsPerBench()
        );
        return ResponseEntity.ok(ApiResponse.success(hall, "Exam hall registered successfully"));
    }

    @GetMapping("/halls")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','EXAM_CONTROLLER')")
    @RequireFeature("examinations.seating_matrix")
    public ResponseEntity<ApiResponse<List<ExamHall>>> getHalls() {
        return ResponseEntity.ok(ApiResponse.success(seatingService.getHalls(), "Exam halls retrieved"));
    }

    @PostMapping("/seating/generate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','EXAM_CONTROLLER')")
    @RequireFeature("examinations.seating_matrix")
    public ResponseEntity<ApiResponse<List<ExamSeatingSlot>>> generateSeating(@RequestBody GenerateSeatingRequest request) {
        LocalDate examDate = request.getExamDate() != null ? request.getExamDate() : LocalDate.now().plusDays(1);
        List<ExamSeatingSlot> seating = seatingService.generateAntiCheatingSeatingPlan(
                request.getHallId(),
                examDate,
                request.getSession(),
                request.getSubject()
        );
        return ResponseEntity.ok(ApiResponse.success(seating, "Anti-cheating exam seating generated successfully"));
    }

    @GetMapping("/seating/hall/{hallId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','EXAM_CONTROLLER')")
    @RequireFeature("examinations.seating_matrix")
    public ResponseEntity<ApiResponse<List<ExamSeatingSlot>>> getSeatingForHall(
            @PathVariable UUID hallId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate examDate) {
        LocalDate targetDate = examDate != null ? examDate : LocalDate.now().plusDays(1);
        List<ExamSeatingSlot> seating = seatingService.getSeatingForHall(hallId, targetDate);
        return ResponseEntity.ok(ApiResponse.success(seating, "Hall seating retrieved"));
    }

    @PostMapping("/invigilation/assign")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','EXAM_CONTROLLER')")
    @RequireFeature("examinations.invigilation_roster")
    public ResponseEntity<ApiResponse<InvigilationDuty>> assignInvigilator(@RequestBody AssignInvigilatorRequest request) {
        LocalDate examDate = request.getExamDate() != null ? request.getExamDate() : LocalDate.now().plusDays(1);
        InvigilationDuty duty = seatingService.assignInvigilator(
                request.getHallId(),
                examDate,
                request.getTimeSlot(),
                request.getSubject(),
                request.getTeacherId()
        );
        return ResponseEntity.ok(ApiResponse.success(duty, "Invigilation duty assigned successfully"));
    }

    @Data
    public static class CreateHallRequest {
        private String hallName;
        private String roomNumber;
        private int totalBenches;
        private int seatsPerBench;
    }

    @Data
    public static class GenerateSeatingRequest {
        private UUID hallId;
        private LocalDate examDate;
        private String session;
        private String subject;
    }

    @Data
    public static class AssignInvigilatorRequest {
        private UUID hallId;
        private LocalDate examDate;
        private String timeSlot;
        private String subject;
        private UUID teacherId;
    }
}

