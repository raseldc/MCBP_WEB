/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wfp.lmmis.selection.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wfp.lmmis.applicant.converter.ApplicantConverter;
import com.wfp.lmmis.applicant.detail.ApplicantAncInformationDetail;
import com.wfp.lmmis.applicant.detail.ApplicantDetail;
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantAncInformation;
import com.wfp.lmmis.applicant.model.BECResponse;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.JsonResult;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.hibernate.criterion.Projections.id;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Shamiul Islam at Anunad Solution
 */
@Controller
public class AncVerificationController {

    @Autowired
    private ApplicantService applicantService;

    @RequestMapping(value = "/anc-comparison/{id}", method = RequestMethod.GET)
    public @ResponseBody
    AncVerificationRespose getAncData(@PathVariable("id") int applicantId, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Applicant applicant = applicantService.getApplicant(applicantId);
        ApplicantDetail applicantDetail = ApplicantConverter.getDetail(applicant);

        try {
            JSONObject becJsonResult = null;
            String dobStr = CalendarUtility.getDateString(applicant.getDateOfBirth().getTime(), "yyyy-MM-dd");

            becJsonResult = getAncCardInfromation(dobStr, applicant.getNid().toString());
            Gson g = new GsonBuilder().registerTypeAdapter(Integer.class, new com.wfp.lmmis.training.controller.IntegerTypeAdapter()).create();
            ApplicantAncInformationDetail applicantAncInformationDetail = g.fromJson(becJsonResult.toString(), ApplicantAncInformationDetail.class);
            //for testing
//            applicantAncInformationDetail = new ApplicantAncInformationDetail();
//            applicantAncInformationDetail.setFatherName("Test");
//            applicantAncInformationDetail.setMotherName("Test");
//            applicantAncInformationDetail.setName("Test");
//            applicantAncInformationDetail.setDob("1987-03-20");
//            applicantAncInformationDetail.setHusbandName("Test");
//            applicantAncInformationDetail.setAnc1("1");
//            applicantAncInformationDetail.setAnc2("1");
//            applicantAncInformationDetail.setAnc3("1");
//            applicantAncInformationDetail.setAnc4("1");
//            applicantAncInformationDetail.setNid(applicant.getNid().toString());
//
//            applicantAncInformationDetail.setPregnancyWeek("12");
            applicantAncInformationDetail.setNid(applicant.getNid().toString());
            AncVerificationRespose ancVerificationRespose = new AncVerificationRespose();
            ancVerificationRespose.setAncInformationDetail(applicantAncInformationDetail);
            ancVerificationRespose.setApplicantDetail(applicantDetail);
            ancVerificationRespose.setStatus(1);
            ancVerificationRespose.setMsg("ok");

            return ancVerificationRespose;

        } catch (Exception e) {
            AncVerificationRespose ancVerificationRespose = new AncVerificationRespose();
            ancVerificationRespose.setApplicantDetail(applicantDetail);
            ancVerificationRespose.setStatus(4);
            ancVerificationRespose.setMsg("Network Error");
            e.printStackTrace();
            return ancVerificationRespose;
            // throw e;
        }
    }

    public JSONObject getAncCardInfromation(String dob, String nid) throws Exception {
        System.out.println("Get Infromation FROM anc");
        String python_url = "http://mamoni.net:8080/rhis_fwc_monitoring/pregwomendata?zilla=4&nid=5512452433";//environment.getRequiredProperty("spring.url");
        URL url = new URL(python_url);
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        System.out.println("url = " + url.toString());
        con.setRequestMethod("GET");
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept-Charset", "UTF-8");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
////        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
////        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//        con.addRequestProperty("User-Agent", "Mozilla/5.0"); // add this line to your code
//        con.setDoOutput(true);
//        con.setDoInput(true);
//        con.setInstanceFollowRedirects(false);
//        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        con.setRequestProperty("charset", "utf-8");
        System.out.println("Request Method" + con.getRequestMethod());
//        JSONObject json = new JSONObject();
//        json.put("nid", nid);
//        json.put("dob", dob);

        // Send post request (login)
        try {
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
////            wr.writeBytes(json.toString());
//            wr.flush();
//            wr.close();
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

                return jsonResponse;

            } else {

                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/anc-update", method = RequestMethod.POST)
    @ResponseBody
    public String applicantMakeWatingSave(@RequestBody AncVerificationRespose ancVerificationRespose, HttpServletRequest request, HttpSession session) throws IOException {

        UserDetail loggedUser = (UserDetail) session.getAttribute("userDetail");

        if (loggedUser == null) {
            return null;
        }
        ancVerificationRespose.setUserId(loggedUser.getId());
        applicantService.updateApplicantAncStatus(ancVerificationRespose);
        JsonResult jr = new JsonResult(false, "ok");
        return jr.toJsonString();
    }

}
