package com.system.finance_dashboard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 20, message = "Username must be 4-20 characters")
        String username,

//        @NotBlank(message = "Password is required")
//        @Size(min = 6, max = 50, message = "Password must be at least 6 characters")
//        @Pattern(
//                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
//                message = "Password must contain uppercase, lowercase, number, and special character"
//        )
//        String password,

        @Email(message = "Invalid email format")
        @NotBlank(message = "email is required")
        String email,

        String role,

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only letters")
        String name
) {
}
