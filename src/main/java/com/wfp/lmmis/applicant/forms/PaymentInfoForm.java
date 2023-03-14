/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.forms;

import com.wfp.lmmis.enums.PaymentTypeEnum;
import com.wfp.lmmis.masterdata.model.AccountType;
import com.wfp.lmmis.masterdata.model.Bank;
import com.wfp.lmmis.masterdata.model.Branch;
import com.wfp.lmmis.masterdata.model.MobileBankingProvider;
import com.wfp.lmmis.masterdata.model.PostOfficeBranch;
import com.wfp.lmmis.model.ApplicantBasicInfo;
import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author PCUser
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PaymentInfoForm extends ApplicantBasicInfo implements Serializable
{
//    private Integer id;
//    private FiscalYear fiscalYear;

    private PaymentTypeEnum paymentType;
    private Bank bank;
    private Branch branch;
    private AccountType accountType;
    private AccountType accountTypePO;
    private MobileBankingProvider mobileBankingProvider;
    private PostOfficeBranch postOfficeBranch;
    private String accountName;
    private String accountNo;
    private Integer permanentDistrictId;

//    public Integer getId()
//    {
//        return id;
//    }
//
//    public void setId(Integer id)
//    {
//        this.id = id;
//    }
//
//    public FiscalYear getFiscalYear()
//    {
//        return fiscalYear;
//    }
//
//    public void setFiscalYear(FiscalYear fiscalYear)
//    {
//        this.fiscalYear = fiscalYear;
//    }

    public PaymentTypeEnum getPaymentType()
    {
        return paymentType;
    }

    /**
     *
     * @param paymentType
     */
    public void setPaymentType(PaymentTypeEnum paymentType)
    {
        this.paymentType = paymentType;
    }
   
    /**
     *
     * @return
     */
    public Bank getBank()
    {
        return bank;
    }

    public void setBank(Bank bank)
    {
        this.bank = bank;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    public AccountType getAccountType()
    {
        return accountType;
    }

    /**
     *
     * @param accountType
     */
    public void setAccountType(AccountType accountType)
    {
        this.accountType = accountType;
    }

    public MobileBankingProvider getMobileBankingProvider()
    {
        return mobileBankingProvider;
    }

    /**
     *
     * @param mobileBankingProvider
     */
    public void setMobileBankingProvider(MobileBankingProvider mobileBankingProvider)
    {
        this.mobileBankingProvider = mobileBankingProvider;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public String getAccountNo()
    {
        return accountNo;
    }

    public void setAccountNo(String accountNo)
    {
        this.accountNo = accountNo;
    }

    public Integer getPermanentDistrictId()
    {
        return permanentDistrictId;
    }

    public void setPermanentDistrictId(Integer permanentDistrictId)
    {
        this.permanentDistrictId = permanentDistrictId;
    }

    /**
     *
     * @return
     */
    public AccountType getAccountTypePO()
    {
        return accountTypePO;
    }

    public void setAccountTypePO(AccountType accountTypePO)
    {
        this.accountTypePO = accountTypePO;
    }

    public PostOfficeBranch getPostOfficeBranch()
    {
        return postOfficeBranch;
    }

    public void setPostOfficeBranch(PostOfficeBranch postOfficeBranch)
    {
        this.postOfficeBranch = postOfficeBranch;
    }

}
