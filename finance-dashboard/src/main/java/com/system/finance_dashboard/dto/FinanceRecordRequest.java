package com.system.finance_dashboard.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record FinanceRecordRequest(

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,
        @NotBlank(message = "Type is required (INCOME or EXPENSE)")
        String type,
        @NotBlank(message = "Category is required")
        @Pattern(regexp = "^[A-Za-z]+$", message = "Only letters allowed, no spaces")
        String category,
        String notes
        ) {
}
