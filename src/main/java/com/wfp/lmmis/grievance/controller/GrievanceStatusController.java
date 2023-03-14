/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.model.GrievanceStatus;
import com.wfp.lmmis.grievance.service.GrievanceStatusService;
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
public class GrievanceStatusController
{

    //private static final logger logger = //logger.getlogger(GrievanceStatusController.class);

    @Autowired
    private GrievanceStatusService grievanceStatusService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/grievanceStatus/list")
    public String showGrievanceStatusList(Model model, Locale locale) throws ExceptionWrapper
    {
        model.addAttribute("grievanceStatusList", this.grievanceStatusService.getGrievanceStatusList());
        return "grievanceStatusList";
    }

    @RequestMapping(value = "/grievanceStatus/create", method = RequestMethod.GET)
    public String createGrievanceStatus(@ModelAttribute GrievanceStatus grievanceStatus, Model model)
    {
        model.addAttribute("actionType", "create");
        return "grievanceStatus";
    }

    @RequestMapping(value = "/grievanceStatus/create", method = RequestMethod.POST)
    public String createGrievanceStatus(@Valid GrievanceStatus grievanceStatus, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createGrievanceStatus(grievanceStatus, model);
        }
        try
        {
            grievanceStatus.setCreatedBy((User) session.getAttribute("user"));
            grievanceStatus.setCreationDate(Calendar.getInstance());
            this.grievanceStatusService.save(grievanceStatus);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/grievanceStatus/list";
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "/grievanceStatus/edit/{id}", method = RequestMethod.GET)
    public String editGrievanceStatus(@PathVariable("id") Integer id, @ModelAttribute GrievanceStatus grievanceStatus, Model model, boolean grievanceStatusLoadRequired)
    {
        try
        {
            if (!grievanceStatusLoadRequired)
            {
                model.addAttribute("grievanceStatus", this.grievanceStatusService.getGrievanceStatus(id));
            }
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "grievanceStatus";
    }

    @RequestMapping(value = "/grievanceStatus/edit/{pk}", method = RequestMethod.POST)
    public String editGrievanceStatus(@PathVariable Integer pk, @Valid @ModelAttribute GrievanceStatus grievanceStatus, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {

        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return editGrievanceStatus(pk, grievanceStatus, model, true);
        }
        grievanceStatus.setModifiedBy((User) session.getAttribute("user"));
        grievanceStatus.setModificationDate(Calendar.getInstance());
        this.grievanceStatusService.edit(grievanceStatus);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/grievanceStatus/list";

    }

    @RequestMapping(value = "/grievanceStatus/delete/{id}", method = RequestMethod.POST)
    public String deleteGrievanceStatus(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        GrievanceStatus grievanceStatus = this.grievanceStatusService.getGrievanceStatus(id);
        grievanceStatus.setModifiedBy((User) session.getAttribute("user"));
        grievanceStatus.setModificationDate(Calendar.getInstance());
        this.grievanceStatusService.delete(grievanceStatus);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/grievanceStatus/list";
    }
}
