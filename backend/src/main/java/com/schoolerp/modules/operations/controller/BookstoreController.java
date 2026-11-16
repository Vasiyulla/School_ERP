package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.dto.*;
import com.schoolerp.modules.operations.service.BookstoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operations/bookstore")
@RequiredArgsConstructor
public class BookstoreController {

    private final BookstoreService bookstoreService;

    @PostMapping("/items")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<BookstoreInventoryItemDTO>> registerItem(
            @RequestBody RegisterBookstoreItemRequest request) {
        BookstoreInventoryItemDTO item = bookstoreService.registerItem(request);
        return ResponseEntity.ok(ApiResponse.ok("Bookstore/Uniform inventory item saved", item));
    }

    @GetMapping("/items")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('TEACHER') or hasRole('PARENT') or hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<BookstoreInventoryItemDTO>>> getItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String applicableGrade) {
        List<BookstoreInventoryItemDTO> items = bookstoreService.getItems(category, applicableGrade);
        return ResponseEntity.ok(ApiResponse.ok("Bookstore items retrieved", items));
    }

    @PostMapping("/sales/checkout")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<BookstoreSalesOrderDTO>> checkout(
            @RequestBody BookstoreCheckoutRequest request) {
        BookstoreSalesOrderDTO order = bookstoreService.checkout(request);
        return ResponseEntity.ok(ApiResponse.ok("POS Sales checkout completed and inventory updated", order));
    }

    @GetMapping("/sales/orders")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('PARENT')")
    public ResponseEntity<ApiResponse<List<BookstoreSalesOrderDTO>>> getSalesOrders(
            @RequestParam(required = false) UUID studentId) {
        List<BookstoreSalesOrderDTO> orders = bookstoreService.getSalesOrders(studentId);
        return ResponseEntity.ok(ApiResponse.ok("Sales orders retrieved", orders));
    }
}
