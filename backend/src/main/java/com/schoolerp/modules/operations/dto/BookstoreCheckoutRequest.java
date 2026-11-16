package com.schoolerp.modules.operations.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class BookstoreCheckoutRequest {
    private UUID studentId;
    private String paymentMode; // CASH, CARD, UPI, STUDENT_LEDGER_DEBIT
    private List<CheckoutItem> items;

    public static class CheckoutItem {
        private UUID itemId;
        private Integer quantity;

        public CheckoutItem() {}

        public CheckoutItem(UUID itemId, Integer quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }

        public UUID getItemId() {
            return itemId;
        }

        public void setItemId(UUID itemId) {
            this.itemId = itemId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public BookstoreCheckoutRequest() {}

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public List<CheckoutItem> getItems() {
        return items;
    }

    public void setItems(List<CheckoutItem> items) {
        this.items = items;
    }
}
