/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.forms.ApplicationDeadlineForm;
import com.wfp.lmmis.masterdata.model.ApplicationDeadline;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.ApplicationDeadlineService;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Philip
 */
@Controller
public class ApplicationDeadlineController
{

    @Autowired
    private UpazillaService upazillaService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private FiscalYearService fiscalYearService;

    @Autowired
    private ApplicationDeadlineService applicationDeadlineService;

    @Autowired
    private DistrictService districtService;
//    @Autowired
//    private CalendarEditor calendarEditor;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // binder.registerCustomEditor(Calendar.class, this.calendarEditor);
    }

    /**
     *
     * @param m
     */
    public void mapGlobalVariables(Model m)
    {
        m.addAttribute("dateFormat", "dd-mm-yy");
    }

    @RequestMapping(value = "/applicationDeadline/search", method = RequestMethod.GET)
    public String getApplicationDeadline(@ModelAttribute ApplicationDeadlineForm applicationDeadlineForm, Model model, HttpSession session)
    {
        mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("actionType", "search");
        User user = (User) session.getAttribute("user");
        if (user.getDistrict() != null)
        {
            applicationDeadlineForm.setDistrict(user.getDistrict());
        }
        if (user.getDivision() != null)
        {
            applicationDeadlineForm.setDivision(user.getDivision());
        }
        if (user.getUpazilla() != null)
        {
            applicationDeadlineForm.setUpazilla(user.getUpazilla());
        }
        return "applicationDeadline";
    }

    @RequestMapping(value = "/applicationDeadline/search", method = RequestMethod.POST)
    public String loadApplicationDeadline(ApplicationDeadlineForm applicationDeadlineForm,
            BindingResult bindingResult, Model model, HttpSession session)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error) ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });

        if (bindingResult.hasErrors())
        {
            return getApplicationDeadline(applicationDeadlineForm, model, session);
        }
        //Following commented code was for setting deadline for bulk upazila under district
//        Integer districtId = applicationDeadlineForm.getDistrict().getId();
//        List<Upazilla> upazilaList = upazillaService.getUpazillaList(districtId);
//        List<ApplicationDeadline> applicationDeadlines = new ArrayList<>();
//        for (Upazilla upazila : upazilaList)
//        {
//            ApplicationDeadline applicationDeadline = new ApplicationDeadline();
//            applicationDeadline.setUpazila(upazila);
//            Calendar deadline = this.applicationDeadlineService.getApplicationDeadlineByUpazila(upazila.getId(), applicationDeadlineForm.getFiscalYear().getId(), applicationDeadlineForm.getScheme().getId());
//            applicationDeadline.setDeadline(deadline);
//            applicationDeadlines.add(applicationDeadline);
//        }
        List<ApplicationDeadline> applicationDeadlines = new ArrayList<>();
        Upazilla upazilla = upazillaService.getUpazilla(applicationDeadlineForm.getUpazilla().getId());
        ApplicationDeadline applicationDeadline = new ApplicationDeadline();
        applicationDeadline.setUpazila(upazilla);
        Calendar deadline = this.applicationDeadlineService.getApplicationDeadlineByUpazila(upazilla.getId(), applicationDeadlineForm.getFiscalYear().getId(), applicationDeadlineForm.getScheme().getId());
        applicationDeadline.setDeadline(deadline);
        applicationDeadlines.add(applicationDeadline);

        applicationDeadlineForm.setApplicationDeadlineList(applicationDeadlines);
//        These line for showing names
        Scheme scheme = this.schemeService.getScheme(applicationDeadlineForm.getScheme().getId());
        FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(applicationDeadlineForm.getFiscalYear().getId());
        District district = this.districtService.getDistrict(applicationDeadlineForm.getDistrict().getId());
        applicationDeadlineForm.setScheme(scheme);
        applicationDeadlineForm.setFiscalYear(fiscalYear);
        applicationDeadlineForm.setDistrict(district);
//        end                
        model.addAttribute("actionType", "create");
        return "applicationDeadline";
    }

    /**
     *
     * @param applicationDeadlineForm
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/applicationDeadline/save", method = RequestMethod.POST)
    public String saveApplicationDeadline(@ModelAttribute ApplicationDeadlineForm applicationDeadlineForm, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        this.applicationDeadlineService.saveApplicationDeadline(applicationDeadlineForm, session);
        model.addAttribute("actionType", "search");
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/applicationDeadline/search";
    }
}
