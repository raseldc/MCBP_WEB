/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.controller;

import com.wfp.lmmis.applicant.editor.BeneficiaryEditor;
import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.beneficiary.model.BeneficiaryView;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.grievance.model.Grievance;
import com.wfp.lmmis.grievance.model.GrievanceListData;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.grievance.service.GrievanceService;
import com.wfp.lmmis.grievance.service.GrievanceStatusService;
import com.wfp.lmmis.grievance.service.GrievanceTypeService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.utility.ApplicationConstants;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

//import static org.hibernate.annotations.common.util.impl.loggerFactory.logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Philip
 */
@Controller
public class GrievanceController {

    //private static final logger logger = //logger.getlogger(GrievanceController.class);
    @Autowired
    private GrievanceService grievanceService;

    @Autowired
    private GrievanceTypeService grievanceTypeService;

    @Autowired
    private GrievanceStatusService grievanceStatusService;

    @Autowired
    private BeneficiaryEditor beneficiaryEditor;

    @Autowired
    private BeneficiaryService beneficiaryService;

    Localizer localizer = Localizer.getBrowserLocalizer();
    //private final logger logger = //logger.getlogger(GrievanceController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Beneficiary.class, this.beneficiaryEditor);
    }

    @RequestMapping(value = "/grievance/list")
    public String showGrievanceList(Model model, HttpServletRequest request) throws ExceptionWrapper {
        mapGrievanceTypeName(model);
        mapGrievanceStatusName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("searchParameterForm", loadSearchParameterForm(request));
        return "grievanceList";
    }

    @RequestMapping(value = "/grievance/union/list")
    public String showGrievanceUnionList(Model model, HttpServletRequest request) throws ExceptionWrapper {
        mapGrievanceTypeName(model);
        mapGrievanceStatusName(model);
        CommonUtility.mapDivisionName(model);
        GrievanceSearchParameterForm searchParameterForm = loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.UNION);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "grievanceListUnion";
    }

    @RequestMapping(value = "/grievance/municipal/list")
    public String showGrievanceMunicipalList(Model model, HttpServletRequest request) throws ExceptionWrapper {
        mapGrievanceTypeName(model);
        mapGrievanceStatusName(model);
        CommonUtility.mapDivisionName(model);
        GrievanceSearchParameterForm searchParameterForm = loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.MUNICIPAL);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "grievanceListMunicipal";
    }

    @RequestMapping(value = "/grievance/cityCorporation/list")
    public String showGrievanceCityCorporationList(Model model, HttpServletRequest request) throws ExceptionWrapper {
        mapGrievanceTypeName(model);
        mapGrievanceStatusName(model);
        CommonUtility.mapDivisionName(model);
        GrievanceSearchParameterForm searchParameterForm = loadSearchParameterForm(request);
        searchParameterForm.setApplicantType(ApplicantType.CITYCORPORATION);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "grievanceListCityCorporation";
    }

    @RequestMapping(value = "/grievance/bgmea/list")
    public String showGrievanceBgmeaList(Model model, HttpServletRequest request) throws ExceptionWrapper {
        mapGrievanceTypeName(model);
        mapGrievanceStatusName(model);
        GrievanceSearchParameterForm searchParameterForm = loadSearchParameterForm(request);
        CommonUtility.mapBGMEAFactoryName(model);
        searchParameterForm.setApplicantType(ApplicantType.BGMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "grievanceListBgmea";
    }

    @RequestMapping(value = "/grievance/bkmea/list")
    public String showGrievanceBkmeaList(Model model, HttpServletRequest request) throws ExceptionWrapper {
        mapGrievanceTypeName(model);
        mapGrievanceStatusName(model);
        CommonUtility.mapDivisionName(model);
        GrievanceSearchParameterForm searchParameterForm = loadSearchParameterForm(request);
        CommonUtility.mapBKMEAFactoryName(model);
        searchParameterForm.setApplicantType(ApplicantType.BKMEA);
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "grievanceListBkmea";
    }

    public static GrievanceSearchParameterForm loadSearchParameterForm(HttpServletRequest request) {
        GrievanceSearchParameterForm searchParameterForm = new GrievanceSearchParameterForm(); // default all false

        UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");
        if (loggedUser.getUnion() != null) {
            searchParameterForm.setIsUnionAvailable(true);
            searchParameterForm.setUnion(loggedUser.getUnion());
            searchParameterForm.setIsUpazilaAvailable(true);
            searchParameterForm.setUpazila(loggedUser.getUpazila());
            searchParameterForm.setIsDistrictAvailable(true);
            searchParameterForm.setDistrict(loggedUser.getDistrict());
            searchParameterForm.setIsDivisionAvailable(true);
            searchParameterForm.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getUpazila() != null) {
            searchParameterForm.setIsUpazilaAvailable(true);
            searchParameterForm.setUpazila(loggedUser.getUpazila());
            searchParameterForm.setIsDistrictAvailable(true);
            searchParameterForm.setDistrict(loggedUser.getDistrict());
            searchParameterForm.setIsDivisionAvailable(true);
            searchParameterForm.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDistrict() != null) {
            searchParameterForm.setIsDistrictAvailable(true);
            searchParameterForm.setDistrict(loggedUser.getDistrict());
            searchParameterForm.setIsDivisionAvailable(true);
            searchParameterForm.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDivision() != null) {
            searchParameterForm.setIsDivisionAvailable(true);
            searchParameterForm.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getBgmeaFactory() != null) {
            searchParameterForm.setIsBgmeaFactoryAvailable(true);
            searchParameterForm.setBgmeaFactory(loggedUser.getBgmeaFactory());
        }
        if (loggedUser.getBkmeaFactory() != null) {
            searchParameterForm.setIsBkmeaFactoryAvailable(true);
            searchParameterForm.setBkmeaFactory(loggedUser.getBkmeaFactory());
        }
        return searchParameterForm;
    }

    @RequestMapping(value = "/grievance/list", method = RequestMethod.POST)
    @ResponseBody
    public void showGrievanceList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {

            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            ApplicantType applicantType = null;
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String unionId = request.getParameter("unionId");
            String bgmeaFactoryId = request.getParameter("bgmeaFactoryId");
            String bkmeaFactoryId = request.getParameter("bkmeaFactoryId");
            if (request.getParameter("applicantType") != null) {
                applicantType = (ApplicantType.valueOf(request.getParameter("applicantType")));
            }

            Map parameter = new HashMap();
            parameter.put("nid", (String) request.getParameter("nid").trim());
            parameter.put("grievanceType", !request.getParameter("grievanceType").equals("") ? Integer.valueOf(request.getParameter("grievanceType")) : null);
            parameter.put("grievanceStatus", !request.getParameter("grievanceStatus").equals("") ? Integer.valueOf(request.getParameter("grievanceStatus")) : null);
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("locale", LocaleContextHolder.getLocale());
            parameter.put("applicantType", applicantType);
            parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
            parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);

            List<Object> resultList = grievanceService.getGrievanceListBySearchParameter(parameter, beginIndex, pageSize);
            List<GrievanceListData> grievanceListData = (List<GrievanceListData>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);
            Locale locale = LocaleContextHolder.getLocale();
            for (GrievanceListData grievance : grievanceListData) {
                JSONArray ja = new JSONArray();
                ja.put(grievance.getBeneficiaryName());
                if (locale.getLanguage().equals("bn")) {
                    ja.put(CommonUtility.getNumberInBangla(grievance.getNid().toString()));
                } else {
                    ja.put(grievance.getNid());
                }
                ja.put(grievance.getGrievanceType());
                ja.put(grievance.getDescription());
                ja.put(grievance.getComments());
                ja.put(grievance.getGrievanceStatus());
                String editText = localizer.getLocalizedText("edit", LocaleContextHolder.getLocale());
                ja.put("<a href=\"" + request.getContextPath() + "/grievance/edit/" + grievance.getGrievanceId() + "\" title=\"" + editText + "\"><span class=\"glyphicon glyphicon-edit\"></span></a>");
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

    @RequestMapping(value = "/grievance/beneficiaryList", method = RequestMethod.GET)
    public String getBeneficiaryListForChangeStatus(Model model, HttpServletRequest request) {
        CommonUtility.mapAllFiscalYearName(model);
        SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
        String defaultSchemeShortName = ((UserDetail) request.getSession().getAttribute("userDetail")).getScheme().getShortName();
        if ("LMA".equals(defaultSchemeShortName)) {
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
        } else {
            if (searchParameterForm.getDivision() == null) {
                CommonUtility.mapDivisionName(model);
            }
        }
        model.addAttribute("searchParameterForm", searchParameterForm);
        return "newGrievanceBeneficiaryList";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/grievance/union/beneficiaryList", method = RequestMethod.GET)
    public String getBeneficiaryUnionListForChangeStatus(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.UNION);
            if (searchParameterForm.getDivision() == null) {
                CommonUtility.mapDivisionName(model);
            }
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "newGrievanceBeneficiaryListUnion";
    }

    @RequestMapping(value = "/grievance/municipal/beneficiaryList", method = RequestMethod.GET)
    public String getBeneficiaryMunicipalListForChangeStatus(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.MUNICIPAL);
            if (searchParameterForm.getDivision() == null) {
                CommonUtility.mapDivisionName(model);
            }
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "newGrievanceBeneficiaryListMunicipal";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/grievance/cityCorporation/beneficiaryList", method = RequestMethod.GET)
    public String getBeneficiaryCCListForChangeStatus(Model model, HttpServletRequest request) {
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
        return "newGrievanceBeneficiaryListCityCorporation";
    }

    @RequestMapping(value = "/grievance/bgmea/beneficiaryList", method = RequestMethod.GET)
    public String getBeneficiaryBgmeaListForChangeStatus(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BGMEA);
            CommonUtility.mapBGMEAFactoryName(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "newGrievanceBeneficiaryListBgmea";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/grievance/bkmea/beneficiaryList", method = RequestMethod.GET)
    public String getBeneficiaryBkmeaListForChangeStatus(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BKMEA);
            CommonUtility.mapBKMEAFactoryName(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "newGrievanceBeneficiaryListBkmea";
    }

    @RequestMapping(value = "/grievance/beneficiaryList", method = RequestMethod.POST)
    @ResponseBody
    public void showBeneficiaryListForCreateGrievance(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {

            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            ApplicantType applicantType = null;
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String unionId = request.getParameter("unionId");

            String bgmeaFactoryId = request.getParameter("bgmeaFactoryId");
            String bkmeaFactoryId = request.getParameter("bkmeaFactoryId");
            if (request.getParameter("applicantType") != null) {
                applicantType = (ApplicantType.valueOf(request.getParameter("applicantType")));
            }

            Map parameter = new HashMap();
            parameter.put("fiscalYearId", Integer.valueOf(request.getParameter("fiscalYear")));
            parameter.put("applicantId", request.getParameter("applicantId").trim());
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("isActive", 0);
            parameter.put("applicantType", applicantType);

            parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
            parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);
//            parameter.put("regularUser", Integer.valueOf(request.getParameter("regularUser")));
//            parameter.put("bgmeaUser", Integer.valueOf(request.getParameter("bgmeaUser")));
//            parameter.put("bkmeaUser", Integer.valueOf(request.getParameter("bkmeaUser")));

            List<Object> resultList = beneficiaryService.getBeneficiaryListBySearchParameter(parameter, beginIndex, pageSize);
            List<BeneficiaryView> beneficiaryList = (List<BeneficiaryView>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);
            Locale locale = LocaleContextHolder.getLocale();
            for (BeneficiaryView beneficiary : beneficiaryList) {
                JSONArray ja = new JSONArray();
                ja.put(beneficiary.getFullNameInBangla());
                ja.put(beneficiary.getFullNameInEnglish());
                ja.put("en".equals(locale.getLanguage()) ? beneficiary.getNid() : CommonUtility.getNumberInBangla(beneficiary.getNid().toString()));
                String dob = CalendarUtility.getDateString(beneficiary.getDateOfBirth(), ApplicationConstants.DATE_FORMAT);
                ja.put("en".equals(locale.getLanguage()) ? dob : CommonUtility.getNumberInBangla(dob));
                String mobileNo = beneficiary.getMobileNo() != null ? "0" + beneficiary.getMobileNo() : "";
                ja.put("en".equals(locale.getLanguage()) ? mobileNo : CommonUtility.getNumberInBangla(mobileNo));
                String viewDetails = localizer.getLocalizedText("viewDetails", LocaleContextHolder.getLocale());
                String createGrievanceText = localizer.getLocalizedText("createGrievance", LocaleContextHolder.getLocale());
                ja.put("<a href=#" + " onclick=loadBeneficiary(" + beneficiary.getId() + ") title=\"" + viewDetails + "\" ><span class=\"glyphicon glyphicon-eye-open\"></span></a>");
                ja.put("<a href=\"" + request.getContextPath() + "/newGrievance/create/" + beneficiary.getId() + "\" title=\"" + createGrievanceText + "\"><span class=\"glyphicon glyphicon-edit\"></span></a>");
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

    @RequestMapping(value = "/newGrievance/create/{beneficiaryId}", method = RequestMethod.GET)
    public String createNewGrievance(@PathVariable("beneficiaryId") Integer beneficiaryId, @ModelAttribute Grievance grievance, Model model) throws ExceptionWrapper {
        try {
            Beneficiary beneficiary = this.beneficiaryService.getBeneficiary(beneficiaryId);
            if (beneficiary == null) {
                String beneficiaryNotFoundText = localizer.getLocalizedText("beneficiaryNotFound", LocaleContextHolder.getLocale());
                throw new ExceptionWrapper(beneficiaryNotFoundText);
            }
            model.addAttribute("beneficiary", beneficiary);
            Locale loc = LocaleContextHolder.getLocale();
            if (loc.getLanguage().equals("bn")) {
                model.addAttribute("nid", CommonUtility.getNumberInBangla(beneficiary.getNid().toString()));
            } else {
                model.addAttribute("nid", beneficiary.getNid());
            }
            model.addAttribute("actionType", "create");
            model.addAttribute("grievanceStatus", this.grievanceStatusService.getGrievanceStatus(1));// NEW Status
            switch (beneficiary.getApplicantType()) {
                case UNION:
                    model.addAttribute("prevUrl", "/grievance/union/beneficiaryList");
                    break;
                case MUNICIPAL:
                    model.addAttribute("prevUrl", "/grievance/municipal/beneficiaryList");
                    break;
                case CITYCORPORATION:
                    model.addAttribute("prevUrl", "/grievance/cityCorporation/beneficiaryList");
                    break;
                case BGMEA:
                    model.addAttribute("prevUrl", "/grievance/bgmea/beneficiaryList");
                    break;
                case BKMEA:
                    model.addAttribute("prevUrl", "/grievance/bkmea/beneficiaryList");
                    break;
            }
            mapGrievanceTypeName(model);
            return "newGrievance";
        } catch (ExceptionWrapper e) {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @RequestMapping(value = "/newGrievance/create/{beneficiaryId}", method = RequestMethod.POST)
    public String createNewGrievance(@PathVariable("beneficiaryId") Integer beneficiaryId, @Valid Grievance grievance, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error)
                -> {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors()) {
            return createNewGrievance(beneficiaryId, grievance, model);
        }
        try {
            grievance.setCreatedBy((User) session.getAttribute("user"));
            grievance.setCreationDate(Calendar.getInstance());
            this.grievanceService.save(grievance);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            Beneficiary beneficiary = beneficiaryService.getBeneficiary(beneficiaryId);
            switch (beneficiary.getApplicantType()) {
                case UNION: {
                    return "redirect:/grievance/union/list";
                }
                case MUNICIPAL: {
                    return "redirect:/grievance/municipal/list";
                }
                case CITYCORPORATION: {
                    return "redirect:/grievance/cityCorporation/list";
                }
                case BGMEA: {
                    return "redirect:/grievance/bgmea/list";
                }
                case BKMEA: {
                    return "redirect:/grievance/bkmea/list";
                }
            }
            return null;
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    private void mapGrievanceTypeName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("grievanceTypeList", this.grievanceTypeService.getActiveGrievanceTypeList());
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("grievanceTypeName", "nameInEnglish");
        } else {
            model.addAttribute("grievanceTypeName", "nameInBangla");
        }
    }

    private void mapGrievanceStatusName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("grievanceStatusList", this.grievanceStatusService.getUnResolvedGrievanceStatusList());
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("grievanceStatusName", "nameInEnglish");
        } else {
            model.addAttribute("grievanceStatusName", "nameInBangla");
        }
    }

    @RequestMapping(value = "/grievance/edit/{id}", method = RequestMethod.GET)
    public String editGrievance(@PathVariable("id") Integer id, @ModelAttribute Grievance grievance, Model model, boolean grievanceLoadRequired) throws Exception {
        if (!grievanceLoadRequired) {
            grievance = this.grievanceService.getGrievance(id);
            grievance.setComment(null);
            grievance.setActionDate(null);
        }
        model.addAttribute("dateTimeFormat", "Y-m-d H:i");
        model.addAttribute("grievance", grievance);
        switch (grievance.getBeneficiary().getApplicantType()) {
            case UNION:
                model.addAttribute("prevUrl", "/grievance/union/list");
                break;
            case MUNICIPAL:
                model.addAttribute("prevUrl", "/grievance/municipal/list");
                break;
            case CITYCORPORATION:
                model.addAttribute("prevUrl", "/grievance/cityCorporation/list");
                break;
            case BGMEA:
                model.addAttribute("prevUrl", "/grievance/bgmea/list");
                break;
            case BKMEA:
                model.addAttribute("prevUrl", "/grievance/bkmea/list");
                break;
        }
        mapGrievanceStatusName(model, grievance);
        return "grievance";
    }

    private void mapGrievanceStatusName(Model model, Grievance grievance) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("grievanceStatusList", this.grievanceStatusService.getGrievanceStatusList(grievance.getGrievanceStatus().getId()));
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("grievanceStatusName", "nameInEnglish");
        } else {
            model.addAttribute("grievanceStatusName", "nameInBangla");
        }
    }

    /**
     *
     * @param pk
     * @param grievance
     * @param bindingResult
     * @param session
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/grievance/edit/{id}", method = RequestMethod.POST)
    public String editGrievance(@PathVariable("id") Integer pk, @Valid @ModelAttribute Grievance grievance, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("find me");
        try {
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach((error)
                    -> {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            });
            if (bindingResult.hasErrors()) {
                return editGrievance(pk, grievance, model, true);
            }
            grievance.setActionBy((User) session.getAttribute("user"));
            grievance.setModifiedBy((User) session.getAttribute("user"));
            grievance.setModificationDate(Calendar.getInstance());
            this.grievanceService.edit(grievance);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            switch (grievance.getBeneficiary().getApplicantType()) {
                case UNION: {
                    return "redirect:/grievance/union/list";
                }
                case MUNICIPAL: {
                    return "redirect:/grievance/municipal/list";
                }
                case CITYCORPORATION: {
                    return "redirect:/grievance/cityCorporation/list";
                }
                case BGMEA: {
                    return "redirect:/grievance/bgmea/list";
                }
                case BKMEA: {
                    return "redirect:/grievance/bkmea/list";
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            //logger.infoer(e.getMessage());
        }
        return "grievance";
    }

    @RequestMapping(value = "/grievance/delete/{id}", method = RequestMethod.POST)
    public String deleteGrievance(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        Grievance grievance = this.grievanceService.getGrievance(id);
        grievance.setModifiedBy((User) session.getAttribute("user"));
        grievance.setModificationDate(Calendar.getInstance());
        this.grievanceService.delete(grievance);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/grievance/list";
    }
}
