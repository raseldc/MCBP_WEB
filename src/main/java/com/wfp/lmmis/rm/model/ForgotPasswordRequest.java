/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.rm.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Philip
 */
@Entity
@Table(name = "forgot_password_request")
public class ForgotPasswordRequest implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "requested_on")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date requestedOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    /**
     *
     * @return
     */
    public Date getRequestedOn()
    {
        return requestedOn;
    }

    public void setRequestedOn(Date requestedOn)
    {
        this.requestedOn = requestedOn;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
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
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ForgotPasswordRequest other = (ForgotPasswordRequest) obj;
        return !(this.id != other.id && (this.id == null || !this.id.equals(other.id)));
    }
}
