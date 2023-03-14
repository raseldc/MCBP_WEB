/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.controller;

import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionStyleExpression;
import java.util.Map;

/**
 *
 * @author user
 */
public class StringConditionExpression extends ConditionStyleExpression implements CustomExpression
{
    private String fieldName;
    private String targetValue;

    public StringConditionExpression(String fieldName, String targetValue)
    {
        this.fieldName = fieldName;
        this.targetValue = targetValue;
    }

    @Override
    public Object evaluate(Map fields, Map variables, Map parameters)
    {
        try
        {
            String val = (String) fields.get(fieldName);
            if(val!=null && val.equalsIgnoreCase(targetValue)){
                return true;
            }
            
        }
        catch (Exception e)
        {
        }
        return false;
    }

    @Override
    public String getClassName()
    {
        if(getCurrentValue()!=null){
            return getCurrentValue().getClass().getName();
        }
        return String.class.getName();
    }

    @Override
    public Object getCurrentValue()
    {
        return super.getCurrentValue(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
