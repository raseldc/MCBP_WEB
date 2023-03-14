/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.controller;

import com.wfp.lmmis.beneficiary.model.BeneficiaryView;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.PayrollListType;
import com.wfp.lmmis.enums.ServiceType;
import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.model.ServiceSettings;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.forms.PaymentGenerationForm;
import com.wfp.lmmis.payroll.forms.PaymentInfo;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.model.Payment;
import com.wfp.lmmis.payroll.model.PaymentCycle;
import com.wfp.lmmis.payroll.model.PaymentInfoForSpbmu;
import com.wfp.lmmis.payroll.model.PaymentView;
import com.wfp.lmmis.payroll.model.PayrollSummary;
import com.wfp.lmmis.payroll.model.SupplementaryPayrollInfo;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.payroll.service.PaymentCycleService;
import com.wfp.lmmis.payroll.service.PaymentService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.service.ServiceSettingsService;
import com.wfp.lmmis.types.PayrollStatus;
import com.wfp.lmmis.utility.ApplicationConstants;
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
import java.net.HttpURLConnection;
import java.net.URL;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaymentController {

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    private FiscalYearService fiscalYearService;
    @Autowired
    private PaymentCycleService paymentCycleService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private DivisionService divisionService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private UpazillaService upazilaService;
    @Autowired
    private ServiceSettingsService serviceSettingsService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    public void mapGlobalVariables(Model m) {
        m.addAttribute("dateFormat", "yy-mm-dd");
    }

    @RequestMapping(value = "/getAllPaymentCycle/{fiscalYearId}/{schemeId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getAllPaymentCycleListByFiscalYear(@PathVariable("fiscalYearId") Integer fiscalYearId, @PathVariable("schemeId") Integer schemeId, Model model, HttpSession session) {
        List<ItemObject> paymentCyclelist = this.paymentCycleService.getPaymentCycleIoList(true, fiscalYearId, false, false, schemeId);
        return paymentCyclelist;
    }

    @RequestMapping(value = "/getActivePaymentCycle/{fiscalYearId}/{schemeId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getActivePaymentCycleListByFiscalYear(@PathVariable("fiscalYearId") Integer fiscalYearId, @PathVariable("schemeId") Integer schemeId, HttpSession session) {
        List<ItemObject> paymentCyclelist = this.paymentCycleService.getPaymentCycleIoList(true, fiscalYearId, true, true, schemeId);
        return paymentCyclelist;
    }

//    @RequestMapping(value = "/getParentPaymentCycleIoList/{fiscalYearId}/{isRural}", method = RequestMethod.GET)
//    @ResponseBody
//    public List<ItemObject> getParentPaymentCycleListByFiscalYear(@PathVariable("fiscalYearId") Integer fiscalYearId, @PathVariable("isRural") boolean isRural, Model model, HttpSession session)
//    {
////        Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
//        Integer schemeId = isRural?1:3; //id should be retrieved from schemeCode
//        System.out.println("schemeId = " + schemeId);
//        List<ItemObject> paymentCyclelist = this.paymentCycleService.getParentPaymentCycleIoList(fiscalYearId, schemeId);
//        return paymentCyclelist;
//    }
    @RequestMapping(value = "/getParentPaymentCycleIoList/{fiscalYearId}/{schemeId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getParentPaymentCycleIOListFYSC(@PathVariable("fiscalYearId") Integer fiscalYearId, @PathVariable("schemeId") Integer schemeId, Model model, HttpSession session) {
//        Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
        List<ItemObject> paymentCyclelist = this.paymentCycleService.getParentPaymentCycleIoList(fiscalYearId, schemeId);
        return paymentCyclelist;
    }

    @RequestMapping(value = "/getChildPaymentCycleIoList/{fiscalYearId}/{schemeId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getChildPaymentCycleListByFiscalYear(@PathVariable("fiscalYearId") Integer fiscalYearId, @PathVariable("schemeId") Integer schemeId, Model model, HttpSession session) {
//        Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
        List<ItemObject> paymentCyclelist = this.paymentCycleService.getChildPaymentCycleIoList(fiscalYearId, schemeId);
        return paymentCyclelist;
    }

    @RequestMapping(value = "/paymentGeneration/list", method = RequestMethod.GET)
    public String getPaymentList(Model model, HttpServletRequest request) {
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapFiscalYearName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "paymentList";
    }

    @RequestMapping(value = "/paymentGeneration/list", method = RequestMethod.POST)
    @ResponseBody
    public void showPaymentList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {
            System.out.println("in list");
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String unionId = request.getParameter("unionId");

            Map parameter = new HashMap();
            parameter.put("schemeId", Integer.valueOf(request.getParameter("scheme")));
//            parameter.put("fiscalYearId", Integer.valueOf(request.getParameter("fiscalYear")));
            parameter.put("paymentCycleId", Integer.valueOf(request.getParameter("paymentCycle")));
            parameter.put("applicantId", request.getParameter("applicantId"));
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);

            List<Object> resultList = paymentService.getPaymentListBySearchParameter(parameter, beginIndex, pageSize);
            List<Payment> paymentList = (List<Payment>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);

            for (Payment payment : paymentList) {
                JSONArray ja = new JSONArray();
                ja.put(payment.getBenefiaryName());
                System.out.println("beneficiary = " + payment.getBeneficiary() + ", name = " + payment.getBenefiaryName() + ", payment id=" + payment.getId());
                ja.put(payment.getBeneficiary().getNid());
                ja.put(new DecimalFormat("#.00").format(payment.getAllowanceAmount()));
                ja.put("0" + payment.getBeneficiary().getMobileNo());
                ja.put(payment.getBank());
                ja.put(payment.getBranch());
                ja.put(payment.getAccountNumber());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Payroll Summary">
    @RequestMapping(value = "/payrollSummary/list", method = RequestMethod.GET)
    public String getPayrollSummary(Model model, HttpServletRequest request) {

        CommonUtility.mapDivisionName(model);
        CommonUtility.mapAllFiscalYearName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);

        UserType userType = ((UserDetail) request.getSession().getAttribute("userDetail")).getUserType();
        if (null != userType) {
            switch (userType) {
                case DIRECTORATE:
                case MINISTRY:
                    CommonUtility.mapDivisionName(model);
                    searchParameterForm.setPayrollListType(PayrollListType.DISTRICT);
                    break;
                case BGMEA:
                    searchParameterForm.setApplicantType(ApplicantType.BGMEA);
                    break;
                case BKMEA:
                    searchParameterForm.setApplicantType(ApplicantType.BKMEA);
                    break;
                case FIELD:
                    CommonUtility.mapDivisionName(model);
                    searchParameterForm.setApplicantType(ApplicantType.REGULAR);
                    break;
            }
        }

        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollSummaryList";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/payrollSummary/union/list", method = RequestMethod.GET)
    public String getPayrollSummaryUnion(Model model, HttpServletRequest request) {
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapAllFiscalYearName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.UNION);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollSummaryListUpazila";
    }

    @RequestMapping(value = "/payrollSummary/municipal/list", method = RequestMethod.GET)
    public String getPayrollSummaryMunicipal(Model model, HttpServletRequest request) {

        CommonUtility.mapDivisionName(model);
        CommonUtility.mapAllFiscalYearName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.MUNICIPAL);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollSummaryListUpazila";
    }

    @RequestMapping(value = "/payrollSummary/district/list", method = RequestMethod.GET)
    public String getPayrollSummaryDistrict(Model model, HttpServletRequest request) {

        CommonUtility.mapDivisionName(model);
        CommonUtility.mapAllFiscalYearName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.DISTRICT);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollSummaryListDistrict";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/payrollSummary/bgmea/list", method = RequestMethod.GET)
    public String getPayrollSummaryBgmea(Model model, HttpServletRequest request) {

        CommonUtility.mapDivisionName(model);
        CommonUtility.mapAllFiscalYearName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.BGMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollSummaryListBgmeaBkmea";
    }

    @RequestMapping(value = "/payrollSummary/bkmea/list", method = RequestMethod.GET)
    public String getPayrollSummaryBkmea(Model model, HttpServletRequest request) {

        CommonUtility.mapDivisionName(model);
        CommonUtility.mapAllFiscalYearName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.BKMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollSummaryListBgmeaBkmea";
    }

    /*
        This method is called from different payroll list page(upazila, district, bgmea/bkmea) to show payroll summary
     */
    @RequestMapping(value = "/payrollSummary/list", method = RequestMethod.POST)
    @ResponseBody
    public void showPayrollSummary(HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {
            int draw = Integer.parseInt(request.getParameter("draw"));
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String payrollListTypeSt = request.getParameter("payrollListType");
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            PayrollListType payrollListType = (payrollListTypeSt != null && payrollListTypeSt != "") ? PayrollListType.valueOf(payrollListTypeSt) : null;
            Locale locale = LocaleContextHolder.getLocale();

            Map parameter = new HashMap();
            parameter.put("fiscalYearId", Integer.valueOf(request.getParameter("fiscalYear")));
            parameter.put("paymentCycleId", request.getParameter("paymentCycle") != null && !request.getParameter("paymentCycle").isEmpty() ? Integer.valueOf(request.getParameter("paymentCycle")) : null);
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("payrollListType", payrollListType);

            List<Object> resultList = paymentService.getPayrollSummaryListBySearchParameter(parameter, beginIndex, pageSize);
            List<PayrollSummary> payrollSummaryList = (List<PayrollSummary>) resultList.get(0);
            int recordsTotal = (int) resultList.get(1);
            int recordsFiltered = (int) resultList.get(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            for (PayrollSummary payrollSummary : payrollSummaryList) {
                JSONArray ja = new JSONArray();
                ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getPaymentCycle().getNameInEnglish() : payrollSummary.getPaymentCycle().getNameInBangla());
                if (payrollListType == PayrollListType.UNION || payrollListType == PayrollListType.MUNICIPAL || payrollListType == PayrollListType.DISTRICT) {
                    if (payrollSummary.getDivision() != null) {
                        ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getDivision().getNameInEnglish() : payrollSummary.getDivision().getNameInBangla());
                    } else {
                        ja.put("");
                    }
                    if (payrollSummary.getDistrict() != null) {
                        ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getDistrict().getNameInEnglish() : payrollSummary.getDistrict().getNameInBangla());
                    } else {
                        ja.put("");
                    }
                    if (payrollSummary.getUpazilla() != null) {
                        ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getUpazilla().getNameInEnglish() : payrollSummary.getUpazilla().getNameInBangla());
                    } else {
                        ja.put("");
                    }
                }
                String totalBen = String.valueOf(payrollSummary.getTotalBeneficiary());
                totalBen = totalBen.replace(".0", "");
                ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getTotalBeneficiary() : CommonUtility.getNumberInBangla(totalBen));
                ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getTotalAllowance() : CommonUtility.getNumberInBangla(String.valueOf(payrollSummary.getTotalAllowance()) + "0"));

                ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getCreatedBy().getFullNameEn() : payrollSummary.getCreatedBy().getFullNameBn());
                ja.put("en".equals(locale.getLanguage()) ? formatter.format(payrollSummary.getCreationDate().getTime()) : CommonUtility.getNumberInBangla(formatter.format(payrollSummary.getCreationDate().getTime())));

                if (payrollSummary.getPayrollStatus() == PayrollStatus.APPROVED) {
                    ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getModifiedBy() != null ? payrollSummary.getModifiedBy().getFullNameEn() : "" : payrollSummary.getModifiedBy() != null ? payrollSummary.getModifiedBy().getFullNameBn() : "");
                    ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getModificationDate() != null ? formatter.format(payrollSummary.getModificationDate().getTime()) : "" : payrollSummary.getModificationDate() != null ? CommonUtility.getNumberInBangla(formatter.format(payrollSummary.getModificationDate().getTime())) : "");
                } else {
                    ja.put("");
                    ja.put("");
                }
                String edit = localizer.getLocalizedText("edit", locale);
                String viewDetails = localizer.getLocalizedText("viewDetails", locale);
                ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getPayrollStatus().toString() : payrollSummary.getPayrollStatus().getDisplayNameBn());
                // this should available or not based on payroll status...skipping for now
                if (payrollSummary.getPayrollStatus().equals(PayrollStatus.SAVED) || payrollSummary.getPayrollStatus().equals(PayrollStatus.RECHECK)) {
                    String paymentGenerationUrl = "paymentGeneration";
                    if (payrollSummary.getPaymentCycle().getParentPaymentCycle() != null) {
                        paymentGenerationUrl = "childPaymentGeneration";
                    }
                    ja.put("<a href=\"" + request.getContextPath() + "/paymentGeneration/viewList/" + payrollSummary.getId() + "\" title=\"" + viewDetails + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>"
                            + " | <a href=\"" + request.getContextPath() + "/" + paymentGenerationUrl + "/edit/" + payrollSummary.getId() + "\" title=\"" + edit + "\"><span class=\"glyphicon glyphicon-edit\"></span></a>");
                } else {
                    ja.put("<a href=\"" + request.getContextPath() + "/paymentGeneration/viewList/" + payrollSummary.getId() + "\" title=\"" + viewDetails + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Payroll Approve">
    @RequestMapping(value = "/payrollApprove/list", method = RequestMethod.GET)
    public String getPayrollApproveList(Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        mapPayrollListName(model);
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapFiscalYearName(model);
        searchParameterForm.setPayrollListType(PayrollListType.UNION);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollApproveList";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/payrollApprove/list/bgmea", method = RequestMethod.GET)
    public String getPayrollApproveListBgmea(Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        CommonUtility.mapFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        searchParameterForm.setPayrollListType(PayrollListType.BGMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollApproveListBgmeaBkmea";
    }

    @RequestMapping(value = "/payrollApprove/list/bkmea", method = RequestMethod.GET)
    public String getPayrollApproveListBkmea(Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        CommonUtility.mapFiscalYearName(model);
        searchParameterForm.setPayrollListType(PayrollListType.BKMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollApproveListBgmeaBkmea";
    }

    @RequestMapping(value = "/payrollApprove/list/municipal", method = RequestMethod.GET)
    public String getPayrollApproveListMunicipal(Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        mapPayrollListName(model);
        CommonUtility.mapFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        searchParameterForm.setPayrollListType(PayrollListType.MUNICIPAL);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "payrollApproveListMunicipal";
    }

    private static void mapPayrollListName(Model model) {
        List<PayrollListType> payrollListTypeList = Arrays.asList(PayrollListType.values());
        model.addAttribute("payrollListTypeList", payrollListTypeList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }

    /**
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/payrollApprove/list", method = RequestMethod.POST)
    @ResponseBody
    public void showPayrollApproveList(HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        System.out.println("in payrollApprove list");

        try {
            int draw = Integer.parseInt(request.getParameter("draw"));
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String payrollListTypeSt = request.getParameter("payrollListType");
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            PayrollListType payrollListType = (payrollListTypeSt != null && !payrollListTypeSt.isEmpty()) ? PayrollListType.valueOf(payrollListTypeSt) : null;
//            System.out.println("payrollListType = " + payrollListType);
            Map parameter = new HashMap();
//            parameter.put("schemeId", ((UserDetail) request.getSession().getAttribute("userDetail")).getScheme().getId());
            parameter.put("fiscalYearId", Integer.valueOf(request.getParameter("fiscalYear")));
            parameter.put("paymentCycleId", request.getParameter("paymentCycle") != null && !request.getParameter("paymentCycle").isEmpty() ? Integer.valueOf(request.getParameter("paymentCycle")) : null);
            parameter.put("payrollListType", payrollListType);
//            PayrollListType payrollListType = PayrollListType.UNION;
//            parameter.put("payrollListType", payrollListType); // this function will only load Maternity(union) payroll list
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("payrollStatus", PayrollStatus.SUBMITTED.ordinal() + ", " + PayrollStatus.APPROVED.ordinal());

            Locale locale = LocaleContextHolder.getLocale();

            List<Object> resultList = paymentService.getPayrollSummaryListBySearchParameter(parameter, beginIndex, pageSize);
            List<PayrollSummary> payrollSummaryList = (List<PayrollSummary>) resultList.get(0);
            int recordsTotal = (int) resultList.get(1);
            int recordsFiltered = (int) resultList.get(2);
            for (PayrollSummary payrollSummary : payrollSummaryList) {
                JSONArray ja = new JSONArray();
                if (payrollSummary.getPayrollStatus().equals(PayrollStatus.SUBMITTED)) {
                    ja.put("<input type=\"checkbox\" class=\"approve-checkbox\" id=\"" + payrollSummary.getId() + "\" name=\"" + payrollSummary.getId() + "\">");
                } else {
                    ja.put("");
                }
                if (payrollSummary.getPayrollStatus().equals(PayrollStatus.APPROVED) && !payrollSummary.isIsExportedToSpbmu()) {
                    ja.put("<input type=\"checkbox\" class=\"export-checkbox\" id=\"" + payrollSummary.getId() + "\" name=\"" + payrollSummary.getId() + "\">");
                } else {
                    ja.put("");
                }
                ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getPaymentCycle().getNameInEnglish() : payrollSummary.getPaymentCycle().getNameInBangla());
                if (payrollListType == PayrollListType.UNION || payrollListType == PayrollListType.MUNICIPAL || payrollListType == PayrollListType.DISTRICT) {
                    ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getDivision().getNameInEnglish() : payrollSummary.getDivision().getNameInBangla());
                    ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getDistrict().getNameInEnglish() : payrollSummary.getDistrict().getNameInBangla());
                    ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getUpazilla().getNameInEnglish() : payrollSummary.getUpazilla().getNameInBangla());
                } else {
                    ja.put("");
                    ja.put("");
                    ja.put("");
                }

                String totalBen = String.valueOf(payrollSummary.getTotalBeneficiary());
                totalBen = totalBen.replace(".0", "");
                ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getTotalBeneficiary() : CommonUtility.getNumberInBangla(totalBen));
                ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getTotalAllowance() : CommonUtility.getNumberInBangla(String.valueOf(payrollSummary.getTotalAllowance()) + "0"));
                ja.put("en".equals(locale.getLanguage()) ? payrollSummary.getPayrollStatus().toString() : payrollSummary.getPayrollStatus().getDisplayNameBn());
//                ja.put(payrollSummary.isIsExportedToSpbmu());
//                String edit = localizer.getLocalizedText("edit", LocaleContextHolder.getLocale());
//                if (payrollSummary.getPayrollStatus().equals(PayrollStatus.SUBMITTED))
//                {
//                    ja.put("<a href=\"" + request.getContextPath() + "/paymentGeneration/pendingList/" + payrollSummary.getId() + "\" title=\"" + edit + "\"><span class=\"glyphicon glyphicon-edit\"></span></a>");
//                }
//                else
//                {
//                    ja.put("");
//                }
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param session
     * @param request
     * @param redirectAttributes
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/payrollApprove", method = RequestMethod.POST)
    public String payrollApprove(HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        try {

            System.out.println("asche..");
            String action = request.getParameter("action");
            System.out.println("action = " + action);
            String selectedPayrollSummaryIdList = request.getParameter("selectedPayrollSummaryIdList");
            System.out.println("in submit payroll approve" + selectedPayrollSummaryIdList);
            boolean success = false;
            if (action.equalsIgnoreCase("accept")) {
                try {
                    if (!"".equals(selectedPayrollSummaryIdList)) {
                        String approvalComments = request.getParameter("reasonFV");
                        String[] payrollSummaryIdArray = selectedPayrollSummaryIdList.split(",");
                        for (String payrollSummaryId : payrollSummaryIdArray) {
                            PayrollSummary payrollSummary = this.paymentService.getPayrollSummary(new Integer(payrollSummaryId));
                            PayrollStatus payrollStatus = "accept".equals(action) ? PayrollStatus.APPROVED : PayrollStatus.RECHECK;
                            payrollSummary.setPayrollStatus(payrollStatus);
                            payrollSummary.setIsExportedToSpbmu(false);
                            payrollSummary.setApprovalComments(approvalComments);
                            payrollSummary.setModifiedBy((User) session.getAttribute("user"));
                            payrollSummary.setModificationDate(Calendar.getInstance());
                            this.paymentService.edit(payrollSummary);
                        }
                    }
                    success = true;
                } catch (Exception e) {
                    throw new ExceptionWrapper("Payroll Approval operation failed!");
                }

            } else if (action.equalsIgnoreCase("export"))// export to spbmu
            {
                Integer paymentCycleId = request.getParameter("paymentCycle") != null && !request.getParameter("paymentCycle").isEmpty() ? Integer.valueOf(request.getParameter("paymentCycle")) : null;
                if (paymentCycleId == null) {
                    throw new ExceptionWrapper("Payment Cycle must be selected!");
                }
                success = exportPaymentDataToSPBMU(paymentCycleId, selectedPayrollSummaryIdList);
//                success = true;
            }
            if (success) {
                if (action.equalsIgnoreCase("export")) {
                    this.paymentService.updateExportStatusOfPayrollSummaryList(selectedPayrollSummaryIdList);
                }
                String actionResult = action.equalsIgnoreCase("export") ? "exportSuccess" : "approveSuccess";
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText(actionResult, LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
                return "redirect:/payrollApprove/list";
            }

        } catch (ExceptionWrapper | NumberFormatException e) {
            throw new ExceptionWrapper(e.getMessage());
        }
        return null;
    }

//</editor-fold>
    @RequestMapping(value = "/paymentGeneration/pendingList")
    public String showPaymentPendingList(Model model) {
        model.addAttribute("paymentList", this.paymentService.getPendingPaymentList(false, null, false, null));
        return "pendingPaymentList";
    }

    @RequestMapping(value = "/paymentGeneration/create", method = RequestMethod.GET)
    public String createPaymentCycle(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        CommonUtility.mapGlobalVariables(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        String defaultSchemeShortName = ((UserDetail) request.getSession().getAttribute("userDetail")).getScheme().getShortName();
        if ("LMA".equals(defaultSchemeShortName)) {
            UserType userType = ((UserDetail) request.getSession().getAttribute("userDetail")).getUserType();
            if (null != userType) {
                switch (userType) {
                    case DIRECTORATE:
                    case MINISTRY:
                        CommonUtility.mapDivisionName(model);
                        searchParameterForm.setApplicantType(ApplicantType.REGULAR);
//                        CommonUtility.mapBGMEAFactoryName(model);
//                        CommonUtility.mapBKMEAFactoryName(model);
                        break;
                    case BGMEA:
                        searchParameterForm.setApplicantType(ApplicantType.BGMEA);
//                        CommonUtility.mapBGMEAFactoryName(model);
                        break;
                    case BKMEA:
                        searchParameterForm.setApplicantType(ApplicantType.BKMEA);
//                        CommonUtility.mapBKMEAFactoryName(model);
                        break;
                    case FIELD:
                        CommonUtility.mapDivisionName(model);
                        searchParameterForm.setApplicantType(ApplicantType.REGULAR);
                        break;
                }
            }
        } else {
            CommonUtility.mapDivisionName(model);
        }
        CommonUtility.mapFiscalYearName(model);
        model.addAttribute("beneficiaryList", null);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        model.addAttribute("actionType", "create");
        return "payment";
    }

    /**
     *
     * @param paymentGenerationForm
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/payroll/union/create", method = RequestMethod.GET)
    public String createPayrollForUnion(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.UNION);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("actionType", "create");
        return "payrollUpazila";
    }

    @RequestMapping(value = "/payroll/municipal/create", method = RequestMethod.GET)
    public String createPayrollForMunicipal(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.MUNICIPAL);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("actionType", "create");
        return "payrollUpazila";
    }

    /**
     *
     * @param paymentGenerationForm
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/payroll/district/create", method = RequestMethod.GET)
    public String createPayrollForDistrict(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.DISTRICT);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("actionType", "create");
        return "payrollDistrict";
    }

    @RequestMapping(value = "/payroll/bgmea/create", method = RequestMethod.GET)
    public String createPayrollForBgmea(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.BGMEA);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("actionType", "create");
        model.addAttribute("type", "bgmea");
        return "payrollBgmeaBkmea";
    }

    @RequestMapping(value = "/payroll/bkmea/create", method = RequestMethod.GET)
    public String createPayrollForBkmea(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.BKMEA);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("actionType", "create");
        model.addAttribute("type", "bkmea");
        return "payrollBgmeaBkmea";
    }

    /*
        This method is used to generate payroll for upazila/district/bgmea/bkmea 
        This is called from above mentioned jsp pages
     */
    @RequestMapping(value = "/paymentGeneration/create", method = RequestMethod.POST)
    public String createPaymentCycle(@ModelAttribute
            @Valid PaymentGenerationForm paymentGenerationForm, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "payment";
        }
        PayrollSummary payrollSummary = null;
        SearchParameterForm searchParameterForm = paymentGenerationForm.getSearchParameterForm();
        try {
            System.out.println("searchParameterForm.getPayrollListType() = " + searchParameterForm.getPayrollListType());
            Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
            searchParameterForm.setScheme(scheme);
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(paymentGenerationForm.getSearchParameterForm().getFiscalYear().getId());
            searchParameterForm.setFiscalYear(fiscalYear);
            PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(paymentGenerationForm.getSearchParameterForm().getPaymentCycle().getId());
            searchParameterForm.setPaymentCycle(paymentCycle);

            if (searchParameterForm.getPayrollListType() == PayrollListType.UNION || searchParameterForm.getPayrollListType() == PayrollListType.MUNICIPAL || searchParameterForm.getPayrollListType() == PayrollListType.DISTRICT) {
                Division division = this.divisionService.getDivision(paymentGenerationForm.getSearchParameterForm().getDivision().getId());
                searchParameterForm.setDivision(division);
                District district = this.districtService.getDistrict(paymentGenerationForm.getSearchParameterForm().getDistrict().getId());
                searchParameterForm.setDistrict(district);
                Upazilla upazila = this.upazilaService.getUpazilla(paymentGenerationForm.getSearchParameterForm().getUpazila().getId());
                searchParameterForm.setUpazila(upazila);

                searchParameterForm.setIsDivisionAvailable(true);
                searchParameterForm.setIsDistrictAvailable(true);
                searchParameterForm.setIsUpazilaAvailable(true);
            }

            // Not sure scheme will be required; for not breaking db structure, scheme is set for IMLMA
            if (null != searchParameterForm.getPayrollListType()) {
                switch (searchParameterForm.getPayrollListType()) {
                    case MUNICIPAL:
                    case BGMEA:
                    case BKMEA:
                        scheme = schemeService.getScheme(new Integer("3"));
                        paymentGenerationForm.getSearchParameterForm().setScheme(scheme);
                        break;
                    default:
                        scheme = schemeService.getScheme(new Integer("1"));
                        paymentGenerationForm.getSearchParameterForm().setScheme(scheme);
                }
            }

            paymentGenerationForm.setSearchParameterForm(searchParameterForm);
            paymentGenerationForm.setCreatedBy((User) session.getAttribute("user"));

            Integer returnValue = this.paymentService.generatePaymentData(paymentGenerationForm);
            if (returnValue > 0) {
                payrollSummary = this.paymentService.getPayrollSummary(returnValue);
                paymentGenerationForm.setPayrollSummary(payrollSummary);
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("generateSuccess", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
                model.addAttribute("beneficiaryList", null);
                model.addAttribute("isSuccess", true);
                model.addAttribute("message", localizer.getLocalizedText("generateSuccess", LocaleContextHolder.getLocale()));
                model.addAttribute("payrollSummary", payrollSummary);
                model.addAttribute("actionType", "submit");
            } else if (returnValue == 0) {
                String errMsg = localizer.getLocalizedText("payrollAlreadyGenerated", LocaleContextHolder.getLocale());
                model.addAttribute("message", errMsg);
                model.addAttribute("isSuccess", false);
                model.addAttribute("actionType", "create");
            } else if (returnValue == -1) {
                String errMsg = localizer.getLocalizedText("noBeneficiaryFound", LocaleContextHolder.getLocale());
                model.addAttribute("message", errMsg);
                model.addAttribute("isSuccess", false);
                model.addAttribute("actionType", "create");
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof ConstraintViolationException) {
                System.out.println("e = " + e.getMessage());
                String errMsg = localizer.getLocalizedText("payrollAlreadyGenerated", LocaleContextHolder.getLocale());
                model.addAttribute("message", errMsg);
            } else {
                model.addAttribute("message", "Error in creating Payroll !");
            }

            System.out.println("type = " + e);
            //logger.infoer(e.getMessage());
            model.addAttribute("isSuccess", false);
            model.addAttribute("actionType", "create");
        }

        CommonUtility.mapFiscalYearName(model);
        model.addAttribute("paymentGenerationForm", paymentGenerationForm);
        if (payrollSummary.getPaymentCycle().getParentPaymentCycle() == null) {
            switch (searchParameterForm.getPayrollListType()) {
                case UNION:
                    CommonUtility.mapDivisionName(model);
                    return "payrollUpazila";
                case MUNICIPAL:
                    CommonUtility.mapDivisionName(model);
                    return "payrollUpazila";
                case DISTRICT:
                    CommonUtility.mapDivisionName(model);
                    return "payrollDistrict";
                case BGMEA:
                case BKMEA:
                    System.out.println(".............  ");
                    return "payrollBgmeaBkmea";
            }
        } else {
            switch (searchParameterForm.getPayrollListType()) {
                case UNION:
                    CommonUtility.mapDivisionName(model);
                    return "subPayrollUpazila";
                case MUNICIPAL:
                    CommonUtility.mapDivisionName(model);
                    return "subPayrollUpazila";
                case DISTRICT:
                    CommonUtility.mapDivisionName(model);
                    return "subPayrollDistrict";
                case BGMEA:
                case BKMEA:
                    System.out.println(".............  ");
                    return "subPayrollBgmeaBkmea";
            }
        }

        return null;
    }

    /**
     *
     * @param paymentGenerationForm
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/subPayroll/union/create", method = RequestMethod.GET)
    public String createSubPayrollForUnion(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.UNION);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("actionType", "create");
        return "subPayrollUpazila";
    }

    @RequestMapping(value = "/subPayroll/municipal/create", method = RequestMethod.GET)
    public String createSubPayrollForMunicipal(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.MUNICIPAL);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("actionType", "create");
        return "subPayrollUpazila";
    }

    @RequestMapping(value = "/subPayroll/district/create", method = RequestMethod.GET)
    public String createSubPayrollForDistrict(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.DISTRICT);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("actionType", "create");
        return "subPayrollDistrict";
    }

    @RequestMapping(value = "/subPayroll/bgmea/create", method = RequestMethod.GET)
    public String createSubPayrollForBgmea(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.BGMEA);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("actionType", "create");
        model.addAttribute("type", "bgmea");
        return "subPayrollBgmeaBkmea";
    }

    @RequestMapping(value = "/subPayroll/bkmea/create", method = RequestMethod.GET)
    public String createSubPayrollForBkmea(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setPayrollListType(PayrollListType.BKMEA);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("actionType", "create");
        model.addAttribute("type", "bkmea");
        return "subPayrollBgmeaBkmea";
    }

    @RequestMapping(value = "/paymentGeneration/beforeCreate/list", method = RequestMethod.POST)
    @ResponseBody
    public void beforeCreatePaymentList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {
            System.out.println("in list");
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String unionId = request.getParameter("unionId");

            Map parameter = new HashMap();
            parameter.put("schemeId", ((UserDetail) session.getAttribute("userDetail")).getScheme().getId());
//            parameter.put("fiscalYearId", Integer.valueOf(request.getParameter("fiscalYear")));
            parameter.put("paymentCycleId", Integer.valueOf(request.getParameter("paymentCycle")));
            parameter.put("applicantType", request.getParameter("applicantType"));
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);

            List<Object[]> resultList = paymentService.getbeforePaymentGenerationList(parameter, beginIndex, pageSize);
            int recordsTotal = 0, recordsFiltered = 0;
            if (resultList != null) {
                recordsTotal = Integer.valueOf(paymentService.getbeforePaymentGenerationCount(parameter).toString());
                recordsFiltered = recordsTotal;
                for (Object[] beneficiaryObj : resultList) {
                    JSONArray ja = new JSONArray();
                    Locale locale = LocaleContextHolder.getLocale();
                    if ("en".equals(locale.getLanguage())) {
                        ja.put(beneficiaryObj[1]);//name
                        ja.put(beneficiaryObj[3]);//bank
                        ja.put(beneficiaryObj[5]);//branch
                    } else {
                        ja.put(beneficiaryObj[2]);
                        ja.put(beneficiaryObj[4]);
                        ja.put(beneficiaryObj[6]);
                    }
                    ja.put(beneficiaryObj[7]);//nid
                    ja.put(beneficiaryObj[8]);//account_number
                    ja.put(beneficiaryObj[9]);//mobile_number
                    ja.put(beneficiaryObj[10]);//routing_number
                    dataArray.put(ja);
                }
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/paymentGeneration/beforeCreate/unions", method = RequestMethod.POST)
    @ResponseBody
    public void beforeCreateUnionList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {
            int draw = 0;
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String unionId = request.getParameter("unionId");

            Map parameter = new HashMap();
            parameter.put("paymentCycleId", Integer.valueOf(request.getParameter("paymentCycle")));
            if (!request.getParameter("payrollListType").equals("")) {
                parameter.put("payrollListType", request.getParameter("payrollListType") != null ? (PayrollListType.valueOf(request.getParameter("payrollListType"))) : null);
            }
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);

            List<Object[]> resultList = paymentService.getbeforePaymentGenerationUnionList(parameter);
            int recordsTotal = 0, recordsFiltered = 0;
            Locale locale = LocaleContextHolder.getLocale();
            if (resultList != null) {
                recordsTotal = resultList.size();
                recordsFiltered = recordsTotal;
                int i = 1;
                for (Object[] unionObj : resultList) {
                    JSONArray ja = new JSONArray();
                    if ("en".equals(locale.getLanguage())) {
                        ja.put(i);
                        ja.put(unionObj[3]);//name
                    } else {
                        ja.put(CommonUtility.getNumberInBangla(i + ""));
                        ja.put(unionObj[2]);
                    }
                    ja.put(unionObj[1]);
                    dataArray.put(ja);
                    i++;
                }
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param id
     * @param paymentGenerationForm
     * @param model
     * @param request
     * @param initialLoadRequired
     * @return
     */
    @RequestMapping(value = "/paymentGeneration/edit/{id}", method = RequestMethod.GET)
    public String editPayroll(@PathVariable("id") Integer id, @ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request, boolean initialLoadRequired) {
        try {
            System.out.println("initalLoadRequired = " + initialLoadRequired);
            if (!initialLoadRequired) {
                CommonUtility.mapDivisionName(model);
                CommonUtility.mapGlobalVariables(model);
                CommonUtility.mapSchemeName(model);
                CommonUtility.mapFiscalYearName(model);

                PayrollSummary payrollSummary = this.paymentService.getPayrollSummary(id);
                if (payrollSummary.getPayrollStatus().equals(PayrollStatus.SAVED) || payrollSummary.getPayrollStatus().equals(PayrollStatus.RECHECK)) {

                    SearchParameterForm searchParameterForm = new SearchParameterForm();
                    searchParameterForm.setScheme(payrollSummary.getScheme());
                    searchParameterForm.setFiscalYear(payrollSummary.getPaymentCycle().getFiscalYear());
                    searchParameterForm.setPaymentCycle(payrollSummary.getPaymentCycle());
                    searchParameterForm.setIsDivisionAvailable(true);
                    searchParameterForm.setDivision(payrollSummary.getDivision());
                    searchParameterForm.setIsDistrictAvailable(true);
                    searchParameterForm.setDistrict(payrollSummary.getDistrict());
                    searchParameterForm.setIsUpazilaAvailable(true);
                    searchParameterForm.setUpazila(payrollSummary.getUpazilla());
                    searchParameterForm.setPayrollListType(payrollSummary.getPayrollListType());
                    paymentGenerationForm.setSearchParameterForm(searchParameterForm);
                    paymentGenerationForm.setPayrollSummary(payrollSummary);

                    if (payrollSummary.getPayrollStatus().equals(PayrollStatus.SAVED)) {
                        model.addAttribute("actionType", "submit");
                    } else {
                        model.addAttribute("actionType", "edit");
                    }
                    switch (payrollSummary.getPayrollListType()) {
                        case UNION:
                            return "payrollUpazila";
                        case MUNICIPAL:
                            return "payrollUpazila";
                        case DISTRICT:
                            return "payrollDistrict";
                        case BGMEA:
                            return "payrollBgmeaBkmea";
                        case BKMEA:
                            return "payrollBgmeaBkmea";
                        default:
                            return null;
                    }
                } else {
                    String message = localizer.getLocalizedText("payrollMessage", LocaleContextHolder.getLocale());
                    ExceptionWrapper exceptionWrapper = new ExceptionWrapper(message);
                    model.addAttribute("exception", exceptionWrapper);
                    return "customError";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "customError";

    }

    @RequestMapping(value = "/paymentGeneration/edit", method = RequestMethod.POST)
    public String editPayrollSummary(@ModelAttribute
            @Valid PaymentGenerationForm paymentGenerationForm, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "payment";
        }

        PayrollSummary payrollSummary = null;
        try {
            SearchParameterForm searchParameterForm = paymentGenerationForm.getSearchParameterForm();
            Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
            searchParameterForm.setScheme(scheme);
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(paymentGenerationForm.getSearchParameterForm().getFiscalYear().getId());
            searchParameterForm.setFiscalYear(fiscalYear);
            PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(paymentGenerationForm.getSearchParameterForm().getPaymentCycle().getId());
            searchParameterForm.setPaymentCycle(paymentCycle);

            Division division = this.divisionService.getDivision(paymentGenerationForm.getSearchParameterForm().getDivision().getId());
            searchParameterForm.setDivision(division);
            District district = this.districtService.getDistrict(paymentGenerationForm.getSearchParameterForm().getDistrict().getId());
            searchParameterForm.setDistrict(district);
            Upazilla upazila = this.upazilaService.getUpazilla(paymentGenerationForm.getSearchParameterForm().getUpazila().getId());
            searchParameterForm.setUpazila(upazila);

            searchParameterForm.setIsDivisionAvailable(true);
            searchParameterForm.setIsDistrictAvailable(true);
            searchParameterForm.setIsUpazilaAvailable(true);

            paymentGenerationForm.setSearchParameterForm(searchParameterForm);

//            paymentGenerationForm.setPaymentType(this.paymentTypeService.getPaymentTypeList(true, "01").get(0));
            paymentGenerationForm.setCreatedBy((User) session.getAttribute("user"));
            Integer returnValue = this.paymentService.generatePaymentData(paymentGenerationForm);
            payrollSummary = this.paymentService.getPayrollSummary(returnValue);

            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            model.addAttribute("isSuccess", true);
            model.addAttribute("message", localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            model.addAttribute("payrollSummary", payrollSummary);
        } catch (Exception e) {
            System.out.println("Test = ");
            System.out.println("" + e.getLocalizedMessage());
            e.printStackTrace();
            //logger.infoer(e.getMessage());
            model.addAttribute("isSuccess", false);
            model.addAttribute("message", "Error in creating Payroll !");

        }
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapFiscalYearName(model);
        CommonUtility.mapPaymentTypeName(model);
        model.addAttribute("actionType", "submit");

        model.addAttribute("paymentGenerationForm", paymentGenerationForm);

        return "payment";

//        return "redirect:/paymentGeneration/list";
    }

    /*
        This method is called at submit button in payroll generation page
     */
    @RequestMapping(value = "/paymentGeneration/submit/{payrollSummaryId}", method = RequestMethod.POST)
    public String finalizePayment(@PathVariable("payrollSummaryId") Integer payrollSummaryId, HttpSession session,
            Model model, RedirectAttributes redirectAttributes) {
        PayrollSummary payrollSummary = this.paymentService.getPayrollSummary(payrollSummaryId);
        payrollSummary.setPayrollStatus(PayrollStatus.SUBMITTED);
        payrollSummary.setModifiedBy((User) session.getAttribute("user"));
        payrollSummary.setModificationDate(Calendar.getInstance());

        this.paymentService.edit(payrollSummary);

        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("submitSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        switch (payrollSummary.getPayrollListType()) {
            case UNION:
                return "redirect:/payrollSummary/union/list";
            case MUNICIPAL:
                return "redirect:/payrollSummary/municipal/list";
            case DISTRICT:
                return "redirect:/payrollSummary/district/list";
            case BGMEA:
                return "redirect:/payrollSummary/bgmea/list";
            case BKMEA:
                return "redirect:/payrollSummary/bkmea/list";
            default:
                return null;
        }
    }

    @RequestMapping(value = "/paymentGeneration/pendingList/{schemeId}/{paymentCycleId}", method = RequestMethod.GET)
    public String getPaymentInfo(@PathVariable("schemeId") Integer schemeId, @PathVariable("paymentCycleId") Integer paymentCycleId,
            @ModelAttribute PaymentInfo paymentInfo, Model model) {
        System.out.println("in specific pending list");
        model.addAttribute("paymentInfo", this.paymentService.getPendingPaymentList(true, schemeId, true, paymentCycleId).get(0));
        return "approvePayment";
    }

    @RequestMapping(value = "/paymentGeneration/pendingList/{schemeId}/{paymentCycleId}", method = RequestMethod.POST)
    public String finalizePayment(@PathVariable("schemeId") Integer schemeId, @PathVariable("paymentCycleId") Integer paymentCycleId,
            @ModelAttribute PaymentInfo paymentInfo, Model model, @RequestParam String action, RedirectAttributes redirectAttributes) {
        if ("accept".equals(action)) {
            paymentInfo.setApprovalStatus(Boolean.TRUE);

        } else {
            paymentInfo.setApprovalStatus(Boolean.FALSE);
        }
        paymentInfo.setPaymentCycle(this.paymentCycleService.getPaymentCycle(paymentCycleId));
        paymentInfo.setScheme(this.schemeService.getScheme(schemeId));
        this.paymentService.editPaymentInfo(paymentInfo);

        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, "Payroll has been approved successfully");
        redirectAttributes.addFlashAttribute("message", controllerMessage);

        return "redirect:/paymentGeneration/pendingList";
    }

    @RequestMapping(value = "/paymentGeneration/pendingList/{payrollSummaryId}", method = RequestMethod.GET)
    public String getPayrollDetails(@PathVariable("payrollSummaryId") Integer payrollSummaryId,
            @ModelAttribute PaymentInfo paymentInfo, Model model, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in specific pending list 2");
        PayrollSummary payrollSummary = this.paymentService.getPayrollSummary(payrollSummaryId);
        model.addAttribute("payrollSummary", payrollSummary);
        System.out.println("size = " + payrollSummary.getId());

        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
//        int draw = Integer.parseInt(request.getParameter("draw"));

        Map parameter = new HashMap();
        parameter.put("schemeId", payrollSummary.getScheme().getId());
        parameter.put("paymentCycleId", payrollSummary.getPaymentCycle().getId());
        parameter.put("payrollSummaryId", payrollSummary.getId());
        parameter.put("divisionId", payrollSummary.getDivision().getId());
        parameter.put("districtId", payrollSummary.getDistrict().getId());
        parameter.put("upazilaId", payrollSummary.getUpazilla().getId());

//        List<Object> resultList = paymentService.getPaymentListBySearchParameter(parameter, 0, Integer.MAX_VALUE);
//        List<Payment> paymentList = (List<Payment>) resultList.get(0);
//        System.out.println("paymentList = " + paymentList.size());
//        model.addAttribute("paymentList", paymentList);
        return "approvePayroll";
    }

    @RequestMapping(value = "/paymentGeneration/viewList/{payrollSummaryId}", method = RequestMethod.GET)
    public String getPayrollDetailList(@PathVariable("payrollSummaryId") Integer payrollSummaryId,
            @ModelAttribute PaymentInfo paymentInfo, Model model, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in specific pending list 2");
        PayrollSummary payrollSummary = this.paymentService.getPayrollSummary(payrollSummaryId);
        model.addAttribute("payrollSummary", payrollSummary);
        if (payrollSummary.getDivision() != null) {
            model.addAttribute("isFactory", false);
        } else {
            model.addAttribute("isFactory", true);
        }
        System.out.println("size = " + payrollSummary.getId());
//
//        JSONObject jsonResult = new JSONObject();
//        JSONArray dataArray = new JSONArray();
////        int draw = Integer.parseInt(request.getParameter("draw"));

//        Map parameter = new HashMap();
//        parameter.put("schemeId", payrollSummary.getScheme().getId());
//        parameter.put("paymentCycleId", payrollSummary.getPaymentCycle().getId());
//        parameter.put("payrollSummaryId", payrollSummary.getId());
//        if (payrollSummary.getDivision() != null)
//        {
//            parameter.put("divisionId", payrollSummary.getDivision().getId());
//            parameter.put("isFactory", false);
//        }
//        else
//        {
//            parameter.put("isFactory", true);
//        }
//        if (payrollSummary.getDistrict() != null)
//        {
//            parameter.put("districtId", payrollSummary.getDistrict().getId());
//        }
//        if (payrollSummary.getUpazilla() != null)
//        {
//            parameter.put("upazilaId", payrollSummary.getUpazilla().getId());
//        }
//        List<Object> resultList = paymentService.getPaymentListBySearchParameter(parameter, 0, Integer.MAX_VALUE);
//        List<Payment> paymentList = (List<Payment>) resultList.get(0);
//        model.addAttribute("paymentList", paymentList);
        return "payrollListUpazila";
    }

    @RequestMapping(value = "/paymentGeneration/pendingList/{payrollSummaryId}", method = RequestMethod.POST)
    public String finalizePayrollDetails(@PathVariable("payrollSummaryId") Integer payrollSummaryId,
            @ModelAttribute PayrollSummary editedPayrollSummary, @RequestParam String action, HttpSession session, RedirectAttributes redirectAttributes) {
        PayrollSummary payrollSummary = this.paymentService.getPayrollSummary(payrollSummaryId);
        if ("accept".equals(action)) {
            payrollSummary.setPayrollStatus(PayrollStatus.APPROVED);
        } else {
            payrollSummary.setPayrollStatus(PayrollStatus.RECHECK);
        }
        System.out.println("editedPayrollSummary.getApprovalComments() = " + editedPayrollSummary.getApprovalComments());
        payrollSummary.setApprovalComments(editedPayrollSummary.getApprovalComments());
        payrollSummary.setModifiedBy((User) session.getAttribute("user"));
        payrollSummary.setModificationDate(Calendar.getInstance());
        this.paymentService.edit(payrollSummary);

        String success = localizer.getLocalizedText("onlySuccess", LocaleContextHolder.getLocale());
        String actionMsg = "en".equals(LocaleContextHolder.getLocale()) ? payrollSummary.getPayrollStatus().getDisplayName() : payrollSummary.getPayrollStatus().getDisplayNameBn();
        String done = localizer.getLocalizedText("afterSuccess", LocaleContextHolder.getLocale());

        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, success + " " + actionMsg + " " + done);
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        redirectAttributes.addFlashAttribute("isSuccess", true);

        return "redirect:/payrollApprove/list";
    }

    @RequestMapping(value = "/payroll/viewList", method = RequestMethod.POST)
    @ResponseBody
    public void showPayrollList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
        Locale locale = LocaleContextHolder.getLocale();
        try {
            System.out.println("in asche");
            int draw = Integer.parseInt(request.getParameter("draw"));
//            int draw = 0;
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            String payrollSummaryId = request.getParameter("payrollSummary");
            PayrollSummary payrollSummary = this.paymentService.getPayrollSummary(Integer.parseInt(payrollSummaryId));
            Map parameter = new HashMap();
            parameter.put("paymentCycleId", payrollSummary.getPaymentCycle().getId());
            parameter.put("payrollSummaryId", payrollSummary.getId());
            parameter.put("divisionId", payrollSummary.getDivision() == null ? null : payrollSummary.getDivision().getId());
            parameter.put("districtId", payrollSummary.getDistrict() == null ? null : payrollSummary.getDistrict().getId());
            parameter.put("upazilaId", payrollSummary.getUpazilla() == null ? null : payrollSummary.getUpazilla().getId());
            if ("bn".equals(LocaleContextHolder.getLocale().getLanguage())) {
                parameter.put("nid", CommonUtility.getNumberInEnglish(request.getParameter("nid")));
            } else {
                parameter.put("nid", request.getParameter("nid"));
            }
            if (!"".equals(request.getParameter("unionId")) && payrollSummary.getDivision() != null) {
                parameter.put("unionId", Integer.parseInt(request.getParameter("unionId")));
            }

            List<Object> resultList = paymentService.getPaymentListBySearchParameter(parameter, beginIndex, pageSize);
            List<PaymentView> paymentList = (List<PaymentView>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);

            for (PaymentView payment : paymentList) {
                JSONArray ja = new JSONArray();
                if ("en".equals(locale.getLanguage())) {
                    ja.put(payment.getFullNameInEnglish());
                    ja.put(payment.getNid().toString());
                    ja.put(payment.getMobileNo() != null ? '0' + payment.getMobileNo().toString() : "");
                    ja.put(payment.getBankEn());
                    ja.put(payment.getBranchEn());
                    ja.put(payment.getAccountNo());
                    ja.put(payment.getUnionNameEn());
                    ja.put(new DecimalFormat("#.00").format(payment.getAllowanceAmount()));
                } else {
                    ja.put(payment.getFullNameInBangla());
                    ja.put(CommonUtility.getNumberInBangla(payment.getNid().toString()));
                    ja.put(payment.getMobileNo() != null ? CommonUtility.getNumberInBangla("0" + payment.getMobileNo().toString()) : "");
                    ja.put(payment.getBankBn());
                    ja.put(payment.getBranchBn());
                    ja.put(CommonUtility.getNumberInBangla(payment.getAccountNo()));
                    ja.put(payment.getUnionNameBn());
                    ja.put(CommonUtility.getNumberInBangla(new DecimalFormat("#.00").format(payment.getAllowanceAmount())));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/payment/statusupdate", method = RequestMethod.GET)
    public String getPaymentStatusUpdateList(Model model, HttpServletRequest request) {
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "paymentStatusUpdate";
    }

    @RequestMapping(value = "/payment/statusupdate/urban", method = RequestMethod.GET)
    public String getPaymentStatusUpdateListUrban(Model model, HttpServletRequest request) {
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "paymentStatusUpdateUrban";
    }

    @RequestMapping(value = "/payment/statusupdate", method = RequestMethod.POST)
    public String getPaymentStatusUpdatedList(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String paymentCycleId = request.getParameter("paymentCycle.id");
        String type = request.getParameter("type");
        try {
            ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.RECONCILIATION);
            //first login to SPBMU
            String token = loginSPBMU(serviceSettings);
            System.out.println("logged in " + token);
            if (token != null) {
                String schemeCode = type.equalsIgnoreCase("urban") ? "2222" : "1111";
                String result = checkPaymentStatusAtSPBMU(serviceSettings, schemeCode, paymentCycleId, token);
                System.out.println("result = " + result);
                if ("1".equals(result)) {
                    ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
                    redirectAttributes.addFlashAttribute("message", controllerMessage);
                    model.addAttribute("isSuccess", true);
                    model.addAttribute("message", localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
                } else {
                    model.addAttribute("isSuccess", false);
                    model.addAttribute("message", "Error in Updating Payroll !");

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            java.util.logging.//logger.getlogger(PaymentController.class
//                    .getName()).log(Level.SEVERE, null, ex);
        }
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "paymentStatusUpdate";
    }

    private String loginSPBMU(ServiceSettings serviceSettings) throws Exception {
        try {
            System.out.println("serviceSettings.getAuthenticatedURL() = " + serviceSettings.getAuthenticatedURL());
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
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println("op result = " + jsonResponse);
                if ("true".equals(jsonResponse.get("operationResult").toString())) {
                    System.out.println("res = " + jsonResponse.get("operationResult").toString());
                    return jsonResponse.getString("token");
                }
                System.out.println("res = " + jsonResponse.get("operationResult").toString());
                return null;

            } else {
                System.out.println("POST request error : " + responseCode);
                return null;
            }
        } catch (IOException | JSONException e) {
            throw e;
        }

    }

    // Payroll Reconciliation
    public String checkPaymentStatusAtSPBMU(ServiceSettings serviceSettings, String schemeCode, String paymentCycleId, String token) throws Exception {
        URL url = new URL(serviceSettings.getServiceURL() + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);
        con.setDoInput(true);

        JSONObject json = new JSONObject();
        PaymentCycle paymentCycle = paymentCycleService.getPaymentCycle(Integer.parseInt(paymentCycleId));
        json.put("fiscalYear", paymentCycle.getFiscalYear().getNameInEnglish());
        json.put("ministryCode", "3005");
        json.put("paymentCycle", paymentCycle.getNameInEnglish());
        json.put("schemeCode", schemeCode);

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
//            {\"operationResult\":true,\"errorMsg\":null,\"errorCode\":null,\"paylist\":[{\"nid\":\"19949318540931145\",\"paymentUid\":\"1160\",\"beneficiaryId\":\"174715\",\"remarks\":\"Comment1\",\"eftReferenceNumber\":\"123456789\"},{\"nid\":\"19883313247044522\",\"paymentUid\":\"1161\",\"beneficiaryId\":\"174715\",\"remarks\":\"Comment2\",\"eftReferenceNumber\":\"987654321\"},{\"nid\":\"19893313060678121\",\"paymentUid\":\"1162\",\"beneficiaryId\":\"174715\",\"remarks\":\"Comment3\",\"eftReferenceNumber\":\"556633669\"}],\"total\":3}
//            JsonObject jsonResponse = new JsonParser().parse("{\\\"operationResult\\\":true,\\\"errorMsg\\\":null,\\\"errorCode\\\":null,\\\"paylist\\\":[{\\\"nid\\\":\\\"19949318540931145\\\",\\\"paymentUid\\\":\\\"1160\\\",\\\"beneficiaryId\\\":\\\"174715\\\",\\\"remarks\\\":\\\"Comment1\\\",\\\"eftReferenceNumber\\\":\\\"123456789\\\"},{\\\"nid\\\":\\\"19883313247044522\\\",\\\"paymentUid\\\":\\\"1161\\\",\\\"beneficiaryId\\\":\\\"174715\\\",\\\"remarks\\\":\\\"Comment2\\\",\\\"eftReferenceNumber\\\":\\\"987654321\\\"},{\\\"nid\\\":\\\"19893313060678121\\\",\\\"paymentUid\\\":\\\"1162\\\",\\\"beneficiaryId\\\":\\\"174715\\\",\\\"remarks\\\":\\\"Comment3\\\",\\\"eftReferenceNumber\\\":\\\"556633669\\\"}],\\\"total\\\":3}").getAsJsonObject();
            JSONObject jsonResponse = new JSONObject(response.toString());
//            JSONObject jsonResponse = new JSONObject("{\"operationResult\":true,\"errorMsg\":null,\"errorCode\":null,\"paylist\":[{\"nid\":\"19949318540931145\",\"paymentUid\":\"1160\",\"beneficiaryId\":\"3297\",\"remarks\":\"Comment1\",\"eftReferenceNumber\":null},{\"nid\":\"19883313247044522\",\"paymentUid\":\"1161\",\"beneficiaryId\":\"3296\",\"remarks\":\"Comment2\",\"eftReferenceNumber\":\"987654321\"},{\"nid\":\"19893313060678121\",\"paymentUid\":\"1162\",\"beneficiaryId\":\"3295\",\"remarks\":\"Comment3\",\"eftReferenceNumber\":\"556633669\"}],\"total\":3}");
            Map parameter = new HashMap();
            parameter.put("paymentCycleId", paymentCycle.getId());
            paymentService.setPaymentStatusToOne(parameter); // set all payment's payroll_status =1 for a cycle first
            System.out.println("reconciliation api result = " + jsonResponse);
            if (jsonResponse.get("operationResult").toString() == "true" && (int) jsonResponse.get("total") > 0) {
                JSONArray paylist = jsonResponse.getJSONArray("paylist");

                for (int i = 0; i < paylist.length(); i++) {
                    parameter = new HashMap();
                    JSONObject paymentInfo = paylist.getJSONObject(i);
                    parameter.put("paymentCycleId", paymentCycle.getId());
                    parameter.put("beneficiaryId", Integer.parseInt(paymentInfo.get("beneficiaryId").toString()));
                    parameter.put("paymentUid", Integer.parseInt(paymentInfo.get("paymentUid").toString()));
                    parameter.put("eftReferenceNumber", paymentInfo.get("eftReferenceNumber"));
                    parameter.put("returnedCode", paymentInfo.get("returnedCode"));
                    parameter.put("returnedText", paymentInfo.get("returnedText"));
                    paymentService.editPaymentStatus(parameter);
                }
            }
            return "1";
        } else {
            return "0";
        }
    }

    @RequestMapping(value = "/payment/cycle/list", method = RequestMethod.POST)
    @ResponseBody
    public void showPaymentListbyCycle(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
        Locale locale = LocaleContextHolder.getLocale();
        try {
            System.out.println("in asche");
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            String paymentCycleId = request.getParameter("paymentCycle");
            String nid = null, accountNo = null;
            if (request.getParameter("nid") != null) {
                nid = request.getParameter("nid").trim();
            }

            if (request.getParameter("accountNo") != null) {
                accountNo = request.getParameter("accountNo").trim();
            }
            Map parameter = new HashMap();
            parameter.put("paymentCycleId", Integer.parseInt(paymentCycleId));
            parameter.put("nid", nid);
            parameter.put("accountNo", accountNo);
            List<Object> resultList = paymentService.getPaymentListBySearchParameter(parameter, beginIndex, pageSize);
            List<PaymentView> paymentList = (List<PaymentView>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);

            for (PaymentView payment : paymentList) {
                JSONArray ja = new JSONArray();
                if ("en".equals(locale.getLanguage())) {
                    ja.put(payment.getFullNameInEnglish());
                    ja.put(payment.getNid().toString());
                    ja.put(payment.getMobileNo() != null ? '0' + payment.getMobileNo().toString() : "");
                    ja.put(payment.getBankEn());
                    ja.put(payment.getBranchEn());
                    ja.put(payment.getAccountNo());
                    ja.put(payment.getUnionNameEn());
                    ja.put(new DecimalFormat("#.00").format(payment.getAllowanceAmount()));
                    ja.put(payment.getPaymentStatus().getDisplayName());
                } else {
                    ja.put(payment.getFullNameInBangla());
                    ja.put(CommonUtility.getNumberInBangla(payment.getNid().toString()));
                    ja.put(payment.getMobileNo() != null ? CommonUtility.getNumberInBangla("0" + payment.getMobileNo().toString()) : "");
                    ja.put(payment.getBankBn());
                    ja.put(payment.getBranchBn());
                    ja.put(CommonUtility.getNumberInBangla(payment.getAccountNo()));
                    ja.put(payment.getUnionNameBn());
                    ja.put(CommonUtility.getNumberInBangla(new DecimalFormat("#.00").format(payment.getAllowanceAmount())));
                    ja.put(payment.getPaymentStatus().getDisplayNameBn());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/supplementaryPayroll/rural/create", method = RequestMethod.GET)
    public String getSupplementaryPayrollPageForRural(Model model, HttpServletRequest request) {
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "supplementaryPayrollRural";
    }

    @RequestMapping(value = "/supplementaryPayroll/urban/create", method = RequestMethod.GET)
    public String getSupplementaryPayrollPageForUrban(Model model, HttpServletRequest request) {
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "supplementaryPayrollUrban";
    }

    @RequestMapping(value = "/supplementaryPayroll/list", method = RequestMethod.POST)
    public void showSupplementaryPayrollList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
        try {
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            Map parameter = new HashMap();
            parameter.put("paymentCycleId", new Integer(request.getParameter("paymentCycle")));
//            parameter.put("divisionId", payrollSummary.getDivision() == null ? null : payrollSummary.getDivision().getId());
//            parameter.put("districtId", payrollSummary.getDistrict() == null ? null : payrollSummary.getDistrict().getId());
//            parameter.put("upazilaId", payrollSummary.getUpazilla() == null ? null : payrollSummary.getUpazilla().getId());
            List<Object> resultList = paymentService.getSupplementaryPayrollInfo(parameter, beginIndex, pageSize);
            List<SupplementaryPayrollInfo> paymentList = (List<SupplementaryPayrollInfo>) resultList.get(0);
            int recordsTotal = (int) resultList.get(1);
            int recordsFiltered = (int) resultList.get(2);
            String payrollBankInfo, currentBankInfo;
            for (SupplementaryPayrollInfo payment : paymentList) {
                JSONArray ja = new JSONArray();
                ja.put(payment.getNid());
                ja.put(payment.getPayrollAccountName());
                ja.put(payment.getCurrentAccountName());
                ja.put(payment.getPayrollAccountNumber());
                ja.put(payment.getCurrentAccountNumber());
                payrollBankInfo = !payment.getPayrollBranch().isEmpty() ? payment.getPayrollBankOrOthers() + "<br/> Branch: " + payment.getPayrollBranch() : payment.getPayrollBankOrOthers();
                currentBankInfo = payment.getCurrentBranch() != null ? payment.getPayrollBankOrOthers() + "<br/> Branch: " + payment.getCurrentBranch() : payment.getCurrentBankOrOthers();
                ja.put(payrollBankInfo);
                ja.put(currentBankInfo);
                ja.put(payment.getPayrollRoutingNo());
                ja.put(payment.getCurrentRoutingNo());
                ja.put(payment.getDivision());
                ja.put(payment.getDistrict());
                ja.put(payment.getUpazila());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/supplementaryPayroll/create", method = RequestMethod.POST)
    public String createSupplementaryPayroll(SearchParameterForm searchParameterForm, HttpServletRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        String pageType = request.getParameter("type");
        PaymentCycle paymentCycle = searchParameterForm.getPaymentCycle();
        if (paymentCycle == null) {
            throw new ExceptionWrapper("Payment cycle not found!");
        }
        Integer schemeCode = pageType.equalsIgnoreCase("urban") ? ApplicationConstants.IMLMA_URBAN_CODE : ApplicationConstants.IMLMA_RURAL_CODE;
        String returnUrl = pageType.equalsIgnoreCase("rural") ? "redirect:/supplementaryPayroll/rural/create" : "redirect:/supplementaryPayroll/urban/create";
        paymentCycle = paymentCycleService.getPaymentCycle(searchParameterForm.getPaymentCycle().getId());
        try {
            System.out.println("paymentCycle = " + paymentCycle.getNameInEnglish());
            JSONObject json = new JSONObject();
            json.put("fiscalYear", paymentCycle.getFiscalYear().getNameInEnglish());
            json.put("ministryCode", 3005);
            json.put("paymentCycle", paymentCycle.getNameInEnglish());
            json.put("schemeCode", schemeCode);

            JSONArray payListArray = new JSONArray();

            List<Object[]> payList = paymentService.getUnpaidListByPaymentCycle(paymentCycle.getId(), searchParameterForm.getUpazila().getId());
            for (Object[] row : payList) {
                JSONObject payInfo = new JSONObject();
                payInfo.put("eftReferenceNumber", row[0]);
                payInfo.put("nid", row[1]);
                payInfo.put("paymentUid", row[2]);
                payListArray.put(payInfo);
            }

            json.put("paylist", payListArray);
            json.put("total", payList.size());

//            System.out.println("json = " + json.toString());
            ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.SUPPLEMENTARY);
            String token = loginSPBMU(serviceSettings);

            if (token == null) {
                throw new ExceptionWrapper("Web Service Authentication error");
            }
            URL url = new URL(serviceSettings.getServiceURL() + token);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println("supplementary api result = " + jsonResponse);
                if (jsonResponse.get("operationResult").toString() == "true") {
                    ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
                    redirectAttributes.addFlashAttribute("message", controllerMessage);
                    return returnUrl;
                } else {
                    ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
                    redirectAttributes.addFlashAttribute("message", controllerMessage);
                    return returnUrl;
                }
            } else {
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
                return returnUrl;

            }

        } catch (Exception e) {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

//    @RequestMapping(value = "/supplementaryPayroll/create", method = RequestMethod.POST)
//    public String createSupplementaryPayroll(SearchParameterForm searchParameterForm, HttpServletRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper
//    {
//        PaymentCycle paymentCycle = searchParameterForm.getPaymentCycle();
//        if (paymentCycle == null)
//        {
//            throw new ExceptionWrapper("Payment cycle not found!");
//        }
//        else
//        {
//            paymentCycle = paymentCycleService.getPaymentCycle(searchParameterForm.getPaymentCycle().getId());
//        }
//        Integer upazilaId = searchParameterForm.getUpazila().getId();
//        String pageType = request.getParameter("type");
//        String action = request.getParameter("action");
//        System.out.println("action = " + action);
//        ControllerMessage controllerMessage;
//        String returnUrl = pageType.equalsIgnoreCase("rural") ? "redirect:/supplementaryPayroll/rural/create" : "redirect:/supplementaryPayroll/urban/create";
//        if (action.equalsIgnoreCase("update"))
//        {
//            try
//            {
//                this.paymentService.updatePayrollInfoForSupplementary(paymentCycle.getId(), upazilaId);
//                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
//                redirectAttributes.addFlashAttribute("message", controllerMessage);
//                return returnUrl;
//            }
//            catch (ExceptionWrapper e)
//            {
//                throw e;
//            }
//        }
//        else
//        {
//            Integer schemeCode = pageType.equalsIgnoreCase("urban") ? ApplicationConstants.IMLMA_URBAN_CODE : ApplicationConstants.IMLMA_RURAL_CODE;
//            try
//            {
//                JSONObject json = new JSONObject();
//                json.put("fiscalYear", paymentCycle.getFiscalYear().getNameInEnglish());
//                json.put("ministryCode", 3005);
//                json.put("paymentCycle", paymentCycle.getNameInEnglish());
//                json.put("schemeCode", schemeCode);
//
//                JSONArray payListArray = new JSONArray();
//
//                List<Object[]> payList = paymentService.getEFTBouncedPaymentList(paymentCycle.getId(), upazilaId);
//                for (Object[] row : payList)
//                {
//                    JSONObject payInfo = new JSONObject();
//                    payInfo.put("eftReferenceNumber", row[0]);
//                    payInfo.put("nid", row[1]);
//                    payInfo.put("paymentUid", row[2]);
//                    payListArray.put(payInfo);
//                }
//
//                json.put("paylist", payListArray);
//                json.put("total", payList.size());
//
//                System.out.println("json = " + json.toString());
//
//                ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.SUPPLEMENTARY);
//                String token = loginSPBMU(serviceSettings);
//
//                if (token == null)
//                {
//                    throw new ExceptionWrapper("Web Service Authentication error");
//                }
//                URL url = new URL(serviceSettings.getServiceURL() + token);
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//                con.setRequestMethod("POST");
//                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//                con.setDoOutput(true);
//                con.setDoInput(true);
//
//                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//                wr.writeBytes(json.toString());
//                wr.flush();
//                wr.close();
//
//                int responseCode = con.getResponseCode();
//
//                if (responseCode == HttpURLConnection.HTTP_OK)
//                {
//                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
//                    String inputLine;
//                    StringBuffer response = new StringBuffer();
//
//                    while ((inputLine = in.readLine()) != null)
//                    {
//                        response.append(inputLine);
//                    }
//                    JSONObject jsonResponse = new JSONObject(response.toString());
//                    System.out.println("supplementary api result = " + jsonResponse);
//                    if (jsonResponse.get("operationResult").toString() == "true")
//                    {
//                        controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
//                    }
//                    else
//                    {
//                        controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
//                    }
//                    redirectAttributes.addFlashAttribute("message", controllerMessage);
//                    return returnUrl;
//                }
//                else
//                {
//                    controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
//                    redirectAttributes.addFlashAttribute("message", controllerMessage);
//                    return returnUrl;
//                }
//            }
//            catch (Exception e)
//            {
//                throw new ExceptionWrapper(e.getMessage());
//            }
//        }
//    }
    @RequestMapping(value = "/childPaymentGeneration/create", method = RequestMethod.GET)
    public String createChildPayment(@ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request) {
        CommonUtility.mapGlobalVariables(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        String defaultSchemeShortName = ((UserDetail) request.getSession().getAttribute("userDetail")).getScheme().getShortName();
        if ("LMA".equals(defaultSchemeShortName)) {
            UserType userType = ((UserDetail) request.getSession().getAttribute("userDetail")).getUserType();
            if (null != userType) {
                switch (userType) {
                    case DIRECTORATE:
                    case MINISTRY:
                        CommonUtility.mapDivisionName(model);
                        searchParameterForm.setApplicantType(ApplicantType.REGULAR);
//                        CommonUtility.mapBGMEAFactoryName(model);
//                        CommonUtility.mapBKMEAFactoryName(model);
                        break;
                    case BGMEA:
                        searchParameterForm.setApplicantType(ApplicantType.BGMEA);
//                        CommonUtility.mapBGMEAFactoryName(model);
                        break;
                    case BKMEA:
                        searchParameterForm.setApplicantType(ApplicantType.BKMEA);
//                        CommonUtility.mapBKMEAFactoryName(model);
                        break;
                    case FIELD:
                        CommonUtility.mapDivisionName(model);
                        searchParameterForm.setApplicantType(ApplicantType.REGULAR);
                        break;
                }
            }
        } else {
            CommonUtility.mapDivisionName(model);
        }
        CommonUtility.mapFiscalYearName(model);
        model.addAttribute("beneficiaryList", null);
        paymentGenerationForm.setSearchParameterForm(searchParameterForm);
        model.addAttribute("actionType", "create");
        return "childPayment";
    }

    @RequestMapping(value = "/childPaymentGeneration/create", method = RequestMethod.POST)
    public String createChildPayment(@ModelAttribute
            @Valid PaymentGenerationForm paymentGenerationForm, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "payment";
        }
        PayrollSummary payrollSummary = null;
        SearchParameterForm searchParameterForm = paymentGenerationForm.getSearchParameterForm();
        try {
//            Scheme scheme = this.schemeService.getScheme(paymentGenerationForm.getSearchParameterForm().getScheme().getId());
            Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
            searchParameterForm.setScheme(scheme);
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(paymentGenerationForm.getSearchParameterForm().getFiscalYear().getId());
            searchParameterForm.setFiscalYear(fiscalYear);
            PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(paymentGenerationForm.getSearchParameterForm().getPaymentCycle().getId());
            searchParameterForm.setPaymentCycle(paymentCycle);

            if (searchParameterForm.getApplicantType() == ApplicantType.REGULAR) {
                Division division = this.divisionService.getDivision(paymentGenerationForm.getSearchParameterForm().getDivision().getId());
                searchParameterForm.setDivision(division);
                District district = this.districtService.getDistrict(paymentGenerationForm.getSearchParameterForm().getDistrict().getId());
                searchParameterForm.setDistrict(district);
                Upazilla upazila = this.upazilaService.getUpazilla(paymentGenerationForm.getSearchParameterForm().getUpazila().getId());
                searchParameterForm.setUpazila(upazila);

                searchParameterForm.setIsDivisionAvailable(true);
                searchParameterForm.setIsDistrictAvailable(true);
                searchParameterForm.setIsUpazilaAvailable(true);
            }

            paymentGenerationForm.setSearchParameterForm(searchParameterForm);
//            paymentGenerationForm.setPaymentType(this.paymentTypeService.getPaymentTypeList(true, "01").get(0));
            paymentGenerationForm.setCreatedBy((User) session.getAttribute("user"));

            // check if all beneficiaries has payment data
            List<BeneficiaryView> beneficiaryListWoPayment = this.paymentService.checkPaymentDataOfBeneficiaries(searchParameterForm);
//            List<Beneficiary> beneficiaryListWoPayment = null;
//            if (beneficiaryListWoPayment != null) // Ben exist without payment data
//            {
//                if (beneficiaryListWoPayment.isEmpty())
//                {
//                    System.out.println("beneficiaryListWoPayment = ");
//                    model.addAttribute("isSuccess", false);
//                    model.addAttribute("payrollSummary", payrollSummary);
//                    model.addAttribute("innerMessage", localizer.getLocalizedText("noBeneficiaryFound", LocaleContextHolder.getLocale()));
//                }
//                else
//                {
//                    System.out.println("size of ben wo payment = " + beneficiaryListWoPayment.size());
//                    model.addAttribute("beneficiaryList", beneficiaryListWoPayment);
//                    model.addAttribute("isSuccess", false);
//                    model.addAttribute("payrollSummary", payrollSummary);
//                    model.addAttribute("innerMessage", localizer.getLocalizedText("beneficiaryFoundWithoutPaymentData", LocaleContextHolder.getLocale()));
//                }
//            }
//            else
//            {

            Integer returnValue = this.paymentService.generatePaymentData(paymentGenerationForm);
            if (returnValue > 0) {
                payrollSummary = this.paymentService.getPayrollSummary(returnValue);
                paymentGenerationForm.setPayrollSummary(payrollSummary);
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("generateSuccess", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
                model.addAttribute("beneficiaryList", null);
                model.addAttribute("isSuccess", true);
                model.addAttribute("message", localizer.getLocalizedText("generateSuccess", LocaleContextHolder.getLocale()));
                model.addAttribute("payrollSummary", payrollSummary);
                model.addAttribute("actionType", "submit");
            } else if (returnValue == 0) {
                String errMsg = localizer.getLocalizedText("payrollAlreadyGenerated", LocaleContextHolder.getLocale());
                model.addAttribute("message", errMsg);
                model.addAttribute("isSuccess", false);
                model.addAttribute("actionType", "create");
            } else if (returnValue == -1) {
                String errMsg = localizer.getLocalizedText("noBeneficiaryFound", LocaleContextHolder.getLocale());
                model.addAttribute("message", errMsg);
                model.addAttribute("isSuccess", false);
                model.addAttribute("actionType", "create");
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Error in creating Payroll !");
            System.out.println("type = " + e);
            //logger.infoer(e.getMessage());
            model.addAttribute("isSuccess", false);
            model.addAttribute("actionType", "create");
        }
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapFiscalYearName(model);
        CommonUtility.mapPaymentTypeName(model);

        model.addAttribute("paymentGenerationForm", paymentGenerationForm);

        CommonUtility.mapFiscalYearName(model);
        model.addAttribute("paymentGenerationForm", paymentGenerationForm);
        switch (searchParameterForm.getPayrollListType()) {
            case UNION:
                CommonUtility.mapDivisionName(model);
                return "subPayrollUpazila";
            case MUNICIPAL:
                CommonUtility.mapDivisionName(model);
                return "subPayrollUpazila";
            case DISTRICT:
                CommonUtility.mapDivisionName(model);
                return "subPayrollDistrict";
            case BGMEA:
            case BKMEA:
                System.out.println(".............  ");
                return "subPayrollBgmeaBkmea";
        }
        return null;
    }

    /**
     *
     * @param id
     * @param paymentGenerationForm
     * @param model
     * @param request
     * @param initialLoadRequired
     * @return
     */
    @RequestMapping(value = "/childPaymentGeneration/edit/{id}", method = RequestMethod.GET)
    public String editChildPayroll(@PathVariable("id") Integer id, @ModelAttribute PaymentGenerationForm paymentGenerationForm, Model model, HttpServletRequest request, boolean initialLoadRequired) {
        try {
            System.out.println("initalLoadRequired = " + initialLoadRequired);
            if (!initialLoadRequired) {
                CommonUtility.mapDivisionName(model);
                CommonUtility.mapGlobalVariables(model);
                CommonUtility.mapSchemeName(model);
                CommonUtility.mapFiscalYearName(model);

                PayrollSummary payrollSummary = this.paymentService.getPayrollSummary(id);
                if (payrollSummary.getPayrollStatus().equals(PayrollStatus.SAVED) || payrollSummary.getPayrollStatus().equals(PayrollStatus.RECHECK)) {

                    SearchParameterForm searchParameterForm = new SearchParameterForm();
                    searchParameterForm.setScheme(payrollSummary.getScheme());
                    searchParameterForm.setFiscalYear(payrollSummary.getPaymentCycle().getFiscalYear());
                    searchParameterForm.setPaymentCycle(payrollSummary.getPaymentCycle());
                    searchParameterForm.setIsDivisionAvailable(true);
                    searchParameterForm.setDivision(payrollSummary.getDivision());
                    searchParameterForm.setIsDistrictAvailable(true);
                    searchParameterForm.setDistrict(payrollSummary.getDistrict());
                    searchParameterForm.setIsUpazilaAvailable(true);
                    searchParameterForm.setUpazila(payrollSummary.getUpazilla());
                    paymentGenerationForm.setSearchParameterForm(searchParameterForm);
                    paymentGenerationForm.setPayrollSummary(payrollSummary);

                    if (payrollSummary.getPayrollStatus().equals(PayrollStatus.SAVED)) {
                        model.addAttribute("actionType", "submit");
                    } else {
                        model.addAttribute("actionType", "edit");
                    }
                    switch (payrollSummary.getPayrollListType()) {
                        case UNION:
                            return "subPayrollUpazila";
                        case MUNICIPAL:
                            return "subPayrollUpazila";
                        case DISTRICT:
                            return "subPayrollDistrict";
                        case BGMEA:
                            return "subPayrollBgmeaBkmea";
                        case BKMEA:
                            return "subPayrollBgmeaBkmea";
                        default:
                            return null;
                    }
                } else {
                    String message = localizer.getLocalizedText("payrollMessage", LocaleContextHolder.getLocale());
                    ExceptionWrapper exceptionWrapper = new ExceptionWrapper(message);
                    model.addAttribute("exception", exceptionWrapper);
                    return "customError";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "customError";

    }

    @RequestMapping(value = "/childPaymentGeneration/edit", method = RequestMethod.POST)
    public String editChildPayrollSummary(@ModelAttribute
            @Valid PaymentGenerationForm paymentGenerationForm, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "childPayment";
        }

        PayrollSummary payrollSummary = null;
        try {
            SearchParameterForm searchParameterForm = paymentGenerationForm.getSearchParameterForm();
            Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
            searchParameterForm.setScheme(scheme);
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(paymentGenerationForm.getSearchParameterForm().getFiscalYear().getId());
            searchParameterForm.setFiscalYear(fiscalYear);
            PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(paymentGenerationForm.getSearchParameterForm().getPaymentCycle().getId());
            searchParameterForm.setPaymentCycle(paymentCycle);

            Division division = this.divisionService.getDivision(paymentGenerationForm.getSearchParameterForm().getDivision().getId());
            searchParameterForm.setDivision(division);
            District district = this.districtService.getDistrict(paymentGenerationForm.getSearchParameterForm().getDistrict().getId());
            searchParameterForm.setDistrict(district);
            Upazilla upazila = this.upazilaService.getUpazilla(paymentGenerationForm.getSearchParameterForm().getUpazila().getId());
            searchParameterForm.setUpazila(upazila);

            searchParameterForm.setIsDivisionAvailable(true);
            searchParameterForm.setIsDistrictAvailable(true);
            searchParameterForm.setIsUpazilaAvailable(true);

            paymentGenerationForm.setSearchParameterForm(searchParameterForm);

//            paymentGenerationForm.setPaymentType(this.paymentTypeService.getPaymentTypeList(true, "01").get(0));
            paymentGenerationForm.setCreatedBy((User) session.getAttribute("user"));
            Integer returnValue = this.paymentService.generatePaymentData(paymentGenerationForm);
            payrollSummary = this.paymentService.getPayrollSummary(returnValue);

            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            model.addAttribute("isSuccess", true);
            model.addAttribute("message", localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            model.addAttribute("payrollSummary", payrollSummary);
        } catch (Exception e) {
            System.out.println("Test = ");
            System.out.println("" + e.getLocalizedMessage());
            e.printStackTrace();
            //logger.infoer(e.getMessage());
            model.addAttribute("isSuccess", false);
            model.addAttribute("message", "Error in creating Payroll !");

        }
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapFiscalYearName(model);
        CommonUtility.mapPaymentTypeName(model);
        model.addAttribute("actionType", "submit");

        model.addAttribute("paymentGenerationForm", paymentGenerationForm);

        return "childPayment";

//        return "redirect:/paymentGeneration/list";
    }

    public boolean exportPaymentDataToSPBMU(Integer paymentCycleId, String selectedPayrollSummaryIdList) throws ExceptionWrapper {
        try {

            PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(paymentCycleId);
            ServiceSettings serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.PAYMENTCYCLE);
            String token = loginSPBMU(serviceSettings);

            if (token == null) {
                throw new ExceptionWrapper("Web Service Authentication error");
            }
            // Export Payment Cycle Start-------------------
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            JSONObject json = new JSONObject();
            Map cycleData = new HashMap<String, Object>() {
                {
//                    String cycleName = paymentCycle.getNameInEnglish();
//                    if (paymentCycle.getScheme().getId() == 1) {
//                        cycleName = cycleName + "-Rural";
//                    } else if (paymentCycle.getScheme().getId() == 3) {
//                        cycleName = cycleName + "-Urban";
//                    }
                    put("allowanceAmount", paymentCycle.getAllowanceAmount());
                    put("approvalComment", "Approved");
                    put("approvalStatus", 1);
                    put("cycleName", paymentCycle.getNameInEnglish());
//                    put("cycleName", cycleName);
                    put("startDate", formatter.format(paymentCycle.getStartDate().getTime()));
                    put("endDate", formatter.format(paymentCycle.getEndDate().getTime()));
                    put("fiscalYear", paymentCycle.getFiscalYear().getNameInEnglish());
                    put("id", paymentCycle.getId());
                    put("ministryCode", paymentCycle.getMinistryCode());
//                    put("schemeCode", new Integer(paymentCycle.getScheme().getCode()));
                    //put("schemeCode", 3555);
                    put("schemeCode", new Integer("3555"));
                    put("totalBeneficiary", 0);

                }
            };
            json.put("token", token);
            json.put("cycle", new JSONObject(cycleData));

            System.out.println("json = " + json.toString());

            URL url = new URL(serviceSettings.getServiceURL() + token);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            try ( DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(json.toString());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            System.out.println("responseCode payment cycle = " + responseCode);
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    System.out.println("payment cycle api result = " + jsonResponse);
                    if (jsonResponse.length() == 0) {
                        throw new ExceptionWrapper("Operation Failed: Response is empty");
                    }
                    break;
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    throw new ExceptionWrapper("Operation failed: HTTP BAD REQUEST");
            }
            //Export Payment Cycle Done ----------------------------------
            // Export Payment Data Start ---------------------------------

            json = new JSONObject();
            JSONArray payListArray = new JSONArray();
            String[] payrollSummaryIdArray = selectedPayrollSummaryIdList.split(",");
            for (String payrollSummaryId : payrollSummaryIdArray) {
                System.out.println("payrollSummaryId = " + payrollSummaryId);
                List<PaymentInfoForSpbmu> paymentInfoForSpbmuList = this.paymentService.getPaymentInfoForSpbmu(new Integer(payrollSummaryId));
                System.out.println("PaymentInfoForSpbmu Data , size " + paymentInfoForSpbmuList.size());
                for (PaymentInfoForSpbmu paymentInfoForSpbmu : paymentInfoForSpbmuList) {
                    //review this code
                    paymentInfoForSpbmu.setSchemeCode(3555);
                    payListArray.put(new JSONObject(paymentInfoForSpbmu));
                }

                System.out.println("PaymentInfoForSpbmu Data , size " + paymentInfoForSpbmuList.size());
            }

            json.put("token", token);
            json.put("info", payListArray);

            System.out.println("json = " + json.toString());

            serviceSettings = serviceSettingsService.getServiceSettings(ServiceType.PAYROLL);
            url = new URL(serviceSettings.getServiceURL() + token);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            System.out.println("export response code = " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println("paylist export api result = " + jsonResponse);
                if ("true".equals(jsonResponse.get("operationResult").toString())) {
                    return true;
                } else {
                    throw new ExceptionWrapper("Operation failed: " + jsonResponse.get("errorMsg"));
                }
            } else {
                throw new ExceptionWrapper("Operation failed: HTTP Connection Exception");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExceptionWrapper(ex.getMessage());
        }

    }

}
