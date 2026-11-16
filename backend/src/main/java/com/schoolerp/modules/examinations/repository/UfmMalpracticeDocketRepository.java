package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.UfmMalpracticeDocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UfmMalpracticeDocketRepository extends JpaRepository<UfmMalpracticeDocket, UUID> {
    Optional<UfmMalpracticeDocket> findByDocketNumber(String docketNumber);
    List<UfmMalpracticeDocket> findByVerdictStatus(String verdictStatus);
    List<UfmMalpracticeDocket> findByStudentId(UUID studentId);
}
