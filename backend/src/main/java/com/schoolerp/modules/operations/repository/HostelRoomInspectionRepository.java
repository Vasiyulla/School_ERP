package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.HostelRoomInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HostelRoomInspectionRepository extends JpaRepository<HostelRoomInspection, UUID> {
    List<HostelRoomInspection> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
    List<HostelRoomInspection> findAllByTenantId(String tenantId);
}
