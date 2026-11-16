package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.BookstoreSalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookstoreSalesOrderRepository extends JpaRepository<BookstoreSalesOrder, UUID> {
    Optional<BookstoreSalesOrder> findByOrderReceiptNumber(String orderReceiptNumber);
    List<BookstoreSalesOrder> findByStudentIdOrderByCreatedAtDesc(UUID studentId);
}
