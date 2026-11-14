package com.schoolerp.modules.attendance.repository;

import com.schoolerp.modules.attendance.entity.VirtualAttendanceTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VirtualAttendanceTelemetryRepository extends JpaRepository<VirtualAttendanceTelemetry, UUID> {
    List<VirtualAttendanceTelemetry> findBySessionIdOrderByJoinedAtAsc(UUID sessionId);
    Optional<VirtualAttendanceTelemetry> findBySessionIdAndStudentId(UUID sessionId, UUID studentId);
}

