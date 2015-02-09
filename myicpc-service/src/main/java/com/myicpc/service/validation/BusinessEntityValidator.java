package com.myicpc.service.validation;

import com.myicpc.model.EntityObject;
import com.myicpc.service.exception.BusinessValidationException;

/**
 * @author Roman Smetana
 */
public abstract class BusinessEntityValidator<T extends EntityObject> {
    public abstract void validate(T entity) throws BusinessValidationException;
}
