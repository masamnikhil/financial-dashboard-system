package com.system.finance_dashboard.dto;

import com.system.finance_dashboard.entity.RecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinanceRecordDto {

    private Long id;
    private BigDecimal amount;
    private RecordType type;
    private String category;
    private String notes;
    private String createdBy;
    private String createdAt;

}
