package com.example.hotel.web.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HotelResponse(
        UUID id,
        String name,
        String adTitle,
        String city,
        String address,
        Double distanceFromCenter,
        Double rating,
        Integer ratingCount) {
}
