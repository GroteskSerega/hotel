package com.example.hotel.web.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoomResponse(
        UUID id,
        String name,
        String description,
        String roomNumber,
        BigDecimal price,
        Integer capacity,
        List<Instant> unavailableDates,
        HotelResponse hotelResponse,
        Instant createAt,
        Instant updateAt
) {
}
