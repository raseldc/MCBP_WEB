/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

@Controller
public class SchemeController
{

    @Autowired
    private SchemeService schemeService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @ModelAttribute
    public void loadDefault(Model m)
    {
        List<Scheme> schemeList = this.schemeService.getSchemeList(null, false);
        m.addAttribute("schemeList", schemeList);
        m.addAttribute("dateFormat", "dd-mm-yy");
    }

    @RequestMapping(value = "/scheme/list")
    public String showPageList(Model model)
    {
        loadDefault(model);
        return "schemeList";
    }

    @RequestMapping(value = "/scheme/create", method = RequestMethod.GET)
    public String createScheme(@ModelAttribute Scheme scheme, Model m)
    {
        m.addAttribute("actionType", "create");
        return "scheme";
    }

    @RequestMapping(value = "/getScheme/{ministryId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getSchemeList(@PathVariable("ministryId") Integer ministryId, Model model)
    {
        return this.schemeService.getSchemeIoList(ministryId);
    }

//    @RequestMapping(value = "/scheme/{id}", method = RequestMethod.GET)
//    public String getScheme(@PathVariable("id") Integer id, Model model)
//    {
//        model.addAttribute("scheme", this.schemeService.getScheme(id));
//        loadDefault(model);
//        return "scheme";
//    }
    @RequestMapping(value = "/scheme/create", method = RequestMethod.POST)
    public String saveScheme(Scheme scheme, BindingResult bindingResult, Model model,
            HttpSession session, RedirectAttributes redirectAttributes)
    {
        System.out.println("here in");
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error)
                ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createScheme(scheme, model);
        }
        User user = (User) session.getAttribute("user");
        if (scheme.getId() == null)
        {
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage()))
            {
                scheme.setCode(CommonUtility.getNumberInEnglish(scheme.getCode()));
            }
            scheme.setCreatedBy(user);
            scheme.setCreationDate(Calendar.getInstance());
            System.out.println("date 1 " + scheme.getActivationDate().getTime());
            this.schemeService.save(scheme);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        loadDefault(model);
        return "redirect:/scheme/list";
    }

    /**
     *
     * @param id
     * @param scheme
     * @param model
     * @param pageLoadRequired
     * @return
     */
    @RequestMapping(value = "/scheme/edit/{id}", method = RequestMethod.GET)
    public String editScheme(@PathVariable("id") Integer id, @ModelAttribute Scheme scheme, Model model, boolean pageLoadRequired)
    {
        try
        {
            System.out.println("pageLoadRequired = " + pageLoadRequired);
            System.out.println("load scheme " + this.schemeService.getScheme(id).getActivationDate());
            if (!pageLoadRequired)
            {
//                model.addAttribute("dateFormat", "yy-mm-dd");
                model.addAttribute("scheme", this.schemeService.getScheme(id));
            }
            model.addAttribute("actionType", "edit");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "scheme";
    }

    @RequestMapping(value = "/scheme/edit/{id}", method = RequestMethod.POST)
    public String doEditScheme(@PathVariable("id") Integer pk, @ModelAttribute Scheme scheme,
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
                return editScheme(pk, scheme, model, true);
            }

            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage()))
            {
                scheme.setCode(CommonUtility.getNumberInEnglish(scheme.getCode()));
            }
            scheme.setModifiedBy((User) session.getAttribute("user"));
            scheme.setModificationDate(Calendar.getInstance());
            this.schemeService.edit(scheme);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "redirect:/scheme/list";
    }

    @RequestMapping(value = "/scheme/delete/{id}", method = RequestMethod.POST)
    public String deleteScheme(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes, Model model)
    {
        Scheme scheme = this.schemeService.getScheme(id);
        scheme.setModifiedBy((User) session.getAttribute("user"));
        scheme.setModificationDate(Calendar.getInstance());
        this.schemeService.delete(scheme);
        loadDefault(model);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/scheme/list";
    }
}
