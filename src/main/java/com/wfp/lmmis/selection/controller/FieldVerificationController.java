/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.selection.controller;

import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantView;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.enums.CommentType;
import com.wfp.lmmis.enums.StageType;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.selection.model.SelectionComments;
import com.wfp.lmmis.selection.service.SelectionCommentsService;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.utility.ApplicationConstants;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
public class FieldVerificationController
{

    @Autowired
    ApplicantService applicantService;
    @Autowired
    SelectionCommentsService selectionCommentsService;
    
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/fieldVerification", method = RequestMethod.GET)
    public String getFieldVerificationPage(Model model, HttpServletRequest request)
    {
        List<ApplicationStatus> statusList = new ArrayList<ApplicationStatus>(Arrays.asList(ApplicationStatus.values()));
        model.addAttribute("statusList", statusList);

        List<ItemObject> stateList = new ArrayList<ItemObject>();
        stateList.add(new ItemObject(0, "যাচাইকরণ পেন্ডিং", "Verification Pending"));
        stateList.add(new ItemObject(1, "যাচাইকরণ কমপ্লিট", "Verification Completed"));
        model.addAttribute("stateList", stateList);

        CommonUtility.mapDivisionName(model);
        CommonUtility.mapBGMEAFactoryName(model);
        CommonUtility.mapBKMEAFactoryName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "fieldVerification";
    }

    @RequestMapping(value = "/fieldVerification", method = RequestMethod.POST)
    public String changeFieldVerificationStatus(Model model, HttpSession session,
            HttpServletRequest request, RedirectAttributes redirectAttributes)
    {
        String action = request.getParameter("action");
        if ("accept".equals(action))
        {
            String approvedApplicantList = request.getParameter("approvedApplicantList");
            System.out.println("in submit fv" + approvedApplicantList);
            if (!"".equals(approvedApplicantList))
            {
                String reason = request.getParameter("reasonFV");
                String[] applicantArray = approvedApplicantList.split(",");
                for (int i = 0; i < applicantArray.length; i++)
                {
                    Applicant applicant = applicantService.getApplicant(new Integer(applicantArray[i]));
                    if (applicant.getApplicationStatus() == ApplicationStatus.FIELD_VERIFICATION_PENDING_WITH_NID)
                    {
                        applicant.setApplicationStatus(ApplicationStatus.FIELD_VERFICATION_DONE_WITH_NID);
                    }
                    else if (applicant.getApplicationStatus() == ApplicationStatus.FIELD_VERIFICATION_PENDING_WITHOUT_NID)
                    {
                        applicant.setApplicationStatus(ApplicationStatus.FIELD_VERFICATION_DONE_WITHOUT_NID);
                    }
//                    applicant.setFieldVerificationDone(Boolean.TRUE);
                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
                    applicantService.edit(applicant);
                    SelectionComments selectionComments = new SelectionComments(null, CommentType.APPROVED, reason, StageType.FIELD_VERIFICATION, applicant, (User) session.getAttribute("user"), Calendar.getInstance());
                    this.selectionCommentsService.save(selectionComments);
                }
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("approveFieldVerification", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }
        }
        if ("reject".equals(action))
        {
            String rejectedApplicantList = request.getParameter("rejectedApplicantList");
            System.out.println("in submit fv" + rejectedApplicantList);
            if (!"".equals(rejectedApplicantList))
            {
                String reason = request.getParameter("reasonFV");
                String[] applicantArray = rejectedApplicantList.split(",");
                for (int i = 0; i < applicantArray.length; i++)
                {
                    Applicant applicant = applicantService.getApplicant(new Integer(applicantArray[i]));
                    applicant.setApplicationStatus(ApplicationStatus.REJECTED_FIELD_VERIFICATION);
//                    applicant.setFieldVerificationDone(Boolean.TRUE);
                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
                    applicantService.edit(applicant);
                    SelectionComments selectionComments = new SelectionComments(null, CommentType.REJECTED, reason, StageType.FIELD_VERIFICATION, applicant, (User) session.getAttribute("user"), Calendar.getInstance());
                    this.selectionCommentsService.save(selectionComments);
                }
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("rejectFieldVerification", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }
        }
        return "redirect:/fieldVerification";
//        return getFieldVerificationPage(model);
    }

    /**
     *
     * @param request
     * @param response
     * @param session
     * @throws IOException
     */
    @RequestMapping(value = "/fieldVerification/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationFieldVerification(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws IOException
    {
        //Fetch the page number from client
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));
        String state = request.getParameter("state");
        String nid = request.getParameter("nid");
        //String applicantIdSt = request.getParameter("applicantId1");
        String divisionId = null, districtId = null, upazilaId = null, unionId = null;
        if (request.getParameter("divisionId") != null && !"".equals(request.getParameter("divisionId")))
        {
            divisionId = request.getParameter("divisionId");
        }
        if (request.getParameter("districtId") != null && !"".equals(request.getParameter("districtId")))
        {
            districtId = request.getParameter("districtId");
        }
        if (request.getParameter("upazilaId") != null && !"".equals(request.getParameter("upazilaId")))
        {
            upazilaId = request.getParameter("upazilaId");
        }
        if (request.getParameter("unionId") != null && !"".equals(request.getParameter("unionId")))
        {
            unionId = request.getParameter("unionId");
        }
        String bgmeaFactoryId = request.getParameter("bgmeaFactoryId");
        String bkmeaFactoryId = request.getParameter("bkmeaFactoryId");
        int regularUser = Integer.parseInt(request.getParameter("regularUser"));
        int bgmeaUser = Integer.parseInt(request.getParameter("bgmeaUser"));
        int bkmeaUser = Integer.parseInt(request.getParameter("bkmeaUser"));
        
        if ("".equals(unionId))
        {
            User user = (User) session.getAttribute("user");
            unionId = user.getUnion().getId().toString();
        }
        
        Map parameter = new HashMap();
        parameter.put("schemeId", ((UserDetail) request.getSession().getAttribute("userDetail")).getSchemeId());
        parameter.put("nid", nid != null && !"".equals(nid) ? (String) nid : null);
        parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
        parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
        parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
        parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
        parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
        parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);
        parameter.put("regularUser", Integer.valueOf(request.getParameter("regularUser")));
        parameter.put("bgmeaUser", Integer.valueOf(request.getParameter("bgmeaUser")));
        parameter.put("bkmeaUser", Integer.valueOf(request.getParameter("bkmeaUser")));
        
        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
        //0 for pending, 1 for processing
        if ("0".equals(state))
        {
            applicationStatusList.add(ApplicationStatus.FIELD_VERIFICATION_PENDING_WITH_NID);
            applicationStatusList.add(ApplicationStatus.FIELD_VERIFICATION_PENDING_WITHOUT_NID);
        }
        else if ("1".equals(state))
        {
            applicationStatusList.add(ApplicationStatus.FIELD_VERFICATION_DONE_WITH_NID);
            applicationStatusList.add(ApplicationStatus.FIELD_VERFICATION_DONE_WITHOUT_NID);
        }
        parameter.put("applicationStatusList", applicationStatusList);
        
        List<Object> resultList = applicantService.getApplicantListBySearchParameter(parameter, beginIndex, pageSize);;
        List<ApplicantView> applicantList = (List<ApplicantView>) resultList.get(0);
        int recordsTotal = (int) applicantList.size();
        int recordsFiltered = (int) applicantList.size();

        for (ApplicantView applicant : applicantList)
        {
            JSONArray ja = new JSONArray();
            ja.put("<input type=\"checkbox\" class=\"checkbox\" id=\"" + applicant.getId() + "\" name=\"" + applicant.getId() + "\">");
            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage()))
            {
//                ja.put((applicant.getFirstNameInEnglish() == null ? ' ' : applicant.getFirstNameInEnglish()) + " " + (applicant.getMiddleNameInEnglish() == null ? ' ' : applicant.getMiddleNameInEnglish()) + " " + (applicant.getLastNameInEnglish() == null ? ' ' : applicant.getLastNameInEnglish()));
                ja.put(applicant.getFullNameInEnglish());
                ja.put(applicant.getNid().toString());
                ja.put(applicant.getMobileNo() != null ? applicant.getMobileNo().toString() : "");
                ja.put(CalendarUtility.getDateString(applicant.getDateOfBirth()));
            }
            else
            {
//                ja.put((applicant.getFirstNameInBangla() == null ? ' ' : applicant.getFirstNameInBangla()) + " " + (applicant.getMiddleNameInBangla() == null ? ' ' : applicant.getMiddleNameInBangla()) + " " + (applicant.getLastNameInBangla() == null ? ' ' : applicant.getLastNameInBangla()));
                ja.put(applicant.getFullNameInBangla());
                ja.put(CommonUtility.getNumberInBangla(applicant.getNid().toString()));
                if (applicant.getMobileNo() != null)
                {
                    ja.put(CommonUtility.getNumberInBangla(applicant.getMobileNo().toString()));
                }
                else
                {
                    ja.put("");
                }
                ja.put(CommonUtility.getNumberInBangla(CalendarUtility.getDateString(applicant.getDateOfBirth())));
            }
            if (applicant.getPresentAddressLine2() != null)
            {
                ja.put(applicant.getPresentAddressLine1() + " " + applicant.getPresentAddressLine2());
            }
            else
            {
                ja.put(applicant.getPresentAddressLine1());
            }
            ja.put("<a href=\"applicant/viewApplicant/" + applicant.getId() + "\" data-toggle=\"modal\" >\n"
                    + "                                                <span class=\"glyphicon glyphicon-eye-open\"></span> \n"
                    + "                                            </a>");
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
}
