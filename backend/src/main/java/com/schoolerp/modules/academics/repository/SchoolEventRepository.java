package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.SchoolEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SchoolEventRepository extends JpaRepository<SchoolEvent, UUID> {

    List<SchoolEvent> findAllByTenantIdOrderByEventDateDesc(String tenantId);

    List<SchoolEvent> findAllByTenantIdAndStatusOrderByEventDateDesc(String tenantId, String status);

    List<SchoolEvent> findAllByTenantIdAndCategoryOrderByEventDateDesc(String tenantId, String category);

    List<SchoolEvent> findAllByTenantIdAndEventDateGreaterThanEqualOrderByEventDateAsc(String tenantId, LocalDate date);

    Optional<SchoolEvent> findByIdAndTenantId(UUID id, String tenantId);
}

