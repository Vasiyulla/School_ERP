package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.DisciplinarySanction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DisciplinarySanctionRepository extends JpaRepository<DisciplinarySanction, UUID> {
    List<DisciplinarySanction> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
    List<DisciplinarySanction> findAllByTenantIdAndHearingDocketId(String tenantId, UUID hearingDocketId);
}
