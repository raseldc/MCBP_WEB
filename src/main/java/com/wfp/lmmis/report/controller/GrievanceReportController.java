/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.controller;

import com.wfp.lmmis.report.data.GrievanceReportData;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.ImageBanner;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.wfp.lmmis.grievance.service.GrievanceService;
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
import ar.com.fdvs.dj.domain.constants.Page;
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DivisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.Localizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Philip
 */
@Controller
public class GrievanceReportController
{

    //private static final logger logger = //logger.getlogger(GrievanceReportController.class);

    @Autowired
    private GrievanceService grievanceService;
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

    @RequestMapping(value = "/grievanceReport")
    public String getGrievanceReport(Model model, HttpServletRequest request)
    {
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("reportParameterForm", CommonUtility.loadReportParameterForm(request));
        return "grievanceReport";
    }

    @RequestMapping(value = "/grievanceReport", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> getGrievanceReport(ReportParameterForm reportParameterForm, HttpServletResponse response, HttpSession session) throws IOException
    {
        try
        {
            Map parameter = new HashMap();
//            parameter.put("schemeId", reportParameterForm.getScheme().getId());
            Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
            reportParameterForm.setScheme(scheme);
            parameter.put("schemeId", scheme.getId());
            parameter.put("fiscalYearId", reportParameterForm.getFiscalYear().getId());
            parameter.put("divisionId", reportParameterForm.getDivision().getId());
            parameter.put("districtId", reportParameterForm.getDistrict().getId());
            parameter.put("upazilaId", reportParameterForm.getUpazila().getId());
            parameter.put("unionId", reportParameterForm.getUnion().getId());
            parameter.put("locale", LocaleContextHolder.getLocale());

            JasperPrint jp = null;
            switch (reportParameterForm.getReportGenerationType())
            {
                case "Details":
                {
                    List<GrievanceReportData> grievanceReportDataList = this.grievanceService.getGrievanceReportData(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(grievanceReportDataList);
                    jp = getReport(ds, reportParameterForm, grievanceReportDataList.isEmpty());
                    break;
                }
//                case "Summary":
//                {
//                    List<BeneficiaryReportDataByLocation> beneficiarySummaryReportDataList = this.beneficiaryService.getBeneficiarySummaryReportData(parameter);
//                    JRDataSource ds = new JRBeanCollectionDataSource(beneficiarySummaryReportDataList);
//                    jp = getReport(ds, reportParameterForm);
//                    break;
//                }
//                case "Group":
//                {
//                    List<BeneficiaryReportDataByLocation> beneficiaryGroupReportDataList = this.beneficiaryService.getBeneficiaryGroupReportData(parameter);
//                    JRDataSource ds = new JRBeanCollectionDataSource(beneficiaryGroupReportDataList);
//                    jp = getReport(ds, reportParameterForm);
//                    break;
//                }
                default:
                    break;
            }

            ResponseEntity<byte[]> responseEntity = null;
            if (reportParameterForm.getReportExportType().equals("pdf"))
            {
                byte[] contents = ReportUtility.getPDFContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
//                String filename = "Grievance_Report.pdf";
//                headers.add("content-disposition", "attachment; filename=" + filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            else if (reportParameterForm.getReportExportType().equals("html"))
            {
                byte[] contents = ReportUtility.getExcelContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                String fileName = "Grievance_Report.xls";
                headers.add("Content-Disposition", "inline; filename=" + fileName);
                headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            return responseEntity;
        }
        catch (ColumnBuilderException | ClassNotFoundException | JRException e)
        {
            //logger.infoer(e.getMessage());
            return null;
        }
    }

    public JasperPrint getReport(JRDataSource ds, ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, JRException, ClassNotFoundException
    {
        DynamicReport dynaReport = null;
        switch (reportParameterForm.getReportGenerationType())
        {
            case "Details":
                dynaReport = getGrievanceDetailDynamicReport(reportParameterForm, emptyData);
                break;
//            case "Summary":
//                dynaReport = getBenSummaryDynamicReport(reportParameterForm);
//                break;
//            case "Group":
//                dynaReport = getBenGroupDynamicReport(reportParameterForm);
//                break;
            default:
                break;
        }
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), ds);
        return jp;
    }

    private DynamicReport getGrievanceDetailDynamicReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException
    {
        DynamicReportBuilder report = new DynamicReportBuilder();

        Style titleStyle = ReportUtility.createTitleStyle();
        Style subTitleStyle = ReportUtility.createSubTitleStyle();
        Style headerStyle = ReportUtility.createHeaderStyle();
        Style detailTextStyle = ReportUtility.createDetailTextStyle();
        Style detailNumStyle = ReportUtility.createDetailNumberStyle();

        Locale locale = LocaleContextHolder.getLocale();

        AbstractColumn columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);
        String name = localizer.getLocalizedText("label.beneficiaryName", locale);
        AbstractColumn columnBenName = ReportUtility.createColumn("benName", String.class, name, 30, headerStyle, detailTextStyle);
        String benId = localizer.getLocalizedText("label.beneficiaryID", locale);
        AbstractColumn columnBenID = ReportUtility.createColumn("benID", String.class, benId, 30, headerStyle, detailTextStyle);
        String grievanceType = localizer.getLocalizedText("grievance.grievanceType", locale);
        AbstractColumn columnType = ReportUtility.createColumn("grievanceType", String.class, grievanceType, 30, headerStyle, detailTextStyle);
        String description = localizer.getLocalizedText("label.description", locale);
        AbstractColumn columnDescription = ReportUtility.createColumn("description", String.class, description, 30, headerStyle, detailTextStyle);
        String comment = localizer.getLocalizedText("label.comment", locale);
        AbstractColumn columnComments = ReportUtility.createColumn("comment", String.class, comment, 30, headerStyle, detailTextStyle);
        String status = localizer.getLocalizedText("label.status", locale);
        AbstractColumn columnStatus = ReportUtility.createColumn("status", String.class, status, 30, headerStyle, detailTextStyle);
        if (!emptyData)
        {
            report.addColumn(columnSerialNumber).addColumn(columnBenName).addColumn(columnBenID).addColumn(columnType).addColumn(columnDescription).addColumn(columnComments).addColumn(columnStatus);
            if (reportParameterForm.getDivision().getId() == null)
            {
                String division = localizer.getLocalizedText("label.division", locale);
                AbstractColumn columnDivision = ReportUtility.createColumn("division", String.class, division, 30, headerStyle, detailTextStyle);
                report.addColumn(columnDivision);

            }
            if (reportParameterForm.getDistrict().getId() == null)
            {
                String district = localizer.getLocalizedText("label.district", locale);
                AbstractColumn columnDistrict = ReportUtility.createColumn("district", String.class, district, 30, headerStyle, detailTextStyle);
                report.addColumn(columnDistrict);

            }
            if (reportParameterForm.getUpazila().getId() == null)
            {
                String upazila = localizer.getLocalizedText("label.upazila", locale);
                AbstractColumn columnUpazila = ReportUtility.createColumn("upazila", String.class, upazila, 30, headerStyle, detailTextStyle);
                report.addColumn(columnUpazila);

            }
            if (reportParameterForm.getUnion().getId() == null)
            {
                String union = localizer.getLocalizedText("label.union", locale);
                AbstractColumn columnUnion = ReportUtility.createColumn("union", String.class, union, 30, headerStyle, detailTextStyle);
                report.addColumn(columnUnion);
            }
        }

        if (reportParameterForm.getReportOrientation().equals("Portrait"))
        {
            report.setPageSizeAndOrientation(Page.Page_A4_Portrait());
        }
        else
        {
            report.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        }

        String grievanceReportTitle = localizer.getLocalizedText("label.grievanceReportTitle", locale);
        report.setTitle(grievanceReportTitle);
        report.setTitleStyle(titleStyle);

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
        if (reportParameterForm.getDivision().getId() != null)
        {
            String divisionSt = localizer.getLocalizedText("label.division", locale);
            Division division = this.divisionService.getDivision(reportParameterForm.getDivision().getId());
            subTitle += "\\n" + divisionSt + " : " + (locale.toString().equals("bn") == true ? division.getNameInBangla() : division.getNameInEnglish());
        }
        if (reportParameterForm.getDistrict().getId() != null)
        {
            String districtSt = localizer.getLocalizedText("label.district", locale);
            District district = this.districtService.getDistrict(reportParameterForm.getDistrict().getId());
            subTitle += "\\t\\t" + districtSt + " : " + (locale.toString().equals("bn") == true ? district.getNameInBangla() : district.getNameInEnglish());
        }
        if (reportParameterForm.getUpazila().getId() != null)
        {
            String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
            Upazilla upazila = this.upazilaService.getUpazilla(reportParameterForm.getUpazila().getId());
            subTitle += "\\t\\t" + upazilaSt + " : " + (locale.toString().equals("bn") == true ? upazila.getNameInBangla() : upazila.getNameInEnglish());
        }
        if (reportParameterForm.getUnion().getId() != null)
        {
            String unionSt = localizer.getLocalizedText("label.union", locale);
            Union union = this.unionService.getUnion(reportParameterForm.getUnion().getId());
            subTitle += "\\t\\t" + unionSt + " : " + (locale.toString().equals("bn") == true ? union.getNameInBangla() : union.getNameInEnglish());
        }
        subTitle += "\\n";

        report.setSubtitle(subTitle);
        report.setSubtitleStyle(subTitleStyle);
        report.setUseFullPageWidth(true);
        report.setIgnorePagination(false);
        report.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
        report.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
        report.setLeftMargin(30);
        report.setRightMargin(30);
        report.setTopMargin(20);
        report.setBottomMargin(20);
        if (locale.toString().equals("bn"))
        {
            report.addFirstPageImageBanner("image/banner_bn3.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }
        else
        {
            report.addFirstPageImageBanner("image/banner_en3.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }
        String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
        report.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle());
        return report.build();
    }

}
