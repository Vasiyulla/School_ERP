package com.schoolerp.modules.staff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitTaxDeclarationRequest {

    @NotNull(message = "Employee ID is mandatory")
    private UUID employeeId;

    @NotBlank(message = "Tax regime is mandatory (NEW_REGIME_115BAC or OLD_REGIME)")
    @Builder.Default
    private String taxRegime = "NEW_REGIME_115BAC";

    @Builder.Default
    private String financialYear = "2026-2027";

    @Builder.Default
    private BigDecimal section80cAmount = BigDecimal.ZERO; // PPF, ELSS, Children Tuition Fee

    @Builder.Default
    private BigDecimal section80dMediclaim = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal hraRentPaidAnnual = BigDecimal.ZERO;
}

