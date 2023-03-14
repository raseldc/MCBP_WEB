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
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.BenQuotaService;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.report.controller.ReportUtility;
import com.wfp.lmmis.report.data.ApplicantReportData;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
public class UpazilaMemberVerificationController
{

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
    private SchemeService schemeService;
    @Autowired
    BeneficiaryController beneficiaryController;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/upazilaMemberVerification", method = RequestMethod.GET)
    public String getAuditingPage(Model model, HttpServletRequest request, HttpSession session)
    {
        CommonUtility.mapDivisionName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        model.addAttribute("quota", "1");
        return "upazilaMemberVerification";
    }

    @RequestMapping(value = "/upazilaMemberVerification/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationUnionVerification(HttpServletRequest request, HttpServletResponse response)
    {
        //Fetch the page number from client
        System.out.println("in upazilaMemberVerification pagination controller");
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));

        String divisionId = request.getParameter("divisionId");
        String districtId = request.getParameter("districtId");
        String upazilaId = request.getParameter("upazilaId");
        String unionId = request.getParameter("unionId");

        Map parameter = new HashMap();
        //parameter.put("schemeId", Integer.valueOf(request.getParameter("scheme")));            
        parameter.put("schemeId", ((UserDetail) request.getSession().getAttribute("userDetail")).getSchemeId());
        parameter.put("fiscalYearId", fiscalYearService.getFiscalYearList(true, true).get(0).getId());
        parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
        parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
        parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
        parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
        parameter.put("regularUser", 1);// as this is for regular users of lma
        parameter.put("bgmeaUser", 0);
        parameter.put("bkmeaUser", 0);
        System.out.println("Division = " + divisionId + " District = " + districtId + " Upazila = " + upazilaId + " Union = " + unionId);

        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
        applicationStatusList.add(ApplicationStatus.UPAZILA_MEMBER_LEVEL_VERIFICATION);
        parameter.put("applicationStatusList", applicationStatusList);
//        applicationStatusList.add(ApplicationStatus.AUDITING_PENDING_WITHOUT_NID);
//        applicationStatusList.add(ApplicationStatus.REJECTED_PRIORITIZATION);
        List<Object> resultList = applicantService.getApplicantListBySearchParameter(parameter, beginIndex, pageSize);
//        List<Object> resultList = applicantService.getApplicantSearchListNew(null, false,
//                null, false, null, false, applicationStatusList, true, divisionId, divisionId != null, districtId, districtId != null, upazilaId, upazilaId != null, unionId, unionId != null, beginIndex, pageSize);
        List<ApplicantView> applicantList = (List<ApplicantView>) resultList.get(0);
        long recordsTotal = (Long) resultList.get(1);
        long recordsFiltered = (Long) resultList.get(2);
        System.out.println("recordsTotal = " + recordsTotal);
        for (ApplicantView applicant : applicantList)
        {
            JSONArray ja = new JSONArray();
            ja.put("<td><input class=\"checkboxAudit\" id=\"" + applicant.getId() + "\" type=\"checkbox\"></td>");
//            if (applicant.getApplicationStatus() == ApplicationStatus.AUDITING_PENDING_WITH_NID || applicant.getApplicationStatus() == ApplicationStatus.AUDITING_PENDING_WITHOUT_NID)
//            {
//                ja.put("<td><input class=\"checkbox\" id=\"" + applicant.getId() + "\" type=\"checkbox\" checked=\"checked\" disabled=\"true\" ></td>");
//            }
//            else
//            {
//                ja.put("<td><input class=\"checkbox\" id=\"" + applicant.getId() + "\" type=\"checkbox\" disabled=\"true\"></td>");
//            }
//            ja.put(applicant.getApplicationID());
            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage()))
            {
//                ja.put((applicant.getFirstNameInEnglish() == null ? ' ' : applicant.getFirstNameInEnglish()) + " " + (applicant.getMiddleNameInEnglish() == null ? ' ' : applicant.getMiddleNameInEnglish()) + " " + (applicant.getLastNameInEnglish() == null ? ' ' : applicant.getLastNameInEnglish()));
                ja.put(applicant.getFullNameInEnglish());
            }
            else
            {
//                ja.put((applicant.getFirstNameInBangla() == null ? ' ' : applicant.getFirstNameInBangla()) + " " + (applicant.getMiddleNameInBangla() == null ? ' ' : applicant.getMiddleNameInBangla()) + " " + (applicant.getLastNameInBangla() == null ? ' ' : applicant.getLastNameInBangla()));
                ja.put(applicant.getFullNameInBangla());
            }
            ja.put(applicant.getNid().toString());
            ja.put(applicant.getMobileNo() != null ? applicant.getMobileNo().toString() : "");
            if ("en".equals(locale.getLanguage()))
            {
                ja.put(CalendarUtility.getDateString(applicant.getDateOfBirth()));
            }
            else
            {
                ja.put(CommonUtility.getNumberInBangla(CalendarUtility.getDateString(applicant.getDateOfBirth())));
            }
            if (applicant.getPresentAddressLine2() != null)
            {
                ja.put(applicant.getPresentAddressLine1() + " " + applicant.getPresentAddressLine2());
            }
            else
            {
                ja.put(applicant.getPresentAddressLine1());
            }
            ja.put("<a href=\"applicant/viewApplicant/" + applicant.getId() + "\" data-toggle=\"modal\" >\n"
                    + "                                                <span class=\"glyphicon glyphicon-eye-open\"></span> \n"
                    + "                                            </a>");
            dataArray.put(ja);
        }
        try
        {
            jsonResult.put("recordsTotal", recordsTotal);
            jsonResult.put("recordsFiltered", recordsFiltered);
            jsonResult.put("data", dataArray);
            jsonResult.put("draw", draw);
        }
        catch (JSONException ex)
        {

        }

        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter out;
        try
        {
            out = response.getWriter();
            out.print(jsonResult);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/upazilaMemberVerification", method = RequestMethod.POST)
    public String changeUnionVerificationStatus(Model model, HttpSession session,
            HttpServletRequest request, RedirectAttributes redirectAttributes)
    {
        String unionId = request.getParameter("unionId1");
        Map parameter = new HashMap();
        parameter.put("schemeId", ((UserDetail) request.getSession().getAttribute("userDetail")).getSchemeId());
        parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
        parameter.put("applicantType", ApplicantType.REGULAR);
        parameter.put("beforeApplicationStatus", ApplicationStatus.UPAZILA_MEMBER_LEVEL_VERIFICATION);
        parameter.put("afterApplicationStatus", ApplicationStatus.UPAZILA_PRESIDENT_LEVEL_VERIFICATION);
        String selectedApplicantList = applicantService.updateApplicantStatus(parameter);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("approveUpazilaMemberVerification", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        redirectAttributes.addFlashAttribute("selectedApplicantListPrint", selectedApplicantList);
        return "redirect:/upazilaMemberVerification";
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
    @RequestMapping(value = "/upazilaMemberVerificationReport", method = RequestMethod.POST)
    public ResponseEntity<byte[]> unionVerificationReport(Model model, HttpSession session,
            HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) throws ColumnBuilderException, ClassNotFoundException, JRException, IOException
    {
        String selectedApplicantList = request.getParameter("selectedApplicantListPrint");
        if (!"".equals(selectedApplicantList))
        {
            //approved (selected only) applicants status update here
            Map parameter = new HashMap();
            parameter.put("selectedApplicantList", selectedApplicantList);
            parameter.put("locale", LocaleContextHolder.getLocale());
            List<ApplicantReportData> applicantReportDataList = this.applicantService.getApplicantReportDataByIds(parameter);
            JRDataSource ds = new JRBeanCollectionDataSource(applicantReportDataList);
            JasperPrint jp = null;
            jp = getReport(ds, applicantReportDataList.get(0));
            ResponseEntity<byte[]> responseEntity = null;
            byte[] contents = ReportUtility.getPDFContents(jp, response);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            responseEntity = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            return responseEntity;
        }
        return null;
    }

    /**
     *
     * @param ds
     * @param applicantReportData
     * @return
     * @throws ColumnBuilderException
     * @throws JRException
     * @throws ClassNotFoundException
     */
    public JasperPrint getReport(JRDataSource ds, ApplicantReportData applicantReportData) throws ColumnBuilderException, JRException, ClassNotFoundException
    {
        DynamicReport dynaReport = null;
        dynaReport = getApplicantDetailDynamicReport(applicantReportData);
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), ds);
        return jp;
    }

    private DynamicReport getApplicantDetailDynamicReport(ApplicantReportData applicantReportData) throws ColumnBuilderException, ClassNotFoundException
    {
        try
        {
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

            if (locale.toString().equals("bn"))
            {
                drb.addFirstPageImageBanner("image/banner_bn3.png", 406, 55, ImageBanner.ALIGN_CENTER);
            }
            else
            {
                drb.addFirstPageImageBanner("image/banner_en3.png", 406, 55, ImageBanner.ALIGN_CENTER);
            }
            drb.setWhenNoData("No Data Found for this report", ReportUtility.noDataStyle());
            return drb.build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

//    @RequestMapping(value = "/upazilaMemberVerification", method = RequestMethod.POST)
//    public String changeAuditingStatus(Model model, HttpSession session,
//            HttpServletRequest request, RedirectAttributes redirectAttributes)
//    {
//        String divisionId = request.getParameter("divisionId");
//        String districtId = request.getParameter("districtId");
//        String upazilaId = request.getParameter("upazilaId");
//        String unionId = request.getParameter("unionId");
//        System.out.println("union is " + divisionId);
//        System.out.println("union is " + districtId);
//        System.out.println("union is " + upazilaId);
//        System.out.println("union is " + unionId);
//        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
//        applicationStatusList.add(ApplicationStatus.UNION_LEVEL_VERIFICATION);
//        List<Applicant> resultList = applicantService.getApplicantSearchListClientSide(null, false,
//                null, false, null, false, applicationStatusList, true, divisionId, divisionId != null, districtId,
//                districtId != null, upazilaId, upazilaId != null, unionId, unionId != null);
//        for (Applicant applicant : resultList)
//        {
//            applicant.setApplicationStatus(ApplicationStatus.UPAZILA_MEMBER_LEVEL_VERIFICATION);
//            applicant.setModifiedBy((User) session.getAttribute("user"));
//            applicant.setModificationDate(Calendar.getInstance());
//            applicantService.edit(applicant);
//        }
//        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, ApplicationConstants.EDIT_MESSAGE);
//        redirectAttributes.addFlashAttribute("message", controllerMessage);
//        return "redirect:/upazilaMemberVerification";
//    }
}
