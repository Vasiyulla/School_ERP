package com.schoolerp.modules.institution.repository;

import com.schoolerp.modules.institution.entity.AcademicCalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AcademicCalendarEventRepository extends JpaRepository<AcademicCalendarEvent, UUID> {
    List<AcademicCalendarEvent> findByTenantIdOrderByStartDateAsc(String tenantId);
    List<AcademicCalendarEvent> findByTenantIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String tenantId, LocalDate targetEnd, LocalDate targetStart);
}

