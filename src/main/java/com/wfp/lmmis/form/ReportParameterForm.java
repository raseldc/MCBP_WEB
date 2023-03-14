/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.form;

import com.wfp.lmmis.enums.ApplicantStatus;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.PayrollListType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.model.Village;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.model.PaymentCycle;
import com.wfp.lmmis.types.BeneficiaryStatus;
import com.wfp.lmmis.types.PaymentStatus;

/**
 *
 * @author Philip
 */
public class ReportParameterForm {

    private String reportName;
    private String reportOrientation;
    private String reportGenerationType;
    private String reportExportType;
    private String language;
    private Scheme scheme;
    private FiscalYear fiscalYear;
    private BeneficiaryStatus beneficiaryStatus;
    private PaymentStatus paymentStatus;
    private PaymentCycle paymentCycle;
    private boolean isDivisionAvailable;
    private Division division;
    private boolean isDistrictAvailable;
    private District district;
    private boolean isUpazilaAvailable;
    private Upazilla upazila;
    private boolean isUnionAvailable;
    private Union union;
    private Integer ward;
    private String startDate;
    private String endDate;
    private ApplicantType applicantType;
    private Factory bgmeaFactory;
    private Factory bkmeaFactory;
    private PayrollListType payrollListType;
    private ApplicantStatus applicantStatus;

    private boolean isBgmeaFactoryAvailable;
    private boolean isBkmeaFactoryAvailable;

    private Village village;
    private String nid;
    private String mobileNo;
    private String accountNumber;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public ReportParameterForm() {
    }

    public String getReportName() {
        return reportName;
    }

    /**
     *
     * @param reportName
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     *
     * @return
     */
    public String getReportOrientation() {
        return reportOrientation;
    }

    public void setReportOrientation(String reportOrientation) {
        this.reportOrientation = reportOrientation;
    }

    public String getReportGenerationType() {
        return reportGenerationType;
    }

    public void setReportGenerationType(String reportGenerationType) {
        this.reportGenerationType = reportGenerationType;
    }

    /**
     *
     * @return
     */
    public String getReportExportType() {
        return reportExportType;
    }

    public void setReportExportType(String reportExportType) {
        this.reportExportType = reportExportType;
    }

    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Division getDivision() {
        return division;
    }

    /**
     *
     * @return
     */
    public PaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(PaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public BeneficiaryStatus getBeneficiaryStatus() {
        return beneficiaryStatus;
    }

    /**
     *
     * @param beneficiaryStatus
     */
    public void setBeneficiaryStatus(BeneficiaryStatus beneficiaryStatus) {
        this.beneficiaryStatus = beneficiaryStatus;
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

    public Upazilla getUpazila() {
        return upazila;
    }

    public void setUpazila(Upazilla upazila) {
        this.upazila = upazila;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public boolean isIsDivisionAvailable() {
        return isDivisionAvailable;
    }

    public void setIsDivisionAvailable(boolean isDivisionAvailable) {
        this.isDivisionAvailable = isDivisionAvailable;
    }

    public boolean isIsDistrictAvailable() {
        return isDistrictAvailable;
    }

    public void setIsDistrictAvailable(boolean isDistrictAvailable) {
        this.isDistrictAvailable = isDistrictAvailable;
    }

    /**
     *
     * @return
     */
    public boolean isIsUpazilaAvailable() {
        return isUpazilaAvailable;
    }

    public void setIsUpazilaAvailable(boolean isUpazilaAvailable) {
        this.isUpazilaAvailable = isUpazilaAvailable;
    }

    /**
     *
     * @return
     */
    public boolean isIsUnionAvailable() {
        return isUnionAvailable;
    }

    public void setIsUnionAvailable(boolean isUnionAvailable) {
        this.isUnionAvailable = isUnionAvailable;
    }

    public Integer getWard() {
        return ward;
    }

    public void setWard(Integer ward) {
        this.ward = ward;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return
     */
    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public Factory getBgmeaFactory() {
        return bgmeaFactory;
    }

    /**
     *
     * @param bgmeaFactory
     */
    public void setBgmeaFactory(Factory bgmeaFactory) {
        this.bgmeaFactory = bgmeaFactory;
    }

    /**
     *
     * @return
     */
    public Factory getBkmeaFactory() {
        return bkmeaFactory;
    }

    public void setBkmeaFactory(Factory bkmeaFactory) {
        this.bkmeaFactory = bkmeaFactory;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PayrollListType getPayrollListType() {
        return payrollListType;
    }

    public void setPayrollListType(PayrollListType payrollListType) {
        this.payrollListType = payrollListType;
    }

    public ApplicantStatus getApplicantStatus() {
        return applicantStatus;
    }

    public void setApplicantStatus(ApplicantStatus applicantStatus) {
        this.applicantStatus = applicantStatus;
    }

    /**
     *
     * @return
     */
    public boolean isIsBgmeaFactoryAvailable() {
        return isBgmeaFactoryAvailable;
    }

    public void setIsBgmeaFactoryAvailable(boolean isBgmeaFactoryAvailable) {
        this.isBgmeaFactoryAvailable = isBgmeaFactoryAvailable;
    }

    public boolean isIsBkmeaFactoryAvailable() {
        return isBkmeaFactoryAvailable;
    }

    public void setIsBkmeaFactoryAvailable(boolean isBkmeaFactoryAvailable) {
        this.isBkmeaFactoryAvailable = isBkmeaFactoryAvailable;
    }

}
