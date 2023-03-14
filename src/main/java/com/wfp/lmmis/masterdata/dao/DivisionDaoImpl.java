/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

@Repository
public class DivisionDaoImpl implements DivisionDao
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    SessionFactory sessionFactory;

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
    public Division getDivision(Integer id)
    {
        Division division = (Division) this.sessionFactory.getCurrentSession().get(Division.class, id);
        return division;
    }

    @Override
    public boolean save(Division division)
    {
        String duplicateCheckQuery = "select count(id) from Division where nameInEnglish = '" + division.getNameInEnglish() + "' or nameInBangla = '" + division.getNameInBangla() + "' or code = '" + division.getCode() + "'";
        System.out.println("query = " + duplicateCheckQuery);
        long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
        System.out.println("duplicate count = " + duplicateCount);
        if (duplicateCount == 0)
        {
            System.out.println("duplicate not found");
            this.sessionFactory.getCurrentSession().save(division);
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.Division, division.getId(), division.getCreatedBy(), division.getCreationDate(), null, division.getNameInBangla() + " status:" + division.isActive() + " code:" + division.getCode());
            this.getCurrentSession().save(changeLog);
            return true;
        }
        else
        {
            return false;
        }

    }

    @Override
    public void edit(Division editedDivision)
    {
        Division division = (Division) this.sessionFactory.getCurrentSession().get(Division.class, editedDivision.getId());
        if (division != null)
        {
            if (!division.getNameInBangla().equals(editedDivision.getNameInBangla()))
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Division, division.getId(), editedDivision.getModifiedBy(), editedDivision.getModificationDate(), division.getNameInBangla(), editedDivision.getNameInBangla(), "name_in_bangla");
                this.getCurrentSession().save(changeLog);
            }
            if (!division.getNameInEnglish().equals(editedDivision.getNameInEnglish()))
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Division, division.getId(), editedDivision.getModifiedBy(), editedDivision.getModificationDate(), division.getNameInEnglish(), editedDivision.getNameInEnglish(), "name_in_english");
                this.getCurrentSession().save(changeLog);
            }
            if (!division.getCode().equals(editedDivision.getCode()))
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Division, division.getId(), editedDivision.getModifiedBy(), editedDivision.getModificationDate(), division.getCode(), editedDivision.getCode(), "code");
                this.getCurrentSession().save(changeLog);
            }
            if (division.isActive() != editedDivision.isActive())
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Division, division.getId(), editedDivision.getModifiedBy(), editedDivision.getModificationDate(), String.valueOf(division.isActive()), String.valueOf(editedDivision.isActive()), "active");
                this.getCurrentSession().save(changeLog);
            }

            division.setNameInBangla(editedDivision.getNameInBangla());
            division.setNameInEnglish(editedDivision.getNameInEnglish());
            division.setCode(editedDivision.getCode());
            division.setActive(editedDivision.isActive());
            division.setModifiedBy(editedDivision.getModifiedBy());
            division.setModificationDate(editedDivision.getModificationDate());
        }
        this.sessionFactory.getCurrentSession().update(division);
    }

    @Override
    public boolean delete(Division division)
    {
        if (division != null)
        {
            System.out.println("Division not null id is = " + division.getId() + " name = " + division.getNameInEnglish());
            String querySt = "select d.id from District d where d.division.id = " + division.getId();
            System.out.println("query = " + querySt);
            Integer districtRefCount = this.sessionFactory.getCurrentSession().createQuery(querySt).list().size();
            if (districtRefCount == 0)
            {
//                Query q = this.sessionFactory.getCurrentSession().createQuery("update Division set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id = " + division.getId());
//                q.setParameter("modifiedBy", division.getModifiedBy());
//                q.setParameter("modificationDate", division.getModificationDate());
                Query q = this.sessionFactory.getCurrentSession().createQuery("delete from Division where id = " + division.getId());
                q.executeUpdate();

                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Division, division.getId(), division.getModifiedBy(), division.getModificationDate(), division.getNameInBangla(), "deleted");
                this.getCurrentSession().save(changeLog);
                return true;
            }
            else
            {
                return false;
            }

        }
        return false;
    }

    @Override
    public List<Division> getDivisionList()
    {
        Locale locale = LocaleContextHolder.getLocale();
        try
        {
            @SuppressWarnings("unchecked")
            String querySt = "from Division d where d.deleted = 0";
            String orderBy = locale.toString().equals("bn") ? " order by d.nameInBangla" : " order by d.nameInEnglish";
            querySt += orderBy;
            List<Division> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
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
     * @return
     */
    @Override
    public List<ItemObject> getDivisionIoList()
    {
        try
        {
            List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery("select new com.wfp.lmmis.utility.ItemObject(d.id, d.nameInBangla, d.nameInEnglish)  from Division d where d.deleted = 0 and d.active = 1").list();
            return itemObjects;
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }
}
