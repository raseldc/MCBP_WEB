/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.data;

import com.wfp.lmmis.enums.PaymentTypeEnum;

/**
 *
 * @author Philip
 */
public class BeneficiaryReportDataByLocation
{

    private String division;
    private String district;
    private String upazila;
    private String union;
    private String wardNo;
    private String factory;
    private PaymentTypeEnum paymentTypeEnum;
    private String paymentProvider;
    private String unionTotal;
    private String upazilaTotal;
    private String benTotal;
    private String grandTotal;
    /**
     * This constructor is called for Beneficiary Summary(count) Report
     *
     * @param division
     * @param district
     * @param upazila
     * @param union
     * @param factory
     * @param benTotal
     */
    public BeneficiaryReportDataByLocation(String division, String district, String upazila, String union, String factory, Long benTotal, String wardNo)
    {
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
        this.wardNo = wardNo;
        this.factory = factory;
        this.benTotal = benTotal.toString();
    }

    /**
     *
     * @param division
     * @param district
     * @param upazila
     * @param union
     * @param factory
     * @param paymentTypeEnum
     * @param benTotal
     * @param wardNo
     */
    public BeneficiaryReportDataByLocation(String division, String district, String upazila, String union, String factory, PaymentTypeEnum paymentTypeEnum, Long benTotal, String wardNo)
    {
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.union = union;
        this.wardNo = wardNo;
        this.factory = factory;
        this.paymentTypeEnum = paymentTypeEnum;
        this.benTotal = benTotal.toString();;
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
    public BeneficiaryReportDataByLocation(String division, String district, String upazila, Long unionTotal, Long benTotal)
    {
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.unionTotal = unionTotal.toString();
        this.benTotal = benTotal.toString();
    }

    /**
     *
     * @param division
     * @param district
     * @param upazila
     * @param upazilaTotal
     */
    public BeneficiaryReportDataByLocation(String division, String district, String upazila, String upazilaTotal)
    {
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.upazilaTotal = upazilaTotal;
    }
    
    /**
     *
     */
    public BeneficiaryReportDataByLocation()
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
    public String getWardNo()
    {
        return wardNo;
    }

    /**
     *
     * @param wardNo
     */
    public void setWardNo(String wardNo)
    {
        this.wardNo = wardNo;
    }

    public String getUnionTotal()
    {
        return unionTotal;
    }

    /**
     *
     * @param unionTotal
     */
    public void setUnionTotal(String unionTotal)
    {
        this.unionTotal = unionTotal;
    }
    
    /**
     *
     * @return
     */
    public String getUpazilaTotal()
    {
        return upazilaTotal;
    }

    public void setUpazilaTotal(String upazilaTotal)
    {
        this.upazilaTotal = upazilaTotal;
    }
    
    public String getBenTotal()
    {
        return benTotal;
    }

    public void setBenTotal(String benTotal)
    {
        this.benTotal = benTotal;
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

    /**
     *
     * @param factory
     */
    public void setFactory(String factory)
    {
        this.factory = factory;
    }

    public PaymentTypeEnum getPaymentTypeEnum()
    {
        return paymentTypeEnum;
    }

    public void setPaymentTypeEnum(PaymentTypeEnum paymentTypeEnum)
    {
        this.paymentTypeEnum = paymentTypeEnum;
    }

    /**
     *
     * @return
     */
    public String getPaymentProvider()
    {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider)
    {
        this.paymentProvider = paymentProvider;
    }
    

}
