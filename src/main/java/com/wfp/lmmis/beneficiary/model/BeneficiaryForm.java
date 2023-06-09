/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.beneficiary.model;

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
import com.wfp.lmmis.masterdata.model.AccountType;
import com.wfp.lmmis.masterdata.model.Bank;
import com.wfp.lmmis.masterdata.model.Branch;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.MobileBankingProvider;
import com.wfp.lmmis.masterdata.model.PostOfficeBranch;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.model.Village;
import com.wfp.lmmis.payroll.model.Batch;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.types.BloodGroup;
import com.wfp.lmmis.types.Gender;
import java.util.Calendar;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Philip
 */
public class BeneficiaryForm {

    private Integer id;
    private Scheme scheme;
    private FiscalYear fiscalYear;
    //basic info
    private String fullNameInEnglish;
    private String fullNameInBangla;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private String nickName;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Calendar dateOfBirth;
    private String nid;
    private District birthPlace;
    private EducationLevelEnum educationLevelEnum;
    private ReligionEnum religionEnum;
    private MaritalInfoEnum maritalInfoEnum;
    private Gender gender;
    private String mobileNo;
    private String email;
    private BloodGroup bloodGroup;
    private Boolean nrb;
    private Occupation occupation;
//    private Batch batch;
    // Address Info
    private ApplicantType applicantType;
    private Factory bgmeaFactory;
    private Factory bkmeaFactory;
    private String presentAddressLine1;
    private String presentAddressLine2;
    private Division presentDivision;
    private District presentDistrict;
    private Upazilla presentUpazila;
    private Union presentUnion;
    private String presentWardNo;
    private String presentPostCode;
    private String permanentAddressLine1;
    private String permanentAddressLine2;
    private Division permanentDivision;
    private District permanentDistrict;
    private Upazilla permanentUpazila;
    private Union permanentUnion;
    private String permanentWardNo;
    private String permanentPostCode;
    private Village presentVillage;
    private Village permanentVillage;

    //payment info
    private PaymentTypeEnum paymentType;
    private Bank bank;
    private Branch branch;
    private MobileBankingProvider mobileBankingProvider;
    private PostOfficeBranch postOfficeBranch;
    private AccountType accountType;
    private AccountType accountTypePO;
    private String accountName;
    private String accountNo;

//    Health Status Info
    private ConceptionTerm conceptionTerm;
    private Integer conceptionDuration;
    //Socio economic info
    private MonthlyIncome monthlyIncome;
    private DisabilityInFamily disability;
    private HHWallMadeOf hHWallMadeOf;
    private YesNoEnum hASElectricity;
    private YesNoEnum hASElectricFan;
//    --------------- for rural--------------
    private LandSize landSizeRural;
    private OccupationRural occupationRural;
    private YesNoEnum hASLatrineRural;
    private YesNoEnum hASTubewellRural;
//     ------------------- for urban ---------
    private YesNoEnum hasResidenceUrban;
    private OccupationUrban occupationUrban;
    private YesNoEnum hASKitchenUrban;
    private YesNoEnum hASTelivisionUrban;
    //biometric info
    private MultipartFile photo;
    private String photoPath;
    private String photoData;
    private MultipartFile signature;
    private String signaturePath;
    private String signatureData;

    // attachment info
    List<MultipartFile> multipartFileList;
    List<BeneficiaryAttachment> attachmentList;
    String removeList;

    // set default primary address
    private boolean isDivisionAvailable;
    private boolean isDistrictAvailable;
    private boolean isUpazilaAvailable;
    private boolean isUnionAvailable;

    private boolean isBgmeaAvailable;
    private boolean isBkmeaAvailable;

    //already exist lm-mis
    private boolean isLMMISExit;

    public boolean isIsLMMISExit() {
        return isLMMISExit;
    }

    public void setIsLMMISExit(boolean isLMMISExit) {
        this.isLMMISExit = isLMMISExit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Scheme getScheme() {
        return scheme;
    }

    /**
     *
     * @param scheme
     */
    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    /**
     *
     * @param fiscalYear
     */
    public void setFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getFullNameInEnglish() {
        return fullNameInEnglish;
    }

    public void setFullNameInEnglish(String fullNameInEnglish) {
        this.fullNameInEnglish = fullNameInEnglish;
    }

    public String getFullNameInBangla() {
        return fullNameInBangla;
    }

    public void setFullNameInBangla(String fullNameInBangla) {
        this.fullNameInBangla = fullNameInBangla;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    /**
     *
     * @param motherName
     */
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getSpouseName() {
        return spouseName;
    }

    /**
     *
     * @param spouseName
     */
    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getNickName() {
        return nickName;
    }

    /**
     *
     * @param nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     *
     * @return
     */
    public String getNid() {
        return nid;
    }

    /**
     *
     * @param nid
     */
    public void setNid(String nid) {
        this.nid = nid;
    }

    public District getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(District birthPlace) {
        this.birthPlace = birthPlace;
    }

    public EducationLevelEnum getEducationLevelEnum() {
        return educationLevelEnum;
    }

    public void setEducationLevelEnum(EducationLevelEnum educationLevelEnum) {
        this.educationLevelEnum = educationLevelEnum;
    }

    public ReligionEnum getReligionEnum() {
        return religionEnum;
    }

    public void setReligionEnum(ReligionEnum religionEnum) {
        this.religionEnum = religionEnum;
    }

    public MaritalInfoEnum getMaritalInfoEnum() {
        return maritalInfoEnum;
    }

    /**
     *
     * @param maritalInfoEnum
     */
    public void setMaritalInfoEnum(MaritalInfoEnum maritalInfoEnum) {
        this.maritalInfoEnum = maritalInfoEnum;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     */
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Boolean getNrb() {
        return nrb;
    }

    public void setNrb(Boolean nrb) {
        this.nrb = nrb;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    /**
     *
     * @param applicantType
     */
    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    /**
     *
     * @return
     */
    public Factory getBgmeaFactory() {
        return bgmeaFactory;
    }

    public void setBgmeaFactory(Factory bgmeaFactory) {
        this.bgmeaFactory = bgmeaFactory;
    }

    public Factory getBkmeaFactory() {
        return bkmeaFactory;
    }

    public void setBkmeaFactory(Factory bkmeaFactory) {
        this.bkmeaFactory = bkmeaFactory;
    }

    public String getPresentAddressLine1() {
        return presentAddressLine1;
    }

    public void setPresentAddressLine1(String presentAddressLine1) {
        this.presentAddressLine1 = presentAddressLine1;
    }

    /**
     *
     * @return
     */
    public String getPresentAddressLine2() {
        return presentAddressLine2;
    }

    public void setPresentAddressLine2(String presentAddressLine2) {
        this.presentAddressLine2 = presentAddressLine2;
    }

    public Division getPresentDivision() {
        return presentDivision;
    }

    public void setPresentDivision(Division presentDivision) {
        this.presentDivision = presentDivision;
    }

    public District getPresentDistrict() {
        return presentDistrict;
    }

    public void setPresentDistrict(District presentDistrict) {
        this.presentDistrict = presentDistrict;
    }

    public Upazilla getPresentUpazila() {
        return presentUpazila;
    }

    /**
     *
     * @param presentUpazila
     */
    public void setPresentUpazila(Upazilla presentUpazila) {
        this.presentUpazila = presentUpazila;
    }

    /**
     *
     * @return
     */
    public Union getPresentUnion() {
        return presentUnion;
    }

    public void setPresentUnion(Union presentUnion) {
        this.presentUnion = presentUnion;
    }

    public String getPresentWardNo() {
        return presentWardNo;
    }

    public void setPresentWardNo(String presentWardNo) {
        this.presentWardNo = presentWardNo;
    }

    public String getPresentPostCode() {
        return presentPostCode;
    }

    public void setPresentPostCode(String presentPostCode) {
        this.presentPostCode = presentPostCode;
    }

    public String getPermanentAddressLine1() {
        return permanentAddressLine1;
    }

    public void setPermanentAddressLine1(String permanentAddressLine1) {
        this.permanentAddressLine1 = permanentAddressLine1;
    }

    public String getPermanentAddressLine2() {
        return permanentAddressLine2;
    }

    public void setPermanentAddressLine2(String permanentAddressLine2) {
        this.permanentAddressLine2 = permanentAddressLine2;
    }

    public Division getPermanentDivision() {
        return permanentDivision;
    }

    public void setPermanentDivision(Division permanentDivision) {
        this.permanentDivision = permanentDivision;
    }

    public District getPermanentDistrict() {
        return permanentDistrict;
    }

    public void setPermanentDistrict(District permanentDistrict) {
        this.permanentDistrict = permanentDistrict;
    }

    public Upazilla getPermanentUpazila() {
        return permanentUpazila;
    }

    public void setPermanentUpazila(Upazilla permanentUpazila) {
        this.permanentUpazila = permanentUpazila;
    }

    /**
     *
     * @return
     */
    public Union getPermanentUnion() {
        return permanentUnion;
    }

    public void setPermanentUnion(Union permanentUnion) {
        this.permanentUnion = permanentUnion;
    }

    public String getPermanentWardNo() {
        return permanentWardNo;
    }

    /**
     *
     * @param permanentWardNo
     */
    public void setPermanentWardNo(String permanentWardNo) {
        this.permanentWardNo = permanentWardNo;
    }

    /**
     *
     * @return
     */
    public String getPermanentPostCode() {
        return permanentPostCode;
    }

    public void setPermanentPostCode(String permanentPostCode) {
        this.permanentPostCode = permanentPostCode;
    }

    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    /**
     *
     * @return
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     *
     * @param branch
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public MobileBankingProvider getMobileBankingProvider() {
        return mobileBankingProvider;
    }

    public void setMobileBankingProvider(MobileBankingProvider mobileBankingProvider) {
        this.mobileBankingProvider = mobileBankingProvider;
    }

    public PostOfficeBranch getPostOfficeBranch() {
        return postOfficeBranch;
    }

    /**
     *
     * @param postOfficeBranch
     */
    public void setPostOfficeBranch(PostOfficeBranch postOfficeBranch) {
        this.postOfficeBranch = postOfficeBranch;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    /**
     *
     * @param accountType
     */
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    /**
     *
     * @return
     */
    public AccountType getAccountTypePO() {
        return accountTypePO;
    }

    public void setAccountTypePO(AccountType accountTypePO) {
        this.accountTypePO = accountTypePO;
    }

    public String getAccountName() {
        return accountName;
    }

    /**
     *
     * @param accountName
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    /**
     *
     * @return
     */
    public ConceptionTerm getConceptionTerm() {
        return conceptionTerm;
    }

    public void setConceptionTerm(ConceptionTerm conceptionTerm) {
        this.conceptionTerm = conceptionTerm;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    /**
     *
     * @param photoPath
     */
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public String getSignaturePath() {
        return signaturePath;
    }

    public void setSignaturePath(String signaturePath) {
        this.signaturePath = signaturePath;
    }

    /**
     *
     * @return
     */
    public MultipartFile getSignature() {
        return signature;
    }

    public void setSignature(MultipartFile signature) {
        this.signature = signature;
    }

    /**
     *
     * @return
     */
    public String getPhotoData() {
        return photoData;
    }

    public void setPhotoData(String photoData) {
        this.photoData = photoData;
    }

    /**
     *
     * @return
     */
    public String getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(String signatureData) {
        this.signatureData = signatureData;
    }

    public boolean isIsDivisionAvailable() {
        return isDivisionAvailable;
    }

    public void setIsDivisionAvailable(boolean isDivisionAvailable) {
        this.isDivisionAvailable = isDivisionAvailable;
    }

    public boolean isIsDistrictAvailable() {
        return isDistrictAvailable;
    }

    public void setIsDistrictAvailable(boolean isDistrictAvailable) {
        this.isDistrictAvailable = isDistrictAvailable;
    }

    public boolean isIsUpazilaAvailable() {
        return isUpazilaAvailable;
    }

    public void setIsUpazilaAvailable(boolean isUpazilaAvailable) {
        this.isUpazilaAvailable = isUpazilaAvailable;
    }

    public boolean isIsUnionAvailable() {
        return isUnionAvailable;
    }

    public void setIsUnionAvailable(boolean isUnionAvailable) {
        this.isUnionAvailable = isUnionAvailable;
    }

//    public Batch getBatch()
//    {
//        return batch;
//    }
//
//    public void setBatch(Batch batch)
//    {
//        this.batch = batch;
//    }
    public List<MultipartFile> getMultipartFileList() {
        return multipartFileList;
    }

    public void setMultipartFileList(List<MultipartFile> multipartFileList) {
        this.multipartFileList = multipartFileList;
    }

    public List<BeneficiaryAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<BeneficiaryAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getRemoveList() {
        return removeList;
    }

    public void setRemoveList(String removeList) {
        this.removeList = removeList;
    }

    public LandSize getLandSizeRural() {
        return landSizeRural;
    }

    public void setLandSizeRural(LandSize landSizeRural) {
        this.landSizeRural = landSizeRural;
    }

    /**
     *
     * @return
     */
    public OccupationRural getOccupationRural() {
        return occupationRural;
    }

    public void setOccupationRural(OccupationRural occupationRural) {
        this.occupationRural = occupationRural;
    }

    /**
     *
     * @return
     */
    public YesNoEnum gethASLatrineRural() {
        return hASLatrineRural;
    }

    public void sethASLatrineRural(YesNoEnum hASLatrineRural) {
        this.hASLatrineRural = hASLatrineRural;
    }

    public YesNoEnum gethASElectricity() {
        return hASElectricity;
    }

    public void sethASElectricity(YesNoEnum hASElectricity) {
        this.hASElectricity = hASElectricity;
    }

    public YesNoEnum gethASElectricFan() {
        return hASElectricFan;
    }

    public void sethASElectricFan(YesNoEnum hASElectricFan) {
        this.hASElectricFan = hASElectricFan;
    }

    public YesNoEnum gethASTubewellRural() {
        return hASTubewellRural;
    }

    public void sethASTubewellRural(YesNoEnum hASTubewellRural) {
        this.hASTubewellRural = hASTubewellRural;
    }

    /**
     *
     * @return
     */
    public YesNoEnum getHasResidenceUrban() {
        return hasResidenceUrban;
    }

    public void setHasResidenceUrban(YesNoEnum hasResidenceUrban) {
        this.hasResidenceUrban = hasResidenceUrban;
    }

    public OccupationUrban getOccupationUrban() {
        return occupationUrban;
    }

    public void setOccupationUrban(OccupationUrban occupationUrban) {
        this.occupationUrban = occupationUrban;
    }

    /**
     *
     * @return
     */
    public YesNoEnum gethASKitchenUrban() {
        return hASKitchenUrban;
    }

    public void sethASKitchenUrban(YesNoEnum hASKitchenUrban) {
        this.hASKitchenUrban = hASKitchenUrban;
    }

    public YesNoEnum gethASTelivisionUrban() {
        return hASTelivisionUrban;
    }

    public void sethASTelivisionUrban(YesNoEnum hASTelivisionUrban) {
        this.hASTelivisionUrban = hASTelivisionUrban;
    }

    public Integer getConceptionDuration() {
        return conceptionDuration;
    }

    public void setConceptionDuration(Integer conceptionDuration) {
        this.conceptionDuration = conceptionDuration;
    }

    /**
     *
     * @return
     */
    public MonthlyIncome getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(MonthlyIncome monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public DisabilityInFamily getDisability() {
        return disability;
    }

    /**
     *
     * @param disability
     */
    public void setDisability(DisabilityInFamily disability) {
        this.disability = disability;
    }

    public HHWallMadeOf gethHWallMadeOf() {
        return hHWallMadeOf;
    }

    public void sethHWallMadeOf(HHWallMadeOf hHWallMadeOf) {
        this.hHWallMadeOf = hHWallMadeOf;
    }

    public Village getPresentVillage() {
        return presentVillage;
    }

    public void setPresentVillage(Village presentVillage) {
        this.presentVillage = presentVillage;
    }

    public Village getPermanentVillage() {
        return permanentVillage;
    }

    public void setPermanentVillage(Village permanentVillage) {
        this.permanentVillage = permanentVillage;
    }

    /**
     *
     * @return
     */
    public boolean isIsBgmeaAvailable() {
        return isBgmeaAvailable;
    }

    public void setIsBgmeaAvailable(boolean isBgmeaAvailable) {
        this.isBgmeaAvailable = isBgmeaAvailable;
    }

    public boolean isIsBkmeaAvailable() {
        return isBkmeaAvailable;
    }

    public void setIsBkmeaAvailable(boolean isBkmeaAvailable) {
        this.isBkmeaAvailable = isBkmeaAvailable;
    }

}
