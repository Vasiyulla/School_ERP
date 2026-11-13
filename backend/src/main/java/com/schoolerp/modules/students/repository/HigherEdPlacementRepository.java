package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.HigherEdPlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HigherEdPlacementRepository extends JpaRepository<HigherEdPlacement, UUID> {

    List<HigherEdPlacement> findAllByTenantIdOrderByCreatedAtDesc(String tenantId);

    List<HigherEdPlacement> findAllByTenantIdAndUniversityNameContainingIgnoreCase(String tenantId, String universityName);

    List<HigherEdPlacement> findAllByTenantIdAndAlumniProfileId(String tenantId, UUID alumniProfileId);

    Optional<HigherEdPlacement> findByIdAndTenantId(UUID id, String tenantId);
}

