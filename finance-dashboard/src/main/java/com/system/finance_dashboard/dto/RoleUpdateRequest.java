package com.system.finance_dashboard.dto;

import com.system.finance_dashboard.entity.Role;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateRequest(

        @NotNull
        Role role
) {
}
