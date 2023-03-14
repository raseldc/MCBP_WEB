/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.model;

import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.payroll.model.FiscalYear;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Philip
 */
@Table(name = "application_deadline")
@Entity
public class ApplicationDeadline extends BaseModel implements Serializable
{

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne
    @JoinColumn(name = "upazila_id")
    private Upazilla upazila;

    @ManyToOne
    @JoinColumn(name = "fiscal_year_id")
    private FiscalYear fiscalYear;

    @ManyToOne
    @JoinColumn(name = "scheme_id")
    private Scheme scheme;

    @Column(name = "deadline")
    @DateTimeFormat(pattern = "dd-MM-yy")
    @Temporal(TemporalType.DATE)
    private Calendar deadline;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Upazilla getUpazila()
    {
        return upazila;
    }

    /**
     *
     * @param upazila
     */
    public void setUpazila(Upazilla upazila)
    {
        this.upazila = upazila;
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

    /**
     *
     * @return
     */
    public Calendar getDeadline()
    {
        return deadline;
    }

    public void setDeadline(Calendar deadline)
    {
        this.deadline = deadline;
    }

    /**
     *
     * @return
     */
    public District getDistrict()
    {
        return district;
    }

    public void setDistrict(District district)
    {
        this.district = district;
    }

}
