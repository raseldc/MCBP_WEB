/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

/**
 *
 * @author user
 */
public class BeneficiaryProfile {

    private String benID;

    private String nameEn;
    private String nameBn;

    private String nid;

    private String fatherName;
    private String motherName;

    private String spouseName;
    private String mobileNumber;

    private String dob;

    private String educationLevelBn;
    private String educationLevelEn;

    private String birthPlaceBn;
    private String birthPlaceEn;

    private String religionBn;
    private String religionEn;
    private int religionID;

    private String divisionNameEn;
    private String divisionNameBn;

    private String districNameBn;
    private String districNameEn;

    private String upazilaNameBn;
    private String upazilaNameEn;

    private String unionNameBn;
    private String unionNameEn;

    private String villageNameBn;
    private String villageNameEn;

    private String wardNo;

    private String paymentNameBn;
    private String paymentNameEn;

    private String paymentDate;

    private String bankNameEn;
    private String bankNameBn;

    private String branchNameEn;
    private String branchNameBn;

    private String paymentCycleBn;
    private String paymentCycleEn;

    private String amount;
    private String accountNO;

    private String paymentStatus;
    private String paymentStatus_str;

    private String paymentEFTRefNum;

    private Integer returnCode;

    private Integer approvalStatus;

    public int getReligionID() {
        return religionID;
    }

    public void setReligionID(int religionID) {
        this.religionID = religionID;
    }

    
    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    /**
     *
     * @return
     */
    public String getPaymentEFTRefNum() {
        return paymentEFTRefNum;
    }

    public void setPaymentEFTRefNum(String paymentEFTRefNum) {
        this.paymentEFTRefNum = paymentEFTRefNum;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus_str() {
        return paymentStatus_str;
    }

    public void setPaymentStatus_str(String paymentStatus_str) {
        this.paymentStatus_str = paymentStatus_str;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getVillageNameBn() {
        return villageNameBn;
    }

    /**
     *
     * @param villageNameBn
     */
    public void setVillageNameBn(String villageNameBn) {
        this.villageNameBn = villageNameBn;
    }

    /**
     *
     * @return
     */
    public String getVillageNameEn() {
        return villageNameEn;
    }

    public void setVillageNameEn(String villageNameEn) {
        this.villageNameEn = villageNameEn;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getBenID() {
        return benID;
    }

    public void setBenID(String benID) {
        this.benID = benID;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameBn() {
        return nameBn;
    }

    public void setNameBn(String nameBn) {
        this.nameBn = nameBn;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     *
     * @return
     */
    public String getEducationLevelBn() {
        return educationLevelBn;
    }

    public void setEducationLevelBn(String educationLevelBn) {
        this.educationLevelBn = educationLevelBn;
    }

    public String getEducationLevelEn() {
        return educationLevelEn;
    }

    public void setEducationLevelEn(String educationLevelEn) {
        this.educationLevelEn = educationLevelEn;
    }

    public String getBirthPlaceBn() {
        return birthPlaceBn;
    }

    public void setBirthPlaceBn(String birthPlaceBn) {
        this.birthPlaceBn = birthPlaceBn;
    }

    public String getBirthPlaceEn() {
        return birthPlaceEn;
    }

    public void setBirthPlaceEn(String birthPlaceEn) {
        this.birthPlaceEn = birthPlaceEn;
    }

    public String getReligionBn() {
        return religionBn;
    }

    public void setReligionBn(String religionBn) {
        this.religionBn = religionBn;
    }

    public String getReligionEn() {
        return religionEn;
    }

    public void setReligionEn(String religionEn) {
        this.religionEn = religionEn;
    }

    public String getDivisionNameEn() {
        return divisionNameEn;
    }

    public void setDivisionNameEn(String divisionNameEn) {
        this.divisionNameEn = divisionNameEn;
    }

    public String getDivisionNameBn() {
        return divisionNameBn;
    }

    public void setDivisionNameBn(String divisionNameBn) {
        this.divisionNameBn = divisionNameBn;
    }

    public String getDistricNameBn() {
        return districNameBn;
    }

    public void setDistricNameBn(String districNameBn) {
        this.districNameBn = districNameBn;
    }

    public String getDistricNameEn() {
        return districNameEn;
    }

    public void setDistricNameEn(String districNameEn) {
        this.districNameEn = districNameEn;
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

    public void setUpazilaNameEn(String upazilaNameEn) {
        this.upazilaNameEn = upazilaNameEn;
    }

    public String getUnionNameBn() {
        return unionNameBn;
    }

    public void setUnionNameBn(String unionNameBn) {
        this.unionNameBn = unionNameBn;
    }

    public String getUnionNameEn() {
        return unionNameEn;
    }

    public void setUnionNameEn(String unionNameEn) {
        this.unionNameEn = unionNameEn;
    }

    public String getPaymentNameBn() {
        return paymentNameBn;
    }

    /**
     *
     * @param paymentNameBn
     */
    public void setPaymentNameBn(String paymentNameBn) {
        this.paymentNameBn = paymentNameBn;
    }

    /**
     *
     * @return
     */
    public String getPaymentNameEn() {
        return paymentNameEn;
    }

    public void setPaymentNameEn(String paymentNameEn) {
        this.paymentNameEn = paymentNameEn;
    }

    public String getBankNameEn() {
        return bankNameEn;
    }

    public void setBankNameEn(String bankNameEn) {
        this.bankNameEn = bankNameEn;
    }

    public String getBankNameBn() {
        return bankNameBn;
    }

    /**
     *
     * @param bankNameBn
     */
    public void setBankNameBn(String bankNameBn) {
        this.bankNameBn = bankNameBn;
    }

    public String getBranchNameEn() {
        return branchNameEn;
    }

    /**
     *
     * @param branchNameEn
     */
    public void setBranchNameEn(String branchNameEn) {
        this.branchNameEn = branchNameEn;
    }

    /**
     *
     * @return
     */
    public String getBranchNameBn() {
        return branchNameBn;
    }

    public void setBranchNameBn(String branchNameBn) {
        this.branchNameBn = branchNameBn;
    }

    public String getPaymentCycleBn() {
        return paymentCycleBn;
    }

    public void setPaymentCycleBn(String paymentCycleBn) {
        this.paymentCycleBn = paymentCycleBn;
    }

    public String getPaymentCycleEn() {
        return paymentCycleEn;
    }

    public void setPaymentCycleEn(String paymentCycleEn) {
        this.paymentCycleEn = paymentCycleEn;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     *
     * @return
     */
    public String getAccountNO() {
        return accountNO;
    }

    public void setAccountNO(String accountNO) {
        this.accountNO = accountNO;
    }
}
