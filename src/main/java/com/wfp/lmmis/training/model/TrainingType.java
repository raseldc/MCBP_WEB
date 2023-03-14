/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.model;

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
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Philip
 */
@Table(name = "training_type")
@Entity
public class TrainingType extends BaseModel implements Serializable
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

    @Size(min = 0, max = 1023)
    @Column(name = "description", length = 1023, nullable = true, unique = false)
    private String description;

    @Column(name = "beneficiary_included", columnDefinition = "bit(1) default false")
    private boolean beneficiaryIncluded;

    @Column(name = "deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    public boolean isBeneficiaryIncluded()
    {
        return beneficiaryIncluded;
    }

    /**
     *
     * @param beneficiaryIncluded
     */
    public void setBeneficiaryIncluded(boolean beneficiaryIncluded)
    {
        this.beneficiaryIncluded = beneficiaryIncluded;
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
        final TrainingType other = (TrainingType) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.wfp.lmmis.training.model.TrainingType[ id=" + id + " ]";
    }

}
