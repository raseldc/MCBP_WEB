/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.model.PaymentCycle;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FiscalYearDaoImpl implements FiscalYearDao
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public FiscalYear getFiscalYear(Integer id)
    {
        FiscalYear fiscalyear = (FiscalYear) this.sessionFactory.getCurrentSession().get(FiscalYear.class, id);
        return fiscalyear;
    }

    @Override
    public boolean save(FiscalYear fiscalYear)
    {
        try
        {
            String duplicateCheckQuery = "select count(id) from FiscalYear where nameInEnglish = '" + fiscalYear.getNameInEnglish() + "' or nameInBangla = '" + fiscalYear.getNameInBangla() + "'";
            System.out.println("query = " + duplicateCheckQuery);
            long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
            System.out.println("duplicate count = " + duplicateCount);
            if(duplicateCount == 0){
            this.sessionFactory.getCurrentSession().save(fiscalYear);
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.FiscalYear, fiscalYear.getId(), fiscalYear.getCreatedBy(), fiscalYear.getCreationDate(), "", fiscalYear.getNameInBangla());
            this.getCurrentSession().save(changeLog);
            return true;
            }
            else
            {
                return false;
            }
            
        }
        catch (Exception e)
        {
            System.out.println("exc in save: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param editedFiscalYear
     * @throws ExceptionWrapper
     */
    @Override
    public void edit(FiscalYear editedFiscalYear) throws ExceptionWrapper
    {
        try
        {
            FiscalYear fiscalYear = (FiscalYear) this.sessionFactory.getCurrentSession().get(FiscalYear.class, editedFiscalYear.getId());
            if (fiscalYear != null)
            {
                fiscalYear.setNameInBangla(editedFiscalYear.getNameInBangla());
                fiscalYear.setNameInEnglish(editedFiscalYear.getNameInEnglish());
                fiscalYear.setStartYear(editedFiscalYear.getStartYear());
                fiscalYear.setEndYear(editedFiscalYear.getEndYear());
                fiscalYear.setActive(editedFiscalYear.isActive());
                fiscalYear.setModifiedBy(editedFiscalYear.getModifiedBy());
                fiscalYear.setModificationDate(editedFiscalYear.getModificationDate());
            }
            this.sessionFactory.getCurrentSession().update(fiscalYear);
        }
        catch (Exception e)
        {
            throw new ExceptionWrapper(e.getMessage());
        }

    }

    @Override
    public void delete(FiscalYear fiscalYear) throws ExceptionWrapper
    {
        try
        {
//            Query q = this.getCurrentSession().createQuery("update FiscalYear set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + fiscalYear.getId());
//            q.setParameter("modifiedBy", fiscalYear.getModifiedBy());
//            q.setParameter("modificationDate", fiscalYear.getModificationDate());
//            q.executeUpdate();
            Query q = this.sessionFactory.getCurrentSession().createQuery("delete from FiscalYear where id = " + fiscalYear.getId());
            q.executeUpdate();

            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.FiscalYear, fiscalYear.getId(), fiscalYear.getModifiedBy(), fiscalYear.getModificationDate(), fiscalYear.getNameInBangla(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<FiscalYear> getFiscalYearList(boolean isSearchByActive, boolean isActive)
    {
        try
        {
            @SuppressWarnings("unchecked")
            String querySt = "from FiscalYear o where 0 = 0 and o.deleted = 0";
            if (isSearchByActive)
            {
                querySt += " and o.active = " + isActive;
            }
            querySt += " order by o.nameInEnglish desc";

            List<FiscalYear> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return list;
        }
        catch (Exception e)
        {
            System.out.println("exc=" + e.getMessage());
            return null;
        }
    }

    @Override
    public FiscalYear getActiveFiscalYear()
    {
        try
        {
            String querySt = "from FiscalYear o where o.active = 1";
            FiscalYear fiscalYear = (FiscalYear) sessionFactory.getCurrentSession().createQuery(querySt).uniqueResult();
            return fiscalYear;
        }
        catch (HibernateException e)
        {
            return null;
        }
    }

    @Override
    public List<ItemObject> getFiscalYearIoList()
    {
        try
        {
            List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery("select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish)  from FiscalYear o where o.deleted = 0 and o.active = 1").list();
            return itemObjects;
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkUniqueFiscalYear(Integer fiscalYearId, String nameInEnglish)
    {
        // return true if unique
        try
        {
            String querySt = "";
            if (null == fiscalYearId)
            {
                querySt = "from FiscalYear o where o.nameInEnglish = '" + nameInEnglish + "'";
            }
            else
            {
                querySt = "from FiscalYear o where o.nameInEnglish = '" + nameInEnglish + "' and o.id != " + fiscalYearId;
            }
            List<PaymentCycle> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return list.isEmpty();
        }
        catch (Exception e)
        {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
