/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.model.ApplicationDeadline;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Philip
 */
@Repository
public class ApplicationDeadlineDaoImpl implements ApplicationDeadlineDao
{

    //private static final logger logger = //logger.getlogger(ApplicationDeadlineDaoImpl.class);
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(ApplicationDeadline applicationDeadline)
    {
        try
        {
            String selectQuery = "select o from ApplicationDeadline o where o.upazila.id=:upazilaId"
                    + " and o.fiscalYear.id=:fiscalYearId and o.scheme.id=:schemeId";
            ApplicationDeadline applicationDeadline1 = (ApplicationDeadline) this.getCurrentSession().createQuery(selectQuery).setParameter("upazilaId", applicationDeadline.getUpazila().getId()).
                    setParameter("fiscalYearId", applicationDeadline.getFiscalYear().getId()).setParameter("schemeId", applicationDeadline.getScheme().getId()).uniqueResult();
            if (applicationDeadline1 != null)
            {
                delete(applicationDeadline1);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        this.getCurrentSession().save(applicationDeadline);
    }

    public void delete(ApplicationDeadline applicationDeadline)
    {
        this.getCurrentSession().delete(applicationDeadline);
    }

    @Override
    public Calendar getApplicationDeadlineByUpazila(Integer upazilaId, Integer fiscalYearId, Integer schemeId)
    {
        String query = "Select o.deadline from ApplicationDeadline o join o.upazila u join o.fiscalYear fy join o.scheme s where u.id=:upazilaId"
                + " and fy.id=:fiscalYearId and s.id=:schemeId";
        System.out.println("query " + query + upazilaId + fiscalYearId + schemeId);
        Calendar deadline = null;
        try
        {
            Object object = this.getCurrentSession().createQuery(query).setParameter("upazilaId", upazilaId).setParameter("fiscalYearId", fiscalYearId).setParameter("schemeId", schemeId).uniqueResult();
            if (object != null)
            {
                deadline = (Calendar) object;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        System.out.println("deadline: " + deadline);
        return deadline;
    }

    @Override
    public ApplicationDeadline getApplicationDeadline(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
