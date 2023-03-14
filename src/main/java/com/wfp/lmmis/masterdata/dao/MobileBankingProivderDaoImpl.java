/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.model.MobileBankingProvider;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MobileBankingProivderDaoImpl implements MobileBankingProviderDao
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public MobileBankingProvider getMobileBankingProvider(Integer id)
    {
        MobileBankingProvider mobileBankingProvider = (MobileBankingProvider) this.sessionFactory.getCurrentSession().get(MobileBankingProvider.class, id);
        return mobileBankingProvider;
    }

    @Override
    public void save(MobileBankingProvider mobileBankingProvider)
    {
        this.sessionFactory.getCurrentSession().save(mobileBankingProvider);
        ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.MobileBankingProvider, mobileBankingProvider.getId(), mobileBankingProvider.getModifiedBy(), mobileBankingProvider.getModificationDate(), mobileBankingProvider.getNameInBangla(), "deleted");
        this.getCurrentSession().save(changeLog);
    }

    @Override
    public void edit(MobileBankingProvider editedMobileBankingProvider)
    {
        MobileBankingProvider mobileBankingProvider = (MobileBankingProvider) this.sessionFactory.getCurrentSession().get(MobileBankingProvider.class, editedMobileBankingProvider.getId());
        if (mobileBankingProvider != null)
        {
            mobileBankingProvider.setNameInBangla(editedMobileBankingProvider.getNameInBangla());
            mobileBankingProvider.setNameInEnglish(editedMobileBankingProvider.getNameInEnglish());
            mobileBankingProvider.setCode(editedMobileBankingProvider.getCode());
            mobileBankingProvider.setRoutingNumber(editedMobileBankingProvider.getRoutingNumber());
            mobileBankingProvider.setActive(editedMobileBankingProvider.isActive());
            mobileBankingProvider.setModifiedBy(editedMobileBankingProvider.getModifiedBy());
            mobileBankingProvider.setModificationDate(editedMobileBankingProvider.getModificationDate());
        }
        this.sessionFactory.getCurrentSession().update(mobileBankingProvider);
    }

    @Override
    public void delete(MobileBankingProvider mobileBankingProvider) throws ExceptionWrapper
    {
        try
        {
            Query q = this.getCurrentSession().createQuery("update MobileBankingProvider set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + mobileBankingProvider.getId());
            q.setParameter("modifiedBy", mobileBankingProvider.getModifiedBy());
            q.setParameter("modificationDate", mobileBankingProvider.getModificationDate());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.MobileBankingProvider, mobileBankingProvider.getId(), mobileBankingProvider.getModifiedBy(), mobileBankingProvider.getModificationDate(), mobileBankingProvider.getNameInBangla(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<MobileBankingProvider> getMobileBankingProviderList(boolean activeOnly)
    {
        try
        {
            @SuppressWarnings("unchecked")
            String querySt = "from MobileBankingProvider o where o.deleted = 0";
            if (activeOnly)
            {
                querySt += " and o.active = 1";
            }
            List<MobileBankingProvider> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            System.out.println("size = " + list.size());
            return list;
        }
        catch (Exception e)
        {
            System.out.println("exc=" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<ItemObject> getMobileBankingProviderIoList(boolean activeOnly)
    {
        try
        {
            System.out.println("in get ");
            String querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish,o.accountNoLength)  from MobileBankingProvider o where o.deleted = 0";
            if (activeOnly)
            {
                querySt += " and o.active = 1";
            }

            List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).list();
            System.out.println("size="+itemObjects.size());
            return itemObjects;
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }
}
