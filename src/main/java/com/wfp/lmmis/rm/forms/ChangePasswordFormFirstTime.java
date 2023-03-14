package com.wfp.lmmis.rm.forms;

import com.wfp.validator.FieldMatch;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", errorMessage = "The password fields must match"), // @FieldMatch(first = "email", second = "confirmEmail", errorMessage = "The email fields must match")
})
public class ChangePasswordFormFirstTime {

    @NotNull
    private String oldPassword;

    @NotNull
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Must match password policy")
    private String password;

    @NotNull
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Must match password policy")
    private String confirmPassword;
    private Integer id;
    private String fullNameBn;
    private String fullNameEn;
    private String email;
    private String designation;
    private String mobileNo;

}
