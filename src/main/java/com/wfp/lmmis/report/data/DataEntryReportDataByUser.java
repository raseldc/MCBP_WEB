/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.data;

/**
 *
 * @author user
 */
public class DataEntryReportDataByUser
{

    private String division;
    private String district;
    private String upazila;
    private String userID;
    private String userTotal;

    /**
     *
     * @param division
     * @param district
     * @param upazila
     * @param userID
     * @param userTotal
     */
    public DataEntryReportDataByUser(String division, String district, String upazila, String userID, Long userTotal)
    {
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.userID = userID;
        this.userTotal = userTotal.toString();
    }

    /**
     *
     * @return
     */
    public String getDivision()
    {
        return division;
    }

    public void setDivision(String division)
    {
        this.division = division;
    }

    /**
     *
     * @return
     */
    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    /**
     *
     * @return
     */
    public String getUpazila()
    {
        return upazila;
    }

    public void setUpazila(String upazila)
    {
        this.upazila = upazila;
    }

    public String getUserTotal()
    {
        return userTotal;
    }

    public void setUserTotal(String userTotal)
    {
        this.userTotal = userTotal;
    }

    /**
     *
     * @return
     */
    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

}
