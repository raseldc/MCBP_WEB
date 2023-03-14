/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.model;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Philip
 */
/**
 * this class will be used for sending pay_list to SPBMU as web service
 *
 * @author Philip
 */
public class SupplementaryPayrollInfo implements Serializable
{

    private BigInteger nid;
    private String payrollAccountName;
    private String currentAccountName;
    private String payrollAccountNumber;
    private String currentAccountNumber;
    private String payrollBankOrOthers;
    private String currentBankOrOthers;
    private String payrollBranch;
    private String currentBranch;
    private String payrollRoutingNo;
    private String currentRoutingNo;
    private String division;
    private String district;
    private String upazila;
    
    public SupplementaryPayrollInfo(){}
    public SupplementaryPayrollInfo(BigInteger nid, String payrollAccountName, String currentAccountName, String payrollAccountNumber, String currentAccountNumber, String payrollBankOrOthers, String currentBankOrOthers, String payrollBranch, String currentBranch, String payrollRoutingNo, String currentRoutingNo, String division, String district, String upazila)
    {
        this.nid = nid;
        this.payrollAccountName = payrollAccountName;
        this.currentAccountName = currentAccountName;
        this.payrollAccountNumber = payrollAccountNumber;
        this.currentAccountNumber = currentAccountNumber;
        this.payrollBankOrOthers = payrollBankOrOthers;
        this.currentBankOrOthers = currentBankOrOthers;
        this.payrollBranch = payrollBranch;
        this.currentBranch = currentBranch;
        this.payrollRoutingNo = payrollRoutingNo;
        this.currentRoutingNo = currentRoutingNo;
        this.division = division;
        this.district = district;
        this.upazila = upazila;
    }

    public BigInteger getNid()
    {
        return nid;
    }

    /**
     *
     * @param nid
     */
    public void setNid(BigInteger nid)
    {
        this.nid = nid;
    }

    public String getPayrollAccountName()
    {
        return payrollAccountName;
    }

    /**
     *
     * @param payrollAccountName
     */
    public void setPayrollAccountName(String payrollAccountName)
    {
        this.payrollAccountName = payrollAccountName;
    }

    public String getCurrentAccountName()
    {
        return currentAccountName;
    }

    public void setCurrentAccountName(String currentAccountName)
    {
        this.currentAccountName = currentAccountName;
    }

    public String getPayrollAccountNumber()
    {
        return payrollAccountNumber;
    }

    public void setPayrollAccountNumber(String payrollAccountNumber)
    {
        this.payrollAccountNumber = payrollAccountNumber;
    }

    /**
     *
     * @return
     */
    public String getCurrentAccountNumber()
    {
        return currentAccountNumber;
    }

    public void setCurrentAccountNumber(String currentAccountNumber)
    {
        this.currentAccountNumber = currentAccountNumber;
    }

    public String getPayrollBankOrOthers()
    {
        return payrollBankOrOthers;
    }

    /**
     *
     * @param payrollBankOrOthers
     */
    public void setPayrollBankOrOthers(String payrollBankOrOthers)
    {
        this.payrollBankOrOthers = payrollBankOrOthers;
    }

    public String getCurrentBankOrOthers()
    {
        return currentBankOrOthers;
    }

    public void setCurrentBankOrOthers(String currentBankOrOthers)
    {
        this.currentBankOrOthers = currentBankOrOthers;
    }

    /**
     *
     * @return
     */
    public String getPayrollBranch()
    {
        return payrollBranch;
    }

    public void setPayrollBranch(String payrollBranch)
    {
        this.payrollBranch = payrollBranch;
    }

    public String getCurrentBranch()
    {
        return currentBranch;
    }

    public void setCurrentBranch(String currentBranch)
    {
        this.currentBranch = currentBranch;
    }

    /**
     *
     * @return
     */
    public String getPayrollRoutingNo()
    {
        return payrollRoutingNo;
    }

    public void setPayrollRoutingNo(String payrollRoutingNo)
    {
        this.payrollRoutingNo = payrollRoutingNo;
    }

    /**
     *
     * @return
     */
    public String getCurrentRoutingNo()
    {
        return currentRoutingNo;
    }

    public void setCurrentRoutingNo(String currentRoutingNo)
    {
        this.currentRoutingNo = currentRoutingNo;
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

}
