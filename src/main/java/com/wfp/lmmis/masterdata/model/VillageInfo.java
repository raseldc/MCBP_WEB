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
public class VillageInfo
{

    private Integer id;
    private String nameBn;
    private String nameEn;
    private String code;
    private Integer wardNo;
    private String unionName;
    private String upazilaName;
    private String districtName;
    private boolean active;

    public VillageInfo(Integer id, String nameBn, String nameEn, String code, Integer wardNo, String unionName, String upazilaName, String districtName, boolean active)
    {
        this.id = id;
        this.nameBn = nameBn;
        this.nameEn = nameEn;
        this.code = code;
        this.wardNo = wardNo;
        this.unionName = unionName;
        this.upazilaName = upazilaName;
        this.districtName = districtName;
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

    public String getUnionName()
    {
        return unionName;
    }

    public void setUnionName(String unionName)
    {
        this.unionName = unionName;
    }

    public String getUpazilaName()
    {
        return upazilaName;
    }

    public void setUpazilaName(String upazilaName)
    {
        this.upazilaName = upazilaName;
    }

    public String getDistrictName()
    {
        return districtName;
    }

    /**
     *
     * @param districtName
     */
    public void setDistrictName(String districtName)
    {
        this.districtName = districtName;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    /**
     *
     * @return
     */
    public Integer getWardNo()
    {
        return wardNo;
    }

    public void setWardNo(Integer wardNo)
    {
        this.wardNo = wardNo;
    }

}
