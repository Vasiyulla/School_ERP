package com.schoolerp.modules.attendance.repository;

import com.schoolerp.modules.attendance.entity.AttendanceCorrection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceCorrectionRepository extends JpaRepository<AttendanceCorrection, UUID> {
    List<AttendanceCorrection> findAllByTenantIdOrderByCreatedAtDesc(String tenantId);
}

