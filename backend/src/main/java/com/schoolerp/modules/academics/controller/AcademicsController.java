package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.entity.ClassSection;
import com.schoolerp.modules.academics.entity.TimetablePeriod;
import com.schoolerp.modules.academics.repository.ClassSectionRepository;
import com.schoolerp.modules.academics.repository.TimetablePeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academics")
@RequiredArgsConstructor
public class AcademicsController {

    private final ClassSectionRepository classSectionRepository;
    private final TimetablePeriodRepository timetableRepository;

    @GetMapping("/sections")
    public ResponseEntity<ApiResponse<List<ClassSection>>> getSections() {
        List<ClassSection> sections = classSectionRepository.findAllByTenantId(TenantContext.getTenantId());
        return ResponseEntity.ok(ApiResponse.ok(sections));
    }

    @GetMapping("/timetable")
    public ResponseEntity<ApiResponse<List<TimetablePeriod>>> getTimetable(
            @RequestParam(required = false) String dayOfWeek) {
        String tenantId = TenantContext.getTenantId();
        List<TimetablePeriod> periods = (dayOfWeek != null && !dayOfWeek.isBlank())
                ? timetableRepository.findAllByTenantIdAndDayOfWeekOrderByPeriodNumberAsc(tenantId, dayOfWeek)
                : timetableRepository.findAllByTenantId(tenantId);
        return ResponseEntity.ok(ApiResponse.ok(periods));
    }
}

