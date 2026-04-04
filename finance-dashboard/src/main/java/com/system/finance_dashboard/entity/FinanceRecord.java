package com.system.finance_dashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "financial_records")
public class FinanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecordType type;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private String notes;
    private LocalDateTime deletedAt;
    @Column(nullable = false)
    private String createdBy;

}
