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
import com.wfp.lmmis.payroll.model.Batch;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BatchDaoImpl implements BatchDao
{

    //private static final logger logger = //logger.getlogger(UserController.class);
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

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Batch getBatch(Integer id)
    {
        Batch batch = (Batch) this.sessionFactory.getCurrentSession().get(Batch.class, id);
        return batch;
    }

    @Override
    public void save(Batch batch)
    {
        try
        {
            batch.setMonthCount(CommonUtility.getMonthCountBetweenCalendars(batch.getStartDate(), batch.getEndDate()));
            this.sessionFactory.getCurrentSession().save(batch);
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.Batch, batch.getId(), batch.getCreatedBy(), batch.getCreationDate(), "", batch.getNameInBangla());
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            System.out.println("exc in save: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     * @param editedBatch
     * @throws ExceptionWrapper
     */
    @Override
    public void edit(Batch editedBatch) throws ExceptionWrapper
    {
        try
        {
            Batch batch = (Batch) this.sessionFactory.getCurrentSession().get(Batch.class, editedBatch.getId());
            if (batch != null)
            {
                batch.setNameInBangla(editedBatch.getNameInBangla());
                batch.setNameInEnglish(editedBatch.getNameInEnglish());
                batch.setStartDate(editedBatch.getStartDate());
                batch.setEndDate(editedBatch.getEndDate());
                batch.setMonthCount(CommonUtility.getMonthCountBetweenCalendars(editedBatch.getStartDate(), editedBatch.getEndDate()));
                batch.setScheme(editedBatch.getScheme());
                batch.setActive(editedBatch.isActive());

                batch.setModifiedBy(editedBatch.getModifiedBy());
                batch.setModificationDate(editedBatch.getModificationDate());
            }
            this.sessionFactory.getCurrentSession().update(batch);
        }
        catch (Exception e)
        {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public boolean delete(Batch batch) throws ExceptionWrapper
    {
        try
        {
            String querySt = "select a.Id from Applicant a where a.batch.id = " + batch.getId();
            Integer batchApplicantRefCount = this.sessionFactory.getCurrentSession().createQuery(querySt).list().size();
            if (batchApplicantRefCount != 0)
            {
                return false;
            }

            querySt = "select b.Id from Beneficiary b where b.batch.id = " + batch.getId();
            Integer batchBeneficiaryRefCount = this.sessionFactory.getCurrentSession().createQuery(querySt).list().size();
//            System.out.println("Num of payrollSummary refers to paymentCycle " + payrollSummaryRefCount);
            if (batchBeneficiaryRefCount != 0)
            {
                return false;
            }

            // Hard Delete ! Be cautious
            Query q = this.getCurrentSession().createQuery("delete from Batch where id=" + batch.getId());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Batch, batch.getId(), batch.getModifiedBy(), batch.getModificationDate(), batch.getNameInBangla(), "deleted");
            this.getCurrentSession().save(changeLog);
            return true;
        }
        catch (Exception e)
        {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<Batch> getBatchList()
    {
        try
        {
            @SuppressWarnings("unchecked")
            List<Batch> list = sessionFactory.getCurrentSession().createQuery("from Batch o where o.deleted = 0 order by o.startDate").list();
            return list;
        }
        catch (Exception e)
        {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Batch> getBatchListByScheme(Integer schemeId)
    {
        try
        {
            @SuppressWarnings("unchecked")
            List<Batch> list = sessionFactory.getCurrentSession().createQuery("from Batch o where o.deleted = 0 and o.scheme.id = " + schemeId + " order by o.startDate").list();
            return list;
        }
        catch (Exception e)
        {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ItemObject> getBatchIoList(Integer schemeId,
            boolean isSearchByActive, boolean isActive)
    {
        try
        {
            String querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish) from Batch o where 0 = 0 ";
            querySt += " and o.deleted = 0 ";
            if (isSearchByActive)
            {
                querySt += " and o.active = " + isActive;
            }

            querySt += " order by o.startDate";

            List<ItemObject> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return list;
        }
        catch (Exception e)
        {
            System.out.println("exc=" + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean checkUniqueBatch(Integer batchId, String nameInEnglish, Integer schemeId)
    {
        // return true if unique
        try
        {
            String querySt = "";
            if (null == batchId)
            {
                querySt = "from Batch o where o.nameInEnglish = '" + nameInEnglish + "' and o.scheme.id = "+schemeId;
            }
            else
            {
                querySt = "from Batch o where o.nameInEnglish = '" + nameInEnglish + "' and o.scheme.id = "+schemeId+" and o.id != " + batchId;
            }
            List<Batch> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
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
