/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.form;

import java.io.Serializable;

/**
 *
 * @author Philip
 */
public class ReportParameterFormNew implements Serializable
{
    private String schemeId;
    private String fiscalYearId;
    private String divisionId;
    private String districtId;
    private String upazilaId;
    private String unionId;
    private String trainingTypeId;

    public String getSchemeId()
    {
        return schemeId;
    }

    public void setSchemeId(String schemeId)
    {
        this.schemeId = schemeId;
    }

    public String getFiscalYearId()
    {
        return fiscalYearId;
    }

    /**
     *
     * @param fiscalYearId
     */
    public void setFiscalYearId(String fiscalYearId)
    {
        this.fiscalYearId = fiscalYearId;
    }

    /**
     *
     * @return
     */
    public String getDivisionId()
    {
        return divisionId;
    }

    public void setDivisionId(String divisionId)
    {
        this.divisionId = divisionId;
    }

    public String getDistrictId()
    {
        return districtId;
    }

    public void setDistrictId(String districtId)
    {
        this.districtId = districtId;
    }

    public String getUpazilaId()
    {
        return upazilaId;
    }

    /**
     *
     * @param upazilaId
     */
    public void setUpazilaId(String upazilaId)
    {
        this.upazilaId = upazilaId;
    }

    public String getUnionId()
    {
        return unionId;
    }

    /**
     *
     * @param unionId
     */
    public void setUnionId(String unionId)
    {
        this.unionId = unionId;
    }

    public String getTrainingTypeId()
    {
        return trainingTypeId;
    }

    /**
     *
     * @param trainingTypeId
     */
    public void setTrainingTypeId(String trainingTypeId)
    {
        this.trainingTypeId = trainingTypeId;
    }

}
