package com.myicpc.validator;

import com.myicpc.model.security.SystemUser;
import com.myicpc.validator.annotation.ValidateSystemUser;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Roman Smetana
 */
public class SystemUserValidator implements ConstraintValidator<ValidateSystemUser, SystemUser> {

    @Override
    public void initialize(ValidateSystemUser constraintAnnotation) {

    }

    @Override
    public boolean isValid(SystemUser systemUser, ConstraintValidatorContext context) {
        boolean valid = true;
        return valid;
    }
}
