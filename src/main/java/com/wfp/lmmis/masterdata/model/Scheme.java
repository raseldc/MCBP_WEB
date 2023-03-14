package com.wfp.lmmis.masterdata.model;

import com.wfp.lmmis.model.BaseModel;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "scheme")
public class Scheme extends BaseModel implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "tinyint unsigned")
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "name_in_bangla", length = 255, nullable = false, unique = true)
    private String nameInBangla;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "name_in_english", length = 255, nullable = false, unique = true)
    private String nameInEnglish;

    @Size(min = 4, max = 4)
    @Column(name = "code", length = 4, nullable = false, unique = true, columnDefinition = "smallint(4) unsigned")
    private String code;

    @Column(name = "active", nullable = false, unique = false)
    private boolean active = false;

    @NotNull
    @Column(name = "default_month", nullable = false, unique = false, columnDefinition = "tinyint unsigned")
    private Integer defaultMonth;

    @NotNull
    @Column(name = "activation_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar activationDate;

    @Column(name = "deactivation_date", nullable = true)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar deactivationDate;

    @Size(min = 0, max = 1023)
    @Column(name = "description", length = 1023, nullable = true, unique = false)
    private String description;

    @Column(name = "deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

    @Column(name = "short_name")
    private String shortName;

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

    public void setNameInEnglish(String nameInEnglish)
    {
        this.nameInEnglish = nameInEnglish;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isActive()
    {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }

    public Integer getDefaultMonth()
    {
        return defaultMonth;
    }

    /**
     *
     * @param defaultMonth
     */
    public void setDefaultMonth(Integer defaultMonth)
    {
        this.defaultMonth = defaultMonth;
    }

    public Calendar getActivationDate()
    {
        return activationDate;
    }

    public void setActivationDate(Calendar activationDate)
    {
        this.activationDate = activationDate;
    }

    /**
     *
     * @return
     */
    public Calendar getDeactivationDate()
    {
        return deactivationDate;
    }

    public void setDeactivationDate(Calendar deactivationDate)
    {
        this.deactivationDate = deactivationDate;
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

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    @Override
    public String toString()
    {
        return this.getNameInEnglish();
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Scheme other = (Scheme) obj;
        if (!Objects.equals(this.nameInEnglish, other.nameInEnglish))
        {
            return false;
        }
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}
