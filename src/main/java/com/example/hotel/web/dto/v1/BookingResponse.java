package com.example.hotel.web.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingResponse(
        UUID id,
        Instant checkInDate,
        Instant checkOutDate,
        RoomResponse roomResponse,
        UserResponse userResponse,
        Instant createAt,
        Instant updateAt
) {
}
