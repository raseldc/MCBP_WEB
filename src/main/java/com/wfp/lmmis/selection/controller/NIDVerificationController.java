/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.selection.controller;

import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.BECNidResponse;
import com.wfp.lmmis.applicant.model.BECResponse;
import com.wfp.lmmis.applicant.model.ApplicantView;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.enums.ServiceType;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.model.ServiceSettings;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.service.ServiceSettingsService;
import com.wfp.lmmis.training.controller.userTrainignController;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.types.UpazilaType;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.Localizer;
import com.wfp.lmmis.utility.ResponseEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author shamiul_islam
 */
@Controller
public class NIDVerificationController {

    public static final String FILE_CREATION_PATH = "/resources/uploadedFile/";// Windows
//    public static final String FILE_CREATION_PATH = "resources/uploadedFile";// Linux
    @Autowired
    ApplicantService applicantService;

    @Autowired
    ServiceSettingsService serviceSettingsService;

    @Autowired
    BeneficiaryService beneficiaryService;

    @Autowired
    private UpazillaService upazillaService;

    Localizer localizer = Localizer.getBrowserLocalizer();
    @Autowired
    private Environment environment;

    @RequestMapping(value = "/NIDVerification", method = RequestMethod.GET)
    public String getNIDVerificationPage(Model model, HttpServletRequest request) {
        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
        applicationStatusList.add(ApplicationStatus.NID_VERIFICATION_PENDING);
        applicationStatusList.add(ApplicationStatus.NEW_NID_VERIFICATION_PENDING);
        List<ItemObject> statusList = new ArrayList<ItemObject>();
        statusList.add(new ItemObject(0, "জাতীয় পরিচয় পত্র যাচাইকরণ পেন্ডিং", "NID Verification Pending"));
        statusList.add(new ItemObject(1, "জাতীয় পরিচয় পত্র যাচাইকরণ কমপ্লিট", "NID Verification Completed"));
        model.addAttribute("status", "0");
        model.addAttribute("statusList", statusList);
        model.addAttribute("statusSelected", "0");
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapBGMEAFactoryName(model);
        CommonUtility.mapBKMEAFactoryName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "NIDVerification";
    }

    @RequestMapping(value = "/NIDVerification", method = RequestMethod.POST)
    public String changeNIDVerificationStatus(Model model, HttpSession session,
            HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        String action = request.getParameter("action");
        String applicantId = request.getParameter("applicantId");
        String selectedUserType = request.getParameter("selectedUserType");
        String divisionId = request.getParameter("divisionId");
        String districtId = request.getParameter("districtId");
        String upazilaId = request.getParameter("upazilaId");
        String unionId = request.getParameter("unionId");

        UserDetail userDetail = userDetail = (UserDetail) request.getSession().getAttribute("userDetail");
        String schemeName = userDetail.getSchemeShortName();

        System.out.println("action = " + action + ", applicant id = " + applicantId + " selectedUserType = " + selectedUserType);
        System.out.println("division id = " + divisionId + ", district id = " + districtId + ", upazila id = " + upazilaId);
        Applicant applicant = applicantService.getApplicant(new Integer(applicantId));
        ApplicationStatus applicationStatus = applicant.getApplicationStatus();
        System.out.println("applicant status before = " + applicant.getApplicationStatus().toString());

        if ("approve".equals(action)) {

            if (!schemeName.isEmpty() && schemeName.equals("LMA")) {
                if (selectedUserType.equals("1"))//regular user
                {
                    if (upazillaService.getUpazilla(Integer.parseInt(upazilaId)).getUpazilaType().equals(UpazilaType.DISTRICT)) {
                        applicant.setApplicationStatus(ApplicationStatus.DISTRICT_MEMBER_LEVEL_VERIFICATION);
                    } else if (upazillaService.getUpazilla(Integer.parseInt(upazilaId)).getUpazilaType().equals(UpazilaType.UPAZILA)) {
                        applicant.setApplicationStatus(ApplicationStatus.UPAZILA_MEMBER_LEVEL_VERIFICATION);
                    }
                } else if (selectedUserType.equals("2"))//bgmea
                {
                    applicant.setApplicationStatus(ApplicationStatus.BGMEA_REPRESENTITIVE_LEVEL_VERIFICATION);
                } else if (selectedUserType.equals("3"))//bkmea
                {
                    applicant.setApplicationStatus(ApplicationStatus.BKMEA_REPRESENTITIVE_LEVEL_VERIFICATION);
                }
            } else if (!schemeName.isEmpty() && schemeName.equals("MA")) {
                if (applicationStatus.equals(ApplicationStatus.NID_VERIFICATION_PENDING)) {
                    applicant.setApplicationStatus(ApplicationStatus.UNION_LEVEL_VERIFICATION);
                } else if (applicationStatus.equals(ApplicationStatus.NEW_NID_VERIFICATION_PENDING)) {
                    applicant.setApplicationStatus(ApplicationStatus.UNION_LEVEL_VERIFICATION);
                }
            }

        } else if ("reject".equals(action)) {
            applicant.setApplicationStatus(ApplicationStatus.REJECTED_NID_VERIFICATION);
        }

        applicant.setModifiedBy((User) session.getAttribute("user"));
        applicant.setModificationDate(Calendar.getInstance());
        applicantService.edit(applicant);
        System.out.println("applicant status after = " + applicant.getApplicationStatus().toString());

        redirectAttributes.addFlashAttribute("redirect", "true");
        redirectAttributes.addFlashAttribute("selectedDivisionId", divisionId);
        redirectAttributes.addFlashAttribute("selectedDistrictId", districtId);
        redirectAttributes.addFlashAttribute("selectedUpazilaId", upazilaId);
        redirectAttributes.addFlashAttribute("selectedUnionId", unionId);
        return "redirect:/NIDVerification";
    }

    /**
     *
     * @param applicantId
     * @param selectedUserType
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/NIDVerification/crossCheck/{selectedUserType}/{applicantId}", method = RequestMethod.GET)
    public String getSystemAndBECInfo(@PathVariable("applicantId") Integer applicantId, @PathVariable("selectedUserType") Integer selectedUserType, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            if (applicantId != null) {
                //get Applicant Infos 
                Applicant applicant = applicantService.getApplicant(applicantId);
                String nid = applicant.getNid().toString();
                Calendar dob = applicant.getDateOfBirth();
                String dobStr = CalendarUtility.getDateString(dob.getTime(), "yyyy-MM-dd");
                System.out.println("dob string = " + dobStr);

                JSONObject becJsonResult = null;

                long timeBefore = System.currentTimeMillis();

                ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.NIDVERIFICATION);
                //first login to SPBMU
                String token = loginSPBMU(serviceSettings);
                //if login successfull then verify NID 
                if (token != null) {
                    becJsonResult = verifyAtSPBMU(serviceSettings, dobStr, nid, token, 1);
                }

                BECResponse becResponse = getBECResponse(becJsonResult);
                long timeAfter = System.currentTimeMillis();
                System.out.println("Time diff = " + (timeAfter - timeBefore) + " ms");
                model.addAttribute("applicant", applicant);
                model.addAttribute("selectedUserType", selectedUserType);
                model.addAttribute("becResponse", becResponse);
                model.addAttribute("imagePath", FILE_CREATION_PATH);
                model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");
                model.addAttribute("headerTitle", "Test");

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "viewComparison";
    }

    @RequestMapping(value = "/NIDVerification/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationNIDVerification(HttpServletRequest request, HttpServletResponse response) {
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
        String bgmeaFactoryId = request.getParameter("bgmeaFactoryId");
        String bkmeaFactoryId = request.getParameter("bkmeaFactoryId");
        int regularUser = Integer.parseInt(request.getParameter("regularUser"));
        int bgmeaUser = Integer.parseInt(request.getParameter("bgmeaUser"));
        int bkmeaUser = Integer.parseInt(request.getParameter("bkmeaUser"));

        int selectedUserType = 0;
        if (regularUser == 1) {
            selectedUserType = 1;
        } else if (bgmeaUser == 1) {
            selectedUserType = 2;
        } else if (bkmeaUser == 1) {
            selectedUserType = 3;
        }
        String divisionId = null, districtId = null, upazilaId = null, unionId = null;
        if (request.getParameter("divisionId") != null && !"".equals(request.getParameter("divisionId"))) {
            divisionId = request.getParameter("divisionId");
        }
        if (request.getParameter("districtId") != null && !"".equals(request.getParameter("districtId"))) {
            districtId = request.getParameter("districtId");
        }
        if (request.getParameter("upazilaId") != null && !"".equals(request.getParameter("upazilaId"))) {
            upazilaId = request.getParameter("upazilaId");
        }
        if (request.getParameter("unionId") != null && !"".equals(request.getParameter("unionId"))) {
            unionId = request.getParameter("unionId");
        }
        System.out.println("Status = " + status + " Division = " + divisionId + " District = " + districtId + " Upazila = " + upazilaId + " Union = " + unionId);

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
        if (status.equals("0")) {
            applicationStatusList.add(ApplicationStatus.NID_VERIFICATION_PENDING);
            applicationStatusList.add(ApplicationStatus.NEW_NID_VERIFICATION_PENDING);
        } else if (status.equals("1")) {
            UserDetail userDetail = (UserDetail) request.getSession().getAttribute("userDetail");
            System.out.println("scheme short name = " + userDetail.getSchemeShortName());
            if (userDetail.getSchemeShortName().equals("MA")) {
                applicationStatusList.add(ApplicationStatus.UNION_LEVEL_VERIFICATION);
                applicationStatusList.add(ApplicationStatus.REJECTED_NID_VERIFICATION);
            } else if (userDetail.getSchemeShortName().equals("LMA")) {
                applicationStatusList.add(ApplicationStatus.UPAZILA_MEMBER_LEVEL_VERIFICATION);
                applicationStatusList.add(ApplicationStatus.DISTRICT_MEMBER_LEVEL_VERIFICATION);
                applicationStatusList.add(ApplicationStatus.BGMEA_REPRESENTITIVE_LEVEL_VERIFICATION);
                applicationStatusList.add(ApplicationStatus.BKMEA_REPRESENTITIVE_LEVEL_VERIFICATION);
                applicationStatusList.add(ApplicationStatus.REJECTED_NID_VERIFICATION);
            }
        }
        parameter.put("applicationStatusList", applicationStatusList);

        List<Object> resultList = applicantService.getApplicantListBySearchParameter(parameter, beginIndex, pageSize);
        List<ApplicantView> applicantList = (List<ApplicantView>) resultList.get(0);
        int recordsTotal = (int) applicantList.size();
        int recordsFiltered = (int) applicantList.size();
        System.out.println("recordsTotal = " + recordsTotal);
        for (ApplicantView applicant : applicantList) {
            JSONArray ja = new JSONArray();
            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage())) {
                //    ja.put(applicant.getApplicationID());
                ja.put(applicant.getFullNameInEnglish());
                ja.put(applicant.getNid().toString());
                ja.put(applicant.getMobileNo() != null ? applicant.getMobileNo().toString() : "");
                ja.put(CalendarUtility.getDateString(applicant.getDateOfBirth()));
            } else {
                //   ja.put(CommonUtility.getNumberInBangla(applicant.getApplicationID().toString()));
                ja.put(applicant.getFullNameInBangla());
                ja.put(CommonUtility.getNumberInBangla(applicant.getNid().toString()));
                if (applicant.getMobileNo() != null) {
                    ja.put(CommonUtility.getNumberInBangla(applicant.getMobileNo().toString()));
                } else {
                    ja.put("");
                }
                ja.put(CommonUtility.getNumberInBangla(CalendarUtility.getDateString(applicant.getDateOfBirth())));
            }
            if (applicant.getPresentAddressLine2() != null) {
                ja.put(applicant.getPresentAddressLine1() + " " + applicant.getPresentAddressLine2());
            } else {
                ja.put(applicant.getPresentAddressLine1());
            }
            ja.put("<a href=\"applicant/viewApplicant/" + applicant.getId() + "\" data-toggle=\"modal\" >\n"
                    + "                                                <span class=\"glyphicon glyphicon-eye-open\"></span> \n"
                    + "                                            </a>");
            if (status.equals("1")) {
                System.out.println("status iss = " + status);
                if ("en".equals(locale.getLanguage())) {
                    if (applicant.getApplicationStatus() == ApplicationStatus.NID_VERIFICATION_PENDING) {
                        ja.put("<span class=\"label\" style='background-color: #D6CC3A;font-size:100%;'>Pending</span>");
                    } else if (applicant.getApplicationStatus() == ApplicationStatus.UNION_LEVEL_VERIFICATION) {
                        ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%;'>Valid</span>");
                    } else if (applicant.getApplicationStatus() == ApplicationStatus.REJECTED_NID_VERIFICATION) {
                        ja.put("<span class=\"label\" style='background-color: #ED4337;font-size:100%;'>Not Valid</span>");
                    }

                } else {
                    if (applicant.getApplicationStatus() == ApplicationStatus.NID_VERIFICATION_PENDING) {
                        ja.put("<span class=\"label\" style='background-color: #D6CC3A;font-size:100%;'>পেন্ডিং</span>");
                    } else if (applicant.getApplicationStatus() == ApplicationStatus.UNION_LEVEL_VERIFICATION) {
                        ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%;'>সঠিক</span>");
                    } else if (applicant.getApplicationStatus() == ApplicationStatus.REJECTED_NID_VERIFICATION) {
                        ja.put("<span class=\"label\" style='background-color: #ED4337;font-size:100%;'>ভুল</span>");
                    }
                }
            } else {
                ja.put("<a href=\"NIDVerification/crossCheck/" + selectedUserType + "/" + applicant.getId() + "\" data-toggle=\"modal\" >\n"
                        + "                                                <span class=\"glyphicon glyphicon-check\"></span> \n"
                        + "                                            </a>");
            }
            dataArray.put(ja);
        }
        try {
            jsonResult.put("recordsTotal", recordsTotal);
            jsonResult.put("recordsFiltered", recordsFiltered);
            jsonResult.put("data", dataArray);
            jsonResult.put("draw", draw);
        } catch (JSONException ex) {

        }

        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(jsonResult);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    public int sendNIDToSpbmu(String applicationId, String nid, Calendar dob, int type) throws Exception
//    {
//        //first login to SPBMU
//        String token = loginSPBMU("sysmowca", "Mowca17");
//        //if login successfull then verify NID 
//        if (token != null)
//        {
//            System.out.println("return value = " + token);
//            String dobStr = CalendarUtility.getDateString(dob.getTime(), "yyyy-MM-dd");
//            System.out.println("dob string = " + dobStr);
//
////            int matchFoundResult = verifyAtSPBMU(dobStr, nid, applicationId, token, type);
////            int matchFoundResult = verifyAtSPBMU("1990-01-01", nid, applicationId, token, type);
////            return matchFoundResult;
//        }
//
//        return 2;//login failed--token null
//    }
    private String loginSPBMU(ServiceSettings serviceSettings) throws Exception {
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

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println("op result = " + jsonResponse);
            if (jsonResponse.get("operationResult").toString() == "true") {
                System.out.println("res = " + jsonResponse.get("operationResult").toString());
                return jsonResponse.getString("token");
            }
            System.out.println("res = " + jsonResponse.get("operationResult").toString());
            return null;

        } else {
            System.out.println("POST request error : " + responseCode);
            return null;
        }

    }

    public JSONObject verifyAtSPBMU(ServiceSettings serviceSettings, String dob, String nid, String token, int type) throws Exception {
        URL url = new URL(serviceSettings.getServiceURL() + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        System.out.println("url = " + url.toString());
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);
        con.setDoInput(true);

        JSONObject json = new JSONObject();
        json.put("dob", dob);
        json.put("nid", nid);
        json.put("referenceNo", nid);
        json.put("token", token);
        json.put("type", type);

        // Send post request (login)
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(json.toString());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "utf-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println("op2 result = " + jsonResponse);

            return jsonResponse;

        } else {
            System.out.println("POST request error : " + responseCode);
            //should be false
            return null;
        }
    }

    private HttpsURLConnection getHttpsURLConnection(String url_str, String postType) throws NoSuchAlgorithmException, KeyManagementException {
        try {
            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[] trust_mgr = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String t) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String t) {
                    }
                }
            };
            ssl_ctx.init(null, // key manager
                    trust_mgr, // trust manager
                    new SecureRandom()); // random number generator
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL url = new URL(url_str);

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setRequestMethod(postType);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0"); // add this line to your code

            con.setDoOutput(true);
            con.setDoInput(true);
            System.out.println("Connention Ok");
            return con;
        } catch (IOException ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ResponseEntity verifyAtIbas(String url, String dob, String nid) throws Exception {
        ResponseEntity responseResult = null;
        url = url.replaceAll("NID_INPUT", nid);
        url = url.replaceAll("DOB_INPUT", dob);
        String url_str = url;// "https://api.finance.gov.bd/ibas2api/NIDService/GetInfo?username=mowca&password=vuREkN8BS1tAwj2IxtThkw==&nid=" + nid + "&dob=" + dob;
        System.out.println("URL :" + url_str);
        try {

            HttpsURLConnection con = getHttpsURLConnection(url_str, "GET");

            int responseCode = con.getResponseCode();
            System.out.println("");
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Response Ok");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream(), "utf-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println("Response " + response.toString());
                String jsonString = response.toString();

                if (jsonString == null || jsonString.equals("")) {
                    throw new Exception("Empty response from server");
                } else {

                    Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class, new NIDVerificationController.ByteArrayToBase64TypeAdapter()).create();

                    responseResult = gson.fromJson(jsonString, ResponseEntity.class);

                    if (response == null) {
                        throw new Exception("Response parsing error");
                    }

                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println("op2 result = " + jsonResponse);

                return responseResult;

            } else {
                System.out.println("POST request error : " + responseCode);
                //should be false
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {

        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.getDecoder().decode(json.getAsString());
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
        }
    }

    public BECResponse getBECResponse(JSONObject becJsonResult) throws JSONException {
        BECResponse bECResponse = new BECResponse();

        if (becJsonResult.get("matchFound").toString() == "true") {
            bECResponse.setMatchFound(true);
            bECResponse.setOperationResult(true);
            BECNidResponse nidData = new BECNidResponse();
            JSONObject nidJSON = becJsonResult.getJSONObject("nidData");

            nidData.setName(nidJSON.get("name").toString());
            nidData.setNameEn(nidJSON.get("nameEn").toString());
            nidData.setNid(nidJSON.get("nid").toString());
            nidData.setDob(nidJSON.get("dob").toString());
            nidData.setGender(nidJSON.get("gender").toString());
            nidData.setPhoto(nidJSON.get("photo").toString());
            nidData.setMother(nidJSON.get("mother").toString());
            nidData.setFather(nidJSON.get("father").toString());
            nidData.setSpouse(nidJSON.get("spouse").toString());
            nidData.setPresentAddress(nidJSON.get("presentAddress").toString());
            nidData.setPermanentAddress(nidJSON.get("permanentAddress").toString());

            bECResponse.setNidData(nidData);

        } else {
            bECResponse.setMatchFound(false);
            System.out.println("match not found");
        }
        return bECResponse;
    }

    public BECResponse getBECResponseFromIbas(ResponseEntity responseResult) throws JSONException {
        BECResponse bECResponse = new BECResponse();

        if (responseResult.getOperationResult().getSuccess() == false) {

            bECResponse.setMatchFound(false);
            bECResponse.setErrorMsg(responseResult.getOperationResult().getError().getErrorMessage());
            return bECResponse;

        } else if (responseResult.getOperationResult().getSuccess() == true) {
            bECResponse.setMatchFound(true);
            bECResponse.setOperationResult(true);
            BECNidResponse nidData = new BECNidResponse();

            nidData.setName(responseResult.getName());
            nidData.setNameEn(responseResult.getNameEn());
            nidData.setNid(responseResult.getNid());
            nidData.setDob(responseResult.getDob());
            nidData.setGender(responseResult.getGender());
            nidData.setPhoto(Base64.getEncoder().encodeToString(responseResult.getPhoto()));
            nidData.setMother(responseResult.getMother());
            nidData.setFather(responseResult.getFather());
            nidData.setSpouse(responseResult.getSpouse());
            nidData.setPresentAddress(responseResult.getPresentAddress());
            nidData.setPermanentAddress(responseResult.getPermanentAddress());

            bECResponse.setNidData(nidData);

        } else {
            bECResponse.setMatchFound(false);
            System.out.println("match not found");
        }
        return bECResponse;
    }

    /*
        Similar for Nid verification for applicant. Only for view purpose
     */
    @RequestMapping(value = "/NIDcomparison/{beneficiaryId}", method = RequestMethod.GET)
    public String getSystemAndBECInfoByBeneficiary(@PathVariable("beneficiaryId") Integer beneficiaryId, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        System.out.println("beneficiaryId = " + beneficiaryId);
        try {
            if (beneficiaryId != null) {
                //get Applicant Infos 
                Beneficiary beneficiary = this.beneficiaryService.getBeneficiary(beneficiaryId);
                String nid = beneficiary.getNid().toString();
                Calendar dob = beneficiary.getDateOfBirth();
                String dobStr = CalendarUtility.getDateString(dob.getTime(), "yyyy-MM-dd");

                JSONObject becJsonResult = null;

                long timeBefore = System.currentTimeMillis();
                ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.NIDVERIFICATION);
                //first login to SPBMU
                String token = loginSPBMU(serviceSettings);
                //if login successfull then verify NID 
                if (token != null) {
                    becJsonResult = verifyAtSPBMU(serviceSettings, dobStr, nid, token, 1);
                }

                BECResponse becResponse = getBECResponse(becJsonResult);
                long timeAfter = System.currentTimeMillis();
                System.out.println("Time diff = " + (timeAfter - timeBefore) + " ms");
                model.addAttribute("beneficiary", beneficiary);
                model.addAttribute("becResponse", becResponse);
                model.addAttribute("imagePath", FILE_CREATION_PATH);
                model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");
                model.addAttribute("headerTitle", "Test");

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "viewBeneficiaryNidComparisonData";
    }

    @RequestMapping(value = "/NIDcomparison/applicant/{applicantId}", method = RequestMethod.GET)
    public String getSystemAndBECInfoByApplicant(@PathVariable("applicantId") Integer applicantId, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        System.out.println("applicantId = " + applicantId);
        try {
            if (applicantId != null) {
                //get Applicant Infos 
                Applicant applicant = this.applicantService.getApplicant(applicantId);
                String nid = applicant.getNid().toString();
                Calendar dob = applicant.getDateOfBirth();
                String dobStr = CalendarUtility.getDateString(dob.getTime(), "yyyy-MM-dd");

                JSONObject becJsonResult = null;

                long timeBefore = System.currentTimeMillis();
                ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.NIDVERIFICATION);
                //first login to SPBMU
                String token = loginSPBMU(serviceSettings);
                //if login successfull then verify NID 
                if (token != null) {
                    becJsonResult = verifyAtSPBMU(serviceSettings, dobStr, nid, token, 1);
                }

                BECResponse becResponse = getBECResponse(becJsonResult);
                long timeAfter = System.currentTimeMillis();
                System.out.println("Time diff = " + (timeAfter - timeBefore) + " ms");
                model.addAttribute("applicant", applicant);
                model.addAttribute("becResponse", becResponse);
                model.addAttribute("imagePath", FILE_CREATION_PATH);
                model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");
                model.addAttribute("headerTitle", "Test");

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "viewApplicantNidComparisonData";
    }

//    //Nid Match info as json format 
//    @RequestMapping(value = "/getNidData/{nid}/{dob}", method = RequestMethod.GET)
//    public @ResponseBody
//    BECResponse getNidData(@PathVariable("nid") String nid, @PathVariable("dob") String dob, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
//        System.out.println("NID = " + nid);
//        try {
//
//            int spbmu = 0;
//            if (spbmu == 1) {
//                JSONObject becJsonResult = null;
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                Date date = sdf.parse(dob);
//                Calendar dobCal = sdf.getCalendar();
//                String dobStr = CalendarUtility.getDateString(dobCal.getTime(), "yyyy-MM-dd");
//
//                ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.NIDVERIFICATION);
//                //first login to SPBMU
//                String token = loginSPBMU(serviceSettings);
//                //if login successfull then verify NID 
//                if (token != null) {
//                    becJsonResult = verifyAtSPBMU(serviceSettings, dobStr, nid, token, 1);
//                    BECResponse becResponse = getBECResponse(becJsonResult);
//                    System.out.println("bec response = " + becJsonResult.get("matchFound"));
////                Gson gson = new Gson();
////                String json = gson.toJson(becJsonResult);
//                    return becResponse;
//                }
//                return null;
//            } else {
//                ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.NIDIBAS);
//                JSONObject becJsonResult = null;
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                Date date = sdf.parse(dob);
//                Calendar dobCal = sdf.getCalendar();
//                String dobStr = CalendarUtility.getDateString(dobCal.getTime(), "yyyy-MM-dd");
//                ResponseEntity responseResult = verifyAtIbas(serviceSettings.getServiceURL(), dobStr, nid);
//
//                BECResponse becResponse = getBECResponseFromIbas(responseResult);
//                return becResponse;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//            // throw e;
//        }
//    }
    //Nid Match info as json format 
    @RequestMapping(value = "/getNidData/{nid}/{dob}", method = RequestMethod.GET)
    public @ResponseBody
    BECResponse getNidData(@PathVariable("nid") String nid, @PathVariable("dob") String dob, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        System.out.println("NID = " + nid);
        try {

            JSONObject becJsonResult = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf.parse(dob);
            Calendar dobCal = sdf.getCalendar();
            String dobStr = CalendarUtility.getDateString(dobCal.getTime(), "yyyy-MM-dd");

            becJsonResult = getNidDataFromPython(dobStr, nid);

            Gson g = new GsonBuilder().registerTypeAdapter(Integer.class, new com.wfp.lmmis.training.controller.IntegerTypeAdapter()).create();
            NIDResponseFromEC dResponseFromEC = g.fromJson(becJsonResult.toString(), NIDResponseFromEC.class);
            BECResponse becResponse = getBECResponseEC(dResponseFromEC);

            return becResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
            // throw e;
        }
    }

    @RequestMapping(value = "/is-exist-other-mis/{nid}/{dob}", method = RequestMethod.GET)
    @ResponseBody
    public String isExistOtherMIS(@PathVariable("nid") String nid,
            @PathVariable("dob") String dob,
            HttpServletRequest request, HttpServletResponse response
    ) throws Exception {
        JsonResult jr = applicantService.isExistOtherMis(nid);
        return jr.toJsonString();
    }

    public JSONObject getNidDataFromPython(String dob, String nid) throws Exception {
        System.out.println("Get NID FROM PYTHON");
        String python_url = "http://43.229.15.117/api/nid/";//environment.getRequiredProperty("spring.url");
        URL url = new URL(python_url);
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        System.out.println("url = " + url.toString());
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);
        con.setDoInput(true);
        System.out.println("Request Method" + con.getRequestMethod());
        JSONObject json = new JSONObject();
        json.put("nid", nid);
        json.put("dob", dob);

        // Send post request (login)
        try {
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream(), "utf-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
//            System.out.println("op2 result = " + jsonResponse);

                return jsonResponse;

            } else {
                System.out.println("GET request error : " + responseCode);
                //should be false
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param becJsonResult
     * @return
     * @throws JSONException
     */
    public BECResponse getBECResponseEC(NIDResponseFromEC becJsonResult) throws JSONException {
        BECResponse bECResponse = new BECResponse();
        String language = LocaleContextHolder.getLocale().getLanguage();
        if (becJsonResult.getStatusCode().equals("SUCCESS")) {
            System.out.println("SUCCESS --------------------------");
            bECResponse.setMatchFound(true);
            bECResponse.setOperationResult(true);
            BECNidResponse nidData = new BECNidResponse();

            nidData.setName(becJsonResult.getSuccess().getData().getName());
            nidData.setNameEn(becJsonResult.getSuccess().getData().getNameEn());
            nidData.setNid(becJsonResult.getSuccess().getData().getNationalId());
            if (becJsonResult.getSuccess().getData().getNationalId() != null && becJsonResult.getSuccess().getData().getNationalId().length() == 10) {
                //nidData.setNid10(becJsonResult.getSuccess().getData().getNationalId());
            }
            if (becJsonResult.getSuccess().getData().getNationalId() != null && becJsonResult.getSuccess().getData().getNationalId().length() == 17) {
                //  nidData.setNid17(becJsonResult.getSuccess().getData().getNationalId());
            }
            nidData.setDob(becJsonResult.getSuccess().getData().getDateOfBirth());
            nidData.setGender(becJsonResult.getSuccess().getData().getGender());
            nidData.setPhoto(becJsonResult.getSuccess().getData().getPhotoBase64());
            nidData.setMother(becJsonResult.getSuccess().getData().getMother());
            nidData.setFather(becJsonResult.getSuccess().getData().getFather());

            nidData.setSpouse(becJsonResult.getSuccess().getData().getSpouse());
            // nidData.setPresentAddress(nidJSON.get("presentAddress").toString());
            // nidData.setPermanentAddress(nidJSON.get("permanentAddress").toString());

            bECResponse.setNidData(nidData);

        } else {
            bECResponse.setMatchFound(false);
            bECResponse.setErrorMsg(language == "en" ? "NID information Not found" : "জাতীয়পরিচয় পত্রের তথ্য খুঁজে পাওয়া যায়নি। পুনরায় চেষ্টা করুন। ");
            bECResponse.setErrorCode("400");

            System.out.println("match not found");
        }
        return bECResponse;
    }

}
