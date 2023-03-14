/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeneficiaryChildInformationDetail {

    private Integer id;
    private int benid;
    private String childName;
    private String childBirthCertificate;
    
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    private Date childDob;
    private Integer childNo;
    private Integer isAlive;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date deathDate;
    private String deathReason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date modificationDate;
    private String attachedFileLocation;

    private String base64;

    public BeneficiaryChildInformationDetail(BeneficiaryChildInformation beneficiaryChildInformation) {
        this.id = beneficiaryChildInformation.getId();
        this.childName = beneficiaryChildInformation.getChildName();
        this.childDob = beneficiaryChildInformation.getChildDob();
        this.childBirthCertificate = beneficiaryChildInformation.getChildBirthCertificate();
        this.childNo = beneficiaryChildInformation.getChildNo();
        this.attachedFileLocation = beneficiaryChildInformation.getAttachedFileLocation();
        this.base64 = beneficiaryChildInformation.getBase64();

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

    /**
     *
     * @return
     */
    public int getBenid() {
        return benid;
    }

    public void setBenid(int benid) {
        this.benid = benid;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildBirthCertificate() {
        return childBirthCertificate;
    }

    public void setChildBirthCertificate(String childBirthCertificate) {
        this.childBirthCertificate = childBirthCertificate;
    }

    public Date getChildDob() {
        return childDob;
    }

    /**
     *
     * @param childDob
     */
    public void setChildDob(Date childDob) {
        this.childDob = childDob;
    }

    public Integer getChildNo() {
        return childNo;
    }

    /**
     *
     * @param childNo
     */
    public void setChildNo(Integer childNo) {
        this.childNo = childNo;
    }

    public Integer getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Integer isAlive) {
        this.isAlive = isAlive;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getAttachedFileLocation() {
        return attachedFileLocation;
    }

    public void setAttachedFileLocation(String attachedFileLocation) {
        this.attachedFileLocation = attachedFileLocation;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

}
