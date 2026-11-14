package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.MaterialAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialAccessLogRepository extends JpaRepository<MaterialAccessLog, UUID> {
    List<MaterialAccessLog> findAllByTenantIdAndMaterialId(String tenantId, UUID materialId);
    List<MaterialAccessLog> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
}

