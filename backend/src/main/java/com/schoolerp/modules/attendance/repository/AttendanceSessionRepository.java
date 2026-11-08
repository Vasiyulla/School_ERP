package com.schoolerp.modules.attendance.repository;

import com.schoolerp.modules.attendance.entity.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, UUID> {
    List<AttendanceSession> findAllByTenantId(String tenantId);
    Optional<AttendanceSession> findByTenantIdAndClassNameAndSectionNameAndAttendanceDate(
            String tenantId, String className, String sectionName, LocalDate date);
}

