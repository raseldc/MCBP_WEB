/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dashboard.controller;

/**
 *
 * @author Philip
 */
public class ApplicantCountByStatus
{

    private String primarilyApproved;
    private String spbmuRejected;
    private String fieldVerifiedCount;
    private String rejectedAtField;
    private String rejectedAtNIDCheck;

    public ApplicantCountByStatus(String primarilyApproved, String fieldVerifiedCount, String rejectedAtField)
    {
        this.primarilyApproved = primarilyApproved;
//        this.spbmuRejected = spbmuRejected;
        this.fieldVerifiedCount = fieldVerifiedCount;
        this.rejectedAtField = rejectedAtField;
//        this.rejectedAtNIDCheck = rejectedAtNIDCheck;
    }

    public String getPrimarilyApproved()
    {
        return primarilyApproved;
    }

    public void setPrimarilyApproved(String primarilyApproved)
    {
        this.primarilyApproved = primarilyApproved;
    }

    public String getSpbmuRejected()
    {
        return spbmuRejected;
    }

    public void setSpbmuRejected(String spbmuRejected)
    {
        this.spbmuRejected = spbmuRejected;
    }

    public String getRejectedAtField()
    {
        return rejectedAtField;
    }

    public void setRejectedAtField(String rejectedAtField)
    {
        this.rejectedAtField = rejectedAtField;
    }

    /**
     *
     * @return
     */
    public String getRejectedAtNIDCheck()
    {
        return rejectedAtNIDCheck;
    }

    public void setRejectedAtNIDCheck(String rejectedAtNIDCheck)
    {
        this.rejectedAtNIDCheck = rejectedAtNIDCheck;
    }

    public String getFieldVerifiedCount()
    {
        return fieldVerifiedCount;
    }

    /**
     *
     * @param fieldVerifiedCount
     */
    public void setFieldVerifiedCount(String fieldVerifiedCount)
    {
        this.fieldVerifiedCount = fieldVerifiedCount;
    }

}
