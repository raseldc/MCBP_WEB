package com.wfp.lmmis.beneficiary.model;

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
import com.wfp.lmmis.payroll.model.Batch;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.types.BeneficiaryDeactivationReasons;
import com.wfp.lmmis.types.BeneficiaryStatus;
import com.wfp.lmmis.types.BloodGroup;
import com.wfp.lmmis.types.Gender;
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
@Table(name = "beneficiary")
public class Beneficiary extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "nick_name", nullable = true)
    private String nickName;

    @Column(name = "nid", nullable = false, length = 17)
    private BigInteger nid;

    @Column(name = "date_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Calendar dateOfBirth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "birth_place_id", nullable = false)
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

    @Column(name = "mobile_number", nullable = true, length = 11)
    private Integer mobileNo;

    @Column(name = "is_lm_mis_exist", nullable = true, length = 11)
    private Integer isLMMISExist;

    @Column(name = "occupation", columnDefinition = "bit(2)", nullable = true)
    private Occupation occupation;

    @Column(name = "blood_group", columnDefinition = "bit(4)", nullable = true)
    private BloodGroup bloodGroup;

    @Column(name = "status", columnDefinition = "char(2)", nullable = false)
    private BeneficiaryStatus beneficiaryStatus;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "batch_id", nullable = false)
//    private Batch batch;
    // Address Info    
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

    @Column(name = "present_postcode", columnDefinition = "char(4)", nullable = false)
    private String presentPostCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "present_village_id", nullable = true)
    private Village presentVillage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permanent_village_id", nullable = true)
    private Village permanentVillage;

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

//    @ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne()
    @JoinColumn(name = "fiscal_year_id", nullable = false)
    private FiscalYear fiscalYear;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @Column(name = "applicant_type", columnDefinition = "bit(3)", nullable = false)
    private ApplicantType applicantType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "factory_id", nullable = true)
    private Factory factory;

//    @OneToMany(mappedBy = "beneficiary", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private List<BeneficiarySchemeAttributeValue> schemeAttributeValueList;
    @OneToMany(mappedBy = "beneficiary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BeneficiaryAttachment> beneficiaryAttachmentList;

    @OneToOne(mappedBy = "beneficiary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BeneficiaryBiometricInfo beneficiaryBiometricInfo;

    @OneToOne(mappedBy = "beneficiary", cascade = CascadeType.ALL)
    private BeneficiarySocioEconomicInfo beneficiarySocioEconomicInfo;
//    @Formula(value = "concat(concat(first_name_in_english, ' ', case when middle_name_in_english is null then '' else middle_name_in_english end), ' ', last_name_in_english)")
    @Column(name = "full_name_in_english", nullable = false)
    private String fullNameInEnglish;

//    @Formula(value = "concat(concat(first_name_in_bangla, ' ', case when middle_name_in_bangla is null then '' else middle_name_in_bangla end), ' ', last_name_in_bangla)")
    @Column(name = "full_name_in_bangla", nullable = false)
    private String fullNameInBangla;

    @Column(name = "deactivation_status", columnDefinition = "char(2)", nullable = true)
    private BeneficiaryDeactivationReasons beneficiaryDeactivationReasons;

    @Column(name = "incident_date", nullable = true)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar incidentDate;

    @Column(name = "deactivation_comment", nullable = true)
    private String deactivationComment;

    @Column(name = "attachment_path")
    private String attachmentPath;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enrollment_date", nullable = false)
    private Calendar enrollmentDate;

    @Transient
    private List<Integer> attachmentRemoveList;
    @Transient
    private Factory bgmeaFactory;
    @Transient
    private Factory bkmeaFactory;

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
    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public BeneficiaryStatus getBeneficiaryStatus() {
        return beneficiaryStatus;
    }

    public void setBeneficiaryStatus(BeneficiaryStatus beneficiaryStatus) {
        this.beneficiaryStatus = beneficiaryStatus;
    }

    public String getPermanentAddressLine1() {
        return permanentAddressLine1;
    }

    public void setPermanentAddressLine1(String permanentAddressLine1) {
        this.permanentAddressLine1 = permanentAddressLine1;
    }

    /**
     *
     * @return
     */
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

    public void setPresentAddressLine1(String presentAddressLine1) {
        this.presentAddressLine1 = presentAddressLine1;
    }

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

    /**
     *
     * @return
     */
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

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

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

    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

//    public List<BeneficiarySchemeAttributeValue> getSchemeAttributeValueList()
//    {
//        return schemeAttributeValueList;
//    }
//
//    public void setSchemeAttributeValueList(List<BeneficiarySchemeAttributeValue> schemeAttributeValueList)
//    {
//        this.schemeAttributeValueList = schemeAttributeValueList;
//    }
    public List<BeneficiaryAttachment> getBeneficiaryAttachmentList() {
        return beneficiaryAttachmentList;
    }

    public void setBeneficiaryAttachmentList(List<BeneficiaryAttachment> beneficiaryAttachmentList) {
        this.beneficiaryAttachmentList = beneficiaryAttachmentList;
    }

    /**
     *
     * @return
     */
    public BeneficiaryBiometricInfo getBeneficiaryBiometricInfo() {
        return beneficiaryBiometricInfo;
    }

    public void setBeneficiaryBiometricInfo(BeneficiaryBiometricInfo beneficiaryBiometricInfo) {
        this.beneficiaryBiometricInfo = beneficiaryBiometricInfo;
    }

    /**
     *
     * @return
     */
    public String getFullNameInEnglish() {
        return fullNameInEnglish;
    }

    public void setFullNameInEnglish(String fullNameInEnglish) {
        this.fullNameInEnglish = fullNameInEnglish;
    }

    /**
     *
     * @return
     */
    public String getFullNameInBangla() {
        return fullNameInBangla;
    }

    public void setFullNameInBangla(String fullNameInBangla) {
        this.fullNameInBangla = fullNameInBangla;
    }

    public BeneficiaryDeactivationReasons getBeneficiaryDeactivationReasons() {
        return beneficiaryDeactivationReasons;
    }

    public void setBeneficiaryDeactivationReasons(BeneficiaryDeactivationReasons beneficiaryDeactivationReasons) {
        this.beneficiaryDeactivationReasons = beneficiaryDeactivationReasons;
    }

    /**
     *
     * @return
     */
    public Calendar getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Calendar incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getDeactivationComment() {
        return deactivationComment;
    }

    /**
     *
     * @param deactivationComment
     */
    public void setDeactivationComment(String deactivationComment) {
        this.deactivationComment = deactivationComment;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
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

    /**
     *
     * @return
     */
    public BeneficiarySocioEconomicInfo getBeneficiarySocioEconomicInfo() {
        return beneficiarySocioEconomicInfo;
    }

    public void setBeneficiarySocioEconomicInfo(BeneficiarySocioEconomicInfo beneficiarySocioEconomicInfo) {
        this.beneficiarySocioEconomicInfo = beneficiarySocioEconomicInfo;
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

    public ConceptionTerm getConceptionTerm() {
        return conceptionTerm;
    }

    public void setConceptionTerm(ConceptionTerm conceptionTerm) {
        this.conceptionTerm = conceptionTerm;
    }

    public Integer getConceptionDuration() {
        return conceptionDuration;
    }

    /**
     *
     * @param conceptionDuration
     */
    public void setConceptionDuration(Integer conceptionDuration) {
        this.conceptionDuration = conceptionDuration;
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
    public Calendar getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Calendar enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

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
        Beneficiary other = (Beneficiary) obj;
        if (Id == null) {
            if (other.Id != null) {
                return false;
            }
        } else if (!Id.equals(other.Id)) {
            return false;
        }
        return true;
    }

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
