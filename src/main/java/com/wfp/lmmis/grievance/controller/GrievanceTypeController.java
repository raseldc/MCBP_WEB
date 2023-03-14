/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.model.GrievanceType;
import com.wfp.lmmis.grievance.service.GrievanceTypeService;
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
public class GrievanceTypeController
{

    //private static final logger logger = //logger.getlogger(GrievanceTypeController.class);

    @Autowired
    private GrievanceTypeService grievanceTypeService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/grievanceType/list")
    public String showGrievanceTypeList(Model model, Locale locale) throws ExceptionWrapper
    {
        model.addAttribute("grievanceTypeList", this.grievanceTypeService.getGrievanceTypeList());
        return "grievanceTypeList";
    }

    @RequestMapping(value = "/grievanceType/create", method = RequestMethod.GET)
    public String createGrievanceType(@ModelAttribute GrievanceType grievanceType, Model model)
    {
        model.addAttribute("actionType", "create");
        return "grievanceType";
    }

    @RequestMapping(value = "/grievanceType/create", method = RequestMethod.POST)
    public String createGrievanceType(@Valid GrievanceType grievanceType, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createGrievanceType(grievanceType, model);
        }
        try
        {
            grievanceType.setCreatedBy((User) session.getAttribute("user"));
            grievanceType.setCreationDate(Calendar.getInstance());
            this.grievanceTypeService.save(grievanceType);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/grievanceType/list";
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    /**
     *
     * @param id
     * @param grievanceType
     * @param model
     * @param grievanceTypeLoadRequired
     * @return
     */
    @RequestMapping(value = "/grievanceType/edit/{id}", method = RequestMethod.GET)
    public String editGrievanceType(@PathVariable("id") Integer id, @ModelAttribute GrievanceType grievanceType, Model model, boolean grievanceTypeLoadRequired)
    {
        try
        {
            if (!grievanceTypeLoadRequired)
            {
                model.addAttribute("grievanceType", this.grievanceTypeService.getGrievanceType(id));
            }
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "grievanceType";
    }

    @RequestMapping(value = "/grievanceType/edit/{pk}", method = RequestMethod.POST)
    public String editGrievanceType(@PathVariable Integer pk, @Valid @ModelAttribute GrievanceType grievanceType, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {

        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return editGrievanceType(pk, grievanceType, model, true);
        }
        grievanceType.setModifiedBy((User) session.getAttribute("user"));
        grievanceType.setModificationDate(Calendar.getInstance());
        this.grievanceTypeService.edit(grievanceType);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/grievanceType/list";

    }

    /**
     *
     * @param id
     * @param session
     * @param redirectAttributes
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/grievanceType/delete/{id}", method = RequestMethod.POST)
    public String deleteGrievanceType(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        GrievanceType grievanceType = this.grievanceTypeService.getGrievanceType(id);
        grievanceType.setModifiedBy((User) session.getAttribute("user"));
        grievanceType.setModificationDate(Calendar.getInstance());
        this.grievanceTypeService.delete(grievanceType);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/grievanceType/list";
    }
}
