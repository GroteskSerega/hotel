package com.example.hotel.web.dto.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.example.hotel.web.dto.FieldsSizes.*;
import static com.example.hotel.web.dto.RegexDto.CYRILLIC_LATIN_DIGITS_SIGNS_REGEX;
import static com.example.hotel.web.dto.v1.HotelErrorMessageTemplates.*;

public record HotelUpsertRequest(

        @NotBlank(message = VALIDATE_HOTEL_NAME_BLANK)
        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_HOTEL_NAME_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_HOTEL_NAME_INCORRECT_REGEX)
        String name,

        @NotBlank(message = VALIDATE_AD_TITLE_BLANK)
        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_AD_TITLE_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_AD_TITLE_INCORRECT_REGEX)
        String adTitle,

        @NotBlank(message = VALIDATE_CITY_BLANK)
        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_CITY_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_CITY_INCORRECT_REGEX)
        String city,

        @NotBlank(message = VALIDATE_ADDRESS_BLANK)
        @Size(min = BIG_TEXT_SIZE_MIN, max = BIG_TEXT_SIZE_MAX, message = VALIDATE_ADDRESS_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ADDRESS_INCORRECT_REGEX)
        String address,

        Double distanceFromCenter
) {
}
