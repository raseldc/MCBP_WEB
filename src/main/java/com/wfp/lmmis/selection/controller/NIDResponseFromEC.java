/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wfp.lmmis.selection.controller;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class NIDResponseFromEC {

    String status;

    String statusCode;

    success success;

    public success getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     */
    public void setSuccess(success success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}

class success {

    data data;

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }
}

class data {

    String nationalId;

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
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

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public PermanentAddress getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(PermanentAddress permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public PresentAddress getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(PresentAddress presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    String name;
    String nameEn;

    String father;
    String mother;
    String spouse;
    String dateOfBirth;
    PermanentAddress permanentAddress;
    PresentAddress presentAddress;
    String photo;
    String photoBase64;
    String gender;

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}

class PermanentAddress {

    String dateOfBirth;
    String district;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRmo() {
        return rmo;
    }

    public void setRmo(String rmo) {
        this.rmo = rmo;
    }

    public String getUpozila() {
        return upozila;
    }

    public void setUpozila(String upozila) {
        this.upozila = upozila;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAdditionalMouzaOrMoholla() {
        return additionalMouzaOrMoholla;
    }

    public void setAdditionalMouzaOrMoholla(String additionalMouzaOrMoholla) {
        this.additionalMouzaOrMoholla = additionalMouzaOrMoholla;
    }

    public String getAdditionalVillageOrRoad() {
        return additionalVillageOrRoad;
    }

    public void setAdditionalVillageOrRoad(String additionalVillageOrRoad) {
        this.additionalVillageOrRoad = additionalVillageOrRoad;
    }

    public String getHomeOrHoldingNo() {
        return homeOrHoldingNo;
    }

    public void setHomeOrHoldingNo(String homeOrHoldingNo) {
        this.homeOrHoldingNo = homeOrHoldingNo;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    String rmo;
    String upozila;
    String postOffice;
    String postalCode;
    String additionalMouzaOrMoholla;
    String additionalVillageOrRoad;
    String homeOrHoldingNo;

    String region;
}

class PresentAddress {

    String dateOfBirth;
    String district;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRmo() {
        return rmo;
    }

    public void setRmo(String rmo) {
        this.rmo = rmo;
    }

    public String getUpozila() {
        return upozila;
    }

    public void setUpozila(String upozila) {
        this.upozila = upozila;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAdditionalMouzaOrMoholla() {
        return additionalMouzaOrMoholla;
    }

    public void setAdditionalMouzaOrMoholla(String additionalMouzaOrMoholla) {
        this.additionalMouzaOrMoholla = additionalMouzaOrMoholla;
    }

    public String getAdditionalVillageOrRoad() {
        return additionalVillageOrRoad;
    }

    public void setAdditionalVillageOrRoad(String additionalVillageOrRoad) {
        this.additionalVillageOrRoad = additionalVillageOrRoad;
    }

    public String getHomeOrHoldingNo() {
        return homeOrHoldingNo;
    }

    public void setHomeOrHoldingNo(String homeOrHoldingNo) {
        this.homeOrHoldingNo = homeOrHoldingNo;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    String rmo;
    String upozila;
    String postOffice;
    String postalCode;
    String additionalMouzaOrMoholla;
    String additionalVillageOrRoad;
    String homeOrHoldingNo;

    String region;
}
