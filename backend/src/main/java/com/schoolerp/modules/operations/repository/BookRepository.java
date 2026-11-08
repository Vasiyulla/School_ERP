package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByTenantId(String tenantId);
    Optional<Book> findByTenantIdAndIsbn(String tenantId, String isbn);
    Optional<Book> findByTenantIdAndBarcodeTag(String tenantId, String barcodeTag);
}

