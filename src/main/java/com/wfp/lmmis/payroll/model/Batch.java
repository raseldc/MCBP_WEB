package com.wfp.lmmis.payroll.model;

import com.wfp.lmmis.masterdata.model.Scheme;
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
 * @author rasel
 */
@Entity
@Table(name = "batch")
public class Batch extends BaseModel implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_in_bangla", nullable = false)
    private String nameInBangla;

    @Column(name = "name_in_english", nullable = false)
    private String nameInEnglish;

    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    @Column(name = "end_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar endDate;

    @Column(name = "month_count", nullable = false)
    private Integer monthCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "is_deleted", columnDefinition = "bit(1) default false")
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

    public void setNameInEnglish(String nameInEnglish)
    {
        this.nameInEnglish = nameInEnglish;
    }

    /**
     *
     * @return
     */
    public Calendar getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Calendar startDate)
    {
        this.startDate = startDate;
    }

    public Calendar getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Calendar endDate)
    {
        this.endDate = endDate;
    }

    public Integer getMonthCount()
    {
        return monthCount;
    }

    public void setMonthCount(Integer monthCount)
    {
        this.monthCount = monthCount;
    }

    public Scheme getScheme()
    {
        return scheme;
    }

    public void setScheme(Scheme scheme)
    {
        this.scheme = scheme;
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

    @Override
    public int hashCode()
    {
        int hash = 5;
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
        final Batch other = (Batch) obj;
        return Objects.equals(this.id, other.id);
    }

}
