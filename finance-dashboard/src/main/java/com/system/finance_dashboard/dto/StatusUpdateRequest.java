package com.system.finance_dashboard.dto;

import com.system.finance_dashboard.entity.Status;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(

        @NotNull
        Status status
) {
}
