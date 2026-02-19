package com.example.hotel.web.dto.v1;

import com.example.hotel.validation.BookingDatesValid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

import static com.example.hotel.web.dto.v1.BookingErrorMessageTemplates.*;

@BookingDatesValid
public record BookingUpsertRequest(
        @NotNull(message = VALIDATE_BOOKING_CHECK_IN_DATE_BLANK)
        @FutureOrPresent(message = VALIDATE_BOOKING_CHECK_IN_DATE_FUTURE)
        Instant checkInDate,

        @NotNull(message = VALIDATE_BOOKING_CHECK_OUT_DATE_BLANK)
        @FutureOrPresent(message = VALIDATE_BOOKING_CHECK_OUT_DATE_FUTURE)
        Instant checkOutDate,
        UUID roomId
) {
}
