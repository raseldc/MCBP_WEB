/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.controller;

import com.wfp.lmmis.applicant.service.BeneficiaryPaymentService;
import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.beneficiary.model.BeneficiaryForm;
import com.wfp.lmmis.enums.AccountInformationChangeReason;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.PaymentInformationHistory;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.training.controller.userTrainignController;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.DoubleTypeAdapter;
import com.wfp.lmmis.utility.IntegerTypeAdapter;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Controller
public class BeneficiaryPaymentInformationController {

    //Beneficiary Payment information View and update
//    //private final org.apache.log4j.logger logger = org.apache.log4j.//logger.getlogger(BeneficiaryController.class);
    @Autowired
    private BeneficiaryPaymentService beneficiaryPaymentService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private CommonForBeneficiaryControllers commonForBeneficiaryControllers;

    @RequestMapping(value = "/beneficiary/paymentInformationView/{type}", method = RequestMethod.GET)
    public String beneficiaryPaymentInformationView(Model model, HttpServletRequest request, @PathVariable("type") String pageType) {
        String returnPage = "paymentInformationView";
        try {
            CommonUtility.mapAllFiscalYearName(model);

            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);

            if (searchParameterForm.getDivision() == null) {
                CommonUtility.mapDivisionName(model);
            }
            CommonUtility.getWardNoList(model);

            switch (pageType) {
                case "union":
                    searchParameterForm.setApplicantType(ApplicantType.UNION);
                    returnPage = "paymentInformationViewUnion";
                    break;
                case "municipal":
                    searchParameterForm.setApplicantType(ApplicantType.MUNICIPAL);
                    returnPage = "paymentInformationViewMunicipal";
                    break;
                case "bgmea":
                    searchParameterForm.setApplicantType(ApplicantType.BGMEA);
                    CommonUtility.mapBGMEAFactoryName(model);
                    returnPage = "paymentInformationViewBgmea";
                    break;
                case "bkmea":
                    searchParameterForm.setApplicantType(ApplicantType.BKMEA);
                    CommonUtility.mapBKMEAFactoryName(model);
                    returnPage = "paymentInformationViewBkmea";
                    break;

                default:
                    returnPage = "paymentInformationView";
                    break;
            }
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }

        return returnPage;
    }

    /**
     *
     * @param request
     * @param response
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/beneficiary/paymentInformationViewDataTable", method = RequestMethod.GET)
    @ResponseBody
    public String beneficiaryPaymentInformationView(
            HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerTypeAdapter()).create();
        String jsonArray = "";
        try {
            UserDetail loginedUser = (UserDetail) request.getSession().getAttribute("User");

            PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo = gson.fromJson(request.getParameter("searchInfo"), PaymnetInformationViewSearchInfo.class);

            paymnetInformationViewSearchInfo.setApplicantType((ApplicantType.valueOf(paymnetInformationViewSearchInfo.getApplicantTypeStr())));
            String startIndexSt = request.getParameter("start");
            int startPageIndex = startIndexSt != null ? Integer.parseInt(startIndexSt) : 0;
            String recordsPerPageSt = request.getParameter("length");
            int recordsPerPage = recordsPerPageSt != null ? Integer.parseInt(recordsPerPageSt) : Integer.MAX_VALUE;
            if (recordsPerPage == -1) {
                recordsPerPage = Integer.MAX_VALUE;
            }
            String draw = (request.getParameter("draw"));

            List<PaymentInformationView> paymentInformationViews = beneficiaryPaymentService.getPaymentInformation(startPageIndex, recordsPerPage, paymnetInformationViewSearchInfo);
            int count = 0;
            if (paymentInformationViews.size() > 0) {
                count = paymentInformationViews.get(0).getCount();
            }
            jsonArray = gson.toJson(paymentInformationViews);
            jsonArray = "{ \"draw\": " + draw + ",\"recordsTotal\": " + paymentInformationViews.size() + ",\"recordsFiltered\": " + count + ",\"data\":" + jsonArray + "}";

        } catch (Exception ex) {
            //logger.getlogger(ApplicantController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonArray;
    }

    @RequestMapping(value = "/beneficiary/paymentInformationUpdate/{benID}", method = RequestMethod.GET)
    public String paymentInformationUpdate(@PathVariable("benID") Integer benID, @ModelAttribute BeneficiaryForm beneficiaryForm,
            Model model, HttpServletRequest request) {

        try {
            UserDetail loginedUser = (UserDetail) request.getSession().getAttribute("User");
            Beneficiary beneficiary = this.beneficiaryService.getBeneficiary(benID);
            commonForBeneficiaryControllers.loadBasicInfo(beneficiaryForm, beneficiary);
            commonForBeneficiaryControllers.loadAddressInfo(beneficiaryForm, beneficiary);

//            loadSocioEconomicInfo(beneficiaryForm, beneficiary);
//            loadHealthStatusInfo(beneficiaryForm, beneficiary);
            commonForBeneficiaryControllers.loadPaymentInfo(beneficiaryForm, beneficiary);
//            loadBiometricInfo(beneficiaryForm, beneficiary);
//            loadAttachmentInfo(beneficiaryForm, beneficiary);
//            CommonUtility.mapAllFiscalYearName(model);
//
            CommonUtility.mapPaymentTypeName(model);
            CommonUtility.mapAccountTypeName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);

            model.addAttribute("searchParameterForm", searchParameterForm);
            model.addAttribute("type", beneficiary.getApplicantType());
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }

        model.addAttribute("reasonList", AccountInformationChangeReason.fromValuesToMap());
        return "paymentInformationUpdate";
    }

    /**
     *
     * @param request
     * @param files
     * @return
     */
    @RequestMapping(value = "/beneficiary/updatePaymentInfo", method = RequestMethod.POST)
    @ResponseBody
    public String updatePaymentInfo(MultipartHttpServletRequest request,
            @RequestPart("file") MultipartFile[] files) {

        String JSonData = request.getParameter("updateApplicantInfo");
        Gson g = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter()).create();;
        PaymentInformationView paymentInformationView = g.fromJson(JSonData, PaymentInformationView.class);

        JsonResult jr = new JsonResult(false, "ok");
        jr.setErrorCode(200);
        User loginedUser = (User) request.getSession().getAttribute("user");

        try {
            paymentInformationView.setFiles(files);
            paymentInformationView.setAccountNo(CommonUtility.getNumberInEnglish(paymentInformationView.getAccountNumber()));

            beneficiaryPaymentService.updatePaymentInfo(paymentInformationView, loginedUser);

            if (files != null) {
                //    fileSaveForAccountInformationChange(files, cycleDetail);
            }
        } catch (Exception ex) {
            jr.setErrorCode(400);
            jr.setErrorMsg("Exp Occured");
            ex.printStackTrace();
            //logger.getlogger(userTrainignController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jr.toJsonString();
    }

    @RequestMapping(value = "/beneficiary/paymentInformationHistory/{benID}", method = RequestMethod.GET)
    @ResponseBody
    public String beneficiaryPaymentInformationHistory(@PathVariable("benID") Integer benID,
            HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerTypeAdapter()).create();
        String jsonArray = "";
        try {

            User loginedUser = (User) request.getSession().getAttribute("user");

            PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo = gson.fromJson(request.getParameter("searchInfo"), PaymnetInformationViewSearchInfo.class);

            String startIndexSt = request.getParameter("start");
            int startPageIndex = startIndexSt != null ? Integer.parseInt(startIndexSt) : 0;
            String recordsPerPageSt = request.getParameter("length");
            int recordsPerPage = recordsPerPageSt != null ? Integer.parseInt(recordsPerPageSt) : Integer.MAX_VALUE;
            if (recordsPerPage == -1) {
                recordsPerPage = Integer.MAX_VALUE;
            }
            String draw = (request.getParameter("draw"));
            if (paymnetInformationViewSearchInfo == null) {
                paymnetInformationViewSearchInfo = new PaymnetInformationViewSearchInfo();
            }
            paymnetInformationViewSearchInfo.setBenId(benID);

            List<PaymentInformationView> paymentInformationViews = beneficiaryPaymentService.getBeneficiaryPaymentHistoryList(startPageIndex, recordsPerPage, paymnetInformationViewSearchInfo);
            int count = 0;
//            if (paymentInformationViews.size() > 0) {
//                count = paymentInformationViews.get(0).getCount();
//            }
            jsonArray = gson.toJson(paymentInformationViews);
            jsonArray = "{ \"draw\": " + draw + ",\"recordsTotal\": " + paymentInformationViews.size() + ",\"recordsFiltered\": " + paymentInformationViews.size() + ",\"data\":" + jsonArray + "}";

        } catch (Exception ex) {
            //logger.getlogger(ApplicantController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonArray;
    }

    @RequestMapping(value = {"/beneficiary/payment-information/file-download"})
    public void downloadPDFResource_(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "paymentHistoryId", required = false) int paymentHistoryId) {

        PaymentInformationHistory cycleDetailFile = beneficiaryPaymentService.getBeneficiaryPaymentHistoryById(paymentHistoryId);

        byte[] fileByte = Base64.decodeBase64(cycleDetailFile.getFileBase64());

        InputStream is = new ByteArrayInputStream(fileByte);

        try {

            // copy it to response's OutputStream
//            response.setContentType("attachment; filename='" + cycleDetailFile.getFileName() + "'");
//            if (cycleDetailFile.getFileExtension().toLowerCase().equals("jpeg")) {
//                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//            }
            response.setHeader("Content-disposition", "attachment; filename=\"" + cycleDetailFile.getId() + "");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
