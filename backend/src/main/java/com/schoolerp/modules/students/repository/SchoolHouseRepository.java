package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.SchoolHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SchoolHouseRepository extends JpaRepository<SchoolHouse, UUID> {
    List<SchoolHouse> findAllByTenantIdOrderByTotalPointsDesc(String tenantId);
    Optional<SchoolHouse> findByTenantIdAndHouseName(String tenantId, String houseName);
    long countByTenantId(String tenantId);
}

