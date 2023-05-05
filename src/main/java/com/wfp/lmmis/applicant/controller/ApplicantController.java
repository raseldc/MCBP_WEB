package com.wfp.lmmis.applicant.controller;

import com.wfp.lmmis.applicant.editor.SchemeAttributeEditor;
import com.wfp.lmmis.applicant.forms.ApplicantForm;
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantAncInformation;
import com.wfp.lmmis.applicant.model.ApplicantAttachment;
import com.wfp.lmmis.applicant.model.ApplicantBiometricInfo;
import com.wfp.lmmis.applicant.model.ApplicantSocioEconomicInfo;
import com.wfp.lmmis.applicant.model.ApplicantView;
import com.wfp.lmmis.applicant.service.ApplicantAncInformationService;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.AttachmentType;
import com.wfp.lmmis.enums.ConceptionTerm;
import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import com.wfp.lmmis.masterdata.service.AccountTypeService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.VillageService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.selection.model.SelectionComments;
import com.wfp.lmmis.selection.service.SelectionCommentsService;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.types.Gender;
import com.wfp.lmmis.types.SystemRecommendedStatus;
import com.wfp.lmmis.utility.AgeCalculator;
import com.wfp.lmmis.utility.ApplicationConstants;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.DateUtilities;
import com.wfp.lmmis.utility.Localizer;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Scope("request")
public class ApplicantController {

    //  private final logger logger = //logger.getlogger(ApplicantController.class);
    public static final String FILE_CREATION_PATH = "D:/files/";// Windows
//    public static final String FILE_CREATION_PATH = "/home/alamin/imlma_files";// Linux

    @Autowired
    private AccountTypeService accountTypeService;
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private SelectionCommentsService selectionCommentsService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private FiscalYearService fiscalYearService;

    @Autowired
    private VillageService villageService;
    @Autowired
    private ApplicantAncInformationService applicantAncInformationService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(SchemeAttribute.class, new SchemeAttributeEditor());
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/applicant/list", method = RequestMethod.GET)
    public String showApplicantList(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapSchemeName(model);
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
//            String defaultSchemeShortName = ((UserDetail) request.getSession().getAttribute("userDetail")).getScheme().getShortName();

            UserType userType = ((UserDetail) request.getSession().getAttribute("userDetail")).getUserType();
            if (null != userType) {
                switch (userType) {
                    case DIRECTORATE:
                    case MINISTRY:
                        CommonUtility.mapBGMEAFactoryName(model);
                        CommonUtility.mapBKMEAFactoryName(model);
                        if (searchParameterForm.getDivision() == null) {
                            CommonUtility.mapDivisionName(model);
                        }
                        break;
                    case FIELD:
                        if (searchParameterForm.getDivision() == null) {
                            CommonUtility.mapDivisionName(model);
                        }
                        break;
                    case BGMEA:
                        CommonUtility.mapBGMEAFactoryName(model);
                        break;
                    case BKMEA:
                        CommonUtility.mapBKMEAFactoryName(model);
                        break;
                    default:
                        break;
                }
            }
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.infoer(e.getMessage());
        }
        return "applicantList";
    }

    @RequestMapping(value = "/applicant/union/list", method = RequestMethod.GET)
    public String getRuralApplicantList(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.UNION);
            if (searchParameterForm.getDivision() == null) {
                CommonUtility.mapDivisionName(model);

            }
            CommonUtility.getWardNoList(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
            model.addAttribute("applicationStatus", ApplicationStatus.fromValuesToMapForApplicationUpdate("union"));
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "applicantListUnion";
    }

    @RequestMapping(value = "/applicant/municipal/list", method = RequestMethod.GET)
    public String getMunicipalApplicantList(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.MUNICIPAL);
            if (searchParameterForm.getDivision() == null) {
                CommonUtility.mapDivisionName(model);
            }
            CommonUtility.getWardNoList(model);
            model.addAttribute("applicationStatus", ApplicationStatus.fromValuesToMapForApplicationUpdate("municipal"));
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "applicantListMunicipal";
    }

    @RequestMapping(value = "/applicant/cityCorporation/list", method = RequestMethod.GET)
    public String getCityCorporationApplicantList(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.CITYCORPORATION);
            if (searchParameterForm.getDivision() == null) {
                CommonUtility.mapDivisionName(model);
            }
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "applicantListCityCorporation";
    }

    @RequestMapping(value = "/applicant/bgmea/list", method = RequestMethod.GET)
    public String getBGMEAApplicantList(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BGMEA);
            CommonUtility.mapBGMEAFactoryName(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
            model.addAttribute("applicationStatus", ApplicationStatus.fromValuesToMapForApplicationUpdate("bgmea"));
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "applicantListBgmea";
    }

    @RequestMapping(value = "/applicant/bkmea/list", method = RequestMethod.GET)
    public String getBKMEAApplicantList(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BKMEA);
            CommonUtility.mapBKMEAFactoryName(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
            model.addAttribute("applicationStatus", ApplicationStatus.fromValuesToMapForApplicationUpdate("bkmea"));
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "applicantListBkmea";
    }

    @RequestMapping(value = "/applicant/list", method = RequestMethod.POST)
    @ResponseBody
    public void showApplicantList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {

            String divisionId = null, districtId = null, upazilaId = null, unionId = null, wardNo = null, bgmeaFactoryId = null, bkmeaFactoryId = null, nid = null, applicationStatus = null;
            int regularUser = 0, bgmeaUser = 0, bkmeaUser = 0;
            ApplicantType applicantType = null;
//            Date startDate, endDate;
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            if (request.getParameter("nid") != null) {
                nid = request.getParameter("nid").trim();
            }
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
            if (request.getParameter("regularUser") != null) {
                regularUser = Integer.parseInt(request.getParameter("regularUser"));
            }
            if (request.getParameter("bgmeaUser") != null) {
                bgmeaUser = Integer.parseInt(request.getParameter("bgmeaUser"));
            }
            if (request.getParameter("applicantType") != null) {
                applicantType = (ApplicantType.valueOf(request.getParameter("applicantType")));
            }
            if (request.getParameter("applicationStatus") != null) {
                applicationStatus = request.getParameter("applicationStatus");
            }
            if (request.getParameter("ward") != null) {
                wardNo = request.getParameter("ward");
            }
            Calendar startDate = null, endDate = null;
            System.out.println("request.getParameter(\"startDate\") = " + request.getParameter("startDate"));
            if (request.getParameter("startDate") != null && !"".equals(request.getParameter("startDate"))) {
                startDate = DateUtilities.stringToCalendar(request.getParameter("startDate"));
            }
            System.out.println("request.getParameter(\"endDate\") = " + request.getParameter("endDate"));
            if (request.getParameter("endDate") != null && !"".equals(request.getParameter("endDate"))) {
                endDate = DateUtilities.stringToCalendar(request.getParameter("endDate"));
            }

            Locale locale = LocaleContextHolder.getLocale();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Map parameter = new HashMap();
            parameter.put("schemeId", ((UserDetail) request.getSession().getAttribute("userDetail")).getSchemeId());
            parameter.put("fiscalYearId", request.getParameter("fiscalYear").equals("") ? null : Integer.valueOf(request.getParameter("fiscalYear")));
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
            parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);
            parameter.put("regularUser", regularUser);
            parameter.put("bgmeaUser", bgmeaUser);
            parameter.put("bkmeaUser", bkmeaUser);
            parameter.put("applicantType", applicantType);
            parameter.put("nid", nid);
            parameter.put("startDate", startDate);
            parameter.put("endDate", endDate);
            parameter.put("applicationStatus", applicationStatus != null && !"".equals(applicationStatus) ? Integer.valueOf(applicationStatus) : null);
            parameter.put("wardNo", wardNo != null && !"".equals(wardNo) ? Integer.valueOf(wardNo) : null);

            boolean isUnionUser = ((UserDetail) request.getSession().getAttribute("userDetail")).getUnion() != null ? Boolean.TRUE : Boolean.FALSE;
            parameter.put("isUnionUser", isUnionUser);

            List<Object> resultList = applicantService.getApplicantListForEditingBySearchParameter(parameter, beginIndex, pageSize);
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
                ja.put("en".equals(locale.getLanguage()) ? applicant.getFullNameEn() : applicant.getFullNameBn());
                ja.put("en".equals(locale.getLanguage()) ? formatter.format(applicant.getCreationDate().getTime()) : CommonUtility.getNumberInBangla(formatter.format(applicant.getCreationDate().getTime())));
                ja.put("en".equals(locale.getLanguage()) ? applicant.getApplicationStatus() : applicant.getApplicationStatus().getDisplayNameBn());
                String edit = localizer.getLocalizedText("edit", LocaleContextHolder.getLocale());
                String viewDetails = localizer.getLocalizedText("viewDetails", LocaleContextHolder.getLocale());
                ja.put("<a href=#" + " onclick=loadApplicant(" + applicant.getId() + ") title=\"" + viewDetails + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>");
                ja.put("<a href=\"" + request.getContextPath() + "/applicant/edit/" + applicant.getId() + "\" title=\"" + edit + "\"><span class=\"glyphicon glyphicon-edit\"></span></a>");
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

    @RequestMapping(value = "/applicant/pendingOnlineList", method = RequestMethod.GET)
    public String getPendingOnlineList(Model model, HttpServletRequest request) {
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapFiscalYearName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "pendingOnlineList";
    }

    @RequestMapping(value = "/applicant/viewApplicant/{id}", method = RequestMethod.GET)
    public String viewApplicantForm(@PathVariable("id") Integer id, Model model) {
        try {
            System.out.println("in view applicant id = " + id);
            Applicant applicant = this.applicantService.getApplicant(id);
            String dob = CalendarUtility.getDateString(applicant.getDateOfBirth(), ApplicationConstants.DATE_FORMAT);
            if (LocaleContextHolder.getLocale().getLanguage().equals("bn")) {
                model.addAttribute("nid", (CommonUtility.getNumberInBangla(applicant.getNid().toString())));
                model.addAttribute("dob", CommonUtility.getNumberInBangla(dob));
                model.addAttribute("mobileNo", applicant.getMobileNo() != null ? '০' + CommonUtility.getNumberInBangla(applicant.getMobileNo().toString()) : "");
                model.addAttribute("presentWardNo", CommonUtility.getNumberInBangla(applicant.getPresentWardNo()));
                model.addAttribute("presentPostCode", CommonUtility.getNumberInBangla(applicant.getPresentPostCode()));
                model.addAttribute("permanentWardNo", CommonUtility.getNumberInBangla(applicant.getPermanentWardNo()));
                model.addAttribute("permanentPostCode", CommonUtility.getNumberInBangla(applicant.getPresentPostCode()));
                model.addAttribute("accountNo", CommonUtility.getNumberInBangla(applicant.getAccountNo()));
//                model.addAttribute("monthlyIncome", CommonUtility.getNumberInBangla(applicant.getApplicantSocioEconomicInfo().getMonthlyIncome().toString()));
//                model.addAttribute("age", CommonUtility.getNumberInBangla(applicant.getApplicantSocioEconomicInfo().getAge().toString()));
//                model.addAttribute("conceptionDuration", CommonUtility.getNumberInBangla(applicant.getApplicantSocioEconomicInfo().getConceptionDuration().toString()));
            } else {
                model.addAttribute("nid", applicant.getNid().toString());
                model.addAttribute("dob", dob);
                model.addAttribute("mobileNo", applicant.getMobileNo() != null ? '0' + applicant.getMobileNo().toString() : "");
                model.addAttribute("presentWardNo", applicant.getPresentWardNo());
                model.addAttribute("presentPostCode", applicant.getPresentPostCode());
                model.addAttribute("permanentWardNo", applicant.getPermanentWardNo());
                model.addAttribute("permanentPostCode", applicant.getPresentPostCode());
                model.addAttribute("accountNo", applicant.getAccountNo());
//                model.addAttribute("monthlyIncome", applicant.getApplicantSocioEconomicInfo().getMonthlyIncome().toString());
//                model.addAttribute("age", applicant.getApplicantSocioEconomicInfo().getAge().toString());
//                model.addAttribute("conceptionDuration", applicant.getApplicantSocioEconomicInfo().getConceptionDuration().toString());

            }
            model.addAttribute("applicant", applicant);
            model.addAttribute("imagePath", FILE_CREATION_PATH);
            model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");
            model.addAttribute("headerTitle", "Test");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "viewApplicant";
    }

    @RequestMapping(value = "/applicant/summary/{id}", method = RequestMethod.GET)
    public String viewApplicantSummary(@PathVariable("id") Integer id, Model model) {
        try {
            System.out.println("in view applicant id = " + id);
            Applicant applicant = this.applicantService.getApplicant(id);
            String dob = CalendarUtility.getDateString(applicant.getDateOfBirth(), ApplicationConstants.DATE_FORMAT);
            if (LocaleContextHolder.getLocale().getLanguage().equals("bn")) {
                model.addAttribute("nid", (CommonUtility.getNumberInBangla(applicant.getNid().toString())));
                model.addAttribute("dob", CommonUtility.getNumberInBangla(dob));
                model.addAttribute("mobileNo", applicant.getMobileNo() != null ? '০' + CommonUtility.getNumberInBangla(applicant.getMobileNo().toString()) : "");
                model.addAttribute("presentWardNo", CommonUtility.getNumberInBangla(applicant.getPresentWardNo()));
                model.addAttribute("presentPostCode", CommonUtility.getNumberInBangla(applicant.getPresentPostCode()));
                model.addAttribute("permanentWardNo", CommonUtility.getNumberInBangla(applicant.getPermanentWardNo()));
                model.addAttribute("permanentPostCode", CommonUtility.getNumberInBangla(applicant.getPresentPostCode()));
                model.addAttribute("accountNo", CommonUtility.getNumberInBangla(applicant.getAccountNo()));
//                model.addAttribute("monthlyIncome", CommonUtility.getNumberInBangla(applicant.getApplicantSocioEconomicInfo().getMonthlyIncome().toString()));
//                model.addAttribute("age", CommonUtility.getNumberInBangla(applicant.getApplicantSocioEconomicInfo().getAge().toString()));
//                model.addAttribute("conceptionDuration", CommonUtility.getNumberInBangla(applicant.getApplicantSocioEconomicInfo().getConceptionDuration().toString()));
            } else {
                model.addAttribute("nid", applicant.getNid().toString());
                model.addAttribute("dob", dob);
                model.addAttribute("mobileNo", applicant.getMobileNo() != null ? '0' + applicant.getMobileNo().toString() : "");
                model.addAttribute("presentWardNo", applicant.getPresentWardNo());
                model.addAttribute("presentPostCode", applicant.getPresentPostCode());
                model.addAttribute("permanentWardNo", applicant.getPermanentWardNo());
                model.addAttribute("permanentPostCode", applicant.getPresentPostCode());
                model.addAttribute("accountNo", applicant.getAccountNo());
//                model.addAttribute("monthlyIncome", applicant.getApplicantSocioEconomicInfo().getMonthlyIncome().toString());
//                model.addAttribute("age", applicant.getApplicantSocioEconomicInfo().getAge().toString());
//                model.addAttribute("conceptionDuration", applicant.getApplicantSocioEconomicInfo().getConceptionDuration().toString());

            }
            model.addAttribute("applicant", applicant);
            model.addAttribute("imagePath", FILE_CREATION_PATH);
            model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");
            model.addAttribute("headerTitle", "Test");
            model.addAttribute("summaryType", "edit");
            switch (applicant.getApplicantType()) {
                case UNION:
                    model.addAttribute("prevUrl", "/applicant/union/create");
                    break;
                case MUNICIPAL:
                    model.addAttribute("prevUrl", "/applicant/municipal/create");
                    break;
                case CITYCORPORATION:
                    model.addAttribute("prevUrl", "/applicant/citycorporation/create");
                    break;
                case BGMEA:
                    model.addAttribute("prevUrl", "/applicant/bgmea/create");
                    break;
                case BKMEA:
                    model.addAttribute("prevUrl", "/applicant/bkmea/create");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "applicantSummary";
    }

    @RequestMapping(value = "/applicant/summary/edit/{id}", method = RequestMethod.GET)
    public String viewApplicantSummaryEdit(@PathVariable("id") Integer id, Model model) {
        try {
            System.out.println("in view applicant id = " + id);
            Applicant applicant = this.applicantService.getApplicant(id);
            String dob = CalendarUtility.getDateString(applicant.getDateOfBirth(), ApplicationConstants.DATE_FORMAT);
            if (LocaleContextHolder.getLocale().getLanguage().equals("bn")) {
                model.addAttribute("nid", (CommonUtility.getNumberInBangla(applicant.getNid().toString())));
                model.addAttribute("dob", CommonUtility.getNumberInBangla(dob));
                model.addAttribute("mobileNo", applicant.getMobileNo() != null ? '০' + CommonUtility.getNumberInBangla(applicant.getMobileNo().toString()) : "");
                model.addAttribute("presentWardNo", CommonUtility.getNumberInBangla(applicant.getPresentWardNo()));
                model.addAttribute("presentPostCode", CommonUtility.getNumberInBangla(applicant.getPresentPostCode()));
                model.addAttribute("permanentWardNo", CommonUtility.getNumberInBangla(applicant.getPermanentWardNo()));
                model.addAttribute("permanentPostCode", CommonUtility.getNumberInBangla(applicant.getPresentPostCode()));
                model.addAttribute("accountNo", CommonUtility.getNumberInBangla(applicant.getAccountNo()));
//                model.addAttribute("monthlyIncome", CommonUtility.getNumberInBangla(applicant.getApplicantSocioEconomicInfo().getMonthlyIncome().toString()));
//                model.addAttribute("age", CommonUtility.getNumberInBangla(applicant.getApplicantSocioEconomicInfo().getAge().toString()));
//                model.addAttribute("conceptionDuration", CommonUtility.getNumberInBangla(applicant.getApplicantSocioEconomicInfo().getConceptionDuration().toString()));
            } else {
                model.addAttribute("nid", applicant.getNid().toString());
                model.addAttribute("dob", dob);
                model.addAttribute("mobileNo", applicant.getMobileNo() != null ? '0' + applicant.getMobileNo().toString() : "");
                model.addAttribute("presentWardNo", applicant.getPresentWardNo());
                model.addAttribute("presentPostCode", applicant.getPresentPostCode());
                model.addAttribute("permanentWardNo", applicant.getPermanentWardNo());
                model.addAttribute("permanentPostCode", applicant.getPresentPostCode());
                model.addAttribute("accountNo", applicant.getAccountNo());
//                model.addAttribute("monthlyIncome", applicant.getApplicantSocioEconomicInfo().getMonthlyIncome().toString());
//                model.addAttribute("age", applicant.getApplicantSocioEconomicInfo().getAge().toString());
//                model.addAttribute("conceptionDuration", applicant.getApplicantSocioEconomicInfo().getConceptionDuration().toString());

            }
            model.addAttribute("applicant", applicant);
            model.addAttribute("imagePath", FILE_CREATION_PATH);
            model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");
            model.addAttribute("headerTitle", "Test");
            model.addAttribute("summaryType", "edit");
            switch (applicant.getApplicantType()) {
                case UNION:
                    model.addAttribute("prevUrl", "/applicant/union/list");
                    break;
                case MUNICIPAL:
                    model.addAttribute("prevUrl", "/applicant/municipal/list");
                    break;
                case CITYCORPORATION:
                    model.addAttribute("prevUrl", "/applicant/citycorporation/list");
                    break;
                case BGMEA:
                    model.addAttribute("prevUrl", "/applicant/bgmea/list");
                    break;
                case BKMEA:
                    model.addAttribute("prevUrl", "/applicant/bkmea/list");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "applicantSummary";
    }

    @RequestMapping(value = "/applicant/viewComment/{id}", method = RequestMethod.GET)
    public String viewCommentForm(@PathVariable("id") Integer id, Model model) {
        try {
            System.out.println("in comment");
            Map parameter = new HashMap();
            parameter.put("applicantId", id);
            List<SelectionComments> selectionCommentsList = this.selectionCommentsService.getSelectionCommentsList(parameter);
            model.addAttribute("selectionCommentsList", selectionCommentsList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "viewComment";
    }

    @RequestMapping(value = "/checkUniqueNid", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUniqueNid(String nid, Integer appId) {
        nid = CommonUtility.getNumberInEnglish(nid);
        boolean result = this.applicantService.checkUniqueNid(new BigInteger(nid), appId);
        return result;
    }

    @RequestMapping(value = "/checkUniqueNidForBeneficiary", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkUniqueNidForBeneficiary(@RequestParam(value = "nid", required = false) String nid, Integer appId) {
        nid = CommonUtility.getNumberInEnglish(nid);
        boolean result = this.applicantService.checkUniqueNid(new BigInteger(nid), appId);
        return result;
    }

    @RequestMapping(value = "/checkUniqueAccountNumber", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUniqueAccountNumber(String accountNo, Integer appId) {
        accountNo = CommonUtility.getNumberInEnglish(accountNo);
        boolean result = this.applicantService.checkUniqueAccountNumber(accountNo);
        return result;
    }

    @RequestMapping(value = "/checkUniqueAccountNumberAtApplicationSave", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUniqueAccountNumberAtApplicationSave(String accountNo, Integer appId) {
        accountNo = CommonUtility.getNumberInEnglish(accountNo);
        boolean result = this.applicantService.checkUniqueAccountNumberAtApplicationSave(accountNo, appId);
        return result;
    }

    @RequestMapping(value = "/checkUniqueAccountNumberForBeneficiary", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkUniqueAccountNumberForBeneficiary(@RequestParam(value = "accountNo", required = false) String accountNo,
            @RequestParam(value = "beneficiaryID", required = false) Integer beneficiaryID) {

        accountNo = CommonUtility.getNumberInEnglish(accountNo);
        boolean result = this.applicantService.checkUniqueAccountNumberForBeneficiary(accountNo, beneficiaryID);
        return result;
    }

    private void mapCommonFields(ApplicantType applicantType, Model model) {
        CommonUtility.mapFiscalYearName(model);
        CommonUtility.mapBirthPlaceName(model);
        CommonUtility.mapEducationLevelEnumName(model);
        CommonUtility.mapReligionEnumName(model);
        CommonUtility.mapMaritalInfoEnumName(model);
        CommonUtility.mapGlobalVariables(model);
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapPaymentTypeName(model);
        CommonUtility.mapAccountTypeName(model);
        CommonUtility.mapYesNoEnumName(model);
        CommonUtility.mapConceptionTermEnumName(model);
        CommonUtility.mapDisabilitlyEnumName(model);
        CommonUtility.mapMonthlyIncome(applicantType, model);
        CommonUtility.mapHHWallMadeOfEnumName(applicantType, model);
//        CommonUtility.mapBatchName(model, new Integer("1"));
        CommonUtility.getWardNoList(model);
        switch (applicantType) {
            case UNION:
                CommonUtility.mapLandOwnerShipEnumName(model);
                CommonUtility.mapOccupationRuralEnumName(model); // husband's occupation
                break;
            case MUNICIPAL:
            case CITYCORPORATION:
                CommonUtility.mapOccupationUrbanEnumName(applicantType, model); // applicant's occupation            
                break;
            case BGMEA:
                CommonUtility.mapOccupationUrbanEnumName(applicantType, model); // applicant's occupation for BGMEa           
                CommonUtility.mapBGMEAFactoryName(model);
                break;
            case BKMEA:
                CommonUtility.mapOccupationUrbanEnumName(applicantType, model); // applicant's occupation  for BKMEA          
                CommonUtility.mapBKMEAFactoryName(model);
                break;

        }
    }

    private void setDefaultAddress(UserDetail loggedUser, ApplicantForm applicantForm) {
        if (loggedUser.getUnion() != null) {
            System.out.println("loggedUser = " + loggedUser.getUnion().getNameInEnglish());
            applicantForm.setIsUnionAvailable(true);
            applicantForm.setPresentUnion(loggedUser.getUnion());
            applicantForm.setIsUpazilaAvailable(true);
            applicantForm.setPresentUpazila(loggedUser.getUpazila());
            applicantForm.setIsDistrictAvailable(true);
            applicantForm.setPresentDistrict(loggedUser.getDistrict());
            applicantForm.setIsDivisionAvailable(true);
            applicantForm.setPresentDivision(loggedUser.getDivision());
        }
        if (loggedUser.getUpazila() != null) {
            applicantForm.setIsUpazilaAvailable(true);
            applicantForm.setPresentUpazila(loggedUser.getUpazila());
            applicantForm.setIsDistrictAvailable(true);
            applicantForm.setPresentDistrict(loggedUser.getDistrict());
            applicantForm.setIsDivisionAvailable(true);
            applicantForm.setPresentDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDistrict() != null) {
            applicantForm.setIsDistrictAvailable(true);
            applicantForm.setPresentDistrict(loggedUser.getDistrict());
            applicantForm.setIsDivisionAvailable(true);
            applicantForm.setPresentDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDivision() != null) {
            applicantForm.setIsDivisionAvailable(true);
            applicantForm.setPresentDivision(loggedUser.getDivision());
        }
        if (loggedUser.getBgmeaFactory() != null) {
            applicantForm.setIsBgmeaAvailable(true);
            applicantForm.setBgmeaFactory(loggedUser.getBgmeaFactory());
        }
        if (loggedUser.getBkmeaFactory() != null) {
            applicantForm.setIsBkmeaAvailable(true);
            applicantForm.setBkmeaFactory(loggedUser.getBkmeaFactory());
        }

    }

    @RequestMapping(value = "/applicant/union/create")
    public String createApplicantUnion(@ModelAttribute ApplicantForm applicantForm, Model model, HttpSession session) throws ExceptionWrapper {
        try {
            UserDetail loggedUser = (UserDetail) session.getAttribute("userDetail");
            mapCommonFields(ApplicantType.UNION, model);
            setDefaultAddress(loggedUser, applicantForm);
            model.addAttribute("actionType", "create");
            applicantForm.setApplicantType(ApplicantType.UNION);
            return "applicantFormUnion";
        } catch (Exception e) {
            //logger.infoer("applicant create page union: " + e.getMessage());
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @RequestMapping(value = "/applicant/municipal/create")
    public String createApplicantMunicipal(@ModelAttribute ApplicantForm applicantForm, Model model, HttpSession session) {
        UserDetail loggedUser = (UserDetail) session.getAttribute("userDetail");
        mapCommonFields(ApplicantType.MUNICIPAL, model);
        setDefaultAddress(loggedUser, applicantForm);
        model.addAttribute("actionType", "create");
        applicantForm.setApplicantType(ApplicantType.MUNICIPAL);
        return "applicantFormMunicipal";
    }

    @RequestMapping(value = "/applicant/cityCorporation/create")
    public String createApplicantCityCorporation(@ModelAttribute ApplicantForm applicantForm, Model model, HttpSession session) {
        UserDetail loggedUser = (UserDetail) session.getAttribute("userDetail");
        mapCommonFields(ApplicantType.CITYCORPORATION, model);
        setDefaultAddress(loggedUser, applicantForm);
        model.addAttribute("actionType", "create");
        applicantForm.setApplicantType(ApplicantType.CITYCORPORATION);
        return "applicantFormCityCorporation";
    }

    @RequestMapping(value = "/applicant/bgmea/create")
    public String createApplicantBgmea(@ModelAttribute ApplicantForm applicantForm, Model model, HttpSession session) {
        UserDetail loggedUser = (UserDetail) session.getAttribute("userDetail");
        mapCommonFields(ApplicantType.BGMEA, model);
        setDefaultAddress(loggedUser, applicantForm);
        model.addAttribute("actionType", "create");
        model.addAttribute("type", "bgmea");
        applicantForm.setApplicantType(ApplicantType.BGMEA);
        return "applicantFormBgmeaBkmea";
    }

    @RequestMapping(value = "/applicant/bkmea/create")
    public String createApplicantBkmea(@ModelAttribute ApplicantForm applicantForm, Model model, HttpSession session) {
        UserDetail loggedUser = (UserDetail) session.getAttribute("userDetail");
        mapCommonFields(ApplicantType.BKMEA, model);
        setDefaultAddress(loggedUser, applicantForm);
        model.addAttribute("actionType", "create");
        model.addAttribute("type", "bkmea");
        applicantForm.setApplicantType(ApplicantType.BKMEA);
        return "applicantFormBgmeaBkmea";
    }

    @RequestMapping(value = "/applicant/create", method = RequestMethod.POST)
    public String createApplicant(@Valid ApplicantForm applicantForm, BindingResult bindingResult, Model model, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException, ExceptionWrapper {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error)
                -> {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors()) {
            if (null != applicantForm.getApplicantType()) {
                System.out.println("applicantForm.getApplicantType() = " + applicantForm.getApplicantType());
                switch (applicantForm.getApplicantType()) {
                    case UNION:
                        return createApplicantUnion(applicantForm, model, session);
                    case MUNICIPAL:
                        return createApplicantMunicipal(applicantForm, model, session);
                    case CITYCORPORATION:
                        return createApplicantMunicipal(applicantForm, model, session);
                    case BGMEA:
                        return createApplicantBgmea(applicantForm, model, session);
                    case BKMEA:
                        return createApplicantBkmea(applicantForm, model, session);
                }
            } else {
                //logger.infoer("Applicant type not found!");
            }
        }

        try {
            System.out.println("applicantForm.getApplicantType() = " + applicantForm.getApplicantType());
            Applicant applicant = new Applicant();

            applicant.setCreatedBy((User) session.getAttribute("user"));
            applicant.setCreationDate(Calendar.getInstance());
            applicant.setApplicationStatus(ApplicationStatus.PRIORITIZATION_PENDING);
            applicant.setFiscalYear(fiscalYearService.getActiveFiscalYear());
//            applicant.setBatch(applicantForm.getBatch());

            createBasicInfo(applicant, applicantForm);
            createAddressInfo(applicant, applicantForm);
            createSocioEconomicInfo(applicant, applicantForm);
            createHealthStatusInfo(applicant, applicantForm);
            createPaymentInfo(applicant, applicantForm);
            createBiometricInfo(applicant, applicantForm);
            createAttachmentInfo(request, applicant, applicantForm);
            //  createIsLMMisExist(applicant);
            this.applicantService.save(applicant);
            Integer applicantId = this.applicantService.getApplicantIdByNid(new BigInteger(applicantForm.getNid()));
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);

            return "redirect:/applicant/summary/" + applicantId;
        } catch (ExceptionWrapper e) {
            e.printStackTrace();
            //logger.error(e.getMessage());
            throw e;
        }
    }

    private void createIsLMMisExist(Applicant applicant) {
        int otherMisSchemId = applicantService.getOtherMISchemeId(applicant.getNid().toString());

        applicant.setIsLMMISExist(otherMisSchemId);
    }

    private void createBasicInfo(Applicant applicant, ApplicantForm applicantForm) {
        applicant.setFullNameInBangla(applicantForm.getFullNameInBangla());
        applicant.setFullNameInEnglish(applicantForm.getFullNameInEnglish());
        applicant.setFatherName(applicantForm.getFatherName());
        applicant.setMotherName(applicantForm.getMotherName());
        applicant.setSpouseName(applicantForm.getSpouseName());
        applicant.setNickName(applicantForm.getNickName());
        applicant.setDateOfBirth(applicantForm.getDateOfBirth());
        applicant.setGender(Gender.FEMALE);
        applicant.setBirthPlace(applicantForm.getBirthPlace());
        applicant.setEducationLevelEnum(applicantForm.getEducationLevelEnum());
        applicant.setReligionEnum(applicantForm.getReligionEnum());
        applicant.setMaritalInfoEnum(applicantForm.getMaritalInfoEnum());
        if (applicantForm.getMobileNo() != null && !applicantForm.getMobileNo().isEmpty()) {
            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
                applicant.setMobileNo(Integer.parseInt(applicantForm.getMobileNo()));
            } else {
                applicant.setMobileNo(Integer.parseInt(CommonUtility.getNumberInEnglish(applicantForm.getMobileNo())));
            }
        }
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            applicant.setNid(new BigInteger(applicantForm.getNid()));
        } else {
            applicant.setNid(new BigInteger(CommonUtility.getNumberInEnglish(applicantForm.getNid())));
        }
        applicant.setOccupation(applicantForm.getOccupation());
        applicant.setBloodGroup(applicantForm.getBloodGroup());
        applicant.setNrb(applicantForm.getNrb());
        applicant.setBeneficiaryInOtherScheme(applicantForm.getBeneficiaryInOtherScheme());
    }

    private void createAddressInfo(Applicant applicant, ApplicantForm applicantForm) {
        applicant.setApplicantType(applicantForm.getApplicantType());
        if (null != applicant.getApplicantType()) {
            switch (applicant.getApplicantType()) {
                case UNION:
                    applicant.setFactory(null);
                    applicant.setScheme(schemeService.getScheme(new Integer("1")));
                    break;
                case MUNICIPAL:
                    applicant.setFactory(null);
                    applicant.setScheme(schemeService.getScheme(new Integer("3")));
                    break;
                case CITYCORPORATION:
                    applicant.setFactory(null);
                    applicant.setScheme(schemeService.getScheme(new Integer("3")));
                    break;
                case BGMEA:
                    applicant.setFactory(applicantForm.getBgmeaFactory());
                    applicant.setScheme(schemeService.getScheme(new Integer("3")));
                    break;
                case BKMEA:
                    applicant.setFactory(applicantForm.getBkmeaFactory());
                    applicant.setScheme(schemeService.getScheme(new Integer("3")));
                    break;
            }
        }
        applicant.setPresentAddressLine1(applicantForm.getPresentAddressLine1());
        applicant.setPresentAddressLine2(applicantForm.getPresentAddressLine2());
        applicant.setPresentDivision(applicantForm.getPresentDivision());
        applicant.setPresentDistrict(applicantForm.getPresentDistrict());
        applicant.setPresentUpazila(applicantForm.getPresentUpazila());
        applicant.setPresentUnion(applicantForm.getPresentUnion());
        if (applicantForm.getPresentVillage() != null && applicantForm.getPresentVillage().getId() != null) {
            applicant.setPresentVillage(applicantForm.getPresentVillage());
            applicant.setPresentAddressLine1((villageService.getVillage(applicantForm.getPresentVillage().getId())).getNameInBangla());
        } else if (applicantForm.getPresentAddressLine1() == null) {
            applicant.setPresentAddressLine1(";");
        }
        applicant.setPermanentAddressLine1(applicantForm.getPermanentAddressLine1());
        applicant.setPermanentAddressLine2(applicantForm.getPermanentAddressLine2());
        applicant.setPermanentDivision(applicantForm.getPermanentDivision());
        applicant.setPermanentDistrict(applicantForm.getPermanentDistrict());
        applicant.setPermanentUpazila(applicantForm.getPermanentUpazila());
        applicant.setPermanentUnion(applicantForm.getPermanentUnion());
        if (applicantForm.getPermanentVillage() != null && applicantForm.getPresentVillage().getId() != null) {
            applicant.setPermanentVillage(applicantForm.getPermanentVillage());
            applicant.setPermanentAddressLine1((villageService.getVillage(applicantForm.getPermanentVillage().getId())).getNameInBangla());
        } else if (applicantForm.getPermanentAddressLine1() == null) {
            applicant.setPermanentAddressLine1(";");
        }
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            applicant.setPresentWardNo(applicantForm.getPresentWardNo());
            applicant.setPresentPostCode(applicantForm.getPresentPostCode());
            applicant.setPermanentWardNo(applicantForm.getPermanentWardNo());
            applicant.setPermanentPostCode(applicantForm.getPermanentPostCode());
        } else {
            if (applicantForm.getPresentWardNo() != null) {
                applicant.setPresentWardNo(CommonUtility.getNumberInEnglish(applicantForm.getPresentWardNo()));
            }
            applicant.setPresentPostCode(CommonUtility.getNumberInEnglish(applicantForm.getPresentPostCode()));
            if (applicantForm.getPermanentWardNo() != null) {
                applicant.setPermanentWardNo(CommonUtility.getNumberInEnglish(applicantForm.getPermanentWardNo()));
            }
            applicant.setPermanentPostCode(CommonUtility.getNumberInEnglish(applicantForm.getPermanentPostCode()));
        }
    }

    private void createSocioEconomicInfo(Applicant applicant, ApplicantForm applicantForm) {

        int totalScore = 0;
        ApplicantSocioEconomicInfo applicantSocioEconomicInfo = applicant.getApplicantSocioEconomicInfo() != null ? applicant.getApplicantSocioEconomicInfo() : new ApplicantSocioEconomicInfo();
        applicantSocioEconomicInfo.setApplicant(applicant);
        applicantSocioEconomicInfo.setDisability(applicantForm.getDisability());
        totalScore += applicantForm.getDisability().getScore();
        applicantSocioEconomicInfo.sethHWallMadeOf(applicantForm.gethHWallMadeOf());
        totalScore += applicantForm.gethHWallMadeOf().getScore();
        applicantSocioEconomicInfo.setMonthlyIncome(applicantForm.getMonthlyIncome());
        totalScore += applicantForm.getMonthlyIncome().getScore();
        applicantSocioEconomicInfo.sethASElectricity(applicantForm.gethASElectricity());
        totalScore += applicantForm.gethASElectricity().getScore();
        applicantSocioEconomicInfo.sethASElectricFan(applicantForm.gethASElectricFan());
        totalScore += applicantForm.gethASElectricFan().getScore();
        if (applicantForm.getApplicantType() == ApplicantType.UNION) {
            applicantSocioEconomicInfo.setLandSizeRural(applicantForm.getLandSizeRural());
            totalScore += applicantForm.getLandSizeRural().getScore();
            applicantSocioEconomicInfo.setOccupationRural(applicantForm.getOccupationRural());
            totalScore += applicantForm.getOccupationRural().getScore();
            applicantSocioEconomicInfo.sethASLatrineRural(applicantForm.gethASLatrineRural());
            totalScore += applicantForm.gethASLatrineRural().getScore();
            applicantSocioEconomicInfo.sethASTubewellRural(applicantForm.gethASTubewellRural());
            totalScore += applicantForm.gethASTubewellRural().getScore();
            if (applicantForm.getLandSizeRural().getScore() == 1 || applicantForm.getOccupationRural().getScore() == 1
                    || applicantForm.gethASLatrineRural().getScore() == 1 || applicantForm.gethASElectricity().getScore() == 1
                    || applicantForm.gethHWallMadeOf().getScore() == 1) {
                applicant.setSystemRecommendedStatus(SystemRecommendedStatus.RECOMMENDED);
            } else {
                applicant.setSystemRecommendedStatus(SystemRecommendedStatus.NOT_RECOMMENDED);
            }
        } else {
            applicantSocioEconomicInfo.setHasResidenceUrban(applicantForm.getHasResidenceUrban());
            totalScore += applicantForm.getHasResidenceUrban().getScore();
            applicantSocioEconomicInfo.setOccupationUrban(applicantForm.getOccupationUrban());
            totalScore += applicantForm.getOccupationUrban().getScore();
            applicantSocioEconomicInfo.sethASKitchenUrban(applicantForm.gethASKitchenUrban());
            totalScore += applicantForm.gethASKitchenUrban().getScore();
            applicantSocioEconomicInfo.sethASTelivisionUrban(applicantForm.gethASTelivisionUrban());
            totalScore += applicantForm.gethASTelivisionUrban().getScore();

            if (applicantForm.gethASElectricity().getScore() == 1 || applicantForm.gethHWallMadeOf().getScore() == 1
                    || applicantForm.gethASKitchenUrban().getScore() == 1 || applicantForm.gethASElectricFan().getScore() == 1
                    || applicantForm.gethASTelivisionUrban().getScore() == 1) {
                applicant.setSystemRecommendedStatus(SystemRecommendedStatus.RECOMMENDED);
            } else {
                applicant.setSystemRecommendedStatus(SystemRecommendedStatus.NOT_RECOMMENDED);
            }
        }
        applicant.setApplicantSocioEconomicInfo(applicantSocioEconomicInfo);
        applicant.setScore(totalScore);

    }

    private void createHealthStatusInfo(Applicant applicant, ApplicantForm applicantForm) {
        applicant.setConceptionTerm(applicantForm.getConceptionTerm());
        applicant.setConceptionDuration(applicantForm.getConceptionDuration());
    }

    private void createPaymentInfo(Applicant applicant, ApplicantForm applicantForm) {
        applicant.setPaymentType(applicantForm.getPaymentType());
        if (applicant.getPaymentType() != null) {
            switch (applicant.getPaymentType()) {
                case BANKING:
                    applicant.setBank(applicantForm.getBank().getId() != null ? applicantForm.getBank() : null);
                    applicant.setBranch(applicantForm.getBranch().getId() != null ? applicantForm.getBranch() : null);
                    applicant.setAccountType(applicantForm.getAccountType());
                    applicant.setMobileBankingProvider(null);
                    applicant.setPostOfficeBranch(null);
                    break;
                case MOBILEBANKING:
                    applicant.setMobileBankingProvider(applicantForm.getMobileBankingProvider().getId() != null ? applicantForm.getMobileBankingProvider() : null);
                    applicant.setAccountType(this.accountTypeService.getAccountType(2));
                    applicant.setBank(null);
                    applicant.setBranch(null);
                    applicant.setPostOfficeBranch(null);
                    break;
                case POSTOFFICE:
                    applicant.setPostOfficeBranch(applicantForm.getPostOfficeBranch().getId() != null ? applicantForm.getPostOfficeBranch() : null);
                    applicant.setAccountType(applicantForm.getAccountTypePO());
                    applicant.setBank(null);
                    applicant.setBranch(null);
                    applicant.setMobileBankingProvider(null);
                    break;
            }
            applicant.setAccountName(applicantForm.getAccountName());
            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
                applicant.setAccountNo(applicantForm.getAccountNo());
            } else {
                applicant.setAccountNo(applicantForm.getAccountNo() != null ? CommonUtility.getNumberInEnglish(applicantForm.getAccountNo()) : null);
            }
        } else {
            applicant.setBank(null);
            applicant.setBranch(null);
            applicant.setPostOfficeBranch(null);
            applicant.setMobileBankingProvider(null);
            applicant.setAccountType(null);
            applicant.setAccountName(null);
            applicant.setAccountNo(null);
        }
    }

    private void createBiometricInfo(Applicant applicant, ApplicantForm applicantForm) {
        try {
            ApplicantBiometricInfo applicantBiometricInfo = applicant.getApplicantBiometricInfo() != null ? applicant.getApplicantBiometricInfo() : new ApplicantBiometricInfo();
            applicantBiometricInfo.setApplicant(applicant);
            if (!applicantForm.getPhotoData().equals("")) {
                applicantBiometricInfo.setPhotoPath(applicantForm.getNid());
                byte[] decodedByte = Base64.getDecoder().decode(applicantForm.getPhotoData());
                applicantBiometricInfo.setPhotoData(decodedByte);
            }

//            if (CommonForBeneficiaryControllers.upazilaId.contains(applicantForm.getPermanentUpazila().getId())) {
//                if (!applicantForm.getPhotoData().equals("")) {
//                    applicantBiometricInfo.setPhotoPath(applicantForm.getNid());
//                    byte[] decodedByte = Base64.getDecoder().decode(applicantForm.getPhotoData());
//                    applicantBiometricInfo.setPhotoData(decodedByte);
//                }
//            } else if (!applicantForm.getPhoto().getOriginalFilename().equals("")) {
//                applicantBiometricInfo.setPhotoPath(applicantForm.getPhoto().getOriginalFilename());
//                applicantBiometricInfo.setPhotoData(applicantForm.getPhoto().getBytes());
//            }
            if (!applicantForm.getSignature().getOriginalFilename().equals("")) {
                applicantBiometricInfo.setSignaturePath(applicantForm.getSignature().getOriginalFilename());
                applicantBiometricInfo.setSignatureData(applicantForm.getSignature().getBytes());
            }
            applicant.setApplicantBiometricInfo(applicantBiometricInfo);
        } catch (IOException ex) {
            ex.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createAttachmentInfo(HttpServletRequest request, Applicant applicant, ApplicantForm applicantForm) {
        String saveDirectory = FILE_CREATION_PATH + applicant.getNid() + "/";
        System.out.println("saveDirectory " + saveDirectory);
        List<MultipartFile> files = applicantForm.getMultipartFileList();
        int i = 0;
        System.out.println(" files.size() " + files.size());
        if (null != files && files.size() > 0) {
            Iterator<MultipartFile> iter = files.iterator();
            while (iter.hasNext()) {
                MultipartFile multipartFile = iter.next();
                String fileName = multipartFile.getOriginalFilename();
                if (fileName != null && !fileName.isEmpty()) {
                    try {
                        System.out.println("i ------------>" + i);
                        if (i == 0) {
                            new File(saveDirectory + "anc").mkdirs();
                            multipartFile.transferTo(new File("/opt/tomcat/bin/" + saveDirectory + "anc/" + fileName));
                        } else {
                            new File(saveDirectory + "others").mkdirs();
                            multipartFile.transferTo(new File("/opt/tomcat/bin/" + saveDirectory + "others/" + fileName));
                        }

                        i++;

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (IllegalStateException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    iter.remove();
                }
            }
        }
        List<ApplicantAttachment> applicantAttachmentList = new ArrayList<>();
        if (null != applicantForm.getMultipartFileList()) {
            i = 0;
            for (MultipartFile multipartFile : applicantForm.getMultipartFileList()) {
                ApplicantAttachment attachmentInfo = new ApplicantAttachment();
                attachmentInfo.setAttachmentName(multipartFile.getOriginalFilename());
                attachmentInfo.setFileName(multipartFile.getOriginalFilename());
                attachmentInfo.setFilePath(FILE_CREATION_PATH);
                if (i == 0) {
                    attachmentInfo.setAttachmentType(AttachmentType.ANCCARD);
                } else {
                    attachmentInfo.setAttachmentType(AttachmentType.OTHERS);
                }
                attachmentInfo.setApplicant(applicant);
                applicantAttachmentList.add(attachmentInfo);
                i++;
            }
            applicant.setApplicantAttachmentList(applicantAttachmentList);
        }
        String removeList = applicantForm.getRemoveList();

        if (removeList != null && !removeList.isEmpty()) {
            List<Integer> removeAttachmentList = new ArrayList<>();
            StringTokenizer stringTokenizer = new StringTokenizer(removeList, ",");
            System.out.println("remove " + removeList);
            while (stringTokenizer.hasMoreElements()) {
                removeAttachmentList.add(new Integer(stringTokenizer.nextElement().toString()));
            }
            applicant.setAttachmentRemoveList(removeAttachmentList);
        }
    }

    @RequestMapping(value = "/applicant/edit/{id}", method = RequestMethod.GET)
    public String editApplicantInformation(@PathVariable("id") Integer id, @ModelAttribute ApplicantForm applicantForm, Model model, boolean applicantLoadRequired, HttpSession session) throws ExceptionWrapper {
        try {
            Applicant applicant = !applicantLoadRequired ? this.applicantService.getApplicant(id) : null;
            ApplicantAncInformation applicantAncInformation = this.applicantAncInformationService.getAncInformationByApplicantId(applicant.getId());

            if (applicantAncInformation == null) {
                model.addAttribute("getAncInfo", "0");
            } else {
                model.addAttribute("getAncInfo", "1");
            }
            loadBasicInfo(applicantForm, applicant);
            loadAddressInfo(applicantForm, applicant);
            loadSocioEconomicInfo(applicantForm, applicant);
            loadHealthStatusInfo(applicantForm, applicant);
            loadANCInfo(applicantForm, applicantAncInformation);
            loadPaymentInfo(applicantForm, applicant);
            loadBiometricInfo(applicantForm, applicant);
            loadAttachmentInfo(applicantForm, applicant);

            UserDetail userDetail = ((UserDetail) session.getAttribute("userDetail"));
            CommonUtility.mapAllFiscalYearName(model);
            CommonUtility.mapBirthPlaceName(model);
            CommonUtility.mapEducationLevelEnumName(model);
            CommonUtility.mapReligionEnumName(model);
            CommonUtility.mapMaritalInfoEnumName(model);
            CommonUtility.mapGlobalVariables(model);
            CommonUtility.mapDivisionName(model);
            CommonUtility.mapPaymentTypeName(model);
            CommonUtility.mapAccountTypeName(model);
            CommonUtility.mapYesNoEnumName(model);
            CommonUtility.mapConceptionTermEnumName(model);
            CommonUtility.mapMonthlyIncome(applicant.getApplicantType(), model);
            CommonUtility.mapHHWallMadeOfEnumName(applicant.getApplicantType(), model);
            setDefaultAddress(userDetail, applicantForm);
//            CommonUtility.mapBatchName(model, null);
            CommonUtility.getWardNoList(model);
            model.addAttribute("imagePath", FILE_CREATION_PATH);
            model.addAttribute("applicantNid", applicant.getNid());

            if (null != applicant.getApplicantType()) {
                switch (applicant.getApplicantType()) {
                    case BGMEA:
                        model.addAttribute("type", "bgmea");
                        applicantForm.setBgmeaFactory(applicant.getFactory());
                        CommonUtility.mapBGMEAFactoryName(model);
                        break;
                    case BKMEA:
                        model.addAttribute("type", "bkmea");
                        applicantForm.setBkmeaFactory(applicant.getFactory());
                        CommonUtility.mapBKMEAFactoryName(model);
                        break;
                }
            }
//            }
            model.addAttribute("applicantForm", applicantForm);
            switch (applicant.getApplicantType()) {
                case UNION:
                    return "applicantFormUnion";
                case MUNICIPAL:
                    return "applicantFormMunicipal";
                case CITYCORPORATION:
                    return "applicantFormCityCorporation";
                case BGMEA:
                    return "applicantFormBgmeaBkmea";
                case BKMEA:
                    return "applicantFormBgmeaBkmea";
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    private void loadBasicInfo(ApplicantForm applicantForm, Applicant applicant) {
        applicantForm.setId(applicant.getId());
        applicantForm.setScheme(applicant.getScheme());
        applicantForm.setFiscalYear(applicant.getFiscalYear());
//        applicantForm.setBatch(applicant.getBatch());

        applicantForm.setFullNameInBangla(applicant.getFullNameInBangla());
        applicantForm.setFullNameInEnglish(applicant.getFullNameInEnglish());
        applicantForm.setFatherName(applicant.getFatherName());
        applicantForm.setMotherName(applicant.getMotherName());
        applicantForm.setSpouseName(applicant.getSpouseName());
        applicantForm.setNickName(applicant.getNickName());
        applicantForm.setDateOfBirth(applicant.getDateOfBirth());
        applicantForm.setGender(Gender.FEMALE);

        applicantForm.setBirthPlace(applicant.getBirthPlace());
        applicantForm.setEducationLevelEnum(applicant.getEducationLevelEnum());
        applicantForm.setReligionEnum(applicant.getReligionEnum());
        applicantForm.setMaritalInfoEnum(applicant.getMaritalInfoEnum());
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            applicantForm.setNid(applicant.getNid().toString());
            applicantForm.setMobileNo(applicant.getMobileNo() != null ? ("0" + applicant.getMobileNo()) : "");
        } else {

            applicantForm.setMobileNo(applicant.getMobileNo() != null ? (CommonUtility.getNumberInBangla("0" + applicant.getMobileNo().toString())) : "");
            applicantForm.setNid(CommonUtility.getNumberInBangla(applicant.getNid().toString()));
        }

        applicantForm.setOccupation(applicant.getOccupation());
        applicantForm.setBloodGroup(applicant.getBloodGroup());
        applicantForm.setNrb(applicant.getNrb());
        applicantForm.setBeneficiaryInOtherScheme(applicant.getBeneficiaryInOtherScheme());
    }

    private void loadANCInfo(ApplicantForm applicantForm, ApplicantAncInformation ancInformation) {
        if (ancInformation == null) {

        } else {
            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
                applicantForm.setAncConceptionTerm(ConceptionTerm.FIRSTCONCEPTION);
                applicantForm.setAncConceptionDuration(ancInformation.getPregnancyWeek());
            } else {
                applicantForm.setAncConceptionTerm(ConceptionTerm.FIRSTCONCEPTION);
                applicantForm.setAncConceptionDuration(CommonUtility.getNumberInBangla(ancInformation.getPregnancyWeek()));
            }
        }

    }

    private void loadAddressInfo(ApplicantForm applicantForm, Applicant applicant) {
        applicantForm.setApplicantType(applicant.getApplicantType());
        if (null != applicantForm.getApplicantType()) {
            switch (applicantForm.getApplicantType()) {
                case REGULAR:
                    //applicantForm.setFactory(null);
                    break;
                case BGMEA:
                    applicantForm.setBgmeaFactory(applicant.getFactory());
                    break;
                case BKMEA:
                    applicantForm.setBkmeaFactory(applicant.getFactory());
                    break;
            }
        }
        applicantForm.setPresentAddressLine1(applicant.getPresentAddressLine1());
        applicantForm.setPresentAddressLine2(applicant.getPresentAddressLine2());
        applicantForm.setPresentDivision(applicant.getPresentDivision());
        applicantForm.setPresentDistrict(applicant.getPresentDistrict());
        applicantForm.setPresentUpazila(applicant.getPresentUpazila());
        applicantForm.setPresentUnion(applicant.getPresentUnion());
        applicantForm.setPresentVillage(applicant.getPresentVillage());

        applicantForm.setPermanentAddressLine1(applicant.getPermanentAddressLine1());
        applicantForm.setPermanentAddressLine2(applicant.getPermanentAddressLine2());
        applicantForm.setPermanentDivision(applicant.getPermanentDivision());
        applicantForm.setPermanentDistrict(applicant.getPermanentDistrict());
        applicantForm.setPermanentUpazila(applicant.getPermanentUpazila());
        applicantForm.setPermanentUnion(applicant.getPermanentUnion());
        applicantForm.setPermanentVillage(applicant.getPermanentVillage());
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            applicantForm.setPresentWardNo(applicant.getPresentWardNo());
            applicantForm.setPresentPostCode(applicant.getPresentPostCode());
            applicantForm.setPermanentWardNo(applicant.getPermanentWardNo());
            applicantForm.setPermanentPostCode(applicant.getPermanentPostCode());
        } else {
            applicantForm.setPresentWardNo(CommonUtility.getNumberInBangla(applicant.getPresentWardNo()));
            applicantForm.setPresentPostCode(CommonUtility.getNumberInBangla(applicant.getPresentPostCode()));
            applicantForm.setPermanentWardNo(CommonUtility.getNumberInBangla(applicant.getPermanentWardNo()));
            applicantForm.setPermanentPostCode(CommonUtility.getNumberInBangla(applicant.getPermanentPostCode()));
        }
    }

    private void loadSocioEconomicInfo(ApplicantForm applicantForm, Applicant applicant) {
        ApplicantSocioEconomicInfo applicantSocioEconomicInfo = applicant.getApplicantSocioEconomicInfo();
        if (applicantSocioEconomicInfo != null) {
            applicantForm.setMonthlyIncome(applicantSocioEconomicInfo.getMonthlyIncome());
            applicantForm.sethHWallMadeOf(applicantSocioEconomicInfo.gethHWallMadeOf());
            applicantForm.setDisability(applicantSocioEconomicInfo.getDisability());
            applicantForm.sethASElectricity(applicantSocioEconomicInfo.gethASElectricity());
            applicantForm.sethASElectricFan(applicantSocioEconomicInfo.gethASElectricFan());
            if (applicant.getApplicantType() == ApplicantType.UNION) {
                applicantForm.setLandSizeRural(applicantSocioEconomicInfo.getLandSizeRural());
                applicantForm.setOccupationRural(applicantSocioEconomicInfo.getOccupationRural());
                applicantForm.sethASLatrineRural(applicantSocioEconomicInfo.gethASLatrineRural());
                applicantForm.sethASTubewellRural(applicantSocioEconomicInfo.gethASTubewellRural());
            } else {
                applicantForm.setHasResidenceUrban(applicantSocioEconomicInfo.getHasResidenceUrban());
                applicantForm.setOccupationUrban(applicantSocioEconomicInfo.getOccupationUrban());
                applicantForm.sethASKitchenUrban(applicantSocioEconomicInfo.gethASKitchenUrban());
                applicantForm.sethASTelivisionUrban(applicantSocioEconomicInfo.gethASTelivisionUrban());
            }
        }

    }

    private void loadHealthStatusInfo(ApplicantForm applicantForm, Applicant applicant) {
        applicantForm.setConceptionTerm(applicant.getConceptionTerm());
        applicantForm.setConceptionDuration(applicant.getConceptionDuration());
    }

    private void loadPaymentInfo(ApplicantForm applicantForm, Applicant applicant) {
        applicantForm.setPaymentType(applicant.getPaymentType());
        if (applicantForm.getPaymentType() != null) {
            switch (applicantForm.getPaymentType()) {
                case BANKING:
                    applicantForm.setBank(applicant.getBank());
                    applicantForm.setBranch(applicant.getBranch());
                    applicantForm.setAccountType(applicant.getAccountType());
                    applicantForm.setMobileBankingProvider(null);
                    applicantForm.setPostOfficeBranch(null);
                    break;
                case MOBILEBANKING:
                    applicantForm.setMobileBankingProvider(applicant.getMobileBankingProvider());
                    applicantForm.setAccountType(applicant.getAccountType());
                    applicantForm.setBank(null);
                    applicantForm.setBranch(null);
                    applicantForm.setPostOfficeBranch(null);
                    break;
                case POSTOFFICE:
                    applicantForm.setPostOfficeBranch(applicant.getPostOfficeBranch());
                    applicantForm.setAccountTypePO(applicant.getAccountType());
                    applicantForm.setBank(null);
                    applicantForm.setBranch(null);
                    applicantForm.setMobileBankingProvider(null);
                    break;
            }
            applicantForm.setAccountName(applicant.getAccountName());
            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
                applicantForm.setAccountNo(applicant.getAccountNo());
            } else {
                applicantForm.setAccountNo(CommonUtility.getNumberInBangla(applicant.getAccountNo()));
            }
        } else {
            applicantForm.setBank(null);
            applicantForm.setBranch(null);
            applicantForm.setPostOfficeBranch(null);
            applicantForm.setMobileBankingProvider(null);
            applicantForm.setAccountType(null);
            applicantForm.setAccountName(null);
            applicantForm.setAccountNo(null);
        }
    }

    private void loadBiometricInfo(ApplicantForm applicantForm, Applicant applicant) {
        try {
            applicantForm.setPhotoPath(applicant.getApplicantBiometricInfo().getPhotoPath());
            applicantForm.setPhotoData(applicant.getApplicantBiometricInfo().getBase64PhotoData());
            applicantForm.setSignaturePath(applicant.getApplicantBiometricInfo().getSignaturePath());
            applicantForm.setSignatureData(applicant.getApplicantBiometricInfo().getBase64SignatureData());
        } catch (Exception ex) {

        }
    }

    private void loadAttachmentInfo(ApplicantForm applicantForm, Applicant applicant) {
        List<ApplicantAttachment> applicantAttachmentList = new ArrayList<ApplicantAttachment>();
        int position = 0, temp = 0;
        for (ApplicantAttachment attachment : applicant.getApplicantAttachmentList()) {
            if (attachment.getAttachmentType() == AttachmentType.ANCCARD) {
                break;
            }
            position++;
        }
        if (applicant.getApplicantAttachmentList() != null && !applicant.getApplicantAttachmentList().isEmpty()) {
            applicantAttachmentList.add(applicant.getApplicantAttachmentList().get(position));
        }
        for (ApplicantAttachment attachment : applicant.getApplicantAttachmentList()) {
            if (position != temp) {
                applicantAttachmentList.add(attachment);
            }
            temp++;
        }

        applicantForm.setAttachmentList(applicantAttachmentList);
    }

    @RequestMapping(value = "/applicant/edit/{id}", method = RequestMethod.POST)
    public String editApplicantInformation(@ModelAttribute ApplicantForm applicantForm, BindingResult bindingResult, Model model, HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        if (bindingResult.hasErrors()) {
            return editApplicantInformation(applicantForm.getId(), applicantForm, model, true, session);
        }
        Applicant applicant;
        try {
            if (applicantForm.getId() != null) {
                applicant = this.applicantService.getApplicant(applicantForm.getId());
            } else {
                applicant = new Applicant();
            }
            applicant.setModifiedBy((User) session.getAttribute("user"));
            applicant.setModificationDate(Calendar.getInstance());
            applicant.setScheme(((UserDetail) session.getAttribute("userDetail")).getScheme());
            applicant.setFiscalYear(fiscalYearService.getActiveFiscalYear());
//            boolean isUnionUser = ((UserDetail) request.getSession().getAttribute("userDetail")).getUnion() != null ? Boolean.TRUE : Boolean.FALSE;
//            if (isUnionUser) {
//                applicant.setApplicationStatus(ApplicationStatus.PRIORITIZATION_PENDING);
//            }

            createBasicInfo(applicant, applicantForm);
            createAddressInfo(applicant, applicantForm);
            createSocioEconomicInfo(applicant, applicantForm);
            createPaymentInfo(applicant, applicantForm);
            createBiometricInfo(applicant, applicantForm);
            createAttachmentInfo(request, applicant, applicantForm);

            this.applicantService.edit(applicant);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
//            switch (applicantForm.getApplicantType())
//            {
//                case UNION:
//                    return "redirect:/applicant/union/list";
//                case MUNICIPAL:
//                    return "redirect:/applicant/municipal/list";
//                case CITYCORPORATION:
//                    return "redirect:/applicant/cityCorporation/list";
//                case BGMEA:
//                    return "redirect:/applicant/bgmea/list";
//                case BKMEA:
//                    return "redirect:/applicant/bkmea/list";
//                default:
//                    return null;
//            }
            return "redirect:/applicant/summary/edit/" + applicantForm.getId();
        } catch (Exception e) {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @RequestMapping(value = "/getFile/{nid}/{fileName}/{fileType}", produces
            = {
                MediaType.MULTIPART_FORM_DATA_VALUE
            })
    @ResponseBody
    public FileSystemResource getFile(
            @PathVariable("nid") String nid,
            @PathVariable("fileType") String fileType,
            @PathVariable("fileName") String fileName) {
        System.out.println("file is " + FILE_CREATION_PATH + nid + "/" + fileType + "/" + fileName);
        File f = new File(FILE_CREATION_PATH + nid + "/" + fileType);
        File[] entries = f.listFiles();
        for (File s : entries) {
            if (s.getName().equals(fileName)) {
                return new FileSystemResource(s);
            }
        }

        return null;
    }

    @RequestMapping(value = "/allowRejectedApplicants")
    public String getAllowRejectedApplicantsPage(Model model) {
        System.out.println("model = " + model);
        return "allowRejectedApplicant";
    }

    @RequestMapping(value = "/allowRejectedApplicants", method = RequestMethod.POST)
    public String allowRejectedApplicants(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        String nid = request.getParameter("nid");
        try {
            this.applicantService.allowRejectedApplicants(nid);
        } catch (ExceptionWrapper ew) {
            throw ew;
        }
        System.out.println("send message = ");
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        model.addAttribute("message", controllerMessage);
        return "allowRejectedApplicant";
    }

    @RequestMapping(value = "/get-age", method = RequestMethod.GET)
    @ResponseBody
    public String getApplicantAge(@RequestParam(value = "dob", required = false) String dob
    ) {

        String[] dob_year_mont_day = dob.split("-");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate dob_date = LocalDate.parse(dob, dateTimeFormatter);

        LocalDate currentDate = LocalDate.now();

        int actual = AgeCalculator.calculateAge(dob_date, currentDate);

        return actual + "";
    }
}
