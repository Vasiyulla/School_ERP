package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.InfirmaryVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InfirmaryVisitRepository extends JpaRepository<InfirmaryVisit, UUID> {
    List<InfirmaryVisit> findAllByTenantId(String tenantId);
    List<InfirmaryVisit> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
}

