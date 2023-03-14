/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.data;

import com.wfp.lmmis.types.BeneficiaryStatus;
import java.math.BigInteger;
import java.util.Calendar;

/**
 *
 * @author Philip
 */
public class BeneficiaryReportData {

    private String benID;
    private String benName;
    private String benNameBn;
    private String benNameEn;
    private String nationalID;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private Calendar dob;
    private String mobileNo;
    private String division;
    private String district;
    private String upazila;
    private String union;
    private String wardNo;
    private String village;
    private String address;
    private BeneficiaryStatus beneficiaryStatus;
    private String status;
    private String paymentProviderName;
    private String bankName;
    private String branchName;
    private String pOBranch;
    private String mobileBankingProviderName;
    private String accountNo;
    private String factory;
    private String conceptionDuration;
    private String conceptionTerm;

    private String lmMisStatus;

    private String oldGeo;
    private String oldDivision;
    private String oldDistrict;
    private String oldUpazila;
    private String oldUnion;
    private String oldFiscalYearName;
    private String oldSchemeName;

    private String oldConceptionDuration;
    private String oldConceptionTerm;

    /**
     * This constructor is used to generate beneficiary details
     * @param benName
     * @param upazila
     * @param spouseName
     * @param union
     * @param bankName
     */
    public BeneficiaryReportData(String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String bankName, String mobileBankingProviderName, String accountNo, BeneficiaryStatus beneficiaryStatus, String factory, String wardNo, String village) {
        this.benName = benName;
        this.nationalID = nationalID.toString();
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.spouseName = spouseName;
        this.dob = dob;
        this.mobileNo = mobileNo != null ? "0" + mobileNo : "";
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
        this.wardNo = wardNo;
        this.village = village;
        this.beneficiaryStatus = beneficiaryStatus;
        this.bankName = bankName;
        this.mobileBankingProviderName = mobileBankingProviderName;
        this.accountNo = accountNo;
        this.factory = factory;
    }

    /**
     * This constructor is used to generate beneficiary detail with payment data
     *
     * @param benNameEn
     * @param accountNo
     */
    public BeneficiaryReportData(String benNameBn, String benNameEn, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String address, String bankName, String branchName, String pOBranch, String mobileBankingProviderName, String accountNo, BeneficiaryStatus beneficiaryStatus, String factory, String wardNo, String village) {
        this.benNameBn = benNameBn;
        this.benNameEn = benNameEn;
        this.nationalID = nationalID.toString();
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.spouseName = spouseName;
        this.dob = dob;
        this.mobileNo = mobileNo != null ? "0" + mobileNo : "";
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
        this.wardNo = wardNo;
        this.village = village;
        this.address = address;
        this.beneficiaryStatus = beneficiaryStatus;
        this.bankName = bankName;
        this.branchName = branchName;
        this.pOBranch = pOBranch;
        this.mobileBankingProviderName = mobileBankingProviderName;
        this.accountNo = accountNo;
        this.factory = factory;
    }

    public BeneficiaryReportData(String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String address, String bankName, String branchName, String pOBranch, String mobileBankingProviderName, String accountNo, BeneficiaryStatus beneficiaryStatus, String wardNo, String village) {
        this.benName = benName;
        this.nationalID = nationalID.toString();
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.spouseName = spouseName;
        this.dob = dob;
        this.mobileNo = mobileNo != null ? "0" + mobileNo : "";
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
        this.wardNo = wardNo;
        this.village = village;
        this.address = address;
        this.beneficiaryStatus = beneficiaryStatus;
        this.bankName = bankName;
        this.branchName = branchName;
        this.pOBranch = pOBranch;
        this.mobileBankingProviderName = mobileBankingProviderName;
        this.accountNo = accountNo;
    }

    /**
     *
     * @return
     */
    public String getLmMisStatus() {
        return lmMisStatus;
    }

    public void setLmMisStatus(String lmMisStatus) {
        this.lmMisStatus = lmMisStatus;
    }

    /**
     *
     */
    public BeneficiaryReportData() {
    }

    public String getBenID() {
        return benID;
    }

    /**
     *
     * @param benID
     */
    public void setBenID(String benID) {
        this.benID = benID;
    }

    public String getBenName() {
        return benName;
    }

    public void setBenName(String benName) {
        this.benName = benName;
    }

    public String getBenNameBn() {
        return benNameBn;
    }

    public void setBenNameBn(String benNameBn) {
        this.benNameBn = benNameBn;
    }

    public String getBenNameEn() {
        return benNameEn;
    }

    public void setBenNameEn(String benNameEn) {
        this.benNameEn = benNameEn;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
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

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public Calendar getDob() {
        return dob;
    }

    public void setDob(Calendar dob) {
        this.dob = dob;
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

    public BeneficiaryStatus getBeneficiaryStatus() {
        return beneficiaryStatus;
    }

    public void setBeneficiaryStatus(BeneficiaryStatus beneficiaryStatus) {
        this.beneficiaryStatus = beneficiaryStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public String getDivision() {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {
        return district;
    }

    /**
     *
     * @param district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUpazila() {
        return upazila;
    }

    public void setUpazila(String upazila) {
        this.upazila = upazila;
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    /**
     *
     * @return
     */
    public String getWardNo() {
        return wardNo;
    }

    /**
     *
     * @param wardNo
     */
    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getPaymentProviderName() {
        return paymentProviderName;
    }

    public void setPaymentProviderName(String paymentProviderName) {
        this.paymentProviderName = paymentProviderName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMobileBankingProviderName() {
        return mobileBankingProviderName;
    }

    public void setMobileBankingProviderName(String mobileBankingProviderName) {
        this.mobileBankingProviderName = mobileBankingProviderName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getBranchName() {
        return branchName;
    }

    /**
     *
     * @param branchName
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getpOBranch() {
        return pOBranch;
    }

    /**
     *
     * @param pOBranch
     */
    public void setpOBranch(String pOBranch) {
        this.pOBranch = pOBranch;
    }

    public String getConceptionDuration() {
        return conceptionDuration;
    }

    public void setConceptionDuration(String conceptionDuration) {
        this.conceptionDuration = conceptionDuration;
    }

    public String getConceptionTerm() {
        return conceptionTerm;
    }

    /**
     *
     * @param conceptionTerm
     */
    public void setConceptionTerm(String conceptionTerm) {
        this.conceptionTerm = conceptionTerm;
    }

    public String getOldGeo() {
        return oldGeo;
    }

    public void setOldGeo(String oldGeo) {
        this.oldGeo = oldGeo;
    }

    public String getOldDivision() {
        return oldDivision;
    }

    public void setOldDivision(String oldDivision) {
        this.oldDivision = oldDivision;
    }

    /**
     *
     * @return
     */
    public String getOldDistrict() {
        return oldDistrict;
    }

    public void setOldDistrict(String oldDistrict) {
        this.oldDistrict = oldDistrict;
    }

    public String getOldUpazila() {
        return oldUpazila;
    }

    public void setOldUpazila(String oldUpazila) {
        this.oldUpazila = oldUpazila;
    }

    public String getOldUnion() {
        return oldUnion;
    }

    public void setOldUnion(String oldUnion) {
        this.oldUnion = oldUnion;
    }

    public String getOldFiscalYearName() {
        return oldFiscalYearName;
    }

    public void setOldFiscalYearName(String oldFiscalYearName) {
        this.oldFiscalYearName = oldFiscalYearName;
    }

    public String getOldSchemeName() {
        return oldSchemeName;
    }

    public void setOldSchemeName(String oldSchemeName) {
        this.oldSchemeName = oldSchemeName;
    }

    public String getOldConceptionDuration() {
        return oldConceptionDuration;
    }

    public void setOldConceptionDuration(String oldConceptionDuration) {
        this.oldConceptionDuration = oldConceptionDuration;
    }

    /**
     *
     * @return
     */
    public String getOldConceptionTerm() {
        return oldConceptionTerm;
    }

    public void setOldConceptionTerm(String oldConceptionTerm) {
        this.oldConceptionTerm = oldConceptionTerm;
    }

}
