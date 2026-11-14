package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.VirtualClassroomSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface VirtualClassroomSessionRepository extends JpaRepository<VirtualClassroomSession, UUID> {
    List<VirtualClassroomSession> findByTenantIdAndSessionDateAndClassNameAndSectionNameOrderByPeriodNumberAsc(
            String tenantId, LocalDate sessionDate, String className, String sectionName);

    List<VirtualClassroomSession> findByTenantIdAndSessionDateOrderByPeriodNumberAsc(
            String tenantId, LocalDate sessionDate);

    List<VirtualClassroomSession> findByTenantIdAndTeacherNameAndSessionDateOrderByPeriodNumberAsc(
            String tenantId, String teacherName, LocalDate sessionDate);
}

