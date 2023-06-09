/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.controller;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.ImageBanner;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.masterdata.service.DivisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.report.data.ApplicantReportData;
import com.wfp.lmmis.report.data.ApplicantReportDataByLocation;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.DateUtilities;
import com.wfp.lmmis.utility.Localizer;
import java.awt.Color;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author PCUser
 */
@Controller
public class ApplicantReportController {

    //private static final logger logger = //logger.getlogger(ApplicantReportController.class);
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private FiscalYearService fiscalYearService;
    @Autowired
    private DivisionService divisionService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private UpazillaService upazilaService;
    @Autowired
    private UnionService unionService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/applicantReport/union")
    public String getApplicantReportUnion(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        CommonUtility.mapDivisionName(model);
        reportParameterForm.setApplicantType(ApplicantType.UNION);
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "applicantReportUnion";
    }

    @RequestMapping(value = "/applicantReport/municipal")
    public String getApplicantReportMunicipal(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        CommonUtility.mapDivisionName(model);
        reportParameterForm.setApplicantType(ApplicantType.MUNICIPAL);
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "applicantReportMunicipal";
    }

    @RequestMapping(value = "/applicantReport/cityCorporation")
    public String getApplicantReportCityCorporation(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        CommonUtility.mapDivisionName(model);
        reportParameterForm.setApplicantType(ApplicantType.CITYCORPORATION);
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "applicantReportCC";
    }

    @RequestMapping(value = "/applicantReport/bgmea")
    public String getApplicantReportBgmea(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        reportParameterForm.setApplicantType(ApplicantType.BGMEA);
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapBGMEAFactoryName(model);
        model.addAttribute("type", "bgmea");
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "applicantReportBgmeaBkmea";
    }

    @RequestMapping(value = "/applicantReport/bkmea")
    public String getApplicantReportBkmea(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        reportParameterForm.setApplicantType(ApplicantType.BKMEA);
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapBKMEAFactoryName(model);
        model.addAttribute("type", "bkmea");
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "applicantReportBgmeaBkmea";
    }

    @RequestMapping(value = "/applicantReport", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getApplicantReport(ReportParameterForm reportParameterForm, HttpServletResponse response, HttpSession session) throws IOException {
        try {
            Map parameter = new HashMap();
            parameter.put("fiscalYearId", reportParameterForm.getFiscalYear() != null ? reportParameterForm.getFiscalYear().getId() : null);
            parameter.put("divisionId", reportParameterForm.getDivision() != null ? reportParameterForm.getDivision().getId() : null);
            parameter.put("districtId", reportParameterForm.getDistrict() != null ? reportParameterForm.getDistrict().getId() : null);
            parameter.put("upazilaId", reportParameterForm.getUpazila() != null ? reportParameterForm.getUpazila().getId() : null);
            parameter.put("unionId", reportParameterForm.getUnion() != null ? reportParameterForm.getUnion().getId() : null);
            parameter.put("ward", reportParameterForm.getWard() != null ? reportParameterForm.getWard() : null);
            parameter.put("locale", reportParameterForm.getLanguage());
            parameter.put("applicantType", reportParameterForm.getApplicantType());
            parameter.put("bgmeaId", reportParameterForm.getBgmeaFactory() != null ? reportParameterForm.getBgmeaFactory().getId() : null);
            parameter.put("bkmeaId", reportParameterForm.getBkmeaFactory() != null ? reportParameterForm.getBkmeaFactory().getId() : null);
            parameter.put("applicantStatus", reportParameterForm.getApplicantStatus());

            Calendar startDate = null, endDate = null;
            System.out.println("request.getParameter(\"startDate\") = " + reportParameterForm.getStartDate());
            if (reportParameterForm.getStartDate() != null && !"".equals(reportParameterForm.getStartDate())) {
                startDate = DateUtilities.stringToCalendar(reportParameterForm.getStartDate());
                startDate.set(Calendar.HOUR_OF_DAY, 0);
            }
            System.out.println("request.getParameter(\"endDate\") = " + reportParameterForm.getEndDate());
            if (reportParameterForm.getEndDate() != null && !"".equals(reportParameterForm.getEndDate())) {
                endDate = DateUtilities.stringToCalendar(reportParameterForm.getEndDate());
                endDate.set(Calendar.HOUR_OF_DAY, 0);
                endDate.add(Calendar.DATE, 1);
            }
            parameter.put("startDate", startDate);
            parameter.put("endDate", endDate);
            JasperPrint jp = null;
            switch (reportParameterForm.getReportGenerationType()) {
                case "Details": {
                    List<ApplicantReportData> applicantReportDataList = this.applicantService.getApplicantReportData(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(applicantReportDataList);
                    jp = getReport(ds, reportParameterForm, applicantReportDataList.isEmpty());
                    break;
                }
                case "Summary": {
                    List<ApplicantReportDataByLocation> applicantSummaryReportDataList = this.applicantService.getApplicantSummaryReportData(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(applicantSummaryReportDataList);
                    jp = getReport(ds, reportParameterForm, applicantSummaryReportDataList.isEmpty());
                    break;
                }

                case "LMMisExit": {
                    List<ApplicantReportData> beneficiaryReportDataList = this.applicantService.getBeneficiaryReportDataWithLMMISExist(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(beneficiaryReportDataList);

                    jp = getReport(ds, reportParameterForm, beneficiaryReportDataList.isEmpty());
                    break;
                }
                case "Group": {
                    List<ApplicantReportDataByLocation> applicantGroupReportDataList = this.applicantService.getApplicantGroupReportData(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(applicantGroupReportDataList);
                    jp = getReport(ds, reportParameterForm, applicantGroupReportDataList.isEmpty());
                    break;
                }
                default:
                    break;
            }

            ResponseEntity<byte[]> responseEntity = null;
            if (reportParameterForm.getReportExportType().equals("pdf")) {
//            String reportDataAsString = Base64.encodeBase64String(ReportUtility.getPDFContents(jp, response));
//            return reportDataAsString;
                byte[] contents = ReportUtility.getPDFContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
//                String filename = "Applicant_Report.pdf";
//                headers.add("content-disposition", "attachment; filename=" + filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            } else if (reportParameterForm.getReportExportType().equals("excel")) {
//                byte[] contents = ReportUtility.getHTMLContents(jp, response);
//                HttpHeaders headers = new HttpHeaders();
//                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
                byte[] contents = ReportUtility.getExcelContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                String fileName = "Applicant_Report.xls";
                headers.add("Content-Disposition", "inline; filename=" + fileName);
                headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            return responseEntity;

        } catch (Exception e) {
            e.printStackTrace();
            //logger.infoer("Error msg for Applicant Report controller " + e.getMessage());
            return null;
        }
    }

    public JasperPrint getReport(JRDataSource ds, ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, JRException, ClassNotFoundException {
        DynamicReport dynaReport = null;
        switch (reportParameterForm.getReportGenerationType()) {
            case "Details":
                dynaReport = getApplicantDetailDynamicReport(reportParameterForm, emptyData);
                break;
            case "Summary":
                dynaReport = getApplicantSummaryDynamicReport(reportParameterForm, emptyData);
                break;
            case "Group":
                dynaReport = getApplicantGroupDynamicReport(reportParameterForm);
                break;
            case "LMMisExit":
                dynaReport = getBenDetailDynamicReportISLmMisExist(reportParameterForm, emptyData);
                break;
            default:
                break;
        }
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), ds);
        return jp;
    }

    private DynamicReport getApplicantDetailDynamicReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException {
        try {
            DynamicReportBuilder drb = new DynamicReportBuilder();

            Style titleStyle = ReportUtility.createTitleStyle();
            Style subTitleStyle = ReportUtility.createSubTitleStyle();
            Style headerStyle = ReportUtility.createHeaderStyle();
            Style detailTextStyle = ReportUtility.createDetailTextStyle();
            Style detailNumStyle = ReportUtility.createDetailNumberStyle();

            Style groupStyleUnion = ReportUtility.createUnionGroupStyle();
            AbstractColumn columnUnion = null, columnUpazila = null, columnDistrict = null, columnDivision = null;
            Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage()).build();
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
                String mobileNo = localizer.getLocalizedText("label.mobileNo", locale);
                AbstractColumn columnMobileNo = ReportUtility.createColumn("mobileNo", String.class, mobileNo, 30, headerStyle, detailTextStyle);
                String paymentProvder = localizer.getLocalizedText("label.paymentProvider", locale);
                AbstractColumn columnPaymentProvider = ReportUtility.createColumn("paymentProviderName", String.class, paymentProvder, 40, headerStyle, detailTextStyle);
                String accountNo = localizer.getLocalizedText("label.accountNo", locale);
                AbstractColumn columnAccountNo = ReportUtility.createColumn("accountNo", String.class, accountNo, 30, headerStyle, detailTextStyle);
                String branch = localizer.getLocalizedText("label.branch", locale);
                AbstractColumn columnBranch = ReportUtility.createColumn("branchName", String.class, branch, 30, headerStyle, detailTextStyle);
                String address = localizer.getLocalizedText("label.address", locale);
                AbstractColumn columnAddress = ReportUtility.createColumn("address", String.class, address, 30, headerStyle, detailTextStyle);
                String enrollment_month = localizer.getLocalizedText("enrollment_month", locale);
                AbstractColumn columnMonth = ReportUtility.createColumn("month", String.class, enrollment_month, 30, headerStyle, detailTextStyle);

                drb.addColumn(columnSerialNumber).addColumn(columnBenName).addColumn(columnNID).addColumn(columnFatherName).addColumn(columnMotherName)
                        .addColumn(columnSpouseName).addColumn(columnMobileNo)
                        .addColumn(columnAddress).addColumn(columnPaymentProvider).addColumn(columnBranch).addColumn(columnAccountNo).addColumn(columnMonth);
                if (reportParameterForm.getUnion() == null || reportParameterForm.getUnion().getId() == null) {
                    String union = localizer.getLocalizedText("label.union", locale);
                    columnUnion = ReportUtility.createColumn("union", String.class, union, 30, headerStyle, detailTextStyle);
                    drb.addColumn(columnUnion);
                }
                if (reportParameterForm.getUnion() == null || reportParameterForm.getUnion().getId() == null) {
                    GroupBuilder gb1 = new GroupBuilder();
                    columnUnion.setStyle(groupStyleUnion);
                    DJGroup dJGroup = gb1.setCriteriaColumn((PropertyColumn) columnUnion)
                            .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                            .build();
                    drb.addGroup(dJGroup);
                }
            }
            StyleBuilder oddRowBgStyle = new StyleBuilder(true);
            oddRowBgStyle.setBackgroundColor(Color.darkGray);
            drb.setOddRowBackgroundStyle(oddRowBgStyle.build());

            if (reportParameterForm.getReportOrientation().equals("Portrait")) {
                drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
            } else {
                drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            }
            if (locale.toString().equals("bn")) {
                drb.addFirstPageImageBanner("image/banner_bn3.png", 395, 55, ImageBanner.ALIGN_CENTER);
            } else {
                drb.addFirstPageImageBanner("image/banner_en3.png", 406, 55, ImageBanner.ALIGN_CENTER);
            }
            String beneficiaryReport = localizer.getLocalizedText("label.applicantReport", locale);
            drb.setTitle(beneficiaryReport);
            drb.setTitleStyle(titleStyle);

            String subTitle = "";
            if (reportParameterForm.getFiscalYear() != null) {
                String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
                FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
                subTitle += fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish()) + " ";
            }
            if (reportParameterForm.getStartDate() != null) {
                subTitle += locale.toString().equals("bn") == true ? CommonUtility.getNumberInBangla("(" + reportParameterForm.getStartDate().replace("-", "/")) : "(" + reportParameterForm.getStartDate().replace("-", "/");
            }
            if (reportParameterForm.getEndDate() != null) {
                subTitle += "-" + (locale.toString().equals("bn") == true ? CommonUtility.getNumberInBangla(reportParameterForm.getEndDate().replace("-", "/")) : "(" + reportParameterForm.getEndDate().replace("-", "/")) + ")";
            }
            if (reportParameterForm.getDivision() != null && reportParameterForm.getDivision().getId() != null) {
                String divisionSt = localizer.getLocalizedText("label.division", locale);
                Division division = this.divisionService.getDivision(reportParameterForm.getDivision().getId());
                subTitle += "\\n" + divisionSt + " : " + (locale.toString().equals("bn") == true ? division.getNameInBangla() : division.getNameInEnglish());
            }
            if (reportParameterForm.getDistrict() != null && reportParameterForm.getDistrict().getId() != null) {
                String districtSt = localizer.getLocalizedText("label.district", locale);
                District district = this.districtService.getDistrict(reportParameterForm.getDistrict().getId());
                subTitle += "\\t\\t" + districtSt + " : " + (locale.toString().equals("bn") == true ? district.getNameInBangla() : district.getNameInEnglish());
            }
            if (reportParameterForm.getUpazila() != null && reportParameterForm.getUpazila().getId() != null) {
                String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
                Upazilla upazila = this.upazilaService.getUpazilla(reportParameterForm.getUpazila().getId());
                subTitle += "\\t\\t" + upazilaSt + " : " + (locale.toString().equals("bn") == true ? upazila.getNameInBangla() : upazila.getNameInEnglish());
            }
            if (reportParameterForm.getUnion() != null && reportParameterForm.getUnion().getId() != null) {
                String unionSt = reportParameterForm.getApplicantType() != ApplicantType.UNION ? localizer.getLocalizedText("label.municipalOrCityCorporation", locale) : localizer.getLocalizedText("label.union", locale);
                Union union = this.unionService.getUnion(reportParameterForm.getUnion().getId());
                subTitle += "\\t\\t" + unionSt + " : " + (locale.toString().equals("bn") == true ? union.getNameInBangla() : union.getNameInEnglish());
            }
            if (reportParameterForm.getWard() != null) {
                String wardST = localizer.getLocalizedText("label.ward", locale);
                int ward = reportParameterForm.getWard();
                subTitle += "\\t\\t " + wardST + " : " + (locale.toString().equals("bn") == true ? CommonUtility.getNumberInBangla(ward + "") + "" : ward + "");
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
            if (reportParameterForm.getReportExportType().equals("excel")) {
                drb.setIgnorePagination(true); // will generate in single sheet
            } else {
                drb.setIgnorePagination(false);
            }

            String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
            drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle(), true, true);
            return drb.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private DynamicReport getApplicantSummaryDynamicReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException {
        try {
            DynamicReportBuilder drb = new DynamicReportBuilder();
            Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage()).build();
            Style titleStyle = ReportUtility.createTitleStyle();
            Style subTitleStyle = ReportUtility.createSubTitleStyle();
            Style headerStyle = ReportUtility.createHeaderStyle();
            Style detailTextStyle = ReportUtility.createDetailTextStyle();
            Style detailNumStyle = ReportUtility.createDetailNumberStyle();
            AbstractColumn columnApplicantTotal = null;
            String factorySt = localizer.getLocalizedText("label.factory", locale);
            String totalSt = localizer.getLocalizedText("label.total", locale);
            if (!emptyData) {
                AbstractColumn columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);
                drb.addColumn(columnSerialNumber);
                if (reportParameterForm.getApplicantType() == ApplicantType.UNION || reportParameterForm.getApplicantType() == ApplicantType.MUNICIPAL || reportParameterForm.getApplicantType() == ApplicantType.CITYCORPORATION) {
                    if (reportParameterForm.getDivision().getId() == null) {
                        String divisionSt = localizer.getLocalizedText("label.division", locale);
                        AbstractColumn columnDivision = ReportUtility.createColumn("division", String.class, divisionSt, 30, headerStyle, detailTextStyle);
                        drb.addColumn(columnDivision);
                    } else {
                        if (reportParameterForm.getDistrict().getId() == null) {
                            String districtSt = localizer.getLocalizedText("label.district", locale);
                            AbstractColumn columnDistrict = ReportUtility.createColumn("district", String.class, districtSt, 30, headerStyle, detailTextStyle);
                            drb.addColumn(columnDistrict);
                        } else {
                            if (reportParameterForm.getUpazila().getId() == null) {
                                String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
                                AbstractColumn columnUpazila = ReportUtility.createColumn("upazila", String.class, upazilaSt, 30, headerStyle, detailTextStyle);
                                drb.addColumn(columnUpazila);
                            } else {
                                if (reportParameterForm.getUnion() == null || reportParameterForm.getUnion().getId() == null) {
                                    String columnName = reportParameterForm.getApplicantType() == ApplicantType.UNION ? localizer.getLocalizedText("label.union", locale) : localizer.getLocalizedText("label.municipal", locale);
                                    AbstractColumn columnUnion = ReportUtility.createColumn("union", String.class, columnName, 30, headerStyle, detailTextStyle);
                                    drb.addColumn(columnUnion);
                                }
                            }
                        }
                    }
                } else {
                    AbstractColumn columnFactory = ReportUtility.createColumn("factory", String.class, factorySt, 30, headerStyle, detailTextStyle);
                    drb.addColumn(columnFactory);
                }
                String totalApplicantSt = localizer.getLocalizedText("label.totalApplicant", locale);
                columnApplicantTotal = ReportUtility.createColumn("applicantTotal", String.class, totalApplicantSt, 30, headerStyle, detailNumStyle);
                drb.addColumn(columnApplicantTotal);
            }

            StyleBuilder oddRowBgStyle = new StyleBuilder(true);
            oddRowBgStyle.setBackgroundColor(Color.darkGray);
            drb.setOddRowBackgroundStyle(oddRowBgStyle.build());

            if (reportParameterForm.getReportOrientation().equals("Portrait")) {
                drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
            } else {
                drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            }
            String applicantReport = localizer.getLocalizedText("label.applicantReport", locale);
            drb.setTitle(applicantReport);
            drb.setTitleStyle(titleStyle);

            String subTitle = "";

            if (reportParameterForm.getFiscalYear().getId() != null) {
                String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
                FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
                subTitle += fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish()) + " ";
            }
            if (reportParameterForm.getStartDate() != null) {
                subTitle += locale.toString().equals("bn") == true ? CommonUtility.getNumberInBangla("(" + reportParameterForm.getStartDate().replace("-", "/")) : "(" + reportParameterForm.getStartDate().replace("-", "/");
            }
            if (reportParameterForm.getEndDate() != null) {
                subTitle += "-" + (locale.toString().equals("bn") == true ? CommonUtility.getNumberInBangla(reportParameterForm.getEndDate().replace("-", "/")) : "(" + reportParameterForm.getEndDate().replace("-", "/")) + ")";
            }
            if (reportParameterForm.getDivision() != null && reportParameterForm.getDivision().getId() != null) {
                String divisionSt = localizer.getLocalizedText("label.division", locale);
                Division division = this.divisionService.getDivision(reportParameterForm.getDivision().getId());
                subTitle += "\\n" + divisionSt + " : " + (locale.toString().equals("bn") == true ? division.getNameInBangla() : division.getNameInEnglish());
            }
            if (reportParameterForm.getDistrict() != null && reportParameterForm.getDistrict().getId() != null) {
                String districtSt = localizer.getLocalizedText("label.district", locale);
                District district = this.districtService.getDistrict(reportParameterForm.getDistrict().getId());
                subTitle += "\\t\\t" + districtSt + " : " + (locale.toString().equals("bn") == true ? district.getNameInBangla() : district.getNameInEnglish());
            }
            if (reportParameterForm.getUpazila() != null && reportParameterForm.getUpazila().getId() != null) {
                String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
                Upazilla upazila = this.upazilaService.getUpazilla(reportParameterForm.getUpazila().getId());
                subTitle += "\\t\\t" + upazilaSt + " : " + (locale.toString().equals("bn") == true ? upazila.getNameInBangla() : upazila.getNameInEnglish());
            }
            if (reportParameterForm.getUnion() != null && reportParameterForm.getUnion().getId() != null) {
                String unionSt = localizer.getLocalizedText("label.union", locale);
                Union union = this.unionService.getUnion(reportParameterForm.getUnion().getId());
                subTitle += "\\t\\t" + unionSt + " : " + (locale.toString().equals("bn") == true ? union.getNameInBangla() : union.getNameInEnglish());
            }
            subTitle += "\\n";
            drb.setSubtitle(subTitle);
            drb.setSubtitleStyle(subTitleStyle);
            drb.setUseFullPageWidth(true);
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
            if (!emptyData) {
                drb.addField("grandTotal", String.class.getName());
                drb.addGlobalFooterVariable(columnApplicantTotal, new CustomExpression() {

                    @Override
                    public Object evaluate(Map fields, Map variables, Map parameters) {
                        String a = (String) fields.get("grandTotal");
                        return a;
                    }

                    @Override
                    public String getClassName() {
                        return String.class.getName();
                    }
                });
                drb.setGrandTotalLegend(totalSt);
                drb.setGrandTotalLegendStyle(ReportUtility.createGrandTotalLegendStyle());
            }
            String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
            drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle(), true, true);
            return drb.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private DynamicReport getApplicantGroupDynamicReport(ReportParameterForm reportParameterForm) throws ColumnBuilderException, ClassNotFoundException {
        try {
            Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage()).build();
            Style titleStyle = ReportUtility.createTitleStyle();
            Style subTitleStyle = ReportUtility.createSubTitleStyle();
            Style headerStyle = ReportUtility.createHeaderStyle();
            Style detailTextStyle = ReportUtility.createDetailTextStyle();
            Style detailNumStyle = ReportUtility.createDetailNumberStyle();

            Style groupTitleStyle = new Style();
            groupTitleStyle.setFont(Font.ARIAL_MEDIUM_BOLD);

            Style col2Style = new Style();
            col2Style.setFont(Font.ARIAL_MEDIUM_BOLD);
            col2Style.setBorderBottom(Border.THIN());
            col2Style.setVerticalAlign(VerticalAlign.TOP);

            Style titleStyle1 = new Style();
            titleStyle1.setFont(new Font(12, Font._FONT_VERDANA, true));
            titleStyle1.setTextColor(Color.black);

            Style importeStyle = new Style();
            importeStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
            Style oddRowStyle = new Style();
            oddRowStyle.setBorder(Border.NO_BORDER());
            oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
            oddRowStyle.setTransparency(Transparency.OPAQUE);

            DynamicReportBuilder drb = new DynamicReportBuilder();

            AbstractColumn columnDivision = ReportUtility.createColumn("division", String.class, "Division :", 50, groupTitleStyle, titleStyle1);
//            AbstractColumn columnDistrict = ReportUtility.createColumn("district", String.class, "District :", 50, col2Style, col2Style);
            AbstractColumn columnDistrict = ReportUtility.createColumn("district", String.class, "District", 50, headerStyle, detailTextStyle);
            AbstractColumn columnUpazila = ReportUtility.createColumn("upazila", String.class, "Upazila", 30, headerStyle, detailTextStyle);
            AbstractColumn columnUnionTotal = ReportUtility.createColumn("unionTotal", Long.class, "Total Union", 30, headerStyle, detailNumStyle);
            AbstractColumn columnBenTotal = ReportUtility.createColumn("applicantTotal", Long.class, "Total Applicant", 30, headerStyle, detailNumStyle);
            drb.addColumn(columnDivision).addColumn(columnDistrict).addColumn(columnUpazila).addColumn(columnUnionTotal).addColumn(columnBenTotal);

            Style g1VariablesStyle = new Style();
            g1VariablesStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
            g1VariablesStyle.setBorder(Border.THIN());
            g1VariablesStyle.setBorderColor(Color.BLACK);
            g1VariablesStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
            g1VariablesStyle.setVerticalAlign(VerticalAlign.MIDDLE);
            g1VariablesStyle.setTextColor(new Color(50, 50, 150));
            g1VariablesStyle.setBackgroundColor(Color.LIGHT_GRAY);
            g1VariablesStyle.setTransparency(Transparency.OPAQUE);
            g1VariablesStyle.setPaddingRight(5);

            Style g2VariablesStyle = new Style();
            g2VariablesStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
            g2VariablesStyle.setBorder(Border.THIN());
            g2VariablesStyle.setBorderColor(Color.BLACK);
            g2VariablesStyle.setTextColor(new Color(150, 150, 150));
            g2VariablesStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
            g2VariablesStyle.setVerticalAlign(VerticalAlign.MIDDLE);
            g2VariablesStyle.setBackgroundColor(new Color(236, 240, 245));
            g2VariablesStyle.setTransparency(Transparency.OPAQUE);
            g2VariablesStyle.setPaddingRight(5);

            GroupBuilder gb1 = new GroupBuilder();

            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columnDivision)
                    .addFooterVariable(columnDistrict, DJCalculation.COUNT, g1VariablesStyle)
                    .addFooterVariable(columnUpazila, DJCalculation.COUNT, g1VariablesStyle)
                    .addFooterVariable(columnUnionTotal, DJCalculation.SUM, g1VariablesStyle)
                    .addFooterVariable(columnBenTotal, DJCalculation.SUM, g1VariablesStyle)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columnDistrict)
                    .addFooterVariable(columnUpazila, DJCalculation.COUNT, g2VariablesStyle)
                    .addFooterVariable(columnUnionTotal, DJCalculation.SUM, g2VariablesStyle)
                    .addFooterVariable(columnBenTotal, DJCalculation.SUM, g2VariablesStyle)
                    .setGroupLayout(GroupLayout.DEFAULT)
                    .build();

            drb.addGroup(g1); // add group g1
            drb.addGroup(g2); // add group g2

            StyleBuilder oddRowBgStyle = new StyleBuilder(true);
            oddRowBgStyle.setBackgroundColor(Color.darkGray);
            drb.setOddRowBackgroundStyle(oddRowBgStyle.build());

            if (reportParameterForm.getReportOrientation().equals("Portrait")) {
                drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
            } else {
                drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            }

            drb.setTitle("Applicant Group Report");
            drb.setTitleStyle(titleStyle);

            String subTitle = "";
            if (reportParameterForm.getScheme().getId() != null) {
                Scheme scheme = this.schemeService.getScheme(reportParameterForm.getScheme().getId());
                subTitle += "Scheme : " + scheme.getNameInEnglish();
            }
            if (reportParameterForm.getFiscalYear().getId() != null) {
                FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
                subTitle += "\\tFiscal year : " + fiscalYear.getNameInEnglish();
            }
            drb.setSubtitle(subTitle);
            drb.setSubtitleStyle(subTitleStyle);
            drb.setUseFullPageWidth(true);
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
            if (reportParameterForm.getReportExportType().equals("excel")) {
                drb.setIgnorePagination(true); // will generate in single sheet
            } else {
                drb.setIgnorePagination(false);
            }
            drb.setPrintColumnNames(false);
            drb.setPrintBackgroundOnOddRows(false);
            drb.setOddRowBackgroundStyle(oddRowStyle);
            drb.setWhenNoData("No Data Found for this report", ReportUtility.noDataStyle());
            return drb.build();
        } catch (ColumnBuilderException e) {
            //logger.infoer(e.getMessage());
            return null;
        }
    }

    private DynamicReport getBenDetailDynamicReportISLmMisExist(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException {

        DynamicReportBuilder drb = new DynamicReportBuilder();

        Style subTitleStyle = ReportUtility.createSubTitleStyle();
        Style headerStyle = ReportUtility.createHeaderStyle();
        Style detailTextStyle = ReportUtility.createDetailTextStyle();
        Style detailNumStyle = ReportUtility.createDetailNumberStyle();
        Style groupStyleUnion = ReportUtility.createUnionGroupStyle();

        Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage()).build();
        AbstractColumn columnUnion = null, columnUpazila = null, columnDistrict = null, columnDivision = null, columnSerialNumber = null, columnBenNameBn = null, columnBenNameEn = null;
        if (!emptyData) {
            columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);
            String nameBn = localizer.getLocalizedText("label.name", locale);
            columnBenNameBn = ReportUtility.createColumn("benNameBn", String.class, nameBn, 35, headerStyle, detailTextStyle);
            String nameEn = localizer.getLocalizedText("label.nameEn", locale);
            columnBenNameEn = ReportUtility.createColumn("benNameEn", String.class, nameEn, 35, headerStyle, detailTextStyle);
            String nid = localizer.getLocalizedText("label.nid", locale);
            AbstractColumn columnNID = ReportUtility.createColumn("nationalID", String.class, nid, 42, headerStyle, detailTextStyle);
//            String fatherName = localizer.getLocalizedText("label.fatherName", locale);
//            AbstractColumn columnFatherName = ReportUtility.createColumn("fatherName", String.class, fatherName, 30, headerStyle, detailTextStyle);
//            String motherName = localizer.getLocalizedText("label.motherName", locale);
//            AbstractColumn columnMotherName = ReportUtility.createColumn("motherName", String.class, motherName, 30, headerStyle, detailTextStyle);
            String spouseName = localizer.getLocalizedText("label.spouseName", locale);
            AbstractColumn columnSpouseName = ReportUtility.createColumn("spouseName", String.class, spouseName, 30, headerStyle, detailTextStyle);
            String mobileNo = localizer.getLocalizedText("label.mobileNo", locale);
            AbstractColumn columnMobileNo = ReportUtility.createColumn("mobileNo", String.class, mobileNo, 30, headerStyle, detailTextStyle);
//            String paymentProvder = localizer.getLocalizedText("label.paymentProvider", locale);
//            AbstractColumn columnPaymentProvider = ReportUtility.createColumn("paymentProviderName", String.class, paymentProvder, 40, headerStyle, detailTextStyle);
//            String accountNo = localizer.getLocalizedText("label.accountNo", locale);
//            AbstractColumn columnAccountNo = ReportUtility.createColumn("accountNo", String.class, accountNo, 30, headerStyle, detailTextStyle);
//            String branch = localizer.getLocalizedText("label.branch", locale);
//            AbstractColumn columnBranch = ReportUtility.createColumn("branchName", String.class, branch, 30, headerStyle, detailTextStyle);
            String wardNo = localizer.getLocalizedText("label.ward", locale);
            AbstractColumn columnwardNo = ReportUtility.createColumn("wardNo", String.class, wardNo, 30, headerStyle, detailTextStyle);
//            String village = localizer.getLocalizedText("label.village", locale);
//            AbstractColumn columnvillage = ReportUtility.createColumn("village", String.class, village, 30, headerStyle, detailTextStyle);

            String conceptionTerm = localizer.getLocalizedText("label.ConceptionTerm", locale);
            AbstractColumn columnConceptionTerm = ReportUtility.createColumn("conceptionTerm", String.class, conceptionTerm, 30, headerStyle, detailTextStyle);

            String lmMisStatus = localizer.getLocalizedText("label.LMMIS", locale);
            AbstractColumn columnlmMisStatus = ReportUtility.createColumn("lmMisStatus", String.class, lmMisStatus, 30, headerStyle, detailTextStyle);

            String oldGeo = localizer.getLocalizedText("label.oldGeo", locale);
            AbstractColumn columnOldGeo = ReportUtility.createColumn("oldGeo", String.class, oldGeo, 30, headerStyle, detailTextStyle);

            String oldFiscalYearName = localizer.getLocalizedText("label.oldFiscalYearName", locale);
            AbstractColumn columnOldFiscalYearName = ReportUtility.createColumn("oldFiscalYearName", String.class, oldFiscalYearName, 30, headerStyle, detailTextStyle);

            String oldConceptionDuration = localizer.getLocalizedText("label.oldConceptionDuration", locale);
            AbstractColumn columnOldConceptionDuration = ReportUtility.createColumn("oldConceptionDuration", String.class, oldConceptionDuration, 30, headerStyle, detailTextStyle);

            String oldConceptionTerm = localizer.getLocalizedText("label.oldConceptionTerm", locale);
            AbstractColumn columnOldConceptionTerm = ReportUtility.createColumn("oldConceptionTerm", String.class, oldConceptionTerm, 30, headerStyle, detailTextStyle);

            drb.addColumn(columnSerialNumber).addColumn(columnBenNameBn).
                    addColumn(columnBenNameEn).addColumn(columnNID).
                    //                    addColumn(columnFatherName).
                    //                    addColumn(columnMotherName).
                    addColumn(columnSpouseName).
                    addColumn(columnwardNo).
                    //                    addColumn(columnvillage).
                    addColumn(columnMobileNo).
                    addColumn(columnConceptionTerm).
                    addColumn(columnOldConceptionTerm).
                    addColumn(columnOldGeo).
                    addColumn(columnOldFiscalYearName).addColumn(columnlmMisStatus);

            if (reportParameterForm.getUnion() == null || reportParameterForm.getUnion().getId() == null) {
                String union = localizer.getLocalizedText("label.union", locale);
                columnUnion = ReportUtility.createColumn("union", String.class, union, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnUnion);
            }
            if (reportParameterForm.getBeneficiaryStatus() == null) {
                String status = localizer.getLocalizedText("label.beneficiaryStatus", locale);
                AbstractColumn columnStatus = ReportUtility.createColumn("status", String.class, status, 15, headerStyle, detailTextStyle);
                drb.addColumn(columnStatus);
            }
            if (reportParameterForm.getUnion() == null || reportParameterForm.getUnion().getId() == null) {

                GroupBuilder gb = new GroupBuilder();
                columnUnion.setStyle(groupStyleUnion);
                DJGroup dJGroup = gb.setCriteriaColumn((PropertyColumn) columnUnion)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                        .build();
                drb.addGroup(dJGroup);
            }
        }
        StyleBuilder oddRowBgStyle = new StyleBuilder(true);
        oddRowBgStyle.setBackgroundColor(Color.darkGray);
        drb.setOddRowBackgroundStyle(oddRowBgStyle.build());

        if (reportParameterForm.getReportOrientation().equals("Portrait")) {
            drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
        } else {
            drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        }
//        String beneficiaryReport = localizer.getLocalizedText("label.beneficiaryReportDetail", locale);
//        drb.setTitle(beneficiaryReport);
        drb.setTitle("");
//        drb.setTitleStyle(titleStyle);

        String subTitle = "";

        if (reportParameterForm.getFiscalYear().getId() != null) {
            String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
            subTitle += fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish()) + " ";
        }
        if (reportParameterForm.getStartDate() != null) {
            subTitle += locale.toString().equals("bn") == true ? CommonUtility.getNumberInBangla("(" + reportParameterForm.getStartDate().replace("-", "/")) : "(" + reportParameterForm.getStartDate().replace("-", "/");
        }
        if (reportParameterForm.getEndDate() != null) {
            subTitle += "-" + (locale.toString().equals("bn") == true ? CommonUtility.getNumberInBangla(reportParameterForm.getEndDate().replace("-", "/")) : "(" + reportParameterForm.getEndDate().replace("-", "/")) + ")";
        }
        if (reportParameterForm.getBeneficiaryStatus() != null) {
            String status = localizer.getLocalizedText("label.beneficiaryStatus", locale);
            if (reportParameterForm.getFiscalYear().getId() != null) {
                subTitle += "\\t\\t";
            }
            subTitle += status + " : " + (locale.toString().equals("bn") == true ? reportParameterForm.getBeneficiaryStatus().getDisplayNameBn() : reportParameterForm.getBeneficiaryStatus().getDisplayName());
        }
        if (reportParameterForm.getDivision() != null && reportParameterForm.getDivision().getId() != null) {
            String divisionSt = localizer.getLocalizedText("label.division", locale);
            Division division = this.divisionService.getDivision(reportParameterForm.getDivision().getId());
            subTitle += "\\n" + divisionSt + " : " + (locale.toString().equals("bn") == true ? division.getNameInBangla() : division.getNameInEnglish());
        }
        if (reportParameterForm.getDistrict() != null && reportParameterForm.getDistrict().getId() != null) {
            String districtSt = localizer.getLocalizedText("label.district", locale);
            District district = this.districtService.getDistrict(reportParameterForm.getDistrict().getId());
            subTitle += "\\t\\t" + districtSt + " : " + (locale.toString().equals("bn") == true ? district.getNameInBangla() : district.getNameInEnglish());
        }
        if (reportParameterForm.getUpazila() != null && reportParameterForm.getUpazila().getId() != null) {
            String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
            Upazilla upazila = this.upazilaService.getUpazilla(reportParameterForm.getUpazila().getId());
            subTitle += "\\t\\t" + upazilaSt + " : " + (locale.toString().equals("bn") == true ? upazila.getNameInBangla() : upazila.getNameInEnglish());
        }
        if (reportParameterForm.getUnion() != null && reportParameterForm.getUnion().getId() != null) {
            String unionSt = null;
            if (reportParameterForm.getApplicantType() == ApplicantType.UNION) {
                unionSt = localizer.getLocalizedText("label.union", locale);
            } else if (reportParameterForm.getApplicantType() == ApplicantType.MUNICIPAL) {
                unionSt = localizer.getLocalizedText("label.municipal", locale);
            } else if (reportParameterForm.getApplicantType() == ApplicantType.CITYCORPORATION) {
                unionSt = localizer.getLocalizedText("label.cityCorporation", locale);
            }
            Union union = this.unionService.getUnion(reportParameterForm.getUnion().getId());
            subTitle += "\\t\\t" + unionSt + " : " + (locale.toString().equals("bn") == true ? union.getNameInBangla() : union.getNameInEnglish());
        }

        subTitle += "\\n";
        drb.setSubtitle(subTitle);
        drb.setSubtitleStyle(subTitleStyle);
        drb.setLeftMargin(30);
        drb.setRightMargin(30);
        drb.setTopMargin(20);
        drb.setBottomMargin(20);
        if (locale.toString().equals("bn")) {
            drb.addImageBanner("image/banner_LmMisExit.png", 406, 55, ImageBanner.ALIGN_CENTER);
        } else {
            drb.addFirstPageImageBanner("image/banner_LmMisExit.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }
        if (reportParameterForm.getReportExportType().equals("excel")) {
            drb.setIgnorePagination(true); // will generate in single sheet
        } else {
            drb.setIgnorePagination(false);
        }
        drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
        drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
        drb.setUseFullPageWidth(true);
        drb.setAllowDetailSplit(false);

        String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
        drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle(), true, true);

        return drb.build();
    }

}
