package com.system.finance_dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePassRequest(

        @NotBlank(message = "Password is required")
        @JsonProperty("old_password")
        String oldPassword,
        @NotBlank(message = "Password is required")
        @JsonProperty("new_password")
        @Size(min = 6, max = 50, message = "Password must be at least 6 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must contain uppercase, lowercase, number, and special character"
        )
        String newPassword
) {
}
