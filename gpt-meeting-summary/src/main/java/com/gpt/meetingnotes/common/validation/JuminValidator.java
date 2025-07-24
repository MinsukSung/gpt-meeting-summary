package com.gpt.meetingnotes.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.gpt.meetingnotes.common.annotation.ValidJumin;
import com.gpt.meetingnotes.common.utils.ValidationUtils;

public class JuminValidator implements ConstraintValidator<ValidJumin, String> {

    @Override
    public void initialize(ValidJumin constraintAnnotation) {
        // no-op
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ValidationUtils.isValidJumin(value);
    }
}