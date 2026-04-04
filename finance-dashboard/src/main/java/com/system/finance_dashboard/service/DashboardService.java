package com.system.finance_dashboard.service;

import com.system.finance_dashboard.dto.CategorySum;
import com.system.finance_dashboard.dto.DashboardDto;
import com.system.finance_dashboard.dto.FinanceRecordDto;
import com.system.finance_dashboard.dto.MonthlyTrendDto;

import java.util.List;

public interface DashboardService {

    DashboardDto getSummary();
    List<CategorySum> getCategoryTotals();
    List<MonthlyTrendDto> getMonthlyTrends(int year);
    List<FinanceRecordDto> getRecentActivity();
}
