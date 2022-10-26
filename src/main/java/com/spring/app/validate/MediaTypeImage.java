package com.spring.app.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = MediaTypeImageValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MediaTypeImage {
    String message() default "{MediaTypeImage.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
