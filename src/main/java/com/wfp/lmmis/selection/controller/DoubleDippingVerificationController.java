/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.selection.controller;

import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantView;
import com.wfp.lmmis.applicant.model.DoubleDippingMatchedStatus;
import com.wfp.lmmis.applicant.model.SpbmuDoubleDippingResponse;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.applicant.service.DoubleDippingMatchedService;
import com.wfp.lmmis.enums.ServiceType;
import com.wfp.lmmis.model.ServiceSettings;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.service.ServiceSettingsService;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
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
public class DoubleDippingVerificationController
{

    @Autowired
    ApplicantService applicantService;

    @Autowired
    ServiceSettingsService serviceSettingsService;

    @Autowired
    DoubleDippingMatchedService doubleDippingMatchedService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/DoubleDippingVerification", method = RequestMethod.GET)
    public String getDoubleDippingVerificationPage(Model model, HttpServletRequest request)
    {
        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
        applicationStatusList.add(ApplicationStatus.DOUBLE_DIPPING_VERIFICATION_PENDING);
//        applicationStatusList.add(ApplicationStatus.NEW_NID_DOUBLE_DIPPING_VERIFICATION_PENDING);
        List<ItemObject> statusList = new ArrayList<ItemObject>();
        statusList.add(new ItemObject(0, "ডাবল ডিপিং যাচাইকরণ পেন্ডিং", "Double Dipping Verification Pending"));
        statusList.add(new ItemObject(1, "ডাবল ডিপিং যাচাইকরণ কমপ্লিট", "Double Dipping Verification Completed"));
        model.addAttribute("statusList", statusList);
        model.addAttribute("statusSelected", "0");
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapBGMEAFactoryName(model);
        CommonUtility.mapBKMEAFactoryName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "DoubleDippingVerification";
    }

    @RequestMapping(value = "/DoubleDippingVerification", method = RequestMethod.POST)
    public String changeDoubleDippingVerificationStatus(Model model, HttpSession session,
            HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception
    {
        System.out.println("in double diping verification post ");
        String action = request.getParameter("action");
        System.out.println("action = " + action);
        int totalPassed = 0, totalFailed = 0;
        if ("accept".equals(action))
        {
            String selectedApplicantList = request.getParameter("selectedApplicantList");
            System.out.println("selected applicant list : " + selectedApplicantList);
            if (!selectedApplicantList.isEmpty())
            {
//                String reason = request.getParameter("reasonFV");
                String[] applicantArray = selectedApplicantList.split(",");
                for (int i = 0; i < applicantArray.length; i++)
                {
                    Applicant applicant = applicantService.getApplicant(new Integer(applicantArray[i]));

                    SpbmuDoubleDippingResponse result = sendNIDToSpbmu(applicant.getNid().toString(), applicant.getDateOfBirth(), 1);

                    if (result.getResult() == "0")
                    {
                        ApplicationStatus applicationStatus = applicant.getApplicationStatus();

                        if (applicationStatus.equals(ApplicationStatus.DOUBLE_DIPPING_VERIFICATION_PENDING))
                        {
                            applicant.setApplicationStatus(ApplicationStatus.PRIORITIZATION_PENDING_WITH_NID);
                            totalPassed++;
                        }

                    }
                    else if (result.getResult() == "1")
                    {
                        System.out.println("duplicate result 1");
                        applicant.setApplicationStatus(ApplicationStatus.REJECTED_SPBMU);
                        DoubleDippingMatchedStatus doubleDippingMatchedStatus = new DoubleDippingMatchedStatus();
                        doubleDippingMatchedStatus.setDdMatchedMInistryNameEn(result.getMinistryNameEn());
                        doubleDippingMatchedStatus.setDdMatchedMinistryNameBn(result.getMinistryNameBn());
                        doubleDippingMatchedStatus.setDdMatchedSchemeEn(result.getSchemeNameEn());
                        doubleDippingMatchedStatus.setDdMatchedSchemeBn(result.getSchemeNameBn());
                        doubleDippingMatchedStatus.setApplicant(applicant);
                        doubleDippingMatchedService.save(doubleDippingMatchedStatus);
                        totalFailed++;
                    }

                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
                    applicantService.edit(applicant);
                }
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("SPBMUVerified", LocaleContextHolder.getLocale()) + " " + localizer.getLocalizedText("duplicateNotFound", LocaleContextHolder.getLocale()) + " : " + ("en".equals(LocaleContextHolder.getLocale().getLanguage()) ? totalPassed : CommonUtility.getNumberInBangla(Integer.toString(totalPassed))) + ", " + localizer.getLocalizedText("duplicateFound", LocaleContextHolder.getLocale()) + " : " + ("en".equals(LocaleContextHolder.getLocale().getLanguage()) ? totalFailed : CommonUtility.getNumberInBangla(Integer.toString(totalFailed))));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }
        }
        System.out.println("total pass = " + totalPassed + " and total fail = " + totalFailed);
        return "redirect:/DoubleDippingVerification";
    }

    @RequestMapping(value = "/DoubleDippingVerification/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationDoubleDippingVerification(HttpServletRequest request, HttpServletResponse response)
    {
        //Fetch the page number from client
        System.out.println("in pagination controller");
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
//        Integer pageNumber = 0;
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));
        String status = request.getParameter("status");
        String nid = request.getParameter("nid");
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

        System.out.println("Nid = " + nid + " Division = " + divisionId + " District = " + districtId + " Upazila = " + upazilaId + " Union = " + unionId);

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
        // 0 for pending, 1 for complete
        if (status.equals("0"))
        {
            applicationStatusList.add(ApplicationStatus.DOUBLE_DIPPING_VERIFICATION_PENDING);
//            applicationStatusList.add(ApplicationStatus.NEW_NID_VERIFICATION_PENDING);
        }
        else if (status.equals("1"))
        {
            applicationStatusList.add(ApplicationStatus.PRIORITIZATION_PENDING_WITH_NID);
            applicationStatusList.add(ApplicationStatus.REJECTED_SPBMU);
//            applicationStatusList.add(ApplicationStatus.NID_VERIFICATION_PROCESSING_WITH_NID_UPDATE);
        }
        parameter.put("applicationStatusList", applicationStatusList);
//        List<Object> resultList = applicantService.getApplicantSearchListNew(nid, nid == null ? false : true,
//                null, false, null, false, applicationStatusList, true, divisionId, divisionId != null, districtId, districtId != null, upazilaId, upazilaId != null, unionId, unionId != null, beginIndex, pageSize);

        List<Object> resultList = applicantService.getApplicantListBySearchParameter(parameter, beginIndex, pageSize);
        List<ApplicantView> applicantList = (List<ApplicantView>) resultList.get(0);
        int recordsTotal = (int) applicantList.size();
        int recordsFiltered = (int) applicantList.size();
        System.out.println("recordsTotal = " + recordsTotal);
        for (ApplicantView applicant : applicantList)
        {
            JSONArray ja = new JSONArray();
            ja.put("<input type=\"checkbox\" class=\"checkbox\" id=\"" + applicant.getId() + "\" name=\"" + applicant.getId() + "\">");

            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage()))
            {
                //ja.put(applicant.getApplicationID());
                ja.put(applicant.getFullNameInEnglish());
                ja.put(applicant.getNid().toString());
                ja.put(applicant.getMobileNo() != null ? applicant.getMobileNo().toString() : "");
                ja.put(CalendarUtility.getDateString(applicant.getDateOfBirth()));
            }
            else
            {
                //ja.put(CommonUtility.getNumberInBangla(applicant.getApplicationID().toString()));
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
            if ("en".equals(locale.getLanguage()))
            {
                if (applicant.getApplicationStatus() == ApplicationStatus.DOUBLE_DIPPING_VERIFICATION_PENDING)
                {
                    ja.put("<span class=\"label\" style='background-color: #D6CC3A;font-size:100%;'>Pending</span>");
                }
                else if (applicant.getApplicationStatus() == ApplicationStatus.PRIORITIZATION_PENDING_WITH_NID)
                {
                    ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%;'>Not Duplicate</span>");
                }
                else if (applicant.getApplicationStatus() == ApplicationStatus.REJECTED_SPBMU)
                {
                    ja.put("<span class=\"label\" style='background-color: #ED4337;font-size:100%;'>Duplicate</span>");
                }

            }
            else
            {
                if (applicant.getApplicationStatus() == ApplicationStatus.DOUBLE_DIPPING_VERIFICATION_PENDING)
                {
                    ja.put("<span class=\"label\" style='background-color: #D6CC3A;font-size:100%;'>পেন্ডিং</span>");
                }
                else if (applicant.getApplicationStatus() == ApplicationStatus.PRIORITIZATION_PENDING_WITH_NID)
                {
                    ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%;'>ডুপ্লিকেট নয়</span>");
                }
                else if (applicant.getApplicationStatus() == ApplicationStatus.REJECTED_SPBMU)
                {
                    ja.put("<span class=\"label\" style='background-color: #ED4337;font-size:100%;'>ডুপ্লিকেট</span>");
                }
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

    public SpbmuDoubleDippingResponse sendNIDToSpbmu(String nid, Calendar dob, int type) throws Exception
    {
        ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.DOUBLEDIPPING);
        //first login to SPBMU
        String token = loginSPBMU(serviceSettings);
        //if login successfull then verify NID 
        SpbmuDoubleDippingResponse sddr = null;
        if (token != null)
        {
            String dobStr = CalendarUtility.getDateString(dob.getTime(), "yyyy-MM-dd");

            sddr = checkDuplicateAtSPBMU(serviceSettings, nid, token);
            System.out.println("duplicate Found = " + sddr.getResult());
            return sddr;
        }

        return sddr;//2 means login or server error
    }

    private String loginSPBMU(ServiceSettings serviceSettings) throws Exception
    {
        URL url = new URL(serviceSettings.getAuthenticatedURL());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);
        con.setDoInput(true);

        JSONObject json = new JSONObject();
        json.put("userName", serviceSettings.getUsername());
        json.put("password", serviceSettings.getPassword());

        // Send post request (login)
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(json.toString());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println("op result = " + jsonResponse);
            if (jsonResponse.get("operationResult").toString() == "true")
            {
                System.out.println("res = " + jsonResponse.get("operationResult").toString());
                return jsonResponse.getString("token");
            }
            System.out.println("res = " + jsonResponse.get("operationResult").toString());
            return null;

        }
        else
        {
            System.out.println("POST request error : " + responseCode);
            return null;
        }

    }

    public SpbmuDoubleDippingResponse checkDuplicateAtSPBMU(ServiceSettings serviceSettings, String nid, String token) throws Exception
    {
        URL url = new URL(serviceSettings.getServiceURL() + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        System.out.println("url = " + url.toString());
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);
        con.setDoInput(true);

        JSONObject json = new JSONObject();
        json.put("nid", nid);
        json.put("token", token);

        // Send post request (login)
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(json.toString());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        SpbmuDoubleDippingResponse sddr = new SpbmuDoubleDippingResponse();
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "utf-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println("duplicate check  result = " + jsonResponse);
            if (jsonResponse.get("operationResult").toString() == "true" && (int) jsonResponse.get("total") > 0)
            {
                System.out.println("res2 = " + jsonResponse.get("operationResult").toString() + " total beneficiary = " + (int) jsonResponse.get("total"));
                sddr.setResult("1");

                JSONArray benList = jsonResponse.getJSONArray("beneficiaryList");
                String matchedMinistryNameBn = "", matchedMinistryNameEn = "", matchedSchemeNameBn = "", matchedSchemeNameEn = "";
                for (int i = 0; i < benList.length(); i++)
                {
                    JSONObject benInfo = benList.getJSONObject(i);

                    matchedMinistryNameBn += benList.length() == 1 ? benInfo.get("ministryNameBn").toString() : benInfo.get("ministryNameBn").toString() + ", ";
                    matchedMinistryNameEn += benList.length() == 1 ? benInfo.get("ministryNameEn").toString() : benInfo.get("ministryNameEn").toString();
                    matchedSchemeNameBn += benList.length() == 1 ? benInfo.get("schemeNameBn").toString() : benInfo.get("schemeNameBn").toString();
                    matchedSchemeNameEn += benList.length() == 1 ? benInfo.get("schemeNameEn").toString() : benInfo.get("schemeNameEn").toString();
                }
                sddr.setMinistryNameBn(matchedMinistryNameBn);
                sddr.setMinistryNameEn(matchedMinistryNameEn);
                sddr.setSchemeNameBn(matchedSchemeNameBn);
                sddr.setSchemeNameEn(matchedSchemeNameEn);

                return sddr;
            }

            sddr.setResult("0");
            //should be false
            return sddr;

        }
        else
        {
            System.out.println("POST request error : " + responseCode);
            sddr.setResult("2");
            //should be false
            return sddr;
        }
    }
}
