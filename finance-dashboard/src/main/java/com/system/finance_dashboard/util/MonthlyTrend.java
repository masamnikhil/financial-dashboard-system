package com.system.finance_dashboard.util;

import com.system.finance_dashboard.entity.RecordType;

import java.math.BigDecimal;

public interface MonthlyTrend {
    Integer getMonth();

    BigDecimal getIncome();

    BigDecimal getExpenses();

    BigDecimal getNet();
}
