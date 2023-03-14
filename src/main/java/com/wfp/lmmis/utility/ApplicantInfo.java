/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.types.BloodGroup;
import com.wfp.lmmis.types.Gender;
import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author user
 */
public class ApplicantInfo implements Serializable
{
    private Integer Id;
    private String nameInBangla;
    private String nameInEnglish;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private String nid;
    private Calendar dateOfBirth;
    private Gender gender;
    private String mobileNo;
    private String applicationID;
    private String address;

    public ApplicantInfo()
    {
    }

    public Integer getId()
    {
        return Id;
    }

    public void setId(Integer Id)
    {
        this.Id = Id;
    }

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

    /**
     *
     * @return
     */
    public String getFatherName()
    {
        return fatherName;
    }

    public void setFatherName(String fatherName)
    {
        this.fatherName = fatherName;
    }

    public String getMotherName()
    {
        return motherName;
    }

    public void setMotherName(String motherName)
    {
        this.motherName = motherName;
    }

    public String getSpouseName()
    {
        return spouseName;
    }

    public void setSpouseName(String spouseName)
    {
        this.spouseName = spouseName;
    }

    public String getNid()
    {
        return nid;
    }

    public void setNid(String nid)
    {
        this.nid = nid;
    }

    public Calendar getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    public String getApplicationID()
    {
        return applicationID;
    }

    public void setApplicationID(String applicationID)
    {
        this.applicationID = applicationID;
    }

    /**
     *
     * @return
     */
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    
}
