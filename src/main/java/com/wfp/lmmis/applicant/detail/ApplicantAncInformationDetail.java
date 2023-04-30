package com.wfp.lmmis.applicant.detail;

import java.util.Date;

public class ApplicantAncInformationDetail {

    private int id;
    private int applicantId;
    private int beneficiaryId;
    private String nid;
    private String FatherName;
    private String MotherName;
    private String HusbandName;
    private String Name;
    private String DOB;
    private String Pregnancy_week;
    private String ANC1;
    private String ANC2;
    private String ANC3;
    private String ANC4;
    private Integer modifiedBy;
    private Date modficationDate;
    private int createdBy;
    private Date creationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public int getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(int beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String FatherName) {
        this.FatherName = FatherName;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String MotherName) {
        this.MotherName = MotherName;
    }

    public String getHusbandName() {
        return HusbandName;
    }

    public void setHusbandName(String HusbandName) {
        this.HusbandName = HusbandName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPregnancy_week() {
        return Pregnancy_week;
    }

    public void setPregnancy_week(String Pregnancy_week) {
        this.Pregnancy_week = Pregnancy_week;
    }

    public String getANC1() {
        return ANC1;
    }

    public void setANC1(String ANC1) {
        this.ANC1 = ANC1;
    }

    public String getANC2() {
        return ANC2;
    }

    public void setANC2(String ANC2) {
        this.ANC2 = ANC2;
    }

    public String getANC3() {
        return ANC3;
    }

    public void setANC3(String ANC3) {
        this.ANC3 = ANC3;
    }

    public String getANC4() {
        return ANC4;
    }

    public void setANC4(String ANC4) {
        this.ANC4 = ANC4;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModficationDate() {
        return modficationDate;
    }

    public void setModficationDate(Date modficationDate) {
        this.modficationDate = modficationDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
