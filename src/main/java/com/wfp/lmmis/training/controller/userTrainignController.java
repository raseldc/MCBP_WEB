/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.controller;

import com.wfp.lmmis.enums.ServiceType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.model.ServiceSettings;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.service.UserService;
import com.wfp.lmmis.service.ServiceSettingsService;
import com.wfp.lmmis.training.model.Training;
import com.wfp.lmmis.training.model.TrainingType;
import com.wfp.lmmis.training.model.UserTraining;
import com.wfp.lmmis.training.service.TrainingService;
import com.wfp.lmmis.training.service.TrainingTypeService;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.UserTrainingView;
import com.wfp.lmmis.utility.UserViewForTraining;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Controller
public class userTrainignController {

    @Autowired
    private DivisionService divisionService;
    @Autowired
    private TrainingTypeService trainingTypeService;
    @Autowired
    private TrainingService trainingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ServiceSettingsService serviceSettingsService;

    private static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImNjODE1NjdlOTZmZTM1OGU3MDVlM2U0MWEwNjgyYjQ1NjgzZTVmY2Y2YTQ1Y2I5NDQ3NGIyMjE1ZmNhMWE0MjRkZjIyNWExMzEwZGE1ZDE5In0.eyJhdWQiOiI1IiwianRpIjoiY2M4MTU2N2U5NmZlMzU4ZTcwNWUzZTQxYTA2ODJiNDU2ODNlNWZjZjZhNDVjYjk0NDc0YjIyMTVmY2ExYTQyNGRmMjI1YTEzMTBkYTVkMTkiLCJpYXQiOjE2NDc0MTkxNzYsIm5iZiI6MTY0NzQxOTE3NiwiZXhwIjoxNjc4OTU1MTc2LCJzdWIiOiIyNzIxNjgyIiwic2NvcGVzIjpbXX0.k0TO4JAxGjre-dM84mqouTV3zKHKr5ld1wEe1cYYQtZQ_hUQzgKK-HbYJlcai66EVbmJ7tyowMxtuKqYwQOTJ373y6AW92gSI7AW1cCO-sEQWADON04GPvTYvNdH430d241r8bfUISjoZ-r96Zl3tWesQTUDXjl2mRzDpT-zx45U1orRWjwSDQxaiwDpO4e5X8KdwfMwpDsOR2DGGoMepgoakoMbLkGyOyE6f-E47R1raSFDFHbWqPhDwVQESHn1df4207eD8LSL3r8LHujt-Emegsgx7-X67BF46zFqZ5PGcEcETcY9K-LzT6bZwxgHjhc8fGhWWLx8SL4Z6CCYZ5GI8_vrEJcuoDYH2fOFlNen-TTDmHw1pMNBV3sUMr9ybDFU84m1vyGL0HImeMGcvStWffcqDqlNxIZ5AWf9LdArtvRNl9lWtE-RQ9sISut2qcy_666BDWjaWtVyhOMnngrgifHMT1xw1rhOlx2uIkMKBOn1oNTAEtjxG_3qU9pR47_iwAZt-qeeDma51-rJDHyzrYf7JpOZQNMmxF3UD5Nz_lNNYayijEbrEoDYhNOja6xRHzBfA7JVc0tKFAgyuTJ1A6bTm-5jZh8fnEImaDcJZaZZfmxyTGjtutQ8YJlZNV5s8zTso9TUOOPHXhJASt55X_Ms6T1K92Jt0XzgcPk";//loginMuktoPath();

    private void mapTrainingTypeName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<TrainingType> trainingTypelList = this.trainingTypeService.getTrainingTypeList(true);
        for (TrainingType t : trainingTypelList) {
            t.setNameInEnglish(t.getNameInEnglish() + (t.isBeneficiaryIncluded() == true ? " (~)" : ""));
            t.setNameInBangla(t.getNameInBangla() + (t.isBeneficiaryIncluded() == true ? " (~)" : ""));
        }
        model.addAttribute("trainingTypeList", trainingTypelList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("trainingTypeName", "nameInEnglish");
        } else {
            model.addAttribute("trainingTypeName", "nameInBangla");
        }

    }

    @RequestMapping(value = "/user/training/list", method = RequestMethod.GET)
    public String showTrainerList(Model model, HttpServletRequest request) throws ExceptionWrapper {
        try {
            CommonUtility.mapSchemeName(model);
            CommonUtility.mapAllFiscalYearName(model);
            mapTrainingTypeName(model);
            mapDivisionName(model);
            model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
            //  model.addAttribute("trainingList", this.trainingService.getTrainingList());
            //  String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI2IiwianRpIjoiODBmZjMwODUyMWMwYTI3YTdkNzgyMTAzMGRlM2YzNTZlMmVhNmE1OTFhZDAzMTVjY2VkMWQxNWZjMmNlZGM0MjA4NzQ2NjcxYTYxOGM0MTciLCJpYXQiOjE2MDg0ODg5NTYsIm5iZiI6MTYwODQ4ODk1NiwiZXhwIjoxNjQwMDI0OTU2LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.mpfqMAihQ-S4P8S1xVvoyAt1iIyDhhULGVeTPMaTjfUFQZ0GYISn5PVtxIl4HKlEHLhCHIAIxSMIZ8aVa5cPoyzsf_zgqQjlu8L12i2zDy2ouSuEm2dgUNR0rSA9mSEPuZa2T9HXUYHBuW4HrtFGLKjdEB9fs2GNRZFtdSsuv5SSU8hjXPFyWiWMiFCS7z4uHP76zK7HQlqQZkWl3SaSh-xfvGM6GnwwPqcOOjGPW4WPxTAdwPNsTCG-sx61Usd-40arGRPMoohzQ5sKUQ35ZA68iBPKXIrGNn06U5BVs_M_Pwh1SSjDOWd-eqQhW83fGaJGO5jtYIZaAi2deKXM2uhzcSth7JVvB1eZ2T_DVEKIAunL8oDu5OTbTtbt2hbbqgpu_9oNDNFEavSoBDsQFfJETK-t31ge3hcSCOwmuGXJkyr64dWuAuBxBwMU9632URIrT8FGZde3LMk07uMjQIH2RUlR2ZvcAe32BsHV3agEGGp4UeYs7LllZk09lVVYxG5R0Yrh6qyUBHZsTdY1DZWujKyGLcJAPZsl6QVqnWDmRsbYIjLV8WZENjF2Ib0cuLvES5c2dxBIRdvL8s2cTOhuEYyxP7ITcUwZC4P9yQA2z2g5gnq0Tc6fjz1BzLSdhVg431LzwnDaHntR5bB77QPy2_Oz9nlq_0A3HOQrlnw";//loginMuktoPath();
            List<UserTrainingCourse> courses = getCourseList(token);
            model.addAttribute("trainingList", courses);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //   model.addAttribute("trainerList", this.trainerService.getTrainerList());
        return "trainingListUser";

//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("/trainingUser/trainingList");
//        return modelAndView;
    }

    public void mapGlobalVariables(Model m) {
        m.addAttribute("dateFormat", "dd-mm-yy");
    }

    /**
     *
     * @param training
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/training/create", method = RequestMethod.GET)
    public String createTraining(@ModelAttribute Training training, Model model, HttpServletRequest request) {

        mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapAllFiscalYearName(model);
        mapTrainingTypeName(model);
        // mapTrainerName(model);
        mapDivisionName(model);
        model.addAttribute("actionType", "create");
//        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));

        UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");
        if (loggedUser.getDivision() != null) {
            training.setDivisionAvailable(true);
            training.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDistrict() != null) {
            training.setDistrictAvailable(true);
            training.setDistrict(loggedUser.getDistrict());
        }
        if (loggedUser.getUpazila() != null) {
            training.setUpazilaAvailable(true);
            training.setUpazilla(loggedUser.getUpazila());
        }
        model.addAttribute("training", training);
        List<UserTrainingCourse> courses = getCourseList(token);
        model.addAttribute("trainingList", courses);
        model.addAttribute("courseId", 0);
        model.addAttribute("batchId", 0);
        model.addAttribute("fiscalYearId", 0);
        return "trainingUser";
    }

    private void mapDivisionName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<Division> divisionList = this.divisionService.getDivisionList();
        model.addAttribute("divisionList", divisionList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("divisionName", "nameInEnglish");
        } else {
            model.addAttribute("divisionName", "nameInBangla");
        }
    }

    private String loginMuktoPath() throws Exception {

        ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.MUKTOPATH);

//         URL url = new URL("http://api.muktopaathdemo.orangebd.com/oauth/token");
        URL url = new URL(serviceSettings.getServiceURL() + "/oauth/token");
      //  HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        HttpsURLConnection con = getHttpsURLConnection(serviceSettings.getServiceURL() + "/oauth/token", "POST");
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//        con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:98.0) Gecko/20100101 Firefox/98.0"); // add this line to your code
//        con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
//        con.setRequestProperty("Connection", "keep-alive");
//        con.setDoOutput(true);
//        con.setDoInput(true);

        JSONObject json = new JSONObject();
        json.put("username", "dwamuktopaath@gmail.com");
        json.put("password", "12345678");
        json.put("client_secret", "MgKcfVGAbPh8JRyfkOVPMLDZ4LqbtXqRYJWyt6WU");
        json.put("client_id", "5");
        json.put("grant_type", "password");

        // Send post request (login)
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(json.toString());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                response.append(inputLine);
            }
            System.out.println(response);
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println("op result = " + jsonResponse);

            if (jsonResponse.has("access_token")) {
                token = jsonResponse.getString("access_token");
                return jsonResponse.getString("access_token");
            }

            System.out.println("res = " + jsonResponse.get("access_token").toString());
            return null;

        } else {
            System.out.println("POST request error : " + responseCode);
            return null;
        }

    }

    private HttpsURLConnection getHttpsURLConnection(String url_str, String postType) throws NoSuchAlgorithmException, KeyManagementException {
        try {
//            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
//            TrustManager[] trust_mgr = new TrustManager[]{
//                new X509TrustManager() {
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
//
//                    public void checkClientTrusted(X509Certificate[] certs, String t) {
//                    }
//
//                    public void checkServerTrusted(X509Certificate[] certs, String t) {
//                    }
//                }
//            };
//            ssl_ctx.init(null, // key manager
//                    trust_mgr, // trust manager
//                    new SecureRandom()); // random number generator
//            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
            String token_ = token;
            URL url = new URL(url_str);

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setRequestMethod(postType);
            con.setRequestProperty("Authorization", "Bearer " + token_);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0"); // add this line to your code

            //  con.setRequestProperty("Accept", "*/*");
            con.setDoOutput(true);
            con.setDoInput(true);
            return con;
        } catch (IOException ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private HttpURLConnection getHttpURLConnection(String url_str, String postType) throws NoSuchAlgorithmException, KeyManagementException {
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
            String token_ = token;
            URL url = new URL(url_str);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod(postType);
            con.setRequestProperty("Authorization", "Bearer " + token_);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0"); // add this line to your code

            //  con.setRequestProperty("Accept", "*/*");
            con.setDoOutput(true);
            con.setDoInput(true);
            return con;
        } catch (IOException ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<UserTrainingCourse> getCourseList(String token) {
        try {

//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .build();
//            Request request = new Request.Builder()
//                    .url("https://api.muktopaath.gov.bd/api/partner/course/list")
//                    .method("GET", null)
//                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjgxMDgyN2ZmNDY2ZjJmNzdkOTUzZGRjNzE1ZDYwOTcyNzg2Y2QwNTk5YjY4MGYxYTdmOWI2YmI3ZGQ4MWM5ZDc0Mzg1NWE0YWExMzc1YmM1In0.eyJhdWQiOiI1IiwianRpIjoiODEwODI3ZmY0NjZmMmY3N2Q5NTNkZGM3MTVkNjA5NzI3ODZjZDA1OTliNjgwZjFhN2Y5YjZiYjdkZDgxYzlkNzQzODU1YTRhYTEzNzViYzUiLCJpYXQiOjE2NDYyMTMxODgsIm5iZiI6MTY0NjIxMzE4OCwiZXhwIjoxNjc3NzQ5MTg4LCJzdWIiOiIyNzIxNjgyIiwic2NvcGVzIjpbXX0.uXc2dxtfT2InlqFhPasFET1v1IFsiCycQvZ9atq2HXpj9yjA8qfWDgt1q6xzibdhbcVyNPEVBjWb2yrcLlCgybqfOeKcssrK9ypxuGNoh7aj2TYENUJbdRWktbJxyk5QS6Tt3DxIDcs-AUvJBIHTvwlq9lzG_DstCVKFX3uzl_pB3JK0LtKG1984FkFM2bBlEdkVFQZDFdIl2c4yXZc_nzJidZJB8HCHqKwxbsfN2nDF6ZdqNVGnKUzlM5j7Zz-9l5kx67pKnfCk9-9xC3bcmO6bC-xDQm7N_hXOEMpbHoxWEOtszZD2C0dEVdPCxCnRHWVzGpstvupvpo_iEoFfSrb8wAXmVdeBFgYPmB1iPSiPtaGFTY_KXuqyE9BzzkxTBkf2c-fBasRwUrteImOUCf83llBkID-iDk220WshCsfVEsQ1AY_MQp5UA3JOtXtCTqOpQlG0wzZk1PWTUHxIbdZ55-8BStBLdYxFBkLVc_HDNv3iQCCk0RgNlJRKZnl31-yQ1TtvzqclDf6AWzU2Q1estYVPFeZ_H4G2a3NMY46o3bRf97zYJZ7qHwgjdLECjKkRF9-P_MEymBSqQrwzWSqBoH4FNJYJoUhfhlYFMrzwKxwEQqwutnpN3ed9JXlmoIPK_wtmJfcu-aamx8w7_rfZ2ycZlER8Jw7DjQR9D1s")
//                    .build();
//            okhttp3.Response respons = client.newCall(request).execute();
            List<UserTrainingCourse> courses = new ArrayList<>();
            int i = 1;
            int try_count = 1;
            String token_ = token;
            ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.MUKTOPATH);
            while (i == 1 && try_count < 3) {
//                String setProperty = System.setProperty("https.protocols", "TLSv1.2");
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                headers.set("Authorization", "Bearer " + token_);
//
//                HttpEntity<String> entity = new HttpEntity<String>("body", headers);
//                String url = serviceSettings.getServiceURL() + "/api/partner/course/list";
//                RestTemplate restTemplate = new RestTemplate();
//                Object result12 = restTemplate.exchange(url, HttpMethod.GET,
//                        entity, String.class);

                HttpsURLConnection con = getHttpsURLConnection(serviceSettings.getServiceURL() + "/api/partner/course/list", "GET");

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED || responseCode == 500) {
                    token_ = loginMuktoPath();
                    try_count++;
                    continue;
                }
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
                    String data = jsonResponse.getString("data");

                    ObjectMapper mapper = new ObjectMapper();

                    UserTrainingCourse[] pp1 = mapper.readValue(data, UserTrainingCourse[].class);
                    courses = Arrays.asList(mapper.readValue(data, UserTrainingCourse[].class));
                    i++;
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String strCurrentLine;
                    while ((strCurrentLine = br.readLine()) != null) {
                        System.out.println(strCurrentLine);
                    }
                    System.out.println("POST request error : " + responseCode);

                    return null;
                }
            }
            return courses;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private List<Batch> getBachListByCourseId(int courserId, String token) throws Exception {

        ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.MUKTOPATH);

        HttpURLConnection con = getHttpURLConnection(serviceSettings.getServiceURL() + "/api/partner/course/" + courserId + "/batches", "GET");

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "utf-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            String data = response.toString();
            String v1 = data.replace("{\"data\":", "");
            v1 = v1.substring(0, v1.length() - 1);

            ObjectMapper mapper = new ObjectMapper();

            List<Batch> batch = Arrays.asList(mapper.readValue(v1, Batch[].class));

            return batch;

        } else {
            System.out.println("POST request error : " + responseCode);

            return null;
        }
    }

    @RequestMapping(value = "/get-batch-by-course", method = RequestMethod.GET)
    @ResponseBody
    public String getAllBatchByCourse(@RequestParam(value = "courseId") int courseId,
            @RequestParam(value = "fiscalYearId") int fiscalYearId,
            @RequestParam(value = "isCreate", required = false) int isCreate) {
        JsonResult jr = new JsonResult(false, "ok");
        List<Batch> batchs = new ArrayList<>();
        List<Batch> batchsToView = new ArrayList<>();
        try {
            batchs = getBachListByCourseId(courseId, token);

            if (isCreate == 1) {
                List<Batch> brachAlreadyCreated = trainingService.getCreatedBranchForCourse(courseId, fiscalYearId);
                List<Integer> btachId = brachAlreadyCreated.stream().map(i -> i.getId()).collect(Collectors.toList());
                for (Batch b : batchs) {
                    if (!btachId.contains(b.getId())) {
                        batchsToView.add(b);
                    }
                }
            } else {
                batchsToView = batchs;
            }
        } catch (Exception ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
        }
        jr.setReturnObject(batchsToView);
        return jr.toJsonString();
    }

    /**
     *
     * @param courseId
     * @param fiscalYearId
     * @return
     */
    @RequestMapping(value = "/get-batch-by-course-already-create", method = RequestMethod.GET)
    @ResponseBody
    public String getAllBatchByCourseAlreadyCreate(@RequestParam(value = "courseId") int courseId, @RequestParam(value = "fiscalYearId") int fiscalYearId) {
        JsonResult jr = new JsonResult(false, "ok");
        List<Batch> batchsToView = new ArrayList<>();
        try {
            batchsToView = trainingService.getCreatedBranchForCourse(courseId, fiscalYearId);

        } catch (Exception ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
        }
        jr.setReturnObject(batchsToView);
        return jr.toJsonString();
    }

    @RequestMapping(value = "/usre/training/get-all-user")
    @ResponseBody
    public String getAllUser(@RequestParam(value = "courseId") int courseId,
            @RequestParam(value = "batchId") int batchId,
            @RequestParam(value = "fiscalYearId") int fiscalYearId,
            HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {

        List<UserViewForTraining> userList = new ArrayList<>();

        userList = userService.getUserListForTraining(courseId, batchId, fiscalYearId);

        String jsonArray = "";
        String startIndexSt = request.getParameter("start");
        int startPageIndex = startIndexSt != null ? Integer.parseInt(startIndexSt) : 0;
        String recordsPerPageSt = request.getParameter("length");
        int recordsPerPage = recordsPerPageSt != null ? Integer.parseInt(recordsPerPageSt) : Integer.MAX_VALUE;
        if (recordsPerPage == -1) {
            recordsPerPage = Integer.MAX_VALUE;
        }
        String draw = (request.getParameter("draw"));
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").setPrettyPrinting().create();
        jsonArray = gson.toJson(userList);
        jsonArray = "{ \"draw\": " + draw + ",\"recordsTotal\": " + userList.size() + ",\"recordsFiltered\": " + userList.size() + ",\"data\":" + jsonArray + "}";

        return jsonArray;

    }

    @RequestMapping(value = "/user-training-enrollment", method = RequestMethod.POST)
    @ResponseBody
    public String enrollmentCourse(@RequestBody EnrollmentInfo enrollmentInfo, HttpServletRequest request) {
        JsonResult jr = new JsonResult(false, "ok");
        jr.setErrorCode(200);
        List<Batch> batchs = new ArrayList<>();
        try {
            List<UserViewForTraining> userViewForTrainings = userService.getUserInfoForEnrollment(enrollmentInfo.getUserIds());
//            UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");
//            trainingService.createUserTrainingAndEnrollment(enrollmentInfo, loggedUser);
            List<UserViewForTraining> userViewForTrainingsToAdd = new ArrayList<>();
            List<Integer> userIdsTOAdd = new ArrayList<>();
            String emailIdNotFoundName = "";
            for (UserViewForTraining userViewForTraining : userViewForTrainings) {
                if (userViewForTraining.getEmail().equals("") || userViewForTraining.getEmail() == null) {
                    emailIdNotFoundName = emailIdNotFoundName + userViewForTraining.getName() + "; ";

                } else {
                    userViewForTraining.setPhone("");
                    userViewForTrainingsToAdd.add(userViewForTraining);
                    userIdsTOAdd.add(userViewForTraining.getId());
                    userViewForTraining.setId(null);
                }
                userViewForTraining.setId(null);
            }
            if (!emailIdNotFoundName.equals("")) {
                jr.setErrorCode(202);
                jr.setErrorMsg(emailIdNotFoundName);
            }

            if (enrollment(userViewForTrainingsToAdd, enrollmentInfo.getBatchId())) {
                enrollmentInfo.setUserIds(userIdsTOAdd);
                UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");
                trainingService.createUserTrainingAndEnrollment(enrollmentInfo, loggedUser);
            } else {
                jr.setErrorCode(500);
                jr.setErrorMsg("");
            }

        } catch (Exception ex) {
            jr.setErrorCode(400);
            jr.setErrorMsg("Exp Occured");
            ex.printStackTrace();
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
        }
        jr.setReturnObject(batchs);
        return jr.toJsonString();
    }
//http://muktopaathdemo.orangebd.com/restricted-course/8484848

    private boolean enrollment(List<UserViewForTraining> userViewForTrainings, int batchId) throws Exception {

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String jsonArray = gson.toJson(userViewForTrainings);

        ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.MUKTOPATH);
        HttpURLConnection con = getHttpURLConnection(serviceSettings.getServiceURL() + "/api/partner/course/enrollment/" + batchId, "POST");

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        wr.write(jsonArray.getBytes("UTF8"));
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

            if (jsonResponse.has("success")) {
                return true;//jsonResponse.getString("success");
            } else {
                return false;
            }

        } else {
            System.out.println("POST request error : " + responseCode);
            return false;
        }
    }

    @RequestMapping(value = "/usre/training/get-all-training")
    @ResponseBody
    public String getAllTraining(@RequestParam(value = "courseId") int courseId,
            @RequestParam(value = "batchId") int batchId,
            @RequestParam(value = "fiscalYearId") int fiscalYearId,
            HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {

        List<UserTrainingView> userList = new ArrayList<>();

        userList = trainingService.getAllUserTraining(courseId, batchId, fiscalYearId);

        String jsonArray = "";
        String startIndexSt = request.getParameter("start");
        int startPageIndex = startIndexSt != null ? Integer.parseInt(startIndexSt) : 0;
        String recordsPerPageSt = request.getParameter("length");
        int recordsPerPage = recordsPerPageSt != null ? Integer.parseInt(recordsPerPageSt) : Integer.MAX_VALUE;
        if (recordsPerPage == -1) {
            recordsPerPage = Integer.MAX_VALUE;
        }
        String draw = (request.getParameter("draw"));
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").setPrettyPrinting().create();
        jsonArray = gson.toJson(userList);
        jsonArray = "{ \"draw\": " + draw + ",\"recordsTotal\": " + userList.size() + ",\"recordsFiltered\": " + userList.size() + ",\"data\":" + jsonArray + "}";

        return jsonArray;

    }

    @RequestMapping(value = "/user/training/edit", method = RequestMethod.GET)
    public String editTraining(@ModelAttribute Training training, Model model,
            @RequestParam(value = "courseId") int courseId,
            @RequestParam(value = "batchId") int batchId,
            @RequestParam(value = "fiscalYearId") int fiscalYearId,
            HttpServletRequest request) {
        UserTraining userTraining = trainingService.getUserTraining(courseId, batchId, fiscalYearId);
        mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapAllFiscalYearName(model);
        mapTrainingTypeName(model);
        // mapTrainerName(model);
        mapDivisionName(model);
        model.addAttribute("actionType", "create");
//        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));       
        model.addAttribute("training", training);
        List<UserTrainingCourse> courses = getCourseList(token);
        model.addAttribute("trainingList", courses);

        model.addAttribute("courseId", courseId);
        model.addAttribute("batchId", batchId);
        model.addAttribute("courseName", userTraining.getCourseName());
        model.addAttribute("batchName", userTraining.getBatchName());
        model.addAttribute("fiscalYearId", fiscalYearId);

        model.addAttribute("fiscalYearName", userTraining.getFiscalYear().getNameInBangla());
        return "trainingUser";
    }

    @RequestMapping(value = "/user/training/applicant-details", method = RequestMethod.GET)
    public String trainingApplicantDetails(@ModelAttribute Training training, Model model,
            HttpServletRequest request) {

        mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapAllFiscalYearName(model);
        mapTrainingTypeName(model);
        // mapTrainerName(model);
        mapDivisionName(model);
        model.addAttribute("actionType", "create");
//        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));       
        model.addAttribute("training", training);
        List<UserTrainingCourse> courses = getCourseList(token);
        model.addAttribute("trainingList", courses);

        return "trainingApplicantDetails";
    }

    @RequestMapping(value = "/usre/training/get-user-list")
    @ResponseBody
    public String getUserListForTraining(@RequestParam(value = "courseId") int courseId, @RequestParam(value = "batchId") int batchId,
            HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {

        List<UserViewForTraining> userList = new ArrayList<>();

        userList = userService.getTrainingUserList(courseId, batchId);

        String jsonArray = "";
        String startIndexSt = request.getParameter("start");
        int startPageIndex = startIndexSt != null ? Integer.parseInt(startIndexSt) : 0;
        String recordsPerPageSt = request.getParameter("length");
        int recordsPerPage = recordsPerPageSt != null ? Integer.parseInt(recordsPerPageSt) : Integer.MAX_VALUE;
        if (recordsPerPage == -1) {
            recordsPerPage = Integer.MAX_VALUE;
        }
        String draw = (request.getParameter("draw"));
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").setPrettyPrinting().create();
        jsonArray = gson.toJson(userList);
        jsonArray = "{ \"draw\": " + draw + ",\"recordsTotal\": " + userList.size() + ",\"recordsFiltered\": " + userList.size() + ",\"data\":" + jsonArray + "}";

        return jsonArray;

    }

    @RequestMapping(value = "/user/training/own-training", method = RequestMethod.GET)
    public String ownTrainingReport(@ModelAttribute Training training, Model model,
            HttpServletRequest request) {
        UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");

        List<UserTrainingCourse> courses = trainingService.getCourseListByUserId(loggedUser.getUserId());
        mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapAllFiscalYearName(model);
        mapTrainingTypeName(model);
        // mapTrainerName(model);
        mapDivisionName(model);
        model.addAttribute("actionType", "create");
//        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));       
        model.addAttribute("training", training);

        model.addAttribute("trainingList", courses);

        return "ownTrainingReport";
    }

    @RequestMapping(value = "/get-batch-by-course-by-user", method = RequestMethod.GET)
    @ResponseBody
    public String getAllBatchByCourseAndUser(@RequestParam(value = "courseId") int courseId, HttpServletRequest request) {
        UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");

        JsonResult jr = new JsonResult(false, "ok");
        List<Batch> batchs = new ArrayList<>();
        try {
            batchs = trainingService.getBatchListByCourseAndUserId(loggedUser.getUserId(), courseId);
        } catch (Exception ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
        }
        jr.setReturnObject(batchs);
        return jr.toJsonString();
    }

    @RequestMapping(value = "/usre/training/get-training-list-by-user")
    @ResponseBody
    public String getTraingListByUserId(@RequestParam(value = "courseId") int courseId, @RequestParam(value = "batchId") int batchId,
            @RequestParam(value = "fiscalYearId") int fiscalYearId,
            HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");

        List<UserViewForTraining> userList = new ArrayList<>();

        userList = trainingService.getAllTraingDetailsByUser(loggedUser.getUserId(), courseId, batchId, fiscalYearId);

        String jsonArray = "";
        String startIndexSt = request.getParameter("start");
        int startPageIndex = startIndexSt != null ? Integer.parseInt(startIndexSt) : 0;
        String recordsPerPageSt = request.getParameter("length");
        int recordsPerPage = recordsPerPageSt != null ? Integer.parseInt(recordsPerPageSt) : Integer.MAX_VALUE;
        if (recordsPerPage == -1) {
            recordsPerPage = Integer.MAX_VALUE;
        }
        String draw = (request.getParameter("draw"));
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").setPrettyPrinting().create();
        jsonArray = gson.toJson(userList);
        jsonArray = "{ \"draw\": " + draw + ",\"recordsTotal\": " + userList.size() + ",\"recordsFiltered\": " + userList.size() + ",\"data\":" + jsonArray + "}";

        return jsonArray;

    }

    @RequestMapping(value = "/api/training/user-training-applicant-status-update", method = RequestMethod.POST)
    @ResponseBody
    public String userTrainingStatusUpdate(@RequestBody UserTrainingUpdateApi trainingUpdateApi, HttpServletRequest request) {
        System.out.println("Detail s Data");
        System.out.println(trainingUpdateApi);
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").setPrettyPrinting().create();
        System.out.println(gson.toJson(trainingUpdateApi));
        //trainingService.userTrainingAttendanceUpdate(trainingUpdateApi);
        UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");

        JsonResult jr = new JsonResult(false, "ok");
        List<Batch> batchs = new ArrayList<>();
        try {
            jr = trainingService.userTrainingAttendanceUpdate(trainingUpdateApi);
        } catch (Exception ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
            jr.setErrorMsg("Exception occure");
        }
        jr.setReturnObject(batchs);
        return jr.toJsonString();
    }

    @RequestMapping(value = "/applicant-get-certificate", method = RequestMethod.POST)
    @ResponseBody
    public String applicantGetCertificate(@RequestParam(value = "userTrainingAttenId") int userTrainingAttenId,
            @RequestParam(value = "isGetCertificate") int isGetCertificate) {
        JsonResult jr = new JsonResult(false, "ok");
        try {
            jr = trainingService.userTrainingAttendanceUpdateOnGetCertificate(userTrainingAttenId, isGetCertificate);
        } catch (Exception ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
            jr.setErrorMsg("Exception occure");
        }

        return jr.toJsonString();
    }

    @RequestMapping(value = "/applicant-zoom-meeting", method = RequestMethod.POST)
    @ResponseBody
    public String applicatnTraingInfoUpdate(@RequestParam(value = "userTrainingAttenId") int userTrainingAttenId,
            @RequestParam(value = "isZoomMeeting") int isZoomMeeting) {
        JsonResult jr = new JsonResult(false, "ok");
        try {
            jr = trainingService.userTrainingAttendanceUpdateOnZoomMeeting(userTrainingAttenId, isZoomMeeting);
        } catch (Exception ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
            jr.setErrorMsg("Exception occure");
        }

        return jr.toJsonString();
    }

    @RequestMapping(value = "/applicant-reamarks-save", method = RequestMethod.POST)
    @ResponseBody
    public String applicatnTraingInfoUpdateReamrks(@RequestParam(value = "userTrainingAttenId") int userTrainingAttenId,
            @RequestParam(value = "remarks") String remarks) {
        JsonResult jr = new JsonResult(false, "ok");
        try {
            jr = trainingService.userTrainingAttendanceUpdateOnRemarks(userTrainingAttenId, remarks);
        } catch (Exception ex) {
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
            jr.setErrorMsg("Exception occure");
        }

        return jr.toJsonString();
    }
}
