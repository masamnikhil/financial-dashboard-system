package com.system.finance_dashboard.dto;

import com.system.finance_dashboard.entity.RecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyTrendDto {
    private String month;
    private BigDecimal expenses;
    private BigDecimal income;
    private BigDecimal netAmount;
}
