/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.model;

/**
 *
 * @author user
 */
public class SpbmuDoubleDippingResponse
{
    private String result;
    private String ministryNameBn;
    private String ministryNameEn;
    private String schemeNameBn;
    private String schemeNameEn;

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     *
     * @return
     */
    public String getMinistryNameBn()
    {
        return ministryNameBn;
    }

    public void setMinistryNameBn(String ministryNameBn)
    {
        this.ministryNameBn = ministryNameBn;
    }

    public String getMinistryNameEn()
    {
        return ministryNameEn;
    }

    public void setMinistryNameEn(String ministryNameEn)
    {
        this.ministryNameEn = ministryNameEn;
    }

    public String getSchemeNameBn()
    {
        return schemeNameBn;
    }

    public void setSchemeNameBn(String schemeNameBn)
    {
        this.schemeNameBn = schemeNameBn;
    }

    public String getSchemeNameEn()
    {
        return schemeNameEn;
    }

    public void setSchemeNameEn(String schemeNameEn)
    {
        this.schemeNameEn = schemeNameEn;
    }
    
}
