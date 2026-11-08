package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.StaffLeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffLeaveRequestRepository extends JpaRepository<StaffLeaveRequest, UUID> {
    List<StaffLeaveRequest> findByTenantId(String tenantId);
    List<StaffLeaveRequest> findByTenantIdAndEmployeeId(String tenantId, UUID employeeId);
    List<StaffLeaveRequest> findByTenantIdAndStatus(String tenantId, String status);
    Optional<StaffLeaveRequest> findByIdAndTenantId(UUID id, String tenantId);
}

