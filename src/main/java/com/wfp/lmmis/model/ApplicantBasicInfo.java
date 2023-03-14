/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.model;

import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.payroll.model.FiscalYear;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author PCUser
 */
@MappedSuperclass
public class ApplicantBasicInfo
{

    private Integer id;
    private Scheme scheme;
    private FiscalYear fiscalYear;
    private String applicationId;

    /**
     *
     * @return
     */
    public Integer getId()
    {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    public FiscalYear getFiscalYear()
    {
        return fiscalYear;
    }

    /**
     *
     * @return
     */
    public Scheme getScheme()
    {
        return scheme;
    }

    /**
     *
     * @param scheme
     */
    public void setScheme(Scheme scheme)
    {
        this.scheme = scheme;
    }

    public void setFiscalYear(FiscalYear fiscalYear)
    {
        this.fiscalYear = fiscalYear;
    }

    public String getApplicationId()
    {
        return applicationId;
    }

    /**
     *
     * @param applicationId
     */
    public void setApplicationId(String applicationId)
    {
        this.applicationId = applicationId;
    }
    
    

}
