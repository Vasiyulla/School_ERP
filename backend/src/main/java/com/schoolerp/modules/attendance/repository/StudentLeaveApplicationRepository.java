package com.schoolerp.modules.attendance.repository;

import com.schoolerp.modules.attendance.entity.StudentLeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentLeaveApplicationRepository extends JpaRepository<StudentLeaveApplication, UUID> {

    List<StudentLeaveApplication> findAllByTenantIdOrderByAppliedAtDesc(String tenantId);

    List<StudentLeaveApplication> findAllByTenantIdAndStudentIdOrderByAppliedAtDesc(String tenantId, UUID studentId);

    List<StudentLeaveApplication> findAllByTenantIdAndStatusOrderByAppliedAtDesc(String tenantId, String status);

    List<StudentLeaveApplication> findAllByTenantIdAndClassNameAndSectionNameOrderByAppliedAtDesc(String tenantId, String className, String sectionName);

    Optional<StudentLeaveApplication> findByIdAndTenantId(UUID id, String tenantId);
}

