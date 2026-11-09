package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.TimetablePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimetablePeriodRepository extends JpaRepository<TimetablePeriod, UUID> {
    List<TimetablePeriod> findAllByTenantIdAndDayOfWeekOrderByPeriodNumberAsc(String tenantId, String dayOfWeek);
    List<TimetablePeriod> findAllByTenantId(String tenantId);
    List<TimetablePeriod> findAllByTenantIdAndTeacherNameAndDayOfWeek(String tenantId, String teacherName, String dayOfWeek);
}

