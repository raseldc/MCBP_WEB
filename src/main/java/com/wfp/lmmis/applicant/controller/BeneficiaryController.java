/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.controller;

import static com.wfp.lmmis.applicant.controller.ApplicantController.FILE_CREATION_PATH;
import com.wfp.lmmis.applicant.forms.AddressForm;
import com.wfp.lmmis.applicant.forms.AttachmentInfoForm;
import com.wfp.lmmis.applicant.forms.BiometricInfoForm;
import com.wfp.lmmis.applicant.forms.PaymentInfoForm;
import com.wfp.lmmis.applicant.forms.PersonalInfoForm;
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantAttachment;
import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.beneficiary.model.BeneficiaryAttachment;
import com.wfp.lmmis.beneficiary.model.BeneficiaryBiometricInfo;
import com.wfp.lmmis.beneficiary.model.BeneficiaryForm;
import com.wfp.lmmis.beneficiary.model.BeneficiarySocioEconomicInfo;
import com.wfp.lmmis.beneficiary.model.BeneficiaryView;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.AttachmentType;
import com.wfp.lmmis.enums.UserType;
import static com.wfp.lmmis.enums.UserType.BGMEA;
import static com.wfp.lmmis.enums.UserType.BKMEA;
import static com.wfp.lmmis.enums.UserType.DIRECTORATE;
import static com.wfp.lmmis.enums.UserType.MINISTRY;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.masterdata.service.AccountTypeService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.VillageService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.types.BeneficiaryDeactivationReasons;
import com.wfp.lmmis.types.BeneficiaryStatus;
import com.wfp.lmmis.types.Gender;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

/**
 *
 * @author Philip
 */
@Controller
public class BeneficiaryController {

    //private final org.apache.log4j.logger logger = org.apache.log4j.//logger.getlogger(BeneficiaryController.class);
    @Autowired
    private BeneficiaryService beneficiaryService;
    @Autowired
    private BeneficiaryService beneficiaryService1;

    @Autowired
    private AccountTypeService accountTypeService;
    @Autowired
    private SchemeService schemeService;

    @Autowired
    private PersonalInfoForm personalInfoForm;
    @Autowired
    private AddressForm addressForm;
    @Autowired
    private PaymentInfoForm paymentInfoForm;
    @Autowired
    private BiometricInfoForm biometricInfoForm;
    @Autowired
    private AttachmentInfoForm attachmentInfoForm;
    @Autowired
    private VillageService villageService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/checkBeneficiaryByID/{benID}", method = RequestMethod.GET)
    public @ResponseBody
    BeneficiaryResponseData getBenefificiaryByID(@PathVariable("benID") String benID, Locale locale) {
        Beneficiary beneficiary = this.beneficiaryService.getBeneficiaryByID(benID);
        if (locale.getLanguage().equals("en")) {
            if (beneficiary != null) {
                return new BeneficiaryResponseData(beneficiary.getFullNameInEnglish(), beneficiary.getScheme().getNameInEnglish());
            } else {
                return new BeneficiaryResponseData(null, null);
            }
        } else {
            if (beneficiary != null) {
                return new BeneficiaryResponseData(beneficiary.getFullNameInBangla(), beneficiary.getScheme().getNameInBangla());
            } else {
                return new BeneficiaryResponseData(null, null);
            }
        }

    }

    public boolean saveBeneficiary(Applicant applicant, HttpSession session, HttpServletRequest request) {
        try {
            Beneficiary beneficiary = new Beneficiary();
            beneficiary.setId(null);
            beneficiary.setScheme(applicant.getScheme());
//                                    System.out.println("fiscal year 2 "+ applicant.getFiscalYear().getNameInEnglish());
            beneficiary.setFiscalYear(applicant.getFiscalYear());
            beneficiary.setEnrollmentDate(applicant.getCreationDate());
//            beneficiary.setBatch(applicant.getBatch());
            beneficiary.setFullNameInBangla(applicant.getFullNameInBangla());
            beneficiary.setFullNameInEnglish(applicant.getFullNameInEnglish());
            beneficiary.setNickName(applicant.getNickName());
            beneficiary.setFatherName(applicant.getFatherName());
            beneficiary.setMotherName(applicant.getMotherName());
            beneficiary.setSpouseName(applicant.getSpouseName());
            beneficiary.setDateOfBirth(applicant.getDateOfBirth());
            beneficiary.setBirthPlace(applicant.getBirthPlace());
            beneficiary.setEducationLevelEnum(applicant.getEducationLevelEnum());
            beneficiary.setReligionEnum(applicant.getReligionEnum());
            beneficiary.setMaritalInfoEnum(applicant.getMaritalInfoEnum());
            beneficiary.setGender(applicant.getGender());
            beneficiary.setBloodGroup(applicant.getBloodGroup());
            beneficiary.setMobileNo(applicant.getMobileNo());
//            beneficiary.setEmail(applicant.getEmail());
            beneficiary.setOccupation(applicant.getOccupation());
            beneficiary.setNid(applicant.getNid());

            beneficiary.setApplicantType(applicant.getApplicantType());
            beneficiary.setFactory(applicant.getFactory());

            beneficiary.setPresentAddressLine1(applicant.getPresentAddressLine1());
            beneficiary.setPresentAddressLine2(applicant.getPresentAddressLine2());
            beneficiary.setPresentDivision(applicant.getPresentDivision());
            beneficiary.setPresentDistrict(applicant.getPresentDistrict());
            beneficiary.setPresentUpazila(applicant.getPresentUpazila());
            beneficiary.setPresentUnion(applicant.getPresentUnion());
            beneficiary.setPresentWardNo(applicant.getPresentWardNo());
            beneficiary.setPresentPostCode(applicant.getPresentPostCode());
            beneficiary.setPresentVillage(applicant.getPresentVillage());

            beneficiary.setPermanentAddressLine1(applicant.getPermanentAddressLine1());
            beneficiary.setPermanentAddressLine2(applicant.getPermanentAddressLine2());
            beneficiary.setPermanentDivision(applicant.getPermanentDivision());
            beneficiary.setPermanentDistrict(applicant.getPermanentDistrict());
            beneficiary.setPermanentUpazila(applicant.getPermanentUpazila());
            beneficiary.setPermanentUnion(applicant.getPermanentUnion());
            beneficiary.setPermanentWardNo(applicant.getPermanentWardNo());
            beneficiary.setPermanentPostCode(applicant.getPermanentPostCode());
            beneficiary.setPermanentVillage(applicant.getPermanentVillage());

            BeneficiaryBiometricInfo beneficiaryBiometricInfo = new BeneficiaryBiometricInfo();
            if (applicant.getApplicantBiometricInfo() != null) {
                if (applicant.getApplicantBiometricInfo().getPhotoData() != null) {
                    beneficiaryBiometricInfo.setPhotoData(applicant.getApplicantBiometricInfo().getPhotoData());
                    beneficiaryBiometricInfo.setPhotoPath(applicant.getApplicantBiometricInfo().getPhotoPath());
                }
                if (applicant.getApplicantBiometricInfo().getSignatureData() != null) {
                    beneficiaryBiometricInfo.setSignatureData(applicant.getApplicantBiometricInfo().getSignatureData());
                    beneficiaryBiometricInfo.setSignaturePath(applicant.getApplicantBiometricInfo().getSignaturePath());
                }
            }
            beneficiaryBiometricInfo.setBeneficiary(beneficiary);
            beneficiary.setBeneficiaryBiometricInfo(beneficiaryBiometricInfo);

            List<BeneficiaryAttachment> beneficiaryAttachments = new ArrayList<BeneficiaryAttachment>();
            for (ApplicantAttachment applicantAttachment : applicant.getApplicantAttachmentList()) {
                BeneficiaryAttachment beneficiaryAttachment = new BeneficiaryAttachment();
                beneficiaryAttachment.setAttachmentName(applicantAttachment.getAttachmentName());
                beneficiaryAttachment.setFileName(applicantAttachment.getFileName());
                beneficiaryAttachment.setFilePath(applicantAttachment.getFilePath());
                beneficiaryAttachment.setAttachmentType(applicantAttachment.getAttachmentType());
                beneficiaryAttachment.setBeneficiary(beneficiary);
                beneficiaryAttachments.add(beneficiaryAttachment);
            }
            beneficiary.setBeneficiaryAttachmentList(beneficiaryAttachments);

            beneficiary.setBeneficiaryStatus(BeneficiaryStatus.ACTIVE); // Check it

            BeneficiarySocioEconomicInfo beneficiarySocioEconomicInfo = new BeneficiarySocioEconomicInfo();
            if (applicant.getApplicantSocioEconomicInfo() != null) {
                //common
                beneficiarySocioEconomicInfo.setMonthlyIncome(applicant.getApplicantSocioEconomicInfo().getMonthlyIncome());
                beneficiarySocioEconomicInfo.setDisability(applicant.getApplicantSocioEconomicInfo().getDisability());
                beneficiarySocioEconomicInfo.sethHWallMadeOf(applicant.getApplicantSocioEconomicInfo().gethHWallMadeOf());
                beneficiarySocioEconomicInfo.sethASElectricity(applicant.getApplicantSocioEconomicInfo().gethASElectricity());
                beneficiarySocioEconomicInfo.sethASElectricFan(applicant.getApplicantSocioEconomicInfo().gethASElectricFan());
                //rural
                beneficiarySocioEconomicInfo.setLandSizeRural(applicant.getApplicantSocioEconomicInfo().getLandSizeRural());
                beneficiarySocioEconomicInfo.setOccupationRural(applicant.getApplicantSocioEconomicInfo().getOccupationRural());
                beneficiarySocioEconomicInfo.sethASLatrineRural(applicant.getApplicantSocioEconomicInfo().gethASLatrineRural());
                beneficiarySocioEconomicInfo.sethASTubewellRural(applicant.getApplicantSocioEconomicInfo().gethASTubewellRural());
                //urban
                beneficiarySocioEconomicInfo.setHasResidenceUrban(applicant.getApplicantSocioEconomicInfo().getHasResidenceUrban());
                beneficiarySocioEconomicInfo.setOccupationUrban(applicant.getApplicantSocioEconomicInfo().getOccupationUrban());
                beneficiarySocioEconomicInfo.sethASKitchenUrban(applicant.getApplicantSocioEconomicInfo().gethASKitchenUrban());
                beneficiarySocioEconomicInfo.sethASTelivisionUrban(applicant.getApplicantSocioEconomicInfo().gethASTelivisionUrban());
            }
            beneficiary.setConceptionDuration(applicant.getConceptionDuration());
            beneficiary.setConceptionTerm(applicant.getConceptionTerm());

            beneficiarySocioEconomicInfo.setBeneficiary(beneficiary);
            beneficiary.setBeneficiarySocioEconomicInfo(beneficiarySocioEconomicInfo);
            beneficiary.setPaymentType(applicant.getPaymentType());
            beneficiary.setBank(applicant.getBank());
            beneficiary.setBranch(applicant.getBranch());
            beneficiary.setMobileBankingProvider(applicant.getMobileBankingProvider());
            beneficiary.setPostOfficeBranch(applicant.getPostOfficeBranch());
            beneficiary.setAccountType(applicant.getAccountType());
            beneficiary.setAccountName(applicant.getAccountName());
            beneficiary.setAccountNo(applicant.getAccountNo());

            beneficiary.setCreatedBy((User) session.getAttribute("user"));
            beneficiary.setCreationDate(Calendar.getInstance());
            beneficiary.setIsLMMISExist(applicant.getIsLMMISExist());
//                        System.out.println("fiscal year 3 "+ applicant.getFiscalYear().getNameInEnglish());
//                        System.out.println("fiscal year 4 "+ beneficiary.getFiscalYear().getNameInEnglish());
            this.beneficiaryService.save(beneficiary);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @RequestMapping(value = "/beneficiary/list", method = RequestMethod.GET)
    public String getBeneficiaryList(Model model, HttpServletRequest request) {
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
        return "beneficiaryList";
    }

    @RequestMapping(value = "/beneficiary/union/list", method = RequestMethod.GET)
    public String getRuralBeneficiaryList(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.UNION);
            if (searchParameterForm.getDivision() == null) {
                CommonUtility.mapDivisionName(model);
            }
            CommonUtility.getWardNoList(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "beneficiaryListUnion";
    }

    @RequestMapping(value = "/beneficiary/municipal/list", method = RequestMethod.GET)
    public String getMunicipalBeneficiaryList(Model model, HttpServletRequest request) {
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
        return "beneficiaryListMunicipal";
    }

    @RequestMapping(value = "/beneficiary/cityCorporation/list", method = RequestMethod.GET)
    public String getCityCorporationBeneficiaryList(Model model, HttpServletRequest request) {
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
        return "beneficiaryListCityCorporation";
    }

    @RequestMapping(value = "/beneficiary/bgmea/list", method = RequestMethod.GET)
    public String getBGMEABeneficiaryList(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BGMEA);
            CommonUtility.mapBGMEAFactoryName(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "beneficiaryListBgmea";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/beneficiary/bkmea/list", method = RequestMethod.GET)
    public String getBKMEABeneficiaryList(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BKMEA);
            CommonUtility.mapBKMEAFactoryName(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "beneficiaryListBkmea";
    }

    @RequestMapping(value = "/beneficiary/list", method = RequestMethod.POST)
    @ResponseBody
    public void showBeneficiaryList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
        try {
            String fiscalYear = null, divisionId = null, districtId = null, upazilaId = null, unionId = null, wardNo = null, villageId = null, bgmeaFactoryId = null, bkmeaFactoryId = null;
            ApplicantType applicantType = null;
            String applicantId = null, mobileNo = null, accountNo = null;
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));

            if (request.getParameter("fiscalYear") != null) {
                fiscalYear = request.getParameter("fiscalYear");
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

            if (request.getParameter("wardNo") != null) {
                wardNo = request.getParameter("wardNo");
            }
            if (request.getParameter("villageId") != null) {
                villageId = request.getParameter("villageId");
            }
            if (request.getParameter("bgmeaFactoryId") != null) {
                bgmeaFactoryId = request.getParameter("bgmeaFactoryId");
            }
            if (request.getParameter("bkmeaFactoryId") != null) {
                bkmeaFactoryId = request.getParameter("bkmeaFactoryId");
            }
            if (request.getParameter("applicantType") != null) {
                applicantType = (ApplicantType.valueOf(request.getParameter("applicantType")));
            }
            if (request.getParameter("applicantId") != null) {
                applicantId = request.getParameter("applicantId").trim();
            }
            if (request.getParameter("mobileNo") != null) {
                mobileNo = request.getParameter("mobileNo").trim();
            }
            if (request.getParameter("accountNo") != null) {
                accountNo = request.getParameter("accountNo").trim();
            }
            Calendar startDate = null, endDate = null;
            if (request.getParameter("startDate") != null && !"".equals(request.getParameter("startDate"))) {
                startDate = DateUtilities.stringToCalendar(request.getParameter("startDate"));
            }
            if (request.getParameter("endDate") != null && !"".equals(request.getParameter("endDate"))) {
                endDate = DateUtilities.stringToCalendar(request.getParameter("endDate"));
            }

            String status = request.getParameter("status");
            Map parameter = new HashMap();
            parameter.put("schemeId", ((UserDetail) request.getSession().getAttribute("userDetail")).getSchemeId());
//            parameter.put("fiscalYearId", Integer.valueOf(request.getParameter("fiscalYear")));
            parameter.put("applicantId", applicantId);
            parameter.put("mobileNo", mobileNo);
            parameter.put("accountNo", accountNo);
            parameter.put("fiscalYearId", fiscalYear != null && !"".equals(fiscalYear) ? Integer.valueOf(fiscalYear) : null);
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("wardNo", wardNo != null && !"".equals(wardNo) ? Integer.valueOf(wardNo) : null);
            parameter.put("villageId", villageId != null && !"".equals(villageId) ? Integer.valueOf(villageId) : null);
            parameter.put("isActive", 1);
            parameter.put("applicantType", applicantType);
            parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
            parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);
            parameter.put("startDate", startDate);
            parameter.put("endDate", endDate);
            parameter.put("status", status != null && !"".equals(status) ? Integer.valueOf(status) : null);
            Locale locale = LocaleContextHolder.getLocale();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            List<Object> resultList = beneficiaryService1.getBeneficiaryListBySearchParameter(parameter, beginIndex, pageSize);
            List<BeneficiaryView> beneficiaryList = (List<BeneficiaryView>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);
            String referer = request.getHeader("referer");
            referer = referer.replaceAll("/", "-");
            for (BeneficiaryView beneficiary : beneficiaryList) {
                JSONArray ja = new JSONArray();
                ja.put(beneficiary.getFullNameInBangla());
                ja.put(beneficiary.getFullNameInEnglish());
                ja.put("en".equals(locale.getLanguage()) ? beneficiary.getNid().toString() : CommonUtility.getNumberInBangla(beneficiary.getNid().toString()));
                String dob = CalendarUtility.getDateString(beneficiary.getDateOfBirth(), ApplicationConstants.DATE_FORMAT);
                ja.put("en".equals(locale.getLanguage()) ? dob : CommonUtility.getNumberInBangla(dob));
                String mobileNo1 = beneficiary.getMobileNo() != null ? "0" + beneficiary.getMobileNo() : "";
                ja.put("en".equals(locale.getLanguage()) ? mobileNo1 : CommonUtility.getNumberInBangla(mobileNo1));
                String accountNo1 = beneficiary.getAccountNo() != null ? beneficiary.getAccountNo() : "";
                ja.put("en".equals(locale.getLanguage()) ? accountNo1 : CommonUtility.getNumberInBangla(accountNo1));
                ja.put("en".equals(locale.getLanguage()) ? beneficiary.getFullNameEn() : beneficiary.getFullNameBn());
                ja.put("en".equals(locale.getLanguage()) ? formatter.format(beneficiary.getCreationDate().getTime()) : CommonUtility.getNumberInBangla(formatter.format(beneficiary.getCreationDate().getTime())));
                String edit = localizer.getLocalizedText("edit", LocaleContextHolder.getLocale());
                String profile = localizer.getLocalizedText("profile", LocaleContextHolder.getLocale());
                String viewDetails = localizer.getLocalizedText("viewDetails", LocaleContextHolder.getLocale());
                ja.put("<a href=#" + " onclick=loadBeneficiary(" + beneficiary.getId() + ") title=\"" + viewDetails + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>");
//                ja.put("<a href=\"" + request.getContextPath() + "/beneficiary/beneficiaryForm/" + beneficiary.getId() + "\" title=\"" + edit + "\"><span class=\"glyphicon glyphicon-edit\"></span></a>");
//                ja.put("<a href=\"" + request.getContextPath() + "/beneficiary/edit/" + beneficiary.getId() + "\" title=\"" + edit + "\"><span class=\"glyphicon glyphicon-edit\"></span></a>");
                ja.put("<a href=\"" + request.getContextPath() + "/beneficiary/profile/" + beneficiary.getId() + "\" title=\"" + profile + "\"><span class=\"glyphicon glyphicon-edit\"></span></a>");

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

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/beneficiary/changeStatusList", method = RequestMethod.GET)
    public String getBeneficiaryListForChangeStatus(Model model, HttpServletRequest request) {
        CommonUtility.mapDivisionName(model);
//        CommonUtility.mapSchemeName(model);
        CommonUtility.mapAllFiscalYearName(model);
        String defaultSchemeShortName = ((UserDetail) request.getSession().getAttribute("userDetail")).getScheme().getShortName();
        if ("LMA".equals(defaultSchemeShortName)) {
            UserType userType = ((UserDetail) request.getSession().getAttribute("userDetail")).getUserType();
            if (null != userType) {
                switch (userType) {
                    case DIRECTORATE:
                    case MINISTRY:
                        CommonUtility.mapBGMEAFactoryName(model);
                        CommonUtility.mapBKMEAFactoryName(model);
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
        }
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "beneficiaryChangeStatusList";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/beneficiaryChangeStatus/union/list", method = RequestMethod.GET)
    public String getBeneficiaryListForChangeStatusUnion(Model model, HttpServletRequest request) {
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
        return "beneficiaryChangeStatusListUnion";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/beneficiaryChangeStatus/municipal/list", method = RequestMethod.GET)
    public String getBeneficiaryListForChangeStatusMunicipal(Model model, HttpServletRequest request) {
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
        return "beneficiaryChangeStatusListMunicipal";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/beneficiaryChangeStatus/cityCorporation/list", method = RequestMethod.GET)
    public String getBeneficiaryListForChangeStatusCityCorporation(Model model, HttpServletRequest request) {
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
        return "beneficiaryChangeStatusListCityCorporation";
    }

    @RequestMapping(value = "/beneficiaryChangeStatus/bgmea/list", method = RequestMethod.GET)
    public String getBeneficiaryListForChangeStatusBGMEA(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BGMEA);
            CommonUtility.mapBGMEAFactoryName(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "beneficiaryChangeStatusListBgmea";
    }

    @RequestMapping(value = "/beneficiaryChangeStatus/bkmea/list", method = RequestMethod.GET)
    public String getBeneficiaryListForChangeStatusBKMEA(Model model, HttpServletRequest request) {
        try {
            CommonUtility.mapAllFiscalYearName(model);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            searchParameterForm.setApplicantType(ApplicantType.BKMEA);
            CommonUtility.mapBKMEAFactoryName(model);
            model.addAttribute("searchParameterForm", searchParameterForm);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "beneficiaryChangeStatusListBkmea";
    }

    @RequestMapping(value = "/beneficiary/changeStatusList", method = RequestMethod.POST)
    @ResponseBody
    public void showBeneficiaryListForChangeStatus(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {

            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String unionId = request.getParameter("unionId");
            String bgmeaFactoryId = request.getParameter("bgmeaFactoryId");
            String bkmeaFactoryId = request.getParameter("bkmeaFactoryId");
            String status = request.getParameter("status");
            ApplicantType applicantType = null;
            String applicantId = null;
            if (request.getParameter("applicantType") != null) {
                applicantType = (ApplicantType.valueOf(request.getParameter("applicantType")));
            }
            if (request.getParameter("applicantId") != null) {
                applicantId = request.getParameter("applicantId").trim();
            }

            Map parameter = new HashMap();
            parameter.put("schemeId", ((UserDetail) request.getSession().getAttribute("userDetail")).getSchemeId());
            parameter.put("fiscalYearId", Integer.valueOf(request.getParameter("fiscalYear")));
            parameter.put("mobileNo", request.getParameter("mobileNo"));
            parameter.put("accountNo", request.getParameter("accountNo"));
            parameter.put("applicantId", applicantId);
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("status", status != null && !"".equals(status) ? Integer.valueOf(status) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);
            parameter.put("applicantType", applicantType);
            parameter.put("isActive", 0);

            parameter.put("bgmeaFactoryId", bgmeaFactoryId != null && !"".equals(bgmeaFactoryId) ? Integer.valueOf(bgmeaFactoryId) : null);
            parameter.put("bkmeaFactoryId", bkmeaFactoryId != null && !"".equals(bkmeaFactoryId) ? Integer.valueOf(bkmeaFactoryId) : null);
            System.out.println("bgmeaFactoryId " + bgmeaFactoryId);
            System.out.println("bkmeaFactoryId " + bkmeaFactoryId);
            System.out.println("divisionId " + bgmeaFactoryId);

            Locale locale = LocaleContextHolder.getLocale();

            List<Object> resultList = beneficiaryService1.getBeneficiaryListBySearchParameter(parameter, beginIndex, pageSize);
            List<BeneficiaryView> beneficiaryList = (List<BeneficiaryView>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);
            for (BeneficiaryView beneficiary : beneficiaryList) {
                JSONArray ja = new JSONArray();
                ja.put(beneficiary.getFullNameInBangla());
                ja.put(beneficiary.getFullNameInEnglish());
                ja.put("en".equals(locale.getLanguage()) ? beneficiary.getNid() : CommonUtility.getNumberInBangla(beneficiary.getNid().toString()));
                String dob = CalendarUtility.getDateString(beneficiary.getDateOfBirth(), ApplicationConstants.DATE_FORMAT);
                ja.put("en".equals(locale.getLanguage()) ? dob : CommonUtility.getNumberInBangla(dob));
                String mobileNo = beneficiary.getMobileNo() != null ? "0" + beneficiary.getMobileNo() : "";
                ja.put("en".equals(locale.getLanguage()) ? mobileNo : CommonUtility.getNumberInBangla(mobileNo));
                String accountNo = beneficiary.getMobileNo() != null ? "0" + beneficiary.getAccountNo() : "";
                ja.put("en".equals(locale.getLanguage()) ? accountNo : CommonUtility.getNumberInBangla(accountNo));
                ja.put("en".equals(locale.getLanguage()) ? BeneficiaryStatus.values()[beneficiary.getBeneficiaryStatus()].getDisplayName()
                        : BeneficiaryStatus.values()[beneficiary.getBeneficiaryStatus()].getDisplayNameBn());
                String viewDetails = localizer.getLocalizedText("viewDetails", LocaleContextHolder.getLocale());
                String changeStatusText = localizer.getLocalizedText("changeStatus", LocaleContextHolder.getLocale());
                ja.put("<a href=#" + " onclick=loadBeneficiary(" + beneficiary.getId() + ") title=\"" + viewDetails + "\" ><span class=\"glyphicon glyphicon-eye-open\"></span></a>");
                ja.put("<a href=\"" + request.getContextPath() + "/beneficiary/changeStatus/" + beneficiary.getId() + "\" title=\"" + changeStatusText + "\"><span class=\"glyphicon glyphicon-edit\"></span></a>");
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

    @RequestMapping(value = "/beneficiary/changeStatus/{id}", method = RequestMethod.GET)
    public String getChangeStatus(@PathVariable("id") Integer id, Model model) {
        try {
            Beneficiary beneficiary = this.beneficiaryService.getBeneficiary(id);
            model.addAttribute("beneficiary", beneficiary);
            model.addAttribute("beneficiaryStatusList", BeneficiaryStatus.values());
            model.addAttribute("beneficiaryDeactivationReasonsList", BeneficiaryDeactivationReasons.values());
            model.addAttribute("imagePath", FILE_CREATION_PATH);
            model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");
            model.addAttribute("certificateImagePath", ApplicationConstants.FILE_CREATION_PATH_BENEFICIARY_CERTIFICATE + "\\" + id + "\\");
            model.addAttribute("dateFormat", "dd-mm-yy");
            model.addAttribute("applicantType", beneficiary.getApplicantType());
            if ("bn".equals(LocaleContextHolder.getLocale().getLanguage())) {
                model.addAttribute("benId", CommonUtility.getNumberInBangla(beneficiary.getNid().toString()));
            } else {
                model.addAttribute("benId", beneficiary.getNid());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "changeBeneficiaryStatus";
    }

    @RequestMapping(value = "/beneficiary/changeStatus/{id}", method = RequestMethod.POST)
    public String updateBeneficiaryStatus(@PathVariable("id") Integer id, Beneficiary beneficiary,
            @RequestParam("certificateProfilePhoto") MultipartFile multipartFile, BindingResult bindingResult,
            HttpSession session, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String saveButton = request.getParameter("save");
        ApplicantType applicantType = null;
        try {
            Beneficiary beneficiaryOld = this.beneficiaryService.getBeneficiary(id);
            applicantType = beneficiaryOld.getApplicantType();
            if ("revert".equals(saveButton)) {
                beneficiaryOld.setBeneficiaryDeactivationReasons(null);
                beneficiaryOld.setBeneficiaryStatus(BeneficiaryStatus.ACTIVE);
                beneficiaryOld.setIncidentDate(null);
                beneficiaryOld.setDeactivationComment(null);
                beneficiaryOld.setAttachmentPath(null);
                this.beneficiaryService.edit(beneficiaryOld);
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, "Beneficiary Status " + ApplicationConstants.CHANGE_MESSAGE);
                redirectAttributes.addFlashAttribute("saveMsg", controllerMessage);
            } else {

                String fileName = multipartFile.getOriginalFilename();
                String fileCreationPath = request.getServletContext().getRealPath(ApplicationConstants.FILE_CREATION_PATH_BENEFICIARY_CERTIFICATE) + "\\" + id + "\\";
                String saveDirectory = fileCreationPath;
                new File(saveDirectory).mkdirs();
                if (!"".equalsIgnoreCase(fileName)) {
                    try {
                        fileName = saveDirectory + fileName;
                        multipartFile.transferTo(new File(fileName));
                    } catch (IOException ex) {
                        // java.util.logging.//logger.getlogger(BeneficiaryController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalStateException ex) {
                        //java.util.logging.//logger.getlogger(BeneficiaryController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                beneficiaryOld.setBeneficiaryDeactivationReasons(beneficiary.getBeneficiaryDeactivationReasons());
                if (beneficiary.getBeneficiaryDeactivationReasons().equals(BeneficiaryDeactivationReasons.MISCARRIAGE)
                        || beneficiary.getBeneficiaryDeactivationReasons().equals(BeneficiaryDeactivationReasons.STILLBIRTH)) {
                    beneficiaryOld.setBeneficiaryStatus(BeneficiaryStatus.TEMPORARILY_ACTIVE);
                } else if (beneficiary.getBeneficiaryDeactivationReasons().equals(BeneficiaryDeactivationReasons.MOTHER_DIED_WITHIN_TWO_YEARS)
                        || beneficiary.getBeneficiaryDeactivationReasons().equals(BeneficiaryDeactivationReasons.WRONG_INFORMATION_PROVIDED)
                        || beneficiary.getBeneficiaryDeactivationReasons().equals(BeneficiaryDeactivationReasons.OTHER)) {
                    beneficiaryOld.setBeneficiaryStatus(BeneficiaryStatus.INACTIVE);
                }
                beneficiaryOld.setIncidentDate(beneficiary.getIncidentDate());
                beneficiaryOld.setDeactivationComment(beneficiary.getDeactivationComment());
                beneficiaryOld.setModifiedBy((User) session.getAttribute("user"));
                beneficiaryOld.setModificationDate(Calendar.getInstance());
                beneficiaryOld.setAttachmentPath(multipartFile.getOriginalFilename());
                this.beneficiaryService.edit(beneficiaryOld);
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, "Beneficiary Status " + ApplicationConstants.CHANGE_MESSAGE);
                redirectAttributes.addFlashAttribute("saveMsg", controllerMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        String returnUrl = "";
        switch (applicantType) {
            case UNION:
                returnUrl = "redirect:/beneficiaryChangeStatus/union/list";
                break;
            case MUNICIPAL:
                returnUrl = "redirect:/beneficiaryChangeStatus/municipal/list";
                break;
            case CITYCORPORATION:
                returnUrl = "redirect:/beneficiaryChangeStatus/cityCorporation/list";
                break;
            case BGMEA:
                returnUrl = "redirect:/beneficiaryChangeStatus/bgmea/list";
                break;
            case BKMEA:
                returnUrl = "redirect:/beneficiaryChangeStatus/bkmea/list";
                break;
        }
        return returnUrl;
    }

    /**
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/beneficiary/viewBeneficiary/{id}", method = RequestMethod.GET)
    public String viewApplicantForm(@PathVariable("id") Integer id, Model model) {
        try {
            Beneficiary beneficiary = this.beneficiaryService.getBeneficiary(id);
            String dob = CalendarUtility.getDateString(beneficiary.getDateOfBirth(), ApplicationConstants.DATE_FORMAT);
            if (LocaleContextHolder.getLocale().getLanguage().equals("bn")) {
                model.addAttribute("nid", (CommonUtility.getNumberInBangla(beneficiary.getNid().toString())));
                model.addAttribute("dob", CommonUtility.getNumberInBangla(dob));
                model.addAttribute("mobileNo", beneficiary.getMobileNo() != null ? '০' + CommonUtility.getNumberInBangla(beneficiary.getMobileNo().toString()) : "");
                model.addAttribute("presentWardNo", CommonUtility.getNumberInBangla(beneficiary.getPresentWardNo()));
                model.addAttribute("presentPostCode", CommonUtility.getNumberInBangla(beneficiary.getPresentPostCode()));
                model.addAttribute("permanentWardNo", CommonUtility.getNumberInBangla(beneficiary.getPermanentWardNo()));
                model.addAttribute("permanentPostCode", CommonUtility.getNumberInBangla(beneficiary.getPresentPostCode()));
                model.addAttribute("accountNo", CommonUtility.getNumberInBangla(beneficiary.getAccountNo()));
//                model.addAttribute("monthlyIncome", CommonUtility.getNumberInBangla(beneficiary.getBeneficiarySocioEconomicInfo().getMonthlyIncome().toString()));
//                model.addAttribute("age", CommonUtility.getNumberInBangla(beneficiary.getBeneficiarySocioEconomicInfo().getAge().toString()));
//                model.addAttribute("conceptionDuration", CommonUtility.getNumberInBangla(beneficiary.getBeneficiarySocioEconomicInfo().getConceptionDuration().toString()));
            } else {
                model.addAttribute("nid", beneficiary.getNid().toString());
                model.addAttribute("dob", dob);
                model.addAttribute("mobileNo", beneficiary.getMobileNo() != null ? '0' + beneficiary.getMobileNo().toString() : "");
                model.addAttribute("presentWardNo", beneficiary.getPresentWardNo());
                model.addAttribute("presentPostCode", beneficiary.getPresentPostCode());
                model.addAttribute("permanentWardNo", beneficiary.getPermanentWardNo());
                model.addAttribute("permanentPostCode", beneficiary.getPresentPostCode());
                model.addAttribute("accountNo", beneficiary.getAccountNo());
//                model.addAttribute("monthlyIncome", beneficiary.getBeneficiarySocioEconomicInfo().getMonthlyIncome().toString());
//                model.addAttribute("age", beneficiary.getBeneficiarySocioEconomicInfo().getAge().toString());
//                model.addAttribute("conceptionDuration", beneficiary.getBeneficiarySocioEconomicInfo().getConceptionDuration().toString());

            }
            model.addAttribute("beneficiary", beneficiary);
            model.addAttribute("imagePath", FILE_CREATION_PATH);
            model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "viewBeneficiary";
    }

    private Beneficiary saveBeneficiary(HttpServletRequest request) {
        // Make this function modular
        HttpSession session = request.getSession();

        Beneficiary beneficiary;// = new Applicant();
        if (this.personalInfoForm.getId() != null) {
            beneficiary = this.beneficiaryService.getBeneficiary(this.personalInfoForm.getId());
        } else {
            beneficiary = new Beneficiary();
        }

        try {

            beneficiary.setScheme(this.personalInfoForm.getScheme());
            beneficiary.setFiscalYear(this.personalInfoForm.getFiscalYear());
            beneficiary.setFullNameInBangla(this.personalInfoForm.getFullNameInBangla());
            beneficiary.setFullNameInEnglish(this.personalInfoForm.getFullNameInEnglish());
            beneficiary.setNickName(this.personalInfoForm.getNickName());
            beneficiary.setFatherName(this.personalInfoForm.getFatherName());
            beneficiary.setMotherName(this.personalInfoForm.getMotherName());
            beneficiary.setSpouseName(this.personalInfoForm.getSpouseName());
            beneficiary.setDateOfBirth(this.personalInfoForm.getDateOfBirth());
            beneficiary.setBirthPlace(this.personalInfoForm.getBirthPlace());
            beneficiary.setEducationLevelEnum(this.personalInfoForm.getEducationLevelEnum());
            beneficiary.setReligionEnum(this.personalInfoForm.getReligionEnum());
            beneficiary.setMaritalInfoEnum(this.personalInfoForm.getMaritalInfoEnum());
            beneficiary.setGender(this.personalInfoForm.getGender());
            beneficiary.setBloodGroup(this.personalInfoForm.getBloodGroup());

            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
                if (this.personalInfoForm.getMobileNo() != null && !this.personalInfoForm.getMobileNo().isEmpty()) {
                    beneficiary.setMobileNo(Integer.parseInt(this.personalInfoForm.getMobileNo()));
                }
                beneficiary.setNid(new BigInteger(this.personalInfoForm.getNid()));
                beneficiary.setPresentWardNo(this.addressForm.getPresentWardNo());
                beneficiary.setPresentPostCode(this.addressForm.getPresentPostCode());
                beneficiary.setPermanentWardNo(this.addressForm.getPresentWardNo());
                beneficiary.setPermanentPostCode(this.addressForm.getPresentPostCode());
                beneficiary.setAccountNo(this.paymentInfoForm.getAccountNo());
            } else {
                if (this.personalInfoForm.getMobileNo() != null && !this.personalInfoForm.getMobileNo().isEmpty()) {
                    beneficiary.setMobileNo(Integer.parseInt(CommonUtility.getNumberInEnglish(this.personalInfoForm.getMobileNo())));
                }
                beneficiary.setNid(new BigInteger(CommonUtility.getNumberInEnglish(this.personalInfoForm.getNid()).toString()));

                if (this.addressForm.getPresentWardNo() != null) {
                    beneficiary.setPresentWardNo(CommonUtility.getNumberInEnglish(this.addressForm.getPresentWardNo()));
                } else {
                    beneficiary.setPresentWardNo("");
                }
                beneficiary.setPresentPostCode(CommonUtility.getNumberInEnglish(this.addressForm.getPresentPostCode()));
                if (this.addressForm.getPermanentWardNo() != null) {
                    beneficiary.setPermanentWardNo(CommonUtility.getNumberInEnglish(this.addressForm.getPermanentWardNo()));
                } else {
                    beneficiary.setPermanentWardNo("");
                }
                beneficiary.setPermanentPostCode(CommonUtility.getNumberInEnglish(this.addressForm.getPermanentPostCode()));
                beneficiary.setAccountNo(CommonUtility.getNumberInEnglish(this.paymentInfoForm.getAccountNo()));
            }

//            beneficiary.setOccupation(this.personalInfoForm.getEmail());
//            beneficiary.setEmail(this.personalInfoForm.getEmail());
            beneficiary.setNid(this.personalInfoForm.getNid() != null ? new BigInteger(this.personalInfoForm.getNid()) : null);

            beneficiary.setApplicantType(this.addressForm.getApplicantType());
            if (null != this.addressForm.getApplicantType()) {
                switch (this.addressForm.getApplicantType()) {
                    case REGULAR:
                        beneficiary.setFactory(null);
                        break;
                    case BGMEA:
                        beneficiary.setFactory(this.addressForm.getBgmeaFactory());
                        break;
                    case BKMEA:
                        beneficiary.setFactory(this.addressForm.getBkmeaFactory());
                    default:
                        break;
                }
            }
            beneficiary.setPresentAddressLine1(this.addressForm.getPresentAddressLine1());
            beneficiary.setPresentAddressLine2(this.addressForm.getPresentAddressLine2());
            beneficiary.setPresentDivision(this.addressForm.getPresentDivision());
            beneficiary.setPresentDistrict(this.addressForm.getPresentDistrict());
            beneficiary.setPresentUpazila(this.addressForm.getPresentUpazila());
            beneficiary.setPresentUnion(this.addressForm.getPresentUnion());

            beneficiary.setPermanentAddressLine1(this.addressForm.getPermanentAddressLine1());
            beneficiary.setPermanentAddressLine2(this.addressForm.getPermanentAddressLine2());
            beneficiary.setPermanentDivision(this.addressForm.getPermanentDivision());
            beneficiary.setPermanentDistrict(this.addressForm.getPermanentDistrict());
            beneficiary.setPermanentUpazila(this.addressForm.getPermanentUpazila());
            beneficiary.setPermanentUnion(this.addressForm.getPermanentUnion());

//            for (BeneficiarySchemeAttributeValue beneficiarySchemeAttributeValue : this.socioEconomicForm.getBeneficiarySchemeAttributeValueList())
//            {
//                if (beneficiarySchemeAttributeValue.getSchemeAttribute().getAttributeType().equals(AttributeType.TEXT))
//                {
//                    if ("en".equals(LocaleContextHolder.getLocale().getLanguage()))
//                    {
//                        beneficiarySchemeAttributeValue.setSchemeAttributeValueInBangla(CommonUtility.getNumberInBangla(beneficiarySchemeAttributeValue.getSchemeAttributeValueInEnglish()));
//                    }
//                    else
//                    {
//                        beneficiarySchemeAttributeValue.setSchemeAttributeValueInEnglish(CommonUtility.getNumberInEnglish(beneficiarySchemeAttributeValue.getSchemeAttributeValueInBangla()));
//                    }
//                }
//
//                beneficiarySchemeAttributeValue.setBeneficiary(beneficiary);
//
//                beneficiarySchemeAttributeValue.setCreatedBy((User) session.getAttribute("user"));
//                beneficiarySchemeAttributeValue.setCreationDate(Calendar.getInstance());
//            }
//            beneficiary.setSchemeAttributeValueList(this.socioEconomicForm.getBeneficiarySchemeAttributeValueList());
            beneficiary.setBeneficiaryStatus(BeneficiaryStatus.ACTIVE);

            if (this.paymentInfoForm.getPaymentType() != null) {
                beneficiary.setPaymentType(this.paymentInfoForm.getPaymentType());
                switch (this.paymentInfoForm.getPaymentType()) {
                    case BANKING:
                        beneficiary.setBank(this.paymentInfoForm.getBank());
                        beneficiary.setBranch(this.paymentInfoForm.getBranch());
                        beneficiary.setMobileBankingProvider(null);
                        beneficiary.setPostOfficeBranch(null);
                        break;
                    case MOBILEBANKING:
                        beneficiary.setBank(null);
                        beneficiary.setBranch(null);
                        beneficiary.setMobileBankingProvider(this.paymentInfoForm.getMobileBankingProvider());
                        beneficiary.setPostOfficeBranch(null);
                        break;
                    case POSTOFFICE:
                        beneficiary.setBank(null);
                        beneficiary.setBranch(null);
                        beneficiary.setMobileBankingProvider(null);
                        beneficiary.setPostOfficeBranch(this.paymentInfoForm.getPostOfficeBranch());
                        break;
                }
            }
            beneficiary.setAccountType(this.paymentInfoForm.getAccountType());
            beneficiary.setAccountName(this.paymentInfoForm.getAccountName());

            BeneficiaryBiometricInfo beneficiaryBiometricInfo = beneficiary.getBeneficiaryBiometricInfo();

            if (beneficiaryBiometricInfo == null) {
                beneficiaryBiometricInfo = new BeneficiaryBiometricInfo();
            }
            beneficiaryBiometricInfo.setPhotoPath(this.biometricInfoForm.getProfilePhotoPath());
            beneficiaryBiometricInfo.setSignaturePath(this.biometricInfoForm.getSignaturePath());
            if (this.biometricInfoForm.getProfilePhoto() != null) {
                beneficiaryBiometricInfo.setPhotoData(this.biometricInfoForm.getProfilePhoto().getBytes());
            }
            if (this.biometricInfoForm.getSignature() != null) {
                beneficiaryBiometricInfo.setSignatureData(this.biometricInfoForm.getSignature().getBytes());
            }
            beneficiaryBiometricInfo.setBeneficiary(beneficiary);
//            beneficiaryBiometricInfo.setCreatedBy((User) session.getAttribute("user"));
//            beneficiaryBiometricInfo.setCreationDate(Calendar.getInstance());

            beneficiary.setBeneficiaryBiometricInfo(beneficiaryBiometricInfo);

            List<BeneficiaryAttachment> beneficiaryAttachmentList = new ArrayList<>();
            if (null != this.attachmentInfoForm.getMultipartFileList()) {
                for (MultipartFile multipartFile : this.attachmentInfoForm.getMultipartFileList()) {
                    BeneficiaryAttachment attachmentInfo = new BeneficiaryAttachment();
                    attachmentInfo.setAttachmentName(multipartFile.getOriginalFilename());
                    attachmentInfo.setFileName(multipartFile.getOriginalFilename());
                    attachmentInfo.setFilePath(request.getServletContext().getRealPath(FILE_CREATION_PATH) + "/Temp/");
                    attachmentInfo.setBeneficiary(beneficiary);
//                    attachmentInfo.setCreatedBy((User) session.getAttribute("user"));
//                    attachmentInfo.setCreationDate(Calendar.getInstance());
                    beneficiaryAttachmentList.add(attachmentInfo);
                }
                beneficiary.setBeneficiaryAttachmentList(beneficiaryAttachmentList);
            }

            beneficiary.setCreatedBy((User) session.getAttribute("user"));
            beneficiary.setCreationDate(Calendar.getInstance());

            if (this.personalInfoForm.getId() != null) {
                beneficiary.setId(this.personalInfoForm.getId());
                this.beneficiaryService.edit(beneficiary);
            } else {
                this.beneficiaryService.save(beneficiary);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return beneficiary;
    }

    @RequestMapping(value = "/beneficiary/create")
    public String createBeneficiary(@ModelAttribute BeneficiaryForm beneficiaryForm, Model model, HttpSession session) {
        UserDetail loggedUser = (UserDetail) session.getAttribute("userDetail");
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
//        CommonUtility.mapBatchName(model, loggedUser.getSchemeId());
        model.addAttribute("actionType", "create");
        if (loggedUser.getUnion() != null) {
            beneficiaryForm.setIsUnionAvailable(true);
            beneficiaryForm.setPresentUnion(loggedUser.getUnion());
            beneficiaryForm.setIsUpazilaAvailable(true);
            beneficiaryForm.setPresentUpazila(loggedUser.getUpazila());
            beneficiaryForm.setIsDistrictAvailable(true);
            beneficiaryForm.setPresentDistrict(loggedUser.getDistrict());
            beneficiaryForm.setIsDivisionAvailable(true);
            beneficiaryForm.setPresentDivision(loggedUser.getDivision());
        }
        if (loggedUser.getUpazila() != null) {
            beneficiaryForm.setIsUpazilaAvailable(true);
            beneficiaryForm.setPresentUpazila(loggedUser.getUpazila());
            beneficiaryForm.setIsDistrictAvailable(true);
            beneficiaryForm.setPresentDistrict(loggedUser.getDistrict());
            beneficiaryForm.setIsDivisionAvailable(true);
            beneficiaryForm.setPresentDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDistrict() != null) {
            beneficiaryForm.setIsDistrictAvailable(true);
            beneficiaryForm.setPresentDistrict(loggedUser.getDistrict());
            beneficiaryForm.setIsDivisionAvailable(true);
            beneficiaryForm.setPresentDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDivision() != null) {
            beneficiaryForm.setIsDivisionAvailable(true);
            beneficiaryForm.setPresentDivision(loggedUser.getDivision());
        }
        //for LMA 
        String defaultSchemeShortName = ((UserDetail) session.getAttribute("userDetail")).getScheme().getShortName();
        if (defaultSchemeShortName.equals("LMA")) {
//            CommonUtility.mapOccupationName(model);
            UserType userType = ((UserDetail) session.getAttribute("userDetail")).getUserType();
            if (null != userType) {
                switch (userType) {
                    case MINISTRY:
                    case DIRECTORATE:
                        CommonUtility.mapBGMEAFactoryName(model);
                        CommonUtility.mapBKMEAFactoryName(model);
                        beneficiaryForm.setApplicantType(ApplicantType.REGULAR);
                        break;
                    case FIELD:
                        beneficiaryForm.setApplicantType(ApplicantType.REGULAR);
                        break;
                    case BGMEA:
                        CommonUtility.mapBGMEAFactoryName(model);
                        beneficiaryForm.setApplicantType(ApplicantType.BGMEA);
                        break;
                    case BKMEA:
                        CommonUtility.mapBKMEAFactoryName(model);
                        beneficiaryForm.setApplicantType(ApplicantType.BKMEA);
                        break;
                }
            }
        } else {
            beneficiaryForm.setApplicantType(ApplicantType.REGULAR);
        }
        return "beneficiaryInformation";
    }

    @RequestMapping(value = "/beneficiary/create", method = RequestMethod.POST)
    public String createBeneficiary(@Valid BeneficiaryForm beneficiaryForm, BindingResult bindingResult, Model model, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        if (bindingResult.hasErrors()) {
            return createBeneficiary(beneficiaryForm, model, session);
        }
        try {
            Beneficiary beneficiary = new Beneficiary();

            beneficiary.setCreatedBy((User) session.getAttribute("user"));
            beneficiary.setCreationDate(Calendar.getInstance());
            beneficiary.setScheme(((UserDetail) session.getAttribute("userDetail")).getScheme());
            beneficiary.setFiscalYear(beneficiaryForm.getFiscalYear());
            beneficiary.setBeneficiaryStatus(BeneficiaryStatus.ACTIVE);
//            beneficiary.setBatch(beneficiaryForm.getBatch());

            createBasicInfo(beneficiary, beneficiaryForm);
            createAddressInfo(beneficiary, beneficiaryForm);
            createSocioEconomicInfo(beneficiary, beneficiaryForm);
            createHealthStatusInfo(beneficiary, beneficiaryForm);
            createPaymentInfo(beneficiary, beneficiaryForm);
            createBiometricInfo(beneficiary, beneficiaryForm);
            createAttachmentInfo(request, beneficiary, beneficiaryForm);
            createIsLMMisExist(beneficiary);
            this.beneficiaryService.save(beneficiary);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/beneficiary/list";
        } catch (Exception e) {
            throw new ExceptionWrapper("Error creating beneficiary" + e.getMessage());
        }
    }

    private void createIsLMMisExist(Beneficiary beneficiary) {
        int otherMisSchemId = beneficiaryService.getOtherMISchemeId(beneficiary.getNid().toString());

        beneficiary.setIsLMMISExist(otherMisSchemId);
    }

    private void createBasicInfo(Beneficiary beneficiary, BeneficiaryForm beneficiaryForm) {
        beneficiary.setFullNameInBangla(beneficiaryForm.getFullNameInBangla());
        beneficiary.setFullNameInEnglish(beneficiaryForm.getFullNameInEnglish());
        beneficiary.setFatherName(beneficiaryForm.getFatherName());
        beneficiary.setMotherName(beneficiaryForm.getMotherName());
        beneficiary.setSpouseName(beneficiaryForm.getSpouseName());
        beneficiary.setNickName(beneficiaryForm.getNickName());
        beneficiary.setDateOfBirth(beneficiaryForm.getDateOfBirth());
        beneficiary.setGender(Gender.FEMALE);
        beneficiary.setBirthPlace(beneficiaryForm.getBirthPlace());
        beneficiary.setEducationLevelEnum(beneficiaryForm.getEducationLevelEnum());
        beneficiary.setReligionEnum(beneficiaryForm.getReligionEnum());
        beneficiary.setMaritalInfoEnum(beneficiaryForm.getMaritalInfoEnum());
        if (beneficiaryForm.getMobileNo() != null && !beneficiaryForm.getMobileNo().isEmpty()) {
            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
                beneficiary.setMobileNo(Integer.parseInt(beneficiaryForm.getMobileNo()));
            } else {
                beneficiary.setMobileNo(Integer.parseInt(CommonUtility.getNumberInEnglish(beneficiaryForm.getMobileNo())));
            }
        }
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            beneficiary.setNid(new BigInteger(beneficiaryForm.getNid()));
        } else {
            beneficiary.setNid(new BigInteger(CommonUtility.getNumberInEnglish(beneficiaryForm.getNid())));
        }
        beneficiary.setOccupation(beneficiaryForm.getOccupation());
//        beneficiary.setEmail(beneficiaryForm.getEmail());
        beneficiary.setBloodGroup(beneficiaryForm.getBloodGroup());
    }

    private void createAddressInfo(Beneficiary beneficiary, BeneficiaryForm beneficiaryForm) {
        beneficiary.setApplicantType(beneficiaryForm.getApplicantType());
        if (null != beneficiary.getApplicantType()) {
            switch (beneficiary.getApplicantType()) {
                case UNION:
                    beneficiary.setFactory(null);
                    beneficiary.setScheme(schemeService.getScheme(new Integer("1")));
                    break;
                case MUNICIPAL:
                    beneficiary.setFactory(null);
                    beneficiary.setScheme(schemeService.getScheme(new Integer("3")));
                    break;
                case CITYCORPORATION:
                    beneficiary.setFactory(null);
                    beneficiary.setScheme(schemeService.getScheme(new Integer("3")));
                    break;
                case BGMEA:
                    beneficiary.setFactory(beneficiaryForm.getBgmeaFactory());
                    beneficiary.setScheme(schemeService.getScheme(new Integer("3")));
                    break;
                case BKMEA:
                    beneficiary.setFactory(beneficiaryForm.getBkmeaFactory());
                    beneficiary.setScheme(schemeService.getScheme(new Integer("3")));
                    break;
            }
        }
        beneficiary.setPresentAddressLine1(beneficiaryForm.getPresentAddressLine1());
        beneficiary.setPresentAddressLine2(beneficiaryForm.getPresentAddressLine2());
        beneficiary.setPresentDivision(beneficiaryForm.getPresentDivision());
        beneficiary.setPresentDistrict(beneficiaryForm.getPresentDistrict());
        beneficiary.setPresentUpazila(beneficiaryForm.getPresentUpazila());
        beneficiary.setPresentUnion(beneficiaryForm.getPresentUnion());
        if (beneficiaryForm.getPresentVillage() != null && beneficiaryForm.getPresentVillage().getId() != null) {
            beneficiary.setPresentVillage(beneficiaryForm.getPresentVillage());
            beneficiary.setPresentAddressLine1((villageService.getVillage(beneficiaryForm.getPresentVillage().getId())).getNameInBangla());
        } else if (beneficiaryForm.getPresentAddressLine1() == null) {
            beneficiary.setPresentAddressLine1(";");
        }
        beneficiary.setPermanentAddressLine1(beneficiaryForm.getPermanentAddressLine1());
        beneficiary.setPermanentAddressLine2(beneficiaryForm.getPermanentAddressLine2());
        beneficiary.setPermanentDivision(beneficiaryForm.getPermanentDivision());
        beneficiary.setPermanentDistrict(beneficiaryForm.getPermanentDistrict());
        beneficiary.setPermanentUpazila(beneficiaryForm.getPermanentUpazila());
        beneficiary.setPermanentUnion(beneficiaryForm.getPermanentUnion());
        if (beneficiaryForm.getPermanentVillage() != null && beneficiaryForm.getPermanentVillage().getId() != null) {
            beneficiary.setPermanentVillage(beneficiaryForm.getPermanentVillage());
            beneficiary.setPermanentAddressLine1((villageService.getVillage(beneficiaryForm.getPermanentVillage().getId())).getNameInBangla());
        } else if (beneficiaryForm.getPermanentAddressLine1() == null) {
            beneficiary.setPermanentAddressLine1(";");
        }
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            beneficiary.setPresentWardNo(beneficiaryForm.getPresentWardNo());
            beneficiary.setPresentPostCode(beneficiaryForm.getPresentPostCode());
            beneficiary.setPermanentWardNo(beneficiaryForm.getPermanentWardNo());
            beneficiary.setPermanentPostCode(beneficiaryForm.getPermanentPostCode());
        } else {
            if (beneficiaryForm.getPresentWardNo() != null) {
                beneficiary.setPresentWardNo(CommonUtility.getNumberInEnglish(beneficiaryForm.getPresentWardNo()));
            }
            beneficiary.setPresentPostCode(CommonUtility.getNumberInEnglish(beneficiaryForm.getPresentPostCode()));
            if (beneficiaryForm.getPermanentWardNo() != null) {
                beneficiary.setPermanentWardNo(CommonUtility.getNumberInEnglish(beneficiaryForm.getPermanentWardNo()));
            }
            beneficiary.setPermanentPostCode(CommonUtility.getNumberInEnglish(beneficiaryForm.getPermanentPostCode()));
        }
    }

    private void createSocioEconomicInfo(Beneficiary beneficiary, BeneficiaryForm beneficiaryForm) {
        BeneficiarySocioEconomicInfo beneficiarySocioEconomicInfo = beneficiary.getBeneficiarySocioEconomicInfo() != null ? beneficiary.getBeneficiarySocioEconomicInfo() : new BeneficiarySocioEconomicInfo();
        beneficiarySocioEconomicInfo.setBeneficiary(beneficiary);
        beneficiarySocioEconomicInfo.setMonthlyIncome(beneficiaryForm.getMonthlyIncome());
        beneficiarySocioEconomicInfo.sethHWallMadeOf(beneficiaryForm.gethHWallMadeOf());
        beneficiarySocioEconomicInfo.setDisability(beneficiaryForm.getDisability());
        beneficiarySocioEconomicInfo.sethASElectricity(beneficiaryForm.gethASElectricity());
        beneficiarySocioEconomicInfo.sethASElectricFan(beneficiaryForm.gethASElectricFan());
        if (beneficiaryForm.getApplicantType() == ApplicantType.UNION) {
            beneficiarySocioEconomicInfo.setLandSizeRural(beneficiaryForm.getLandSizeRural());
            beneficiarySocioEconomicInfo.setOccupationRural(beneficiaryForm.getOccupationRural());
            beneficiarySocioEconomicInfo.sethASLatrineRural(beneficiaryForm.gethASLatrineRural());
            beneficiarySocioEconomicInfo.sethASTubewellRural(beneficiaryForm.gethASTubewellRural());
        } else {
            beneficiarySocioEconomicInfo.setHasResidenceUrban(beneficiaryForm.getHasResidenceUrban());
            beneficiarySocioEconomicInfo.setOccupationUrban(beneficiaryForm.getOccupationUrban());
            beneficiarySocioEconomicInfo.sethASKitchenUrban(beneficiaryForm.gethASKitchenUrban());
            beneficiarySocioEconomicInfo.sethASTelivisionUrban(beneficiaryForm.gethASTelivisionUrban());
        }
        beneficiary.setBeneficiarySocioEconomicInfo(beneficiarySocioEconomicInfo);
    }

    private void createHealthStatusInfo(Beneficiary beneficiary, BeneficiaryForm beneficiaryForm) {
        beneficiary.setConceptionTerm(beneficiaryForm.getConceptionTerm());
        beneficiary.setConceptionDuration(beneficiaryForm.getConceptionDuration());
    }

    private void createPaymentInfo(Beneficiary beneficiary, BeneficiaryForm beneficiaryForm) {
        beneficiary.setPaymentType(beneficiaryForm.getPaymentType());
        if (beneficiary.getPaymentType() != null) {
            switch (beneficiary.getPaymentType()) {
                case BANKING:
                    beneficiary.setBank(beneficiaryForm.getBank());
                    beneficiary.setBranch(beneficiaryForm.getBranch());
                    beneficiary.setAccountType(beneficiaryForm.getAccountType());
                    beneficiary.setMobileBankingProvider(null);
                    beneficiary.setPostOfficeBranch(null);
                    break;
                case MOBILEBANKING:
                    beneficiary.setMobileBankingProvider(beneficiaryForm.getMobileBankingProvider());
                    beneficiary.setAccountType(this.accountTypeService.getAccountType(2));
                    beneficiary.setBank(null);
                    beneficiary.setBranch(null);
                    beneficiary.setPostOfficeBranch(null);
                    break;
                case POSTOFFICE:
                    beneficiary.setPostOfficeBranch(beneficiaryForm.getPostOfficeBranch());
                    beneficiary.setAccountType(beneficiaryForm.getAccountTypePO());
                    beneficiary.setBank(null);
                    beneficiary.setBranch(null);
                    beneficiary.setMobileBankingProvider(null);
                    break;
            }
            beneficiary.setAccountName(beneficiaryForm.getAccountName());
            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
                beneficiary.setAccountNo(beneficiaryForm.getAccountNo());
            } else {
                beneficiary.setAccountNo(CommonUtility.getNumberInEnglish(beneficiaryForm.getAccountNo()));
            }
        } else {
            beneficiary.setBank(null);
            beneficiary.setBranch(null);
            beneficiary.setPostOfficeBranch(null);
            beneficiary.setMobileBankingProvider(null);
            beneficiary.setAccountType(null);
            beneficiary.setAccountName(null);
            beneficiary.setAccountNo(null);
        }
    }

    private void createBiometricInfo(Beneficiary beneficiary, BeneficiaryForm beneficiaryForm) {
        try {
            BeneficiaryBiometricInfo beneficiaryBiometricInfo = beneficiary.getBeneficiaryBiometricInfo() != null ? beneficiary.getBeneficiaryBiometricInfo() : new BeneficiaryBiometricInfo();
            beneficiaryBiometricInfo.setBeneficiary(beneficiary);
            if (!beneficiaryForm.getPhoto().getOriginalFilename().equals("")) {
                beneficiaryBiometricInfo.setPhotoPath(beneficiaryForm.getPhoto().getOriginalFilename());
                beneficiaryBiometricInfo.setPhotoData(beneficiaryForm.getPhoto().getBytes());
            }
            if (!beneficiaryForm.getSignature().getOriginalFilename().equals("")) {
                beneficiaryBiometricInfo.setSignaturePath(beneficiaryForm.getSignature().getOriginalFilename());
                beneficiaryBiometricInfo.setSignatureData(beneficiaryForm.getSignature().getBytes());
            }
            beneficiary.setBeneficiaryBiometricInfo(beneficiaryBiometricInfo);
        } catch (IOException ex) {
            //logger.getlogger(BeneficiaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createAttachmentInfo(HttpServletRequest request, Beneficiary beneficiary, BeneficiaryForm beneficiaryForm) {
        String saveDirectory = FILE_CREATION_PATH + beneficiary.getNid() + "/";
        List<MultipartFile> files = beneficiaryForm.getMultipartFileList();
        int i = 0;
        if (null != files && files.size() > 0) {
            Iterator<MultipartFile> iter = files.iterator();

            while (iter.hasNext()) {
                MultipartFile multipartFile = iter.next();
                String fileName = multipartFile.getOriginalFilename();
                if (fileName != null && !fileName.isEmpty()) {
                    try {
                        if (i == 0) {
                            new File(saveDirectory + "anc").mkdirs();
                            multipartFile.transferTo(new File(saveDirectory + "anc/" + fileName));
                        } else {
                            new File(saveDirectory + "others").mkdirs();
                            multipartFile.transferTo(new File(saveDirectory + "others/" + fileName));
                        }
                        i++;
                    } catch (IOException ex) {
                    } catch (IllegalStateException ex) {
                    }
                } else {
                    iter.remove();
                }
            }
        }
        List<BeneficiaryAttachment> beneficiaryAttachmentList = new ArrayList<>();
        if (null != beneficiaryForm.getMultipartFileList()) {
            i = 0;
            for (MultipartFile multipartFile : beneficiaryForm.getMultipartFileList()) {
                BeneficiaryAttachment attachmentInfo = new BeneficiaryAttachment();
                attachmentInfo.setAttachmentName(multipartFile.getOriginalFilename());
                attachmentInfo.setFileName(multipartFile.getOriginalFilename());
                attachmentInfo.setFilePath(FILE_CREATION_PATH);
                if (i == 0) {
                    attachmentInfo.setAttachmentType(AttachmentType.ANCCARD);
                } else {
                    attachmentInfo.setAttachmentType(AttachmentType.OTHERS);
                }
                attachmentInfo.setBeneficiary(beneficiary);
                beneficiaryAttachmentList.add(attachmentInfo);
                i++;
            }
            beneficiary.setBeneficiaryAttachmentList(beneficiaryAttachmentList);
        }
        String removeList = beneficiaryForm.getRemoveList();

        if (removeList != null && !removeList.isEmpty()) {
            List<Integer> removeAttachmentList = new ArrayList<>();
            StringTokenizer stringTokenizer = new StringTokenizer(removeList, ",");
            while (stringTokenizer.hasMoreElements()) {
                removeAttachmentList.add(new Integer(stringTokenizer.nextElement().toString()));
            }
            beneficiary.setAttachmentRemoveList(removeAttachmentList);
        }
    }

    @RequestMapping(value = "/beneficiary/edit/{id}", method = RequestMethod.GET)
    public String editBeneficiaryInformation(@PathVariable("id") Integer id,
            @ModelAttribute BeneficiaryForm beneficiaryForm, Model model, boolean beneficiaryLoadRequired, HttpSession session) {
        try {
            Beneficiary beneficiary = !beneficiaryLoadRequired ? this.beneficiaryService.getBeneficiary(id) : null;
            loadBasicInfo(beneficiaryForm, beneficiary);
            loadAddressInfo(beneficiaryForm, beneficiary);
            loadSocioEconomicInfo(beneficiaryForm, beneficiary);
            loadHealthStatusInfo(beneficiaryForm, beneficiary);
            loadPaymentInfo(beneficiaryForm, beneficiary);
            loadBiometricInfo(beneficiaryForm, beneficiary);
            loadAttachmentInfo(beneficiaryForm, beneficiary);

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
            CommonUtility.mapMonthlyIncome(beneficiary.getApplicantType(), model);
            CommonUtility.mapHHWallMadeOfEnumName(beneficiary.getApplicantType(), model);
//            CommonUtility.mapBatchName(model, userDetail.getSchemeId());
            CommonUtility.getWardNoList(model);
            model.addAttribute("imagePath", FILE_CREATION_PATH);
            model.addAttribute("applicantNid", beneficiary.getNid());
            if (null != beneficiary.getApplicantType()) {
                switch (beneficiary.getApplicantType()) {
                    case BGMEA:
                        model.addAttribute("type", "bgmea");
                        beneficiary.setBgmeaFactory(beneficiary.getFactory());
                        CommonUtility.mapBGMEAFactoryName(model);
                        break;
                    case BKMEA:
                        model.addAttribute("type", "bkmea");
                        CommonUtility.mapBKMEAFactoryName(model);
                        beneficiary.setBkmeaFactory(beneficiary.getFactory());
                        break;
                }
            }
            model.addAttribute("beneficiaryForm", beneficiaryForm);
            switch (beneficiaryForm.getApplicantType()) {
                case UNION:
                    return "beneficiaryFormUnion";
                case MUNICIPAL:
                    return "beneficiaryFormMunicipal";
                case CITYCORPORATION:
                    return "beneficiaryFormCityCorporation";
                case BGMEA:
                    return "beneficiaryFormBgmeaBkmea";
                case BKMEA:
                    return "beneficiaryFormBgmeaBkmea";
                default:
                    return null;
            }
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    private void loadBasicInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setId(beneficiary.getId());
        beneficiaryForm.setScheme(beneficiary.getScheme());
        beneficiaryForm.setFiscalYear(beneficiary.getFiscalYear());
//        beneficiaryForm.setBatch(beneficiary.getBatch());

        beneficiaryForm.setFullNameInBangla(beneficiary.getFullNameInBangla());
        beneficiaryForm.setFullNameInEnglish(beneficiary.getFullNameInEnglish());
        beneficiaryForm.setFatherName(beneficiary.getFatherName());
        beneficiaryForm.setMotherName(beneficiary.getMotherName());
        beneficiaryForm.setSpouseName(beneficiary.getSpouseName());
        beneficiaryForm.setNickName(beneficiary.getNickName());
        beneficiaryForm.setDateOfBirth(beneficiary.getDateOfBirth());
        beneficiaryForm.setGender(Gender.FEMALE);
        beneficiaryForm.setBirthPlace(beneficiary.getBirthPlace());
        beneficiaryForm.setEducationLevelEnum(beneficiary.getEducationLevelEnum());
        beneficiaryForm.setReligionEnum(beneficiary.getReligionEnum());
        beneficiaryForm.setMaritalInfoEnum(beneficiary.getMaritalInfoEnum());

        beneficiaryForm.setIsLMMISExit(beneficiary.getIsLMMISExist() == 0 ? false : true);
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            beneficiaryForm.setNid(beneficiary.getNid().toString());
            beneficiaryForm.setMobileNo(beneficiary.getMobileNo() != null ? ("0" + beneficiary.getMobileNo()) : "");
        } else {

            beneficiaryForm.setMobileNo(beneficiary.getMobileNo() != null ? (CommonUtility.getNumberInBangla("0" + beneficiary.getMobileNo().toString())) : "");
            beneficiaryForm.setNid(CommonUtility.getNumberInBangla(beneficiary.getNid().toString()));
        }
        beneficiaryForm.setOccupation(beneficiary.getOccupation());
//        beneficiaryForm.setEmail(beneficiary.getEmail());
        beneficiaryForm.setBloodGroup(beneficiary.getBloodGroup());
    }

    private void loadAddressInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setApplicantType(beneficiary.getApplicantType());
        if (null != beneficiaryForm.getApplicantType()) {
            switch (beneficiaryForm.getApplicantType()) {
                case REGULAR:
                    //beneficiaryForm.setFactory(null);
                    break;
                case BGMEA:
                    beneficiaryForm.setBgmeaFactory(beneficiary.getFactory());
                    break;
                case BKMEA:
                    beneficiaryForm.setBkmeaFactory(beneficiary.getFactory());
                    break;
            }
        }
        beneficiaryForm.setPresentAddressLine1(beneficiary.getPresentAddressLine1());
        beneficiaryForm.setPresentAddressLine2(beneficiary.getPresentAddressLine2());
        beneficiaryForm.setPresentDivision(beneficiary.getPresentDivision());
        beneficiaryForm.setPresentDistrict(beneficiary.getPresentDistrict());
        beneficiaryForm.setPresentUpazila(beneficiary.getPresentUpazila());
        beneficiaryForm.setPresentUnion(beneficiary.getPresentUnion());
        beneficiaryForm.setPresentVillage(beneficiary.getPresentVillage());

        beneficiaryForm.setPermanentAddressLine1(beneficiary.getPermanentAddressLine1());
        beneficiaryForm.setPermanentAddressLine2(beneficiary.getPermanentAddressLine2());
        beneficiaryForm.setPermanentDivision(beneficiary.getPermanentDivision());
        beneficiaryForm.setPermanentDistrict(beneficiary.getPermanentDistrict());
        beneficiaryForm.setPermanentUpazila(beneficiary.getPermanentUpazila());
        beneficiaryForm.setPermanentUnion(beneficiary.getPermanentUnion());
        beneficiaryForm.setPermanentVillage(beneficiary.getPermanentVillage());
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            beneficiaryForm.setPresentWardNo(beneficiary.getPresentWardNo());
            beneficiaryForm.setPresentPostCode(beneficiary.getPresentPostCode());
            beneficiaryForm.setPermanentWardNo(beneficiary.getPermanentWardNo());
            beneficiaryForm.setPermanentPostCode(beneficiary.getPermanentPostCode());
        } else {
            beneficiaryForm.setPresentWardNo(CommonUtility.getNumberInBangla(beneficiary.getPresentWardNo()));
            beneficiaryForm.setPresentPostCode(CommonUtility.getNumberInBangla(beneficiary.getPresentPostCode()));
            beneficiaryForm.setPermanentWardNo(CommonUtility.getNumberInBangla(beneficiary.getPermanentWardNo()));
            beneficiaryForm.setPermanentPostCode(CommonUtility.getNumberInBangla(beneficiary.getPermanentPostCode()));
        }
    }

    private void loadSocioEconomicInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        BeneficiarySocioEconomicInfo beneficiarySocioEconomicInfo = beneficiary.getBeneficiarySocioEconomicInfo();
        if (beneficiarySocioEconomicInfo != null) {
            beneficiaryForm.setMonthlyIncome(beneficiarySocioEconomicInfo.getMonthlyIncome());
            beneficiaryForm.setDisability(beneficiarySocioEconomicInfo.getDisability());
            beneficiaryForm.sethHWallMadeOf(beneficiarySocioEconomicInfo.gethHWallMadeOf());
            beneficiaryForm.sethASElectricity(beneficiarySocioEconomicInfo.gethASElectricity());
            beneficiaryForm.sethASElectricFan(beneficiarySocioEconomicInfo.gethASElectricFan());
            if (beneficiary.getApplicantType() == ApplicantType.UNION) {
                beneficiaryForm.setLandSizeRural(beneficiarySocioEconomicInfo.getLandSizeRural());
                beneficiaryForm.setOccupationRural(beneficiarySocioEconomicInfo.getOccupationRural());
                beneficiaryForm.sethASLatrineRural(beneficiarySocioEconomicInfo.gethASLatrineRural());
                beneficiaryForm.sethASTubewellRural(beneficiarySocioEconomicInfo.gethASTubewellRural());
            } else {
                beneficiaryForm.setHasResidenceUrban(beneficiarySocioEconomicInfo.getHasResidenceUrban());
                beneficiaryForm.setOccupationUrban(beneficiarySocioEconomicInfo.getOccupationUrban());
                beneficiaryForm.sethASKitchenUrban(beneficiarySocioEconomicInfo.gethASKitchenUrban());
                beneficiaryForm.sethASTelivisionUrban(beneficiarySocioEconomicInfo.gethASTelivisionUrban());
            }
        }
    }

    private void loadHealthStatusInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setConceptionTerm(beneficiary.getConceptionTerm());
        beneficiaryForm.setConceptionDuration(beneficiary.getConceptionDuration());
    }

    private void loadPaymentInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setPaymentType(beneficiary.getPaymentType());
        if (beneficiaryForm.getPaymentType() != null) {
            switch (beneficiaryForm.getPaymentType()) {
                case BANKING:
                    beneficiaryForm.setBank(beneficiary.getBank());
                    beneficiaryForm.setBranch(beneficiary.getBranch());
                    beneficiaryForm.setAccountType(beneficiary.getAccountType());
                    beneficiaryForm.setMobileBankingProvider(null);
                    beneficiaryForm.setPostOfficeBranch(null);
                    break;
                case MOBILEBANKING:
                    beneficiaryForm.setMobileBankingProvider(beneficiary.getMobileBankingProvider());
                    beneficiaryForm.setAccountType(beneficiary.getAccountType());
                    beneficiaryForm.setBank(null);
                    beneficiaryForm.setBranch(null);
                    beneficiaryForm.setPostOfficeBranch(null);
                    beneficiaryForm.setAccountType(null);
                    break;
                case POSTOFFICE:
                    beneficiaryForm.setPostOfficeBranch(beneficiary.getPostOfficeBranch());
                    beneficiaryForm.setAccountTypePO(beneficiary.getAccountType());
                    beneficiaryForm.setBank(null);
                    beneficiaryForm.setBranch(null);
                    beneficiaryForm.setMobileBankingProvider(null);
                    break;
            }
            beneficiaryForm.setAccountName(beneficiary.getAccountName());
            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
                beneficiaryForm.setAccountNo(beneficiary.getAccountNo());
            } else {
                beneficiaryForm.setAccountNo(CommonUtility.getNumberInBangla(beneficiary.getAccountNo()));
            }
        } else {
            beneficiaryForm.setBank(null);
            beneficiaryForm.setBranch(null);
            beneficiaryForm.setPostOfficeBranch(null);
            beneficiaryForm.setMobileBankingProvider(null);
            beneficiaryForm.setAccountType(null);
            beneficiaryForm.setAccountName(null);
            beneficiaryForm.setAccountNo(null);
        }
    }

    private void loadBiometricInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        try {
            beneficiaryForm.setPhoto(null);
            beneficiaryForm.setPhotoPath(beneficiary.getBeneficiaryBiometricInfo().getPhotoPath());
            beneficiaryForm.setPhotoData(beneficiary.getBeneficiaryBiometricInfo().getBase64PhotoData());
            beneficiaryForm.setSignature(null);
            beneficiaryForm.setSignaturePath(beneficiary.getBeneficiaryBiometricInfo().getSignaturePath());
            beneficiaryForm.setSignatureData(beneficiary.getBeneficiaryBiometricInfo().getBase64SignatureData());
        } catch (Exception ex) {
            //logger.getlogger(ApplicantController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadAttachmentInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setAttachmentList(beneficiary.getBeneficiaryAttachmentList());
    }

    @RequestMapping(value = "/beneficiary/edit/{id}", method = RequestMethod.POST)
    public String editBeneficiaryInformation(@ModelAttribute BeneficiaryForm beneficiaryForm, BindingResult bindingResult, Model model, HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return editBeneficiaryInformation(beneficiaryForm.getId(), beneficiaryForm, model, true, session);
        }
        Beneficiary beneficiary = null;
        try {
            if (beneficiaryForm.getId() != null) {
                beneficiary = this.beneficiaryService.getBeneficiary(beneficiaryForm.getId());
            }
            beneficiary.setModifiedBy((User) session.getAttribute("user"));
            beneficiary.setModificationDate(Calendar.getInstance());
            beneficiary.setScheme(((UserDetail) session.getAttribute("userDetail")).getScheme());
            beneficiary.setFiscalYear(beneficiaryForm.getFiscalYear());
            beneficiary.setBeneficiaryStatus(BeneficiaryStatus.ACTIVE);

            createBasicInfo(beneficiary, beneficiaryForm);
            createAddressInfo(beneficiary, beneficiaryForm);
            createSocioEconomicInfo(beneficiary, beneficiaryForm);
            createHealthStatusInfo(beneficiary, beneficiaryForm);
            createPaymentInfo(beneficiary, beneficiaryForm);
            createBiometricInfo(beneficiary, beneficiaryForm);
            createAttachmentInfo(request, beneficiary, beneficiaryForm);

            this.beneficiaryService.edit(beneficiary);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            switch (beneficiaryForm.getApplicantType()) {
                case UNION:
                    return "redirect:/beneficiary/union/list";
                case MUNICIPAL:
                    return "redirect:/beneficiary/municipal/list";
                case CITYCORPORATION:
                    return "redirect:/beneficiary/cityCorporation/list";
                case BGMEA:
                    return "redirect:/beneficiary/bgmea/list";
                case BKMEA:
                    return "redirect:/beneficiary/bkmea/list";
                default:
                    return null;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/checkUniqueBenNid", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUniqueBenNid(String nid, Integer benId) {
        nid = CommonUtility.getNumberInEnglish(nid);
        boolean result = this.beneficiaryService.checkUniqueBenNid(new BigInteger(nid), benId);
        return result;
    }

    @RequestMapping(value = "/beneficiaryPaymentInfo/edit/{id}", method = RequestMethod.GET)
    public String editBeneficiaryPaymentInfo(@PathVariable("id") Integer id, @ModelAttribute BeneficiaryForm beneficiaryForm, Model model, boolean beneficiaryLoadRequired, HttpSession session) {
        try {
            Beneficiary beneficiary = !beneficiaryLoadRequired ? this.beneficiaryService.getBeneficiary(id) : null;
            loadBasicInfo(beneficiaryForm, beneficiary);
            loadAddressInfo(beneficiaryForm, beneficiary);
            loadPaymentInfo(beneficiaryForm, beneficiary);
            CommonUtility.mapPaymentTypeName(model);
            CommonUtility.mapAccountTypeName(model);

            model.addAttribute("beneficiaryForm", beneficiaryForm);
            return "editBeneficiaryPaymentInfo";
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/beneficiaryPaymentInfo/edit/{id}", method = RequestMethod.POST)
    public String editBeneficiaryPaymentInfo(@ModelAttribute BeneficiaryForm beneficiaryForm, BindingResult bindingResult, Model model, HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return editBeneficiaryInformation(beneficiaryForm.getId(), beneficiaryForm, model, true, session);
        }
        Beneficiary beneficiary = null;
        try {
            if (beneficiaryForm.getId() != null) {
                beneficiary = this.beneficiaryService.getBeneficiary(beneficiaryForm.getId());
            }
            beneficiary.setModifiedBy((User) session.getAttribute("user"));
            beneficiary.setModificationDate(Calendar.getInstance());

            createPaymentInfo(beneficiary, beneficiaryForm);

            this.beneficiaryService.edit(beneficiary, true);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/payrollBouncedBack/list";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}

class BeneficiaryResponseData {

    private String name;
    private String schemeName;

    public BeneficiaryResponseData(String name, String schemeName) {
        this.name = name;
        this.schemeName = schemeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

}
