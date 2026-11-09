package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.MorningAssemblyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MorningAssemblyRosterRepository extends JpaRepository<MorningAssemblyRoster, UUID> {
    List<MorningAssemblyRoster> findByTenantIdOrderByWeekStartDateDesc(String tenantId);
    Optional<MorningAssemblyRoster> findByTenantIdAndWeekStartDate(String tenantId, LocalDate weekStartDate);
}

