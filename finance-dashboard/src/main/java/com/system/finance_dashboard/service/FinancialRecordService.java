package com.system.finance_dashboard.service;

import com.system.finance_dashboard.dto.FinanceRecordDto;
import com.system.finance_dashboard.dto.FinanceRecordRequest;
import com.system.finance_dashboard.dto.UpdateRecordRequest;
import com.system.finance_dashboard.entity.RecordType;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface FinancialRecordService {

    FinanceRecordDto createRecord(FinanceRecordRequest request);
    FinanceRecordDto updateRecord(Long id, UpdateRecordRequest request);
    void softDeleteRecord(Long id);
    Page<FinanceRecordDto> filterRecords(LocalDate date,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         String category,
                                         RecordType type,
                                         int page,
                                         int size);

    FinanceRecordDto getRecordById(Long id);

    Page<FinanceRecordDto> searchRecords(String keyword, int page, int size);

}
