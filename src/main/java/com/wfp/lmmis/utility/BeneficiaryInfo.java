/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import java.math.BigInteger;

/**
 *
 * @author user
 */
public class BeneficiaryInfo
{

    private Integer Id;
    private String nameInEnglish;
    private String nameInBangla;
    private BigInteger nid;
    private Integer mobileNo;

    /**
     *
     * @param Id
     * @param nameInEnglish
     * @param nameInBangla
     * @param nid
     * @param mobileNo
     */
    public BeneficiaryInfo(Integer Id, String nameInEnglish, String nameInBangla, BigInteger nid, Integer mobileNo)
    {
        this.Id = Id;
        this.nameInEnglish = nameInEnglish;
        this.nameInBangla = nameInBangla;
        this.nid = nid;
        this.mobileNo = mobileNo;
    }

    public Integer getId()
    {
        return Id;
    }

    public void setId(Integer Id)
    {
        this.Id = Id;
    }

    /**
     *
     * @return
     */
    public String getNameInBangla()
    {
        return nameInBangla;
    }

    public void setNameInBangla(String nameInBangla)
    {
        this.nameInBangla = nameInBangla;
    }

    public String getNameInEnglish()
    {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish)
    {
        this.nameInEnglish = nameInEnglish;
    }

    public BigInteger getNid()
    {
        return nid;
    }

    public void setNid(BigInteger nid)
    {
        this.nid = nid;
    }

    public Integer getMobileNo()
    {
        return mobileNo;
    }

    /**
     *
     * @param mobileNo
     */
    public void setMobileNo(Integer mobileNo)
    {
        this.mobileNo = mobileNo;
    }

}
