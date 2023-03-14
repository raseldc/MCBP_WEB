/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.model;

import java.math.BigInteger;

/**
 *
 * @author Philip
 */
public class GrievanceListData
{

    private Integer grievanceId;
    private BigInteger nid;
    private String beneficiaryName;
    private String grievanceType;
    private String description;
    private String comments;
    private String grievanceStatus;

    public GrievanceListData(Integer grievanceId, String beneficiaryName, BigInteger nid, String grievanceType, String grievance, String comments, String grievanceStatus)
    {
        this.grievanceId = grievanceId;
        this.nid = nid;
        this.beneficiaryName = beneficiaryName;
        this.grievanceType = grievanceType;
        this.description = grievance;
        this.comments = comments;
        this.grievanceStatus = grievanceStatus;
    }

    public Integer getGrievanceId()
    {
        return grievanceId;
    }

    public void setGrievanceId(Integer grievanceId)
    {
        this.grievanceId = grievanceId;
    }

    public BigInteger getNid()
    {
        return nid;
    }

    public void setNid(BigInteger nid)
    {
        this.nid = nid;
    }

    public String getBeneficiaryName()
    {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName)
    {
        this.beneficiaryName = beneficiaryName;
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

    /**
     *
     * @return
     */
    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    /**
     *
     * @return
     */
    public String getGrievanceStatus()
    {
        return grievanceStatus;
    }

    public void setGrievanceStatus(String grievanceStatus)
    {
        this.grievanceStatus = grievanceStatus;
    }

}
