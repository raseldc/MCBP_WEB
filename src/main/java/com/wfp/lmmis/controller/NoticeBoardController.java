/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.model.Notice;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.service.NoticeBoardService;
import com.wfp.lmmis.utility.CommonUtility;
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
 * @author user
 */
@Controller
public class NoticeBoardController
{

    //private static final logger logger = //logger.getlogger(NoticeBoardController.class);
    @Autowired
    private NoticeBoardService noticeBoardService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/notice/list")
    public String showNoticeList(Model model, Locale locale) throws ExceptionWrapper
    {
        List<Notice> noticeList = this.noticeBoardService.getNoticeList();
        model.addAttribute("noticeList", noticeList);
        return "noticeList";
    }

    @RequestMapping(value = "/notice/create", method = RequestMethod.GET)
    public String createNotice(@ModelAttribute Notice notice, Model model)
    {
        model.addAttribute("actionType", "create");
        model.addAttribute("dateFormat", "dd-mm-yy");
        return "notice";
    }

    @RequestMapping(value = "/notice/create", method = RequestMethod.POST)
    public String createNotice(@Valid Notice notice, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        if (bindingResult.hasErrors())
        {
            return "notice";
        }
        boolean saveResult = false;
        try
        {
            System.out.println("in save - notice is = " + notice.getNoticeEn());
            Locale locale = LocaleContextHolder.getLocale();
            notice.setCreatedBy((User) session.getAttribute("user"));
            notice.setCreationDate(Calendar.getInstance());
            saveResult = this.noticeBoardService.save(notice);
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
            return "redirect:/notice/list";
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            model.addAttribute("actionType", "create");
            return "redirect:/notice/create";
        }

    }

    @RequestMapping(value = "/notice/edit/{id}", method = RequestMethod.GET)
    public String editNotice(@PathVariable("id") Integer id, @ModelAttribute Notice notice, Model model, boolean noticeLoadRequired)
    {
        try
        {
            if (!noticeLoadRequired)
            {
                model.addAttribute("dateFormat", "dd-mm-yy");
                model.addAttribute("notice", this.noticeBoardService.getNotice(id));
            }
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }

        return "notice";
    }

    @RequestMapping(value = "/notice/edit/{id}", method = RequestMethod.POST)
    public String editNotice(@PathVariable("id") Integer pk, @Valid @ModelAttribute Notice notice, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
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
                return editNotice(pk, notice, model, true);
            }
            if (notice.getId() == null)
            {
                notice.setCreatedBy((User) session.getAttribute("user"));
                notice.setCreationDate(Calendar.getInstance());
                this.noticeBoardService.save(notice);
            }
            else
            {
                notice.setModifiedBy((User) session.getAttribute("user"));
                notice.setModificationDate(Calendar.getInstance());
                this.noticeBoardService.edit(notice);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/notice/list";
    }

    @RequestMapping(value = "/notice/view/{id}", method = RequestMethod.GET)
    public String viewNotice(@PathVariable("id") Integer id, Model model)
    {
        try
        {
            model.addAttribute("notice", this.noticeBoardService.getNotice(id));
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "viewNotice";
    }
}
