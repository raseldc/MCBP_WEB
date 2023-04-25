package com.wfp.lmmis.applicant.detail;

import java.util.Date;

public class ApplicantAncInformationDetail {

    private int id;
    private int applicantId;
    private int beneficiaryId;
    private String nid;
    private String fatherName;
    private String motherName;
    private String husbandName;
    private String name;
    private String dob;
    private String pregnancyWeek;
    private String anc1;
    private String anc2;
    private String anc3;
    private String anc4;
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

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPregnancyWeek() {
        return pregnancyWeek;
    }

    public void setPregnancyWeek(String pregnancyWeek) {
        this.pregnancyWeek = pregnancyWeek;
    }

    public String getAnc1() {
        return anc1;
    }

    public void setAnc1(String anc1) {
        this.anc1 = anc1;
    }

    public String getAnc2() {
        return anc2;
    }

    public void setAnc2(String anc2) {
        this.anc2 = anc2;
    }

    public String getAnc3() {
        return anc3;
    }

    public void setAnc3(String anc3) {
        this.anc3 = anc3;
    }

    public String getAnc4() {
        return anc4;
    }

    public void setAnc4(String anc4) {
        this.anc4 = anc4;
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
