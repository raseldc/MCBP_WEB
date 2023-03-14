/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.ApplicationConstants;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DivisionController
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    private DivisionService divisionService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/division/list")
    public String showDivisionList(Model model)
    {
        System.out.println("division list");
        List<Division> divisionList = this.divisionService.getDivisionList();
        Locale locale = LocaleContextHolder.getLocale();
        if ("bn".equals(locale.getLanguage()))
        {
            for (Division division : divisionList)
            {
                division.setCode(CommonUtility.getNumberInBangla(division.getCode()));
            }
        }

        model.addAttribute("divisionList", divisionList);
        return "divisionList";
    }

    @RequestMapping(value = "/getDivision", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getDivisionList(Model model)
    {
        return this.divisionService.getDivisionIoList();
    }

    /**
     *
     * @param division
     * @param model
     * @return
     */
    @RequestMapping(value = "/division/create", method = RequestMethod.GET)
    public String createDivision(@ModelAttribute Division division, Model model)
    {
        System.out.println("in division create");
        model.addAttribute("actionType", "create");
        return "division";
    }

    @RequestMapping(value = "/division/create", method = RequestMethod.POST)
    public String createDivision(@Valid Division division, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "division";
        }
        boolean saveResult = false;
        try
        {
            System.out.println("in save");
            division.setId(new Integer(division.getCode()));
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage()))
            {
                division.setCode(CommonUtility.getNumberInEnglish(division.getCode()));
            }
            division.setCreatedBy((User) session.getAttribute("user"));
            division.setCreationDate(Calendar.getInstance());
            saveResult = this.divisionService.save(division);
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
            return "redirect:/division/list";
        }
        else{
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            model.addAttribute("actionType", "create");
            return "redirect:/division/create";
        }
        
    }

    @RequestMapping(value = "/division/edit/{id}", method = RequestMethod.GET)
    public String editDivision(@PathVariable("id") Integer id, @ModelAttribute Division division, Model model, boolean divisionLoadRequired)
    {
        try
        {
            System.out.println("divisionLoadRequired = " + divisionLoadRequired);
            if (!divisionLoadRequired)
            {
                model.addAttribute("division", this.divisionService.getDivision(id));
            }
            System.out.println("division.getCreationDate() = " + division.getCreationDate());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "division";
    }

    /**
     *
     * @param pk
     * @param division
     * @param bindingResult
     * @param session
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/division/edit/{id}", method = RequestMethod.POST)
    public String editDivision(@PathVariable("id") Integer pk, @Valid @ModelAttribute Division division, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
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
                return editDivision(pk, division, model, true);
            }
            if (division.getId() == null)
            {
                division.setCreatedBy((User) session.getAttribute("user"));
                division.setCreationDate(Calendar.getInstance());
                this.divisionService.save(division);
            }
            else
            {
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage()))
                {
                    division.setCode(CommonUtility.getNumberInEnglish(division.getCode()));
                }
                System.out.println("division.creationDate = " + division.getCreationDate());
//                division.setId(new Integer(division.getCode()));
                division.setModifiedBy((User) session.getAttribute("user"));
                division.setModificationDate(Calendar.getInstance());
                this.divisionService.edit(division);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/division/list";
    }

    @RequestMapping(value = "division/delete/{divisionId}")
    public String deleteDivision(@PathVariable("divisionId") Integer divisionId, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        System.out.println("in division delete");
        Division division = this.divisionService.getDivision(divisionId);
        division.setModifiedBy((User) session.getAttribute("user"));
        division.setModificationDate(Calendar.getInstance());
        boolean deleteResult = this.divisionService.delete(division);
        if (deleteResult)
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("deleteFailDivision", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }

        return "redirect:/division/list";
    }
}
