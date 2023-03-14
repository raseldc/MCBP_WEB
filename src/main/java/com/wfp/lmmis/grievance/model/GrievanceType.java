/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.model;

import com.wfp.lmmis.model.BaseModel;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Philip
 */
@Table(name = "grievance_type")
@Entity
public class GrievanceType extends BaseModel implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "tinyint unsigned")
    private Integer id;

    @Size(min = 3, max = 255)
    @Column(name = "name_in_bangla", nullable = false)
    private String nameInBangla;

    @Size(min = 3, max = 255)
    @Column(name = "name_in_english", nullable = false)
    private String nameInEnglish;

    @Column(name = "active", nullable = false, columnDefinition = "bit(1)")
    private boolean active;

    @Column(name="is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;
    
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

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    /**
     *
     * @return
     */
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
        final GrievanceType other = (GrievanceType) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.wfp.lmmis.grievance.model.GrievanceType[ id=" + id + " ]";
    }

}
