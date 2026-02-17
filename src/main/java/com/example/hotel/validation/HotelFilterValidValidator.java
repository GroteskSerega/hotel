package com.example.hotel.validation;

import com.example.hotel.web.dto.v1.HotelFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class HotelFilterValidValidator implements ConstraintValidator<HotelFilterValid, HotelFilter> {

    @Override
    public boolean isValid(HotelFilter hotelFilter,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(hotelFilter.pageNumber()) || Objects.isNull(hotelFilter.pageSize())) {
            return false;
        }

        return true;
    }
}
