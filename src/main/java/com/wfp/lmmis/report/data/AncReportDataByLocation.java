/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.data;

/**
 *
 * @author PCUser
 */
public class AncReportDataByLocation
{

    private String division;
    private String district;
    private String upazila;
    private String union;
    private String factory;
    private String unionTotal;
    private String applicantTotal;
    private String grandTotal;
    
    private String ancCheckSuccess;
    private String ancCheckFailed;
    private String ancCheckNo;
    /**
     * This constructor is called for Applicant Summary(count) Report
     *
     * @param division
     * @param district
     * @param upazila
     * @param union
     * @param benTotal
     * @param factory
     */
    public AncReportDataByLocation(String division, String district, String upazila, String union, String factory, Long benTotal)
    {
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
        this.applicantTotal = benTotal.toString();
        this.factory = factory;
    }

    /**
     * This constructor is called for Beneficiary Group Report
     *
     * @param division
     * @param district
     * @param upazila
     * @param unionTotal
     * @param benTotal
     */
    public AncReportDataByLocation(String division, String district, String upazila, Long unionTotal, Long benTotal)
    {
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.unionTotal = unionTotal.toString();
        this.applicantTotal = benTotal.toString();
    }

    public AncReportDataByLocation()
    {
    }

    public String getDivision()
    {
        return division;
    }

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

    /**
     *
     * @return
     */
    public String getUnionTotal()
    {
        return unionTotal;
    }

    public void setUnionTotal(String unionTotal)
    {
        this.unionTotal = unionTotal;
    }

    public String getApplicantTotal()
    {
        return applicantTotal;
    }

    public void setApplicantTotal(String applicantTotal)
    {
        this.applicantTotal = applicantTotal;
    }

    public String getGrandTotal()
    {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal)
    {
        this.grandTotal = grandTotal;
    }

    public String getFactory()
    {
        return factory;
    }

    public void setFactory(String factory)
    {
        this.factory = factory;
    }

    public String getAncCheckSuccess() {
        return ancCheckSuccess;
    }

    public void setAncCheckSuccess(String ancCheckSuccess) {
        this.ancCheckSuccess = ancCheckSuccess;
    }

    public String getAncCheckFailed() {
        return ancCheckFailed;
    }

    public void setAncCheckFailed(String ancCheckFailed) {
        this.ancCheckFailed = ancCheckFailed;
    }

    public String getAncCheckNo() {
        return ancCheckNo;
    }

    public void setAncCheckNo(String ancCheckNo) {
        this.ancCheckNo = ancCheckNo;
    }
    
    

}
