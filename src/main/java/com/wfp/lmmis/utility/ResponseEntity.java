/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import java.io.Serializable;

/**
 *
 * @author shamiul
 */
public class ResponseEntity implements Serializable {

    private OperationResult operationResult;
    private Long serviceId;
    private String requestId;
    private String dob;
    private String father;
    private String gender;
    private String mother;
    private String spouse;
    private String name;
    private String nameEn;
    private String nid;
    private String permanentAddress;
    private String presentAddress;
    private byte[] photo;
    private String religion;

    public OperationResult getOperationResult() {
        return operationResult;
    }

    /**
     *
     * @param operationResult
     */
    public void setOperationResult(OperationResult operationResult) {
        this.operationResult = operationResult;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMother() {
        return mother;
    }

    /**
     *
     * @param mother
     */
    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    /**
     *
     * @param permanentAddress
     */
    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    //*

    /**
     *
     * @return
     */
    public byte[] getPhoto() {
        return photo;
    }

    /**
     *
     * @param photo
     */
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    //*/

    public String getRequestId() {
        return requestId;
    }

    /**
     *
     * @param requestId
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

}
