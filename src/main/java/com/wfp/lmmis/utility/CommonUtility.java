/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.ConceptionTerm;
import com.wfp.lmmis.enums.DisabilityInFamily;
import com.wfp.lmmis.enums.EducationLevelEnum;
import com.wfp.lmmis.enums.HHWallMadeOf;
import com.wfp.lmmis.enums.LandSize;
import com.wfp.lmmis.enums.MaritalInfoEnum;
import com.wfp.lmmis.enums.MonthlyIncome;
import com.wfp.lmmis.enums.Occupation;
import com.wfp.lmmis.enums.OccupationRural;
import com.wfp.lmmis.enums.OccupationUrban;
import com.wfp.lmmis.enums.PaymentTypeEnum;
import com.wfp.lmmis.enums.ReligionEnum;
import com.wfp.lmmis.enums.YesNoEnum;
import com.wfp.lmmis.form.ReportParameterForm;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.masterdata.model.AccountType;
import com.wfp.lmmis.masterdata.model.Bank;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.MobileBankingProvider;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import com.wfp.lmmis.masterdata.service.AccountTypeService;
import com.wfp.lmmis.masterdata.service.BankService;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.FactoryService;
import com.wfp.lmmis.masterdata.service.MobileBankingProviderService;
import com.wfp.lmmis.masterdata.service.SchemeAttributeService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.VillageService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.BatchService;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.payroll.service.PaymentCycleService;
import com.wfp.lmmis.types.FactoryType;
import java.io.UnsupportedEncodingException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 *
 * @author PCUser
 */
@Component
public class CommonUtility {

    private static FiscalYearService fiscalYearService;
    @Autowired
    private FiscalYearService fiscalYearService0;

    private static SchemeService schemeService;
    @Autowired
    private SchemeService schemeService0;

    private static PaymentCycleService paymentCycleService;
    @Autowired
    private PaymentCycleService paymentCycleService0;

    private static DivisionService divisionService;
    @Autowired
    private DivisionService divisionService0;

    private static DistrictService districtService;
    @Autowired
    private DistrictService districtService0;

    private static MobileBankingProviderService mobileBankingProviderService;
    @Autowired
    private MobileBankingProviderService mobileBankingProviderService0;

    private static BankService bankService;
    @Autowired
    private BankService bankService0;

    private static AccountTypeService accountTypeService;
    @Autowired
    private AccountTypeService accountTypeService0;

    private static SchemeAttributeService schemeAttributeService;
    @Autowired
    private SchemeAttributeService schemeAttributeService0;

    private static FactoryService factoryService;
    @Autowired
    private FactoryService factoryService0;
    private static BatchService batchService;
    @Autowired
    private BatchService batchService0;

    private static VillageService villageService;
    @Autowired
    private VillageService villageService0;

    @PostConstruct
    public void initStaticService() {
        fiscalYearService = this.fiscalYearService0;
        schemeService = this.schemeService0;
        paymentCycleService = this.paymentCycleService0;
        divisionService = this.divisionService0;
        districtService = this.districtService0;
        mobileBankingProviderService = this.mobileBankingProviderService0;
        bankService = this.bankService0;
        accountTypeService = this.accountTypeService0;
        schemeAttributeService = this.schemeAttributeService0;
        factoryService = this.factoryService0;
        batchService = this.batchService0;
        villageService = this.villageService0;
    }

    public static void mapDivisionName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<Division> divisionlist = divisionService.getDivisionList();
        model.addAttribute("divisionList", divisionlist);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("divisionName", "nameInEnglish");
        } else {
            model.addAttribute("divisionName", "nameInBangla");
        }
    }

    public static void mapDistrictName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<ItemObject> districtlist = districtService.getDistrictIoList(null);
        model.addAttribute("districtList", districtlist);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("districtName", "nameInEnglish");
        } else {
            model.addAttribute("districtName", "nameInBangla");
        }
    }

    public static void mapBirthPlaceName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<ItemObject> districtlist = districtService.getDistrictIoList(null);
        model.addAttribute("birthPlaceList", districtlist);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("birthPlaceName", "nameInEnglish");
        } else {
            model.addAttribute("birthPlaceName", "nameInBangla");
        }
    }

    public static void mapBGMEAFactoryName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<Factory> factorylist = factoryService.getFactoryList(FactoryType.GARMENTS);
        model.addAttribute("bgmeaFactoryList", factorylist);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("bgmeaFactoryName", "nameInEnglish");
        } else {
            model.addAttribute("bgmeaFactoryName", "nameInBangla");
        }
    }

    public static void mapBKMEAFactoryName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<Factory> factorylist = factoryService.getFactoryList(FactoryType.KNITWEAR);
        model.addAttribute("bkmeaFactoryList", factorylist);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("bkmeaFactoryName", "nameInEnglish");
        } else {
            model.addAttribute("bkmeaFactoryName", "nameInBangla");
        }
    }

    public static void mapSchemeName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<Scheme> schemelist = schemeService.getSchemeList(null, true);
        model.addAttribute("schemeList", schemelist);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("schemeName", "nameInEnglish");
        } else {
            model.addAttribute("schemeName", "nameInBangla");
        }
    }

    public static void mapFiscalYearName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
//            StaticContextAccessor.getBean(FiscalYearService.class).doStuff();
            List<FiscalYear> fiscalYearlist = fiscalYearService.getFiscalYearList(true, true);
            model.addAttribute("fiscalYearList", fiscalYearlist);
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("fiscalYearName", "nameInEnglish");
            } else {
                model.addAttribute("fiscalYearName", "nameInBangla");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mapAllFiscalYearName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
//            StaticContextAccessor.getBean(FiscalYearService.class).doStuff();
            List<FiscalYear> fiscalYearlist = fiscalYearService.getFiscalYearList(false, false);
            model.addAttribute("fiscalYearList", fiscalYearlist);
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("fiscalYearName", "nameInEnglish");
            } else {
                model.addAttribute("fiscalYearName", "nameInBangla");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mapOtherFiscalYearName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
//            StaticContextAccessor.getBean(FiscalYearService.class).doStuff();
            List<FiscalYear> fiscalYearlist = fiscalYearService.getFiscalYearList(true, false);
            model.addAttribute("fiscalYearList", fiscalYearlist);
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("fiscalYearName", "nameInEnglish");
            } else {
                model.addAttribute("fiscalYearName", "nameInBangla");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mapPaymentCycleName(Model model, Integer fiscalYearId, Integer schemeId) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            List<ItemObject> paymentCyclelist = paymentCycleService.getPaymentCycleIoList(true, fiscalYearId, true, true, schemeId);
            model.addAttribute("paymentCyclelist", paymentCyclelist);
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("paymentCycleName", "nameInEnglish");
            } else {
                model.addAttribute("paymentCycleName", "nameInBangla");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mapEducationLevelEnumName(Model model) {
        List<EducationLevelEnum> educationLevelEnumList = Arrays.asList(EducationLevelEnum.values());
        model.addAttribute("educationLevelEnumList", educationLevelEnumList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("educationLevelDisplayName", "displayName");
        } else {
            model.addAttribute("educationLevelDisplayName", "displayNameBn");
        }
    }

    public static void mapReligionEnumName(Model model) {
        List<ReligionEnum> religionEnumList = Arrays.asList(ReligionEnum.values());
        model.addAttribute("religionEnumList", religionEnumList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("religionDisplayName", "displayName");
        } else {
            model.addAttribute("religionDisplayName", "displayNameBn");
        }
    }

    public static void mapMaritalInfoEnumName(Model model) {
        List<MaritalInfoEnum> maritalEnumList = Arrays.asList(MaritalInfoEnum.values());
        model.addAttribute("maritalInfoEnumList", maritalEnumList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("maritalInfoDisplayName", "displayName");
        } else {
            model.addAttribute("maritalInfoDisplayName", "displayNameBn");
        }
    }

    /**
     *
     * @param model
     */
    public static void mapOccupationName(Model model) {
        List<Occupation> occupationList = Arrays.asList(Occupation.values());
        model.addAttribute("occupationList", occupationList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }

    public static void mapYesNoEnumName(Model model) {
        List<YesNoEnum> yesNoEnumList = Arrays.asList(YesNoEnum.values());
        model.addAttribute("yesNoEnum", yesNoEnumList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }

    public static void mapConceptionTermEnumName(Model model) {
        List<ConceptionTerm> conceptionTermList = Arrays.asList(ConceptionTerm.values());
        model.addAttribute("conceptionTermEnum", conceptionTermList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }

    public static void mapDisabilitlyEnumName(Model model) {
        List<DisabilityInFamily> disabilityList = Arrays.asList(DisabilityInFamily.values());
        model.addAttribute("disabilityList", disabilityList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }

    public static void mapMonthlyIncome(ApplicantType applicantType, Model model) {
        List<MonthlyIncome> monthlyIncomes = new ArrayList<>();
        switch (applicantType) {
            case UNION:
                monthlyIncomes.add(MonthlyIncome.LESSTHAN6000);
                monthlyIncomes.add(MonthlyIncome.GREATERTHAN6000);
                break;
            case MUNICIPAL:
            case CITYCORPORATION:
                monthlyIncomes.add(MonthlyIncome.LESSTHAN8000);
                monthlyIncomes.add(MonthlyIncome.GREATERTHAN8000);
                break;
            case BGMEA:
            case BKMEA:
                monthlyIncomes.add(MonthlyIncome.LESSTHAN12000);
                monthlyIncomes.add(MonthlyIncome.GREATERTHAN12000);
                break;
        }
        model.addAttribute("monthlyIncomeList", monthlyIncomes);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }

    }

    /**
     *
     * @param applicantType
     * @param model
     */
    public static void mapHHWallMadeOfEnumName(ApplicantType applicantType, Model model) {
        List<HHWallMadeOf> hHWallMadeOfs = new ArrayList<>();
        if (applicantType == ApplicantType.UNION) {
            hHWallMadeOfs.add(HHWallMadeOf.JUTESTICKSBAMBOO);
        } else {
            hHWallMadeOfs.add(HHWallMadeOf.HAMPHAYBAMBOO);
        }
        hHWallMadeOfs.add(HHWallMadeOf.OTHERS);

        model.addAttribute("hHWallMadeOfList", hHWallMadeOfs);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }

    public static void mapLandOwnerShipEnumName(Model model) {
        List<LandSize> landOwnerShipEnumList = Arrays.asList(LandSize.values());
        model.addAttribute("landOwnerShipEnum", landOwnerShipEnumList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }

    public static void mapOccupationRuralEnumName(Model model) {
        List<OccupationRural> occupationRuralEnumList = Arrays.asList(OccupationRural.values());
        model.addAttribute("occupationRuralEnum", occupationRuralEnumList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }

    public static void mapOccupationUrbanEnumName(ApplicantType applicantType, Model model) {
        List<OccupationUrban> occupationUrbans = new ArrayList<>();
        if (applicantType == ApplicantType.BGMEA || applicantType == ApplicantType.BKMEA) {
            occupationUrbans.add(OccupationUrban.RMGWORKER);
        } else {
            occupationUrbans.add(OccupationUrban.DOMESTICWORKER);
            occupationUrbans.add(OccupationUrban.DAYLABOUR);
//            occupationUrbans.add(OccupationUrban.CRAFTSMAN);
            occupationUrbans.add(OccupationUrban.UNEMPLOYED);
            occupationUrbans.add(OccupationUrban.OTHERS);
        }
        model.addAttribute("occupationUrbanEnum", occupationUrbans);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }

    public static void mapPaymentTypeName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<PaymentTypeEnum> paymentTypeList = Arrays.asList(PaymentTypeEnum.values());
        model.addAttribute("paymentTypeList", PaymentTypeEnum.fromValuesToMap());
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("paymentTypeDisplayName", "displayName");
        } else {
            model.addAttribute("paymentTypeDisplayName", "displayNameBn");
        }
    }

    public static void mapMobileBankingProviderName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<MobileBankingProvider> list = mobileBankingProviderService.getMobileBankingProviderList(true);
        model.addAttribute("mobileBankingProviderList", list);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("mobileBankingProviderName", "nameInEnglish");
        } else {
            model.addAttribute("mobileBankingProviderName", "nameInBangla");
        }
    }

    public static void mapBankName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<Bank> bankList = bankService.getBankList();
        model.addAttribute("bankList", bankList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("bankName", "nameInEnglish");
        } else {
            model.addAttribute("bankName", "nameInBangla");
        }
    }

    /**
     *
     * @param model
     */
    public static void mapAccountTypeName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<AccountType> accountTypeList = accountTypeService.getAccountTypeList();
        model.addAttribute("accountTypeList", accountTypeList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("accountTypeName", "nameInEnglish");
        } else {
            model.addAttribute("accountTypeName", "nameInBangla");
        }
    }

    public static void mapSchemeAttributeName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<SchemeAttribute> schemeAttributeList = schemeAttributeService.getSchemeAttributeDDList();
        model.addAttribute("schemeAttributeList", schemeAttributeList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("schemeAttributeName", "nameInEnglish");
        } else {
            model.addAttribute("schemeAttributeName", "nameInBangla");
        }

    }

    public static void mapBatchName(Model model, Integer schemeId) {
        Locale locale = LocaleContextHolder.getLocale();
        List<ItemObject> batchIoList = batchService.getBatchIoList(schemeId, true, true);
        model.addAttribute("batchList", batchIoList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("batchName", "nameInEnglish");
        } else {
            model.addAttribute("batchName", "nameInBangla");
        }

    }

    public static boolean getMapVariable(String varName, Model model) {
        boolean isOld = false;
        Map map = model.asMap();
        if (map != null) {
            try {
                isOld = (boolean) map.get(varName);
//                System.out.println("varName=" + varName + ", value = " + isOld);
            } catch (Exception e) {
//                System.out.println("exception: varName=" + varName + ", value = " + isOld);
                isOld = false;
            }
        }
        return isOld;
    }

    public static void mapGlobalVariables(Model m) {
        m.addAttribute("dateFormat", ApplicationConstants.DATE_FORMAT_JQUERY);
    }

//    public static void mapBloodGroup(Model m)
//    {
//        Locale locale = LocaleContextHolder.getLocale();
//        List<SchemeAttribute> schemeAttributeList = schemeAttributeService.getSchemeAttributeDDList();
//        m.addAttribute("bloodGroupList", BloodGroup.values());
//        if ("en".equals(locale.getLanguage()))
//        {
//            m.addAttribute("bloodGroupName", "nameInEnglish");
//        }
//        else
//        {
//            m.addAttribute("schemeAttributeName", "nameInBangla");
//        }
//    }
    public static String getValueOrDefault(String defaultValue) {
        return Optional.ofNullable(System.getProperty("property"))
                .filter(s -> s != null && !s.isEmpty()).orElse(defaultValue);
    }

    public static String getNumberInBangla(String number) {
        if (number != null) {
            return number.replaceAll("0", "০").replaceAll("1", "১").replaceAll("2", "২").replaceAll("3", "৩").replaceAll("4", "৪").replaceAll("5", "৫").replaceAll("6", "৬").replaceAll("7", "৭").replaceAll("8", "৮").replaceAll("9", "৯");
        }
        return number;
    }

    public static String getNumberInEnglish(String number) {
        return number.replaceAll("০", "0").replaceAll("১", "1").replaceAll("২", "2").replaceAll("৩", "3").replaceAll("৪", "4").replaceAll("৫", "5").replaceAll("৬", "6").replaceAll("৭", "7").replaceAll("৮", "8").replaceAll("৯", "9");
    }

//     public static SearchParameterForm loadSearchParameterForm(HttpServletRequest request)
//    {
////        System.out.println("request = " + request);
//        SearchParameterForm searchParameterForm = new SearchParameterForm(); // default all false
//
//        User loggedUser = (User) request.getSession().getAttribute("user");
////        System.out.println("loggedUser = " + loggedUser);
//        if (loggedUser.getUnion() != null)
//        {
//            searchParameterForm.setIsUnionAvailable(true);
//            searchParameterForm.setUnion(loggedUser.getUnion());
//            searchParameterForm.setIsUpazilaAvailable(true);
//            searchParameterForm.setUpazila(loggedUser.getUpazilla());
//            searchParameterForm.setIsDistrictAvailable(true);
//            searchParameterForm.setDistrict(loggedUser.getDistrict());
//            searchParameterForm.setIsDivisionAvailable(true);
//            searchParameterForm.setDivision(loggedUser.getDivision());
//        }
//        if (loggedUser.getUpazilla() != null)
//        {
//            searchParameterForm.setIsUpazilaAvailable(true);
//            searchParameterForm.setUpazila(loggedUser.getUpazilla());
//            searchParameterForm.setIsDistrictAvailable(true);
//            searchParameterForm.setDistrict(loggedUser.getDistrict());
//            searchParameterForm.setIsDivisionAvailable(true);
//            searchParameterForm.setDivision(loggedUser.getDivision());
//        }
//        if (loggedUser.getDistrict() != null)
//        {
//            searchParameterForm.setIsDistrictAvailable(true);
//            searchParameterForm.setDistrict(loggedUser.getDistrict());
//            searchParameterForm.setIsDivisionAvailable(true);
//            searchParameterForm.setDivision(loggedUser.getDivision());
//        }
//        if (loggedUser.getDivision() != null)
//        {
//            searchParameterForm.setIsDivisionAvailable(true);
//            searchParameterForm.setDivision(loggedUser.getDivision());
//        }
//        searchParameterForm.setIsHeadOfficeUser(loggedUser.isHeadOfficeUser());
//
//        return searchParameterForm;
//    }
    // changes to incorporate lma
    public static SearchParameterForm loadSearchParameterForm(HttpServletRequest request) {
//        System.out.println("request = " + request);
        SearchParameterForm searchParameterForm = new SearchParameterForm(); // default all false

        UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");
//        System.out.println("loggedUser = " + loggedUser);
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
            System.out.println("loggedUser.getDivision() = " + loggedUser.getDivision());
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

        //searchParameterForm.setIsHeadOfficeUser(loggedUser.isHeadOfficeUser());
        return searchParameterForm;
    }

    public static ReportParameterForm loadReportParameterForm(HttpServletRequest request) {
//        System.out.println("request = " + request);
        ReportParameterForm reportParameterForm = new ReportParameterForm(); // default all false

        UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");
//        System.out.println("loggedUser = " + loggedUser);
        if (loggedUser.getUnion() != null) {
            reportParameterForm.setIsUnionAvailable(true);
            reportParameterForm.setUnion(loggedUser.getUnion());
            reportParameterForm.setIsUpazilaAvailable(true);
            reportParameterForm.setUpazila(loggedUser.getUpazila());
            reportParameterForm.setIsDistrictAvailable(true);
            reportParameterForm.setDistrict(loggedUser.getDistrict());
            reportParameterForm.setIsDivisionAvailable(true);
            reportParameterForm.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getUpazila() != null) {
            reportParameterForm.setIsUpazilaAvailable(true);
            reportParameterForm.setUpazila(loggedUser.getUpazila());
            reportParameterForm.setIsDistrictAvailable(true);
            reportParameterForm.setDistrict(loggedUser.getDistrict());
            reportParameterForm.setIsDivisionAvailable(true);
            reportParameterForm.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDistrict() != null) {
            reportParameterForm.setIsDistrictAvailable(true);
            reportParameterForm.setDistrict(loggedUser.getDistrict());
            reportParameterForm.setIsDivisionAvailable(true);
            reportParameterForm.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDivision() != null) {
            reportParameterForm.setIsDivisionAvailable(true);
            reportParameterForm.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getBgmeaFactory() != null) {
            reportParameterForm.setIsBgmeaFactoryAvailable(true);
            reportParameterForm.setBgmeaFactory(loggedUser.getBgmeaFactory());
        }
        if (loggedUser.getBkmeaFactory() != null) {
            reportParameterForm.setIsBkmeaFactoryAvailable(true);
            reportParameterForm.setBkmeaFactory(loggedUser.getBkmeaFactory());
        }
        return reportParameterForm;
    }

    public static int calculateAge(Calendar dateOfBirth, Calendar currentCalendar) {
        System.out.println("start calculateAge");
        System.out.println("year = " + dateOfBirth.get(Calendar.YEAR));
        System.out.println("month = " + dateOfBirth.get(Calendar.MONTH));
        System.out.println("day = " + dateOfBirth.get(Calendar.DAY_OF_MONTH));
        System.out.println("test");
        try {
            LocalDate birthDate = LocalDate.of(dateOfBirth.get(Calendar.YEAR), dateOfBirth.get(Calendar.MONTH) + 1, dateOfBirth.get(Calendar.DAY_OF_MONTH));
            System.out.println("birthDate=" + birthDate);
            LocalDate currentDate = LocalDate.of(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, currentCalendar.get(Calendar.DAY_OF_MONTH));
            System.out.println("in between");
            if ((birthDate != null) && (currentDate != null)) {
                System.out.println("end calculateAge");
                return Period.between(birthDate, currentDate).getYears();
            } else {
                return 0;
            }
        } catch (DateTimeException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getBase64String(byte[] byteArray) {
        byte[] encodeBase64Signature = Base64.encodeBase64(byteArray);
        try {
            return new String(encodeBase64Signature, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            //logger.getlogger(CommonUtility.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static int getMonthCountBetweenCalendars(Calendar startCalendar, Calendar endCalendar) {
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH) + 1;
        return diffMonth;
    }
//    public String getValue(HttpServletRequest request)
//    {
//        String fileName = "messages_en.properties";
////        if ("en".equals(request.getLocale().toString()))
////        {
////            fileName += "en";
////        }
////        else 
////        {
////            fileName += "bn";
////        }
////        fileName += ".properties";
////        System.out.println("fileName = " + fileName);
//        Properties prop = new Properties();
//        try
//        {
//            //load a properties file from class path, inside static method
////            ResourceBundle rb = ResourceBundle.getBundle(fileName);
//            prop.load(this.getClass().getResourceAsStream("config.properties"));
//            System.out.println("ben id string = " + rb.getString("beneficiaryId"));
//
//
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//        return "";
//    }

    public static String getMonthNameByIndex(int index, Locale locale) {
        String months[];
        if (locale.getLanguage().equals("en")) {
            months = new String[]{
                "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
            };
        } else {
            months = new String[]{
                "জানুয়ারী", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "অগাস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর",};
        }
        return months[index];
    }

    /**
     *
     * @param model
     */
    public static void getWardNoList(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<String> wardNoList;
        if ("en".equals(locale.getLanguage())) {
            wardNoList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
        } else {
            wardNoList = Arrays.asList("১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯");
        }
        model.addAttribute("wardNoList", wardNoList);
    }

    public static void mapVillageName(Model model, Integer wardNo, Integer unionId) {
        List<ItemObject> villageList = villageService.getVillageIoList(wardNo, unionId);
        model.addAttribute("villageList", villageList);
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
    }
}
