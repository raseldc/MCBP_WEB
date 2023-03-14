/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.data;

import com.wfp.lmmis.enums.PaymentTypeEnum;
import java.text.DecimalFormat;

/**
 *
 * @author Philip
 */
public class PaymentReportData {

    private String division;
    private String district;
    private String upazila;
    private String union;
    private String unionCount;
    private String name;
    private String nid;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private String mobileNo;
    private String accountNo;
    private String paymentProvider;
    private String branch;
    private String allowanceAmount;
    private String totalAllowanceAmount;
    private String beneficiaryCount;
    private String benTotal;
    private String factory;
    DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public PaymentReportData() {
    }

    public PaymentReportData(String name, String nid, String fatherName, String motherName, String spouseName, String mobileNo, String accountNo, String paymentProvider, String branch, double allowanceAmount, String division, String district, String upazila, String union) {
        this.name = name;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.spouseName = spouseName;
        this.nid = nid != null ? nid : "";
        this.mobileNo = mobileNo != null ? "0" + mobileNo : "";
        this.accountNo = accountNo;
        this.paymentProvider = paymentProvider;
        this.branch = branch;
        this.allowanceAmount = formatter.format(allowanceAmount);
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
    }

    public PaymentReportData(String cycleName, String division, String district, String upazila, String union, Long beneficiaryCount, double allowanceAmount) {
        this.paymentProvider = cycleName;
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
        this.beneficiaryCount = beneficiaryCount.toString();
        this.allowanceAmount = String.valueOf(allowanceAmount);
    }

    public PaymentReportData(String cycleName, String division, String district, String upazila, Long unionCount, Long beneficiaryCount, double allowanceAmount) {
        this.paymentProvider = cycleName;
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.unionCount = unionCount.toString();
        this.beneficiaryCount = beneficiaryCount.toString();
        this.allowanceAmount = String.valueOf(allowanceAmount);
    }

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

    public String getUnionCount() {
        return unionCount;
    }

    public void setUnionCount(String unionCount) {
        this.unionCount = unionCount;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getNid() {
        return nid;
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

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    /**
     *
     * @param motherName
     */
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public void setNid(String nid) {
        this.nid = nid;
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
    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAllowanceAmount() {
        return allowanceAmount;
    }

    public void setAllowanceAmount(String allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    public String getBeneficiaryCount() {
        return beneficiaryCount;
    }

    public void setBeneficiaryCount(String beneficiaryCount) {
        this.beneficiaryCount = beneficiaryCount;
    }

    public String getTotalAllowanceAmount() {
        return totalAllowanceAmount;
    }

    public void setTotalAllowanceAmount(String totalAllowanceAmount) {
        this.totalAllowanceAmount = totalAllowanceAmount;
    }

    public DecimalFormat getFormatter() {
        return formatter;
    }

    public void setFormatter(DecimalFormat formatter) {
        this.formatter = formatter;
    }

    /**
     *
     * @return
     */
    public String getBenTotal() {
        return benTotal;
    }

    public void setBenTotal(String benTotal) {
        this.benTotal = benTotal;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

}
