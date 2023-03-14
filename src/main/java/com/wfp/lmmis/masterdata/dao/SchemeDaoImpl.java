/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author rasel
 */
@Repository
public class SchemeDaoImpl implements SchemeDao
{

    @Autowired
    SessionFactory sessionFactory;

    /**
     *
     * @return
     */
    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Scheme getScheme(Integer id)
    {
        return (Scheme) this.sessionFactory.getCurrentSession().get(Scheme.class, id);
    }

    /**
     *
     * @param scheme
     */
    @Override
    public void save(Scheme scheme)
    {
        try
        {
            this.sessionFactory.getCurrentSession().save(scheme);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void edit(Scheme editedScheme)
    {
        Scheme scheme = (Scheme) sessionFactory.getCurrentSession().get(Scheme.class, editedScheme.getId());
        scheme.setNameInEnglish(editedScheme.getNameInEnglish());
        scheme.setNameInBangla(editedScheme.getNameInBangla());
        scheme.setCode(editedScheme.getCode());
        scheme.setDefaultMonth(editedScheme.getDefaultMonth());
        scheme.setActive(editedScheme.isActive());
        scheme.setActivationDate(editedScheme.getActivationDate());
        scheme.setDeactivationDate(editedScheme.getDeactivationDate());
        scheme.setDescription(editedScheme.getDescription());
        scheme.setModifiedBy(editedScheme.getModifiedBy());
        scheme.setModificationDate(editedScheme.getModificationDate());
        this.sessionFactory.getCurrentSession().update(scheme);
    }

    /**
     *
     * @param ministryId
     * @param activeOnly
     * @return
     */
    @Override
    public List<Scheme> getSchemeList(Integer ministryId, boolean activeOnly)
    {
        @SuppressWarnings("unchecked")
        String querySt = "from Scheme o where o.deleted=0 ";
        if (activeOnly == true)
        {
            querySt += " and o.active = 1";
        }
        if (null != ministryId)
        {
            querySt += " and o.ministry.id = " + ministryId;
        }
        List<Scheme> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        return list;
    }

    /**
     *
     * @param ministryId
     * @return
     */
    @Override
    public List<ItemObject> getSchemeIoList(Integer ministryId)
    {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (ministryId == null)
        {
            querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish)  from Scheme o where o.deleted=0 and o.active=1";
        }
        else
        {
            querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish)  from Scheme o where o.deleted=0 and o.active=1 and o.ministry.id = " + ministryId;
        }

        List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).list();
        return itemObjects;
    }

    @Override
    public void delete(Scheme scheme)
    {
        try
        {
            Query q = this.getCurrentSession().createQuery("update Scheme set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + scheme.getId());
            q.setParameter("modifiedBy", scheme.getModifiedBy());
            q.setParameter("modificationDate", scheme.getModificationDate());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Scheme, scheme.getId(), scheme.getModifiedBy(), scheme.getModificationDate(), scheme.getNameInEnglish(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
