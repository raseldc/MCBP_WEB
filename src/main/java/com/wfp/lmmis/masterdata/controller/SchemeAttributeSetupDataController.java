/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.editor.SchemeAttributeEditor;
import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import com.wfp.lmmis.masterdata.model.SchemeAttributeSetupData;
import com.wfp.lmmis.masterdata.service.SchemeAttributeService;
import com.wfp.lmmis.masterdata.service.SchemeAttributeSetupDataService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.ApplicationConstants;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
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

@Controller
public class SchemeAttributeSetupDataController
{

    @Autowired
    private SchemeAttributeSetupDataService schemeAttributeSetupDataService;
    @Autowired
    private SchemeAttributeService schemeAttributeService;
    @Autowired
    private SchemeAttributeEditor schemeAttributeEditor;

    @ModelAttribute
    public void loadDefault(Model m)
    {
        List<SchemeAttributeSetupData> schemeAttributeSetupDataList = this.schemeAttributeSetupDataService.getSchemeAttributeSetupDataList(null);
        m.addAttribute("schemeAttributeSetupDataList", schemeAttributeSetupDataList);
        List<SchemeAttribute> schemeAttributeList = this.schemeAttributeService.getSchemeAttributeDDList();
        m.addAttribute("schemeAttributeList", schemeAttributeList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage()))
        {
            m.addAttribute("schemeAttributeName", "nameInEnglish");
        }
        else
        {
            m.addAttribute("schemeAttributeName", "nameInBangla");
        }
    }

    @RequestMapping(value = "/schemeAttributeSetupData/list")
    public String showPageList(Model model)
    {
        loadDefault(model);
        return "schemeAttributeSetupDataList";
    }

    /**
     *
     * @param schemeAttributeSetupData
     * @param m
     * @return
     */
    @RequestMapping(value = "/schemeAttributeSetupData/create", method = RequestMethod.GET)
    public String createSchemeAttributeSetupData(@ModelAttribute SchemeAttributeSetupData schemeAttributeSetupData, Model m)
    {
        m.addAttribute("actionType", "create");
        return "schemeAttributeSetupData";
    }

    /**
     *
     * @param schemeAttributeSetupData
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/schemeAttributeSetupData/create", method = RequestMethod.POST)
    public String saveSchemeAttributeSetupData(@ModelAttribute("schemeAttributeSetupData") SchemeAttributeSetupData schemeAttributeSetupData, BindingResult bindingResult, Model model,
            HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createSchemeAttributeSetupData(schemeAttributeSetupData, model);
        }
        User user = (User) session.getAttribute("user");
        if (schemeAttributeSetupData.getId() == null)
        {
            schemeAttributeSetupData.setCreatedBy(user);
            schemeAttributeSetupData.setCreationDate(Calendar.getInstance());
            this.schemeAttributeSetupDataService.save(schemeAttributeSetupData);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, ApplicationConstants.CREATE_MESSAGE);
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        loadDefault(model);
        return "redirect:/schemeAttributeSetupData/list";
    }

    @RequestMapping(value = "/schemeAttributeSetupData/edit/{id}", method = RequestMethod.GET)
    public String editSchemeAttributeSetupData(@PathVariable("id") Integer id, @ModelAttribute SchemeAttributeSetupData schemeAttributeSetupData, Model model, boolean pageLoadRequired)
    {
        try
        {
            System.out.println("pageLoadRequired = " + pageLoadRequired);
            if (!pageLoadRequired)
            {
                model.addAttribute("schemeAttributeSetupData", this.schemeAttributeSetupDataService.getSchemeAttributeSetupData(id));
            }
            model.addAttribute("actionType", "edit");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "schemeAttributeSetupData";
    }

    @RequestMapping(value = "/schemeAttributeSetupData/edit/{id}", method = RequestMethod.POST)
    public String doEditSchemeAttributeSetupData(@PathVariable("id") Integer pk,
            @Valid @ModelAttribute SchemeAttributeSetupData schemeAttributeSetupData,
            BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request)
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
                return editSchemeAttributeSetupData(pk, schemeAttributeSetupData, model, true);
            }
            schemeAttributeSetupData.setModifiedBy((User) session.getAttribute("user"));
            schemeAttributeSetupData.setModificationDate(Calendar.getInstance());
            this.schemeAttributeSetupDataService.edit(schemeAttributeSetupData);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, ApplicationConstants.EDIT_MESSAGE);
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "redirect:/schemeAttributeSetupData/list";
    }

    @RequestMapping(value = "/schemeAttributeSetupData/delete/{id}", method = RequestMethod.POST)
    public String deleteScheme(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model)
    {
        System.out.println("ok delete");
        SchemeAttributeSetupData attributeSetupData = this.schemeAttributeSetupDataService.getSchemeAttributeSetupData(id);
        this.schemeAttributeSetupDataService.delete(id);
        model.addAttribute("schemeAttribute", this.schemeAttributeService.getSchemeAttribute(attributeSetupData.getSchemeAttribute().getId()));
        model.addAttribute("schemeAttributeSetupDataList", this.schemeAttributeSetupDataService.getSchemeAttributeSetupDataList(attributeSetupData.getSchemeAttribute().getId()));
        model.addAttribute("schemeAttributeSetupData", new SchemeAttributeSetupData());
        model.addAttribute("actionType", "create");

        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, ApplicationConstants.DELETE_MESSAGE);
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        model.addAttribute("message", controllerMessage);
        return "schemeAttributeSetup";
    }

    @RequestMapping(value = "/schemeAttributeSetupData/attribute/edit/{schemeAttributeId}", method = RequestMethod.GET)
    public String editSchemeAttribute(@PathVariable("schemeAttributeId") Integer schemeAttributeId,
            @ModelAttribute SchemeAttributeSetupData schemeAttributeSetupData, Model model, boolean pageLoadRequired)
    {
        try
        {
            model.addAttribute("schemeAttribute", this.schemeAttributeService.getSchemeAttribute(schemeAttributeId));
            model.addAttribute("schemeAttributeSetupDataList", this.schemeAttributeSetupDataService.getSchemeAttributeSetupDataList(schemeAttributeId));
            model.addAttribute("actionType", "create");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "schemeAttributeSetup";
    }

    @RequestMapping(value = "/schemeAttributeSetupData/create/{schemeAttributeid}", method = RequestMethod.POST)
    public String saveSchemeAttributeSetup(@PathVariable("schemeAttributeid") Integer schemeAttributeid, @ModelAttribute("schemeAttributeSetupData") SchemeAttributeSetupData schemeAttributeSetupData, BindingResult bindingResult, Model model,
            HttpSession session, RedirectAttributes redirectAttributes)
    {
        System.out.println("here in new");
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createSchemeAttributeSetupData(schemeAttributeSetupData, model);
        }
        User user = (User) session.getAttribute("user");
        if (schemeAttributeSetupData.getId() == null)
        {
            schemeAttributeSetupData.setSchemeAttribute(schemeAttributeService.getSchemeAttribute(schemeAttributeid));
            schemeAttributeSetupData.setCreatedBy(user);
            schemeAttributeSetupData.setCreationDate(Calendar.getInstance());
            this.schemeAttributeSetupDataService.save(schemeAttributeSetupData);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, ApplicationConstants.CREATE_MESSAGE);
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        model.addAttribute("schemeAttribute", this.schemeAttributeService.getSchemeAttribute(schemeAttributeid));
        model.addAttribute("schemeAttributeSetupDataList", this.schemeAttributeSetupDataService.getSchemeAttributeSetupDataList(schemeAttributeid));
        model.addAttribute("actionType", "edit");
        return "schemeAttributeSetup";
    }

    @RequestMapping(value = "/schemeAttributeSetupData/attribute/edit/{schemeAttributeId}/{sasdId}", method = RequestMethod.GET)
    public String editSchemeAttributeSetup(@PathVariable("schemeAttributeId") Integer schemeAttributeId, @PathVariable("sasdId") Integer sasId,
            @ModelAttribute SchemeAttributeSetupData schemeAttributeSetupData, Model model, boolean pageLoadRequired)
    {
        try
        {
            model.addAttribute("schemeAttribute", this.schemeAttributeService.getSchemeAttribute(schemeAttributeId));
            model.addAttribute("schemeAttributeSetupData", this.schemeAttributeSetupDataService.getSchemeAttributeSetupData(sasId));
            model.addAttribute("schemeAttributeSetupDataList", this.schemeAttributeSetupDataService.getSchemeAttributeSetupDataList(schemeAttributeId));
            model.addAttribute("actionType", "edit");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "schemeAttributeSetup";
    }

    @RequestMapping(value = "/schemeAttributeSetupData/edit/{schemeAttributeId}/{sasdId}", method = RequestMethod.POST)
    public String doEditSchemeAttributeSetup(@PathVariable("schemeAttributeId") Integer schemeAttributeId,
            @PathVariable("sasdId") Integer sasId,
            @Valid @ModelAttribute SchemeAttributeSetupData schemeAttributeSetupData,
            BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request)
    {
        try
        {
            schemeAttributeSetupData.setId(sasId);
            schemeAttributeSetupData.setSchemeAttribute(schemeAttributeService.getSchemeAttribute(schemeAttributeId));
            schemeAttributeSetupData.setModifiedBy((User) session.getAttribute("user"));
            schemeAttributeSetupData.setModificationDate(Calendar.getInstance());
            this.schemeAttributeSetupDataService.edit(schemeAttributeSetupData);
            model.addAttribute("schemeAttribute", this.schemeAttributeService.getSchemeAttribute(schemeAttributeId));
            model.addAttribute("schemeAttributeSetupDataList", this.schemeAttributeSetupDataService.getSchemeAttributeSetupDataList(schemeAttributeId));
            model.addAttribute("actionType", "edit");

            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, ApplicationConstants.EDIT_MESSAGE);
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            model.addAttribute("message", controllerMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "schemeAttributeSetup";
    }

}
