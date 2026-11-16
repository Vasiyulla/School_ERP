package com.schoolerp.modules.operations.dto;

import java.util.List;
import java.util.UUID;

public class AllergenCheckRequest {
    private UUID studentId;
    private String itemName;
    private List<String> itemIngredients;

    public AllergenCheckRequest() {}

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<String> getItemIngredients() {
        return itemIngredients;
    }

    public void setItemIngredients(List<String> itemIngredients) {
        this.itemIngredients = itemIngredients;
    }
}
