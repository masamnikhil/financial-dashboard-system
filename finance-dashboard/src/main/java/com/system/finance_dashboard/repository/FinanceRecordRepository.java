package com.system.finance_dashboard.repository;

import com.system.finance_dashboard.entity.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRecordRepository extends JpaRepository<FinancialRecord, Long> {
}
