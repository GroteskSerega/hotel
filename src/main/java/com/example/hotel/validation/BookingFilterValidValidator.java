package com.example.hotel.validation;

import com.example.hotel.web.dto.v1.BookingFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class BookingFilterValidValidator implements ConstraintValidator<BookingFilterValid, BookingFilter> {

    @Override
    public boolean isValid(BookingFilter bookingFilter, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(bookingFilter.pageNumber()) || Objects.isNull(bookingFilter.pageSize())) {
            return false;
        }

        return true;
    }
}
