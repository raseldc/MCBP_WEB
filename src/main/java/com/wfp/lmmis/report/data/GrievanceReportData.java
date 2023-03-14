/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Philip
 */
public class GrievanceReportData implements Serializable
{

    String benName;
    String benID;
    String grievanceType;
    String description;
    String comment;
    String status;
    private String division;
    private String district;
    private String upazila;
    private String union;

    public GrievanceReportData(String benName, BigInteger benID, String grievanceType, String description, String comment, String status, String division, String district, String upazila, String union)
    {
        this.benName = benName;
        this.benID = benID.toString();
        this.grievanceType = grievanceType;
        this.description = description;
        this.comment = comment;
        this.status = status;
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
    }
    
    public String getBenName()
    {
        return benName;
    }

    public void setBenName(String benName)
    {
        this.benName = benName;
    }

    public String getBenID()
    {
        return benID;
    }

    public void setBenID(String benID)
    {
        this.benID = benID;
    }

    /**
     *
     * @return
     */
    public String getGrievanceType()
    {
        return grievanceType;
    }

    public void setGrievanceType(String grievanceType)
    {
        this.grievanceType = grievanceType;
    }

    public String getDescription()
    {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    /**
     *
     * @return
     */
    public String getStatus()
    {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
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

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public String getUpazila()
    {
        return upazila;
    }

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

}
