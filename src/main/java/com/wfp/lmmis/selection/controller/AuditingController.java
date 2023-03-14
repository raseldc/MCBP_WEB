/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.selection.controller;

import static com.wfp.lmmis.applicant.controller.ApplicantController.FILE_CREATION_PATH;
import com.wfp.lmmis.applicant.controller.BeneficiaryController;
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.masterdata.service.BenQuotaService;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.utility.ApplicationConstants;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author user
 */
@Controller
public class AuditingController
{

    @Autowired
    ApplicantService applicantService;
    @Autowired
    private BenQuotaService benQuotaService;
    @Autowired
    private FiscalYearService fiscalYearService;
    @Autowired
    DivisionService divisionService;
    @Autowired
    DistrictService districtService;
    @Autowired
    UnionService unionService;
    @Autowired
    UpazillaService upazillaService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    BeneficiaryController beneficiaryController;

    @RequestMapping(value = "/auditing", method = RequestMethod.GET)
    public String getAuditingPage(Model model, HttpServletRequest request, HttpSession session)
    {
        CommonUtility.mapDivisionName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        model.addAttribute("quota", "1");
        return "auditing";
    }

//    @RequestMapping(value = "/auditing/list", method = RequestMethod.POST)
//    public @ResponseBody
//    void paginationAuditing(HttpServletRequest request, HttpServletResponse response)
//    {
//        //Fetch the page number from client
//        System.out.println("in auditing pagination controller");
//        JSONObject jsonResult = new JSONObject();
//        JSONArray dataArray = new JSONArray();
//        int draw = Integer.parseInt(request.getParameter("draw"));
//        int beginIndex = Integer.parseInt(request.getParameter("start"));
//        int pageSize = Integer.parseInt(request.getParameter("length"));
//
//        String divisionId = request.getParameter("divisionId");
//        String districtId = request.getParameter("districtId");
//        String upazilaId = request.getParameter("upazilaId");
//        String unionId = request.getParameter("unionId");
//        System.out.println("Division = " + divisionId + " District = " + districtId + " Upazila = " + upazilaId + " Union = " + unionId);
//
//        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
//        applicationStatusList.add(ApplicationStatus.AUDITING_PENDING_WITH_NID);
//        applicationStatusList.add(ApplicationStatus.AUDITING_PENDING_WITHOUT_NID);
//        applicationStatusList.add(ApplicationStatus.REJECTED_PRIORITIZATION);
//
//        List<Object> resultList = applicantService.getApplicantSearchList(null, false,
//                null, false, null, false, applicationStatusList, true, divisionId, divisionId != null, districtId, districtId != null, upazilaId, upazilaId != null, unionId, unionId != null, beginIndex, pageSize);
//        List<Applicant> applicantList = (List<Applicant>) resultList.get(0);
//        int recordsTotal = (int) resultList.get(1);
//        int recordsFiltered = (int) resultList.get(2);
//        System.out.println("recordsTotal = " + recordsTotal);
//        for (Applicant applicant : applicantList)
//        {
//            JSONArray ja = new JSONArray();
//            ja.put("<td><input class=\"checkboxAudit\" id=\"" + applicant.getId() + "\" type=\"checkbox\"></td>");
//            if (applicant.getApplicationStatus() == ApplicationStatus.AUDITING_PENDING_WITH_NID || applicant.getApplicationStatus() == ApplicationStatus.AUDITING_PENDING_WITHOUT_NID)
//            {
//                ja.put("<td><input class=\"checkbox\" id=\"" + applicant.getId() + "\" type=\"checkbox\" checked=\"checked\" disabled=\"true\" ></td>");
//            }
//            else
//            {
//                ja.put("<td><input class=\"checkbox\" id=\"" + applicant.getId() + "\" type=\"checkbox\" disabled=\"true\"></td>");
//            }
//            Locale locale = LocaleContextHolder.getLocale();
//            if ("en".equals(locale.getLanguage()))
//            {
////                ja.put((applicant.getFirstNameInEnglish() == null ? ' ' : applicant.getFirstNameInEnglish()) + " " + (applicant.getMiddleNameInEnglish() == null ? ' ' : applicant.getMiddleNameInEnglish()) + " " + (applicant.getLastNameInEnglish() == null ? ' ' : applicant.getLastNameInEnglish()));
//                ja.put(applicant.getFullNameInEnglish());
//            }
//            else
//            {
////                ja.put((applicant.getFirstNameInBangla() == null ? ' ' : applicant.getFirstNameInBangla()) + " " + (applicant.getMiddleNameInBangla() == null ? ' ' : applicant.getMiddleNameInBangla()) + " " + (applicant.getLastNameInBangla() == null ? ' ' : applicant.getLastNameInBangla()));
//                ja.put(applicant.getFullNameInBangla());
//            }
//            ja.put(applicant.getNid());
//            ja.put(applicant.getMobileNo());
//            ja.put(applicant.getPresentAddressLine1() + " " + applicant.getPresentAddressLine2());
//            ja.put("<a href=\"applicant/viewApplicant/" + applicant.getId() + "\" data-toggle=\"modal\" >\n"
//                    + "                                                <span class=\"glyphicon glyphicon-eye-open\"></span> \n"
//                    + "                                            </a>");
//            dataArray.put(ja);
//        }
//        try
//        {
//            jsonResult.put("recordsTotal", recordsTotal);
//            jsonResult.put("recordsFiltered", recordsFiltered);
//            jsonResult.put("data", dataArray);
//            jsonResult.put("draw", draw);
//        }
//        catch (JSONException ex)
//        {
//
//        }
//
//        response.setContentType("application/json");
//        response.setHeader("Cache-Control", "no-store");
//        PrintWriter out;
//        try
//        {
//            out = response.getWriter();
//            out.print(jsonResult);
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
//    }

    /**
     *
     * @param model
     * @param session
     * @param request
     * @param redirectAttributes
     * @return
     */

    @RequestMapping(value = "/auditing", method = RequestMethod.POST)
    public String changeAuditingStatus(Model model, HttpSession session,
            HttpServletRequest request, RedirectAttributes redirectAttributes)
    {
        String action = request.getParameter("action");
        if ("accept".equals(action))
        {
            String selectedApplicantList = request.getParameter("selectedApplicantList");
            String unselectedApplicantList = request.getParameter("unselectedApplicantList");
            System.out.println("Selected applicant list = " + selectedApplicantList);
            if (!"".equals(selectedApplicantList))
            {
                //approved (selected only) applicants status update here
                String[] applicantArray = selectedApplicantList.split(",");
                for (int i = 0; i < applicantArray.length; i++)
                {
                    Applicant applicant = applicantService.getApplicant(new Integer(applicantArray[i]));
                    if (applicant.getApplicationStatus() == ApplicationStatus.AUDITING_PENDING_WITH_NID)
                    {
                        applicant.setApplicationStatus(ApplicationStatus.CARD_PRINTING_PENDING_WITH_NID);
                    }
                    else if (applicant.getApplicationStatus() == ApplicationStatus.AUDITING_PENDING_WITHOUT_NID)
                    {
                        applicant.setApplicationStatus(ApplicationStatus.NID_NEEDED);
                    }
                    else if (applicant.getApplicationStatus() == ApplicationStatus.REJECTED_PRIORITIZATION)
                    {
                        if (applicant.getNid() != null)
                        {
                            applicant.setApplicationStatus(ApplicationStatus.CARD_PRINTING_PENDING_WITH_NID);
                        }
                        else
                        {
                            applicant.setApplicationStatus(ApplicationStatus.NID_NEEDED);
                        }
                    }
                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
                    applicantService.edit(applicant);
                    
                    
                    System.out.println("path = " + request.getServletContext().getRealPath(FILE_CREATION_PATH) + "/");
                    
                    beneficiaryController.saveBeneficiary(applicant, session, request);

                }
            }

            if (!"".equals(unselectedApplicantList))
            {
                System.out.println("unselected applicant list = " + unselectedApplicantList);
                String[] unselectedApplicantArray = unselectedApplicantList.split(",");
                for (int i = 0; i < unselectedApplicantArray.length; i++)
                {
                    Applicant applicant = applicantService.getApplicant(new Integer(unselectedApplicantArray[i]));
                    if (applicant != null)
                    {
                        applicant.setApplicationStatus(ApplicationStatus.REJECTED_AUDITING);
                    }
                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
                    applicantService.edit(applicant);
                }
            }
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, ApplicationConstants.EDIT_MESSAGE);
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        return "redirect:/auditing";
    }
}
