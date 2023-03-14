/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.rm.forms;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Philip
 */
public class LoginForm
{

//    @Email
//    private String email;
    @NotEmpty(message = "User ID can not be empty")
    private String userID;

    @NotEmpty(message = "Password can not be empty")
    private String password;

    @NotEmpty(message = "Captch text can not be empty")
    private String captcha;

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCaptcha()
    {
        return captcha;
    }

    public void setCaptcha(String captcha)
    {
        this.captcha = captcha;
    }

}
