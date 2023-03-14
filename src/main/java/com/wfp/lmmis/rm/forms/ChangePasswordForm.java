package com.wfp.lmmis.rm.forms;

import com.wfp.validator.FieldMatch;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", errorMessage = "The password fields must match"), // @FieldMatch(first = "email", second = "confirmEmail", errorMessage = "The email fields must match")
})
public class ChangePasswordForm{

    @NotNull
    private String oldPassword;

    @NotNull
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Must match password policy")
    private String password;

    @NotNull
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Must match password policy")
    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    /**
     *
     * @param oldPassword
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
