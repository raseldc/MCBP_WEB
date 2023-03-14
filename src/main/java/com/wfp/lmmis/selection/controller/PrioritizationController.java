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
import com.wfp.lmmis.applicant.forms.SocioEconomicForm;
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantView;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.report.controller.ReportUtility;
import com.wfp.lmmis.report.data.ApplicantReportData;
import com.wfp.lmmis.masterdata.service.BenQuotaService;
import com.wfp.lmmis.masterdata.service.FactoryService;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.utility.ApplicationConstants;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author user
 */
@Controller
public class PrioritizationController {

    @Autowired
    ApplicantService applicantService;

    @Autowired
    private BenQuotaService benQuotaService;

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

    Localizer localizer = Localizer.getBrowserLocalizer();

    //  private final logger logger = //logger.getlogger(PrioritizationController.class);
    @RequestMapping(value = "/prioritization", method = RequestMethod.GET)
    public String getPrioritizationPage(Model model, HttpSession session, HttpServletRequest request) {
        try {
            CommonUtility.mapDivisionName(model);
            CommonUtility.mapFiscalYearName(model);
            model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        } catch (Exception ex) {
            return "notAuthorized";
        }
        return "prioritization";
    }

    @RequestMapping(value = "/prioritization/union", method = RequestMethod.GET)
    public String getUnionPrioritizationPage(Model model, HttpSession session, HttpServletRequest request) {
        try {
            CommonUtility.mapDivisionName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.UNION);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception ex) {
            return "notAuthorized";
        }
        return "prioritizationUnion";
    }

    @RequestMapping(value = "/prioritization/municipal", method = RequestMethod.GET)
    public String getMunicipalPrioritizationPage(Model model, HttpSession session, HttpServletRequest request) {
        try {
            CommonUtility.mapDivisionName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.MUNICIPAL);
            model.addAttribute("type", "municipal");
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception ex) {
            return "notAuthorized";
        }
        return "prioritizationMunicipal";
    }

    @RequestMapping(value = "/prioritization/cityCorporation", method = RequestMethod.GET)
    public String getCityCorporationPrioritizationPage(Model model, HttpSession session, HttpServletRequest request) {
        try {
            CommonUtility.mapDivisionName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.CITYCORPORATION);
            model.addAttribute("type", "cityCorporation");
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception ex) {
            return "notAuthorized";
        }
        return "prioritizationCityCorporation";
    }

    @RequestMapping(value = "/prioritization/bgmea", method = RequestMethod.GET)
    public String getBgmeaPrioritizationPage(Model model, HttpSession session, HttpServletRequest request) {
        try {
            CommonUtility.mapBGMEAFactoryName(model);
            model.addAttribute("type", "bgmea");
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BGMEA);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception ex) {
            return "notAuthorized";
        }
        return "prioritizationBgmea";
    }

    /**
     *
     * @param model
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/prioritization/bkmea", method = RequestMethod.GET)
    public String getBkmeaPrioritizationPage(Model model, HttpSession session, HttpServletRequest request) {
        try {
            CommonUtility.mapBKMEAFactoryName(model);
            model.addAttribute("type", "bkmea");
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BKMEA);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception ex) {
            return "notAuthorized";
        }
        return "prioritizationBkmea";
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/prioritization/list", method = RequestMethod.POST)
    public @ResponseBody
    void prioritizeApplicants(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
        try {
            String divisionId = null, districtId = null, upazilaId = null, unionId = null, bgmeaFactoryId = null, bkmeaFactoryId = null;
            ApplicantType applicantType = null;
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            if (request.getParameter("divisionId") != null) {
                divisionId = request.getParameter("divisionId");
            }
            if (request.getParameter("districtId") != null) {
                districtId = request.getParameter("districtId");
            }
            if (request.getParameter("upazilaId") != null) {
                upazilaId = request.getParameter("upazilaId");
            }
            if (request.getParameter("unionId") != null) {
                unionId = request.getParameter("unionId");
            }
            if (request.getParameter("bgmeaFactoryId") != null) {
                bgmeaFactoryId = request.getParameter("bgmeaFactoryId");
            }
            if (request.getParameter("bkmeaFactoryId") != null) {
                bkmeaFactoryId = request.getParameter("bkmeaFactoryId");
            }
            if (request.getParameter("applicantType") != null) {
                applicantType = (ApplicantType.valueOf(request.getParameter("applicantType")));
            }

            Locale locale = LocaleContextHolder.getLocale();

            Map parameter = new HashMap();
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
            parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);
            parameter.put("applicantType", applicantType);

            List<Object> resultList = applicantService.getPrioritizedApplicantList(parameter, beginIndex, pageSize);
            List<ApplicantView> applicantList = (List<ApplicantView>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);

            for (ApplicantView applicant : applicantList) {
                JSONArray ja = new JSONArray();
                ja.put(applicant.getFullNameInBangla());
                ja.put(applicant.getFullNameInEnglish());
                ja.put("en".equals(locale.getLanguage()) ? applicant.getNid().toString() : CommonUtility.getNumberInBangla(applicant.getNid().toString()));
                String dob = CalendarUtility.getDateString(applicant.getDateOfBirth(), ApplicationConstants.DATE_FORMAT);
                ja.put("en".equals(locale.getLanguage()) ? dob : CommonUtility.getNumberInBangla(dob));
                String mobileNo = applicant.getMobileNo() != null ? applicant.getMobileNo() : "";
                ja.put("en".equals(locale.getLanguage()) ? mobileNo : CommonUtility.getNumberInBangla(mobileNo));
//                ja.put("en".equals(locale.getLanguage()) ? applicant.getApplicationStatus() : applicant.getApplicationStatus().getDisplayNameBn());
                if (applicant.getPresentAddressLine2() != null) {
                    ja.put(applicant.getPresentAddressLine1() + " " + applicant.getPresentAddressLine2());
                } else {
                    ja.put(applicant.getPresentAddressLine1());
                }
                ja.put("en".equals(locale.getLanguage()) ? applicant.getSystemRecommendedStatus() : applicant.getSystemRecommendedStatus().getDisplayNameBn());
                String edit = localizer.getLocalizedText("edit", LocaleContextHolder.getLocale());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/prioritization", method = RequestMethod.POST)
    public Object changePrioritizationStatus(Model model, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response)
            throws ColumnBuilderException, ClassNotFoundException, JRException, IOException {
        String action = request.getParameter("action");
        ApplicantType applicantType = null;
        String unionId = null, factoryId = null;
        Locale locale = LocaleContextHolder.getLocale();

        if ("accept".equals(action)) {
            if (request.getParameter("selectedUnionId") != null) {
                unionId = request.getParameter("selectedUnionId");
            }
            if (request.getParameter("selectedFactoryId") != null) {
                factoryId = request.getParameter("selectedFactoryId");
            }
            if (request.getParameter("selectedApplicantType") != null) {
                applicantType = (ApplicantType.valueOf(request.getParameter("selectedApplicantType")));
            }

            Map parameter = new HashMap();
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("factoryId", factoryId != null && !"".equals(factoryId) ? Integer.valueOf(factoryId) : null);
            parameter.put("applicantType", applicantType);
            try {
                applicantService.sendPrioritizedListForVerification(parameter);
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForFieldVerification", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            } catch (ExceptionWrapper ex) {
                //logger.infoer(ex.getMessage());
            }
        }
//        if ("finalize".equals(action))
//        {
//
//            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("sentForNIDVerification", LocaleContextHolder.getLocale()));
//            redirectAttributes.addFlashAttribute("message", controllerMessage);
//
//        }
        if ("print".equals(action)) {
            try {
                if (request.getParameter("selectedUnionIdPrint") != null) {
                    unionId = request.getParameter("selectedUnionIdPrint");
                }
                if (request.getParameter("selectedFactoryIdPrint") != null) {
                    factoryId = request.getParameter("selectedFactoryIdPrint");
                }
                if (request.getParameter("selectedApplicantTypePrint") != null) {
                    applicantType = (ApplicantType.valueOf(request.getParameter("selectedApplicantTypePrint")));
                }
                Map parameter = new HashMap();
                parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
                parameter.put("factoryId", factoryId != null && !"".equals(factoryId) ? Integer.valueOf(factoryId) : null);
                parameter.put("applicantType", applicantType);
                parameter.put("locale", locale);

                List<ApplicantReportData> applicantReportDataList = this.applicantService.getApplicantReportDataForPrioritizationPrint(parameter);
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
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        switch (applicantType) {
            case UNION:
                return "redirect:/prioritization/union";
            case MUNICIPAL:
                return "redirect:/prioritization/municipal";
            case CITYCORPORATION:
                return "redirect:/prioritization/cityCorporation";
            case BGMEA:
                return "redirect:/prioritization/bgmea";
            case BKMEA:
                return "redirect:/prioritization/bkmea";
            default:
                return null;
        }
    }

    /**
     *
     * @param ds
     * @param request
     * @param emptyData
     * @return
     * @throws ColumnBuilderException
     * @throws JRException
     * @throws ClassNotFoundException
     */
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
                AbstractColumn columnRecommendation = ReportUtility.createColumn("blankRecommendation", String.class, recommendation, 30, headerStyle, detailTextStyle);
                String remarks = localizer.getLocalizedText("label.remarks", locale);
                AbstractColumn columnRemarks = ReportUtility.createColumn("blankRemarks", String.class, remarks, 30, headerStyle, detailTextStyle);

                drb.addColumn(columnSerialNumber).addColumn(columnBenName).addColumn(columnNID)
                        .addColumn(columnFatherName)
                        .addColumn(columnMotherName)
                        .addColumn(columnSpouseName)
                        .addColumn(columnAddress)
                        .addColumn(columnMobileNo)
                        .addColumn(columnSocioEconomicInfo)
                        .addColumn(columnRecommendation)
                        .addColumn(columnRemarks);

                StyleBuilder oddRowBgStyle = new StyleBuilder(true);
                oddRowBgStyle.setBackgroundColor(Color.darkGray);
                drb.setOddRowBackgroundStyle(oddRowBgStyle.build());
                drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            }

            String beneficiaryReport = localizer.getLocalizedText("label.prioritizedList", locale);
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
}
