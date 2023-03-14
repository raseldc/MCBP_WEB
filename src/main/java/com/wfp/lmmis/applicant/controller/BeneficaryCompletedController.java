/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.controller;

import com.wfp.lmmis.applicant.service.BeneficiaryPaymentCompleteService;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.IntegerTypeAdapter;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Controller
public class BeneficaryCompletedController {
    //Beneficiary Payment information View and update

    //private final org.apache.log4j.logger logger = org.apache.log4j.//logger.getlogger(BeneficiaryController.class);
    @Autowired(required = true)
    private BeneficiaryPaymentCompleteService beneficiaryPaymentCompleteService;

    @RequestMapping(value = "/beneficiary/payment-complete-view/{type}", method = RequestMethod.GET)
    public String beneficiaryPaymentCompleteView(Model model, HttpServletRequest request, @PathVariable("type") String pageType) {
        String returnPage = "paymentInformationView";
        try {
            CommonUtility.mapAllFiscalYearName(model);

            //  SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            ReportParameterForm searchParameterForm = CommonUtility.loadReportParameterForm(request);
            if (searchParameterForm.getDivision() == null) {
                CommonUtility.mapDivisionName(model);
            }
            CommonUtility.getWardNoList(model);

            switch (pageType) {
                case "union":
                    searchParameterForm.setApplicantType(ApplicantType.UNION);
                    returnPage = "paymentCompleteViewUnion";
                    break;
                case "municipal":
                    searchParameterForm.setApplicantType(ApplicantType.MUNICIPAL);
                    returnPage = "paymentCompleteViewMunicipal";
                    break;
                case "bgmea":
                    searchParameterForm.setApplicantType(ApplicantType.BGMEA);
                    CommonUtility.mapBGMEAFactoryName(model);
                    returnPage = "paymentCompleteViewBgmea";
                    break;
                case "bkmea":
                    searchParameterForm.setApplicantType(ApplicantType.BKMEA);
                    CommonUtility.mapBKMEAFactoryName(model);
                    returnPage = "paymentCompleteViewBkmea";
                    break;

                default:
                    returnPage = "paymentCompleteViewUnion";
                    break;
            }
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }

        return returnPage;
    }

    @RequestMapping(value = "/beneficiary/payment-complete-view-data-table", method = RequestMethod.GET)
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

            List<PaymentInformationView> paymentInformationViews = beneficiaryPaymentCompleteService.getPaymentCompleteBeneficiaryList(startPageIndex, recordsPerPage, paymnetInformationViewSearchInfo);
            int count = 0;
            if (paymentInformationViews.size() > 0) {
                count = paymentInformationViews.get(0).getCount();
            }
            jsonArray = gson.toJson(paymentInformationViews);
            jsonArray = "{ \"draw\": " + draw + ",\"recordsTotal\": " + paymentInformationViews.size() + ",\"recordsFiltered\": " + count + ",\"data\":" + jsonArray + "}";

        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.getlogger(ApplicantController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonArray;
    }

}
