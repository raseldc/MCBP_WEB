/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.Bank;
import com.wfp.lmmis.masterdata.service.BankService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author user
 */
@Controller
public class BankController
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    private BankService bankService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/bank/list")
    public String showBankList(Model model)
    {
        List<Bank> bankList = this.bankService.getBankList();
        Locale locale = LocaleContextHolder.getLocale();
        if ("bn".equals(locale.getLanguage()))
        {
            for (Bank bank : bankList)
            {
                bank.setCode(CommonUtility.getNumberInBangla(bank.getCode()));
            }
        }
        model.addAttribute("bankList", bankList);
        return "bankList";
    }

    @RequestMapping(value = "/bank/create", method = RequestMethod.GET)
    public String createBank(@ModelAttribute Bank bank, Model model)
    {
        model.addAttribute("actionType", "create");
        return "bank";
    }

    /**
     *
     * @param bank
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/bank/create", method = RequestMethod.POST)
    public String createDivision(@Valid Bank bank, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "bank";
        }
        boolean saveResult = false;
        try
        {
            bank.setCreatedBy((User) session.getAttribute("user"));
            bank.setCreationDate(Calendar.getInstance());
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage()))
            {
                bank.setCode(CommonUtility.getNumberInEnglish(bank.getCode()));
            }
            saveResult = this.bankService.save(bank);
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        if (saveResult)
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }

        return "redirect:/bank/list";
    }

    @RequestMapping(value = "/bank/edit/{id}", method = RequestMethod.GET)
    public String editDivision(@PathVariable("id") Integer id, @ModelAttribute Bank bank, Model model, boolean bankLoadRequired)
    {
        try
        {
            if (!bankLoadRequired)
            {
                model.addAttribute("bank", this.bankService.getBank(id));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "bank";
    }

    @RequestMapping(value = "/bank/edit/{id}", method = RequestMethod.POST)
    public String editDivision(@PathVariable("id") Integer pk, @Valid @ModelAttribute Bank bank, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
    {
        try
        {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (bindingResult.hasErrors())
            {
                return editDivision(pk, bank, model, true);
            }
            if (bank.getId() == null)
            {
                bank.setCreatedBy((User) session.getAttribute("user"));
                bank.setCreationDate(Calendar.getInstance());
                this.bankService.save(bank);
            }
            else
            {
                bank.setModifiedBy((User) session.getAttribute("user"));
                bank.setModificationDate(Calendar.getInstance());
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage()))
                {
                    bank.setCode(CommonUtility.getNumberInEnglish(bank.getCode()));
                }
                this.bankService.edit(bank);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/bank/list";
    }

    @RequestMapping(value = "/bank/{id}", method = RequestMethod.GET)
    public String getBank(@PathVariable("id") Integer id, Model model)
    {
        model.addAttribute("bank", this.bankService.getBank(id));
//        loadDefault(model);
        return "bank";
    }

    @RequestMapping(value = "/getBankList", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getBankIoList(Model model)
    {
        List<ItemObject> branchList = this.bankService.getBankIoList();
        return branchList;
    }

    @RequestMapping(value = "bank/delete/{bankId}")
    public String deleteBank(@PathVariable("bankId") Integer bankId, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        Bank bank = this.bankService.getBank(bankId);
        bank.setModifiedBy((User) session.getAttribute("user"));
        bank.setModificationDate(Calendar.getInstance());

        boolean deleteResult = this.bankService.delete(bank);
        if (deleteResult)
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("deleteFailbank", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        return "redirect:/bank/list";
    }
}
