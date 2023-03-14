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

@Entity
@Table(name = "payment_cycle")
public class PaymentCycle extends BaseModel implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_in_bangla", nullable = false)
    private String nameInBangla;

    @Column(name = "name_in_english", nullable = false)
    private String nameInEnglish;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fiscal_year_in_english", referencedColumnName = "name_in_english", nullable = false)
    private FiscalYear fiscalYear;

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

    @Column(name = "ministry_code", nullable = false)
    private Integer ministryCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_code", referencedColumnName = "code", nullable = false)
    private Scheme scheme;

    @Column(name = "allowance_amount", nullable = false)
    private Float allowanceAmount;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_cycle_id", nullable = true)
    private PaymentCycle parentPaymentCycle;

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

    /**
     *
     * @param nameInBangla
     */
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

    public FiscalYear getFiscalYear()
    {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear)
    {
        this.fiscalYear = fiscalYear;
    }

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

    public Float getAllowanceAmount()
    {
        return allowanceAmount;
    }

    public void setAllowanceAmount(Float allowanceAmount)
    {
        this.allowanceAmount = allowanceAmount;
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

    /**
     *
     * @return
     */
    public Integer getMinistryCode()
    {
        return ministryCode;
    }

    public void setMinistryCode(Integer ministryCode)
    {
        this.ministryCode = ministryCode;
    }

    public PaymentCycle getParentPaymentCycle()
    {
        return parentPaymentCycle;
    }

    public void setParentPaymentCycle(PaymentCycle parentPaymentCycle)
    {
        this.parentPaymentCycle = parentPaymentCycle;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode()
    {
        int hash = 5;
        return hash;
    }

    /**
     *
     * @param obj
     * @return
     */
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
        final PaymentCycle other = (PaymentCycle) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}
