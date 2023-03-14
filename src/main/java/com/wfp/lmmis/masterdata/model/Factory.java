/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.model;

import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.types.FactoryType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "factory")
public class Factory extends BaseModel implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint(4) unsigned")
    private Integer id;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_bangla", length = 255, nullable = false)
    private String nameInBangla;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_english", length = 255, nullable = false)
    private String nameInEnglish;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "type", columnDefinition = "bit(1)", nullable = false)
    private FactoryType type;

    @Size(min = 4, max = 4)
    @Column(name = "reg_no", columnDefinition = "smallint(2) unsigned zerofill", nullable = false)
    private String regNo;

    @Column(name = "active", columnDefinition = "bit(1)", nullable = false)
    private boolean active;

    @Column(name = "is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", nullable = true)
    private Division division;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upazila_id", nullable = true)
    private Upazilla upazila;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = true)
    private District district;
  
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

    /**
     *
     * @param nameInEnglish
     */
    public void setNameInEnglish(String nameInEnglish)
    {
        this.nameInEnglish = nameInEnglish;
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

    /**
     *
     * @return
     */
    public FactoryType getType()
    {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(FactoryType type)
    {
        this.type = type;
    }

    public String getRegNo()
    {
        return regNo;
    }

    public void setRegNo(String regNo)
    {
        this.regNo = regNo;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    public Upazilla getUpazila()
    {
        return upazila;
    }

    public void setUpazila(Upazilla upazila)
    {
        this.upazila = upazila;
    }

    public District getDistrict()
    {
        return district;
    }

    public void setDistrict(District district)
    {
        this.district = district;
    }

    public Division getDivision()
    {
        return division;
    }

    public void setDivision(Division division)
    {
        this.division = division;
    }

}
