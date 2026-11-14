package com.schoolerp.modules.attendance.repository;

import com.schoolerp.modules.attendance.entity.BiometricPunchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BiometricPunchLogRepository extends JpaRepository<BiometricPunchLog, UUID> {
    List<BiometricPunchLog> findAllByTenantId(String tenantId);
    List<BiometricPunchLog> findAllByTenantIdAndPunchTimestampBetween(String tenantId, LocalDateTime start, LocalDateTime end);
    List<BiometricPunchLog> findAllByTenantIdAndStaffCode(String tenantId, String staffCode);
}

