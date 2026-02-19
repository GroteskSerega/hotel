package com.example.hotel.web.dto.v1;

import com.example.hotel.validation.RoomFilterValid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static com.example.hotel.web.dto.FieldsSizes.*;
import static com.example.hotel.web.dto.FieldsSizes.NAME_SIZE_MAX;
import static com.example.hotel.web.dto.PageErrorMessageTemplates.*;
import static com.example.hotel.web.dto.RegexDto.CYRILLIC_LATIN_DIGITS_SIGNS_REGEX;
import static com.example.hotel.web.dto.v1.RoomErrorMessageTemplates.*;
import static com.example.hotel.web.dto.v1.RoomErrorMessageTemplates.VALIDATE_ROOM_DESCRIPTION_INCORRECT_REGEX;

@RoomFilterValid
public record RoomFilter(
        @Min(value = PAGE_SIZE_MIN, message = VALIDATE_PAGE_SIZE_MIN_INCORRECT)
        @Max(value = PAGE_SIZE_MAX, message = VALIDATE_PAGE_SIZE_MAX_INCORRECT)
        Integer pageSize,

        @PositiveOrZero(message = VALIDATE_PAGE_NUMBER_INCORRECT)
        Integer pageNumber,

        UUID id,

        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_ROOM_NAME_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ROOM_NAME_INCORRECT_REGEX)
        String name,

        @Size(min = BIG_TEXT_SIZE_MIN, max = BIG_TEXT_SIZE_MAX, message = VALIDATE_ROOM_DESCRIPTION_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ROOM_DESCRIPTION_INCORRECT_REGEX)
        String description,

        @Size(min = BIG_TEXT_SIZE_MIN, max = BIG_TEXT_SIZE_MAX, message = VALIDATE_ROOM_ROOM_NUMBER_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ROOM_ROOM_NUMBER_INCORRECT_REGEX)
        String roomNumber,

        @PositiveOrZero
        BigDecimal minPrice,

        @PositiveOrZero
        BigDecimal maxPrice,

        @Min(value = CAPACITY_MIN, message = VALIDATE_ROOM_CAPACITY_MIN_INCORRECT)
        @Max(value = CAPACITY_MAX, message = VALIDATE_ROOM_CAPACITY_MAX_INCORRECT)
        Integer minCapacity,

        @Min(value = CAPACITY_MIN, message = VALIDATE_ROOM_CAPACITY_MIN_INCORRECT)
        @Max(value = CAPACITY_MAX, message = VALIDATE_ROOM_CAPACITY_MAX_INCORRECT)
        Integer maxCapacity,

        UUID hotelId,

        Instant checkIn,

        Instant checkOut,

        Instant createBefore,

        Instant updateBefore,

        Instant createAfter,

        Instant updateAfter
) {
}
