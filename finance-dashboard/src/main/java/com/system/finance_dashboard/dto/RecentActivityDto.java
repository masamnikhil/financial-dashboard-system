package com.system.finance_dashboard.dto;

import com.system.finance_dashboard.entity.RecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentActivityDto {

    private BigDecimal amount;
    private RecordType type;
    private String date;
    private String notes;
    private String category;

}
