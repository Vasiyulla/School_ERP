package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.PostDatedCheque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostDatedChequeRepository extends JpaRepository<PostDatedCheque, UUID> {
    List<PostDatedCheque> findAllByTenantIdAndMaturityDateBetween(String tenantId, LocalDate start, LocalDate end);
    List<PostDatedCheque> findAllByTenantIdAndStatus(String tenantId, String status);
    List<PostDatedCheque> findAllByTenantId(String tenantId);
    Optional<PostDatedCheque> findByIdAndTenantId(UUID id, String tenantId);
}
