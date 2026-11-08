package com.schoolerp.modules.attendance.repository;

import com.schoolerp.modules.attendance.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, UUID> {
    List<AttendanceRecord> findAllByTenantIdAndSessionId(String tenantId, UUID sessionId);
    Optional<AttendanceRecord> findByIdAndTenantId(UUID id, String tenantId);
}

