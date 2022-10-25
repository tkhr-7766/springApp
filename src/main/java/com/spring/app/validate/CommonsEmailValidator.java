package com.spring.app.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.EmailValidator;

public class CommonsEmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public void initialize(Email email) {
        // nothing to do
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) {
            return true;
        }

        return EmailValidator.getInstance(false).isValid(s);
    }
}
