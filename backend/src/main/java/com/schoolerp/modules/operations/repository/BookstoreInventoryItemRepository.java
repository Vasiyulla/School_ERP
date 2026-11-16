package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.BookstoreInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookstoreInventoryItemRepository extends JpaRepository<BookstoreInventoryItem, UUID> {
    Optional<BookstoreInventoryItem> findBySkuCode(String skuCode);
    List<BookstoreInventoryItem> findByItemCategory(String itemCategory);
    List<BookstoreInventoryItem> findByTargetGrade(String targetGrade);
    List<BookstoreInventoryItem> findByItemCategoryAndTargetGrade(String itemCategory, String targetGrade);
}
