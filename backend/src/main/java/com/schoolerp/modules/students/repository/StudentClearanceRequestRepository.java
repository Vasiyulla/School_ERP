package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.StudentClearanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentClearanceRequestRepository extends JpaRepository<StudentClearanceRequest, UUID> {
    List<StudentClearanceRequest> findAllByTenantId(String tenantId);
    Optional<StudentClearanceRequest> findByTenantIdAndStudentId(String tenantId, UUID studentId);
}

