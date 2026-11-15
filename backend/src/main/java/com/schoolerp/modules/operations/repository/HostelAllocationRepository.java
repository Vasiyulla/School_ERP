package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.HostelAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HostelAllocationRepository extends JpaRepository<HostelAllocation, UUID> {
    List<HostelAllocation> findByTenantIdAndRoomIdAndStatus(String tenantId, UUID roomId, String status);
    Optional<HostelAllocation> findByTenantIdAndStudentIdAndStatus(String tenantId, UUID studentId, String status);
    List<HostelAllocation> findByTenantIdAndStatus(String tenantId, String status);
}

