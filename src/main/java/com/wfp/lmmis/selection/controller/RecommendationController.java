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
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantView;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.CommentType;
import com.wfp.lmmis.enums.StageType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.service.FactoryService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
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
public class RecommendationController {

    @Autowired
    ApplicantService applicantService;
    @Autowired
    SelectionCommentsService selectionCommentsService;
    @Autowired
    UnionService unionService;
    @Autowired
    UpazillaService upazillaService;
    @Autowired
    FactoryService factoryService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/recommendation/union", method = RequestMethod.GET)
    public String getRecommendationUnion(Model model, HttpServletRequest request) throws ExceptionWrapper {
        List<ItemObject> stateList = new ArrayList<ItemObject>();
        stateList.add(new ItemObject(0, "সুপারিশ পেন্ডিং", "Recommendation Pending"));
        stateList.add(new ItemObject(1, "সুপারিশ কমপ্লিট", "Recommendation Completed"));
        model.addAttribute("stateList", stateList);

        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.UNION);
        model.addAttribute("searchParameterForm", searchParameterForm);
        CommonUtility.mapDivisionName(model);

        return "recommendationUnion";
    }

    @RequestMapping(value = "/recommendation/municipal", method = RequestMethod.GET)
    public String getRecommendationMunicipal(Model model, HttpServletRequest request) {
        List<ItemObject> stateList = new ArrayList<ItemObject>();
        stateList.add(new ItemObject(0, "সুপারিশ পেন্ডিং", "Recommendation Pending"));
        stateList.add(new ItemObject(1, "সুপারিশ কমপ্লিট", "Recommendation Completed"));
        model.addAttribute("stateList", stateList);

        CommonUtility.mapDivisionName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.MUNICIPAL);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "recommendationMunicipal";
    }

    @RequestMapping(value = "/recommendation/cityCorporation", method = RequestMethod.GET)
    public String getRecommendationCC(Model model, HttpServletRequest request) {
        List<ItemObject> stateList = new ArrayList<ItemObject>();
        stateList.add(new ItemObject(0, "সুপারিশ পেন্ডিং", "Recommendation Pending"));
        stateList.add(new ItemObject(1, "সুপারিশ কমপ্লিট", "Recommendation Completed"));
        model.addAttribute("stateList", stateList);

        CommonUtility.mapDivisionName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.CITYCORPORATION);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "recommendationCC";
    }

    @RequestMapping(value = "/recommendation/bgmea", method = RequestMethod.GET)
    public String getRecommendationBgmea(Model model, HttpServletRequest request) {
        List<ItemObject> stateList = new ArrayList<ItemObject>();
        stateList.add(new ItemObject(0, "সুপারিশ পেন্ডিং", "Recommendation Pending"));
        stateList.add(new ItemObject(1, "সুপারিশ কমপ্লিট", "Recommendation Completed"));
        model.addAttribute("stateList", stateList);

        CommonUtility.mapBGMEAFactoryName(model);
        model.addAttribute("type", "bgmea");
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.BGMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "recommendationBgmeaBkmea";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/recommendation/bkmea", method = RequestMethod.GET)
    public String getRecommendationBkmea(Model model, HttpServletRequest request) {
        List<ItemObject> stateList = new ArrayList<ItemObject>();
        stateList.add(new ItemObject(0, "সুপারিশ পেন্ডিং", "Recommendation Pending"));
        stateList.add(new ItemObject(1, "সুপারিশ কমপ্লিট", "Recommendation Completed"));
        model.addAttribute("stateList", stateList);

        CommonUtility.mapBKMEAFactoryName(model);
        model.addAttribute("type", "bkmea");
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.BKMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "recommendationBgmeaBkmea";
    }

    /**
     *
     * @param request
     * @param response
     * @param session
     * @throws IOException
     */
    @RequestMapping(value = "/recommendation/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationRecommendation(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws IOException {
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
            //  unionId = request.getParameter("unionIdHidden");

            if (unionId == null) {
                return;
            }
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
            applicationStatusList.add(ApplicationStatus.RECOMMENDATION_PENDING);
        } else if ("1".equals(state)) {
            applicationStatusList.add(ApplicationStatus.RECOMMENDATION_APPROVED);
            applicationStatusList.add(ApplicationStatus.RECOMMENDATION_REJECTED);
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
            if (applicant.getApplicationStatus().equals(ApplicationStatus.RECOMMENDATION_APPROVED)) {
                if ("en".equals(locale.getLanguage())) {
                    ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%'>Recommended</span>");
                } else {
                    ja.put("<span class=\"label\" style='background-color: #00a65a;font-size:100%'>সুপারিশকৃত</span>");
                }
            } else if (applicant.getApplicationStatus().equals(ApplicationStatus.RECOMMENDATION_REJECTED)) {
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

    /**
     *
     * @param model
     * @param session
     * @param request
     * @param redirectAttributes
     * @param response
     * @return
     * @throws ColumnBuilderException
     * @throws ClassNotFoundException
     * @throws JRException
     * @throws IOException
     */
    @RequestMapping(value = "/recommendation", method = RequestMethod.POST)
    public Object changeFieldVerificationStatus(Model model, HttpSession session,
            HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) throws ColumnBuilderException, ClassNotFoundException, JRException, IOException {
        String action = request.getParameter("action");
        ApplicantType applicantType = null;
        String factoryId = null;
        Locale locale = LocaleContextHolder.getLocale();
        if (request.getParameter("selectedApplicantType") != null) {
            applicantType = (ApplicantType.valueOf(request.getParameter("selectedApplicantType")));
        }
        if (request.getParameter("selectedFactoryId") != null) {
            factoryId = request.getParameter("selectedFactoryId");
        }
        if ("accept".equals(action)) {
            String approvedApplicantList = request.getParameter("approvedApplicantList");
            System.out.println("in submit fv" + approvedApplicantList);
            if (!"".equals(approvedApplicantList)) {
                String reason = request.getParameter("reasonFV");
                String[] applicantArray = approvedApplicantList.split(",");
                for (int i = 0; i < applicantArray.length; i++) {
                    Applicant applicant = applicantService.getApplicant(new Integer(applicantArray[i]));
                    applicant.setApplicationStatus(ApplicationStatus.RECOMMENDATION_APPROVED);
                    applicant.setRecommendationStatus(Boolean.TRUE);
                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
                    applicantService.edit(applicant);
                    SelectionComments selectionComments = new SelectionComments(null, CommentType.APPROVED, reason, StageType.FIELD_VERIFICATION, applicant, (User) session.getAttribute("user"), Calendar.getInstance());
                    this.selectionCommentsService.save(selectionComments);
                }
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("recommendFieldVerification", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }
        }
        if ("reject".equals(action)) {
            String rejectedApplicantList = request.getParameter("rejectedApplicantList");
            System.out.println("in submit fv" + rejectedApplicantList);
            if (!"".equals(rejectedApplicantList)) {
                String reason = request.getParameter("reasonFV");
                String[] applicantArray = rejectedApplicantList.split(",");
                for (int i = 0; i < applicantArray.length; i++) {
                    Applicant applicant = applicantService.getApplicant(new Integer(applicantArray[i]));
                    applicant.setApplicationStatus(ApplicationStatus.RECOMMENDATION_REJECTED);
                    applicant.setRecommendationStatus(Boolean.FALSE);
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
        if ("advance".equals(action)) {
            // String unionId = request.getParameter("unionId");
            String unionId = request.getParameter("unionIdHidden");
            String returnMsg = "";
            if (unionId == null || unionId.equals("0") || unionId.equals("")) {
                returnMsg = "Union is not seleted ";

            } else {
                String bgmeaFactoryId = request.getParameter("selectedBgmea");
                String bkmeaFactoryId = request.getParameter("selectedBkmea");
                System.out.println("bgmea " + bgmeaFactoryId);
                System.out.println("bkmea " + bkmeaFactoryId);
                Map parameter = new HashMap();
                parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
                parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
                parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);
                parameter.put("applicantType", applicantType);

                List<ApplicationStatus> applicationStatusList = new ArrayList<>();
                //0 for pending, 1 for processing
                applicationStatusList.add(ApplicationStatus.RECOMMENDATION_APPROVED);
                applicationStatusList.add(ApplicationStatus.RECOMMENDATION_REJECTED);
                parameter.put("applicationStatusList", applicationStatusList);
                List<Object> resultList = applicantService.getApplicantListBySearchParameter(parameter, 0, 1000000);;
                List<ApplicantView> applicantList = (List<ApplicantView>) resultList.get(0);
                for (ApplicantView applicantView : applicantList) {
                    Applicant applicant = applicantService.getApplicant(applicantView.getId());
                    applicant.setApplicationStatus(ApplicationStatus.VERIFICATION_PENDING);
                    applicant.setModifiedBy((User) session.getAttribute("user"));
                    applicant.setModificationDate(Calendar.getInstance());
                    applicantService.edit(applicant);
                }
            }
            ControllerMessage controllerMessage;
            switch (applicantType) {
                case UNION:
                    controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForUpazilaApproval", LocaleContextHolder.getLocale()));
                    break;
                case MUNICIPAL:
                    controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForUpazilaApproval", LocaleContextHolder.getLocale()));
                    break;
                case CITYCORPORATION:
                    controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForDistrictApproval", LocaleContextHolder.getLocale()));
                    break;
                case BGMEA:
                    controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForBGMEAVerification", LocaleContextHolder.getLocale()));
                    break;
                case BKMEA:
                    controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForBKMEAVerification", LocaleContextHolder.getLocale()));
                    break;
                default:
                    controllerMessage = null;
            }
            if (!returnMsg.equals("")) {
                controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, returnMsg);
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
                    statusList.add(ApplicationStatus.RECOMMENDATION_PENDING);
                } else if (request.getParameter("selectedStatusPrint").equals("1")) {
                    statusList.add(ApplicationStatus.RECOMMENDATION_APPROVED);
                    statusList.add(ApplicationStatus.RECOMMENDATION_REJECTED);
                }
            }

            Map parameter = new HashMap();
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("factoryId", factoryId != null && !"".equals(factoryId) ? Integer.valueOf(factoryId) : null);
            parameter.put("statusList", statusList);
            parameter.put("applicantType", applicantType);
            parameter.put("locale", locale);
            parameter.put("stageType", StageType.FIELD_VERIFICATION);

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
                return "redirect:/recommendation/union";
            case MUNICIPAL:
                return "redirect:/recommendation/municipal";
            case CITYCORPORATION:
                return "redirect:/recommendation/cityCorporation";
            case BGMEA:
                return "redirect:/recommendation/bgmea";
            case BKMEA:
                return "redirect:/recommendation/bkmea";
            default:
                return null;
        }
//        return getFieldVerificationPage(model);
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
                AbstractColumn columnNID = ReportUtility.createColumn("nationalID", String.class, nid, 32, headerStyle, detailTextStyle);
                String fatherName = localizer.getLocalizedText("label.fatherName", locale);
                AbstractColumn columnFatherName = ReportUtility.createColumn("fatherName", String.class, fatherName, 30, headerStyle, detailTextStyle);
                String motherName = localizer.getLocalizedText("label.motherName", locale);
                AbstractColumn columnMotherName = ReportUtility.createColumn("motherName", String.class, motherName, 30, headerStyle, detailTextStyle);
                String spouseName = localizer.getLocalizedText("label.spouseName", locale);
                AbstractColumn columnSpouseName = ReportUtility.createColumn("spouseName", String.class, spouseName, 30, headerStyle, detailTextStyle);
                String address = localizer.getLocalizedText("label.address", locale);
                AbstractColumn columnAddress = ReportUtility.createColumn("address", String.class, address, 30, headerStyle, detailTextStyle);
                String mobileNo = localizer.getLocalizedText("label.mobileNo", locale);
                AbstractColumn columnMobileNo = ReportUtility.createColumn("mobileNo", String.class, mobileNo, 27, headerStyle, detailTextStyle);
                String socioEconomicInfo = localizer.getLocalizedText("label.socioEconomicInfo", locale);
                AbstractColumn columnSocioEconomicInfo = ReportUtility.createColumn("socioEconomicInfo", String.class, socioEconomicInfo, 50, headerStyle, detailTextStyle);
                String recommendation = localizer.getLocalizedText("label.recommendationByIMLMACommittee", locale);
                AbstractColumn columnRecommendation = ReportUtility.createColumn("recommendationStatus", String.class, recommendation, 30, headerStyle, detailTextStyle);
                String remarks = localizer.getLocalizedText("label.remarks", locale);
                AbstractColumn columnRemarks = ReportUtility.createColumn("remarks", String.class, remarks, 30, headerStyle, detailTextStyle);

                drb.addColumn(columnSerialNumber).addColumn(columnBenName).addColumn(columnNID).addColumn(columnFatherName).addColumn(columnMotherName).addColumn(columnSpouseName).addColumn(columnAddress).addColumn(columnMobileNo)
                        .addColumn(columnSocioEconomicInfo).addColumn(columnRecommendation).addColumn(columnRemarks);

                StyleBuilder oddRowBgStyle = new StyleBuilder(true);
                oddRowBgStyle.setBackgroundColor(Color.darkGray);
                drb.setOddRowBackgroundStyle(oddRowBgStyle.build());
                drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            }

            String beneficiaryReport = localizer.getLocalizedText("label.recommendedList", locale);
            drb.setTitle(beneficiaryReport);
            drb.setTitleStyle(titleStyle);

            String subTitle = "";

            if (request.getParameter("selectedUnionIdPrint") != null) {
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
            }
            if (request.getParameter("selectedFactoryIdPrint") != null) {
                String factorySt = localizer.getLocalizedText("label.factory", locale);
                Factory factory = this.factoryService.getFactory(Integer.parseInt(request.getParameter("selectedFactoryIdPrint")));
                subTitle += factorySt + " : " + (locale.toString().equals("bn") == true ? factory.getNameInBangla() : factory.getNameInEnglish());
            }
            if (request.getParameter("selectedStatusPrint") != null) {
                subTitle += "\\n";
                String statusSt = localizer.getLocalizedText("label.status", locale);
                if (request.getParameter("selectedStatusPrint").equals("0")) {
                    subTitle += statusSt + " : " + (locale.toString().equals("bn") == true ? "পেন্ডিং" : "Pending");
                } else {
                    subTitle += statusSt + " : " + (locale.toString().equals("bn") == true ? "কমপ্লিট" : "Complete");
                }
            }
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
            drb.setAllowDetailSplit(false);

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

    @RequestMapping("/checkUnionEligibility/{unionName}")
    @ResponseBody
    public boolean checkUnionEligibility(@PathVariable("unionName") Integer unionName, HttpSession session,
            Model map) throws ExceptionWrapper {
        System.out.println("union id " + unionName);
        boolean isEligible = applicantService.eligibilityToForwardUpazilaStage(unionName, ApplicantType.UNION);
        return isEligible;
    }

    /**
     *
     * @param unionName
     * @param session
     * @param map
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping("/checkMunicipalEligibility/{unionName}")
    @ResponseBody
    public boolean checkMunicipalEligibility(@PathVariable("unionName") Integer unionName, HttpSession session,
            Model map) throws ExceptionWrapper {
        System.out.println("union id " + unionName);
        boolean isEligible = applicantService.eligibilityToForwardUpazilaStage(unionName, ApplicantType.MUNICIPAL);
        return isEligible;
    }

    @RequestMapping("/checkCCEligibility/{unionName}")
    @ResponseBody
    public boolean checkCCEligibility(@PathVariable("unionName") Integer unionName, HttpSession session,
            Model map) throws ExceptionWrapper {
        boolean isEligible = applicantService.eligibilityToForwardUpazilaStage(unionName, ApplicantType.CITYCORPORATION);
        return isEligible;
    }

    @RequestMapping("/checkBGMEAEligibility/{bgmeaFactoryId}")
    @ResponseBody
    public boolean checkBGMEAEligibility(@PathVariable("bgmeaFactoryId") Integer bgmeaFactoryId, HttpSession session,
            Model map) throws ExceptionWrapper {
        boolean isEligible = applicantService.eligibilityToForwardUpazilaStage(bgmeaFactoryId, ApplicantType.BGMEA);
        return isEligible;
    }

    /**
     *
     * @param bkmeaFactoryId
     * @param session
     * @param map
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping("/checkBKMEAEligibility/{bkmeaFactoryId}")
    @ResponseBody
    public boolean checkBKMEAEligibility(@PathVariable("bkmeaFactoryId") Integer bkmeaFactoryId, HttpSession session,
            Model map) throws ExceptionWrapper {
        boolean isEligible = applicantService.eligibilityToForwardUpazilaStage(bkmeaFactoryId, ApplicantType.BKMEA);
        return isEligible;
    }

}
