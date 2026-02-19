package com.example.hotel.repository;

import com.example.hotel.entity.Booking;
import com.example.hotel.entity.Booking_;
import com.example.hotel.entity.Room_;
import com.example.hotel.web.dto.v1.BookingFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.awt.print.Book;
import java.time.Instant;
import java.util.UUID;

public interface BookingSpecification {

    static Specification<Booking> withFilter(BookingFilter bookingFilter) {
        return Specification.allOf(
                byRoomId(bookingFilter.roomId()),
//                byCheckInDateBefore(bookingFilter.checkInDateBefore()),
//                byCheckOutDateBefore(bookingFilter.checkOutDateBefore()),
//                byCheckInDateAfter(bookingFilter.checkInDateAfter()),
                byCheckInDateAfterAndCheckOutDateBefore(bookingFilter.checkInDateAfter(),
                        bookingFilter.checkOutDateBefore()),
//                byCheckOutDateAfter(bookingFilter.checkOutDateAfter()),
                byCreateAtBefore(bookingFilter.createBefore()),
                byUpdateAtBefore(bookingFilter.updateBefore()),
                byCreateAtAfter(bookingFilter.createAfter()),
                byUpdateAtAfter(bookingFilter.updateAfter())
        );
    }

    static Specification<Booking> byRoomId(UUID roomId) {
        return (root, query, criteriaBuilder) -> {
            if (roomId == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get(Booking_.ROOM).get(Room_.ID), roomId);
        };
    }

    static Specification<Booking> byCheckInDateBefore(Instant checkInDateBefore) {
        return (root, query, criteriaBuilder) -> {
            if (checkInDateBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Booking_.CHECK_IN_DATE), checkInDateBefore);
        };
    }

    static Specification<Booking> byCheckOutDateBefore(Instant checkOutDateBefore) {
        return (root, query, criteriaBuilder) -> {
            if (checkOutDateBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Booking_.CHECK_OUT_DATE), checkOutDateBefore);
        };
    }

    static Specification<Booking> byCheckInDateAfter(Instant checkInDateAfter) {
        return (root, query, criteriaBuilder) -> {
            if (checkInDateAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Booking_.CHECK_IN_DATE), checkInDateAfter);
        };
    }

    static Specification<Booking> byCheckInDateAfterAndCheckOutDateBefore(Instant checkInDateAfter,
                                                                          Instant checkOutDateBefore) {
        return (root, query, criteriaBuilder) -> {
            if (checkInDateAfter == null || checkOutDateBefore == null) {
                return null;
            }

            return criteriaBuilder.and(
                    criteriaBuilder.lessThan(root.get(Booking_.CHECK_IN_DATE), checkOutDateBefore),
                    criteriaBuilder.greaterThan(root.get(Booking_.CHECK_OUT_DATE), checkInDateAfter)
            );
        };
    }

    static Specification<Booking> byCheckOutDateAfter(Instant checkOutDateAfter) {
        return (root, query, criteriaBuilder) -> {
            if (checkOutDateAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Booking_.CHECK_OUT_DATE), checkOutDateAfter);
        };
    }

    static Specification<Booking> byCreateAtBefore(Instant createBefore) {
        return (root, query, criteriaBuilder) -> {
            if (createBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Booking_.CREATE_AT), createBefore);
        };
    }

    static Specification<Booking> byUpdateAtBefore(Instant updateBefore) {
        return (root, query, criteriaBuilder) -> {
            if (updateBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Booking_.UPDATE_AT), updateBefore);
        };
    }

    static Specification<Booking> byCreateAtAfter(Instant createAfter) {
        return (root, query, criteriaBuilder) -> {
            if (createAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Booking_.CREATE_AT), createAfter);
        };
    }

    static Specification<Booking> byUpdateAtAfter(Instant updateAfter) {
        return (root, query, criteriaBuilder) -> {
            if (updateAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Booking_.UPDATE_AT), updateAfter);
        };
    }
}
