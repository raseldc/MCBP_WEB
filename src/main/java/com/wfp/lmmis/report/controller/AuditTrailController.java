/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.masterdata.service.DivisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.rm.service.UserService;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.Localizer;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author PCUser
 */
@Controller
public class AuditTrailController
{

    //private static final logger logger = //logger.getlogger(ApplicantReportController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private FiscalYearService fiscalYearService;
    @Autowired
    private DivisionService divisionService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private UpazillaService upazilaService;
    @Autowired
    private UnionService unionService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/auditTrail")
    public String getBeneficiaryReport(Model model, HttpServletRequest request)
    {
        List<LoggingServiceType> loggingServiceTypeList = Arrays.asList(LoggingServiceType.values());
        model.addAttribute("loggingServiceTypeList", loggingServiceTypeList);
        List<LoggingTableType> loggingTableTypeList = Arrays.asList(LoggingTableType.values());
        model.addAttribute("loggingTableTypeList", loggingTableTypeList);
        model.addAttribute("dateFormat", "dd-mm-yy");
        return "auditTrail";
    }

    @RequestMapping(value = "/auditTrail/list", method = RequestMethod.POST)
    @ResponseBody
    public void showAuditTrailList(HttpServletRequest request, HttpServletResponse response)
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
            parameter.put("loggingServiceType", request.getParameter("loggingServiceType"));
            parameter.put("loggingTableType", request.getParameter("loggingTableType"));
            parameter.put("startDate", request.getParameter("startDate"));
            parameter.put("endDate", request.getParameter("endDate"));

            List<Object> resultList = this.userService.getAudittrailListBySearchParameter(parameter, beginIndex, pageSize);
            List<ChangeLog> changeLogList = (List<ChangeLog>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);
            Locale locale = LocaleContextHolder.getLocale();
            for (ChangeLog changeLog : changeLogList)
            {
                JSONArray ja = new JSONArray();
                if ("en".equals(locale.getLanguage()))
                {
                    ja.put(changeLog.getChangedBy().getFullNameEn());
                }
                else
                {
                    ja.put(changeLog.getChangedBy().getFullNameBn());
                }
                ja.put(CalendarUtility.getDateString(changeLog.getChangedDate()));
                if ("en".equals(locale.getLanguage()))
                {
                    ja.put(changeLog.getLoggingTableType().getDisplayName());
                    ja.put(changeLog.getLoggingServiceType().getDisplayName());
                }
                else
                {
                    ja.put(changeLog.getLoggingTableType().getDisplayNameBn());
                    ja.put(changeLog.getLoggingServiceType().getDisplayNameBn());
                }
                ja.put(changeLog.getBeforeValue());
                ja.put(changeLog.getAfterValue());
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
