/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.data;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.types.BeneficiaryStatus;
import java.math.BigInteger;
import java.util.Calendar;

/**
 *
 * @author PCUser
 */
public class ApplicantReportData {

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
    private String factory;
    private String address;
    private String address2;
    private String score;
    private String socioEconomicInfo;
    private String blankRecommendation; // for showing blank column
    private String blankRemarks; // for showing blank column
    private ApplicantType applicantType;
    private String recommendationStatus; // for showing in recommendation report
    private String approvalStatus; // for showing in upazila approval report
    private ApplicationStatus applicationStatus;
    private String remarks;
    private String paymentProviderName;
    private String bankName;
    private String branchName;
    private String pOBranch;
    private String mobileBankingProviderName;
    private String accountNo;
    private String month;
    private Calendar creationDate;
    private String conceptionDuration;
    private String conceptionTerm;
    private String lmMisStatus;
    private String status;
    private String oldGeo;
    private String oldDivision;
    private String oldDistrict;
    private String oldUpazila;
    private String oldUnion;
    private String oldFiscalYearName;
    private String oldSchemeName;

    private String oldConceptionDuration;
    private String oldConceptionTerm;

    private String ancStatus;

    public ApplicantReportData(String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String factory, String address) {
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
        this.factory = factory;
        this.address = address;
    }

    public ApplicantReportData(Integer benID, String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String address, Integer score) {
        this.benID = benID.toString();
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
        this.address = address;
        this.score = score.toString();
    }

    public ApplicantReportData(Integer benID, String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName,
            Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String wardNo, String address, Integer score) {
        this.benID = benID.toString();
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
        this.address = address;
        this.score = score.toString();
    }

    /**
     * This method is used by prioritization report
     *
     * @param benID
     * @param benName
     * @param nationalID
     * @param fatherName
     * @param motherName
     * @param spouseName
     * @param dob
     * @param mobileNo
     * @param division
     * @param district
     * @param upazila
     * @param union
     * @param address
     * @param score
     * @param applicantType
     */
    public ApplicantReportData(Integer benID, String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String address, String address2, Integer score, ApplicantType applicantType) {
        this.benID = benID.toString();
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
        this.address = address;
        this.address2 = address2;
        this.score = score.toString();
        this.applicantType = applicantType;
    }

    /**
     *
     * @param benID
     * @param benName
     * @param nationalID
     * @param fatherName
     * @param motherName
     * @param spouseName
     * @param dob
     * @param mobileNo
     * @param division
     * @param district
     * @param upazila
     * @param union
     * @param address
     * @param score
     * @param applicantType
     * @param applicationStatus
     */
    public ApplicantReportData(Integer benID, String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String address, Integer score, ApplicantType applicantType, ApplicationStatus applicationStatus) {
        this.benID = benID.toString();
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
        this.address = address;
        this.score = score.toString();
        this.applicantType = applicantType;
        this.applicationStatus = applicationStatus;
    }

    /**
     * Constructor for generating report for Applicant detail with payment info
     *
     * @param district
     * @param accountNo
     * @param factory
     * @param motherName
     */
    public ApplicantReportData(String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String address, String bankName, String branchName, String pOBranch, String mobileBankingProviderName, String accountNo, ApplicationStatus applicationStatus, String factory, Calendar creationDate) {
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
        this.address = address;
        this.applicationStatus = applicationStatus;
        this.bankName = bankName;
        this.branchName = branchName;
        this.pOBranch = pOBranch;
        this.mobileBankingProviderName = mobileBankingProviderName;
        this.accountNo = accountNo;
        this.factory = factory;
        this.creationDate = creationDate;
    }

    public ApplicantReportData(String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo,
            String division, String district, String upazila, String union, String wardNo, String address, String bankName, String branchName, String pOBranch, String mobileBankingProviderName, String accountNo, ApplicationStatus applicationStatus, String factory, Calendar creationDate) {
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
        this.address = address;
        this.applicationStatus = applicationStatus;
        this.bankName = bankName;
        this.branchName = branchName;
        this.pOBranch = pOBranch;
        this.mobileBankingProviderName = mobileBankingProviderName;
        this.accountNo = accountNo;
        this.factory = factory;
        this.creationDate = creationDate;
    }

    public ApplicantReportData(String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo,
            String division, String district, String upazila, String union, String wardNo, String address, String bankName, String branchName, String pOBranch,
            String mobileBankingProviderName, String accountNo, ApplicationStatus applicationStatus, String factory, Calendar creationDate, Integer ancStatusInt) {
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
        this.address = address;
        this.applicationStatus = applicationStatus;
        this.bankName = bankName;
        this.branchName = branchName;
        this.pOBranch = pOBranch;
        this.mobileBankingProviderName = mobileBankingProviderName;
        this.accountNo = accountNo;
        this.factory = factory;
        this.creationDate = creationDate;
        ancStatusInt = ancStatusInt == null ? 0 : ancStatusInt;
        if (ancStatusInt == 0) {
            this.ancStatus = "Not Check";
        } else if (ancStatusInt == 1) {
            this.ancStatus = "Success";
        } else if (ancStatusInt == 0) {
            this.ancStatus = "Failed";
        }
    }

    public ApplicantReportData() {
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

    public String getBenID() {
        return benID;
    }

    public void setBenID(String benID) {
        this.benID = benID;
    }

    public String getBenName() {
        return benName;
    }

    /**
     *
     * @param benName
     */
    public void setBenName(String benName) {
        this.benName = benName;
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

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public Calendar getDob() {
        return dob;
    }

    /**
     *
     * @param dob
     */
    public void setDob(Calendar dob) {
        this.dob = dob;
    }

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
    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    /**
     *
     * @return
     */
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUpazila() {
        return upazila;
    }

    /**
     *
     * @param upazila
     */
    public void setUpazila(String upazila) {
        this.upazila = upazila;
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSocioEconomicInfo() {
        return socioEconomicInfo;
    }

    public void setSocioEconomicInfo(String socioEconomicInfo) {
        this.socioEconomicInfo = socioEconomicInfo;
    }

    public String getBlankRecommendation() {
        return blankRecommendation;
    }

    public void setBlankRecommendation(String blankRecommendation) {
        this.blankRecommendation = blankRecommendation;
    }

    public String getBlankRemarks() {
        return blankRemarks;
    }

    public void setBlankRemarks(String blankRemarks) {
        this.blankRemarks = blankRemarks;
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

    public String getRecommendationStatus() {
        return recommendationStatus;
    }

    /**
     *
     * @param recommendationStatus
     */
    public void setRecommendationStatus(String recommendationStatus) {
        this.recommendationStatus = recommendationStatus;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    /**
     *
     * @param approvalStatus
     */
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getpOBranch() {
        return pOBranch;
    }

    public void setpOBranch(String pOBranch) {
        this.pOBranch = pOBranch;
    }

    /**
     *
     * @return
     */
    public String getMobileBankingProviderName() {
        return mobileBankingProviderName;
    }

    /**
     *
     * @param mobileBankingProviderName
     */
    public void setMobileBankingProviderName(String mobileBankingProviderName) {
        this.mobileBankingProviderName = mobileBankingProviderName;
    }

    public String getAddress2() {
        return address2;
    }

    /**
     *
     * @param address2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getLmMisStatus() {
        return lmMisStatus;
    }

    /**
     *
     * @param lmMisStatus
     */
    public void setLmMisStatus(String lmMisStatus) {
        this.lmMisStatus = lmMisStatus;
    }

    /**
     *
     * @return
     */
    public String getOldGeo() {
        return oldGeo;
    }

    /**
     *
     * @param oldGeo
     */
    public void setOldGeo(String oldGeo) {
        this.oldGeo = oldGeo;
    }

    public String getOldDivision() {
        return oldDivision;
    }

    public void setOldDivision(String oldDivision) {
        this.oldDivision = oldDivision;
    }

    public String getOldDistrict() {
        return oldDistrict;
    }

    /**
     *
     * @param oldDistrict
     */
    public void setOldDistrict(String oldDistrict) {
        this.oldDistrict = oldDistrict;
    }

    /**
     *
     * @return
     */
    public String getOldUpazila() {
        return oldUpazila;
    }

    public void setOldUpazila(String oldUpazila) {
        this.oldUpazila = oldUpazila;
    }

    /**
     *
     * @return
     */
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

    public String getOldConceptionTerm() {
        return oldConceptionTerm;
    }

    public void setOldConceptionTerm(String oldConceptionTerm) {
        this.oldConceptionTerm = oldConceptionTerm;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    /**
     *
     * @return
     */
    public String getConceptionDuration() {
        return conceptionDuration;
    }

    public void setConceptionDuration(String conceptionDuration) {
        this.conceptionDuration = conceptionDuration;
    }

    /**
     *
     * @return
     */
    public String getConceptionTerm() {
        return conceptionTerm;
    }

    public void setConceptionTerm(String conceptionTerm) {
        this.conceptionTerm = conceptionTerm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAncStatus() {
        return ancStatus;
    }

    public void setAncStatus(String ancStatus) {
        this.ancStatus = ancStatus;
    }

}
