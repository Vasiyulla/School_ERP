package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.HolisticProgressEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HolisticProgressEntryRepository extends JpaRepository<HolisticProgressEntry, UUID> {
    List<HolisticProgressEntry> findByTenantIdAndStudentIdOrderByRecordedAtDesc(String tenantId, UUID studentId);
}

