package com.system.finance_dashboard.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateRecordRequest(

        @NotNull(message = "provide value for amount")
        @Positive(message = "amount value should be greater than 0")
        BigDecimal amount,
        String type,
        String notes,
        String category
) {
}
