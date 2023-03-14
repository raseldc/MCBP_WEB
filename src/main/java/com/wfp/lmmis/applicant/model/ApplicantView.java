/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.model;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.types.SystemRecommendedStatus;
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
@Table(name = "applicant_view")
public class ApplicantView implements Serializable
{

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
    @Column(name = "status")
    private ApplicationStatus applicationStatus;
    @Column(name = "system_status")
    private SystemRecommendedStatus systemRecommendedStatus;
    @Column(name = "recommendation_status")
    private Boolean recommendationStatus;
    @Column(name = "present_addressline1", nullable = false)
    private String presentAddressLine1;
    @Column(name = "present_addressline2")
    private String presentAddressLine2;
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
    @Column(name = "factory_id")
    private Integer factoryId;
    @Column(name = "applicant_type")
    private ApplicantType applicantType;
    @Column(name = "score")
    private Integer score;
    @Column(name = "full_name_en")
    private String fullNameEn;
    @Column(name = "full_name_bn")
    private String fullNameBn;
    @Column(name = "creation_date")
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getFullNameInBangla()
    {
        return fullNameInBangla;
    }

    public void setFullNameInBangla(String fullNameInBangla)
    {
        this.fullNameInBangla = fullNameInBangla;
    }

    public String getFullNameInEnglish()
    {
        return fullNameInEnglish;
    }

    public void setFullNameInEnglish(String fullNameInEnglish)
    {
        this.fullNameInEnglish = fullNameInEnglish;
    }

    public String getMobileNo()
    {
        return mobileNo;
    }

    /**
     *
     * @param mobileNo
     */
    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    /**
     *
     * @return
     */
    public ApplicationStatus getApplicationStatus()
    {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus)
    {
        this.applicationStatus = applicationStatus;
    }

    public SystemRecommendedStatus getSystemRecommendedStatus()
    {
        return systemRecommendedStatus;
    }

    public void setSystemRecommendedStatus(SystemRecommendedStatus systemRecommendedStatus)
    {
        this.systemRecommendedStatus = systemRecommendedStatus;
    }

    public String getNid()
    {
        return nid;
    }

    public void setNid(String nid)
    {
        this.nid = nid;
    }

    /**
     *
     * @return
     */
    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPresentAddressLine1()
    {
        return presentAddressLine1;
    }

    /**
     *
     * @param presentAddressLine1
     */
    public void setPresentAddressLine1(String presentAddressLine1)
    {
        this.presentAddressLine1 = presentAddressLine1;
    }

    public String getPresentAddressLine2()
    {
        return presentAddressLine2;
    }

    public void setPresentAddressLine2(String presentAddressLine2)
    {
        this.presentAddressLine2 = presentAddressLine2;
    }

    public Integer getDivisionId()
    {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId)
    {
        this.divisionId = divisionId;
    }

    public Integer getDistrictId()
    {
        return districtId;
    }

    public void setDistrictId(Integer districtId)
    {
        this.districtId = districtId;
    }

    public Integer getUpazilaId()
    {
        return upazilaId;
    }

    public void setUpazilaId(Integer upazilaId)
    {
        this.upazilaId = upazilaId;
    }

    public Integer getUnionId()
    {
        return unionId;
    }

    public void setUnionId(Integer unionId)
    {
        this.unionId = unionId;
    }

    public Integer getSchemeId()
    {
        return schemeId;
    }

    public void setSchemeId(Integer schemeId)
    {
        this.schemeId = schemeId;
    }

    public Integer getFiscalYearId()
    {
        return fiscalYearId;
    }

    /**
     *
     * @param fiscalYearId
     */
    public void setFiscalYearId(Integer fiscalYearId)
    {
        this.fiscalYearId = fiscalYearId;
    }

    /**
     *
     * @return
     */
    public Integer getFactoryId()
    {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId)
    {
        this.factoryId = factoryId;
    }

    public ApplicantType getApplicantType()
    {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType)
    {
        this.applicantType = applicantType;
    }

    public Boolean getRecommendationStatus()
    {
        return recommendationStatus;
    }

    public void setRecommendationStatus(Boolean recommendationStatus)
    {
        this.recommendationStatus = recommendationStatus;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public String getFullNameEn()
    {
        return fullNameEn;
    }

    public void setFullNameEn(String fullNameEn)
    {
        this.fullNameEn = fullNameEn;
    }

    public String getFullNameBn()
    {
        return fullNameBn;
    }

    public void setFullNameBn(String fullNameBn)
    {
        this.fullNameBn = fullNameBn;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

}
