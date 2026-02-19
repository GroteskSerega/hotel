package com.example.hotel.event;

import java.time.Instant;
import java.util.UUID;

public record BookingEvent(
        UUID userId,
        Instant checkIn,
        Instant checkOut
) {
}
