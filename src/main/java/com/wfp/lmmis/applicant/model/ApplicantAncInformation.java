/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wfp.lmmis.applicant.model;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Shamiul Islam at Anunad Solution
 */
@Entity
@Table(name = "applicant_anc_information",
         catalog = "imlma",
         uniqueConstraints = @UniqueConstraint(columnNames = "nid")
)
public class ApplicantAncInformation implements java.io.Serializable {

    private int id;
    private Applicant applicant;
    private Beneficiary beneficiary;
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

    public ApplicantAncInformation() {
    }

    public ApplicantAncInformation(int id, String nid, String fatherName, String motherName, String husbandName, String name, String dob, String pregnancyWeek, int createdBy, Date creationDate) {
        this.id = id;
        this.nid = nid;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.husbandName = husbandName;
        this.name = name;
        this.dob = dob;
        this.pregnancyWeek = pregnancyWeek;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
    }

    public ApplicantAncInformation(int id, Applicant applicant, Beneficiary beneficiary, String nid, String fatherName, String motherName, String husbandName, String name, String dob, String pregnancyWeek, String anc1, String anc2, String anc3, String anc4, Integer modifiedBy, Date modficationDate, int createdBy, Date creationDate) {
        this.id = id;
        this.applicant = applicant;
        this.beneficiary = beneficiary;
        this.nid = nid;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.husbandName = husbandName;
        this.name = name;
        this.dob = dob;
        this.pregnancyWeek = pregnancyWeek;
        this.anc1 = anc1;
        this.anc2 = anc2;
        this.anc3 = anc3;
        this.anc4 = anc4;
        this.modifiedBy = modifiedBy;
        this.modficationDate = modficationDate;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
    }

    @Id

    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    public Applicant getApplicant() {
        return this.applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id")
    public Beneficiary getBeneficiary() {
        return this.beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    @Column(name = "nid", unique = true, nullable = false, length = 50)
    public String getNid() {
        return this.nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    @Column(name = "father_name", nullable = false, length = 50)
    public String getFatherName() {
        return this.fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Column(name = "mother_name", nullable = false, length = 50)
    public String getMotherName() {
        return this.motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    @Column(name = "husband_name", nullable = false, length = 50)
    public String getHusbandName() {
        return this.husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "dob", nullable = false, length = 50)
    public String getDob() {
        return this.dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Column(name = "pregnancy_week", nullable = false, length = 50)
    public String getPregnancyWeek() {
        return this.pregnancyWeek;
    }

    public void setPregnancyWeek(String pregnancyWeek) {
        this.pregnancyWeek = pregnancyWeek;
    }

    @Column(name = "anc1", length = 50)
    public String getAnc1() {
        return this.anc1;
    }

    public void setAnc1(String anc1) {
        this.anc1 = anc1;
    }

    @Column(name = "anc2", length = 50)
    public String getAnc2() {
        return this.anc2;
    }

    public void setAnc2(String anc2) {
        this.anc2 = anc2;
    }

    @Column(name = "anc3", length = 50)
    public String getAnc3() {
        return this.anc3;
    }

    public void setAnc3(String anc3) {
        this.anc3 = anc3;
    }

    @Column(name = "anc4", length = 50)
    public String getAnc4() {
        return this.anc4;
    }

    public void setAnc4(String anc4) {
        this.anc4 = anc4;
    }

    @Column(name = "modified_by")
    public Integer getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modfication_date", length = 19)
    public Date getModficationDate() {
        return this.modficationDate;
    }

    public void setModficationDate(Date modficationDate) {
        this.modficationDate = modficationDate;
    }

    @Column(name = "created_by", nullable = false)
    public int getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, length = 19)
    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
