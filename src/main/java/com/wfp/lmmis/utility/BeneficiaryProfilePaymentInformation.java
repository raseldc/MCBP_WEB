/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class BeneficiaryProfilePaymentInformation {

    private String paymentDate;

    private String paymentNameBn;
    private String paymentNameEn;

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

    /**
     *
     * @return
     */
    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    /**
     *
     * @param returnCode
     */
    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getPaymentEFTRefNum() {
        return paymentEFTRefNum;
    }

    public void setPaymentEFTRefNum(String paymentEFTRefNum) {
        this.paymentEFTRefNum = paymentEFTRefNum;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     *
     * @param paymentStatus
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
    public String getPaymentNameBn() {
        return paymentNameBn;
    }

    public void setPaymentNameBn(String paymentNameBn) {
        this.paymentNameBn = paymentNameBn;
    }

    public String getPaymentNameEn() {
        return paymentNameEn;
    }

    public void setPaymentNameEn(String paymentNameEn) {
        this.paymentNameEn = paymentNameEn;
    }

    /**
     *
     * @return
     */
    public String getBankNameEn() {
        return bankNameEn;
    }

    public void setBankNameEn(String bankNameEn) {
        this.bankNameEn = bankNameEn;
    }

    /**
     *
     * @return
     */
    public String getBankNameBn() {
        return bankNameBn;
    }

    public void setBankNameBn(String bankNameBn) {
        this.bankNameBn = bankNameBn;
    }

    /**
     *
     * @return
     */
    public String getBranchNameEn() {
        return branchNameEn;
    }

    public void setBranchNameEn(String branchNameEn) {
        this.branchNameEn = branchNameEn;
    }

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

    /**
     *
     * @param amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccountNO() {
        return accountNO;
    }

    public void setAccountNO(String accountNO) {
        this.accountNO = accountNO;
    }

}
