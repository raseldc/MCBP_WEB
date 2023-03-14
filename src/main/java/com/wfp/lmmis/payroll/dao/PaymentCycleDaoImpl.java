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
import com.wfp.lmmis.payroll.model.PaymentCycle;
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
public class PaymentCycleDaoImpl implements PaymentCycleDao
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public PaymentCycle getPaymentCycle(Integer id)
    {
        PaymentCycle paymentcycle = (PaymentCycle) this.sessionFactory.getCurrentSession().get(PaymentCycle.class, id);
        return paymentcycle;
    }

    @Override
    public void save(PaymentCycle paymentCycle)
    {
        try
        {
            System.out.println("paymentCycle save calling ");
            paymentCycle.setMonthCount(CommonUtility.getMonthCountBetweenCalendars(paymentCycle.getStartDate(), paymentCycle.getEndDate()));
            this.sessionFactory.getCurrentSession().save(paymentCycle);
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.PaymentCycle, paymentCycle.getId(), paymentCycle.getCreatedBy(), paymentCycle.getCreationDate(), "", paymentCycle.getNameInBangla());
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            System.out.println("exc in save: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void edit(PaymentCycle editedPaymentCycle) throws ExceptionWrapper
    {
        try
        {
            PaymentCycle paymentcycle = (PaymentCycle) this.sessionFactory.getCurrentSession().get(PaymentCycle.class, editedPaymentCycle.getId());
            if (paymentcycle != null)
            {
                paymentcycle.setNameInBangla(editedPaymentCycle.getNameInBangla());
                paymentcycle.setNameInEnglish(editedPaymentCycle.getNameInEnglish());
                paymentcycle.setFiscalYear(editedPaymentCycle.getFiscalYear());
                paymentcycle.setStartDate(editedPaymentCycle.getStartDate());
                paymentcycle.setEndDate(editedPaymentCycle.getEndDate());
                paymentcycle.setMonthCount(CommonUtility.getMonthCountBetweenCalendars(editedPaymentCycle.getStartDate(), editedPaymentCycle.getEndDate()));
                paymentcycle.setMinistryCode(editedPaymentCycle.getMinistryCode());
                paymentcycle.setScheme(editedPaymentCycle.getScheme());
                paymentcycle.setAllowanceAmount(editedPaymentCycle.getAllowanceAmount());
                paymentcycle.setActive(editedPaymentCycle.isActive());

                paymentcycle.setModifiedBy(editedPaymentCycle.getModifiedBy());
                paymentcycle.setModificationDate(editedPaymentCycle.getModificationDate());
            }
            this.sessionFactory.getCurrentSession().update(paymentcycle);
        }
        catch (Exception e)
        {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public boolean delete(PaymentCycle paymentCycle) throws ExceptionWrapper
    {
        try
        {
            String querySt = "select p.id from Payment p where p.paymentCycle.id = " + paymentCycle.getId();
            Integer paymentRefCount = this.sessionFactory.getCurrentSession().createQuery(querySt).list().size();
//            System.out.println("Num of payment refers to paymentCycle " + paymentRefCount);
            if (paymentRefCount != 0)
            {
                return false;
            }

            querySt = "select ps.id from PayrollSummary ps where ps.paymentCycle.id = " + paymentCycle.getId();
            Integer payrollSummaryRefCount = this.sessionFactory.getCurrentSession().createQuery(querySt).list().size();
//            System.out.println("Num of payrollSummary refers to paymentCycle " + payrollSummaryRefCount);
            if (payrollSummaryRefCount != 0)
            {
                return false;
            }

            // Hard Delete ! Be cautious
            Query q = this.getCurrentSession().createQuery("delete from PaymentCycle where id=" + paymentCycle.getId());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.PaymentCycle, paymentCycle.getId(), paymentCycle.getModifiedBy(), paymentCycle.getModificationDate(), paymentCycle.getNameInBangla(), "deleted");
            this.getCurrentSession().save(changeLog);
            return true;
        }
        catch (Exception e)
        {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<PaymentCycle> getPaymentCycleList(Integer schemeId)
    {
        try
        {
            @SuppressWarnings("unchecked")
            List<PaymentCycle> list = sessionFactory.getCurrentSession().createQuery("from PaymentCycle o where o.deleted = 0 order by o.startDate").list();
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
    public List<PaymentCycle> getParentPaymentCycleList(Integer schemeId)
    {
        try
        {
            @SuppressWarnings("unchecked")
            List<PaymentCycle> list = sessionFactory.getCurrentSession().createQuery("from PaymentCycle o where o.deleted = 0 and o.parentPaymentCycle is null order by o.startDate").list();
            return list;
        }
        catch (Exception e)
        {
            System.out.println("exc=" + e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param isSearchByFiscalYear
     * @param fiscalYearId
     * @param isSearchByActive
     * @param isActive
     * @param schemeId
     * @return
     */
    @Override
    public List<ItemObject> getPaymentCycleIoList(boolean isSearchByFiscalYear, Integer fiscalYearId,
            boolean isSearchByActive, boolean isActive, Integer schemeId)
    {
        try
        {
            @SuppressWarnings("unchecked")
            String querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish) from PaymentCycle o where 0 = 0 ";
            querySt += " and o.deleted = 0 and o.scheme.id=" + schemeId;
            if (isSearchByFiscalYear)
            {
                querySt += " and o.fiscalYear.id = " + fiscalYearId;
            }
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
    public List<ItemObject> getParentPaymentCycleIoList(Integer fiscalYearId, Integer schemeId)
    {
        try
        {
            @SuppressWarnings("unchecked")
            String querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish) from PaymentCycle o where 0 = 0 ";
            querySt += " and o.deleted = 0 and o.parentPaymentCycle.id is null";
            querySt += " and o.fiscalYear.id = " + fiscalYearId;
            if (schemeId != null)
            {
                querySt += " and o.scheme.id = " + schemeId;
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

    /**
     * this method is used to load parent cycles when user selects scheme in
     * payment cycle page
     *
     * @param schemeId
     * @return
     */
    @Override
    public List<ItemObject> getParentPaymentCycleIoListByScheme(Integer schemeId)
    {
        try
        {
            @SuppressWarnings("unchecked")
            String querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish) from PaymentCycle o where 0 = 0 ";
            querySt += " and o.deleted = 0 and o.parentPaymentCycle.id is null";

            querySt += " and o.scheme.id = " + schemeId;

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
    public List<ItemObject> getChildPaymentCycleIoList(Integer fiscalYearId, Integer schemeId)
    {
        try
        {
            @SuppressWarnings("unchecked")
            String querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish) from PaymentCycle o where 0 = 0 ";
            querySt += " and o.deleted = 0 and o.parentPaymentCycle.id is not null";

            querySt += " and o.fiscalYear.id = " + fiscalYearId;
            if (schemeId != null)
            {
                querySt += " and o.scheme.id = " + schemeId;
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
    public boolean checkUniquePaymentCycle(Integer schemeId, Integer paymentCycleId, String nameInEnglish)
    {
        // return true if unique
        try
        {

            String querySt;
            if (null == paymentCycleId)
            {
                querySt = "from PaymentCycle o where o.nameInEnglish = :name and o.scheme.id=:schemeId";
            }
            else
            {
                querySt = "from PaymentCycle o where o.nameInEnglish = :name and o.scheme.id=:schemeId and o.id != " + paymentCycleId;
            }
            List<PaymentCycle> list = sessionFactory.getCurrentSession().createQuery(querySt).setParameter("name", nameInEnglish).setParameter("schemeId", schemeId).list();
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
