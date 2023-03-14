/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.forms;

import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.model.PaymentCycle;
import java.io.Serializable;
import org.springframework.stereotype.Component;

/**
 *
 * @author PCUser
 */
@Component
public class PaymentInfo implements Serializable
{
    private Scheme scheme;
    private FiscalYear fiscalYear;
    private PaymentCycle paymentCycle;
    private Boolean approvalStatus;
    private String approvalComments;
    private Integer totalBeneficiary;
    private Float totalAllowanceAmount;    

    /**
     *
     * @return
     */
    public Scheme getScheme()
    {
        return scheme;
    }

    public void setScheme(Scheme scheme)
    {
        this.scheme = scheme;
    }

    /**
     *
     * @return
     */
    public FiscalYear getFiscalYear()
    {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear)
    {
        this.fiscalYear = fiscalYear;
    }

    public PaymentCycle getPaymentCycle()
    {
        return paymentCycle;
    }

    public void setPaymentCycle(PaymentCycle paymentCycle)
    {
        this.paymentCycle = paymentCycle;
    }

    /**
     *
     * @return
     */
    public String getApprovalComments()
    {
        return approvalComments;
    }

    public Boolean getApprovalStatus()
    {
        return approvalStatus;
    }

    /**
     *
     * @param approvalStatus
     */
    public void setApprovalStatus(Boolean approvalStatus)
    {
        this.approvalStatus = approvalStatus;
    }

    public void setApprovalComments(String approvalComments)
    {
        this.approvalComments = approvalComments;
    }

    public Integer getTotalBeneficiary()
    {
        return totalBeneficiary;
    }

    public void setTotalBeneficiary(Integer totalBeneficiary)
    {
        this.totalBeneficiary = totalBeneficiary;
    }

    public Float getTotalAllowanceAmount()
    {
        return totalAllowanceAmount;
    }

    public void setTotalAllowanceAmount(Float totalAllowanceAmount)
    {
        this.totalAllowanceAmount = totalAllowanceAmount;
    }

    


}
