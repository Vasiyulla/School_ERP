package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionMeritOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionMeritOfferRepository extends JpaRepository<AdmissionMeritOffer, UUID> {
    List<AdmissionMeritOffer> findByTenantIdOrderByIssuedAtDesc(String tenantId);
    Optional<AdmissionMeritOffer> findByTenantIdAndInquiryId(String tenantId, UUID inquiryId);
    Optional<AdmissionMeritOffer> findByIdAndTenantId(UUID id, String tenantId);
    long countByTenantIdAndOfferStatus(String tenantId, String offerStatus);
}

