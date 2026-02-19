package com.example.hotel.web.dto.v1;

import com.example.hotel.validation.BookingDatesValid;
import com.example.hotel.validation.BookingFilterValid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;
import java.util.UUID;

import static com.example.hotel.web.dto.FieldsSizes.PAGE_SIZE_MAX;
import static com.example.hotel.web.dto.FieldsSizes.PAGE_SIZE_MIN;
import static com.example.hotel.web.dto.PageErrorMessageTemplates.*;

@BookingDatesValid
@BookingFilterValid
public record BookingFilter(
        @Min(value = PAGE_SIZE_MIN, message = VALIDATE_PAGE_SIZE_MIN_INCORRECT)
        @Max(value = PAGE_SIZE_MAX, message = VALIDATE_PAGE_SIZE_MAX_INCORRECT)
        Integer pageSize,

        @PositiveOrZero(message = VALIDATE_PAGE_NUMBER_INCORRECT)
        Integer pageNumber,

        Instant checkInDateBefore,

        Instant checkOutDateBefore,

        Instant checkInDateAfter,

        Instant checkOutDateAfter,

        UUID roomId,

        Instant createBefore,

        Instant updateBefore,

        Instant createAfter,

        Instant updateAfter
) {
}
