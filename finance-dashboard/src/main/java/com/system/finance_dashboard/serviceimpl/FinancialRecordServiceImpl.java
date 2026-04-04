package com.system.finance_dashboard.serviceimpl;

import com.system.finance_dashboard.dto.FinanceRecordDto;
import com.system.finance_dashboard.dto.FinanceRecordRequest;
import com.system.finance_dashboard.dto.UpdateRecordRequest;
import com.system.finance_dashboard.entity.FinanceRecord;
import com.system.finance_dashboard.entity.RecordType;
import com.system.finance_dashboard.jpaspecification.FilterSpecification;
import com.system.finance_dashboard.jpaspecification.SearchSpecification;
import com.system.finance_dashboard.repository.FinanceRecordRepository;
import com.system.finance_dashboard.repository.UserRepository;
import com.system.finance_dashboard.service.FinancialRecordService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final UserRepository userRepository;

    private final FinanceRecordRepository financeRecordRepository;

    @Override
    @Transactional
    public FinanceRecordDto createRecord(FinanceRecordRequest request) {

        String currentUser = getCurrentUser();

        FinanceRecord record = FinanceRecord.builder()
                .createdBy(currentUser)
                .amount(request.amount())
                .type(RecordType.valueOf(request.type().toUpperCase()))
                .category(request.category())
                .createdAt(LocalDateTime.now())
                .notes(request.notes())
                .build();
       try {
           FinanceRecord savedRecord = financeRecordRepository.saveAndFlush(record);
           return mapToResponse(savedRecord);
       }
       catch (RuntimeException ex){
           throw new RuntimeException(ex);
       }

    }

    @Override
    public FinanceRecordDto updateRecord(Long id, UpdateRecordRequest request) {
        FinanceRecord record = financeRecordRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("record not found with id: " + id));

        if (!(request.amount() == null)) {
            record.setAmount(request.amount());
        }
        if (request.type() != null && !request.type().trim().isEmpty()) {
            record.setType(RecordType.valueOf(request.type().toUpperCase()));
        }
        if (request.category() != null && !request.category().trim().isEmpty()) {
            record.setCategory(request.category().trim());
        }
        if (request.notes() != null && !request.notes().trim().isEmpty()) {
            record.setNotes(request.notes().trim());
        }

        FinanceRecord updatedRecord = financeRecordRepository.save(record);
        return mapToResponse(updatedRecord);

    }

    @Override
    public void softDeleteRecord(Long id) {
        FinanceRecord record = financeRecordRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("record not found with id: " + id));

        record.setDeletedAt(LocalDateTime.now());
        financeRecordRepository.save(record);
    }

    // if you don't provide any parameters it returns all records which are not soft deleted
    @Override
    public Page<FinanceRecordDto> filterRecords(LocalDate date,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                String category,
                                                RecordType type,
                                                int page,
                                                int size) {

        LocalDateTime start = null;
        LocalDateTime end = null;

        if (date != null) {
            start = date.atStartOfDay();
            end = date.atTime(23, 59, 59);
        } else if (startDate != null && endDate != null) {
            start = startDate.atStartOfDay();
            end = endDate.atTime(23, 59, 59);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Specification<FinanceRecord> spec =
                FilterSpecification.filter(
                        start,
                        end,
                        category,
                        type
                );

        Page<FinanceRecord> result =
                financeRecordRepository.findAll(spec, pageable);
        return result.map(this::mapToResponse);
    }


    @Override
    public FinanceRecordDto getRecordById(Long id) {
        FinanceRecord record = financeRecordRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("record not found with id: " + id));
        return mapToResponse(record);
    }

    @Override
    public Page<FinanceRecordDto> searchRecords(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<FinanceRecord> spec = SearchSpecification.search(keyword);

        Page<FinanceRecord> pageResult =
                financeRecordRepository.findAll(spec, pageable);
        return pageResult.map(this::mapToResponse);
    }

    private String getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
    }

    private FinanceRecordDto mapToResponse(FinanceRecord record) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");

        FinanceRecordDto response = FinanceRecordDto.builder().id(record.getId())
                .amount(record.getAmount()).type(record.getType()).createdAt(record.getCreatedAt().format(formatter))
                .createdBy(record.getCreatedBy()).notes(record.getNotes())
                .category(record.getCategory()).build();
        return response;
    }
}
