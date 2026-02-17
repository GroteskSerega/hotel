package com.example.hotel.web.dto.v1;

import com.example.hotel.validation.UserFilterValid;
import jakarta.validation.constraints.*;

import java.time.Instant;

import static com.example.hotel.web.dto.FieldsSizes.*;
import static com.example.hotel.web.dto.PageErrorMessageTemplates.*;
import static com.example.hotel.web.dto.RegexDto.EMAIL_REGEX;
import static com.example.hotel.web.dto.RegexDto.LATIN_REGEX;
import static com.example.hotel.web.dto.v1.UserErrorMessageTemplates.*;

@UserFilterValid
public record UserFilter(
        @Min(value = PAGE_SIZE_MIN, message = VALIDATE_PAGE_SIZE_MIN_INCORRECT)
        @Max(value = PAGE_SIZE_MAX, message = VALIDATE_PAGE_SIZE_MAX_INCORRECT)
        Integer pageSize,

        @PositiveOrZero(message = VALIDATE_PAGE_NUMBER_INCORRECT)
        Integer pageNumber,

        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_USER_USERNAME_INCORRECT_SIZE)
        @Pattern(regexp = LATIN_REGEX, message = VALIDATE_USER_USERNAME_INCORRECT_REGEX)
        String username,

        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_USER_EMAIL_INCORRECT_SIZE)
        @Email(regexp = EMAIL_REGEX, message = VALIDATE_USER_EMAIL_INCORRECT_REGEX)
        String email,

        Instant createBefore,

        Instant updateBefore,

        Instant createAfter,

        Instant updateAfter
) {
}
