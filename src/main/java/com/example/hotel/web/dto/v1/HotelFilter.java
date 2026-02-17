package com.example.hotel.web.dto.v1;

import com.example.hotel.validation.HotelFilterValid;
import jakarta.validation.constraints.*;

import java.time.Instant;

import static com.example.hotel.web.dto.FieldsSizes.*;
import static com.example.hotel.web.dto.FieldsSizes.NAME_SIZE_MAX;
import static com.example.hotel.web.dto.FieldsSizes.NAME_SIZE_MIN;
import static com.example.hotel.web.dto.PageErrorMessageTemplates.*;
import static com.example.hotel.web.dto.RegexDto.CYRILLIC_LATIN_DIGITS_SIGNS_REGEX;
import static com.example.hotel.web.dto.v1.HotelErrorMessageTemplates.*;
import static com.example.hotel.web.dto.v1.HotelErrorMessageTemplates.VALIDATE_ADDRESS_INCORRECT_REGEX;

@HotelFilterValid
public record HotelFilter(
        @Min(value = PAGE_SIZE_MIN, message = VALIDATE_PAGE_SIZE_MIN_INCORRECT)
        @Max(value = PAGE_SIZE_MAX, message = VALIDATE_PAGE_SIZE_MAX_INCORRECT)
        Integer pageSize,

        @PositiveOrZero(message = VALIDATE_PAGE_NUMBER_INCORRECT)
        Integer pageNumber,

        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_HOTEL_NAME_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_HOTEL_NAME_INCORRECT_REGEX)
        String name,

        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_AD_TITLE_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_AD_TITLE_INCORRECT_REGEX)
        String adTitle,

        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_CITY_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_CITY_INCORRECT_REGEX)
        String city,

        @Size(min = FILTER_BIG_TEXT_SIZE_MIN, max = FILTER_BIG_TEXT_SIZE_MAX, message = VALIDATE_ADDRESS_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ADDRESS_INCORRECT_REGEX)
        String address,

        Double minDistanceFromCenter,

        Double maxDistanceFromCenter,

        Double minRating,

        Double maxRating,

        Integer minRatingCount,

        Integer maxRatingCount,

        Instant createBefore,

        Instant updateBefore,

        Instant createAfter,

        Instant updateAfter
) {
}
