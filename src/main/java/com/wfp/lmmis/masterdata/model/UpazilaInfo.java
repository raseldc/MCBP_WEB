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
public class UpazilaInfo
{
    private Integer id;
    private String nameBn;
    private String nameEn;
    private String code;   
    private String districtNameBn;
    private String districtNameEn;
    private boolean active;

    public UpazilaInfo(Integer id, String nameBn, String nameEn, String code, String upazilaNameBn, String upazilaNameEn, boolean active)
    {
        this.id = id;
        this.nameBn = nameBn;
        this.nameEn = nameEn;
        this.code = code;
        this.districtNameBn = upazilaNameBn;
        this.districtNameEn = upazilaNameEn;
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

    /**
     *
     * @return
     */
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

    /**
     *
     * @param nameEn
     */
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

    public String getUpazilaNameBn()
    {
        return districtNameBn;
    }

    public void setUpazilaNameBn(String upazilaNameBn)
    {
        this.districtNameBn = upazilaNameBn;
    }

    public String getDistrictNameBn()
    {
        return districtNameBn;
    }

    public void setDistrictNameBn(String districtNameBn)
    {
        this.districtNameBn = districtNameBn;
    }

    public String getDistrictNameEn()
    {
        return districtNameEn;
    }

    /**
     *
     * @param districtNameEn
     */
    public void setDistrictNameEn(String districtNameEn)
    {
        this.districtNameEn = districtNameEn;
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
