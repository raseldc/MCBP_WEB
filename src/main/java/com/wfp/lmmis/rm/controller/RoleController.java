/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.rm.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.model.Role;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.service.RoleService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Philip
 */
@Controller
public class RoleController
{

    //private static final logger logger = //logger.getlogger(RoleController.class);

    @Autowired
    private RoleService roleService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    /**
     *
     * @param model
     * @param locale
     * @param session
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/role/list")
    public String showRoleList(Model model, Locale locale, HttpSession session) throws ExceptionWrapper
    {
//        Integer schemeId = ((UserDetail) session.getAttribute("userDetail")).getScheme().getId();
        model.addAttribute("roleList", this.roleService.getRoleList(null));
        return "roleList";
    }

    @RequestMapping(value = "/role/create", method = RequestMethod.GET)
    public String createRole(@ModelAttribute Role role, Model model)
    {
        model.addAttribute("actionType", "create");
        return "role";
    }

    @RequestMapping(value = "/role/create", method = RequestMethod.POST)
    public String createRole(@Valid Role role, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createRole(role, model);
        }
        try
        {
            role.setCreatedBy((User) session.getAttribute("user"));
            role.setCreationDate(Calendar.getInstance());
            this.roleService.Save(role);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/role/list";
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
     * @param role
     * @param model
     * @param roleLoadRequired
     * @return
     */
    @RequestMapping(value = "/role/edit/{id}", method = RequestMethod.GET)
    public String editRole(@PathVariable("id") Integer id, @ModelAttribute Role role, Model model, boolean roleLoadRequired)
    {
        try
        {
            if (!roleLoadRequired)
            {
                model.addAttribute("role", this.roleService.getRole(id));
            }
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "role";
    }

    @RequestMapping(value = "/role/edit/{pk}", method = RequestMethod.POST)
    public String editRole(@PathVariable Integer pk, @Valid @ModelAttribute Role role, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        try
        {
            if (bindingResult.hasErrors())
            {
                return editRole(pk, role, model, true);
            }
            role.setModifiedBy((User) session.getAttribute("user"));
            role.setModificationDate(Calendar.getInstance());
            this.roleService.Edit(role);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/role/list";
        }
        catch (ExceptionWrapper e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "/role/delete/{id}", method = RequestMethod.POST)
    public String deleteRole(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        Role role = this.roleService.getRole(id);
        role.setModifiedBy((User) session.getAttribute("user"));
        role.setModificationDate(Calendar.getInstance());
        this.roleService.delete(role);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/role/list";
    }

    /**
     *
     * @param schemeId
     * @param model
     * @return
     */
    @RequestMapping(value = "/getRoleByScheme/{schemeId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getRoleListByScheme(@PathVariable("schemeId") Integer schemeId, Model model)
    {
        return this.roleService.getRoleByScheme(schemeId);
    }

}
