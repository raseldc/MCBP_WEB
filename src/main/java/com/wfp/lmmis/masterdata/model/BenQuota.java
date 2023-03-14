/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.model;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.payroll.model.FiscalYear;
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

/**
 *
 * @author Philip
 */
@Table(name = "benquota")
@Entity
public class BenQuota extends BaseModel implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "upazila_id", nullable = true)
    private Upazilla upazila;

    @ManyToOne
    @JoinColumn(name = "union_id", nullable = true)
    private Union union;

    @ManyToOne
    @JoinColumn(name = "fiscal_year_id")
    private FiscalYear fiscalYear;

    @ManyToOne
    @JoinColumn(name = "scheme_id")
    private Scheme scheme;

    @Column(name = "quota")
    private int quota;

    @Column(name = "quota_filled")
    private int quotaFilled;

    @Column(name = "applicant_type", columnDefinition = "bit(2)", nullable = false)
    private ApplicantType applicantType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "factory_id", nullable = true)
    private Factory factory;

    public BenQuota()
    {
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

    public Union getUnion()
    {
        return union;
    }

    public void setUnion(Union union)
    {
        this.union = union;
    }

    public FiscalYear getFiscalYear()
    {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear)
    {
        this.fiscalYear = fiscalYear;
    }

    public Scheme getScheme()
    {
        return scheme;
    }

    public void setScheme(Scheme scheme)
    {
        this.scheme = scheme;
    }

    public Upazilla getUpazila()
    {
        return upazila;
    }

    public void setUpazila(Upazilla upazila)
    {
        this.upazila = upazila;
    }

    public int getQuota()
    {
        return quota;
    }

    public void setQuota(int quota)
    {
        this.quota = quota;
    }

    public int getQuotaFilled()
    {
        return quotaFilled;
    }

    public void setQuotaFilled(int quotaFilled)
    {
        this.quotaFilled = quotaFilled;
    }

    public ApplicantType getApplicantType()
    {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType)
    {
        this.applicantType = applicantType;
    }

    /**
     *
     * @return
     */
    public Factory getFactory()
    {
        return factory;
    }

    public void setFactory(Factory factory)
    {
        this.factory = factory;
    }

}
