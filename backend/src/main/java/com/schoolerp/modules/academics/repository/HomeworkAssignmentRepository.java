package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.HomeworkAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HomeworkAssignmentRepository extends JpaRepository<HomeworkAssignment, UUID> {
    List<HomeworkAssignment> findAllByTenantId(String tenantId);
    List<HomeworkAssignment> findAllByTenantIdAndClassName(String tenantId, String className);
    List<HomeworkAssignment> findAllByTenantIdAndClassNameAndSectionName(String tenantId, String className, String sectionName);
}

