/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.selection.controller;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.ImageBanner;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.wfp.lmmis.applicant.controller.BeneficiaryController;
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantView;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.CommentType;
import com.wfp.lmmis.enums.StageType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.FactoryService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.report.controller.ReportUtility;
import com.wfp.lmmis.report.data.ApplicantReportData;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.selection.model.SelectionComments;
import com.wfp.lmmis.selection.service.SelectionCommentsService;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author user
 */
@Controller
public class UpazilaVerificationController {

    @Autowired
    ApplicantService applicantService;
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
    FactoryService factoryService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    BeneficiaryController beneficiaryController;
    @Autowired
    SelectionCommentsService selectionCommentsService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/upazilaVerification/union", method = RequestMethod.GET)
    public String getUpazilaVerificationUnion(Model model, HttpServletRequest request, HttpSession session) {
        List<ItemObject> stateList = new ArrayList();
        stateList.add(new ItemObject(0, "যাচাই পেন্ডিং", "Verification Pending"));
        stateList.add(new ItemObject(1, "যাচাই কমপ্লিট", "Verification Completed"));
        model.addAttribute("stateList", stateList);
        CommonUtility.mapDivisionName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.UNION);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "upazilaVerificationUnion";
    }

    @RequestMapping(value = "/upazilaVerification/municipal", method = RequestMethod.GET)
    public String getUpazilaVerificationMunicipal(Model model, HttpServletRequest request, HttpSession session) {
        List<ItemObject> stateList = new ArrayList();
        stateList.add(new ItemObject(0, "যাচাই পেন্ডিং", "Verification Pending"));
        stateList.add(new ItemObject(1, "যাচাই কমপ্লিট", "Verification Completed"));
        model.addAttribute("stateList", stateList);
        CommonUtility.mapDivisionName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.MUNICIPAL);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "upazilaVerificationMunicipal";
    }

    @RequestMapping(value = "/dwaoVerification", method = RequestMethod.GET)
    public String getVerificationCC(Model model, HttpServletRequest request) {
        List<ItemObject> stateList = new ArrayList<ItemObject>();
        stateList.add(new ItemObject(0, "যাচাই পেন্ডিং", "Verification Pending"));
        stateList.add(new ItemObject(1, "যাচাই কমপ্লিট", "Verification Completed"));
        model.addAttribute("stateList", stateList);

        CommonUtility.mapDivisionName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.CITYCORPORATION);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "districtVerificationCC";
    }

    @RequestMapping(value = "/bgmeaVerification", method = RequestMethod.GET)
    public String getVerificationBgmea(Model model, HttpServletRequest request) {
        List<ItemObject> stateList = new ArrayList<ItemObject>();
        stateList.add(new ItemObject(0, "যাচাই পেন্ডিং", "Verification Pending"));
        stateList.add(new ItemObject(1, "যাচাই কমপ্লিট", "Verification Completed"));
        model.addAttribute("stateList", stateList);

        CommonUtility.mapBGMEAFactoryName(model);
        model.addAttribute("type", "bgmea");
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.BGMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "bgmeaBkmeaVerification";
    }

    @RequestMapping(value = "/bkmeaVerification", method = RequestMethod.GET)
    public String getVerificationBkmea(Model model, HttpServletRequest request) {
        List<ItemObject> stateList = new ArrayList<ItemObject>();
        stateList.add(new ItemObject(0, "যাচাই পেন্ডিং", "Verification Pending"));
        stateList.add(new ItemObject(1, "যাচাই কমপ্লিট", "Verification Completed"));
        model.addAttribute("stateList", stateList);

        CommonUtility.mapBKMEAFactoryName(model);
        model.addAttribute("type", "bkmea");
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.BKMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "bgmeaBkmeaVerification";
    }

    @RequestMapping(value = "/verification/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationVerification(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws IOException, JSONException {
        try {
            //Fetch the page number from client
            JSONObject jsonResult = new JSONObject();
            JSONArray dataArray = new JSONArray();
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            String state = request.getParameter("state");
            String nid = request.getParameter("nid");
            ApplicantType applicantType = null;
            //String applicantIdSt = request.getParameter("applicantId1");
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
            String bgmeaFactoryId = request.getParameter("bgmeaFactoryId");
            String bkmeaFactoryId = request.getParameter("bkmeaFactoryId");
            if (request.getParameter("applicantType") != null) {
                applicantType = (ApplicantType.valueOf(request.getParameter("applicantType")));
            }
            if ("".equals(unionId)) {
                User user = (User) session.getAttribute("user");
                unionId = user.getUnion().getId().toString();
            }

            Map parameter = new HashMap();
            parameter.put("nid", nid != null && !"".equals(nid) ? (String) nid : null);
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
            parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);
            parameter.put("applicantType", applicantType);

            List<ApplicationStatus> applicationStatusList = new ArrayList<>();
            //0 for pending, 1 for processing
            if ("0".equals(state)) {
                applicationStatusList.add(ApplicationStatus.VERIFICATION_PENDING);
            } else if ("1".equals(state)) {
                applicationStatusList.add(ApplicationStatus.VERIFICATION_APPROVED);
                applicationStatusList.add(ApplicationStatus.VERIFICATION_REJECTED);
            }
            parameter.put("applicationStatusList", applicationStatusList);

            List<Object> resultList = applicantService.getApplicantListBySearchParameter(parameter, beginIndex, pageSize);;
            List<ApplicantView> applicantList = (List<ApplicantView>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);
            for (ApplicantView applicant : applicantList) {
                JSONArray ja = new JSONArray();
                ja.put("<input type=\"checkbox\" class=\"checkbox\" id=\"" + applicant.getId() + "\" name=\"" + applicant.getId() + "\">");
                Locale locale = LocaleContextHolder.getLocale();
                if ("en".equals(locale.getLanguage())) {
//                ja.put((applicant.getFirstNameInEnglish() == null ? ' ' : applicant.getFirstNameInEnglish()) + " " + (applicant.getMiddleNameInEnglish() == null ? ' ' : applicant.getMiddleNameInEnglish()) + " " + (applicant.getLastNameInEnglish() == null ? ' ' : applicant.getLastNameInEnglish()));
                    ja.put(applicant.getFullNameInEnglish());
                    ja.put(applicant.getNid().toString());
                    ja.put(applicant.getMobileNo() != null ? applicant.getMobileNo().toString() : "");
                    ja.put(CalendarUtility.getDateString(applicant.getDateOfBirth()));
                } else {
//                ja.put((applicant.getFirstNameInBangla() == null ? ' ' : applicant.getFirstNameInBangla()) + " " + (applicant.getMiddleNameInBangla() == null ? ' ' : applicant.getMiddleNameInBangla()) + " " + (applicant.getLastNameInBangla() == null ? ' ' : applicant.getLastNameInBangla()));
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

                if (applicant.getRecommendationStatus() == null) {
                    applicant.setRecommendationStatus(false);
                }
                if (applicant.getRecommendationStatus().equals(Boolean.TRUE)) {
                    if ("en".equals(locale.getLanguage())) {
                        ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%'>Recommended</span>");
                    } else {
                        ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%'>সুপারিশকৃত</span>");
                    }
                } else {
                    if ("en".equals(locale.getLanguage())) {
                        ja.put("<span class=\"label\" style='background-color: #8b0000;font-size:100%'>Rejected</span>");
                    } else {
                        ja.put("<span class=\"label\" style='background-color: #8b0000;font-size:100%'>বাতিল</span>");
                    }
                }
                if (applicant.getApplicationStatus().equals(ApplicationStatus.VERIFICATION_APPROVED)) {
                    if ("en".equals(locale.getLanguage())) {
                        ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%'>Approved</span>");
                    } else {
                        ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%'>অনুমোদিত</span>");
                    }
                } else if (applicant.getApplicationStatus().equals(ApplicationStatus.VERIFICATION_REJECTED)) {
                    if ("en".equals(locale.getLanguage())) {
                        ja.put("<span class=\"label\" style='background-color: #8b0000;font-size:100%'>Rejected</span>");
                    } else {
                        ja.put("<span class=\"label\" style='background-color: #8b0000;font-size:100%'>বাতিল</span>");
                    }
                } else {
                    ja.put("");
                }
                ja.put("en".equals(locale.getLanguage()) ? applicant.getSystemRecommendedStatus() : applicant.getSystemRecommendedStatus().getDisplayNameBn());
//            Map commentParameter = new HashMap();
//            System.out.println("applicant id "+ applicant.getId());
//            System.out.println("applicant size "+ applicantList.size());
//            commentParameter.put("applicantId", applicant.getId());
//            List<SelectionComments> selectionComments = this.selectionCommentsService.getSelectionCommentsList(commentParameter);
//            
//            if (selectionComments != null && selectionComments.size() > 0)
//            {
//                String viewDetails = localizer.getLocalizedText("viewDetails", LocaleContextHolder.getLocale());
//                ja.put("<a href=\"applicant/viewApplicant/" + applicant.getId() + "\" data-toggle=\"modal\" >\n"
//                        + "<span class=\"glyphicon glyphicon-eye-open\"></span> \n"
//                        + "</a>||<a href=#" + " onclick=loadComment(" + applicant.getId() + ") title=\"" + viewDetails + "\"><span class=\"glyphicon glyphicon-comment\"></span></a>");
//            }
//            else
//            {
//                ja.put("<a href=\"applicant/viewApplicant/" + applicant.getId() + "\" data-toggle=\"modal\" >\n"
//                        + "                                                <span class=\"glyphicon glyphicon-eye-open\"></span> \n"
//                        + "                                            </a>");
//            }
//            ja.put("<a href=\"applicant/viewApplicant/" + applicant.getId() + "\" data-toggle=\"modal\" >\n"
//                    + "                                                <span class=\"glyphicon glyphicon-eye-open\"></span> \n"
//                    + "                                            </a>");
//ja.put("<a href=#" + " onclick=loadApplicant(" + applicant.getId() + ")><span class=\"glyphicon glyphicon-eye-open\"></span></a>");
//ja.put("<a href=\"" + request.getContextPath() + "/applicant/viewApplicant/" + applicant.getId() + "\" ><span class=\"glyphicon glyphicon-edit\"></span></a>");
                String viewDetails = localizer.getLocalizedText("viewDetails", LocaleContextHolder.getLocale());
                ja.put("<a href=#" + " onclick=loadApplicant(" + applicant.getId() + ") title=\"" + viewDetails + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>");
                dataArray.put(ja);
            }

            jsonResult.put("recordsTotal", recordsTotal);
            jsonResult.put("recordsFiltered", recordsFiltered);
            jsonResult.put("data", dataArray);
            jsonResult.put("draw", draw);
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-store");
            PrintWriter out;

            out = response.getWriter();
            out.print(jsonResult);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Object changeFieldVerificationStatus(Model model, HttpSession session,
            HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) throws ColumnBuilderException, ClassNotFoundException, JRException, IOException {
        String returnMsg = "";
        String action = request.getParameter("action");

        ApplicantType applicantType = null;
        String factoryId = null;
        Locale locale = LocaleContextHolder.getLocale();
        if (request.getParameter("selectedApplicantType") != null && !request.getParameter("selectedApplicantType").equals("")) {
            applicantType = (ApplicantType.valueOf(request.getParameter("selectedApplicantType")));
        }
        if (request.getParameter("selectedFactoryId") != null) {
            factoryId = request.getParameter("selectedFactoryId");
        }
        if ("accept".equals(action)) {
            String approvedApplicantList = request.getParameter("approvedApplicantList");
            String bgmeaFactoryId = request.getParameter("selectedBgmea");
            String bkmeaFactoryId = request.getParameter("selectedBkmea");
            if (!"".equals(approvedApplicantList)) {
                String reason = request.getParameter("reasonFV");
                String[] applicantArray = approvedApplicantList.split(",");
                for (int i = 0; i < applicantArray.length; i++) {
                    Applicant applicant = applicantService.getApplicant(new Integer(applicantArray[i]));
                    applicant.setApplicationStatus(ApplicationStatus.VERIFICATION_APPROVED);
                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
                    applicantService.edit(applicant);
                    StageType stageType = null;
                    if (bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) || bkmeaFactoryId != null && !"".equals(bkmeaFactoryId)) {
                        stageType = StageType.FACTORY_VERIFICATION;
                    } else {
                        stageType = StageType.FINAL_APPROVAL;
                    }
                    SelectionComments selectionComments = new SelectionComments(null, CommentType.APPROVED, reason, stageType, applicant, (User) session.getAttribute("user"), Calendar.getInstance());
                    this.selectionCommentsService.save(selectionComments);
                }

                System.out.println("----------Bgmea factory id = " + bgmeaFactoryId);
                ControllerMessage controllerMessage;
                if (bgmeaFactoryId != null && !"".equals(bgmeaFactoryId)) {
                    controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("recommendUpazilaPresidentVerification", LocaleContextHolder.getLocale()));
                } else if (bkmeaFactoryId != null && !"".equals(bkmeaFactoryId)) {
                    controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("recommendUpazilaPresidentVerification", LocaleContextHolder.getLocale()));
                } else {
                    controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("approveUpazilaPresidentVerification", LocaleContextHolder.getLocale()));

                }
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }
        }
        if ("reject".equals(action)) {
            String rejectedApplicantList = request.getParameter("rejectedApplicantList");
            if (!"".equals(rejectedApplicantList)) {
                String reason = request.getParameter("reasonFV");
                String[] applicantArray = rejectedApplicantList.split(",");
                for (int i = 0; i < applicantArray.length; i++) {
                    Applicant applicant = applicantService.getApplicant(new Integer(applicantArray[i]));
                    applicant.setApplicationStatus(ApplicationStatus.VERIFICATION_REJECTED);
                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
                    applicantService.edit(applicant);
                    SelectionComments selectionComments = new SelectionComments(null, CommentType.REJECTED, reason, StageType.FIELD_VERIFICATION, applicant, (User) session.getAttribute("user"), Calendar.getInstance());
                    this.selectionCommentsService.save(selectionComments);
                }
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("rejectUpazilaPresidentVerification", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }
        }
        if ("advance".equals(action)) {
//            String unionId = request.getParameter("unionId");
            String unionId = request.getParameter("unionIdHidden");
            String bgmeaFactoryId = request.getParameter("selectedBgmea");
            String bkmeaFactoryId = request.getParameter("selectedBkmea");
            if (unionId == null || unionId.equals("0") || unionId.equals("")) {
                returnMsg = "Union is not seleted ";

            } else {

                Map parameter = new HashMap();
                parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
                parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
                parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);
                parameter.put("applicantType", applicantType);

                List<ApplicationStatus> applicationStatusList = new ArrayList<>();
                //0 for pending, 1 for processing
                applicationStatusList.add(ApplicationStatus.VERIFICATION_APPROVED);
                applicationStatusList.add(ApplicationStatus.VERIFICATION_REJECTED);
                parameter.put("applicationStatusList", applicationStatusList);
                List<Object> resultList = applicantService.getApplicantListBySearchParameter(parameter, 0, 1000000);;
                List<ApplicantView> applicantList = (List<ApplicantView>) resultList.get(0);

                int totalApplicant = applicantList.size();
                int totlaSuccess = 0;
                int totlaSuccessToReject = 0;
                int totalFailedForDuplicateAccount = 0;
                int totalFailedForBeneficiaryCreation = 0;
                UserDetail loggedUser = (UserDetail) session.getAttribute("userDetail");
                for (ApplicantView applicantView : applicantList) {

                    Applicant applicant = applicantService.getApplicant(applicantView.getId());

                    String accountMsg = "Account";
                    if (!validationForBeneficiaryAdd(applicant)) {
                        //   returnMsg = returnMsg + " NID: " + applicant.getNid() + " get duplicate Account Number,";
                        totalFailedForDuplicateAccount = totalFailedForDuplicateAccount + 1;
                        continue;
                    }
                    if (applicant.getApplicantType() == ApplicantType.BGMEA || applicant.getApplicantType() == ApplicantType.BKMEA) {
                        applicant.setApplicationStatus(ApplicationStatus.DWA_APPROVAL_PENDING);
                    } else {
                        if (applicant.getApplicationStatus() == ApplicationStatus.VERIFICATION_APPROVED) {

                            if (!applicant.getPresentUpazila().getId().equals(loggedUser.getUpazila().getId())) {

                                continue;
                            }
                            applicant.setApplicationStatus(ApplicationStatus.SELECTED_AT_FINAL_APPROVAL);
//                        System.out.println("fiscal year 1 "+ applicant.getFiscalYear().getNameInEnglish());
                            if (!beneficiaryController.saveBeneficiary(applicant, session, request)) {
                                //returnMsg = returnMsg + "NID " + applicant.getNid() + " : Failed To create Beneficiary,";
                                totalFailedForBeneficiaryCreation = totalFailedForBeneficiaryCreation + 1;
                                continue;
                            }
                        } else {
                            applicant.setApplicationStatus(ApplicationStatus.REJECTED_AT_FINAL_APPROVAL);
                            totlaSuccessToReject = totlaSuccessToReject + 1;
                        }
                    }

                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
//                                        System.out.println("fiscal year 5 "+ applicant.getFiscalYear().getNameInEnglish());
                    applicantService.edit(applicant);
                    totlaSuccess = totlaSuccess + 1;
                }
                returnMsg = "মোট " + totalApplicant + " জন আবেদনকারীর মধ্যে ,"
                        + totlaSuccess + " জনকে সফল ভাবে ভাতাভোগী বানানো হয়েছে।"
                        + totlaSuccessToReject + " জনকে চূড়ান্ত ভাবে বাতিল করা হয়েছে।"
                        + totalFailedForDuplicateAccount + " জনের হিসেব নম্বর ইতোমধ্যে ব্যবহার হয়েছে।"
                        + totalFailedForBeneficiaryCreation + " জনের ভাতাভোগী বানাতে ব্যর্থ হয়েছে।";
            }

            ControllerMessage controllerMessage;

//            if (bgmeaFactoryId != null && !"".equals(bgmeaFactoryId)) {
//                String msg =  localizer.getLocalizedText("sentForBGMEAApproval", LocaleContextHolder.getLocale());
//                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS,msg);
//            } else if (bkmeaFactoryId != null && !"".equals(bkmeaFactoryId)) {
//                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForBKMEAApproval", LocaleContextHolder.getLocale()));
//            } else {
//                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("recordedAsBeneficiary", LocaleContextHolder.getLocale()));
//            }
            if (bgmeaFactoryId != null && !"".equals(bgmeaFactoryId)) {
                String msg = localizer.getLocalizedText("sentForBGMEAApproval", LocaleContextHolder.getLocale());
                if (!returnMsg.equals("")) {
                    msg = msg + localizer.getLocalizedText("messageAccountNoDuplicateNID", LocaleContextHolder.getLocale()) + " " + returnMsg;
                }
                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, msg);
                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForBKMEAApproval", LocaleContextHolder.getLocale()));
            } else if (bkmeaFactoryId != null && !"".equals(bkmeaFactoryId)) {
                String msg = localizer.getLocalizedText("sentForBKMEAApproval", LocaleContextHolder.getLocale());
                if (!returnMsg.equals("")) {
                    msg = msg + localizer.getLocalizedText("messageAccountNoDuplicateNID", LocaleContextHolder.getLocale()) + " " + returnMsg;
                }
                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, msg);

//                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForBKMEAApproval", LocaleContextHolder.getLocale()));
            } else {
                String msg = localizer.getLocalizedText("recordedAsBeneficiary", LocaleContextHolder.getLocale());
                if (!returnMsg.equals("")) {
//                    msg = msg + localizer.getLocalizedText("messageAccountNoDuplicateNID", LocaleContextHolder.getLocale()) + " " + returnMsg;
                    msg = msg + returnMsg;
                }
                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, msg);
                //controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("recordedAsBeneficiary", LocaleContextHolder.getLocale()));
            }
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        if ("print".equals(action)) {
            String unionId = null;
            List<ApplicationStatus> statusList = new ArrayList<>();
            if (request.getParameter("selectedUnionIdPrint") != null) {
                unionId = request.getParameter("selectedUnionIdPrint");
            }
            if (request.getParameter("selectedFactoryIdPrint") != null) {
                factoryId = request.getParameter("selectedFactoryIdPrint");
            }
            if (request.getParameter("selectedApplicantTypePrint") != null) {
                applicantType = (ApplicantType.valueOf(request.getParameter("selectedApplicantTypePrint")));
            }
            if (request.getParameter("selectedStatusPrint") != null) {
                if (request.getParameter("selectedStatusPrint").equals("0")) {
                    statusList.add(ApplicationStatus.VERIFICATION_PENDING);
                } else if (request.getParameter("selectedStatusPrint").equals("1")) {
                    statusList.add(ApplicationStatus.VERIFICATION_APPROVED);
                    statusList.add(ApplicationStatus.VERIFICATION_REJECTED);
                }
            }

            Map parameter = new HashMap();
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("factoryId", factoryId != null && !"".equals(factoryId) ? Integer.valueOf(factoryId) : null);
            parameter.put("statusList", statusList);
            parameter.put("applicantType", applicantType);
            parameter.put("locale", locale);
            parameter.put("stageType", StageType.FINAL_APPROVAL);

            List<ApplicantReportData> applicantReportDataList = this.applicantService.getApplicantReportDataForPrint(parameter);
            JRDataSource ds = new JRBeanCollectionDataSource(applicantReportDataList);
            JasperPrint jp = null;
            ResponseEntity<byte[]> responseEntity = null;
            jp = getReport(ds, request, applicantReportDataList.isEmpty());
            byte[] contents = ReportUtility.getPDFContents(jp, response);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            return responseEntity;
        }

        Map redirectParameter = new HashMap();
        redirectParameter.put("applicantType", applicantType);

        switch (applicantType) {
            case UNION:
                return "redirect:/upazilaVerification/union";
            case MUNICIPAL:
                return "redirect:/upazilaVerification/municipal";
            case CITYCORPORATION:
                return "redirect:/dwaoVerification";
            case BGMEA:
                return "redirect:/bgmeaVerification";
            case BKMEA:
                return "redirect:/bkmeaVerification";
            default:
                return null;
        }
//        return getFieldVerificationPage(model);
    }

    private boolean validationForBeneficiaryAdd(Applicant applicant) {

        return this.applicantService.checkUniqueAccountNumber(applicant.getAccountNo());
    }

    public JasperPrint getReport(JRDataSource ds, HttpServletRequest request, boolean emptyData) throws ColumnBuilderException, JRException, ClassNotFoundException {
        DynamicReport dynaReport = null;
        dynaReport = getApplicantDetailDynamicReport(request, emptyData);
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), ds);
        return jp;
    }

    private DynamicReport getApplicantDetailDynamicReport(HttpServletRequest request, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException {
        try {
            DynamicReportBuilder drb = new DynamicReportBuilder();

            Style titleStyle = ReportUtility.createTitleStyle();
            Style subTitleStyle = ReportUtility.createSubTitleStyle();
            Style headerStyle = ReportUtility.createHeaderStyle();
            Style detailTextStyle = ReportUtility.createDetailTextStyle();
            Style detailNumStyle = ReportUtility.createDetailNumberStyle();

            Locale locale = LocaleContextHolder.getLocale();

            if (!emptyData) {
                AbstractColumn columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);

                String name = localizer.getLocalizedText("label.name", locale);
                AbstractColumn columnBenName = ReportUtility.createColumn("benName", String.class, name, 30, headerStyle, detailTextStyle);
                String nid = localizer.getLocalizedText("label.nid", locale);
                AbstractColumn columnNID = ReportUtility.createColumn("nationalID", String.class, nid, 30, headerStyle, detailTextStyle);
                String fatherName = localizer.getLocalizedText("label.fatherName", locale);
                AbstractColumn columnFatherName = ReportUtility.createColumn("fatherName", String.class, fatherName, 30, headerStyle, detailTextStyle);
                String motherName = localizer.getLocalizedText("label.motherName", locale);
                AbstractColumn columnMotherName = ReportUtility.createColumn("motherName", String.class, motherName, 30, headerStyle, detailTextStyle);
                String spouseName = localizer.getLocalizedText("label.spouseName", locale);
                AbstractColumn columnSpouseName = ReportUtility.createColumn("spouseName", String.class, spouseName, 30, headerStyle, detailTextStyle);
                String address = localizer.getLocalizedText("label.address", locale);
                AbstractColumn columnAddress = ReportUtility.createColumn("address", String.class, address, 30, headerStyle, detailTextStyle);
                String mobileNo = localizer.getLocalizedText("label.mobileNo", locale);
                AbstractColumn columnMobileNo = ReportUtility.createColumn("mobileNo", String.class, mobileNo, 30, headerStyle, detailTextStyle);
                String score = localizer.getLocalizedText("label.score", locale);
                AbstractColumn columnScore = ReportUtility.createColumn("score", String.class, score, 30, headerStyle, detailTextStyle);
                String socioEconomicInfo = localizer.getLocalizedText("label.socioEconomicInfo", locale);
                AbstractColumn columnSocioEconomicInfo = ReportUtility.createColumn("socioEconomicInfo", String.class, socioEconomicInfo, 50, headerStyle, detailTextStyle);
                String status = localizer.getLocalizedText("label.recommendationByIMLMACommittee", locale);
                AbstractColumn columnRecommendation = ReportUtility.createColumn("approvalStatus", String.class, status, 30, headerStyle, detailTextStyle);
                String remarks = localizer.getLocalizedText("label.remarks", locale);
                AbstractColumn columnRemarks = ReportUtility.createColumn("remarks", String.class, remarks, 30, headerStyle, detailTextStyle);

                drb.addColumn(columnSerialNumber).addColumn(columnBenName).addColumn(columnNID).addColumn(columnFatherName).addColumn(columnMotherName).addColumn(columnSpouseName).addColumn(columnAddress)
                        .addColumn(columnMobileNo).addColumn(columnSocioEconomicInfo).addColumn(columnRecommendation).addColumn(columnRemarks);
                System.out.println("COLUMN ADDED = ");
                StyleBuilder oddRowBgStyle = new StyleBuilder(true);
                oddRowBgStyle.setBackgroundColor(Color.darkGray);
                drb.setOddRowBackgroundStyle(oddRowBgStyle.build());

                drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            }

            String beneficiaryReport = localizer.getLocalizedText("label.finalSelectionList", locale);
            drb.setTitle(beneficiaryReport);
            drb.setTitleStyle(titleStyle);

            String subTitle = "";

            if (request.getParameter("selectedUnionIdPrint") != null && request.getParameter("selectedUnionIdPrint") != "") {
                String unionSt = "";
                if (ApplicantType.valueOf(request.getParameter("selectedApplicantTypePrint")).equals(ApplicantType.UNION)) {
                    unionSt = unionSt = localizer.getLocalizedText("label.union", locale);
                } else if (ApplicantType.valueOf(request.getParameter("selectedApplicantTypePrint")).equals(ApplicantType.MUNICIPAL)) {
                    unionSt = localizer.getLocalizedText("label.municipal", locale);
                } else if (ApplicantType.valueOf(request.getParameter("selectedApplicantTypePrint")).equals(ApplicantType.CITYCORPORATION)) {
                    unionSt = localizer.getLocalizedText("label.cityCorporation", locale);
                }
                Union union = this.unionService.getUnion(Integer.parseInt(request.getParameter("selectedUnionIdPrint")));
                subTitle += unionSt + " : " + (locale.toString().equals("bn") == true ? union.getNameInBangla() : union.getNameInEnglish());
                subTitle += "\\n";
            }
            if (request.getParameter("selectedFactoryIdPrint") != null) {
                String factorySt = localizer.getLocalizedText("label.factory", locale);
                Factory factory = this.factoryService.getFactory(Integer.parseInt(request.getParameter("selectedFactoryIdPrint")));
                subTitle += factorySt + " : " + (locale.toString().equals("bn") == true ? factory.getNameInBangla() : factory.getNameInEnglish());
                subTitle += "\\n";
            }
            if (request.getParameter("selectedStatusPrint") != null) {
                String statusSt = localizer.getLocalizedText("label.status", locale);
                if (request.getParameter("selectedStatusPrint").equals("0")) {
                    subTitle += statusSt + " : " + (locale.toString().equals("bn") == true ? "পেন্ডিং" : "Pending");
                } else {
                    subTitle += statusSt + " : " + (locale.toString().equals("bn") == true ? "কমপ্লিট" : "Complete");
                }
                subTitle += "\\n";
            }
            drb.setSubtitle(subTitle);
            drb.setSubtitleStyle(subTitleStyle);
            drb.setUseFullPageWidth(true);
            drb.setRightMargin(30);
            drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
            drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
            drb.setLeftMargin(30);
            drb.setRightMargin(30);
            drb.setTopMargin(20);
            drb.setBottomMargin(20);

            if (locale.toString().equals("bn")) {
                drb.addFirstPageImageBanner("image/banner_bn3.png", 406, 55, ImageBanner.ALIGN_CENTER);
            } else {
                drb.addFirstPageImageBanner("image/banner_en3.png", 406, 55, ImageBanner.ALIGN_CENTER);
            }
            String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
            drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle(), true, true);
            return drb.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/checkUnionVerificationEligibility/{unionName}")
    @ResponseBody
    public boolean checkUnionEligibility(@PathVariable("unionName") Integer unionName, HttpSession session,
            Model map) throws ExceptionWrapper {
        System.out.println("union id " + unionName);
        boolean isEligible = applicantService.eligibilityToForwardFromUpazilaStage(unionName, ApplicantType.UNION);
        return isEligible;
    }

    @RequestMapping("/checkMunicipalVerificationEligibility/{unionName}")
    @ResponseBody
    public boolean checkMunicipalEligibility(@PathVariable("unionName") Integer unionName, HttpSession session,
            Model map) throws ExceptionWrapper {
        System.out.println("union id " + unionName);
        boolean isEligible = applicantService.eligibilityToForwardFromUpazilaStage(unionName, ApplicantType.MUNICIPAL);
        return isEligible;
    }

    @RequestMapping("/checkCCVerificationEligibility/{unionName}")
    @ResponseBody
    public boolean checkCCEligibility(@PathVariable("unionName") Integer unionName, HttpSession session,
            Model map) throws ExceptionWrapper {
        System.out.println("union id " + unionName);
        boolean isEligible = applicantService.eligibilityToForwardFromUpazilaStage(unionName, ApplicantType.CITYCORPORATION);
        return isEligible;
    }

    @RequestMapping("/checkBGMEAVerificationEligibility/{bgmeaFactoryId}")
    @ResponseBody
    public boolean checkBGMEAEligibility(@PathVariable("bgmeaFactoryId") Integer bgmeaFactoryId, HttpSession session,
            Model map) throws ExceptionWrapper {
        System.out.println("bgmeaFactoryId id " + bgmeaFactoryId);
        boolean isEligible = applicantService.eligibilityToForwardFromUpazilaStage(bgmeaFactoryId, ApplicantType.BGMEA);
        return isEligible;
    }

    @RequestMapping("/checkBKMEAVerificationEligibility/{bkmeaFactoryId}")
    @ResponseBody
    public boolean checkBKMEAEligibility(@PathVariable("bkmeaFactoryId") Integer bkmeaFactoryId, HttpSession session,
            Model map) throws ExceptionWrapper {
        System.out.println("bkmeaFactoryId id " + bkmeaFactoryId);
        boolean isEligible = applicantService.eligibilityToForwardFromUpazilaStage(bkmeaFactoryId, ApplicantType.BKMEA);
        return isEligible;
    }

    public JasperPrint getReport(JRDataSource ds, ApplicantReportData applicantReportData) throws ColumnBuilderException, JRException, ClassNotFoundException {
        DynamicReport dynaReport = null;
        dynaReport = getApplicantDetailDynamicReport(applicantReportData);
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), ds);
        return jp;
    }

    private DynamicReport getApplicantDetailDynamicReport(ApplicantReportData applicantReportData) throws ColumnBuilderException, ClassNotFoundException {
        try {
            DynamicReportBuilder drb = new DynamicReportBuilder();

            Style titleStyle = ReportUtility.createTitleStyle();
            Style subTitleStyle = ReportUtility.createSubTitleStyle();
            Style headerStyle = ReportUtility.createHeaderStyle();
            Style detailTextStyle = ReportUtility.createDetailTextStyle();
            Style detailNumStyle = ReportUtility.createDetailNumberStyle();

            Locale locale = LocaleContextHolder.getLocale();

            AbstractColumn columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);

            String name = localizer.getLocalizedText("label.name", locale);
            AbstractColumn columnBenName = ReportUtility.createColumn("benName", String.class, name, 30, headerStyle, detailTextStyle);
            String nid = localizer.getLocalizedText("label.nid", locale);
            AbstractColumn columnNID = ReportUtility.createColumn("nationalID", String.class, nid, 30, headerStyle, detailTextStyle);
            String fatherName = localizer.getLocalizedText("label.fatherName", locale);
            AbstractColumn columnFatherName = ReportUtility.createColumn("fatherName", String.class, fatherName, 30, headerStyle, detailTextStyle);
            String motherName = localizer.getLocalizedText("label.motherName", locale);
            AbstractColumn columnMotherName = ReportUtility.createColumn("motherName", String.class, motherName, 30, headerStyle, detailTextStyle);
            String spouseName = localizer.getLocalizedText("label.spouseName", locale);
            AbstractColumn columnSpouseName = ReportUtility.createColumn("spouseName", String.class, spouseName, 30, headerStyle, detailTextStyle);
            String mobileNo = localizer.getLocalizedText("label.mobileNo", locale);
            AbstractColumn columnMobileNo = ReportUtility.createColumn("mobileNo", String.class, mobileNo, 30, headerStyle, detailTextStyle);

            drb.addColumn(columnSerialNumber).addColumn(columnBenName).addColumn(columnNID).addColumn(columnFatherName).addColumn(columnMotherName).addColumn(columnSpouseName).addColumn(columnMobileNo);

            StyleBuilder oddRowBgStyle = new StyleBuilder(true);
            oddRowBgStyle.setBackgroundColor(Color.darkGray);
            drb.setOddRowBackgroundStyle(oddRowBgStyle.build());

            drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());

            String beneficiaryReport = localizer.getLocalizedText("label.upazilaMemberApproval", locale);
            drb.setTitle(beneficiaryReport);
            drb.setTitleStyle(titleStyle);

            String subTitle = "";
            Applicant applicant = this.applicantService.getApplicant(Integer.parseInt(applicantReportData.getBenID()));

            String schemeSt = localizer.getLocalizedText("label.scheme", locale);
            Scheme scheme = this.schemeService.getScheme(applicant.getScheme().getId());
            subTitle += schemeSt + " : " + (locale.toString().equals("bn") == true ? scheme.getNameInBangla() : scheme.getNameInEnglish());
            String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(applicant.getFiscalYear().getId());
            subTitle += ",\\t\\t" + fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish());
            String divisionSt = localizer.getLocalizedText("label.division", locale);
            Division division = this.divisionService.getDivision(applicant.getPermanentDivision().getId());
            subTitle += "\\n" + divisionSt + " : " + (locale.toString().equals("bn") == true ? division.getNameInBangla() : division.getNameInEnglish());
            String districtSt = localizer.getLocalizedText("label.district", locale);
            District district = this.districtService.getDistrict(applicant.getPermanentDistrict().getId());
            subTitle += ",\\t\\t" + districtSt + " : " + (locale.toString().equals("bn") == true ? district.getNameInBangla() : district.getNameInEnglish());
            String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
            Upazilla upazila = this.upazillaService.getUpazilla(applicant.getPermanentUpazila().getId());
            subTitle += ",\\t\\t" + upazilaSt + " : " + (locale.toString().equals("bn") == true ? upazila.getNameInBangla() : upazila.getNameInEnglish());
            String unionSt = localizer.getLocalizedText("label.union", locale);
            Union union = this.unionService.getUnion(applicant.getPermanentUnion().getId());
            subTitle += ",\\t\\t" + unionSt + " : " + (locale.toString().equals("bn") == true ? union.getNameInBangla() : union.getNameInEnglish());
            subTitle += "\\n";
            drb.setSubtitle(subTitle);
            drb.setSubtitleStyle(subTitleStyle);
            drb.setUseFullPageWidth(true);
            drb.setRightMargin(30);
            drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
            drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
            drb.setLeftMargin(30);
            drb.setRightMargin(30);
            drb.setTopMargin(20);
            drb.setBottomMargin(20);

            if (locale.toString().equals("bn")) {
                drb.addFirstPageImageBanner("image/banner_bn3.png", 406, 55, ImageBanner.ALIGN_CENTER);
            } else {
                drb.addFirstPageImageBanner("image/banner_en3.png", 406, 55, ImageBanner.ALIGN_CENTER);
            }
            drb.setWhenNoData("No Data Found for this report", ReportUtility.noDataStyle());
            return drb.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
