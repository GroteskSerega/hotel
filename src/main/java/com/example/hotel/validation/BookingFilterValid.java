package com.example.hotel.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static com.example.hotel.validation.ValidErrorMessageTemplates.FIELDS_PAGE_REQUEST_NULL;

@Documented
@Constraint(validatedBy = BookingFilterValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BookingFilterValid {

    String message() default FIELDS_PAGE_REQUEST_NULL;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
