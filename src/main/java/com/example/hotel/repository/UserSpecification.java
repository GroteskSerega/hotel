package com.example.hotel.repository;

import com.example.hotel.entity.User;
import com.example.hotel.entity.User_;
import com.example.hotel.web.dto.v1.UserFilter;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;
import java.time.Instant;

import static com.example.hotel.repository.SpecificationRegex.TEMPLATE_LIKE;

public interface UserSpecification {

    static Specification<User> withFilter(UserFilter userFilter) {
        return Specification.allOf(
                byUsername(userFilter.username()),
                byEmail(userFilter.email()),
                byCreateAtBefore(userFilter.createBefore()),
                byUpdateAtBefore(userFilter.updateBefore()),
                byCreateAtAfter(userFilter.createAfter()),
                byUpdateAtAfter(userFilter.updateAfter())
        );
    }

    static Specification<User> byUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null) {
                return null;
            }

            String pattern = MessageFormat.format(TEMPLATE_LIKE, username.toLowerCase());

            Expression<String> lowerCaseField = criteriaBuilder.lower(root.get(User_.USERNAME));

            return criteriaBuilder.like(lowerCaseField, pattern);
        };
    }

    static Specification<User> byEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null) {
                return null;
            }

            String pattern = MessageFormat.format(TEMPLATE_LIKE, email.toLowerCase());

            Expression<String> lowerCaseField = criteriaBuilder.lower(root.get(User_.EMAIL));

            return criteriaBuilder.like(lowerCaseField, pattern);
        };
    }

    static Specification<User> byCreateAtBefore(Instant createBefore) {
        return (root, query, criteriaBuilder) -> {
            if (createBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(User_.CREATE_AT), createBefore);
        };
    }

    static Specification<User> byUpdateAtBefore(Instant updateBefore) {
        return (root, query, criteriaBuilder) -> {
            if (updateBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(User_.UPDATE_AT), updateBefore);
        };
    }

    static Specification<User> byCreateAtAfter(Instant createAfter) {
        return (root, query, criteriaBuilder) -> {
            if (createAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(User_.CREATE_AT), createAfter);
        };
    }

    static Specification<User> byUpdateAtAfter(Instant updateAfter) {
        return (root, query, criteriaBuilder) -> {
            if (updateAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(User_.UPDATE_AT), updateAfter);
        };
    }
}
