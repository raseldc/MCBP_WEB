/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.beneficiary.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Philip
 */
@Entity
@Table(name = "beneficiary_view")
public class BeneficiaryView implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
//    @Column(name = "photo")
//    private byte[] photo;
    @Column(name = "full_name_in_bangla")
    private String fullNameInBangla;
    @Column(name = "full_name_in_english")
    private String fullNameInEnglish;
    @Column(name = "nid")
    private String nid;
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Column(name = "mobile_number")
    private String mobileNo;
    @Column(name = "account_number")
    private String accountNo;
    @Column(name = "scheme_id")
    private Integer schemeId;
    @Column(name = "fiscal_year_id")
    private Integer fiscalYearId;
    @Column(name = "division_id")
    private Integer divisionId;
    @Column(name = "district_id")
    private Integer districtId;
    @Column(name = "upazila_id")
    private Integer upazilaId;
    @Column(name = "union_id")
    private Integer unionId;
    @Column(name = "ward_no")
    private Integer wardNo;
    @Column(name = "village_id")
    private Integer villageId;
    @Column(name = "payment_type")
    private Integer paymentType;
    @Column(name = "status")
    private Integer beneficiaryStatus;
    @Column(name = "factory_id")
    private Integer factoryId;
    @Column(name = "applicant_type")
    private Integer applicantType;
    @Column(name = "full_name_en")
    private String fullNameEn;
    @Column(name = "full_name_bn")
    private String fullNameBn;
    @Column(name = "creation_date")
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @Column(name = "enrollment_date")
    @Temporal(TemporalType.DATE)
    private Date enrollmentDate;

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

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
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
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

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

    /**
     *
     * @return
     */
    public Integer getVillageId() {
        return villageId;
    }

    public void setVillageId(Integer villageId) {
        this.villageId = villageId;
    }

    /**
     *
     * @return
     */
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
    public Integer getFiscalYearId() {
        return fiscalYearId;
    }

    public void setFiscalYearId(Integer fiscalYearId) {
        this.fiscalYearId = fiscalYearId;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getBeneficiaryStatus() {
        return beneficiaryStatus;
    }

    /**
     *
     * @param beneficiaryStatus
     */
    public void setBeneficiaryStatus(Integer beneficiaryStatus) {
        this.beneficiaryStatus = beneficiaryStatus;
    }

    public Integer getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(Integer applicantType) {
        this.applicantType = applicantType;
    }

    /**
     *
     * @return
     */
    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    /**
     *
     * @return
     */
    public String getFullNameEn() {
        return fullNameEn;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }

    public String getFullNameBn() {
        return fullNameBn;
    }

    public void setFullNameBn(String fullNameBn) {
        this.fullNameBn = fullNameBn;
    }

    /**
     *
     * @return
     */
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

}
