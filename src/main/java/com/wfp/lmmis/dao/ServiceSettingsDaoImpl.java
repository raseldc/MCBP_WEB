/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dao;

import com.wfp.lmmis.enums.ServiceType;
import com.wfp.lmmis.model.ServiceSettings;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceSettingsDaoImpl implements ServiceSettingsDao
{

    //private static final logger logger = //logger.getlogger(ServiceSettingsDaoImpl.class);
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    /**
     *
     * @param serviceType
     * @return
     */
    @Override
    public ServiceSettings getServiceSettings(ServiceType serviceType)
    {
        String query = "select s from ServiceSettings s where s.serviceType=:serviceType";
        try
        {
            ServiceSettings serviceSettings = (ServiceSettings) this.sessionFactory.getCurrentSession().createQuery(query).setParameter("serviceType", serviceType).uniqueResult();
            return serviceSettings;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("err = " + e.getMessage());
        }
        return null;
    }

}
