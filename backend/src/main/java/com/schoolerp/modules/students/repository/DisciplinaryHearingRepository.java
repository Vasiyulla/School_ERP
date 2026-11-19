package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.DisciplinaryHearing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DisciplinaryHearingRepository extends JpaRepository<DisciplinaryHearing, UUID> {
    List<DisciplinaryHearing> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
    List<DisciplinaryHearing> findAllByTenantId(String tenantId);
    Optional<DisciplinaryHearing> findByIdAndTenantId(UUID id, String tenantId);
    long countByTenantId(String tenantId);
}
