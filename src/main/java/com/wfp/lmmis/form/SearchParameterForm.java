/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.form;

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
import com.wfp.lmmis.training.model.TrainingType;
import com.wfp.lmmis.types.CoverageArea;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Sarwar
 */
public class SearchParameterForm {

    private Scheme scheme;
    private FiscalYear fiscalYear;
    private PaymentCycle paymentCycle;
    private ApplicantType applicantType;
    private Factory bgmeaFactory;
    private Factory bkmeaFactory;
    private CoverageArea coverageArea;
    private TrainingType trainingType;

    private boolean isDivisionAvailable;
    private Division division;
    private boolean isDistrictAvailable;
    private District district;
    private boolean isUpazilaAvailable;
    private Upazilla upazila;
    private boolean isUnionAvailable;
    private Union union;
    private String ward;
    private Village village;
    private boolean isHeadOfficeUser;
    private boolean isBgmeaFactoryAvailable;
    private boolean isBkmeaFactoryAvailable;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date startDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date endDate;

    private String applicantId;
    private PayrollListType payrollListType;

    private int status;    
    
    /**
     *
     */
    public SearchParameterForm() {
        isDivisionAvailable = false;
        isDistrictAvailable = false;
        isUpazilaAvailable = false;
        isUnionAvailable = false;
        isBgmeaFactoryAvailable = false;
        isBkmeaFactoryAvailable = false;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public PaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    /**
     *
     * @param paymentCycle
     */
    public void setPaymentCycle(PaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public CoverageArea getCoverageArea() {
        return coverageArea;
    }

    public void setCoverageArea(CoverageArea coverageArea) {
        this.coverageArea = coverageArea;
    }

    public boolean isIsDivisionAvailable() {
        return isDivisionAvailable;
    }

    public void setIsDivisionAvailable(boolean isDivisionAvailable) {
        this.isDivisionAvailable = isDivisionAvailable;
    }

    public Division getDivision() {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(Division division) {
        this.division = division;
    }

    public boolean isIsDistrictAvailable() {
        return isDistrictAvailable;
    }

    public void setIsDistrictAvailable(boolean isDistrictAvailable) {
        this.isDistrictAvailable = isDistrictAvailable;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public boolean isIsUpazilaAvailable() {
        return isUpazilaAvailable;
    }

    public void setIsUpazilaAvailable(boolean isUpazilaAvailable) {
        this.isUpazilaAvailable = isUpazilaAvailable;
    }

    public Upazilla getUpazila() {
        return upazila;
    }

    /**
     *
     * @param upazila
     */
    public void setUpazila(Upazilla upazila) {
        this.upazila = upazila;
    }

    public boolean isIsUnionAvailable() {
        return isUnionAvailable;
    }

    public void setIsUnionAvailable(boolean isUnionAvailable) {
        this.isUnionAvailable = isUnionAvailable;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    /**
     *
     * @return
     */
    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public boolean isIsHeadOfficeUser() {
        return isHeadOfficeUser;
    }

    public void setIsHeadOfficeUser(boolean isHeadOfficeUser) {
        this.isHeadOfficeUser = isHeadOfficeUser;
    }

    /**
     *
     * @return
     */
    public Factory getBgmeaFactory() {
        return bgmeaFactory;
    }

    public void setBgmeaFactory(Factory bgmeaFactory) {
        this.bgmeaFactory = bgmeaFactory;
    }

    public Factory getBkmeaFactory() {
        return bkmeaFactory;
    }

    public void setBkmeaFactory(Factory bkmeaFactory) {
        this.bkmeaFactory = bkmeaFactory;
    }

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public boolean isIsBgmeaFactoryAvailable() {
        return isBgmeaFactoryAvailable;
    }

    public void setIsBgmeaFactoryAvailable(boolean isBgmeaFactoryAvailable) {
        this.isBgmeaFactoryAvailable = isBgmeaFactoryAvailable;
    }

    /**
     *
     * @return
     */
    public boolean isIsBkmeaFactoryAvailable() {
        return isBkmeaFactoryAvailable;
    }

    public void setIsBkmeaFactoryAvailable(boolean isBkmeaFactoryAvailable) {
        this.isBkmeaFactoryAvailable = isBkmeaFactoryAvailable;
    }

    public PayrollListType getPayrollListType() {
        return payrollListType;
    }

    public void setPayrollListType(PayrollListType payrollListType) {
        this.payrollListType = payrollListType;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    /**
     *
     * @param trainingType
     */
    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    /**
     *
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

}
