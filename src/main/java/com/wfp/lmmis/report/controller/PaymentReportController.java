/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.controller;

import com.wfp.lmmis.report.data.PaymentReportData;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.ImageBanner;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
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
import com.wfp.lmmis.enums.PayrollListType;
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.service.DivisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.model.PaymentCycle;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.payroll.service.PaymentCycleService;
import com.wfp.lmmis.payroll.service.PaymentService;
import com.wfp.lmmis.types.PaymentStatus;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.Localizer;
import java.awt.Color;
import java.util.Arrays;
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
 * @author Sarwar
 */
@Controller
public class PaymentReportController {

    //private static final logger logger = //logger.getlogger(PaymentReportController.class);

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private FiscalYearService fiscalYearService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private PaymentCycleService paymentCycleService;
    @Autowired
    private DivisionService divisionService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private UpazillaService upazilaService;
    @Autowired
    private UnionService unionService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/paymentReport/union")
    public String getBeneficiaryReportUnion(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        CommonUtility.mapDivisionName(model);
        reportParameterForm.setPayrollListType(PayrollListType.UNION);
        CommonUtility.mapAllFiscalYearName(model);
        List<PaymentStatus> paymentStatusList = Arrays.asList(PaymentStatus.values());
        model.addAttribute("paymentStatusList", PaymentStatus.getSelectedList());
        model.addAttribute("dateFormat", "yy-mm-dd");
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "paymentReportUnion";
    }

    @RequestMapping(value = "/paymentReport/municipal")
    public String getBeneficiaryReportMunicipal(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        CommonUtility.mapDivisionName(model);
        reportParameterForm.setPayrollListType(PayrollListType.MUNICIPAL);
        CommonUtility.mapAllFiscalYearName(model);
        List<PaymentStatus> paymentStatusList = Arrays.asList(PaymentStatus.values());
        model.addAttribute("paymentStatusList", PaymentStatus.getSelectedList());
        model.addAttribute("dateFormat", "yy-mm-dd");
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "paymentReportMunicipal";
    }

    @RequestMapping(value = "/paymentReport/cc")
    public String getBeneficiaryReportCC(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        CommonUtility.mapDivisionName(model);
        reportParameterForm.setPayrollListType(PayrollListType.DISTRICT);
        CommonUtility.mapAllFiscalYearName(model);
        List<PaymentStatus> paymentStatusList = Arrays.asList(PaymentStatus.values());
        model.addAttribute("paymentStatusList", PaymentStatus.getSelectedList());
        model.addAttribute("dateFormat", "yy-mm-dd");
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "paymentReportCC";
    }

    @RequestMapping(value = "/paymentReport/bgmea")
    public String getBeneficiaryReportBgmea(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        CommonUtility.mapDivisionName(model);
        reportParameterForm.setPayrollListType(PayrollListType.BGMEA);
        CommonUtility.mapAllFiscalYearName(model);
        List<PaymentStatus> paymentStatusList = Arrays.asList(PaymentStatus.values());
        model.addAttribute("paymentStatusList", PaymentStatus.getSelectedList());
        CommonUtility.mapBGMEAFactoryName(model);
        model.addAttribute("dateFormat", "yy-mm-dd");
        model.addAttribute("type", "bgmea");
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "paymentReportBgmeaBkmea";
    }

    @RequestMapping(value = "/paymentReport/bkmea")
    public String getBeneficiaryReportBkmea(Model model, HttpServletRequest request) {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        CommonUtility.mapDivisionName(model);
        reportParameterForm.setPayrollListType(PayrollListType.BKMEA);
        CommonUtility.mapAllFiscalYearName(model);
        List<PaymentStatus> paymentStatusList = Arrays.asList(PaymentStatus.values());
        model.addAttribute("paymentStatusList", PaymentStatus.getSelectedList());
        CommonUtility.mapBKMEAFactoryName(model);
        model.addAttribute("dateFormat", "yy-mm-dd");
        model.addAttribute("type", "bkmea");
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "paymentReportBgmeaBkmea";
    }

    @RequestMapping(value = "/paymentReport", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getPaymentReport(ReportParameterForm reportParameterForm, HttpServletResponse response,
            HttpServletRequest request, HttpSession session) throws IOException {
        try {
            System.out.println("session = " + session);
            Map parameter = new HashMap();
            parameter.put("fiscalYearId", reportParameterForm.getFiscalYear().getId());
            parameter.put("paymentCycleId", reportParameterForm.getPaymentCycle().getId());
            parameter.put("divisionId", reportParameterForm.getDivision() != null ? reportParameterForm.getDivision().getId() : null);
            parameter.put("districtId", reportParameterForm.getDistrict() != null ? reportParameterForm.getDistrict().getId() : null);
            parameter.put("upazilaId", reportParameterForm.getUpazila() != null ? reportParameterForm.getUpazila().getId() : null);
            parameter.put("bgmeaId", reportParameterForm.getBgmeaFactory() != null ? reportParameterForm.getBgmeaFactory().getId() : null);
            parameter.put("bkmeaId", reportParameterForm.getBkmeaFactory() != null ? reportParameterForm.getBkmeaFactory().getId() : null);
            parameter.put("paymentStatus", reportParameterForm.getPaymentStatus());
            parameter.put("orderBy", request.getParameter("orderBy"));
            String payrollListTypeSt = request.getParameter("payrollListType");
            PayrollListType payrollListType = (payrollListTypeSt != null && payrollListTypeSt != "") ? PayrollListType.valueOf(payrollListTypeSt) : null;
            parameter.put("payrollListType", payrollListType);
            if (reportParameterForm.getUnion() != null && reportParameterForm.getUnion() != null) {
                parameter.put("unionId", reportParameterForm.getUnion().getId());
            }
            parameter.put("locale", reportParameterForm.getLanguage());

            JasperPrint jp = null;
            System.out.println("jp = " + jp);
            switch (reportParameterForm.getReportGenerationType()) {
                // View Details
                case "Details": {
                    List<PaymentReportData> paymentReportDataList = this.paymentService.getPaymentReportDataList(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(paymentReportDataList);
                    jp = getReport(ds, reportParameterForm, paymentReportDataList.isEmpty());
                    break;
                }
                // View Summary
                case "Summary": {
                    List<PaymentReportData> paymentReportDataList = this.paymentService.getPaymentReportDataCountList(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(paymentReportDataList);
                    jp = getReport(ds, reportParameterForm, paymentReportDataList.isEmpty());
                    break;
                }
                // View As Group
                case "Group": {
                    System.out.println("Group Report");
                    List<PaymentReportData> paymentReportDataList = this.paymentService.getPaymentReportDataGroupList(parameter);
                    System.out.println("paymentReportDataList = " + paymentReportDataList.size());
                    JRDataSource ds = new JRBeanCollectionDataSource(paymentReportDataList);
                    jp = getReport(ds, reportParameterForm, paymentReportDataList.isEmpty());
                    break;
                }
                default:
                    break;
            }

            ResponseEntity<byte[]> responseEntity = null;
            if (reportParameterForm.getReportExportType().equals("pdf")) {
                byte[] contents = ReportUtility.getPDFContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
//                String filename = "Payment_Report.pdf";
//                headers.add("content-disposition", "attachment; filename=" + filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            } else if (reportParameterForm.getReportExportType().equals("excel")) {
                byte[] contents = ReportUtility.getExcelContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                String fileName = "Payment_Report.xls";
                headers.add("Content-Disposition", "inline; filename=" + fileName);
                headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            return responseEntity;
        } catch (ColumnBuilderException | ClassNotFoundException | JRException e) {
            e.printStackTrace();
            //logger.infoer(e.getMessage());
            return null;
        }
    }

    public JasperPrint getReport(JRDataSource ds, ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, JRException, ClassNotFoundException {
        DynamicReport dynaReport = null;
        switch (reportParameterForm.getReportGenerationType()) {
            case "Details":
                dynaReport = getPaymentDetailReport(reportParameterForm, emptyData);
                break;
            case "Summary":
                dynaReport = getPaymentSummaryReport(reportParameterForm, emptyData);
                break;
            case "Group":
                dynaReport = getPaymentGroupReport(reportParameterForm);
                break;
            default:
                break;
        }

        try {
            JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), ds);
            return jp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private DynamicReport getPaymentDetailReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException {
        try {
            DynamicReportBuilder drb = new DynamicReportBuilder();

            Style titleStyle = ReportUtility.createTitleStyle();
            Style subTitleStyle = ReportUtility.createSubTitleStyle();
            Style headerStyle = ReportUtility.createHeaderStyle();
            Style detailTextStyle = ReportUtility.createDetailTextStyle();
            Style detailNumStyle = ReportUtility.createDetailNumberStyle();
            Style detailAmountStyle = ReportUtility.createDetailAmountStyle();
            Style leftRightHeaderStyle = ReportUtility.createLeftRightStyle();

            AbstractColumn columnUnion = null, columnSerialNumber = null;
            Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage()).build();
            Style groupStyleUnion = ReportUtility.createUnionGroupStyle();
            if (!emptyData) {
                columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);
                String name = localizer.getLocalizedText("label.name", locale);
                AbstractColumn columnName = ReportUtility.createColumn("name", String.class, name, 35, headerStyle, detailTextStyle);
                String nid = localizer.getLocalizedText("label.nid", locale);
                AbstractColumn columnNid = ReportUtility.createColumn("nid", String.class, nid, 42, headerStyle, detailTextStyle);
                String fatherName = localizer.getLocalizedText("label.fatherName", locale);
                AbstractColumn columnFatherName = ReportUtility.createColumn("fatherName", String.class, fatherName, 30, headerStyle, detailTextStyle);
                String motherName = localizer.getLocalizedText("label.motherName", locale);
                AbstractColumn columnMotherName = ReportUtility.createColumn("motherName", String.class, motherName, 30, headerStyle, detailTextStyle);
                String spouseName = localizer.getLocalizedText("label.spouseName", locale);
                AbstractColumn columnSpouseName = ReportUtility.createColumn("spouseName", String.class, spouseName, 30, headerStyle, detailTextStyle);
                String mobileNo = localizer.getLocalizedText("label.mobileNo", locale);
                AbstractColumn columnMobileNo = ReportUtility.createColumn("mobileNo", String.class, mobileNo, 28, headerStyle, detailTextStyle);
                String accountNo = localizer.getLocalizedText("label.accountNo", locale);
                AbstractColumn columnAccounteNo = ReportUtility.createColumn("accountNo", String.class, accountNo, 28, headerStyle, detailTextStyle);
                String paymentProvider = localizer.getLocalizedText("label.paymentProvider", locale);
                AbstractColumn columnPaymentProvider = ReportUtility.createColumn("paymentProvider", String.class, paymentProvider, 35, headerStyle, detailTextStyle);
                String branch = localizer.getLocalizedText("label.branch", locale);
                AbstractColumn columnBranch = ReportUtility.createColumn("branch", String.class, branch, 25, headerStyle, detailTextStyle);
                String allowanceAmount = localizer.getLocalizedText("label.allowanceAmount", locale);
                AbstractColumn columnAllowance = ReportUtility.createColumn("allowanceAmount", String.class, allowanceAmount, 20, headerStyle, detailAmountStyle);

                drb.addColumn(columnSerialNumber).addColumn(columnName).addColumn(columnNid).addColumn(columnFatherName).addColumn(columnMotherName).addColumn(columnSpouseName).addColumn(columnMobileNo).addColumn(columnPaymentProvider).addColumn(columnBranch).addColumn(columnAccounteNo).addColumn(columnAllowance);

                if (reportParameterForm.getUnion() == null || reportParameterForm.getUnion().getId() == null) {
                    String union = localizer.getLocalizedText("label.union", locale);
                    columnUnion = ReportUtility.createColumn("union", String.class, union, 30, headerStyle, detailTextStyle);
                    drb.addColumn(columnUnion);
                }
                if (reportParameterForm.getDivision() == null || reportParameterForm.getDivision().getId() == null) {
                    GroupBuilder gb4 = new GroupBuilder();
                    columnUnion.setStyle(groupStyleUnion);
                    DJGroup dJGroup4 = gb4.setCriteriaColumn((PropertyColumn) columnUnion).setGroupLayout(GroupLayout.VALUE_IN_HEADER).build();
                    drb.addGroup(dJGroup4);
                } else if (reportParameterForm.getDistrict() == null || reportParameterForm.getDistrict().getId() == null) {
                    GroupBuilder gb3 = new GroupBuilder();
                    columnUnion.setStyle(groupStyleUnion);
                    DJGroup dJGroup3 = gb3.setCriteriaColumn((PropertyColumn) columnUnion).setGroupLayout(GroupLayout.VALUE_IN_HEADER).build();
                    drb.addGroup(dJGroup3);
                } else if (reportParameterForm.getUpazila() == null || reportParameterForm.getUpazila().getId() == null) {
                    GroupBuilder gb2 = new GroupBuilder();
                    columnUnion.setStyle(groupStyleUnion);
                    DJGroup dJGroup2 = gb2.setCriteriaColumn((PropertyColumn) columnUnion).setGroupLayout(GroupLayout.VALUE_IN_HEADER).build();
                    drb.addGroup(dJGroup2);
                }

            }
            if (reportParameterForm.getReportOrientation().equals("Portrait")) {
                drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
            } else {
                drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            }

            //drb.setOddRowBackgroundStyle(ReportUtility.createAlternateRowStyle(drb));
            String payrollReport = localizer.getLocalizedText("payrollReport", locale);
            drb.setTitle(payrollReport);
            drb.setTitleStyle(titleStyle);

            String subTitle = "";

//            if (reportParameterForm.getScheme().getId() != null)
//            {
//                String schemeSt = localizer.getLocalizedText("label.scheme", locale);
//                Scheme scheme = this.schemeService.getScheme(reportParameterForm.getScheme().getId());
//                subTitle += schemeSt + " : " + (locale.toString().equals("bn") == true ? scheme.getNameInBangla() : scheme.getNameInEnglish());
//                System.out.println("subTitle = " + subTitle);
//            }
            if (reportParameterForm.getFiscalYear().getId() != null) {
                String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
                FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
                subTitle += fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish());
            }
            if (reportParameterForm.getPaymentCycle().getId() != null) {
                String paymentCycleSt = localizer.getLocalizedText("label.paymentCycle", locale);
                PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(reportParameterForm.getPaymentCycle().getId());
                subTitle += "\\t\\t" + paymentCycleSt + " : " + (locale.toString().equals("bn") == true ? paymentCycle.getNameInBangla() : paymentCycle.getNameInEnglish());
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
            drb.setAllowDetailSplit(false);
            if (reportParameterForm.getReportExportType().equals("excel")) {
                drb.setIgnorePagination(true); // will generate in single sheet
            } else {
                drb.setIgnorePagination(false);
            }
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
            drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle());
            // drb.addGlobalFooterVariable(columnAllowance, DJCalculation.SUM);
            // drb.setGrandTotalLegend("TOTAL");
            return drb.build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private DynamicReport getPaymentSummaryReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException {
        DynamicReportBuilder drb = new DynamicReportBuilder();

        Style titleStyle = ReportUtility.createTitleStyle();
        Style subTitleStyle = ReportUtility.createSubTitleStyle();
        Style headerStyle = ReportUtility.createHeaderStyle();
        Style detailTextStyle = ReportUtility.createDetailTextStyle();
        Style detailNumStyle = ReportUtility.createDetailNumberStyle();
        Style detailAmountStyle = ReportUtility.createDetailAmountStyle();
        Style leftRightHeaderStyle = ReportUtility.createLeftRightStyle();

        Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage()).build();
        String divisionSt = localizer.getLocalizedText("label.division", locale);
        String districtSt = localizer.getLocalizedText("label.district", locale);
        String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
        String unionSt = localizer.getLocalizedText("label.union", locale);
        String schemeSt = localizer.getLocalizedText("label.scheme", locale);
        String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
        String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
//        String cycleNameSt = localizer.getLocalizedText("label.cycleName", locale);
        String noOfBeneficiariesSt = localizer.getLocalizedText("label.No.Of.Beneficiaries", locale);
        String totalAllowanceAmount = localizer.getLocalizedText("label.totalAllowanceAmount", locale);
        String paymentCycleSt = localizer.getLocalizedText("label.paymentCycle", locale);
        String payrollReportSummarySt = localizer.getLocalizedText("label.payrollReportSummary", locale);
        String totalSt = localizer.getLocalizedText("label.total", locale);
        AbstractColumn columnAllowanceAmount = null, columnBeneficiaryCount = null;
        if (!emptyData) {
//        AbstractColumn columnCycleName = ReportUtility.createColumn("cycleName", String.class, cycleNameSt, 30, headerStyle, detailTextStyle);
//        drb.addColumn(columnCycleName);
            if (reportParameterForm.getDivision() == null || reportParameterForm.getDivision().getId() == null) {
                AbstractColumn columnDivision = ReportUtility.createColumn("division", String.class, divisionSt, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnDivision);
            } else {
                if (reportParameterForm.getDistrict() == null || reportParameterForm.getDistrict().getId() == null) {
                    AbstractColumn columnDistrict = ReportUtility.createColumn("district", String.class, districtSt, 30, headerStyle, detailTextStyle);
                    drb.addColumn(columnDistrict);
                } else {
                    if (reportParameterForm.getUpazila() == null || reportParameterForm.getUpazila().getId() == null) {
                        AbstractColumn columnUpazila = ReportUtility.createColumn("upazila", String.class, upazilaSt, 30, headerStyle, detailTextStyle);
                        drb.addColumn(columnUpazila);
                    } else {
                        if (reportParameterForm.getUnion() == null || reportParameterForm.getUnion().getId() == null) {
                            AbstractColumn columnUnion = ReportUtility.createColumn("union", String.class, unionSt, 30, headerStyle, detailTextStyle);
                            drb.addColumn(columnUnion);
                        }
                    }
                }
            }

            columnBeneficiaryCount = ReportUtility.createColumn("beneficiaryCount", String.class, noOfBeneficiariesSt, 30, headerStyle, detailTextStyle);
            drb.addColumn(columnBeneficiaryCount);
            columnAllowanceAmount = ReportUtility.createColumn("allowanceAmount", String.class, totalAllowanceAmount, 30, headerStyle, detailAmountStyle);
            drb.addColumn(columnAllowanceAmount);
        }
        if (reportParameterForm.getReportOrientation().equals("Portrait")) {
            drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
        } else {
            drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        }

        drb.setOddRowBackgroundStyle(ReportUtility.createAlternateRowStyle(drb));

        drb.setTitle(payrollReportSummarySt);
        drb.setTitleStyle(titleStyle);

        String subTitle = "";

//        if (reportParameterForm.getScheme().getId() != null)
//        {
//            Scheme scheme = this.schemeService.getScheme(reportParameterForm.getScheme().getId());
//            subTitle += schemeSt + " : " + (locale.toString().equals("bn") == true ? scheme.getNameInBangla() : scheme.getNameInEnglish());
//        }
        if (reportParameterForm.getFiscalYear().getId() != null) {
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
            subTitle += fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish());
        }
        if (reportParameterForm.getPaymentCycle().getId() != null) {
            PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(reportParameterForm.getPaymentCycle().getId());
            subTitle += "\\t\\t" + paymentCycleSt + " : " + (locale.toString().equals("bn") == true ? paymentCycle.getNameInBangla() : paymentCycle.getNameInEnglish());
        }
        if (reportParameterForm.getDivision() != null && reportParameterForm.getDivision().getId() != null) {
            Division division = this.divisionService.getDivision(reportParameterForm.getDivision().getId());
            subTitle += "\\n" + divisionSt + " :" + (locale.toString().equals("bn") == true ? division.getNameInBangla() : division.getNameInEnglish());
        }
        if (reportParameterForm.getDistrict() != null && reportParameterForm.getDistrict().getId() != null) {
            District district = this.districtService.getDistrict(reportParameterForm.getDistrict().getId());
            subTitle += "\\t\\t" + districtSt + " : " + (locale.toString().equals("bn") == true ? district.getNameInBangla() : district.getNameInEnglish());
        }
        if (reportParameterForm.getUpazila() != null && reportParameterForm.getUpazila().getId() != null) {
            Upazilla upazila = this.upazilaService.getUpazilla(reportParameterForm.getUpazila().getId());
            subTitle += "\\t\\t" + upazilaSt + " : " + (locale.toString().equals("bn") == true ? upazila.getNameInBangla() : upazila.getNameInEnglish());
        }
        if (reportParameterForm.getUnion() != null && reportParameterForm.getUnion().getId() != null) {
            Union union = this.unionService.getUnion(reportParameterForm.getUnion().getId());
            subTitle += "\\t\\t" + unionSt + " : " + (locale.toString().equals("bn") == true ? union.getNameInBangla() : union.getNameInEnglish());
        }
        subTitle += "\\n";
        drb.setSubtitle(subTitle);
        drb.setSubtitleStyle(subTitleStyle);

        drb.setUseFullPageWidth(true);
        drb.setRightMargin(30);
        drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
        drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
        drb.setBottomMargin(20);
        if (locale.toString().equals("bn")) {
            drb.addFirstPageImageBanner("image/banner_bn3.png", 406, 55, ImageBanner.ALIGN_CENTER);
        } else {
            drb.addFirstPageImageBanner("image/banner_en3.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }
        if (!emptyData) {
            drb.addField("totalAllowanceAmount", String.class.getName());
            drb.addGlobalFooterVariable(columnAllowanceAmount, new CustomExpression() {

                @Override
                public Object evaluate(Map fields, Map variables, Map parameters) {
                    String a = (String) fields.get("totalAllowanceAmount");
                    return a;
                }

                @Override
                public String getClassName() {
                    return String.class.getName();
                }
            });
            drb.addField("benTotal", String.class.getName());
            drb.addGlobalFooterVariable(columnBeneficiaryCount, new CustomExpression() {

                @Override
                public Object evaluate(Map fields, Map variables, Map parameters) {
                    String a = (String) fields.get("benTotal");
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
        drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle());

        return drb.build();
    }

    private DynamicReport getPaymentGroupReport(ReportParameterForm reportParameterForm) throws ColumnBuilderException, ClassNotFoundException {
        try {
            Style titleStyle = ReportUtility.createTitleStyle();
            Style subTitleStyle = ReportUtility.createSubTitleStyle();
            Style headerStyle = ReportUtility.createHeaderStyle();
            Style detailTextStyle = ReportUtility.createDetailTextStyle();
            Style detailNumStyle = ReportUtility.createDetailNumberStyle();
            Style detailAmountStyle = ReportUtility.createDetailAmountStyle();
            Style leftRightHeaderStyle = ReportUtility.createLeftRightStyle();

            Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage()).build();
            String divisionSt = localizer.getLocalizedText("label.division", locale);
            String districtSt = localizer.getLocalizedText("label.district", locale);
            String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
            String unionSt = localizer.getLocalizedText("label.union", locale);
            String totalSt = localizer.getLocalizedText("label.total", locale);
            String schemeSt = localizer.getLocalizedText("label.scheme", locale);
            String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
            String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
            String cycleNameSt = localizer.getLocalizedText("label.cycleName", locale);
            String NoOfBeneficiariesSt = localizer.getLocalizedText("label.No.Of.Beneficiaries", locale);
            String totalAllowanceAmount = localizer.getLocalizedText("label.totalAllowanceAmount", locale);
            String paymentCycleSt = localizer.getLocalizedText("label.paymentCycle", locale);
            String payrollGroupReportSt = localizer.getLocalizedText("label.payrollGroupReport", locale);

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

            AbstractColumn columnDivision = ReportUtility.createColumn("division", String.class, divisionSt + " :", 50, groupTitleStyle, titleStyle1);
            AbstractColumn columnDistrict = ReportUtility.createColumn("district", String.class, districtSt + " :", 50, col2Style, col2Style);
            AbstractColumn columnUpazila = ReportUtility.createColumn("upazila", String.class, upazilaSt, 30, headerStyle, detailTextStyle);
            AbstractColumn columnUnionTotal = ReportUtility.createColumn("unionCount", String.class, totalSt + " " + unionSt, 30, headerStyle, detailNumStyle);
            AbstractColumn columnNoOfBeneficiaries = ReportUtility.createColumn("beneficiaryCount", String.class, NoOfBeneficiariesSt, 30, headerStyle, detailNumStyle);
            AbstractColumn columnTotalAllowanceAmount = ReportUtility.createColumn("totalAllowanceAmount", String.class, totalAllowanceAmount, 30, headerStyle, detailAmountStyle);
            drb.addColumn(columnDivision).addColumn(columnDistrict).addColumn(columnUpazila).addColumn(columnUnionTotal).addColumn(columnNoOfBeneficiaries).addColumn(columnTotalAllowanceAmount);

            Style g1VariablesStyle = ReportUtility.createGroup1VariableStyle();
            Style g1AmountVariableStyle = ReportUtility.createGroup1AmountStyle();
            Style g2VariablesStyle = ReportUtility.createGroup2VariableStyle();
            Style g2AmountVariableStyle = ReportUtility.createGroup2AmountStyle();

            GroupBuilder gb1 = new GroupBuilder();

            //		 define the criteria column to group by (columnState)
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columnDivision)
                    .addFooterVariable(columnUpazila, DJCalculation.COUNT, g1VariablesStyle)
                    .addFooterVariable(columnUnionTotal, DJCalculation.SUM, g1VariablesStyle)
                    .addFooterVariable(columnNoOfBeneficiaries, DJCalculation.SUM, g1VariablesStyle)
                    .addFooterVariable(columnTotalAllowanceAmount, DJCalculation.SUM, g1AmountVariableStyle)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            GroupBuilder gb2 = new GroupBuilder(); // Create another group (using another column as criteria)
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columnDistrict) // and we add the same operations for the columnAmount and
                    .addFooterVariable(columnUpazila, DJCalculation.COUNT, g2VariablesStyle)
                    .addFooterVariable(columnUnionTotal, DJCalculation.SUM, g2VariablesStyle)
                    .addFooterVariable(columnNoOfBeneficiaries, DJCalculation.SUM, g2VariablesStyle)
                    .addFooterVariable(columnTotalAllowanceAmount, DJCalculation.SUM, g2AmountVariableStyle)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g1); // add group g1
            drb.addGroup(g2); // add group g2

            if (reportParameterForm.getReportOrientation().equals("Portrait")) {
                drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
            } else {
                drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            }

            drb.setOddRowBackgroundStyle(ReportUtility.createAlternateRowStyle(drb));

            drb.setTitle(payrollGroupReportSt);
            drb.setTitleStyle(titleStyle);

            String subTitle = "";
            if (reportParameterForm.getScheme().getId() != null) {
                Scheme scheme = this.schemeService.getScheme(reportParameterForm.getScheme().getId());
                subTitle += schemeSt + " : " + (locale.toString().equals("bn") == true ? scheme.getNameInBangla() : scheme.getNameInEnglish());
            }
            if (reportParameterForm.getFiscalYear().getId() != null) {
                FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
                subTitle += "\\t\\t " + fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish());
            }
            if (reportParameterForm.getPaymentCycle().getId() != null) {
                PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(reportParameterForm.getPaymentCycle().getId());
                subTitle += "\\t\\t" + paymentCycleSt + " : " + (locale.toString().equals("bn") == true ? paymentCycle.getNameInBangla() : paymentCycle.getNameInEnglish());
            }
            subTitle += "\\n";
            drb.setSubtitle(subTitle);
            drb.setSubtitleStyle(subTitleStyle);
            drb.setUseFullPageWidth(true);
            drb.setRightMargin(30);
            drb.setPrintColumnNames(false);
            drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
            drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
            drb.setBottomMargin(20);
            if (locale.toString().equals("bn")) {
                drb.addFirstPageImageBanner("image/banner_bn3.png", 406, 55, ImageBanner.ALIGN_CENTER);
            } else {
                drb.addFirstPageImageBanner("image/banner_en3.png", 406, 55, ImageBanner.ALIGN_CENTER);
            }
            drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle());

            return drb.build();

        } catch (ColumnBuilderException e) {
            e.printStackTrace();
            //logger.infoer(e.getMessage());
            return null;
        }
    }
}
