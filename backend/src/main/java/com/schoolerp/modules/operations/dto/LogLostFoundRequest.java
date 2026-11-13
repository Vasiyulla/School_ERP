package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogLostFoundRequest {

    @NotBlank(message = "Item category is mandatory (e.g. UNIFORM_CLOTHING, ELECTRONICS_CALCULATOR, SPECTACLES, BAGS_BOTTLES)")
    private String itemCategory;

    @NotBlank(message = "Item description is mandatory")
    private String itemDescription;

    @NotBlank(message = "Found location is mandatory")
    private String foundLocation;

    @NotNull(message = "Found date is mandatory")
    private LocalDate foundDate;

    private String photoUrl;

    @Builder.Default
    private String storageLockerRef = "Security Office Vault B-2";
}

