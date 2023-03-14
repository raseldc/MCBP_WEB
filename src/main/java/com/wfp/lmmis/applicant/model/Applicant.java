package com.wfp.lmmis.applicant.model;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.ConceptionTerm;
import com.wfp.lmmis.enums.EducationLevelEnum;
import com.wfp.lmmis.enums.MaritalInfoEnum;
import com.wfp.lmmis.enums.Occupation;
import com.wfp.lmmis.enums.PaymentTypeEnum;
import com.wfp.lmmis.enums.ReligionEnum;
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
import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.types.BloodGroup;
import com.wfp.lmmis.types.Gender;
import com.wfp.lmmis.types.SystemRecommendedStatus;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "applicant")
public class Applicant extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "nick_name", nullable = true)
    private String nickName;

    @Column(name = "nid", nullable = true)
    private BigInteger nid;

    @Column(name = "date_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Calendar dateOfBirth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "birth_place_id", nullable = true)
    private District birthPlace;

    @Column(name = "education_level", nullable = true)
    private EducationLevelEnum educationLevelEnum;

    @Column(name = "religion", nullable = false)
    private ReligionEnum religionEnum;

    @Column(name = "marital_info", nullable = true)
    private MaritalInfoEnum maritalInfoEnum;

    @Column(name = "gender", columnDefinition = "bit(1)", nullable = false)
    private Gender gender;

    @Column(name = "father_name", nullable = true)
    private String fatherName;

    @Column(name = "mother_name", nullable = true)
    private String motherName;

    @Column(name = "spouse_name", nullable = true)
    private String spouseName;

    @Column(name = "mobile_number", nullable = false, length = 11)
    private Integer mobileNo;

    @Column(name = "occupation", columnDefinition = "bit(2)", nullable = true)
    private Occupation occupation;

    @Column(name = "blood_group", columnDefinition = "bit(4)", nullable = true)
    private BloodGroup bloodGroup;

    @Column(name = "is_nrb", columnDefinition = "bit(1)", nullable = false)
    private Boolean nrb;

    @Column(name = "is_beneficiary_in_other_scheme", columnDefinition = "bit(1)", nullable = false)
    private Boolean beneficiaryInOtherScheme;

    @Column(name = "status", columnDefinition = "char(3)", nullable = false)
    private ApplicationStatus applicationStatus;

    @Column(name = "recommendation_status", columnDefinition = "bit(1)", nullable = false)
    private Boolean recommendationStatus;

    @Column(name = "system_status", columnDefinition = "bit(1)", nullable = false)
    private SystemRecommendedStatus systemRecommendedStatus;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "batch_id", nullable = true)
//    private Batch batch;
    // Address Info
    @Column(name = "applicant_type", columnDefinition = "bit(3)", nullable = false)
    private ApplicantType applicantType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "factory_id", nullable = true)
    private Factory factory;

    @Column(name = "permanent_addressline1", nullable = false)
    private String permanentAddressLine1;

    @Column(name = "permanent_addressline2")
    private String permanentAddressLine2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permanent_division_id", columnDefinition = "tinyint unsigned", nullable = false)
    private Division permanentDivision;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permanent_district_id", columnDefinition = "tinyint unsigned", nullable = false)
    private District permanentDistrict;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permanent_upazila_id", columnDefinition = "smallint unsigned", nullable = false)
    private Upazilla permanentUpazila;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permanent_union_id", columnDefinition = "mediumint unsigned", nullable = false)
    private Union permanentUnion;

    @Column(name = "permanent_ward_no", nullable = true)
    private String permanentWardNo;

    @Column(name = "permanent_postcode", columnDefinition = "char(4)", nullable = false)
    private String permanentPostCode;

    @Column(name = "present_addressline1", nullable = false)
    private String presentAddressLine1;

    @Column(name = "present_addressline2")
    private String presentAddressLine2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "present_division_id", columnDefinition = "tinyint unsigned", nullable = false)
    private Division presentDivision;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "present_district_id", columnDefinition = "tinyint unsigned", nullable = false)
    private District presentDistrict;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "present_upazila_id", columnDefinition = "smallint unsigned", nullable = false)
    private Upazilla presentUpazila;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "present_union_id", columnDefinition = "mediumint unsigned", nullable = false)
    private Union presentUnion;

    @Column(name = "present_ward_no", nullable = true)
    private String presentWardNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "present_village_id", nullable = true)
    private Village presentVillage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permanent_village_id", nullable = true)
    private Village permanentVillage;

    @Column(name = "present_postcode", columnDefinition = "char(4)", nullable = false)
    private String presentPostCode;

    @Column(name = "conception_term", nullable = false, columnDefinition = "bit(1)")
    private ConceptionTerm conceptionTerm;

    @Column(name = "conception_duration", nullable = false, columnDefinition = "tinyint unsigned")
    private Integer conceptionDuration;
    // Account Info
    @Column(name = "payment_type", nullable = true)
    private PaymentTypeEnum paymentType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id", nullable = true)
    private Bank bank;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", nullable = true)
    private Branch branch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mobile_banking_provider_id", nullable = true)
    private MobileBankingProvider mobileBankingProvider;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_office_branch_id", nullable = true)
    private PostOfficeBranch postOfficeBranch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_type_id", columnDefinition = "tinyint unsigned", nullable = true)
    private AccountType accountType;

    @Column(name = "account_name", nullable = true)
    private String accountName;

    @Column(name = "account_no", nullable = true, length = 17)
    private String accountNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fiscal_year_id", nullable = false)
    private FiscalYear fiscalYear;

//    @OneToMany(mappedBy = "applicant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<SchemeAttributeValue> schemeAttributeValueList;
    @OneToMany(mappedBy = "applicant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ApplicantAttachment> applicantAttachmentList;

    @OneToOne(mappedBy = "applicant", cascade = CascadeType.ALL)
    private ApplicantBiometricInfo applicantBiometricInfo;

    @OneToOne(mappedBy = "applicant", cascade = CascadeType.ALL)
    private ApplicantSocioEconomicInfo applicantSocioEconomicInfo;

    @OneToOne(mappedBy = "applicant")
    private DoubleDippingMatchedStatus doubleDippingMatchedStatus;

    @Transient
    private List<Integer> attachmentRemoveList;

    @Transient
    private Factory bgmeaFactory;
    @Transient
    private Factory bkmeaFactory;

//    @Formula(value = "concat(concat(first_name_in_english, ' ', case when middle_name_in_english is null then '' else middle_name_in_english end), ' ', last_name_in_english)")
    @Column(name = "full_name_in_english", nullable = false)
    private String fullNameInEnglish;

//    @Formula(value = "concat(concat(first_name_in_bangla, ' ', case when middle_name_in_bangla is null then '' else middle_name_in_bangla end), ' ', last_name_in_bangla)")
    @Column(name = "full_name_in_bangla", nullable = false)
    private String fullNameInBangla;

    @Column(name = "score", columnDefinition = "tinyint unsigned default 0", nullable = false)
    private int score;

    /**
     *
     * @return
     */
    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    /**
     *
     * @return
     */
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     *
     * @return
     */
    public BigInteger getNid() {
        return nid;
    }

    /**
     *
     * @param nid
     */
    public void setNid(BigInteger nid) {
        this.nid = nid;
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public District getBirthPlace() {
        return birthPlace;
    }

    /**
     *
     * @param birthPlace
     */
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

    /**
     *
     * @return
     */
    public MaritalInfoEnum getMaritalInfoEnum() {
        return maritalInfoEnum;
    }

    public void setMaritalInfoEnum(MaritalInfoEnum maritalInfoEnum) {
        this.maritalInfoEnum = maritalInfoEnum;
    }

    /**
     *
     * @return
     */
    public Gender getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
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

    public Integer getMobileNo() {
        return mobileNo;
    }

    /**
     *
     * @param mobileNo
     */
    public void setMobileNo(Integer mobileNo) {
        this.mobileNo = mobileNo;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    /**
     *
     * @param bloodGroup
     */
    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    /**
     *
     * @return
     */
    public Boolean getNrb() {
        return nrb;
    }

    /**
     *
     * @param nrb
     */
    public void setNrb(Boolean nrb) {
        this.nrb = nrb;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public Boolean getBeneficiaryInOtherScheme() {
        return beneficiaryInOtherScheme;
    }

    public void setBeneficiaryInOtherScheme(Boolean beneficiaryInOtherScheme) {
        this.beneficiaryInOtherScheme = beneficiaryInOtherScheme;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    /**
     *
     * @param applicationStatus
     */
    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
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

    public String getPermanentPostCode() {
        return permanentPostCode;
    }

    public void setPermanentPostCode(String permanentPostCode) {
        this.permanentPostCode = permanentPostCode;
    }

    /**
     *
     * @return
     */
    public String getPresentAddressLine1() {
        return presentAddressLine1;
    }

    public void setPresentAddressLine1(String presentAddressline1) {
        this.presentAddressLine1 = presentAddressline1;
    }

    public String getPresentAddressLine2() {
        return presentAddressLine2;
    }

    public void setPresentAddressLine2(String presentAddressline2) {
        this.presentAddressLine2 = presentAddressline2;
    }

    public Division getPresentDivision() {
        return presentDivision;
    }

    public void setPresentDivision(Division presentDivision) {
        this.presentDivision = presentDivision;
    }

    /**
     *
     * @return
     */
    public District getPresentDistrict() {
        return presentDistrict;
    }

    public void setPresentDistrict(District presentDistrict) {
        this.presentDistrict = presentDistrict;
    }

    public Upazilla getPresentUpazila() {
        return presentUpazila;
    }

    public void setPresentUpazila(Upazilla presentUpazila) {
        this.presentUpazila = presentUpazila;
    }

    public Union getPresentUnion() {
        return presentUnion;
    }

    public void setPresentUnion(Union presentUnion) {
        this.presentUnion = presentUnion;
    }

    public String getPresentWardNo() {
        return presentWardNo;
    }

    /**
     *
     * @param presentWardNo
     */
    public void setPresentWardNo(String presentWardNo) {
        this.presentWardNo = presentWardNo;
    }

    public String getPresentPostCode() {
        return presentPostCode;
    }

    public void setPresentPostCode(String presentPostCode) {
        this.presentPostCode = presentPostCode;
    }

    /**
     *
     * @return
     */
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

    public Branch getBranch() {
        return branch;
    }

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

    public void setPostOfficeBranch(PostOfficeBranch postOfficeBranch) {
        this.postOfficeBranch = postOfficeBranch;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
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

    /**
     *
     * @return
     */
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

//    public List<SchemeAttributeValue> getSchemeAttributeValueList()
//    {
//        return schemeAttributeValueList;
//    }
//
//    public void setSchemeAttributeValueList(List<SchemeAttributeValue> schemeAttributeValueList)
//    {
//        this.schemeAttributeValueList = schemeAttributeValueList;
//    }
    public List<ApplicantAttachment> getApplicantAttachmentList() {
        return applicantAttachmentList;
    }

    /**
     *
     * @param applicantAttachmentList
     */
    public void setApplicantAttachmentList(List<ApplicantAttachment> applicantAttachmentList) {
        this.applicantAttachmentList = applicantAttachmentList;
    }

    public ApplicantBiometricInfo getApplicantBiometricInfo() {
        return applicantBiometricInfo;
    }

    /**
     *
     * @param applicantBiometricInfo
     */
    public void setApplicantBiometricInfo(ApplicantBiometricInfo applicantBiometricInfo) {
        this.applicantBiometricInfo = applicantBiometricInfo;
    }

    public List<Integer> getAttachmentRemoveList() {
        return attachmentRemoveList;
    }

    /**
     *
     * @param attachmentRemoveList
     */
    public void setAttachmentRemoveList(List<Integer> attachmentRemoveList) {
        this.attachmentRemoveList = attachmentRemoveList;
    }

    public String getFullNameInEnglish() {
        return fullNameInEnglish;
    }

    /**
     *
     * @param fullNameInEnglish
     */
    public void setFullNameInEnglish(String fullNameInEnglish) {
        this.fullNameInEnglish = fullNameInEnglish;
    }

    public String getFullNameInBangla() {
        return fullNameInBangla;
    }

    public void setFullNameInBangla(String fullNameInBangla) {
        this.fullNameInBangla = fullNameInBangla;
    }

    public Boolean getRecommendationStatus() {
        return recommendationStatus;
    }

    /**
     *
     * @param recommendationStatus
     */
    public void setRecommendationStatus(Boolean recommendationStatus) {
        this.recommendationStatus = recommendationStatus;
    }

    public SystemRecommendedStatus getSystemRecommendedStatus() {
        return systemRecommendedStatus;
    }

    public void setSystemRecommendedStatus(SystemRecommendedStatus systemRecommendedStatus) {
        this.systemRecommendedStatus = systemRecommendedStatus;
    }

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    public DoubleDippingMatchedStatus getDoubleDippingMatchedStatus() {
        return doubleDippingMatchedStatus;
    }

    public void setDoubleDippingMatchedStatus(DoubleDippingMatchedStatus doubleDippingMatchedStatus) {
        this.doubleDippingMatchedStatus = doubleDippingMatchedStatus;
    }

    public ApplicantSocioEconomicInfo getApplicantSocioEconomicInfo() {
        return applicantSocioEconomicInfo;
    }

    /**
     *
     * @param applicantSocioEconomicInfo
     */
    public void setApplicantSocioEconomicInfo(ApplicantSocioEconomicInfo applicantSocioEconomicInfo) {
        this.applicantSocioEconomicInfo = applicantSocioEconomicInfo;
    }

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

//    public Batch getBatch()
//    {
//        return batch;
//    }
//
//    public void setBatch(Batch batch)
//    {
//        this.batch = batch;
//    }
    public ConceptionTerm getConceptionTerm() {
        return conceptionTerm;
    }

    public void setConceptionTerm(ConceptionTerm conceptionTerm) {
        this.conceptionTerm = conceptionTerm;
    }

    /**
     *
     * @return
     */
    public Integer getConceptionDuration() {
        return conceptionDuration;
    }

    public void setConceptionDuration(Integer conceptionDuration) {
        this.conceptionDuration = conceptionDuration;
    }

    public int getScore() {
        return score;
    }

    /**
     *
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    public Village getPresentVillage() {
        return presentVillage;
    }

    /**
     *
     * @param presentVillage
     */
    public void setPresentVillage(Village presentVillage) {
        this.presentVillage = presentVillage;
    }

    /**
     *
     * @return
     */
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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Id == null) ? 0 : Id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Applicant other = (Applicant) obj;
        if (Id == null) {
            if (other.Id != null) {
                return false;
            }
        } else if (!Id.equals(other.Id)) {
            return false;
        }
        return true;
    }

    @Column(name = "is_lm_mis_exist", nullable = true, length = 11)
    private Integer isLMMISExist;

    /**
     *
     * @return
     */
    public Integer getIsLMMISExist() {
        return isLMMISExist;
    }

    public void setIsLMMISExist(Integer isLMMISExist) {
        this.isLMMISExist = isLMMISExist;
    }
}
