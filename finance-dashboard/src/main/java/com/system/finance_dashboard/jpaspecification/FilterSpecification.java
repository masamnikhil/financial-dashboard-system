package com.system.finance_dashboard.jpaspecification;

import com.system.finance_dashboard.entity.FinanceRecord;
import com.system.finance_dashboard.entity.RecordType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class FilterSpecification {

    public static Specification<FinanceRecord> filter(
            LocalDateTime start,
            LocalDateTime end,
            String category,
            RecordType type
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (start != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("createdAt"), start));
            }

            if (end != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("createdAt"), end));
            }

            if (category != null) {
                predicates.add(cb.equal(
                        cb.lower(root.get("category")),
                        category.toLowerCase()));
            }

            if (type != null) {
                predicates.add(cb.equal(
                        root.get("type"), type));
            }

            // deletedAt IS NULL
            predicates.add(cb.isNull(root.get("deletedAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
