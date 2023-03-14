/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FiscalYearController
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    private FiscalYearService fiscalYearService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/fiscalYear/list")
    public String showFiscalYearList(Model model)
    {
        System.out.println("fiscalyear list");
        model.addAttribute("fiscalYearList", this.fiscalYearService.getFiscalYearList(false, false));
        return "fiscalYearList";
    }

    /**
     *
     * @param fiscalyear
     * @param model
     * @return
     */
    @RequestMapping(value = "/fiscalYear/create", method = RequestMethod.GET)
    public String createFiscalYear(@ModelAttribute FiscalYear fiscalyear, Model model)
    {
        System.out.println("in fiscalyear create");
        FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
        model.addAttribute("activeFiscalYear", fiscalYear);
        model.addAttribute("actionType", "create");
        return "fiscalYear";
    }

    @RequestMapping(value = "/fiscalYear/create", method = RequestMethod.POST)
    public String createFiscalYear(@Valid FiscalYear fiscalyear, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "fiscalYear";
        }
        boolean saveResult = false;
        try
        {
            System.out.println("in save");
            fiscalyear.setCreatedBy((User) session.getAttribute("user"));
            fiscalyear.setCreationDate(Calendar.getInstance());
            saveResult = this.fiscalYearService.save(fiscalyear);
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

        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "redirect:/fiscalYear/list";
    }

    @RequestMapping(value = "/fiscalYear/edit/{id}", method = RequestMethod.GET)
    public String editFiscalYear(@PathVariable("id") Integer id, @ModelAttribute FiscalYear fiscalyear, Model model, boolean fiscalyearLoadRequired)
    {
        try
        {
            System.out.println("fiscalyearLoadRequired = " + fiscalyearLoadRequired);
            FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
            model.addAttribute("activeFiscalYear", fiscalYear != null ? fiscalYear : null);
            model.addAttribute("actionType", "edit");
            if (!fiscalyearLoadRequired)
            {
                System.out.println("in get");
                FiscalYear fy = this.fiscalYearService.getFiscalYear(id);
                model.addAttribute("fiscalYear", fy);
                System.out.println("start year = " + fy.getStartYear());
            }
            System.out.println("fiscalyear.getCreationDate() = " + fiscalyear.getCreationDate());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "fiscalYear";
    }

    /**
     *
     * @param pk
     * @param fiscalYear
     * @param bindingResult
     * @param session
     * @param model
     * @param redirectAttributes
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/fiscalYear/edit/{id}", method = RequestMethod.POST)
    public String editFiscalYear(@PathVariable("id") Integer pk, @Valid @ModelAttribute FiscalYear fiscalYear, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        try
        {
            if (bindingResult.hasErrors())
            {
                return editFiscalYear(pk, fiscalYear, model, true);
            }
            System.out.println("fiscal year = " + fiscalYear.getNameInEnglish());
//            fiscalYear.setNameInEnglish(fiscalYear.getStartYear() + "-" + fiscalYear.getEndYear());
            fiscalYear.setModifiedBy((User) session.getAttribute("user"));
            fiscalYear.setModificationDate(Calendar.getInstance());
            this.fiscalYearService.edit(fiscalYear);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "redirect:/fiscalYear/list";
    }

    /**
     *
     * @param fiscalyearId
     * @param session
     * @param redirectAttributes
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/fiscalYear/delete/{fiscalYearId}", method = RequestMethod.POST)
    public String deleteFiscalYear(@PathVariable("fiscalYearId") Integer fiscalyearId, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(fiscalyearId);
        fiscalYear.setModifiedBy((User) session.getAttribute("user"));
        fiscalYear.setModificationDate(Calendar.getInstance());
        this.fiscalYearService.delete(fiscalYear);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/fiscalYear/list";
    }

    /**
     *
     * @param fiscalYearName
     * @param fiscalYearId
     * @return
     */
    @RequestMapping(value = "/checkUniqueFiscalYear", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUniquePaymentCycle(String fiscalYearName, Integer fiscalYearId)
    {
        System.out.println("fiscalYearName = " + fiscalYearName);
        System.out.println("fiscalYearId = " + fiscalYearId);

        boolean result = this.fiscalYearService.checkUniqueFiscalYear(fiscalYearId, fiscalYearName);

        System.out.println("result = " + result);

        return result;
    }

    @RequestMapping(value = "/getFiscalYear", method = RequestMethod.POST)
    @ResponseBody
    public Integer getFiscalYear(Integer fiscalYearId)
    {
        System.out.println("fiscalYearId = " + fiscalYearId);

        FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(fiscalYearId);

        return fiscalYear.getStartYear();

    }
}
