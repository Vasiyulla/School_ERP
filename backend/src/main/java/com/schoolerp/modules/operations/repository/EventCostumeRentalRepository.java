package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.EventCostumeRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventCostumeRentalRepository extends JpaRepository<EventCostumeRental, UUID> {
    List<EventCostumeRental> findByEventNameOrderByCreatedAtDesc(String eventName);
    List<EventCostumeRental> findByAssignedStudentId(UUID assignedStudentId);
    List<EventCostumeRental> findByReturnCondition(String returnCondition);
}
