/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "double_dipping_matched_status")
public class DoubleDippingMatchedStatus implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dd_matched_ministry_bn", nullable = false)
    private String ddMatchedMinistryNameBn;

    @Column(name = "dd_matched_ministry_en", nullable = false)
    private String ddMatchedMInistryNameEn;

    @Column(name = "dd_matched_scheme_bn", nullable = false)
    private String ddMatchedSchemeBn;

    @Column(name = "dd_matched_scheme_en", nullable = false)
    private String ddMatchedSchemeEn;

    @OneToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

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

    public String getDdMatchedMinistryNameBn()
    {
        return ddMatchedMinistryNameBn;
    }

    public void setDdMatchedMinistryNameBn(String ddMatchedMinistryNameBn)
    {
        this.ddMatchedMinistryNameBn = ddMatchedMinistryNameBn;
    }

    public String getDdMatchedMInistryNameEn()
    {
        return ddMatchedMInistryNameEn;
    }

    /**
     *
     * @param ddMatchedMInistryNameEn
     */
    public void setDdMatchedMInistryNameEn(String ddMatchedMInistryNameEn)
    {
        this.ddMatchedMInistryNameEn = ddMatchedMInistryNameEn;
    }

    public String getDdMatchedSchemeBn()
    {
        return ddMatchedSchemeBn;
    }

    public void setDdMatchedSchemeBn(String ddMatchedSchemeBn)
    {
        this.ddMatchedSchemeBn = ddMatchedSchemeBn;
    }

    public String getDdMatchedSchemeEn()
    {
        return ddMatchedSchemeEn;
    }

    public void setDdMatchedSchemeEn(String ddMatchedSchemeEn)
    {
        this.ddMatchedSchemeEn = ddMatchedSchemeEn;
    }

    /**
     *
     * @return
     */
    public Applicant getApplicant()
    {
        return applicant;
    }

    public void setApplicant(Applicant applicant)
    {
        this.applicant = applicant;
    }
    
    

}
