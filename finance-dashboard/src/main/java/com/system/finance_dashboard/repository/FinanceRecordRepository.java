package com.system.finance_dashboard.repository;

import com.system.finance_dashboard.entity.FinanceRecord;
import com.system.finance_dashboard.entity.RecordType;
import com.system.finance_dashboard.util.CategoryTotal;
import com.system.finance_dashboard.util.MonthlyTrend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long>,
                                                 JpaSpecificationExecutor<FinanceRecord> {

    Optional<FinanceRecord> findByIdAndDeletedAtIsNull(Long id);


    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinanceRecord r " +
            "WHERE r.type = :type AND r.deletedAt IS NULL")
    BigDecimal sumByType(@Param("type") RecordType type);

    @Query("SELECT r.category as category, COALESCE(SUM(r.amount),0) as total " +
            "FROM FinanceRecord r " +
            "WHERE r.deletedAt IS NULL " +
            "GROUP BY r.category")
    List<CategoryTotal> getCategoryTotals();

    @Query(""" 
        SELECT 
        MONTH(r.createdAt) as month,
        SUM(CASE WHEN r.type = 'INCOME' THEN r.amount ELSE 0 END) as income,
        SUM(CASE WHEN r.type = 'EXPENSE' THEN r.amount ELSE 0 END) as expenses,
        SUM(CASE WHEN r.type = 'INCOME' THEN r.amount ELSE -r.amount END) as net
        FROM FinanceRecord r
        WHERE r.deletedAt IS NULL
        AND YEAR(r.createdAt) = :year
        GROUP BY MONTH(r.createdAt)
        ORDER BY MONTH(r.createdAt)
    """)
    List<MonthlyTrend> getMonthlyTrends(int year);

    List<FinanceRecord> findTop5ByDeletedAtIsNullOrderByCreatedAtDesc();
}
