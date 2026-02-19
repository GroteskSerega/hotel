package com.example.hotel.web.dto.v1;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static com.example.hotel.web.dto.FieldsSizes.RATING_MAX;
import static com.example.hotel.web.dto.FieldsSizes.RATING_MIN;
import static com.example.hotel.web.dto.v1.HotelErrorMessageTemplates.*;

public record HotelRatingUpsertRequest(
        @NotNull(message = VALIDATE_RATING_BLANK)
        @Min(value = RATING_MIN, message = VALIDATE_RATING_INCORRECT_MIN_VALUE)
        @Max(value = RATING_MAX, message = VALIDATE_RATING_INCORRECT_MAX_VALUE)
        Integer newMark
) {
}
