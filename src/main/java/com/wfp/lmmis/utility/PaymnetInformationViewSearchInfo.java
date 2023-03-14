/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.enums.ApplicantType;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class PaymnetInformationViewSearchInfo {

    private Integer fiscalYear;
    private Integer scheme;
    private Integer applicantId;
    private String mobileNo;
    private String accountNo;
    private Integer divisionId;
    private Integer districtId;
    private Integer upazilaId;
    private Integer unionId;
    private Integer wardNo;
    private Integer villageId;
    private Integer accountType;
    private String accountNumber;
    private Integer benId;
    private int paymentStatus;
    private int eftReferenceNumberNotNull;
    private int informationNotUpdate;
    private int isExportedToSpbmu;
    private int paymentCycle;

    private String applicantTypeStr;
    private ApplicantType applicantType;

    private Integer bkmeaFactoryId;
    private Integer bgmeaFactoryId;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApplicantTypeStr() {
        return applicantTypeStr;
    }

    public void setApplicantTypeStr(String applicantTypeStr) {
        this.applicantTypeStr = applicantTypeStr;
    }

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public Integer getBkmeaFactoryId() {
        return bkmeaFactoryId;
    }

    public void setBkmeaFactoryId(Integer bkmeaFactoryId) {
        this.bkmeaFactoryId = bkmeaFactoryId;
    }

    /**
     *
     * @return
     */
    public Integer getBgmeaFactoryId() {
        return bgmeaFactoryId;
    }

    public void setBgmeaFactoryId(Integer bgmeaFactoryId) {
        this.bgmeaFactoryId = bgmeaFactoryId;
    }

    public int getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(int paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public int getIsExportedToSpbmu() {
        return isExportedToSpbmu;
    }

    public void setIsExportedToSpbmu(int isExportedToSpbmu) {
        this.isExportedToSpbmu = isExportedToSpbmu;
    }

    public int getInformationNotUpdate() {
        return informationNotUpdate;
    }

    /**
     *
     * @param informationNotUpdate
     */
    public void setInformationNotUpdate(int informationNotUpdate) {
        this.informationNotUpdate = informationNotUpdate;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    /**
     *
     * @param paymentStatus
     */
    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getEftReferenceNumberNotNull() {
        return eftReferenceNumberNotNull;
    }

    public void setEftReferenceNumberNotNull(int eftReferenceNumberNotNull) {
        this.eftReferenceNumberNotNull = eftReferenceNumberNotNull;
    }

    public Integer getBenId() {
        return benId;
    }

    public void setBenId(Integer benId) {
        this.benId = benId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    private String nid;

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Integer getScheme() {
        return scheme;
    }

    public void setScheme(Integer scheme) {
        this.scheme = scheme;
    }

    /**
     *
     * @return
     */
    public Integer getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
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

    public Integer getUnionId() {
        return unionId;
    }

    public void setUnionId(Integer unionId) {
        this.unionId = unionId;
    }

    public Integer getWardNo() {
        return wardNo;
    }

    public void setWardNo(Integer wardNo) {
        this.wardNo = wardNo;
    }

    public Integer getVillageId() {
        return villageId;
    }

    /**
     *
     * @param villageId
     */
    public void setVillageId(Integer villageId) {
        this.villageId = villageId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    /**
     *
     * @return
     */
    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

}
