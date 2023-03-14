/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.data;

import java.math.BigInteger;
import java.util.Calendar;

/**
 *
 * @author PCUser
 */
public class DoubleDippingReportData
{

    private String benID;
    private String benName;
    private String nationalID;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private Calendar dob;
    private String mobileNo;
    private String division;
    private String district;
    private String upazila;
    private String union;
    private String ddMatchedMinistryName;
    private String ddMatchedSchemeName;

    /**
     *
     * @param benName
     * @param nationalID
     * @param fatherName
     * @param motherName
     * @param spouseName
     * @param dob
     * @param mobileNo
     * @param division
     * @param district
     * @param upazila
     * @param union
     * @param ddMatchedMinistryName
     * @param ddMatchedSchemeName
     */
    public DoubleDippingReportData(String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String ddMatchedMinistryName, String ddMatchedSchemeName)
    {
        this.benName = benName;
        this.nationalID = nationalID.toString();
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.spouseName = spouseName;
        this.dob = dob;
        this.mobileNo = mobileNo != null ? "0" + mobileNo : "";
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
        this.ddMatchedMinistryName = ddMatchedMinistryName;
        this.ddMatchedSchemeName = ddMatchedSchemeName;
    }

    public DoubleDippingReportData()
    {
    }

    public String getBenID()
    {
        return benID;
    }

    public void setBenID(String benID)
    {
        this.benID = benID;
    }

    public String getBenName()
    {
        return benName;
    }

    public void setBenName(String benName)
    {
        this.benName = benName;
    }

    public String getNationalID()
    {
        return nationalID;
    }

    public void setNationalID(String nationalID)
    {
        this.nationalID = nationalID;
    }

    public String getFatherName()
    {
        return fatherName;
    }

    public void setFatherName(String fatherName)
    {
        this.fatherName = fatherName;
    }

    public String getMotherName()
    {
        return motherName;
    }

    /**
     *
     * @param motherName
     */
    public void setMotherName(String motherName)
    {
        this.motherName = motherName;
    }

    /**
     *
     * @return
     */
    public String getSpouseName()
    {
        return spouseName;
    }

    public void setSpouseName(String spouseName)
    {
        this.spouseName = spouseName;
    }

    public Calendar getDob()
    {
        return dob;
    }

    public void setDob(Calendar dob)
    {
        this.dob = dob;
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

    public String getDivision()
    {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(String division)
    {
        this.division = division;
    }

    public String getDistrict()
    {
        return district;
    }

    /**
     *
     * @param district
     */
    public void setDistrict(String district)
    {
        this.district = district;
    }

    public String getUpazila()
    {
        return upazila;
    }

    /**
     *
     * @param upazila
     */
    public void setUpazila(String upazila)
    {
        this.upazila = upazila;
    }

    public String getUnion()
    {
        return union;
    }

    public void setUnion(String union)
    {
        this.union = union;
    }

    public String getDdMatchedMinistryName()
    {
        return ddMatchedMinistryName;
    }

    /**
     *
     * @param ddMatchedMinistryName
     */
    public void setDdMatchedMinistryName(String ddMatchedMinistryName)
    {
        this.ddMatchedMinistryName = ddMatchedMinistryName;
    }

    public String getDdMatchedSchemeName()
    {
        return ddMatchedSchemeName;
    }

    public void setDdMatchedSchemeName(String ddMatchedSchemeName)
    {
        this.ddMatchedSchemeName = ddMatchedSchemeName;
    }

}
