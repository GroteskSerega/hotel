package com.example.hotel.web.dto.v1;

import java.util.UUID;

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
