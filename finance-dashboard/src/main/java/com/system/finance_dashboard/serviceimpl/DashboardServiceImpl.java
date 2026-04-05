package com.system.finance_dashboard.serviceimpl;

import com.system.finance_dashboard.dto.*;
import com.system.finance_dashboard.entity.FinanceRecord;
import com.system.finance_dashboard.entity.RecordType;
import com.system.finance_dashboard.repository.FinanceRecordRepository;
import com.system.finance_dashboard.repository.UserRepository;
import com.system.finance_dashboard.service.DashboardService;
import com.system.finance_dashboard.util.CategoryTotal;
import com.system.finance_dashboard.util.MonthlyTrend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final FinanceRecordRepository financeRecordRepository;

    @Override
    public DashboardDto getSummary() {

        BigDecimal totalExpense = financeRecordRepository.sumByType(RecordType.EXPENSE);
        BigDecimal totalIncome = financeRecordRepository.sumByType(RecordType.INCOME);
        BigDecimal netAmount = totalIncome.subtract(totalExpense);

        DashboardDto dashboardDto = DashboardDto.builder().totalExpense(totalExpense).totalIncome(totalIncome)
                .netBalance(netAmount).build();
        return dashboardDto;
    }

    @Override
    public List<CategorySum> getCategoryTotals() {
          List<CategoryTotal> categoryTotalList = financeRecordRepository.getCategoryTotals();
          return categoryTotalList.stream()
                  .map(categoryTotal -> CategorySum.builder().category(categoryTotal.getCategory())
                          .total(categoryTotal.getTotal()).build())
                  .toList();
    }

    @Override
    public List<MonthlyTrendDto> getMonthlyTrends(int year) {
        List<MonthlyTrend> monthlyTrendList = financeRecordRepository.getMonthlyTrends(year);
        return monthlyTrendList.stream()
                .map(monthlyTrend -> MonthlyTrendDto.builder().month(String.valueOf(Month.of(monthlyTrend.getMonth()).name()) + " " + year)
                        .expenses(monthlyTrend.getExpenses()).income(monthlyTrend.getIncome()).netAmount(monthlyTrend.getNet()).build())
                .toList();
    }

    @Override
    public List<RecentActivityDto> getRecentActivity() {
        List<FinanceRecord> records = financeRecordRepository.findTop5ByDeletedAtIsNullOrderByCreatedAtDesc();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");

        return records.stream()
                .map(record -> RecentActivityDto.builder()
                .amount(record.getAmount()).type(record.getType()).date(record.getCreatedAt().format(formatter))
                .notes(record.getNotes())
                .category(record.getCategory()).build())
                .toList();
    }
}
