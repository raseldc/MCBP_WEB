/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.monitoring.controller;

import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.monitoring.model.Monitoring;
import com.wfp.lmmis.monitoring.model.Purpose;
import com.wfp.lmmis.monitoring.service.MonitoringService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.monitoring.service.PurposeService;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
public class MonitoringController
{

    //private static final logger logger = //logger.getlogger(UserController.class);

    @Autowired
    private MonitoringService monitoringService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private PurposeService purposeService;
    @Autowired
    private DivisionService divisionService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    public void mapGlobalVariables(Model m)
    {
        m.addAttribute("dateFormat", "dd-mm-yy");
    }

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/monitoring/list")
    public String showMonitoringList(Model model)
    {
        CommonUtility.mapSchemeName(model);
        mapPurposeName(model);
        return "monitoringList";
    }

    @RequestMapping(value = "/monitoring/create", method = RequestMethod.GET)
    public String createMonitoring(@ModelAttribute Monitoring monitoring, Model model)
    {
        mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        mapPurposeName(model);
        mapDivisionName(model);
        model.addAttribute("actionType", "create");
        return "monitoring";
    }

    private void mapDivisionName(Model model)
    {
        Locale locale = LocaleContextHolder.getLocale();
        List<Division> divisionList = this.divisionService.getDivisionList();
        model.addAttribute("divisionList", divisionList);
        if ("en".equals(locale.getLanguage()))
        {
            model.addAttribute("divisionName", "nameInEnglish");
        }
        else
        {
            model.addAttribute("divisionName", "nameInBangla");
        }
    }

    private void mapPurposeName(Model model)
    {
        Locale locale = LocaleContextHolder.getLocale();
        List<Purpose> purposeList = this.purposeService.getPurposeList(true);
        model.addAttribute("purposeList", purposeList);
        if ("en".equals(locale.getLanguage()))
        {
            model.addAttribute("purposeName", "nameInEnglish");
        }
        else
        {
            model.addAttribute("purposeName", "nameInBangla");
        }
    }

    /**
     *
     * @param monitoring
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/monitoring/create", method = RequestMethod.POST)
    public String createMonitoring(@Valid Monitoring monitoring, BindingResult bindingResult, Model model,
            HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "monitoring";
        }
        try
        {
            System.out.println("in save");
//            monitoring.setNameInEnglish(monitoring.getStartDate().getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH) + "-"
//                    + monitoring.getEndDate().getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH));

            monitoring.setScheme(schemeService.getScheme(monitoring.getScheme().getId()));
            monitoring.setPurpose(purposeService.getPurpose(monitoring.getPurpose().getId()));
            if (monitoring.getDivision().getId() == null)
            {
                monitoring.setDivision(null);
            }
            if (monitoring.getDistrict().getId() == null)
            {
                monitoring.setDistrict(null);
            }
            if (monitoring.getUpazilla().getId() == null)
            {
                monitoring.setUpazilla(null);
            }
            if (monitoring.getUnion().getId() == null)
            {
                monitoring.setUnion(null);
            }
            monitoring.setCreatedBy((User) session.getAttribute("user"));
            monitoring.setCreationDate(Calendar.getInstance());
            this.monitoringService.save(monitoring);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);

        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "redirect:/monitoring/list";
    }

    /**
     *
     * @param id
     * @param monitoring
     * @param model
     * @param monitoringLoadRequired
     * @return
     */
    @RequestMapping(value = "/monitoring/edit/{id}", method = RequestMethod.GET)
    public String editMonitoring(@PathVariable("id") Integer id, @ModelAttribute Monitoring monitoring, Model model, boolean monitoringLoadRequired)
    {
        try
        {
            mapGlobalVariables(model);
            CommonUtility.mapSchemeName(model);
            mapPurposeName(model);
            mapDivisionName(model);
            System.out.println("monitoringLoadRequired = " + monitoringLoadRequired);
            if (!monitoringLoadRequired)
            {
                model.addAttribute("monitoring", this.monitoringService.getMonitoring(id));
            }
            System.out.println("monitoring.getCreationDate() = " + monitoring.getCreationDate());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "monitoring";
    }

    public String getMonthName(int month, Locale locale)
    {
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] monthNames = symbols.getMonths();
        return monthNames[month - 1];
    }

    @RequestMapping(value = "/monitoring/edit/{id}", method = RequestMethod.POST)
    public String editMonitoring(@PathVariable("id") Integer pk, @Valid @ModelAttribute Monitoring monitoring, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
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
                return editMonitoring(pk, monitoring, model, true);
            }
            if (monitoring.getId() == null)
            {
                monitoring.setScheme(schemeService.getScheme(monitoring.getScheme().getId()));
                monitoring.setPurpose(purposeService.getPurpose(monitoring.getPurpose().getId()));

                monitoring.setCreatedBy((User) session.getAttribute("user"));
                monitoring.setCreationDate(Calendar.getInstance());
                this.monitoringService.save(monitoring);
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }
            else
            {
                System.out.println("monitoring.creationDate = " + monitoring.getCreationDate());
//                monitoring.setMinistry(ministryService.getMinistry(monitoring.getMinistry().getId()));
                monitoring.setScheme(schemeService.getScheme(monitoring.getScheme().getId()));

//                monitoring.setNameInEnglish(monitoring.getStartDate().getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH) + "-"
//                        + monitoring.getEndDate().getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH));
                monitoring.setModifiedBy((User) session.getAttribute("user"));
                monitoring.setModificationDate(Calendar.getInstance());
                if (monitoring.getDivision().getId() == null)
                {
                    monitoring.setDivision(null);
                }
                if (monitoring.getDistrict().getId() == null)
                {
                    monitoring.setDistrict(null);
                }
                if (monitoring.getUpazilla().getId() == null)
                {
                    monitoring.setUpazilla(null);
                }
                if (monitoring.getUnion().getId() == null)
                {
                    monitoring.setUnion(null);
                }
                this.monitoringService.edit(monitoring);
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "redirect:/monitoring/list";
    }

    @RequestMapping(value = "monitoring/delete/{monitoringId}")
    public String deleteMonitoring(@PathVariable("monitoringId") Integer monitoringId, RedirectAttributes redirectAttributes)
    {
        System.out.println("in monitoring delete");
        this.monitoringService.delete(monitoringId);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/monitoring/list";
    }

    @RequestMapping(value = "/monitoring/list", method = RequestMethod.POST)
    @ResponseBody
    public void showMonitoringList(HttpServletRequest request, HttpServletResponse response)
    {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try
        {
            System.out.println("in list");
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));

            Map parameter = new HashMap();
//            parameter.put("schemeId", ((UserDetail) request.getSession().getAttribute("userDetail")).getScheme().getId());
            parameter.put("purposeId", Integer.valueOf(request.getParameter("purpose")));

            List<Object> resultList = monitoringService.getMonitoringListBySearchParameter(parameter, beginIndex, pageSize);
            List<Monitoring> monitoringList = (List<Monitoring>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);
            Locale locale = LocaleContextHolder.getLocale();
            String location = "";
            for (Monitoring monitoring : monitoringList)
            {
                JSONArray ja = new JSONArray();
                ja.put(monitoring.getOfficerName());
                ja.put(monitoring.getDesignation());
                ja.put(CalendarUtility.getDateString(monitoring.getMonitoringDate()));
                ja.put(monitoring.getDurationDay());
                ja.put(monitoring.getFindings());
                if ("en".equals(locale.getLanguage()))
                {
                    location = monitoring.getDivision().getNameInEnglish();
                    if (null != monitoring.getDistrict())
                    {
                        location += "->" + monitoring.getDistrict().getNameInEnglish();
                        if (null != monitoring.getUpazilla())
                        {
                            location += "->" + monitoring.getUpazilla().getNameInEnglish();
                            if (null != monitoring.getUnion())
                            {
                                location += "->" + monitoring.getUnion().getNameInEnglish();
                            }
                        }
                    }
                    ja.put(location);
                    ja.put("<a href=\"edit/" + monitoring.getId() + "\" data-toggle=\"tooltip\" "
                            + "title=\"Edit\">\n"
                            + "<span class=\"glyphicon glyphicon-edit\"></span>\n</a>");
                }
                else
                {
                    location = monitoring.getDivision().getNameInBangla();
                    if (null != monitoring.getDistrict())
                    {
                        location += "->" + monitoring.getDistrict().getNameInBangla();
                        if (null != monitoring.getUpazilla())
                        {
                            location += "->" + monitoring.getUpazilla().getNameInBangla();
                            if (null != monitoring.getUnion())
                            {
                                location += "->" + monitoring.getUnion().getNameInBangla();
                            }
                        }
                    }
                    ja.put(location);
                    ja.put("<a href=\"edit/" + monitoring.getId() + "\" data-toggle=\"tooltip\" "
                            + "title=\"সম্পাদন করুন\">\n"
                            + "<span class=\"glyphicon glyphicon-edit\"></span>\n</a>");
                }
                dataArray.put(ja);
            }

            try
            {
                jsonResult.put("recordsTotal", recordsTotal);
                jsonResult.put("recordsFiltered", recordsFiltered);
                jsonResult.put("data", dataArray);
                jsonResult.put("draw", draw);
            }
            catch (JSONException ex)
            {

            }

            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-store");
            PrintWriter out;
            try
            {
                out = response.getWriter();
                out.print(jsonResult);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
