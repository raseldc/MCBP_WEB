/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.model;

/**
 *
 * @author USER
 */
public class DistrictInfo
{

    private Integer id;
    private String nameBn;
    private String nameEn;
    private String code;
    private String divisionNameBn;
    private String divisionNameEn;
    private boolean active;

    public DistrictInfo(Integer id, String nameBn, String nameEn, String code, String divisionNameBn, String divisionNameEn, boolean active)
    {
        this.id = id;
        this.nameBn = nameBn;
        this.nameEn = nameEn;
        this.code = code;
        this.divisionNameBn = divisionNameBn;
        this.divisionNameEn = divisionNameEn;
        this.active = active;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNameBn()
    {
        return nameBn;
    }

    public void setNameBn(String nameBn)
    {
        this.nameBn = nameBn;
    }

    public String getNameEn()
    {
        return nameEn;
    }

    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDivisionNameBn()
    {
        return divisionNameBn;
    }

    public void setDivisionNameBn(String divisionNameBn)
    {
        this.divisionNameBn = divisionNameBn;
    }

    public String getDivisionNameEn()
    {
        return divisionNameEn;
    }

    /**
     *
     * @param divisionNameEn
     */
    public void setDivisionNameEn(String divisionNameEn)
    {
        this.divisionNameEn = divisionNameEn;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

}
