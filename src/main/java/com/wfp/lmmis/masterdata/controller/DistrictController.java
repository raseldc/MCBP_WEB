/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.DistrictInfo;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author user
 */
@Controller
public class DistrictController
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    DistrictService districtService;
    @Autowired
    DivisionService divisionService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/district/list")
    public String showDistrictList(Model model)

    {
        List<DistrictInfo> districtList = this.districtService.getDistrictList(null);
        Locale locale = LocaleContextHolder.getLocale();
        if ("bn".equals(locale.getLanguage()))
        {
            for (DistrictInfo district : districtList)
            {
                district.setCode(CommonUtility.getNumberInBangla(district.getCode()));
            }
        }
        model.addAttribute("districtList", districtList);
        return "districtList";
    }

    @RequestMapping(value = "/district/create", method = RequestMethod.GET)
    public String createDistrict(@ModelAttribute District district, Model model)
    {
        model.addAttribute("actionType", "create");
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage()))
        {
            model.addAttribute("divisionName", "nameInEnglish");
        }
        else
        {
            model.addAttribute("divisionName", "nameInBangla");
        }
        model.addAttribute("divisionList", this.divisionService.getDivisionIoList());
        return "district";
    }

    // Sarwar
    @RequestMapping(value = "/getDistrict/{divisionId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getDistrictList(@PathVariable("divisionId") Integer divisionId, Model model)
    {
        return this.districtService.getDistrictIoList(divisionId);
    }

    /**
     *
     * @param district
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/district/create", method = RequestMethod.POST)
    public String createDistrict(@Valid District district, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "district";
        }
        boolean saveResult = false;
        try
        {
            district.setId(new Integer(district.getCode()));
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage()))
            {
                district.setCode(CommonUtility.getNumberInEnglish(district.getCode()));
            }
            district.setCreatedBy((User) session.getAttribute("user"));
            district.setCreationDate(Calendar.getInstance());
            saveResult = this.districtService.save(district);
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
            return "redirect:/district/list";
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/district/create";
        }

    }

    @RequestMapping(value = "/district/edit/{id}", method = RequestMethod.GET)
    public String editDistrict(@PathVariable("id") Integer id, @ModelAttribute District district, Model model, boolean districtLoadRequired)
    {
        try
        {
            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage()))
            {
                model.addAttribute("divisionName", "nameInEnglish");
            }
            else
            {
                model.addAttribute("divisionName", "nameInBangla");
            }
            if (!districtLoadRequired)
            {
                model.addAttribute("district", this.districtService.getDistrict(id));
                model.addAttribute("divisionList", this.divisionService.getDivisionIoList());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "district";
    }

    @RequestMapping(value = "/district/edit/{id}", method = RequestMethod.POST)
    public String editDistrict(@PathVariable("id") Integer pk, @Valid @ModelAttribute District district, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
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
                return editDistrict(pk, district, model, true);
            }
            if (district.getId() == null)
            {
                district.setCreatedBy((User) session.getAttribute("user"));
                district.setCreationDate(Calendar.getInstance());
                this.districtService.save(district);
            }
            else
            {
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage()))
                {
                    district.setCode(CommonUtility.getNumberInEnglish(district.getCode()));
                }
//                district.setId(new Integer(district.getCode()));
                district.setModifiedBy((User) session.getAttribute("user"));
                district.setModificationDate(Calendar.getInstance());
                this.districtService.edit(district);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/district/list";
    }

    @RequestMapping(value = "district/delete/{districtId}")
    public String deleteDistrict(@PathVariable("districtId") Integer districtId, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        District district = this.districtService.getDistrict(districtId);
        district.setModifiedBy((User) session.getAttribute("user"));
        district.setModificationDate(Calendar.getInstance());
        boolean deleteResult = this.districtService.delete(district);
        if (deleteResult)
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("deleteFailDistrict", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        return "redirect:/district/list";
    }
}
