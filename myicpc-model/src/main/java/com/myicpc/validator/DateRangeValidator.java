package com.myicpc.validator;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.model.StartEndDateObject;
import com.myicpc.model.security.SystemUser;
import com.myicpc.validator.annotation.ValidateDateRange;
import com.myicpc.validator.annotation.ValidateSystemUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Roman Smetana
 */
public class DateRangeValidator implements ConstraintValidator<ValidateDateRange, StartEndDateObject> {

    @Override
    public void initialize(ValidateDateRange constraintAnnotation) {

    }

    @Override
    public boolean isValid(StartEndDateObject startEndDateObject, ConstraintValidatorContext context) {
        boolean valid = true;
        if (startEndDateObject.getStartDate() != null &&
                startEndDateObject.getEndDate() != null &&
                startEndDateObject.getStartDate().after(startEndDateObject.getEndDate())) {
            valid = false;
            context.buildConstraintViolationWithTemplate(MessageUtils.getMessage("ValidateDateRange")).addPropertyNode("localStartDate")
                    .addConstraintViolation();
        }
        return valid;
    }
}
