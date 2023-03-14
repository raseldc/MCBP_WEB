/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.model;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.rm.model.User;
import java.io.Serializable;
import java.util.Calendar;
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
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Philip
 */
@Table(name = "grievance")
@Entity
public class Grievance extends BaseModel implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "comment", nullable = true)
    private String comment;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "action_by", nullable = true, columnDefinition = "smallint unsigned")
    private User actionBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "action_date", nullable = true)
    private Calendar actionDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grievance_type_id", nullable = false, columnDefinition = "tinyint unsigned")
    private GrievanceType grievanceType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grievance_status_id", nullable = false, columnDefinition = "tinyint unsigned")
    private GrievanceStatus grievanceStatus;
    
    @Column(name="is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;
    
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getComment()
    {
        return comment;
    }

    /**
     *
     * @param comment
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Beneficiary getBeneficiary()
    {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary)
    {
        this.beneficiary = beneficiary;
    }

    public User getActionBy()
    {
        return actionBy;
    }

    public void setActionBy(User actionBy)
    {
        this.actionBy = actionBy;
    }

    public Calendar getActionDate()
    {
        return actionDate;
    }

    public void setActionDate(Calendar actionDate)
    {
        this.actionDate = actionDate;
    }

    public GrievanceType getGrievanceType()
    {
        return grievanceType;
    }

    public void setGrievanceType(GrievanceType grievanceType)
    {
        this.grievanceType = grievanceType;
    }

    public GrievanceStatus getGrievanceStatus()
    {
        return grievanceStatus;
    }

    /**
     *
     * @param grievanceStatus
     */
    public void setGrievanceStatus(GrievanceStatus grievanceStatus)
    {
        this.grievanceStatus = grievanceStatus;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grievance))
        {
            return false;
        }
        Grievance other = (Grievance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return "com.wfp.lmmis.grievance.model.Grievance[ id=" + id + " ]";
    }

}
