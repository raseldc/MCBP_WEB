/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;

/**
 *
 * @author user
 */
public class BECNidResponse implements Serializable
{
    private String name;
    private String nameEn;
    private String nid;
    private String dob;
    private String gender;
    private String photo;
    private String mother;
    private String father;
    private String spouse;
    private String presentAddress;
    private String permanentAddress;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getNameEn()
    {
        return nameEn;
    }

    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }

    public String getNid()
    {
        return nid;
    }

    /**
     *
     * @param nid
     */
    public void setNid(String nid)
    {
        this.nid = nid;
    }

    public String getDob()
    {
        return dob;
    }

    public void setDob(String dob)
    {
        this.dob = dob;
    }

    public String getGender()
    {
        return gender;
    }

    /**
     *
     * @param gender
     */
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    public String getMother()
    {
        return mother;
    }

    public void setMother(String mother)
    {
        this.mother = mother;
    }

    /**
     *
     * @return
     */
    public String getFather()
    {
        return father;
    }

    public void setFather(String father)
    {
        this.father = father;
    }

    /**
     *
     * @return
     */
    public String getSpouse()
    {
        return spouse;
    }

    /**
     *
     * @param spouse
     */
    public void setSpouse(String spouse)
    {
        this.spouse = spouse;
    }

    
    public String getPresentAddress()
    {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress)
    {
        this.presentAddress = presentAddress;
    }

    public String getPermanentAddress()
    {
        return permanentAddress;
    }

    /**
     *
     * @param permanentAddress
     */
    public void setPermanentAddress(String permanentAddress)
    {
        this.permanentAddress = permanentAddress;
    }
    
    
}
