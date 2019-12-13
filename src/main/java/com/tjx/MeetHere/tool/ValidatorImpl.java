package com.tjx.MeetHere.tool;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;

    public ValidationResult validate(Object bean) {
        ValidationResult validationResult = new ValidationResult();
        Set<ConstraintViolation<Object>> validationSet = validator.validate(bean);
        if (validationSet.size() > 0) {
            validationResult.setHasError(true);
            validationSet.forEach(validation -> {
                String errorMessage = validation.getMessage();
                String propertyName = validation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertyName, errorMessage);
            });
        }
        return validationResult;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
