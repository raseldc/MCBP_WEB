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
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
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
import ar.com.fdvs.dj.domain.constants.Page;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.UserType;
import static com.wfp.lmmis.enums.UserType.BKMEA;
import static com.wfp.lmmis.enums.UserType.DIRECTORATE;
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.report.data.DoubleDippingReportData;
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

/**
 *
 * @author Philip
 */
@Controller
public class DoubleDippingReportController
{

    //private static final logger logger = //logger.getlogger(DoubleDippingReportController.class);

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

    @RequestMapping(value = "/doubleDippingFoundReport")
    public String getDoubleDippingReport(Model model, HttpServletRequest request)
    {
        ReportParameterForm reportParameterForm = CommonUtility.loadReportParameterForm(request);
        String defaultSchemeShortName = ((UserDetail) request.getSession().getAttribute("userDetail")).getScheme().getShortName();
        if ("LMA".equals(defaultSchemeShortName))
        {
            UserType userType = ((UserDetail) request.getSession().getAttribute("userDetail")).getUserType();
            if (null != userType)
            {
                switch (userType)
                {
                    case DIRECTORATE:
                    case MINISTRY:
                        CommonUtility.mapDivisionName(model);
                        reportParameterForm.setApplicantType(ApplicantType.REGULAR);
                        CommonUtility.mapBGMEAFactoryName(model);
                        CommonUtility.mapBKMEAFactoryName(model);
                        break;
                    case BGMEA:
                        reportParameterForm.setApplicantType(ApplicantType.BGMEA);
                        CommonUtility.mapBGMEAFactoryName(model);
                        break;
                    case BKMEA:
                        reportParameterForm.setApplicantType(ApplicantType.BKMEA);
                        CommonUtility.mapBKMEAFactoryName(model);
                        break;
                    case FIELD:
                        CommonUtility.mapDivisionName(model);
                        reportParameterForm.setApplicantType(ApplicantType.REGULAR);
                        break;
                }
            }
        }
        else
        {
            CommonUtility.mapDivisionName(model);
            reportParameterForm.setApplicantType(ApplicantType.REGULAR);
        }
//        CommonUtility.mapSchemeName(model);
//        CommonUtility.mapDivisionName(model);
        CommonUtility.mapAllFiscalYearName(model);
        model.addAttribute("reportParameterForm", reportParameterForm);
        return "doubleDippingReport";
    }

    /**
     *
     * @param reportParameterForm
     * @param response
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/doubleDippingFoundReport", method = RequestMethod.POST)
    // @ResponseBody
    public ResponseEntity<byte[]> getDoubleDippingReport(ReportParameterForm reportParameterForm, HttpServletResponse response, HttpSession session) throws IOException
    {
        try
        {
            Map parameter = new HashMap();
            Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
            reportParameterForm.setScheme(scheme);
            parameter.put("schemeId", scheme.getId());
            parameter.put("fiscalYearId", reportParameterForm.getFiscalYear().getId());
            parameter.put("divisionId", reportParameterForm.getDivision().getId());
            parameter.put("districtId", reportParameterForm.getDistrict().getId());
            parameter.put("upazilaId", reportParameterForm.getUpazila().getId());
            parameter.put("unionId", reportParameterForm.getUnion().getId());
            parameter.put("locale", reportParameterForm.getLanguage());
            System.out.println("reportParameterForm.getApplicantType() = " + reportParameterForm.getApplicantType());
            parameter.put("applicantType", reportParameterForm.getApplicantType());
            parameter.put("bgmeaId", reportParameterForm.getBgmeaFactory() != null ? reportParameterForm.getBgmeaFactory().getId() : null);
            parameter.put("bkmeaId", reportParameterForm.getBkmeaFactory() != null ? reportParameterForm.getBkmeaFactory().getId() : null);

            JasperPrint jp = null;
            switch (reportParameterForm.getReportGenerationType())
            {
                case "Details":
                {
                    List<DoubleDippingReportData> applicantList = this.applicantService.getDoubleDippingFoundApplicants(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(applicantList);
                    jp = getReport(ds, reportParameterForm, applicantList.isEmpty());
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
//            String reportDataAsString = Base64.encodeBase64String(ReportUtility.getPDFContents(jp, response));
//            return reportDataAsString;
                byte[] contents = ReportUtility.getPDFContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
//                String filename = "Double_Dipping_Report.pdf";
                // headers.add("content-disposition", "attachment; filename=" + filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            else if (reportParameterForm.getReportExportType().equals("html"))
            {
                byte[] contents = ReportUtility.getExcelContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                String fileName = "Double_Dipping_Report.xls";
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

//    @RequestMapping(value = "/downloadExcel")
//    public void getReportInExcel(HttpServletResponse response) throws ColumnBuilderException, ClassNotFoundException, JRException
//    {
//        try
//        {
//            Map parameter = new HashMap();
//            List<GrievanceReportData> grievanceReportDataList = this.grievanceService.getGrievanceReportData(parameter);
//            JRDataSource ds = new JRBeanCollectionDataSource(grievanceReportDataList);
//            JasperPrint jp = getReport(ds);
//            ReportUtility reportUtility = new ReportUtility();
//            reportUtility.downloadXLS(jp, response);
//        }
//        catch (ColumnBuilderException | IOException | ClassNotFoundException | JRException e)
//        {
//            //logger.infoer(e.getMessage());
//        }
//    }
    public JasperPrint getReport(JRDataSource ds, ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, JRException, ClassNotFoundException
    {
        DynamicReport dynaReport = null;
        switch (reportParameterForm.getReportGenerationType())
        {
            case "Details":
                dynaReport = getDoubleDippingFoundDetailDynamicReport(reportParameterForm, emptyData);
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

    private DynamicReport getDoubleDippingFoundDetailDynamicReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException
    {

        DynamicReportBuilder drb = new DynamicReportBuilder();

        Style titleStyle = ReportUtility.createTitleStyle();
        Style subTitleStyle = ReportUtility.createSubTitleStyle();
        Style headerStyle = ReportUtility.createHeaderStyle();
        Style markedHeaderStyle = ReportUtility.createMarkedHeaderStyle();
        Style detailTextStyle = ReportUtility.createDetailTextStyle();
        Style detailNumStyle = ReportUtility.createDetailNumberStyle();
        
        Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage()).build();
        AbstractColumn columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);

        if (!emptyData)
        {
            String name = localizer.getLocalizedText("label.name", locale);
            AbstractColumn columnBenName = ReportUtility.createColumn("benName", String.class, name, 30, headerStyle, detailTextStyle);
            String nid = localizer.getLocalizedText("label.nid", locale);
            AbstractColumn columnNID = ReportUtility.createColumn("nationalID", String.class, nid, 40, headerStyle, detailTextStyle);
            String fatherName = localizer.getLocalizedText("label.fatherName", locale);
            AbstractColumn columnFatherName = ReportUtility.createColumn("fatherName", String.class, fatherName, 30, headerStyle, detailTextStyle);
            String motherName = localizer.getLocalizedText("label.motherName", locale);
            AbstractColumn columnMotherName = ReportUtility.createColumn("motherName", String.class, motherName, 30, headerStyle, detailTextStyle);
            String spouseName = localizer.getLocalizedText("label.spouseName", locale);
            AbstractColumn columnSpouseName = ReportUtility.createColumn("spouseName", String.class, spouseName, 30, headerStyle, detailTextStyle);
            String mobileNo = localizer.getLocalizedText("label.mobileNo", locale);
            AbstractColumn columnMobileNo = ReportUtility.createColumn("mobileNo", String.class, mobileNo, 30, headerStyle, detailTextStyle);

            String ministry = localizer.getLocalizedText("label.ministry", locale);
            AbstractColumn columnMinistry = ReportUtility.createColumn("ddMatchedMinistryName", String.class, ministry, 30, markedHeaderStyle, detailTextStyle);
            String scheme = localizer.getLocalizedText("label.scheme", locale);
            AbstractColumn columnScheme = ReportUtility.createColumn("ddMatchedSchemeName", String.class, scheme, 30, markedHeaderStyle, detailTextStyle);

            drb.addColumn(columnSerialNumber).addColumn(columnBenName).addColumn(columnNID).addColumn(columnFatherName).addColumn(columnMotherName).addColumn(columnSpouseName).addColumn(columnMobileNo);

            if (reportParameterForm.getDivision().getId() == null)
            {
                String division = localizer.getLocalizedText("label.division", locale);
                AbstractColumn columnDivision = ReportUtility.createColumn("division", String.class, division, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnDivision);

            }
            if (reportParameterForm.getDistrict().getId() == null)
            {
                String district = localizer.getLocalizedText("label.district", locale);
                AbstractColumn columnDistrict = ReportUtility.createColumn("district", String.class, district, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnDistrict);

            }
            if (reportParameterForm.getUpazila().getId() == null)
            {
                String upazila = localizer.getLocalizedText("label.upazila", locale);
                AbstractColumn columnUpazila = ReportUtility.createColumn("upazila", String.class, upazila, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnUpazila);

            }
            if (reportParameterForm.getUnion().getId() == null)
            {
                String union = localizer.getLocalizedText("label.union", locale);
                AbstractColumn columnUnion = ReportUtility.createColumn("union", String.class, union, 30, headerStyle, detailTextStyle);
                drb.addColumn(columnUnion);
            }
            drb.addColumn(columnMinistry).addColumn(columnScheme);

        }

        if (reportParameterForm.getReportOrientation().equals("Portrait"))
        {
            drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
        }
        else
        {
            drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        }

        String beneficiaryReport = localizer.getLocalizedText("label.doubleDippingReport", locale);
        drb.setTitle(beneficiaryReport);
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
        drb.setSubtitle(subTitle);
        drb.setSubtitleStyle(subTitleStyle);
        drb.setUseFullPageWidth(true);
        drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 60, 60);
        drb.addAutoText(AutoText.AUTOTEXT_CREATED_ON, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_LEFT, AutoText.PATTERN_DATE_DATE_TIME, 200, 200);
        drb.setLeftMargin(30);
        drb.setRightMargin(30);
        drb.setTopMargin(20);
        drb.setBottomMargin(20);
        if (locale.toString().equals("bn"))
        {
            drb.addFirstPageImageBanner("image/banner_bn.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }
        else
        {
            drb.addFirstPageImageBanner("image/banner_en.png", 406, 55, ImageBanner.ALIGN_CENTER);
        }

        String noDataFoundforthisreportSt = localizer.getLocalizedText("label.noDataFoundforthisreport", locale);
        drb.setWhenNoData(noDataFoundforthisreportSt, ReportUtility.noDataStyle(), true, true);
        drb.setIgnorePagination(false);
        return drb.build();
    }

}
