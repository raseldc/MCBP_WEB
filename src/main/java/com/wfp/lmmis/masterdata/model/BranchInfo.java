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
public class BranchInfo
{

    private Integer id;
    private String nameBn;
    private String nameEn;
    private String code;
    private String address;
    private String routingNumber;
    private String bankNameBn;
    private String bankNameEn;
    private String districtNameBn;
    private String districtNameEn;
    private boolean active;

    public BranchInfo(Integer id, String nameBn, String nameEn, String code, String address, String routingNumber, String bankNameBn, String bankNameEn, String districtNameBn, String districtNameEn, boolean active)
    {
        this.id = id;
        this.nameBn = nameBn;
        this.nameEn = nameEn;
        this.code = code;
        this.address = address;
        this.routingNumber = routingNumber;
        this.bankNameBn = bankNameBn;
        this.bankNameEn = bankNameEn;
        this.districtNameBn = districtNameBn;
        this.districtNameEn = districtNameEn;
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

    /**
     *
     * @param id
     */
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

    /**
     *
     * @param code
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getRoutingNumber()
    {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber)
    {
        this.routingNumber = routingNumber;
    }

    /**
     *
     * @return
     */
    public String getBankNameBn()
    {
        return bankNameBn;
    }

    public void setBankNameBn(String bankNameBn)
    {
        this.bankNameBn = bankNameBn;
    }

    public String getBankNameEn()
    {
        return bankNameEn;
    }

    public void setBankNameEn(String bankNameEn)
    {
        this.bankNameEn = bankNameEn;
    }

    public String getDistrictNameBn()
    {
        return districtNameBn;
    }

    /**
     *
     * @param districtNameBn
     */
    public void setDistrictNameBn(String districtNameBn)
    {
        this.districtNameBn = districtNameBn;
    }

    public String getDistrictNameEn()
    {
        return districtNameEn;
    }

    public void setDistrictNameEn(String districtNameEn)
    {
        this.districtNameEn = districtNameEn;
    }

    /**
     *
     * @return
     */
    public boolean isActive()
    {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }

}
