/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.model;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.rm.model.User;
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
@Entity
@Table(name = "change_log")
public class ChangeLog implements Serializable
{

    @Id    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "logging_service_type", nullable = false)
    private LoggingServiceType loggingServiceType;
    
    @Column(name = "logging_table_type", nullable = false)
    private LoggingTableType loggingTableType;

    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    
    @ManyToOne
    @JoinColumn(name = "changed_by", columnDefinition = "smallint unsigned", nullable = false)
    private User changedBy;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "changed_date", nullable = false)
    private Calendar changedDate;

    @Column(name = "before_value")
    private String beforeValue;

    @Column(name = "after_value")
    private String afterValue;
    
    @Column(name = "column_name")
    private String columnName;
    
    /**
     *
     */
    public ChangeLog()
    {
    }

    public ChangeLog(Integer id, LoggingServiceType loggingServiceType, LoggingTableType loggingTableType, Integer recordId, User changedBy, Calendar changedDate, String beforeValue, String afterValue)
    {
        this.id = id;
        this.loggingServiceType = loggingServiceType;
        this.loggingTableType = loggingTableType;
        this.recordId = recordId;
        this.changedBy = changedBy;
        this.changedDate = changedDate;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
    }
    public ChangeLog(Integer id, LoggingServiceType loggingServiceType, LoggingTableType loggingTableType, Integer recordId, User changedBy, Calendar changedDate, String beforeValue, String afterValue, String columnName)
    {
        this.id = id;
        this.loggingServiceType = loggingServiceType;
        this.loggingTableType = loggingTableType;
        this.recordId = recordId;
        this.changedBy = changedBy;
        this.changedDate = changedDate;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
        this.columnName = columnName;
    }
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public LoggingServiceType getLoggingServiceType()
    {
        return loggingServiceType;
    }

    /**
     *
     * @param loggingServiceType
     */
    public void setLoggingServiceType(LoggingServiceType loggingServiceType)
    {
        this.loggingServiceType = loggingServiceType;
    }

    public LoggingTableType getLoggingTableType()
    {
        return loggingTableType;
    }

    public void setLoggingTableType(LoggingTableType loggingTableType)
    {
        this.loggingTableType = loggingTableType;
    }

    /**
     *
     * @return
     */
    public Integer getRecordId()
    {
        return recordId;
    }

    public void setRecordId(Integer recordId)
    {
        this.recordId = recordId;
    }

    /**
     *
     * @return
     */
    public User getChangedBy()
    {
        return changedBy;
    }

    public void setChangedBy(User changedBy)
    {
        this.changedBy = changedBy;
    }

    public Calendar getChangedDate()
    {
        return changedDate;
    }

    public void setChangedDate(Calendar changedDate)
    {
        this.changedDate = changedDate;
    }

    public String getBeforeValue()
    {
        return beforeValue;
    }

    /**
     *
     * @param beforeValue
     */
    public void setBeforeValue(String beforeValue)
    {
        this.beforeValue = beforeValue;
    }

    /**
     *
     * @return
     */
    public String getAfterValue()
    {
        return afterValue;
    }

    public void setAfterValue(String afterValue)
    {
        this.afterValue = afterValue;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

}
