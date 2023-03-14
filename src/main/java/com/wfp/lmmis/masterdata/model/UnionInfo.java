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
public class UnionInfo
{
    private Integer id;
    private String nameBn;
    private String nameEn;
    private String code;   
    private String upazilaNameBn;
    private String upazilaNameEn;
    private boolean active;

    /**
     *
     * @param id
     * @param nameBn
     * @param nameEn
     * @param code
     * @param upazilaNameBn
     * @param upazilaNameEn
     * @param active
     */
    public UnionInfo(Integer id, String nameBn, String nameEn, String code, String upazilaNameBn, String upazilaNameEn, boolean active)
    {
        this.id = id;
        this.nameBn = nameBn;
        this.nameEn = nameEn;
        this.code = code;
        this.upazilaNameBn = upazilaNameBn;
        this.upazilaNameEn = upazilaNameEn;
        this.active = active;
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
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
        return upazilaNameBn;
    }

    public void setUpazilaNameBn(String upazilaNameBn)
    {
        this.upazilaNameBn = upazilaNameBn;
    }

    public String getUpazilaNameEn()
    {
        return upazilaNameEn;
    }

    public void setUpazilaNameEn(String upazilaNameEn)
    {
        this.upazilaNameEn = upazilaNameEn;
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
