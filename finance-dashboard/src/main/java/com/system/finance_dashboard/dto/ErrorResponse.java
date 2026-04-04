package com.system.finance_dashboard.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        String message,
        int status,
        LocalDateTime timestamp
) {
}
