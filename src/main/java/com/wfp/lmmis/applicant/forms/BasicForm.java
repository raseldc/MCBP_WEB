/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.forms;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author PCUser
 */
public class BasicForm implements Serializable
{

    @Autowired
    private PersonalInfoForm personalInfoForm;

    public PersonalInfoForm getPersonalInfoForm()
    {
        return personalInfoForm;
    }

    public void setPersonalInfoForm(PersonalInfoForm personalInfoForm)
    {
        this.personalInfoForm = personalInfoForm;
    }

}
