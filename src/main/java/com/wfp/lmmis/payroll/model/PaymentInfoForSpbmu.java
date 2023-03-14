/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.model;

import com.wfp.lmmis.enums.PaymentTypeEnum;
import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Philip
 */
/**
 * this class will be used for sending pay_list to SPBMU as web service
 *
 * @author Philip
 */
public class PaymentInfoForSpbmu implements Serializable {

    private String accountName;
    private String accountNumber;
    private String accountType;
    private double allowanceAmount;
    private String bankName;
    private String beneficiaryName;
    private String branchName;
    private Integer cycleId;
    private Integer district;
    private Integer division;
    private Integer eunion;
    private Integer upozila;
    private Integer id;
    private Integer ministryCode;
    private Integer mobile;
    private long nid;
    private Integer paymentStatus;
    private String paymentType;
    private String referenceNo;
    private Integer routingNumber;
    private Integer schemeCode;

    public PaymentInfoForSpbmu(String accountName, String accountNumber, String accountType, double allowanceAmount, String bankName, String beneficiaryName, String branchName, Integer cycleId, Integer district, Integer division, Integer eunion, Integer upozila, Integer id, Integer ministryCode, Integer mobile, long nid, Integer paymentStatus, String paymentType, String referenceNo, Integer routingNumber, Integer schemeCode) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.allowanceAmount = allowanceAmount;
        this.bankName = bankName;
        this.beneficiaryName = beneficiaryName;
        this.branchName = branchName;
        this.cycleId = cycleId;
        this.district = district;
        this.division = division;
        this.eunion = eunion;
        this.upozila = upozila;
        this.id = id;
        this.ministryCode = ministryCode;
        this.mobile = mobile;
        this.nid = nid;
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.referenceNo = referenceNo;
        this.routingNumber = routingNumber;
        this.schemeCode = schemeCode;
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
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getAllowanceAmount() {
        return allowanceAmount;
    }

    public void setAllowanceAmount(double allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
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

    public Integer getCycleId() {
        return cycleId;
    }

    public void setCycleId(Integer cycleId) {
        this.cycleId = cycleId;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Integer getDivision() {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(Integer division) {
        this.division = division;
    }

    public Integer getEunion() {
        return eunion;
    }

    public void setEunion(Integer eunion) {
        this.eunion = eunion;
    }

    public Integer getUpozila() {
        return upozila;
    }

    public void setUpozila(Integer upozila) {
        this.upozila = upozila;
    }

    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMinistryCode() {
        return ministryCode;
    }

    public void setMinistryCode(Integer ministryCode) {
        this.ministryCode = ministryCode;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public long getNid() {
        return nid;
    }

    public void setNid(long nid) {
        this.nid = nid;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    /**
     *
     * @param paymentStatus
     */
    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     *
     * @return
     */
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Integer getRoutingNumber() {
        return routingNumber;
    }

    /**
     *
     * @param routingNumber
     */
    public void setRoutingNumber(Integer routingNumber) {
        this.routingNumber = routingNumber;
    }

    /**
     *
     * @return
     */
    public Integer getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(Integer schemeCode) {
        this.schemeCode = schemeCode;
    }

}
