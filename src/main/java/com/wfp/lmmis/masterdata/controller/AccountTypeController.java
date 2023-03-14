/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.AccountType;
import com.wfp.lmmis.masterdata.service.AccountTypeService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author user
 */
@Controller
public class AccountTypeController
{

    @Autowired
    private AccountTypeService accountTypeService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/accountType/list")
    public String showAccountTypeList(Model model)
    {
        System.out.println("accountType list");
        List<AccountType> accountTypeList = this.accountTypeService.getAccountTypeList();
        Locale locale = LocaleContextHolder.getLocale();
        if ("bn".equals(locale.getLanguage()))
        {
            for (AccountType accountType : accountTypeList)
            {
                accountType.setCode(CommonUtility.getNumberInBangla(accountType.getCode()));
            }
        }
        model.addAttribute("accountTypeList", accountTypeList);
        return "accountTypeList";
    }

    @RequestMapping(value = "/accountType/create", method = RequestMethod.GET)
    public String createAccountType(@ModelAttribute AccountType accountType, Model model)
    {
        System.out.println("in accountType create");
        model.addAttribute("actionType", "create");
        return "accountType";
    }

    @RequestMapping(value = "/accountType/{id}", method = RequestMethod.GET)
    public String getAccountType(@PathVariable("id") Integer id, Model model)
    {
        model.addAttribute("accountType", this.accountTypeService.getAccountType(id));
        return "accountType";
    }

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/accountTypeList", method = RequestMethod.GET)
    public String getAccountTypeList(Model model)
    {
        List<AccountType> accountTypeList = this.accountTypeService.getAccountTypeList();
        for (AccountType accountType : accountTypeList)
        {
            System.out.println("AccountType = " + accountType.getNameInEnglish());
        }
        model.addAttribute("accountTypeList", this.accountTypeService.getAccountTypeList());
        return "";
    }

    @RequestMapping(value = "/accountType/create", method = RequestMethod.POST)
    public String saveAccountType(@Valid AccountType accountType, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        System.out.println("account type post");
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "accountType";
        }
        boolean saveResult = false;
        try
        {
            if (accountType.getId() == null)
            {
                accountType.setCreationDate(Calendar.getInstance());
                accountType.setCreatedBy((User) session.getAttribute("user"));
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage()))
                {
                    accountType.setCode(CommonUtility.getNumberInEnglish(accountType.getCode()));
                }
                saveResult = this.accountTypeService.save(accountType);
                if (saveResult)
                {
                    String message = "Account Type has been created successfully";
                    ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, message);
                    redirectAttributes.addFlashAttribute("saveMsg", controllerMessage);
                }
                else
                {
                    ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
                    redirectAttributes.addFlashAttribute("message", controllerMessage);
                }

            }
            else
            {
                accountType.setModificationDate(Calendar.getInstance());
                accountType.setModifiedBy((User) session.getAttribute("user"));
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage()))
                {
                    accountType.setCode(CommonUtility.getNumberInEnglish(accountType.getCode()));
                }
                this.accountTypeService.edit(accountType);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/accountType/list";
    }

    @RequestMapping(value = "/accountType/edit/{id}", method = RequestMethod.GET)
    public String editAccountType(@PathVariable("id") Integer id, @ModelAttribute AccountType accountType, Model model, boolean accountTypeLoadRequired)
    {
        try
        {
            System.out.println("accountTypeLoadRequired = " + accountTypeLoadRequired);
            if (!accountTypeLoadRequired)
            {
                model.addAttribute("accountType", this.accountTypeService.getAccountType(id));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "accountType";
    }

    @RequestMapping(value = "/accountType/edit/{id}", method = RequestMethod.POST)
    public String editAccountType(@PathVariable("id") Integer pk, @Valid @ModelAttribute AccountType accountType, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
    {
        try
        {
            System.out.println("in accountType dao impl");
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (bindingResult.hasErrors())
            {
                return editAccountType(pk, accountType, model, true);
            }
            if (accountType.getId() == null)
            {
                accountType.setCreatedBy((User) session.getAttribute("user"));
                accountType.setCreationDate(Calendar.getInstance());
                this.accountTypeService.save(accountType);
            }
            else
            {
                System.out.println("bank.creationDate = " + accountType.getCreationDate());
                accountType.setModifiedBy((User) session.getAttribute("user"));
                accountType.setModificationDate(Calendar.getInstance());
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage()))
                {
                    accountType.setCode(CommonUtility.getNumberInEnglish(accountType.getCode()));
                }
                this.accountTypeService.edit(accountType);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/accountType/list";
    }

    /**
     *
     * @param accountTypeId
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "accountType/delete/{accountTypeId}")
    public String deleteAccountType(@PathVariable("accountTypeId") Integer accountTypeId, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        System.out.println("in accountType delete");
        AccountType accountType = this.accountTypeService.getAccountType(accountTypeId);
        accountType.setModifiedBy((User) session.getAttribute("user"));
        accountType.setModificationDate(Calendar.getInstance());
        this.accountTypeService.delete(accountType);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/accountType/list";
    }
}
