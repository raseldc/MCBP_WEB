/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.model;

import com.wfp.lmmis.rm.model.User;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Philip
 */
@MappedSuperclass
public class BaseModel
{    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, columnDefinition = "smallint unsigned")
    private User createdBy;    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    private Calendar creationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by", columnDefinition = "smallint unsigned")
    private User modifiedBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modification_date")
    private Calendar modificationDate;

    public User getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(User createdBy)
    {
        this.createdBy = createdBy;
    }

    public Calendar getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate)
    {
        this.creationDate = creationDate;
    }
   
    public User getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }

    /**
     *
     * @return
     */
    public Calendar getModificationDate()
    {
        return modificationDate;
    }

    public void setModificationDate(Calendar modificationDate)
    {
        this.modificationDate = modificationDate;
    }
}
