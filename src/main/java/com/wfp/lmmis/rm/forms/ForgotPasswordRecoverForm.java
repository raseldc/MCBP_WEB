/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.rm.forms;

import com.wfp.validator.FieldMatch;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Philip
 */
@FieldMatch.List(
        {
            @FieldMatch(first = "newPassword", second = "reEnterPassword", errorMessage = "New password and Re-enter password are not matched"),
        })
public class ForgotPasswordRecoverForm
{

    @NotEmpty(message = "Password can not be blank")
    @Pattern(regexp = "|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Password policy is not matched")
    private String newPassword;
    @NotEmpty(message = "Re-enter password can not be blank")
    @Pattern(regexp = "|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Password policy is not matched")
    private String reEnterPassword;

    public String getNewPassword()
    {
        return newPassword;
    }

    /**
     *
     * @param newPassword
     */
    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }

    /**
     *
     * @return
     */
    public String getReEnterPassword()
    {
        return reEnterPassword;
    }

    public void setReEnterPassword(String reEnterPassword)
    {
        this.reEnterPassword = reEnterPassword;
    }
}
