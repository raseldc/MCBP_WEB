/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.enums.AttributeType;
import com.wfp.lmmis.enums.ComparisonType;
import com.wfp.lmmis.masterdata.editor.SchemeEditor;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import com.wfp.lmmis.masterdata.service.SchemeAttributeService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.types.OrderingType;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.util.Arrays;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SchemeAttributeController
{

    @Autowired
    private SchemeAttributeService schemeAttributeService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private SchemeEditor schemeEditor;
    Localizer localizer = Localizer.getBrowserLocalizer();

    public void initBinder(WebDataBinder binder)
    {
        binder.registerCustomEditor(Scheme.class, this.schemeEditor);
    }

    @ModelAttribute
    public void loadDefault(Model m)
    {
        List<SchemeAttribute> schemeAttributeList = this.schemeAttributeService.getSchemeAttributeListByCode(4715);
        Locale locale = LocaleContextHolder.getLocale();
        if ("bn".equals(locale.getLanguage()))
        {
            for (SchemeAttribute schemeAttribute : schemeAttributeList)
            {
                if ("Yes".equals(schemeAttribute.getComparedValue()))
                {
                    schemeAttribute.setComparedValue("হ্যাঁ");
                }
                else if ("No".equals(schemeAttribute.getComparedValue()))
                {
                    schemeAttribute.setComparedValue("না");
                }
                else
                {
                    schemeAttribute.setComparedValue(CommonUtility.getNumberInBangla(schemeAttribute.getComparedValue()));
                }
            }

        }
        m.addAttribute("schemeAttributeList", schemeAttributeList);
        List<Scheme> schemeList = this.schemeService.getSchemeList(null, true);
        m.addAttribute("schemeList", schemeList);
        if ("en".equals(locale.getLanguage()))
        {
            m.addAttribute("schemeName", "nameInEnglish");
        }
        else
        {
            m.addAttribute("schemeName", "nameInBangla");
        }
    }

    @RequestMapping(value = "/schemeAttribute/list")
    public String showPageList(Model model)
    {
        loadDefault(model);
        return "schemeAttributeList";
    }

    /**
     *
     * @param schemeAttribute
     * @param m
     * @return
     */
    @RequestMapping(value = "/schemeAttribute/create", method = RequestMethod.GET)
    public String createSchemeAttribute(@ModelAttribute SchemeAttribute schemeAttribute, Model m)
    {
        m.addAttribute("actionType", "create");
        List<OrderingType> orderingTypeList = Arrays.asList(OrderingType.values());
        m.addAttribute("orderingTypeList", orderingTypeList);
        List<AttributeType> attributeTypeList = Arrays.asList(AttributeType.values());
        m.addAttribute("attributeTypeList", attributeTypeList);
        List<ComparisonType> comparisonTypeList = Arrays.asList(ComparisonType.values());
        m.addAttribute("comparisonTypeList", comparisonTypeList);
        return "schemeAttribute";
    }

    @RequestMapping(value = "/schemeAttribute/create", method = RequestMethod.POST)
    public String saveSchemeAttribute(@ModelAttribute("schemeAttribute") SchemeAttribute schemeAttribute, BindingResult bindingResult, Model model,
            HttpSession session, RedirectAttributes redirectAttributes)
    {
        System.out.println("here in");
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createSchemeAttribute(schemeAttribute, model);
        }
        User user = (User) session.getAttribute("user");
        if (schemeAttribute.getId() == null)
        {
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage()))
            {
                schemeAttribute.setViewOrder(Integer.parseInt(CommonUtility.getNumberInEnglish(schemeAttribute.getViewOrder().toString())));
                schemeAttribute.setSelectionCriteriaPriority(Integer.parseInt(CommonUtility.getNumberInEnglish(schemeAttribute.getSelectionCriteriaPriority().toString())));
            }
            schemeAttribute.setCreatedBy(user);
            schemeAttribute.setCreationDate(Calendar.getInstance());
            this.schemeAttributeService.save(schemeAttribute);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        loadDefault(model);
        return "redirect:/schemeAttribute/list";
    }

    @RequestMapping(value = "/schemeAttribute/edit/{id}", method = RequestMethod.GET)
    public String editSchemeAttribute(@PathVariable("id") Integer id, @ModelAttribute SchemeAttribute schemeAttribute, Model model, boolean pageLoadRequired)
    {
        try
        {
            System.out.println("pageLoadRequired = " + pageLoadRequired);
            model.addAttribute("actionType", "create");
            List<OrderingType> orderingTypeList = Arrays.asList(OrderingType.values());
            model.addAttribute("orderingTypeList", orderingTypeList);
            List<AttributeType> attributeTypeList = Arrays.asList(AttributeType.values());
            model.addAttribute("attributeTypeList", attributeTypeList);
            List<ComparisonType> comparisonTypeList = Arrays.asList(ComparisonType.values());
            model.addAttribute("comparisonTypeList", comparisonTypeList);
            if (!pageLoadRequired)
            {
                model.addAttribute("schemeAttribute", this.schemeAttributeService.getSchemeAttribute(id));
            }
            model.addAttribute("actionType", "edit");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "schemeAttribute";
    }

    @RequestMapping(value = "/schemeAttribute/edit/{id}", method = RequestMethod.POST)
    public String doEditSchemeAttribute(@PathVariable("id") Integer pk, @Valid @ModelAttribute SchemeAttribute schemeAttribute,
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
                return editSchemeAttribute(pk, schemeAttribute, model, true);
            }
            schemeAttribute.setModifiedBy((User) session.getAttribute("user"));
            schemeAttribute.setModificationDate(Calendar.getInstance());
            this.schemeAttributeService.edit(schemeAttribute);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "redirect:/schemeAttribute/list";
    }

    @RequestMapping(value = "/schemeAttribute/delete/{id}", method = RequestMethod.POST)
    public String deleteScheme(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes, Model model)
    {
        SchemeAttribute schemeAttribute = this.schemeAttributeService.getSchemeAttribute(id);
        schemeAttribute.setModifiedBy((User) session.getAttribute("user"));
        schemeAttribute.setModificationDate(Calendar.getInstance());
        this.schemeAttributeService.delete(schemeAttribute);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        loadDefault(model);
        return "redirect:/schemeAttribute/list";
    }
}
