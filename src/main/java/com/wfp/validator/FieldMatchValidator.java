package com.wfp.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>
{
    private String firstFieldName;
    private String secondFieldName;
    private String errorMessageName; 

    @Override
    public void initialize(final FieldMatch constraintAnnotation)
    {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        errorMessageName = constraintAnnotation.errorMessage(); 
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext cvc){
        boolean toReturn = false;

        try{
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName );
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName );

            //System.out.println("firstObj = "+firstObj+"   secondObj = "+secondObj);

            toReturn = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        }
        catch (final Exception e){
            System.out.println(e.toString());
        }
        //If the validation failed
        if(!toReturn) {
            cvc.disableDefaultConstraintViolation();
            //In the initialiaze method you get the errorMessage: constraintAnnotation.message();
            cvc.buildConstraintViolationWithTemplate(errorMessageName).addNode(secondFieldName).addConstraintViolation();
        }
        return toReturn;
    }
}