/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.model.MobileBankingProvider;
import com.wfp.lmmis.masterdata.service.MobileBankingProviderService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rasel
 */
@Controller
public class MobileBankingProviderController
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    private MobileBankingProviderService mobileBankingProviderService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/mobileBankingProvider/list")
    public String showMobileBankingProviderList(Model model)
    {
        System.out.println("mobileBankingProvider list");
        model.addAttribute("mobileBankingProviderList", this.mobileBankingProviderService.getMobileBankingProviderList(false));
        return "mobileBankingProviderList";
    }

    @RequestMapping(value = "/getMobileBankingProviderList", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getMobileBankingProviderList(Model model)
    {
        return this.mobileBankingProviderService.getMobileBankingProviderIoList(true);
    }

    @RequestMapping(value = "/mobileBankingProvider/create", method = RequestMethod.GET)
    public String createMobileBankingProvider(@ModelAttribute MobileBankingProvider mobileBankingProvider, Model model)
    {
        System.out.println("in mobileBankingProvider create");
        model.addAttribute("actionType", "create");
        return "mobileBankingProvider";
    }

    /**
     *
     * @param mobileBankingProvider
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/mobileBankingProvider/create", method = RequestMethod.POST)
    public String createMobileBankingProvider(@Valid MobileBankingProvider mobileBankingProvider, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
        {
            return "mobileBankingProvider";
        }
        try
        {
            mobileBankingProvider.setRoutingNumber(CommonUtility.getNumberInEnglish(mobileBankingProvider.getRoutingNumber()));
            mobileBankingProvider.setCode(CommonUtility.getNumberInEnglish(mobileBankingProvider.getCode()));
            mobileBankingProvider.setCreatedBy((User) session.getAttribute("user"));
            mobileBankingProvider.setCreationDate(Calendar.getInstance());
            this.mobileBankingProviderService.save(mobileBankingProvider);
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/mobileBankingProvider/list";
    }

    @RequestMapping(value = "/mobileBankingProvider/edit/{id}", method = RequestMethod.GET)
    public String editMobileBankingProvider(@PathVariable("id") Integer id, @ModelAttribute MobileBankingProvider mobileBankingProvider, Model model, boolean mobileBankingProviderLoadRequired)
    {
        Locale locale = LocaleContextHolder.getLocale();
        try
        {
            if (!mobileBankingProviderLoadRequired)
            {
                MobileBankingProvider mobileBankingProvider1 = this.mobileBankingProviderService.getMobileBankingProvider(id);
                if (locale.getLanguage().equals("bn"))
                {
                    mobileBankingProvider1.setRoutingNumber(CommonUtility.getNumberInBangla(mobileBankingProvider1.getRoutingNumber()));
                    mobileBankingProvider1.setCode(CommonUtility.getNumberInBangla(mobileBankingProvider1.getCode()));
                }
                model.addAttribute("mobileBankingProvider", mobileBankingProvider1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "mobileBankingProvider";
    }

    @RequestMapping(value = "/mobileBankingProvider/edit/{id}", method = RequestMethod.POST)
    public String editMobileBankingProvider(@PathVariable("id") Integer pk, @Valid @ModelAttribute MobileBankingProvider mobileBankingProvider, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
    {
        try
        {
            if (bindingResult.hasErrors())
            {
                return editMobileBankingProvider(pk, mobileBankingProvider, model, true);
            }
            mobileBankingProvider.setRoutingNumber(CommonUtility.getNumberInEnglish(mobileBankingProvider.getRoutingNumber()));
            mobileBankingProvider.setCode(CommonUtility.getNumberInEnglish(mobileBankingProvider.getCode()));
            mobileBankingProvider.setModifiedBy((User) session.getAttribute("user"));
            mobileBankingProvider.setModificationDate(Calendar.getInstance());
            this.mobileBankingProviderService.edit(mobileBankingProvider);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/mobileBankingProvider/list";
    }

    /**
     *
     * @param mobileBankingProviderId
     * @param session
     * @param redirectAttributes
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/mobileBankingProvider/delete/{mobileBankingProviderId}")
    public String deleteMobileBankingProvider(@PathVariable("mobileBankingProviderId") Integer mobileBankingProviderId, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        MobileBankingProvider mobileBankingProvider = this.mobileBankingProviderService.getMobileBankingProvider(mobileBankingProviderId);
        mobileBankingProvider.setModifiedBy((User) session.getAttribute("user"));
        mobileBankingProvider.setModificationDate(Calendar.getInstance());
        this.mobileBankingProviderService.delete(mobileBankingProvider);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/mobileBankingProvider/list";
    }
}
