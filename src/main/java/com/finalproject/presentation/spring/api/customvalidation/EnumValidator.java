package com.finalproject.presentation.spring.api.customvalidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValueOfEnum, String> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = false;
        if (value != null) {
            valid = acceptedValues.contains(value);
        }

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "must be one of: " + String.join(", ", acceptedValues)
            ).addConstraintViolation();
        }

        return valid;
    }
}