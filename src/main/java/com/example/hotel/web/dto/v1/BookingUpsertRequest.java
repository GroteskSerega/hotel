package com.example.hotel.web.dto.v1;

import com.example.hotel.validation.BookingDatesValid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@BookingDatesValid
public record BookingUpsertRequest(
        @NotNull
        @FutureOrPresent
        Instant checkInDate,

        @NotNull
        @FutureOrPresent
        Instant checkOutDate,
        UUID roomId
) {
}
