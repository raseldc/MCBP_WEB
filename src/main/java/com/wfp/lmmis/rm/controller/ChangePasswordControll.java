/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.rm.controller;

import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.forms.ChangePasswordForm;
import com.wfp.lmmis.rm.forms.ChangePasswordFormFirstTime;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.service.UserService;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.Localizer;
import java.security.SecureRandom;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Controller
public class ChangePasswordControll {

    @Autowired
    private UserService userService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/change-password-first-time", method = RequestMethod.GET)
    public String getChangePassword(Model modelAndView, @ModelAttribute ChangePasswordFormFirstTime changePasswordFormFirstTime, HttpSession session) {

        User user = this.userService.getUser((Integer) session.getAttribute("userId"));
        changePasswordFormFirstTime.setEmail(user.getEmail());
        changePasswordFormFirstTime.setMobileNo(user.getMobileNo());
        changePasswordFormFirstTime.setDesignation(user.getDesignation());
        changePasswordFormFirstTime.setFullNameBn(user.getFullNameBn());
        changePasswordFormFirstTime.setFullNameEn(user.getFullNameEn());
        changePasswordFormFirstTime.setId(user.getId());
        return "changePasswordFirstTime";
    }

    /**
     *
     * @param changePasswordFormFirstTime
     * @param bindingResult
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/change-password-first-time", method = RequestMethod.POST)
    public String changePassword(@Valid ChangePasswordFormFirstTime changePasswordFormFirstTime, BindingResult bindingResult, HttpSession session, Model model) {
        String msg;
        if (bindingResult.hasErrors()) {
            return "changePasswordFirstTime";
        }
        String oldPassword = changePasswordFormFirstTime.getOldPassword();
        String newPassword = changePasswordFormFirstTime.getPassword();
        JsonResult jr = new JsonResult(false, "");
        User user = this.userService.getUser((Integer) session.getAttribute("userId"));
        String retrievedOldPasswordHash = DigestUtils.sha512Hex(oldPassword + user.getSalt());
        if (retrievedOldPasswordHash.equals(user.getPassword())) {
            String salt = getSalt();
            String combined = newPassword + salt;
            String hashedPassword = DigestUtils.sha512Hex(combined);
            UserDetail userDetail = new UserDetail();
            userDetail.setId(user.getId());
            userDetail.setEmail(changePasswordFormFirstTime.getEmail());
            userDetail.setMobile(changePasswordFormFirstTime.getMobileNo());
            userDetail.setPasseword(hashedPassword);
            userDetail.setUserNameBn(changePasswordFormFirstTime.getFullNameBn());
            userDetail.setUserNameEn(changePasswordFormFirstTime.getFullNameEn());
            userDetail.setDesignation(changePasswordFormFirstTime.getDesignation());
            userDetail.setSalt(salt);
            jr = this.userService.changePasswordAndBasicInfo(userDetail);
            msg = localizer.getLocalizedText("label.passwordChangeMessage", LocaleContextHolder.getLocale());
        } else {
            msg = localizer.getLocalizedText("label.currentPasswordNotMatched", LocaleContextHolder.getLocale());
        }
        model.addAttribute("msg", msg);
        if (jr.isIsError()) {
            return "changePasswordFirstTime";
        }
        return "redirect:/logout";
        //
    }

    public String getSalt() {
        String salt;
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        salt = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
        return salt;
    }

}
