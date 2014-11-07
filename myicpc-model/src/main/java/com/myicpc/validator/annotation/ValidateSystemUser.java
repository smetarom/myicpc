package com.myicpc.validator.annotation;

import com.myicpc.validator.SystemUserValidator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = SystemUserValidator.class)
@Documented
public @interface ValidateSystemUser {
    String message() default "An unknown error within System user has occurred!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
