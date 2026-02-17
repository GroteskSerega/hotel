package com.example.hotel.repository;

import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.Hotel_;
import com.example.hotel.web.dto.v1.HotelFilter;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;
import java.time.Instant;

public interface HotelSpecification {

    String TEMPLATE_LIKE = "%{0}%";

    static Specification<Hotel> withFilter(HotelFilter hotelFilter) {
        return Specification.allOf(byName(hotelFilter.name())
                .and(byAdTitle(hotelFilter.adTitle()))
                .and(byCity(hotelFilter.city()))
                .and(byAddress(hotelFilter.address()))
                .and(byMinDistanceFromCenter(hotelFilter.minDistanceFromCenter()))
                .and(byMaxDistanceFromCenter(hotelFilter.maxDistanceFromCenter()))
                .and(byMinRating(hotelFilter.minRating()))
                .and(byMaxRating(hotelFilter.maxRating()))
                .and(byCreateAtBefore(hotelFilter.createBefore()))
                .and(byUpdateAtBefore(hotelFilter.updateBefore()))
                .and(byCreateAtAfter(hotelFilter.createAfter()))
                .and(byUpdateAtAfter(hotelFilter.updateAfter())));
    }

    static Specification<Hotel> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }

            String pattern = MessageFormat.format(TEMPLATE_LIKE, name.toLowerCase());

            Expression<String> lowerCaseField = criteriaBuilder.lower(root.get(Hotel_.NAME));

            return criteriaBuilder.like(lowerCaseField, pattern);
        };
    }

    static Specification<Hotel> byAdTitle(String adTitle) {
        return (root, query, criteriaBuilder) -> {
            if (adTitle == null) {
                return null;
            }

            String pattern = MessageFormat.format(TEMPLATE_LIKE, adTitle.toLowerCase());

            Expression<String> lowerCaseField = criteriaBuilder.lower(root.get(Hotel_.AD_TITLE));

            return criteriaBuilder.like(lowerCaseField, pattern);
        };
    }

    static Specification<Hotel> byCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null) {
                return null;
            }

            String pattern = MessageFormat.format(TEMPLATE_LIKE, city.toLowerCase());

            Expression<String> lowerCaseField = criteriaBuilder.lower(root.get(Hotel_.CITY));

            return criteriaBuilder.like(lowerCaseField, pattern);
        };
    }

    static Specification<Hotel> byAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (address == null) {
                return null;
            }

            String pattern = MessageFormat.format(TEMPLATE_LIKE, address.toLowerCase());

            Expression<String> lowerCaseField = criteriaBuilder.lower(root.get(Hotel_.ADDRESS));

            return criteriaBuilder.like(lowerCaseField, pattern);
        };
    }

    static Specification<Hotel> byMinDistanceFromCenter(Double minDistanceFromCenter) {
        return (root, query, criteriaBuilder) -> {
            if (minDistanceFromCenter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel_.DISTANCE_FROM_CENTER),
                    minDistanceFromCenter);
        };
    }

    static Specification<Hotel> byMaxDistanceFromCenter(Double maxDistanceFromCenter) {
        return (root, query, criteriaBuilder) -> {
            if (maxDistanceFromCenter == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Hotel_.DISTANCE_FROM_CENTER),
                    maxDistanceFromCenter);
        };
    }

    static Specification<Hotel> byMinRating(Double minRating) {
        return (root, query, criteriaBuilder) -> {
            if (minRating == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel_.RATING), minRating);
        };
    }

    static Specification<Hotel> byMaxRating(Double maxRating) {
        return (root, query, criteriaBuilder) -> {
            if (maxRating == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Hotel_.RATING), maxRating);
        };
    }

    static Specification<Hotel> byMinRatingCount(Integer minRatingCount) {
        return (root, query, criteriaBuilder) -> {
            if (minRatingCount == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel_.RATING_COUNT), minRatingCount);
        };
    }

    static Specification<Hotel> byMaxRatingCount(Integer maxRatingCount) {
        return (root, query, criteriaBuilder) -> {
            if (maxRatingCount == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Hotel_.RATING_COUNT), maxRatingCount);
        };
    }

    static Specification<Hotel> byCreateAtBefore(Instant createBefore) {
        return (root, query, criteriaBuilder) -> {
            if (createBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Hotel_.CREATE_AT), createBefore);
        };
    }

    static Specification<Hotel> byUpdateAtBefore(Instant updateBefore) {
        return (root, query, criteriaBuilder) -> {
            if (updateBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Hotel_.UPDATE_AT), updateBefore);
        };
    }

    static Specification<Hotel> byCreateAtAfter(Instant createAfter) {
        return (root, query, criteriaBuilder) -> {
            if (createAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel_.CREATE_AT), createAfter);
        };
    }

    static Specification<Hotel> byUpdateAtAfter(Instant updateAfter) {
        return (root, query, criteriaBuilder) -> {
            if (updateAfter == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel_.UPDATE_AT), updateAfter);
        };
    }
}
