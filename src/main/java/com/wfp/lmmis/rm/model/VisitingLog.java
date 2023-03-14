/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.rm.model;

import com.wfp.lmmis.utility.CalendarUtility;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author demeter
 */
@Entity
@Table(name = "visiting_log")

public class VisitingLog implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ip_st")
    private String ipSt;
    @Column(name = "login_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar loginDate;
    @Column(name = "logout_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar logoutDate;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "logged")
    private boolean logged;

    public VisitingLog()
    {
        loginDate = CalendarUtility.getCurrentDateAsCalendar();
        logged = true;
    }

    public boolean isLogged()
    {
        return logged;
    }

    public void setLogged(boolean logged)
    {
        this.logged = logged;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getIpSt()
    {
        return ipSt;
    }

    public void setIpSt(String ipSt)
    {
        this.ipSt = ipSt;
    }

    public Calendar getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(Calendar loginDate)
    {
        this.loginDate = loginDate;
    }

    public Calendar getLogoutDate()
    {
        return logoutDate;
    }

    /**
     *
     * @param logoutDate
     */
    public void setLogoutDate(Calendar logoutDate)
    {
        this.logoutDate = logoutDate;
    }

    /**
     *
     * @return
     */
    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
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
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VisitingLog))
        {
            return false;
        }
        VisitingLog other = (VisitingLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.wfp.cbs.rm.ejb.entity.VisitingLog[idNr=" + id + "]";
    }

}
