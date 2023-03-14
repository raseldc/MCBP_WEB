/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.forms;

import com.wfp.lmmis.applicant.model.SchemeAttributeValue;
import com.wfp.lmmis.beneficiary.model.BeneficiarySchemeAttributeValue;
import com.wfp.lmmis.model.ApplicantBasicInfo;
import com.wfp.lmmis.payroll.model.FiscalYear;
import java.io.Serializable;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author PCUser
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SocioEconomicForm extends ApplicantBasicInfo implements Serializable
{    
    List<SchemeAttributeValue> schemeAttributeValueList;
    List<BeneficiarySchemeAttributeValue> beneficiarySchemeAttributeValueList;

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
    
    public List<SchemeAttributeValue> getSchemeAttributeValueList()
    {
        return schemeAttributeValueList;
    }

    public void setSchemeAttributeValueList(List<SchemeAttributeValue> schemeAttributeValueList)
    {
        this.schemeAttributeValueList = schemeAttributeValueList;
    }    

    public List<BeneficiarySchemeAttributeValue> getBeneficiarySchemeAttributeValueList()
    {
        return beneficiarySchemeAttributeValueList;
    }

    public void setBeneficiarySchemeAttributeValueList(List<BeneficiarySchemeAttributeValue> beneficiarySchemeAttributeValueList)
    {
        this.beneficiarySchemeAttributeValueList = beneficiarySchemeAttributeValueList;
    }
    
    
}
