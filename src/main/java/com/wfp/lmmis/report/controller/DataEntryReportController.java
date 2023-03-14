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
import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.report.data.DataEntryReportData;
import com.wfp.lmmis.report.data.DataEntryReportDataByUser;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.Localizer;
import java.awt.Color;
import java.io.IOException;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author user
 */
@Controller
public class DataEntryReportController
{

    //private static final logger logger = //logger.getlogger(DataEntryReportController.class);

    @Autowired
    private BeneficiaryService beneficiaryService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private FiscalYearService fiscalYearService;
  
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/dataEntryReport")
    public String getDataEntryReport(Model model, HttpServletRequest request)
    {
        model.addAttribute("reportParameterForm", CommonUtility.loadReportParameterForm(request));
//        CommonUtility.mapSchemeName(model);
        CommonUtility.mapAllFiscalYearName(model);
        return "dataEntryReport";
    }

    @RequestMapping(value = "/dataEntryReport", method = RequestMethod.POST)
    //@ResponseBody
    public ResponseEntity<byte[]> getDataEntryReport(ReportParameterForm reportParameterForm, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try
        {
            Map parameter = new HashMap();
            Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
            reportParameterForm.setScheme(scheme);
            parameter.put("schemeId", scheme.getId());
            parameter.put("fiscalYearId", reportParameterForm.getFiscalYear().getId());
            parameter.put("locale", LocaleContextHolder.getLocale());
            User user = (User) request.getSession().getAttribute("user");
            parameter.put("userId", user.getId());

            JasperPrint jp = null;
            switch (reportParameterForm.getReportGenerationType())
            {
                case "Details":
                {
                    List<DataEntryReportData> dataEntryReportDataList = this.beneficiaryService.getDataEntryDetailsReportData(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(dataEntryReportDataList);
                    jp = getReport(ds, reportParameterForm, dataEntryReportDataList.isEmpty());
                    break;
                }
                case "Summary":
                {
                    List<DataEntryReportDataByUser> beneficiarySummaryReportDataList = this.beneficiaryService.getDataEntrySummaryReportData(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(beneficiarySummaryReportDataList);
                    jp = getReport(ds, reportParameterForm, beneficiarySummaryReportDataList.isEmpty());
                    break;
                }
                default:
                    break;
            }
            ResponseEntity<byte[]> responseEntity = null;
            if (reportParameterForm.getReportExportType().equals("pdf"))
            {
//            String reportDataAsString = Base64.encodeBase64String(ReportUtility.getPDFContents(jp, response));
//            return reportDataAsString;
                byte[] contents = ReportUtility.getPDFContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
                headers.setContentLength(contents.length);
                String filename = "Beneficiary_Report.pdf";
                headers.add("content-disposition", "inline; filename=" + filename);
//                headers.add("content-disposition", "inline; filename=" + filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            else if (reportParameterForm.getReportExportType().equals("excel"))
            {
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //logger.infoer(e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param ds
     * @param reportParameterForm
     * @param emptyData
     * @return
     * @throws ColumnBuilderException
     * @throws JRException
     * @throws ClassNotFoundException
     */
    public JasperPrint getReport(JRDataSource ds, ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, JRException, ClassNotFoundException
    {
        DynamicReport dynaReport = null;
        switch (reportParameterForm.getReportGenerationType())
        {
            case "Details":
                dynaReport = getDataEntryDetailDynamicReport(reportParameterForm, emptyData);
                break;
            case "Summary":
                dynaReport = getDataEntrySummaryDynamicReport(reportParameterForm, emptyData);
                break;
            default:
                break;
        }
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), ds);
        return jp;
    }

    private DynamicReport getDataEntryDetailDynamicReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException
    {

        DynamicReportBuilder drb = new DynamicReportBuilder();

//        Style titleStyle = ReportUtility.createTitleStyle();
        Style subTitleStyle = ReportUtility.createSubTitleStyle();
        Style headerStyle = ReportUtility.createHeaderStyle();
        Style detailTextStyle = ReportUtility.createDetailTextStyle();
        Style detailNumStyle = ReportUtility.createDetailNumberStyle();
//        Style groupStyleDistrict = ReportUtility.createDistrictGroupStyle();
//        Style groupStyleUpazila = ReportUtility.createUpazilaGroupStyle();
        Style groupStyleUnion = ReportUtility.createUnionGroupStyle();

        Locale locale = LocaleContextHolder.getLocale();
        AbstractColumn columnUnion = null, columnUpazila = null, columnDistrict = null, columnDivision = null, columnSerialNumber = null, columnBenName = null;
        AbstractColumn columnUser = null;
        if (!emptyData)
        {
            columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);
            String name = localizer.getLocalizedText("label.name", locale);
            columnBenName = ReportUtility.createColumn("benName", String.class, name, 35, headerStyle, detailTextStyle);
            String nid = localizer.getLocalizedText("label.nid", locale);
            AbstractColumn columnNID = ReportUtility.createColumn("nationalID", String.class, nid, 42, headerStyle, detailTextStyle);
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
            String division = localizer.getLocalizedText("label.division", locale);
            columnDivision = ReportUtility.createColumn("division", String.class, division, 30, headerStyle, detailTextStyle);
            String district = localizer.getLocalizedText("label.district", locale);
            columnDistrict = ReportUtility.createColumn("district", String.class, district, 30, headerStyle, detailTextStyle);
            String upazila = localizer.getLocalizedText("label.upazila", locale);
            columnUpazila = ReportUtility.createColumn("upazila", String.class, upazila, 30, headerStyle, detailTextStyle);
            String union = localizer.getLocalizedText("label.union", locale);
            columnUnion = ReportUtility.createColumn("union", String.class, union, 30, headerStyle, detailTextStyle);

            drb.addColumn(columnSerialNumber).addColumn(columnNID).addColumn(columnBenName).addColumn(columnFatherName).addColumn(columnSpouseName).addColumn(columnMobileNo).addColumn(columnPaymentProvider).addColumn(columnAccountNo);
            drb.addColumn(columnDivision).addColumn(columnDistrict).addColumn(columnUpazila).addColumn(columnUnion);
            String user = localizer.getLocalizedText("label.user", locale);
            columnUser = ReportUtility.createColumn("userId", String.class, user, 30, headerStyle, detailTextStyle);
            drb.addColumn(columnUser);

            Style style = ReportUtility.createUnionGroupStyle();
            GroupBuilder gb1 = new GroupBuilder();
            columnUser.setStyle(style);
            DJGroup dJGroup = gb1.setCriteriaColumn((PropertyColumn) columnUser)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .build();
            drb.addGroup(dJGroup);
        }

        StyleBuilder oddRowBgStyle = new StyleBuilder(true);
        oddRowBgStyle.setBackgroundColor(Color.darkGray);
        drb.setOddRowBackgroundStyle(oddRowBgStyle.build());

        if (reportParameterForm.getReportOrientation().equals("Portrait"))
        {
            drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
        }
        else
        {
            drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        }
        String dataEntryReportTitle = localizer.getLocalizedText("label.dataEntryReport", locale);
        drb.setTitle(dataEntryReportTitle);
        Style titleStyle = ReportUtility.createTitleStyle();
        drb.setTitleStyle(titleStyle);

        String subTitle = "";

        if (reportParameterForm.getScheme().getId() != null)
        {
            String schemeSt = localizer.getLocalizedText("label.scheme", locale);
            Scheme scheme = this.schemeService.getScheme(reportParameterForm.getScheme().getId());
            subTitle += schemeSt + " : " + (locale.toString().equals("bn") == true ? scheme.getNameInBangla() : scheme.getNameInEnglish());
        }
        if (reportParameterForm.getFiscalYear().getId() != null)
        {
            String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
            subTitle += "\\t\\t" + fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish());
        }

        subTitle += "\\n";
        drb.setSubtitle(subTitle);
        drb.setSubtitleStyle(subTitleStyle);
        drb.setLeftMargin(30);
        drb.setRightMargin(30);
        drb.setTopMargin(20);
        drb.setBottomMargin(20);
        if (locale.toString().equals("bn"))
        {
            drb.addImageBanner("image/banner_bn3.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }
        else
        {
            drb.addFirstPageImageBanner("image/banner_en3.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }
        drb.setIgnorePagination(false);
        drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
        drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
        drb.setUseFullPageWidth(true);
        drb.setAllowDetailSplit(false);

        String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
        drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle(), true, true);

        return drb.build();
    }

    private DynamicReport getDataEntrySummaryDynamicReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException
    {

        DynamicReportBuilder drb = new DynamicReportBuilder();

        Style titleStyle = ReportUtility.createTitleStyle();
        Style subTitleStyle = ReportUtility.createSubTitleStyle();
        Style headerStyle = ReportUtility.createHeaderStyle();
        Style detailTextStyle = ReportUtility.createDetailTextStyle();
        Style detailNumStyle = ReportUtility.createDetailNumberStyle();
        Locale locale = LocaleContextHolder.getLocale();
        String dataEntryReportSt = localizer.getLocalizedText("label.dataEntryReport", locale);
        String schemeSt = localizer.getLocalizedText("label.scheme", locale);
        String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
        String totalSt = localizer.getLocalizedText("label.total", locale);
        String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
        AbstractColumn columnCreatedTotal = null;

        AbstractColumn columnUpazila = null, columnDistrict = null, columnDivision = null, columnSerialNumber = null;
        AbstractColumn columnUser = null;

        if (!emptyData)
        {
            String division = localizer.getLocalizedText("label.division", locale);
            columnDivision = ReportUtility.createColumn("division", String.class, division, 30, headerStyle, detailTextStyle);
            String district = localizer.getLocalizedText("label.district", locale);
            columnDistrict = ReportUtility.createColumn("district", String.class, district, 30, headerStyle, detailTextStyle);
            String upazila = localizer.getLocalizedText("label.upazila", locale);
            columnUpazila = ReportUtility.createColumn("upazila", String.class, upazila, 30, headerStyle, detailTextStyle);
            String userID = localizer.getLocalizedText("label.userID", locale);
            columnUser = ReportUtility.createColumn("userID", String.class, userID, 30, headerStyle, detailTextStyle);
            drb.addColumn(columnDivision).addColumn(columnDistrict).addColumn(columnUpazila).addColumn(columnUser);
            String total = localizer.getLocalizedText("label.total", locale);
            columnCreatedTotal = ReportUtility.createColumn("userTotal", String.class, total, 30, headerStyle, detailNumStyle);
            drb.addColumn(columnCreatedTotal);
        }
        StyleBuilder oddRowBgStyle = new StyleBuilder(true);
        oddRowBgStyle.setBackgroundColor(Color.darkGray);
        drb.setOddRowBackgroundStyle(oddRowBgStyle.build());

        if (reportParameterForm.getReportOrientation().equals("Portrait"))
        {
            drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
        }
        else
        {
            drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        }
        drb.setTitle(dataEntryReportSt);
        drb.setTitleStyle(titleStyle);

        String subTitle = "";

        if (reportParameterForm.getScheme().getId() != null)
        {
            Scheme scheme = this.schemeService.getScheme(reportParameterForm.getScheme().getId());
            subTitle += schemeSt + " : " + (locale.toString().equals("bn") == true ? scheme.getNameInBangla() : scheme.getNameInEnglish());
        }
        if (reportParameterForm.getFiscalYear().getId() != null)
        {
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
            subTitle += "\\t " + fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish());
        }

        subTitle += "\\n";
        drb.setSubtitle(subTitle);
        drb.setSubtitleStyle(subTitleStyle);
        drb.setUseFullPageWidth(true);
        drb.setIgnorePagination(false);
        drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
        drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
        drb.setLeftMargin(30);
        drb.setRightMargin(30);
        drb.setTopMargin(20);
        drb.setBottomMargin(20);
        if (locale.toString().equals("bn"))
        {
            drb.addFirstPageImageBanner("image/banner_bn3.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }
        else
        {
            drb.addFirstPageImageBanner("image/banner_en3.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }
        Style style = ReportUtility.createUnionGroupStyle();
        GroupBuilder gb1 = new GroupBuilder();
        columnUser.setStyle(style);
        DJGroup dJGroup = gb1.setCriteriaColumn((PropertyColumn) columnUser)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                .build();
        drb.addGroup(dJGroup);
//        if (!emptyData)
//        {
//            drb.addField("userTotal", String.class.getName());
//            drb.addGlobalFooterVariable(columnCreatedTotal, new CustomExpression()
//            {
//
//                @Override
//                public Object evaluate(Map fields, Map variables, Map parameters)
//                {
//                    String a = (String) fields.get("userTotal");
//                    return a;
//                }
//
//                @Override
//                public String getClassName()
//                {
//                    return String.class.getName();
//                }
//            });
//            drb.setGrandTotalLegend(totalSt);
//        }
        drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle());
        return drb.build();
    }

}
