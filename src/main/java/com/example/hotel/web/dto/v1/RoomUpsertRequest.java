package com.example.hotel.web.dto.v1;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.example.hotel.web.dto.FieldsSizes.*;
import static com.example.hotel.web.dto.RegexDto.CYRILLIC_LATIN_DIGITS_SIGNS_REGEX;
import static com.example.hotel.web.dto.v1.RoomErrorMessageTemplates.*;

public record RoomUpsertRequest(
        @NotBlank(message = VALIDATE_ROOM_NAME_BLANK)
        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_ROOM_NAME_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ROOM_NAME_INCORRECT_REGEX)
        String name,

        @NotBlank(message = VALIDATE_ROOM_DESCRIPTION_BLANK)
        @Size(min = BIG_TEXT_SIZE_MIN, max = BIG_TEXT_SIZE_MAX, message = VALIDATE_ROOM_DESCRIPTION_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ROOM_DESCRIPTION_INCORRECT_REGEX)
        String description,

        @NotBlank(message = VALIDATE_ROOM_ROOM_NUMBER_BLANK)
        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_ROOM_ROOM_NUMBER_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ROOM_ROOM_NUMBER_INCORRECT_REGEX)
        String roomNumber,

        @Positive
        BigDecimal price,

        @NotNull(message = VALIDATE_ROOM_CAPACITY_BLANK)
        @Min(value = CAPACITY_MIN, message = VALIDATE_ROOM_CAPACITY_MIN_INCORRECT)
        @Max(value = CAPACITY_MAX, message = VALIDATE_ROOM_CAPACITY_MAX_INCORRECT)
        Integer capacity,

        List<Instant> unavailableDates,

        UUID hotelId
) {
}
