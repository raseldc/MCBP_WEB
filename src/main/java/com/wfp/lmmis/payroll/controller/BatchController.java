/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.Batch;
import com.wfp.lmmis.payroll.service.BatchService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.text.DateFormatSymbols;
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
 * @author rasel
 */
@Controller
public class BatchController
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    private BatchService batchService;
    @Autowired
    private SchemeService schemeService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    public void mapGlobalVariables(Model m)
    {
        m.addAttribute("dateFormat", "yy-mm-dd");
    }
   
    @RequestMapping(value = "/batch/list")
    public String showBatchList(Model model, HttpSession session)
    {
        System.out.println("batch list");
        model.addAttribute("batchList", this.batchService.getBatchListByScheme(((UserDetail) session.getAttribute("userDetail")).getSchemeId()));
        return "batchList";
    }

//    @RequestMapping(value = "/getBatch", method = RequestMethod.GET)
//    @ResponseBody
//    public List<ItemObject> getBatchList(Model model)
//    {
//        return this.batchService.getBatchIoList();
//    }
    @RequestMapping(value = "/batch/create", method = RequestMethod.GET)
    public String createBatch(@ModelAttribute Batch batch, Model model)
    {
        mapGlobalVariables(model);
        model.addAttribute("actionType", "create");
        return "batch";
    }

    @RequestMapping(value = "/batch/create", method = RequestMethod.POST)
    public String createBatch(@Valid Batch batch, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        System.out.println("in create post = ");
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "batch";
        }
        try
        {
            System.out.println("in save");
//            batch.setNameInEnglish(batch.getStartDate().getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH) + "-"
//                    + batch.getEndDate().getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH));

            batch.setScheme(schemeService.getScheme(((UserDetail) session.getAttribute("userDetail")).getSchemeId()));
            batch.setCreatedBy((User) session.getAttribute("user"));
            batch.setCreationDate(Calendar.getInstance());
            this.batchService.save(batch);

        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/batch/list";
    }

    @RequestMapping(value = "/batch/edit/{id}", method = RequestMethod.GET)
    public String editBatch(@PathVariable("id") Integer id, @ModelAttribute Batch batch, Model model, boolean batchLoadRequired)
    {
        try
        {
            mapGlobalVariables(model);
//            mapMinistryName(model);
            CommonUtility.mapSchemeName(model);
            System.out.println("batchLoadRequired = " + batchLoadRequired);
            if (!batchLoadRequired)
            {
                model.addAttribute("batch", this.batchService.getBatch(id));
            }
            System.out.println("batch.getCreationDate() = " + batch.getCreationDate());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "batch";
    }

    /**
     *
     * @param month
     * @param locale
     * @return
     */
    public String getMonthName(int month, Locale locale)
    {
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] monthNames = symbols.getMonths();
        return monthNames[month - 1];
    }

    @RequestMapping(value = "/batch/edit/{id}", method = RequestMethod.POST)
    public String editBatch(@PathVariable("id") Integer pk, @Valid @ModelAttribute Batch batch, BindingResult bindingResult, 
            HttpSession session, Model model, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        try
        {
            List<FieldError> errors = bindingResult.getFieldErrors();
            if (bindingResult.hasErrors())
            {
                return editBatch(pk, batch, model, true);
            }
//                batch.setMinistry(ministryService.getMinistry(batch.getMinistry().getId()));
            batch.setScheme(schemeService.getScheme(((UserDetail) session.getAttribute("userDetail")).getSchemeId()));
            batch.setModifiedBy((User) session.getAttribute("user"));
            batch.setModificationDate(Calendar.getInstance());
            this.batchService.edit(batch);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/batch/list";
    }

    /**
     *
     * @param batchId
     * @param session
     * @param redirectAttributes
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "batch/delete/{batchId}")
    public String deleteBatch(@PathVariable("batchId") Integer batchId, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        Batch batch = this.batchService.getBatch(batchId);
        batch.setModifiedBy((User) session.getAttribute("user"));
        batch.setModificationDate(Calendar.getInstance());

        boolean deleteResult = this.batchService.delete(batch);
        if (deleteResult)
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("deleteFailBatch", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }

//        this.batchService.delete(batch);
//        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
//        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/batch/list";
    }

    @RequestMapping(value = "/checkUniqueBatch", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUniqueBatch(String batchName, Integer batchId, HttpSession session)
    {
        boolean result = this.batchService.checkUniqueBatch(batchId, batchName, ((UserDetail) session.getAttribute("userDetail")).getSchemeId());
        return result;
    }
//

}
