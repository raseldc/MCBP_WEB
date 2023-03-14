/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.controller;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.ImageBanner;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import com.wfp.lmmis.applicant.service.BeneficiaryPaymentCompleteService;
import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.FactoryService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.report.data.BeneficiaryReportData;
import com.wfp.lmmis.utility.DateUtilities;
import com.wfp.lmmis.utility.IntegerTypeAdapter;
import com.wfp.lmmis.utility.Localizer;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.io.IOException;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Controller
public class BeneficiaryPaymentCompleteReport {

    //private static final logger logger = //logger.getlogger(BeneficiaryReportController.class);

    @Autowired
    private BeneficiaryPaymentCompleteService beneficiaryPaymentCompleteService;
    @Autowired
    private BeneficiaryService beneficiaryService;
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
    @Autowired
    private FactoryService factoryService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/beneficiary-payment-complete-report", method = RequestMethod.POST)
    //@ResponseBody
    public ResponseEntity<byte[]> getBeneficiaryReport(ReportParameterForm reportParameterForm, HttpServletResponse response, HttpSession session, HttpServletRequest request) throws IOException {
        try {
            reportParameterForm.setReportExportType("pdf");
            Map parameter = new HashMap();
            // String wardNo = request.getParameter("wardNumber");
            parameter.put("fiscalYearId", reportParameterForm.getFiscalYear().getId());
            // parameter.put("beneficiaryStatus", reportParameterForm.getBeneficiaryStatus());
            parameter.put("divisionId", reportParameterForm.getDivision() != null ? reportParameterForm.getDivision().getId() : null);
            parameter.put("districtId", reportParameterForm.getDistrict() != null ? reportParameterForm.getDistrict().getId() : null);
            parameter.put("upazilaId", reportParameterForm.getUpazila() != null ? reportParameterForm.getUpazila().getId() : null);
            if (reportParameterForm.getUnion() != null) {
                parameter.put("unionId", reportParameterForm.getUnion().getId());
            }
            parameter.put("locale", reportParameterForm.getLanguage());

            parameter.put("applicantType", reportParameterForm.getApplicantType());
            parameter.put("bgmeaId", reportParameterForm.getBgmeaFactory() != null ? reportParameterForm.getBgmeaFactory().getId() : null);
            parameter.put("bkmeaId", reportParameterForm.getBkmeaFactory() != null ? reportParameterForm.getBkmeaFactory().getId() : null);
            parameter.put("orderBy", request.getParameter("orderBy"));

            Calendar startDate = null, endDate = null;
            if (reportParameterForm.getStartDate() != null && !"".equals(reportParameterForm.getStartDate())) {
                startDate = DateUtilities.stringToCalendar(reportParameterForm.getStartDate());
                startDate.set(Calendar.HOUR_OF_DAY, 0);
            }
            if (reportParameterForm.getEndDate() != null && !"".equals(reportParameterForm.getEndDate())) {
                endDate = DateUtilities.stringToCalendar(reportParameterForm.getEndDate());
                endDate.set(Calendar.HOUR_OF_DAY, 0);
                endDate.add(Calendar.DATE, 1);
            }
            parameter.put("startDate", startDate);
            parameter.put("endDate", endDate);
            //      parameter.put("wardNo", (wardNo != null && !"".equals(wardNo)) ? Integer.parseInt(wardNo) : null);
            Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerTypeAdapter()).create();
            PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo = new PaymnetInformationViewSearchInfo();
            paymnetInformationViewSearchInfo.setFiscalYear(reportParameterForm.getFiscalYear().getId());
            paymnetInformationViewSearchInfo.setDivisionId(reportParameterForm.getDivision() != null ? reportParameterForm.getDivision().getId() : null);
            paymnetInformationViewSearchInfo.setDistrictId(reportParameterForm.getDistrict() != null ? reportParameterForm.getDistrict().getId() : null);
            paymnetInformationViewSearchInfo.setUpazilaId(reportParameterForm.getUpazila() != null ? reportParameterForm.getUpazila().getId() : null);
            paymnetInformationViewSearchInfo.setUnionId(reportParameterForm.getUnion() != null ? reportParameterForm.getUnion().getId() : null);
            paymnetInformationViewSearchInfo.setNid(reportParameterForm.getNid());
            paymnetInformationViewSearchInfo.setMobileNo(reportParameterForm.getMobileNo());
            paymnetInformationViewSearchInfo.setAccountNumber(reportParameterForm.getAccountNumber());
            paymnetInformationViewSearchInfo.setApplicantType(reportParameterForm.getApplicantType());
            JasperPrint jp = null;
            //  List<BeneficiaryReportData> beneficiaryReportDataList = this.beneficiaryService.getBeneficiaryReportData(parameter);
            List<PaymentInformationView> paymentInformationViews = beneficiaryPaymentCompleteService.getPaymentCompleteBeneficiaryList(0, 10000, paymnetInformationViewSearchInfo);

            JRDataSource ds = new JRBeanCollectionDataSource(paymentInformationViews);
            jp = getReport(ds, reportParameterForm, paymentInformationViews.isEmpty());

            ResponseEntity<byte[]> responseEntity = null;
            if (reportParameterForm.getReportExportType().equals("pdf")) {

                byte[] contents = ReportUtility.getPDFContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
                headers.setContentLength(contents.length);
                String filename = "Beneficiary_Report.pdf";
                headers.add("content-disposition", "inline; filename=" + filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            } else if (reportParameterForm.getReportExportType().equals("excel")) {
                byte[] contents = ReportUtility.getExcelContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                String fileName = "Beneficiary_Report.xls";
                headers.add("Content-Disposition", "inline; filename=" + fileName);
                headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            System.out.println("responseEntity = " + responseEntity.getBody());
            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            //logger.infoer(e.getMessage());
            return null;
        }
    }

    public JasperPrint getReport(JRDataSource ds, ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, JRException, ClassNotFoundException {
        DynamicReport dynaReport = null;
        dynaReport = getBenDetailDynamicReport(reportParameterForm, emptyData);

        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), ds);
        return jp;
    }

    private DynamicReport getBenDetailDynamicReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException {

        DynamicReportBuilder drb = new DynamicReportBuilder();

        Style titleStyle = ReportUtility.createTitleStyle();
        Style subTitleStyle = ReportUtility.createSubTitleStyle();
        Style headerStyle = ReportUtility.createHeaderStyle();
        Style detailTextStyle = ReportUtility.createDetailTextStyle();
        Style detailNumStyle = ReportUtility.createDetailNumberStyle();
        Style groupStyleUnion = ReportUtility.createUnionGroupStyle();
        AbstractColumn columnUnion = null;
        Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage() == null ? "bn" : reportParameterForm.getLanguage()).build();
        if (!emptyData) {
            AbstractColumn columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);
            String name = localizer.getLocalizedText("label.name", locale);
            AbstractColumn columnBenName = ReportUtility.createColumn("benNameBn", String.class, name, 30, headerStyle, detailTextStyle);
            String nid = localizer.getLocalizedText("label.nid", locale);
            AbstractColumn columnNID = ReportUtility.createColumn("nid", String.class, nid, 40, headerStyle, detailTextStyle);
//            String fatherName = localizer.getLocalizedText("label.fatherName", locale);
//            AbstractColumn columnFatherName = ReportUtility.createColumn("fatherName", String.class, fatherName, 30, headerStyle, detailTextStyle);
//            String motherName = localizer.getLocalizedText("label.motherName", locale);
//            AbstractColumn columnMotherName = ReportUtility.createColumn("motherName", String.class, motherName, 30, headerStyle, detailTextStyle);
//            String spouseName = localizer.getLocalizedText("label.spouseName", locale);
//            AbstractColumn columnSpouseName = ReportUtility.createColumn("spouseName", String.class, spouseName, 30, headerStyle, detailTextStyle);
            String mobileNo = localizer.getLocalizedText("label.mobileNo", locale);
            AbstractColumn columnMobileNo = ReportUtility.createColumn("mobile", String.class, mobileNo, 30, headerStyle, detailTextStyle);

            String acountNumber = localizer.getLocalizedText("label.accountNo", locale);
            AbstractColumn columnAcountNumber = ReportUtility.createColumn("accountNumber", String.class, acountNumber, 30, headerStyle, detailTextStyle);

            String lastPaymentCycleDate = localizer.getLocalizedText("label.lastPaymentCycleDate", locale);
            AbstractColumn columnLastPaymentCycleDate = ReportUtility.createColumn("lastPaymentCycleDate", String.class, lastPaymentCycleDate, 30, headerStyle, detailTextStyle);

            drb.addColumn(columnSerialNumber).addColumn(columnBenName).addColumn(columnNID).addColumn(columnMobileNo).addColumn(columnAcountNumber).addColumn(columnLastPaymentCycleDate);

            if (reportParameterForm.getDivision() == null || reportParameterForm.getDivision().getId() == null) {
                String divisionSt = localizer.getLocalizedText("label.division", locale);
                AbstractColumn columnDivision = ReportUtility.createColumn("divisionNameBN", String.class, divisionSt, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnDivision);

            }
            if (reportParameterForm.getDistrict() == null || reportParameterForm.getDistrict().getId() == null) {
                String district = localizer.getLocalizedText("label.district", locale);
                AbstractColumn columnDistrict = ReportUtility.createColumn("districtNameBN", String.class, district, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnDistrict);

            }
            if (reportParameterForm.getUpazila() == null || reportParameterForm.getUpazila().getId() == null) {
                String upazila = localizer.getLocalizedText("label.upazila", locale);
                AbstractColumn columnUpazila = ReportUtility.createColumn("upazilaNameBN", String.class, upazila, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnUpazila);

            }
            if (reportParameterForm.getUnion() == null || reportParameterForm.getUnion().getId() == null || reportParameterForm.getUnion().getId() == 0) {
                String unionSt = reportParameterForm.getApplicantType() != ApplicantType.UNION ? localizer.getLocalizedText("label.municipalOrCityCorporation", locale) : localizer.getLocalizedText("label.union", locale);
                columnUnion = ReportUtility.createColumn("union", String.class, unionSt, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnUnion);
            }
//            String wardNo = localizer.getLocalizedText("label.ward", locale);
//            AbstractColumn columnWardNo = ReportUtility.createColumn("wardNo", String.class, wardNo, 30, headerStyle, detailTextStyle);
//            drb.addColumn(columnWardNo);
//            String villageSt = localizer.getLocalizedText("label.village", locale);
//            AbstractColumn columnVillage = ReportUtility.createColumn("village", String.class, villageSt, 30, headerStyle, detailTextStyle);
//            drb.addColumn(columnVillage);
//            if (reportParameterForm.getBeneficiaryStatus() == null) {
//                String status = localizer.getLocalizedText("label.beneficiaryStatus", locale);
//                AbstractColumn columnUnion = ReportUtility.createColumn("status", String.class, status, 30, headerStyle, detailTextStyle);
//                drb.addColumn(columnUnion);
//            }

            if (reportParameterForm.getUnion() == null || reportParameterForm.getUnion().getId() == null || reportParameterForm.getUnion().getId() == 0) {

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

        drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
//        if (reportParameterForm.getReportOrientation().equals("Portrait")) {
//            drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
//        } else {
//            drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
//        }
        String beneficiaryReport = localizer.getLocalizedText("label.beneficiaryCompleteReport", locale);
        drb.setTitle(beneficiaryReport);
        drb.setTitleStyle(titleStyle);

        String subTitle = "";

//        if (reportParameterForm.getScheme().getId() != null)
//        {
//            String schemeSt = localizer.getLocalizedText("label.scheme", locale);
//            Scheme scheme = this.schemeService.getScheme(reportParameterForm.getScheme().getId());
//            subTitle += schemeSt + " : " + (locale.toString().equals("bn") == true ? scheme.getNameInBangla() : scheme.getNameInEnglish());
//        }
        if (reportParameterForm.getFiscalYear().getId() != 0 && reportParameterForm.getFiscalYear().getId() != null) {
            String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
            subTitle += "\\t\\t" + fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish());
        }

//        if (reportParameterForm.getBeneficiaryStatus() != null) {
//            String status = localizer.getLocalizedText("label.beneficiaryStatus", locale);
//            subTitle += "\\t\\t" + status + " : " + (locale.toString().equals("bn") == true ? reportParameterForm.getBeneficiaryStatus().getDisplayNameBn() : reportParameterForm.getBeneficiaryStatus().getDisplayName());
//        }
        if (reportParameterForm.getDivision() != null && reportParameterForm.getDivision().getId() != null && reportParameterForm.getDivision().getId() != 0) {
            String divisionSt = localizer.getLocalizedText("label.division", locale);
            Division division = this.divisionService.getDivision(reportParameterForm.getDivision().getId());
            subTitle += "\\n" + divisionSt + " : " + (locale.toString().equals("bn") == true ? division.getNameInBangla() : division.getNameInEnglish());
        }
        if (reportParameterForm.getDistrict() != null && reportParameterForm.getDistrict().getId() != null && reportParameterForm.getDistrict().getId() != 0) {
            String districtSt = localizer.getLocalizedText("label.district", locale);
            District district = this.districtService.getDistrict(reportParameterForm.getDistrict().getId());
            subTitle += "\\t\\t" + districtSt + " : " + (locale.toString().equals("bn") == true ? district.getNameInBangla() : district.getNameInEnglish());
        }
        if (reportParameterForm.getUpazila() != null && reportParameterForm.getUpazila().getId() != null && reportParameterForm.getUpazila().getId() != 0) {
            String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
            Upazilla upazila = this.upazilaService.getUpazilla(reportParameterForm.getUpazila().getId());
            subTitle += "\\t\\t" + upazilaSt + " : " + (locale.toString().equals("bn") == true ? upazila.getNameInBangla() : upazila.getNameInEnglish());
        }
        if (reportParameterForm.getUnion() != null && reportParameterForm.getUnion().getId() != null && reportParameterForm.getUnion().getId() != 0) {
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
            drb.addFirstPageImageBanner("image/banner_bn.png", 406, 55, ImageBanner.ALIGN_CENTER);
//            drb.addFirstPageImageBanner("image/gov-logo.png", 70, 70, ImageBanner.ALIGN_LEFT);
        } else {
            drb.addFirstPageImageBanner("image/banner_en.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }

        drb.setIgnorePagination(false);
//        if (reportParameterForm.getReportExportType().equals("excel")) {
//            drb.setIgnorePagination(true); // will generate in single sheet
//        } else {
//            drb.setIgnorePagination(false);
//        }
        drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
        drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
        drb.setUseFullPageWidth(true);
        drb.setAllowDetailSplit(false);

        String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
        drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle(), true, true);
        return drb.build();
    }

}
