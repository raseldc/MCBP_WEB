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
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.training.model.Training;
import com.wfp.lmmis.training.service.TrainingService;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.DateUtilities;
import com.wfp.lmmis.utility.Localizer;
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
 * @author Philip
 */
@Controller
public class TrainingReportController
{

    //private static final logger logger = //logger.getlogger(TrainingReportController.class);

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private FiscalYearService fiscalYearService;
    @Autowired
    private DivisionService divisionService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private UpazillaService upazilaService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/trainingReport")
    public String getTrainingReport(Model model, HttpServletRequest request)
    {
        CommonUtility.mapAllFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("dateFormat", "yy-mm-dd");
        model.addAttribute("reportParameterForm", CommonUtility.loadReportParameterForm(request));
        return "trainingReport";
    }

    @RequestMapping(value = "/trainingReport", method = RequestMethod.POST)
//    @ResponseBody
    public ResponseEntity<byte[]> getTrainingReport(ReportParameterForm reportParameterForm, HttpServletResponse response, HttpSession session) throws IOException
    {
        try
        {
            Map parameter = new HashMap();
            parameter.put("fiscalYearId", reportParameterForm.getFiscalYear().getId());
            parameter.put("divisionId", reportParameterForm.getDivision().getId());
            parameter.put("districtId", reportParameterForm.getDistrict().getId());
            parameter.put("upazilaId", reportParameterForm.getUpazila().getId());
            parameter.put("locale", reportParameterForm.getLanguage());

            Calendar startDate = null, endDate = null;
            if (reportParameterForm.getStartDate() != null && reportParameterForm.getStartDate() != "")
            {
                startDate = DateUtilities.stringToCalendar(reportParameterForm.getStartDate());
            }
            if (reportParameterForm.getEndDate() != null && reportParameterForm.getEndDate() != "")
            {
                endDate = DateUtilities.stringToCalendar(reportParameterForm.getEndDate());
            }
            parameter.put("startDate", startDate);
            parameter.put("endDate", endDate);

            JasperPrint jp = null;
            switch (reportParameterForm.getReportGenerationType())
            {
                case "Details":
                {
                    List<Training> trainingReportDataList = this.trainingService.getTrainingReportData(parameter);
                    JRDataSource ds = new JRBeanCollectionDataSource(trainingReportDataList);
                    jp = getReport(ds, reportParameterForm, trainingReportDataList.isEmpty());
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
//                String filename = "Training_Report.pdf";
//                headers.add("content-disposition", "attachment; filename=" + filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            else if (reportParameterForm.getReportExportType().equals("excel"))
            {
                byte[] contents = ReportUtility.getExcelContents(jp, response);
                HttpHeaders headers = new HttpHeaders();
                String fileName = "Training_Report.xls";
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
        catch(Exception e){
            e.printStackTrace();
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
                dynaReport = getTrainingDetailDynamicReport(reportParameterForm, emptyData);
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

    private DynamicReport getTrainingDetailDynamicReport(ReportParameterForm reportParameterForm, boolean emptyData) throws ColumnBuilderException, ClassNotFoundException
    {
        try
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        DynamicReportBuilder report = new DynamicReportBuilder();

        Style titleStyle = ReportUtility.createTitleStyle();
        Style subTitleStyle = ReportUtility.createSubTitleStyle();
        Style headerStyle = ReportUtility.createHeaderStyle();
        Style detailTextStyle = ReportUtility.createDetailTextStyle();
        Style detailNumStyle = ReportUtility.createDetailNumberStyle();
        Style detailAmountStyle = ReportUtility.createDetailAmountStyle();

        Locale locale = new Locale.Builder().setLanguage(reportParameterForm.getLanguage()).build();

        String trainingType = localizer.getLocalizedText("label.trainingType", locale);
        String organization = localizer.getLocalizedText("label.organization", locale);
        String trainer = localizer.getLocalizedText("label.trainer", locale);
        String numOfParticipants = localizer.getLocalizedText("label.numOfParticipants", locale);
        String trainingCost = localizer.getLocalizedText("label.trainingCost", locale);

        AbstractColumn columnSerialNumber = ReportUtility.createSerialColumn(headerStyle, detailNumStyle, locale);
        if (!emptyData)
        {
            if ("en".equals(locale.getLanguage()))
            {
                AbstractColumn columnTrainingType = ReportUtility.createColumn("trainingType.nameInEnglish", String.class, trainingType, 30, headerStyle, detailTextStyle);
                AbstractColumn columnOrganization = ReportUtility.createColumn("trainer.nameInEnglish", String.class, organization, 30, headerStyle, detailTextStyle);
                AbstractColumn columnTrainer = ReportUtility.createColumn("trainer.contactPersonName", String.class, trainer, 30, headerStyle, detailTextStyle);
                AbstractColumn columnParticipants = ReportUtility.createColumn("numberOfPerticipants", Integer.class, numOfParticipants, 30, headerStyle, detailNumStyle);
                AbstractColumn columnCost = ReportUtility.createColumn("trainingCost", Float.class, trainingCost, 30, headerStyle, detailAmountStyle);
                report.addColumn(columnSerialNumber)
                        .addColumn(columnTrainingType)
                        .addColumn(columnOrganization)
                        .addColumn(columnTrainer)
                        //.addColumn(columnLocation)
                        .addColumn(columnParticipants)
                        .addColumn(columnCost);
            }
            else
            {
                AbstractColumn columnTrainingType = ReportUtility.createColumn("trainingType.nameInBangla", String.class, trainingType, 30, headerStyle, detailTextStyle);
                AbstractColumn columnOrganization = ReportUtility.createColumn("trainer.nameInBangla", String.class, organization, 30, headerStyle, detailTextStyle);
                AbstractColumn columnTrainer = ReportUtility.createColumn("trainer.contactPersonName", String.class, trainer, 30, headerStyle, detailTextStyle);
                AbstractColumn columnParticipants = ReportUtility.createColumn("numberOfPerticipants", Integer.class, numOfParticipants, 30, headerStyle, detailNumStyle);
                AbstractColumn columnCost = ReportUtility.createColumn("trainingCost", Float.class, trainingCost, 30, headerStyle, detailAmountStyle);
                report.addColumn(columnSerialNumber)
                        .addColumn(columnTrainingType)
                        .addColumn(columnOrganization)
                        .addColumn(columnTrainer)
                        //.addColumn(columnLocation)
                        .addColumn(columnParticipants)
                        .addColumn(columnCost);
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
        String trainingReportTitle = localizer.getLocalizedText("label.trainingReportTitle", locale);
        report.setTitle(trainingReportTitle);
        report.setTitleStyle(titleStyle);

        String subTitle = "";

        
        if (reportParameterForm.getFiscalYear().getId() != null)
        {
            String fiscalYearSt = localizer.getLocalizedText("label.fiscalYear", locale);
            FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(reportParameterForm.getFiscalYear().getId());
            subTitle += fiscalYearSt + " : " + (locale.toString().equals("bn") == true ? fiscalYear.getNameInBangla() : fiscalYear.getNameInEnglish());
        }
        if(reportParameterForm.getStartDate() != null)
        {
            subTitle += locale.toString().equals("bn") == true ? CommonUtility.getNumberInBangla("("+reportParameterForm.getStartDate().replace("-", "/")): "("+reportParameterForm.getStartDate().replace("-", "/");
        }
        if(reportParameterForm.getEndDate() != null)
        {
            subTitle += "-"+(locale.toString().equals("bn") == true ? CommonUtility.getNumberInBangla(reportParameterForm.getEndDate().replace("-", "/")): "("+reportParameterForm.getEndDate().replace("-", "/"))+")";
        }
        if (reportParameterForm.getDivision() != null && reportParameterForm.getDivision().getId() != null)
        {
            String divisionSt = localizer.getLocalizedText("label.division", locale);
            Division division = this.divisionService.getDivision(reportParameterForm.getDivision().getId());
            subTitle += "\\n" + divisionSt + " : " + (locale.toString().equals("bn") == true ? division.getNameInBangla() : division.getNameInEnglish());
        }
        if (reportParameterForm.getDistrict() != null && reportParameterForm.getDistrict().getId() != null)
        {
            String districtSt = localizer.getLocalizedText("label.district", locale);
            District district = this.districtService.getDistrict(reportParameterForm.getDistrict().getId());
            subTitle += "\\t\\t" + districtSt + " : " + (locale.toString().equals("bn") == true ? district.getNameInBangla() : district.getNameInEnglish());
        }
        if (reportParameterForm.getUpazila() != null && reportParameterForm.getUpazila().getId() != null)
        {
            String upazilaSt = localizer.getLocalizedText("label.upazila", locale);
            Upazilla upazila = this.upazilaService.getUpazilla(reportParameterForm.getUpazila().getId());
            subTitle += "\\t\\t" + upazilaSt + " : " + (locale.toString().equals("bn") == true ? upazila.getNameInBangla() : upazila.getNameInEnglish());
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
        //report.addGlobalFooterVariable(columnParticipants, DJCalculation.SUM);
        //report.addGlobalFooterVariable(columnCost, DJCalculation.SUM);
        //report.setGrandTotalLegend("TOTAL");
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
