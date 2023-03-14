/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.model;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.PayrollListType;
import com.wfp.lmmis.types.PaymentStatus;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Philip
 */
@Entity
@Table(name = "payment_view")
public class PaymentView implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "beneficiary_id")
    private Integer beneficiaryId;
    @Column(name = "beneficiary_name_bn")
    private String fullNameInBangla;
    @Column(name = "beneficiary_name_en")
    private String fullNameInEnglish;
    @Column(name = "nid")
    private String nid;
    @Column(name = "father_name")
    private String fatherName;
    @Column(name = "mother_name")
    private String motherName;
    @Column(name = "spouse_name")
    private String spouseName;
    @Column(name = "mobile_number")
    private String mobileNo;
    @Column(name = "bank_name_en")
    private String bankEn;
    @Column(name = "bank_name_bn")
    private String bankBn;
    @Column(name = "branch_name_en")
    private String branchEn;
    @Column(name = "branch_name_bn")
    private String branchBn;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "approval_status")
    private boolean approvalStatus;
    @Column(name = "returned_code")
    private Integer returnedCode;
    @Column(name = "eft_reference_number", nullable = true)
    private String eftReferenceNumber;

    @Column(name = "division_name_bn")
    private String divisionNameBn;
    @Column(name = "division_name_en")
    private String divisionNameEn;

    @Column(name = "district_name_bn")
    private String districtNameBn;
    @Column(name = "district_name_en")
    private String districtNameEn;

    @Column(name = "upazila_name_bn")
    private String upazilaNameBn;
    @Column(name = "upazila_name_en")
    private String upazilaNameEn;

    @Column(name = "union_name_bn")
    private String unionNameBn;
    @Column(name = "union_name_en")
    private String unionNameEn;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "scheme_id")
    private Integer schemeId;
    @Column(name = "cycle_id")
    private Integer paymentCycleId;
    @Column(name = "division_id")
    private Integer divisionId;
    @Column(name = "district_id")
    private Integer districtId;
    @Column(name = "upazila_id")
    private Integer upazilaId;
    @Column(name = "union_id")
    private Integer unionId;
//    @Column(name = "payroll_summary_id")
//    private Integer payrollSummaryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payroll_summary_id", nullable = false)
    private PayrollSummary payrollSummary;

    @Column(name = "allowance_amount")
    private Double allowanceAmount;
    @Column(name = "applicant_type")
    private ApplicantType applicantType;
    @Column(name = "payroll_list_type")
    private PayrollListType payrollListType;

    public PayrollListType getPayrollListType() {
        return payrollListType;
    }

    /**
     *
     * @param payrollListType
     */
    public void setPayrollListType(PayrollListType payrollListType) {
        this.payrollListType = payrollListType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBeneficiaryId() {
        return beneficiaryId;
    }

    /**
     *
     * @param beneficiaryId
     */
    public void setBeneficiaryId(Integer beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getFullNameInBangla() {
        return fullNameInBangla;
    }

    public void setFullNameInBangla(String fullNameInBangla) {
        this.fullNameInBangla = fullNameInBangla;
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

    public String getMobileNo() {
        return mobileNo;
    }

    /**
     *
     * @param mobileNo
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    /**
     *
     * @return
     */
    public Integer getUnionId() {
        return unionId;
    }

    public void setUnionId(Integer unionId) {
        this.unionId = unionId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    /**
     *
     * @param accountNo
     */
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankEn() {
        return bankEn;
    }

    public void setBankEn(String bankEn) {
        this.bankEn = bankEn;
    }

    public String getBankBn() {
        return bankBn;
    }

    public void setBankBn(String bankBn) {
        this.bankBn = bankBn;
    }

    /**
     *
     * @return
     */
    public String getBranchEn() {
        return branchEn;
    }

    public void setBranchEn(String branchEn) {
        this.branchEn = branchEn;
    }

    /**
     *
     * @return
     */
    public String getBranchBn() {
        return branchBn;
    }

    public void setBranchBn(String branchBn) {
        this.branchBn = branchBn;
    }

    public String getUnionNameBn() {
        return unionNameBn;
    }

    public void setUnionNameBn(String unionNameBn) {
        this.unionNameBn = unionNameBn;
    }

    /**
     *
     * @return
     */
    public String getUnionNameEn() {
        return unionNameEn;
    }

    public void setUnionNameEn(String unionNameEn) {
        this.unionNameEn = unionNameEn;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public PayrollSummary getPayrollSummary() {
        return payrollSummary;
    }

    public void setPayrollSummary(PayrollSummary payrollSummary) {
        this.payrollSummary = payrollSummary;
    }

//
//    public Integer getPayrollSummaryId() {
//        return payrollSummaryId;
//    }
//
//    public void setPayrollSummaryId(Integer payrollSummaryId) {
//        this.payrollSummaryId = payrollSummaryId;
//    }
    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Integer schemeId) {
        this.schemeId = schemeId;
    }

    /**
     *
     * @return
     */
    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getUpazilaId() {
        return upazilaId;
    }

    public void setUpazilaId(Integer upazilaId) {
        this.upazilaId = upazilaId;
    }

    public Double getAllowanceAmount() {
        return allowanceAmount;
    }

    public void setAllowanceAmount(Double allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    /**
     *
     * @return
     */
    public Integer getPaymentCycleId() {
        return paymentCycleId;
    }

    public void setPaymentCycleId(Integer paymentCycleId) {
        this.paymentCycleId = paymentCycleId;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    /**
     *
     * @param fatherName
     */
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
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

    public String getDivisionNameBn() {
        return divisionNameBn;
    }

    public void setDivisionNameBn(String divisionNameBn) {
        this.divisionNameBn = divisionNameBn;
    }

    public String getDivisionNameEn() {
        return divisionNameEn;
    }

    public void setDivisionNameEn(String divisionNameEn) {
        this.divisionNameEn = divisionNameEn;
    }

    /**
     *
     * @return
     */
    public String getDistrictNameBn() {
        return districtNameBn;
    }

    public void setDistrictNameBn(String districtNameBn) {
        this.districtNameBn = districtNameBn;
    }

    public String getDistrictNameEn() {
        return districtNameEn;
    }

    /**
     *
     * @param districtNameEn
     */
    public void setDistrictNameEn(String districtNameEn) {
        this.districtNameEn = districtNameEn;
    }

    public String getUpazilaNameBn() {
        return upazilaNameBn;
    }

    public void setUpazilaNameBn(String upazilaNameBn) {
        this.upazilaNameBn = upazilaNameBn;
    }

    public String getUpazilaNameEn() {
        return upazilaNameEn;
    }

    /**
     *
     * @param upazilaNameEn
     */
    public void setUpazilaNameEn(String upazilaNameEn) {
        this.upazilaNameEn = upazilaNameEn;
    }

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     *
     * @return
     */
    public Integer getReturnedCode() {
        return returnedCode;
    }

    public void setReturnedCode(Integer returnedCode) {
        this.returnedCode = returnedCode;
    }

    public String getEftReferenceNumber() {
        return eftReferenceNumber;
    }

    public void setEftReferenceNumber(String eftReferenceNumber) {
        this.eftReferenceNumber = eftReferenceNumber;
    }

}
