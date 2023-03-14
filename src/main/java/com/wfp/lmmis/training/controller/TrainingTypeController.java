/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.training.model.TrainingType;
import com.wfp.lmmis.training.service.TrainingTypeService;
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
public class TrainingTypeController
{

    //private static final logger logger = //logger.getlogger(TrainingTypeController.class);

    @Autowired
    private TrainingTypeService trainingTypeService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/trainingType/list")
    public String showTrainingTypeList(Model model, Locale locale) throws ExceptionWrapper
    {
        model.addAttribute("trainingTypeList", this.trainingTypeService.getTrainingTypeList(false));
        return "trainingTypeList";
    }

    @RequestMapping(value = "/trainingType/create", method = RequestMethod.GET)
    public String createTrainingType(@ModelAttribute TrainingType trainingType, Model model)
    {
        model.addAttribute("actionType", "create");
        return "trainingType";
    }

    @RequestMapping(value = "/trainingType/create", method = RequestMethod.POST)
    public String createTrainingType(@Valid TrainingType trainingType, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createTrainingType(trainingType, model);
        }
        try
        {
            trainingType.setCreatedBy((User) session.getAttribute("user"));
            trainingType.setCreationDate(Calendar.getInstance());
            this.trainingTypeService.save(trainingType);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/trainingType/list";
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "/trainingType/edit/{id}", method = RequestMethod.GET)
    public String editTrainingType(@PathVariable("id") Integer id, @ModelAttribute TrainingType trainingType, Model model, boolean trainingTypeLoadRequired)
    {
        try
        {
            if (!trainingTypeLoadRequired)
            {
                model.addAttribute("trainingType", this.trainingTypeService.getTrainingType(id));
            }
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "trainingType";
    }

    @RequestMapping(value = "/trainingType/edit/{id}", method = RequestMethod.POST)
    public String editTrainingType(@PathVariable("id") Integer pk, @Valid @ModelAttribute TrainingType trainingType, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
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
                return editTrainingType(pk, trainingType, model, true);
            }
            trainingType.setModifiedBy((User) session.getAttribute("user"));
            trainingType.setModificationDate(Calendar.getInstance());
            this.trainingTypeService.edit(trainingType);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/trainingType/list";
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
        }
        return "trainingType";
    }

    @RequestMapping(value = "/trainingType/delete/{id}", method = RequestMethod.POST)
    public String deleteTrainingType(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes)
    {
        TrainingType trainingType = this.trainingTypeService.getTrainingType(id);
        trainingType.setModifiedBy((User) session.getAttribute("user"));
        trainingType.setModificationDate(Calendar.getInstance());
        this.trainingTypeService.delete(trainingType);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/trainingType/list";
    }
}
