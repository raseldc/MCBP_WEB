/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dashboard.controller;

import com.wfp.lmmis.dashboard.service.DashboardService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.utility.Localizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Philip
 */
@Controller
public class DashboardController {

    @Autowired
    private FiscalYearService fiscalYearService;

    @Autowired
    private DashboardService dashboardService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/dashboard-old")
    public String loadDashboard(Model model, HttpSession session) {
        FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
//        mapFiscalYear(model, fiscalYear);
        UserDetail user = (UserDetail) session.getAttribute("userDetail");
        System.out.println("user.getSchemeId() = " + user.getSchemeId());
//        Map countData = this.dashboardService.getDashBoardData(new Integer("1"), fiscalYear.getId(), user);
        Map countData = this.dashboardService.getDashBoardData(null, fiscalYear.getId(), user);
        model.addAttribute("countData", countData);
        return "dashboard";
    }

    /**
     *
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = {"/", "/dashboard"})
    public String loadDashboardUpdate(Model model, HttpSession session) {
        FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
//        mapFiscalYear(model, fiscalYear);
        try {
            UserDetail user = (UserDetail) session.getAttribute("userDetail");

            System.out.println("user.getSchemeId() = " + user.getSchemeId());
//        Map countData = this.dashboardService.getDashBoardData(new Integer("1"), fiscalYear.getId(), user);
            Map countData = this.dashboardService.getDashBoardDataUpdate(null, fiscalYear.getId(), user);
            model.addAttribute("countData", countData);
            return "dashboardUpdate";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:/login";

        }
    }

    private void mapFiscalYear(Model model, FiscalYear fiscalYear) {
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("fiscalYearName", fiscalYear.getNameInEnglish());
        } else {
            model.addAttribute("fiscalYearName", fiscalYear.getNameInBangla());
        }
    }

    /**
     *
     * @param locale
     * @return
     */
    @RequestMapping(value = "/payroll")
    public @ResponseBody
    List<Object> getPaymentData(Locale locale) {
        FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
        Map map = this.dashboardService.getPayrollByCycle(new Integer("1"), fiscalYear.getId(), locale);
        List<Object> objects = new ArrayList<>();
        objects.add(map);
        objects.add(localizer.getLocalizedText("dashboard.amount", LocaleContextHolder.getLocale()));// legend
        objects.add(localizer.getLocalizedText("dashboard.cycleName", LocaleContextHolder.getLocale()));//labelX
        objects.add(localizer.getLocalizedText("dashboard.amount", LocaleContextHolder.getLocale()));//labelY
        return objects;
    }

    @RequestMapping(value = "/payrollByFiscalYear")
    public @ResponseBody
    List<Object> getPayrollByFiscalYear(Locale locale, HttpSession session) {
        Integer schemeId = ((UserDetail) session.getAttribute("userDetail")).getSchemeId();
        Map map = this.dashboardService.getPayrollByFiscalYear(schemeId, locale);
        List<Object> objects = new ArrayList<>();
        objects.add(map);
        objects.add(localizer.getLocalizedText("dashboard.amount", LocaleContextHolder.getLocale()));// legend
        objects.add(localizer.getLocalizedText("dashboard.fiscalYear", LocaleContextHolder.getLocale()));//labelX
        objects.add(localizer.getLocalizedText("dashboard.amount", LocaleContextHolder.getLocale()));//labelY
        return objects;
    }

    @RequestMapping(value = "/quotaVsBen")
    public @ResponseBody
    List<Object> getQuotaVsBenData(Locale locale) {
        FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
        List<Object> objects = this.dashboardService.getQuotaVsBenByDivision(new Integer("1"), fiscalYear.getId(), locale);

        objects.add(localizer.getLocalizedText("dashboard.quota", LocaleContextHolder.getLocale())); // legendX
        objects.add(localizer.getLocalizedText("dashboard.beneficiary", LocaleContextHolder.getLocale())); // legendY
        objects.add(localizer.getLocalizedText("dashboard.division", LocaleContextHolder.getLocale())); //labelX
        objects.add(localizer.getLocalizedText("dashboard.count", LocaleContextHolder.getLocale())); //labelY

        return objects;
    }

    @RequestMapping(value = "/benByDivision")
    public @ResponseBody
    List<Object> getBenByDivision(Locale locale, HttpSession session) {
        FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
        UserDetail userDetail = ((UserDetail) session.getAttribute("userDetail"));
        boolean isLma = false;
//        Integer schemeId = userDetail.getSchemeId();
//        if(userDetail.getScheme().getShortName().equalsIgnoreCase("lma"))
//        {
        isLma = true;
//        }
        List<Object> objects = this.dashboardService.getBenByDivision(isLma, null, fiscalYear.getId(), locale);
        objects.add(localizer.getLocalizedText("dashboard.beneficiary", LocaleContextHolder.getLocale())); // legendY
        objects.add(localizer.getLocalizedText("dashboard.division", LocaleContextHolder.getLocale())); //labelX
        objects.add(localizer.getLocalizedText("dashboard.count", LocaleContextHolder.getLocale())); //labelY

        return objects;
    }

    /**
     *
     * @param locale
     * @return
     */
    @RequestMapping(value = "/trainingByMonth")
    public @ResponseBody
    List<Object> getTrainingByMonth(Locale locale) {
        FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
        //Map map = this.dashboardService.getTrainingByMonth(new Integer("1"), fiscalYear.getId(), locale);
        List<Object> objects = new ArrayList<>();
        //objects.add(map);
        objects.add(localizer.getLocalizedText("dashboard.count", LocaleContextHolder.getLocale()));// legend
        objects.add(localizer.getLocalizedText("dashboard.month", LocaleContextHolder.getLocale()));//labelX
        objects.add(localizer.getLocalizedText("dashboard.trainingSession", LocaleContextHolder.getLocale()));//labelY
        return objects;
    }

    @RequestMapping(value = "/trainingByFiscalYear")
    public @ResponseBody
    List<Object> getTrainingByFiscalYear(Locale locale, HttpSession session) {
        Integer schemeId = ((UserDetail) session.getAttribute("userDetail")).getSchemeId();
        Map map = this.dashboardService.getTrainingByFiscalYear(schemeId, locale);
        List<Object> objects = new ArrayList<>();
        objects.add(map);
        objects.add(localizer.getLocalizedText("dashboard.count", LocaleContextHolder.getLocale()));// legend
        objects.add(localizer.getLocalizedText("dashboard.fiscalYear", LocaleContextHolder.getLocale()));//labelX
        objects.add(localizer.getLocalizedText("dashboard.trainingSession", LocaleContextHolder.getLocale()));//labelY
        return objects;
    }

    @RequestMapping(value = "/applicantByMonth")
    public @ResponseBody
    List<Object> getApplicantByMonth(Locale locale, HttpSession session) {
        FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
        UserDetail userDetail = ((UserDetail) session.getAttribute("userDetail"));
        Map map = this.dashboardService.getApplicantByMonth(new Integer("1"), fiscalYear.getId(), locale, userDetail);
        List<Object> objects = new ArrayList<>();
        objects.add(map);
        objects.add(localizer.getLocalizedText("dashboard.count", LocaleContextHolder.getLocale()));// legend
        objects.add(localizer.getLocalizedText("dashboard.month", LocaleContextHolder.getLocale()));//labelX
        objects.add(localizer.getLocalizedText("dashboard.applicantCount", LocaleContextHolder.getLocale()));//labelY
        return objects;
    }

    ///////////////////////
//    @RequestMapping(value = "/dashboard-update")
//    public String loadDashboardUpdate(Model model, HttpSession session)
//    {
//        FiscalYear fiscalYear = this.fiscalYearService.getActiveFiscalYear();
////        mapFiscalYear(model, fiscalYear);
//        UserDetail user = (UserDetail) session.getAttribute("userDetail");
//        System.out.println("user.getSchemeId() = " + user.getSchemeId());
////        Map countData = this.dashboardService.getDashBoardData(new Integer("1"), fiscalYear.getId(), user);
//        Map countData = this.dashboardService.getDashBoardDataUpdate(null, fiscalYear.getId(), user);
//        model.addAttribute("countData", countData);
//        return "dashboardUpdate";
//    }
}
