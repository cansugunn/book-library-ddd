package com.finalproject.presentation.spring.api.customvalidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueOfEnum {

    Class<? extends Enum<?>> enumClass();

    String message() default "must be any of {acceptedValues}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}