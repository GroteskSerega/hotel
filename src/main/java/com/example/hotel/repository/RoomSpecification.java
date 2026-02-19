package com.example.hotel.repository;

import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.Hotel_;
import com.example.hotel.entity.Room;
import com.example.hotel.entity.Room_;
import com.example.hotel.web.dto.v1.RoomFilter;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.UUID;

import static com.example.hotel.repository.SpecificationRegex.TEMPLATE_LIKE;

public interface RoomSpecification {

    static Specification<Room> withFilter(RoomFilter roomFilter) {
        return Specification.allOf(
                byName(roomFilter.name()),
                byDescription(roomFilter.description()),
                byRoomNumber(roomFilter.roomNumber()),
                byMinPrice(roomFilter.minPrice()),
                byMaxPrice(roomFilter.maxPrice()),
                byMinCapacity(roomFilter.minCapacity()),
                byMaxCapacity(roomFilter.maxCapacity()),
                byHotelId(roomFilter.hotelId()),
                byCreateAtBefore(roomFilter.createBefore()),
                byUpdateAtBefore(roomFilter.updateBefore()),
                byCreateAtAfter(roomFilter.createAfter()),
                byUpdateAtAfter(roomFilter.updateAfter())
        );
    }

    static Specification<Room> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }

            String pattern = MessageFormat.format(TEMPLATE_LIKE, name.toLowerCase());

            Expression<String> lowerCaseField = criteriaBuilder.lower(root.get(Room_.NAME));

            return criteriaBuilder.like(lowerCaseField, pattern);
        };
    }

    static Specification<Room> byDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null) {
                return null;
            }

            String pattern = MessageFormat.format(TEMPLATE_LIKE, description.toLowerCase());

            Expression<String> lowerCaseField = criteriaBuilder.lower(root.get(Room_.DESCRIPTION));

            return criteriaBuilder.like(lowerCaseField, pattern);
        };
    }

    static Specification<Room> byRoomNumber(String roomNumber) {
        return (root, query, criteriaBuilder) -> {
            if (roomNumber == null) {
                return null;
            }

            String pattern = MessageFormat.format(TEMPLATE_LIKE, roomNumber.toLowerCase());

            Expression<String> lowerCaseField = criteriaBuilder.lower(root.get(Room_.ROOM_NUMBER));

            return criteriaBuilder.like(lowerCaseField, pattern);
        };
    }

    static Specification<Room> byMinPrice(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Room_.PRICE), minPrice);
        };
    }

    static Specification<Room> byMaxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Room_.PRICE), maxPrice);
        };
    }

    static Specification<Room> byMinCapacity(Integer minCapacity) {
        return (root, query, criteriaBuilder) -> {
            if (minCapacity == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Room_.CAPACITY), minCapacity);
        };
    }

    static Specification<Room> byMaxCapacity(Integer maxCapacity) {
        return (root, query, criteriaBuilder) -> {
            if (maxCapacity == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Room_.CAPACITY), maxCapacity);
        };
    }

    static Specification<Room> byHotelId(UUID hostelId) {
        return (root, query, criteriaBuilder) -> {
            if (hostelId == null) {
                return null;
            }

            Join<Room, Hotel> hotelJoin = root.join(Room_.HOTEL);

            return criteriaBuilder.equal(hotelJoin.get(Hotel_.ID), hostelId);
        };
    }

    static Specification<Room> byCreateAtBefore(Instant createBefore) {
        return (root, query, criteriaBuilder) -> {
            if (createBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Room_.CREATE_AT), createBefore);
        };
    }

    static Specification<Room> byUpdateAtBefore(Instant updateBefore) {
        return (root, query, criteriaBuilder) -> {
            if (updateBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Room_.UPDATE_AT), updateBefore);
        };
    }

    static Specification<Room> byCreateAtAfter(Instant createAfter) {
        return (root, query, criteriaBuilder) -> {
            if (createAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Room_.CREATE_AT), createAfter);
        };
    }

    static Specification<Room> byUpdateAtAfter(Instant updateAfter) {
        return (root, query, criteriaBuilder) -> {
            if (updateAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Room_.UPDATE_AT), updateAfter);
        };
    }
}
