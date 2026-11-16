package com.schoolerp.modules.operations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolerp.modules.operations.dto.*;
import com.schoolerp.modules.operations.entity.BookstoreInventoryItem;
import com.schoolerp.modules.operations.entity.BookstoreSalesOrder;
import com.schoolerp.modules.operations.repository.BookstoreInventoryItemRepository;
import com.schoolerp.modules.operations.repository.BookstoreSalesOrderRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookstoreService {

    private final BookstoreInventoryItemRepository inventoryRepository;
    private final BookstoreSalesOrderRepository salesOrderRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public BookstoreInventoryItemDTO registerItem(RegisterBookstoreItemRequest req) {
        Optional<BookstoreInventoryItem> existing = inventoryRepository.findBySkuCode(req.getSkuCode());
        BookstoreInventoryItem item;
        if (existing.isPresent()) {
            item = existing.get();
            item.setItemName(req.getItemName());
            item.setItemCategory(req.getItemCategory() != null ? req.getItemCategory() : item.getItemCategory());
            item.setTargetGrade(req.getTargetGrade());
            item.setSizeSpecification(req.getSizeSpecification());
            item.setUnitSellingPrice(req.getUnitSellingPrice() != null ? req.getUnitSellingPrice() : item.getUnitSellingPrice());
            item.setCostPrice(req.getCostPrice() != null ? req.getCostPrice() : item.getCostPrice());
            item.setStockQuantity(item.getStockQuantity() + (req.getStockQuantity() != null ? req.getStockQuantity() : 0));
            item.setReorderThreshold(req.getReorderThreshold() != null ? req.getReorderThreshold() : item.getReorderThreshold());
            item.setSupplierVendorName(req.getSupplierVendorName());
        } else {
            item = BookstoreInventoryItem.builder()
                    .tenantId("tenant_apex_mumbai_01")
                    .skuCode(req.getSkuCode())
                    .itemName(req.getItemName())
                    .itemCategory(req.getItemCategory() != null ? req.getItemCategory() : "TEXTBOOK_BUNDLE")
                    .targetGrade(req.getTargetGrade())
                    .sizeSpecification(req.getSizeSpecification())
                    .unitSellingPrice(req.getUnitSellingPrice() != null ? req.getUnitSellingPrice() : BigDecimal.ZERO)
                    .costPrice(req.getCostPrice() != null ? req.getCostPrice() : BigDecimal.ZERO)
                    .stockQuantity(req.getStockQuantity() != null ? req.getStockQuantity() : 0)
                    .reorderThreshold(req.getReorderThreshold() != null ? req.getReorderThreshold() : 10)
                    .supplierVendorName(req.getSupplierVendorName() != null ? req.getSupplierVendorName() : "Apex Academic Publishing")
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        BookstoreInventoryItem saved = inventoryRepository.save(item);
        log.info("Bookstore inventory item registered/updated: {} (Stock: {})", saved.getItemName(), saved.getStockQuantity());
        return mapToItemDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<BookstoreInventoryItemDTO> getItems(String category, String applicableGrade) {
        List<BookstoreInventoryItem> list;
        if (category != null && applicableGrade != null) {
            list = inventoryRepository.findByItemCategoryAndTargetGrade(category, applicableGrade);
        } else if (category != null) {
            list = inventoryRepository.findByItemCategory(category);
        } else if (applicableGrade != null) {
            list = inventoryRepository.findByTargetGrade(applicableGrade);
        } else {
            list = inventoryRepository.findAll();
        }
        return list.stream().map(this::mapToItemDTO).collect(Collectors.toList());
    }

    @Transactional
    public BookstoreSalesOrderDTO checkout(BookstoreCheckoutRequest req) {
        Student student = null;
        if (req.getStudentId() != null) {
            student = studentRepository.findById(req.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found: " + req.getStudentId()));
        }

        BigDecimal total = BigDecimal.ZERO;
        List<Map<String, Object>> purchasedSummary = new ArrayList<>();

        if (req.getItems() != null) {
            for (BookstoreCheckoutRequest.CheckoutItem line : req.getItems()) {
                BookstoreInventoryItem item = inventoryRepository.findById(line.getItemId())
                        .orElseThrow(() -> new IllegalArgumentException("Inventory item not found: " + line.getItemId()));

                int qty = line.getQuantity() != null && line.getQuantity() > 0 ? line.getQuantity() : 1;
                if (item.getStockQuantity() < qty) {
                    throw new IllegalStateException("Insufficient stock for item: " + item.getItemName() +
                            " (Requested: " + qty + ", Available: " + item.getStockQuantity() + ")");
                }

                // Deduct stock
                item.setStockQuantity(item.getStockQuantity() - qty);
                inventoryRepository.save(item);

                BigDecimal lineTotal = item.getUnitSellingPrice().multiply(BigDecimal.valueOf(qty));
                total = total.add(lineTotal);

                Map<String, Object> summary = new HashMap<>();
                summary.put("itemId", item.getId());
                summary.put("skuCode", item.getSkuCode());
                summary.put("itemName", item.getItemName());
                summary.put("quantity", qty);
                summary.put("unitPrice", item.getUnitSellingPrice());
                summary.put("lineTotal", lineTotal);
                purchasedSummary.add(summary);
            }
        }

        String jsonItems = "[]";
        try {
            jsonItems = objectMapper.writeValueAsString(purchasedSummary);
        } catch (Exception e) {
            log.error("Failed to serialize purchased items", e);
        }

        String receiptNumber = "BK-REC-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String paymentMode = req.getPaymentMode() != null ? req.getPaymentMode() : "CASH";

        BookstoreSalesOrder order = BookstoreSalesOrder.builder()
                .tenantId(student != null && student.getTenantId() != null ? student.getTenantId() : "tenant_apex_mumbai_01")
                .orderReceiptNumber(receiptNumber)
                .studentId(student != null ? student.getId() : null)
                .studentName(student != null ? student.getFullName() : "Walk-in Student")
                .admissionNumber(student != null ? student.getAdmissionNumber() : "WALKIN")
                .paymentMode(paymentMode)
                .totalAmount(total)
                .tenderedAmount(total)
                .itemsJson(jsonItems)
                .cashierStaffName("Campus Bookstore Cashier")
                .createdAt(LocalDateTime.now())
                .build();

        BookstoreSalesOrder saved = salesOrderRepository.save(order);
        log.info("Bookstore checkout completed: Receipt {} for Total {} (Payment: {})",
                saved.getOrderReceiptNumber(), saved.getTotalAmount(), saved.getPaymentMode());

        return mapToOrderDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<BookstoreSalesOrderDTO> getSalesOrders(UUID studentId) {
        List<BookstoreSalesOrder> list;
        if (studentId != null) {
            list = salesOrderRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
        } else {
            list = salesOrderRepository.findAll();
        }
        return list.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
    }

    private BookstoreInventoryItemDTO mapToItemDTO(BookstoreInventoryItem item) {
        BookstoreInventoryItemDTO dto = new BookstoreInventoryItemDTO();
        dto.setId(item.getId());
        dto.setSkuCode(item.getSkuCode());
        dto.setItemCategory(item.getItemCategory());
        dto.setItemName(item.getItemName());
        dto.setTargetGrade(item.getTargetGrade());
        dto.setSizeSpecification(item.getSizeSpecification());
        dto.setUnitSellingPrice(item.getUnitSellingPrice());
        dto.setCostPrice(item.getCostPrice());
        dto.setStockQuantity(item.getStockQuantity());
        dto.setReorderThreshold(item.getReorderThreshold());
        dto.setSupplierVendorName(item.getSupplierVendorName());
        dto.setCreatedAt(item.getCreatedAt());
        return dto;
    }

    private BookstoreSalesOrderDTO mapToOrderDTO(BookstoreSalesOrder o) {
        BookstoreSalesOrderDTO dto = new BookstoreSalesOrderDTO();
        dto.setId(o.getId());
        dto.setOrderReceiptNumber(o.getOrderReceiptNumber());
        dto.setStudentId(o.getStudentId());
        dto.setStudentName(o.getStudentName());
        dto.setAdmissionNumber(o.getAdmissionNumber());
        dto.setPaymentMode(o.getPaymentMode());
        dto.setTotalAmount(o.getTotalAmount());
        dto.setTenderedAmount(o.getTenderedAmount());
        dto.setItemsJson(o.getItemsJson());
        dto.setCashierStaffName(o.getCashierStaffName());
        dto.setCreatedAt(o.getCreatedAt());
        return dto;
    }
}
