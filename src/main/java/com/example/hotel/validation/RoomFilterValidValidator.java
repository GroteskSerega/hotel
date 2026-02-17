package com.example.hotel.validation;

import com.example.hotel.web.dto.v1.RoomFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class RoomFilterValidValidator implements ConstraintValidator<RoomFilterValid, RoomFilter> {

    @Override
    public boolean isValid(RoomFilter roomFilter,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(roomFilter.pageNumber()) || Objects.isNull(roomFilter.pageSize())) {
            return false;
        }

        return true;
    }
}
