/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.forms;

import static com.wfp.lmmis.enums.LoggingTableType.PaymentType;
import com.wfp.lmmis.enums.PaymentTypeEnum;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.payroll.model.PayrollSummary;
import com.wfp.lmmis.rm.model.User;
import java.io.Serializable;
import org.springframework.stereotype.Component;

/**
 *
 * @author PCUser
 */
@Component
public class PaymentGenerationForm implements Serializable
{

    private SearchParameterForm searchParameterForm;
    private PayrollSummary payrollSummary;
    private PaymentTypeEnum paymentType;
    private String comments;
    private User createdBy;

    public SearchParameterForm getSearchParameterForm()
    {
        return searchParameterForm;
    }

    /**
     *
     * @param searchParameterForm
     */
    public void setSearchParameterForm(SearchParameterForm searchParameterForm)
    {
        this.searchParameterForm = searchParameterForm;
    }

    /**
     *
     * @return
     */
    public PayrollSummary getPayrollSummary()
    {
        return payrollSummary;
    }

    public void setPayrollSummary(PayrollSummary payrollSummary)
    {
        this.payrollSummary = payrollSummary;
    }

    /**
     *
     * @return
     */
    public PaymentTypeEnum getPaymentType()
    {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType)
    {
        this.paymentType = paymentType;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public User getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(User createdBy)
    {
        this.createdBy = createdBy;
    }

}
