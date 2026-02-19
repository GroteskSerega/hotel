package com.example.hotel.validation;

import com.example.hotel.web.dto.v1.BookingFilter;
import com.example.hotel.web.dto.v1.BookingUpsertRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookingDatesValidValidator implements ConstraintValidator<BookingDatesValid, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value instanceof BookingUpsertRequest req) {
            return req.checkInDate() == null || req.checkOutDate() == null ||
                    req.checkInDate().isBefore(req.checkOutDate());
        }

        if (value instanceof BookingFilter filter) {
            if (filter.checkInDateAfter() != null && filter.checkOutDateBefore() != null) {
                return filter.checkInDateAfter().isBefore(filter.checkOutDateBefore());
            }

            return true;
        }

        return true;
    }
}
