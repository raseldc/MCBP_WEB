/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.model;

/**
 *
 * @author USER
 */
public class UserSchemeDetail
{

    private String schemeNameBn;
    private String schemeNameEn;
    private Integer userPerSchemeId;

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

    public Integer getUserPerSchemeId()
    {
        return userPerSchemeId;
    }

    public void setUserPerSchemeId(Integer userPerSchemeId)
    {
        this.userPerSchemeId = userPerSchemeId;
    }

}
