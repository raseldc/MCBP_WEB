/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.data;

import com.wfp.lmmis.types.BeneficiaryStatus;
import java.math.BigInteger;
import java.util.Calendar;

/**
 *
 * @author Philip
 */
public class DataEntryReportData
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
    private BeneficiaryStatus beneficiaryStatus;
    private String status;
    private String paymentProviderName;
    private String bankName;
    private String mobileBankingProviderName;
    private String accountNo;
    private String userId;

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
     * @param bankName
     * @param mobileBankingProviderName
     * @param accountNo
     * @param beneficiaryStatus
     * @param userId
     */
    public DataEntryReportData(String benName, BigInteger nationalID, String fatherName, String motherName, String spouseName, Calendar dob, Integer mobileNo, String division, String district, String upazila, String union, String bankName, String mobileBankingProviderName, String accountNo, BeneficiaryStatus beneficiaryStatus, String userId)
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
        this.beneficiaryStatus = beneficiaryStatus;
        this.bankName = bankName;
        this.mobileBankingProviderName = mobileBankingProviderName;
        this.accountNo = accountNo;
        this.userId =userId;
    }

    public DataEntryReportData()
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

    /**
     *
     * @return
     */
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

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    public BeneficiaryStatus getBeneficiaryStatus()
    {
        return beneficiaryStatus;
    }

    public void setBeneficiaryStatus(BeneficiaryStatus beneficiaryStatus)
    {
        this.beneficiaryStatus = beneficiaryStatus;
    }

    /**
     *
     * @return
     */
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

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

    public String getUnion()
    {
        return union;
    }

    /**
     *
     * @param union
     */
    public void setUnion(String union)
    {
        this.union = union;
    }

    public String getPaymentProviderName()
    {
        return paymentProviderName;
    }

    public void setPaymentProviderName(String paymentProviderName)
    {
        this.paymentProviderName = paymentProviderName;
    }

    public String getBankName()
    {
        return bankName;
    }

    /**
     *
     * @param bankName
     */
    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public String getMobileBankingProviderName()
    {
        return mobileBankingProviderName;
    }

    public void setMobileBankingProviderName(String mobileBankingProviderName)
    {
        this.mobileBankingProviderName = mobileBankingProviderName;
    }
    
    public String getAccountNo()
    {
        return accountNo;
    }

    public void setAccountNo(String accountNo)
    {
        this.accountNo = accountNo;
    }

    /**
     *
     * @return
     */
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    
}
