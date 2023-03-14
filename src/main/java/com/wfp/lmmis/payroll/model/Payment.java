package com.wfp.lmmis.payroll.model;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.enums.PaymentTypeEnum;
import com.wfp.lmmis.masterdata.model.AccountType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.types.PaymentStatus;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "payment")
public class Payment extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ministry_code", nullable = false)
    private Integer ministryCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_code", referencedColumnName = "code", nullable = false)
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cycle_id", nullable = false)
    private PaymentCycle paymentCycle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beneficiary_id", referencedColumnName = "id", nullable = false)
    private Beneficiary beneficiary;

    @Column(name = "nid", nullable = false)
    private String nid;

    @Column(name = "allowance_amount", nullable = false)
    private double allowanceAmount;

    @Column(name = "paid_amount", nullable = true)
    private float paidAmount;

    @Column(name = "payment_status", nullable = true)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_type", nullable = false)
    private PaymentTypeEnum paymentType;

    @Column(name = "payment_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Calendar paymentDate;

    @Column(name = "beneficiary_name", nullable = false)
    private String benefiaryName;

    @Column(name = "mobile_number", nullable = true)
    private Integer mobileNumber;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "bank", referencedColumnName = "name_in_english", nullable = false)
    @Column(name = "bank", nullable = false)
    private String bank;
    @Column(name = "bank_bn", nullable = false)
    private String bankBn;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "branch", referencedColumnName = "name_in_english", nullable = false)
    @Column(name = "branch", nullable = true)
    private String branch;
    @Column(name = "branch_bn", nullable = true)
    private String branchBn;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_type_id", nullable = true)
    private AccountType accountType;
    @Column(name = "eft_reference_number", nullable = true)
    private String eftReferenceNumber;
    @Column(name = "payment_uid", nullable = true)
    private Integer paymentUid;
    @Column(name = "returned_code", nullable = true)
    private Integer returnedCode;
    @Column(name = "returned_text", nullable = true)
    private String returnedText;
    @Column(name = "branch_id", nullable = true)
    private Integer branchId;
    @Column(name = "account_number", nullable = false)
    private String accountNumber;
    @Column(name = "routing_number", nullable = true)
    private String routingNumber;
    @Column(name = "division_code", nullable = false)
    private String divisionCode;
    @Column(name = "district_code", nullable = false)
    private String districtCode;
    @Column(name = "upazila_code", nullable = false)
    private String upazilaCode;
    @Column(name = "union_code", nullable = false)
    private String unionCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "division_id", nullable = false)
    private Division division;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "upazila_id", nullable = false)
    private Upazilla upazilla;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "union_id", nullable = false)
    private Union union;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payroll_summary_id", nullable = false)
    private PayrollSummary payrollSummary;

    @Column(name = "comments", nullable = true)
    private String comments;

    @Column(name = "approval_status", nullable = true)
    private Boolean approvalStatus;

    @Column(name = "approval_comments", nullable = true)
    private String approvalComments;

    @Column(name = "payment_charge", nullable = true)
    private Double paymentCharge;

    public Double getPaymentCharge() {
        return paymentCharge;
    }

    /**
     *
     * @param paymentCharge
     */
    public void setPaymentCharge(Double paymentCharge) {
        this.paymentCharge = paymentCharge;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMinistryCode() {
        return ministryCode;
    }

    public void setMinistryCode(Integer ministryCode) {
        this.ministryCode = ministryCode;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public PaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(PaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public double getAllowanceAmount() {
        return allowanceAmount;
    }

    public void setAllowanceAmount(double allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    /**
     *
     * @return
     */
    public float getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public Calendar getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Calendar paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getBenefiaryName() {
        return benefiaryName;
    }

    /**
     *
     * @param benefiaryName
     */
    public void setBenefiaryName(String benefiaryName) {
        this.benefiaryName = benefiaryName;
    }

    /**
     *
     * @return
     */
    public Integer getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Integer mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    /**
     *
     * @param branch
     */
    public void setBranch(String branch) {
        this.branch = branch;
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
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    /**
     *
     * @param districtCode
     */
    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    /**
     *
     * @return
     */
    public String getUpazilaCode() {
        return upazilaCode;
    }

    public void setUpazilaCode(String upazilaCode) {
        this.upazilaCode = upazilaCode;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    /**
     *
     * @return
     */
    public Upazilla getUpazilla() {
        return upazilla;
    }

    public void setUpazilla(Upazilla upazilla) {
        this.upazilla = upazilla;
    }

    /**
     *
     * @return
     */
    public Union getUnion() {
        return union;
    }

    /**
     *
     * @param union
     */
    public void setUnion(Union union) {
        this.union = union;
    }

    public PayrollSummary getPayrollSummary() {
        return payrollSummary;
    }

    /**
     *
     * @param payrollSummary
     */
    public void setPayrollSummary(PayrollSummary payrollSummary) {
        this.payrollSummary = payrollSummary;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalComments() {
        return approvalComments;
    }

    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    /**
     *
     * @param routingNumber
     */
    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
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

    public String getEftReferenceNumber() {
        return eftReferenceNumber;
    }

    public void setEftReferenceNumber(String eftReferenceNumber) {
        this.eftReferenceNumber = eftReferenceNumber;
    }

    public Integer getPaymentUid() {
        return paymentUid;
    }

    public void setPaymentUid(Integer paymentUid) {
        this.paymentUid = paymentUid;
    }

    public Integer getReturnedCode() {
        return returnedCode;
    }

    public void setReturnedCode(Integer returnedCode) {
        this.returnedCode = returnedCode;
    }

    public String getReturnedText() {
        return returnedText;
    }

    public void setReturnedText(String returnedText) {
        this.returnedText = returnedText;
    }

    /**
     *
     * @return
     */
    public String getBankBn() {
        return bankBn;
    }

    public void setBankBn(String bankBn) {
        this.bankBn = bankBn;
    }

    public String getBranchBn() {
        return branchBn;
    }

    /**
     *
     * @param branchBn
     */
    public void setBranchBn(String branchBn) {
        this.branchBn = branchBn;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
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
        final Payment other = (Payment) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
