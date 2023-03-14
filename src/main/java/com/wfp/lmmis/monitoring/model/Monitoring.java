/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.monitoring.model;

import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.model.BaseModel;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "monitoring")
@Entity
public class Monitoring extends BaseModel implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int unsigned")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @Column(name = "officer_name", nullable = false)
    private String officerName;
    
    @Column(name = "designation", nullable = false)
    private String designation;
    
    @Column(name = "monitoring_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar monitoringDate;
    
    @Column(name = "duration_day", nullable = false)
    private Integer durationDay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purpose_id", nullable = false)
    private Purpose purpose;
    
    @Column(name = "findings", nullable = false)
    private String findings;
    
    @ManyToOne()
    @JoinColumn(name = "division_id", nullable = true)
    private Division division;

    @ManyToOne()
    @JoinColumn(name = "district_id", nullable = true)
    private District district;

    @ManyToOne()
    @JoinColumn(name = "upazilla_id", nullable = true)
    private Upazilla upazilla;

    @ManyToOne()
    @JoinColumn(name = "union_id", nullable = true)
    private Union union;

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
    public Scheme getScheme()
    {
        return scheme;
    }

    public void setScheme(Scheme scheme)
    {
        this.scheme = scheme;
    }

    public String getOfficerName()
    {
        return officerName;
    }

    public void setOfficerName(String officerName)
    {
        this.officerName = officerName;
    }

    public String getDesignation()
    {
        return designation;
    }

    public void setDesignation(String designation)
    {
        this.designation = designation;
    }

    public Calendar getMonitoringDate()
    {
        return monitoringDate;
    }

    /**
     *
     * @param monitoringDate
     */
    public void setMonitoringDate(Calendar monitoringDate)
    {
        this.monitoringDate = monitoringDate;
    }

    public Integer getDurationDay()
    {
        return durationDay;
    }

    public void setDurationDay(Integer durationDay)
    {
        this.durationDay = durationDay;
    }

    public Purpose getPurpose()
    {
        return purpose;
    }

    /**
     *
     * @param purpose
     */
    public void setPurpose(Purpose purpose)
    {
        this.purpose = purpose;
    }

    public String getFindings()
    {
        return findings;
    }

    /**
     *
     * @param findings
     */
    public void setFindings(String findings)
    {
        this.findings = findings;
    }

    public Division getDivision()
    {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(Division division)
    {
        this.division = division;
    }

    public District getDistrict()
    {
        return district;
    }

    /**
     *
     * @param district
     */
    public void setDistrict(District district)
    {
        this.district = district;
    }

    /**
     *
     * @return
     */
    public Upazilla getUpazilla()
    {
        return upazilla;
    }

    public void setUpazilla(Upazilla upazilla)
    {
        this.upazilla = upazilla;
    }

    /**
     *
     * @return
     */
    public Union getUnion()
    {
        return union;
    }

    public void setUnion(Union union)
    {
        this.union = union;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Monitoring other = (Monitoring) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.wfp.lmmis.monitoring.model.Monitoring[ id=" + id + " ]";
    }

}
