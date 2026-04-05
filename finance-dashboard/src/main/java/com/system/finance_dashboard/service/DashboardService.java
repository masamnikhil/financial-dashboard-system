package com.system.finance_dashboard.service;

import com.system.finance_dashboard.dto.*;

import java.util.List;

public interface DashboardService {

    DashboardDto getSummary();
    List<CategorySum> getCategoryTotals();
    List<MonthlyTrendDto> getMonthlyTrends(int year);
    List<RecentActivityDto> getRecentActivity();
}
