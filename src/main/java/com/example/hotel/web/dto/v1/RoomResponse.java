package com.example.hotel.web.dto.v1;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RoomResponse(
        UUID id,
        String name,
        String description,
        String roomNumber,
        BigDecimal price,
        Integer capacity,
        List<Instant> unavailableDates,
        HotelResponse hotelResponse,
//        UUID hotelId,
        Instant createAt,
        Instant updateAt
) {
}
