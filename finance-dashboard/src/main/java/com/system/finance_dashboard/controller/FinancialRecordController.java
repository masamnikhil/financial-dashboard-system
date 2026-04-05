package com.system.finance_dashboard.controller;

import com.system.finance_dashboard.dto.FinanceRecordDto;
import com.system.finance_dashboard.dto.FinanceRecordRequest;
import com.system.finance_dashboard.dto.UpdateRecordRequest;
import com.system.finance_dashboard.entity.RecordType;
import com.system.finance_dashboard.service.FinancialRecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/records")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Validated
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

    @PostMapping
    public ResponseEntity<FinanceRecordDto> createRecord(@Valid @RequestBody FinanceRecordRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(financialRecordService.createRecord(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<?> filterRecords(
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FinanceRecordDto> filteredRecords = financialRecordService.filterRecords(date, from, to, category, type, page, size);
        if(filteredRecords.isEmpty())
            return new ResponseEntity<>("Records not found with applied filters", HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(filteredRecords);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<FinanceRecordDto> getRecordById(@PathVariable
                                                              @NotNull(message = "id is required")
                                                              @Positive(message = "id should be positive")
                                                              Long id) {
        return ResponseEntity.ok(financialRecordService.getRecordById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<?> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FinanceRecordDto> searchedRecords = financialRecordService.searchRecords(keyword, page, size);
        if(searchedRecords.isEmpty())
            return new ResponseEntity<>("Records not found with: " + keyword, HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(searchedRecords);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FinanceRecordDto> updateRecord(
            @PathVariable
            @NotNull(message = "id is required")
            @Positive(message = "id should be positive")
            Long id,
            @Valid @RequestBody UpdateRecordRequest request) {
        return ResponseEntity.ok(financialRecordService.updateRecord(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable
                                                        @NotNull(message = "id is required")
                                                        @Positive(message = "id should be positive")
                                                        Long id) {
        financialRecordService.softDeleteRecord(id);
        return ResponseEntity.noContent().build();
    }



}
