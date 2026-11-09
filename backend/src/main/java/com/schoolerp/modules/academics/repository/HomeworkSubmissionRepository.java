package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.HomeworkSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HomeworkSubmissionRepository extends JpaRepository<HomeworkSubmission, UUID> {
    List<HomeworkSubmission> findAllByTenantId(String tenantId);
    List<HomeworkSubmission> findAllByTenantIdAndAssignmentId(String tenantId, UUID assignmentId);
    List<HomeworkSubmission> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<HomeworkSubmission> findByTenantIdAndAssignmentIdAndStudentId(String tenantId, UUID assignmentId, UUID studentId);
}

