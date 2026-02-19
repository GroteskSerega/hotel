package com.example.hotel.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static com.example.hotel.validation.ValidErrorMessageTemplates.FIELDS_CHECK_IN_CHECK_OUT_INCORRECT_ORDER;

@Documented
@Constraint(validatedBy = BookingDatesValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BookingDatesValid {

    String message() default FIELDS_CHECK_IN_CHECK_OUT_INCORRECT_ORDER;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
