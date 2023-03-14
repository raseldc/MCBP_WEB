package com.wfp.lmmis.rm.forms;

import com.wfp.validator.FieldMatch;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@FieldMatch.List(
        {
            @FieldMatch(first = "password", second = "confirmPassword", errorMessage = "The password fields must match"), // @FieldMatch(first = "email", second = "confirmEmail", errorMessage = "The email fields must match")
        })
public class ChangePasswordByAdminForm {

    @NotNull
    private String userID;

    private Integer userId;

    private int loginedUserType;

    @NotNull
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Must match password policy")
    private String password;

    @NotNull
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Must match password policy")
    private String confirmPassword;

    public int getLoginedUserType() {
        return loginedUserType;
    }

    public void setLoginedUserType(int loginedUserType) {
        this.loginedUserType = loginedUserType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     *
     * @return
     */
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    /**
     *
     * @param confirmPassword
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
