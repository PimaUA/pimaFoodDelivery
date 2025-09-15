package com.pimaua.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OpeningHoursValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOpeningHours {
    String message() default "Invalid opening hours configuration";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}