package com.system.finance_dashboard.controller;

import com.system.finance_dashboard.dto.CategorySum;
import com.system.finance_dashboard.dto.DashboardDto;
import com.system.finance_dashboard.dto.FinanceRecordDto;
import com.system.finance_dashboard.dto.MonthlyTrendDto;
import com.system.finance_dashboard.service.DashboardService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<DashboardDto> getSummary(){
         return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getSummary());
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<CategorySum>> getCategoryTotals(){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getCategoryTotals());
    }

    @GetMapping("/trends")
    public ResponseEntity<?> getMonthlyTrends(@RequestParam
                                                @NotNull(message = "provide year")
                                                @Positive(message = "provide valid year")
                                                int year){
        List<MonthlyTrendDto> monthlyTrendList = dashboardService.getMonthlyTrends(year);
        if(monthlyTrendList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No records found");
        else
            return ResponseEntity.status(HttpStatus.OK).body(monthlyTrendList);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<FinanceRecordDto>> getRecentActivity(){
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getRecentActivity());
    }
}
