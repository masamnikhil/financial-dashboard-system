package com.system.finance_dashboard.jpaspecification;

import com.system.finance_dashboard.entity.FinanceRecord;
import org.springframework.data.jpa.domain.Specification;

public class SearchSpecification {

    public static Specification<FinanceRecord> search(String search) {

        return (root, query, cb) -> {

            if (search == null || search.isBlank()) {
                return null;
            }

            String like = "%" + search.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("category")), like),
                    cb.like(cb.lower(root.get("notes")), like),
                    cb.like(cb.lower(root.get("createdBy")), like)
            );
        };
    }
}
