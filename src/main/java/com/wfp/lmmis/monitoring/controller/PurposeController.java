/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.monitoring.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.monitoring.model.Purpose;
import com.wfp.lmmis.monitoring.service.PurposeService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.ApplicationConstants;
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
 * @author Philip
 */
@Controller
public class PurposeController
{

    //private static final logger logger = //logger.getlogger(PurposeController.class);

    @Autowired
    private PurposeService purposeService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    /**
     *
     * @param model
     * @param locale
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/purpose/list")
    public String showPurposeList(Model model, Locale locale) throws ExceptionWrapper
    {
        model.addAttribute("purposeList", this.purposeService.getPurposeList(false));
        return "purposeList";
    }

    @RequestMapping(value = "/purpose/create", method = RequestMethod.GET)
    public String createPurpose(@ModelAttribute Purpose purpose, Model model)
    {
        model.addAttribute("actionType", "create");
        return "purpose";
    }

    /**
     *
     * @param purpose
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/purpose/create", method = RequestMethod.POST)
    public String createPurpose(@Valid Purpose purpose, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createPurpose(purpose, model);
        }
        try
        {
            purpose.setCreatedBy((User) session.getAttribute("user"));
            purpose.setCreationDate(Calendar.getInstance());
            this.purposeService.save(purpose);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/purpose/list";
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "/purpose/edit/{id}", method = RequestMethod.GET)
    public String editPurpose(@PathVariable("id") Integer id, @ModelAttribute Purpose purpose, Model model, boolean purposeLoadRequired)
    {
        try
        {
            if (!purposeLoadRequired)
            {
                model.addAttribute("purpose", this.purposeService.getPurpose(id));
            }
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "purpose";
    }

    @RequestMapping(value = "/purpose/edit/{id}", method = RequestMethod.POST)
    public String editPurpose(@PathVariable("id") Integer pk, @Valid @ModelAttribute Purpose purpose, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
    {
        System.out.println("in ");
        try
        {
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach((error) ->
            {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            });
            if (bindingResult.hasErrors())
            {
                return editPurpose(pk, purpose, model, true);
            }
            purpose.setModifiedBy((User) session.getAttribute("user"));
            purpose.setModificationDate(Calendar.getInstance());
            this.purposeService.edit(purpose);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/purpose/list";
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
        }
        return "purpose";
    }

    /**
     *
     * @param id
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/purpose/delete/{id}", method = RequestMethod.POST)
    public String deletePurpose(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes)
    {
        Purpose purpose = this.purposeService.getPurpose(id);
        purpose.setModifiedBy((User) session.getAttribute("user"));
        purpose.setModificationDate(Calendar.getInstance());
        this.purposeService.delete(purpose);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/purpose/list";
    }
}
