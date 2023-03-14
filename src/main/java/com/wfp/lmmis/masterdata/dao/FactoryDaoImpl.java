/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.types.FactoryType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public class FactoryDaoImpl implements FactoryDao
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Factory getFactory(Integer id)
    {
        Factory factory = (Factory) this.sessionFactory.getCurrentSession().get(Factory.class, id);
        try
        {
            if (factory.getDivision() != null)
            {
                Hibernate.initialize(factory.getDivision());
            }
            if (factory.getDistrict() != null)
            {
                Hibernate.initialize(factory.getDistrict());
            }
            if (factory.getUpazila() != null)
            {
                Hibernate.initialize(factory.getUpazila());
            }
        }
        catch (ObjectNotFoundException onfe)
        {
        }
        return factory;
    }

    @Override
    public boolean save(Factory factory)
    {
        String duplicateCheckQuery = "select count(id) from Factory where nameInEnglish = '" + factory.getNameInEnglish() + "' or nameInBangla = '" + factory.getNameInBangla() + "' or regNo = '" + factory.getRegNo() + "'";
        System.out.println("query = " + duplicateCheckQuery);
        long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
        System.out.println("duplicate count = " + duplicateCount);
        if (duplicateCount == 0)
        {
            System.out.println("duplicate not found");
            try
            {
                this.sessionFactory.getCurrentSession().save(factory);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.Factory, factory.getId(), factory.getCreatedBy(), factory.getCreationDate(), null, factory.getNameInBangla() + " status:" + factory.isActive() + " code:" + factory.getRegNo());
            this.getCurrentSession().save(changeLog);
            System.out.println("after change log");
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void edit(Factory editedFactory)
    {
        Factory factory = (Factory) this.sessionFactory.getCurrentSession().get(Factory.class, editedFactory.getId());
        if (factory != null)
        {
            if (!factory.getNameInBangla().equals(editedFactory.getNameInBangla()))
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Factory, factory.getId(), editedFactory.getModifiedBy(), editedFactory.getModificationDate(), factory.getNameInBangla(), editedFactory.getNameInBangla(), "name_in_bangla");
                this.getCurrentSession().save(changeLog);
            }
            if (!factory.getNameInEnglish().equals(editedFactory.getNameInEnglish()))
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Factory, factory.getId(), editedFactory.getModifiedBy(), editedFactory.getModificationDate(), factory.getNameInEnglish(), editedFactory.getNameInEnglish(), "name_in_english");
                this.getCurrentSession().save(changeLog);
            }
            if (!factory.getRegNo().equals(editedFactory.getRegNo()))
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Factory, factory.getId(), editedFactory.getModifiedBy(), editedFactory.getModificationDate(), factory.getRegNo(), editedFactory.getRegNo(), "code");
                this.getCurrentSession().save(changeLog);
            }
            if (factory.isActive() != editedFactory.isActive())
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Factory, factory.getId(), editedFactory.getModifiedBy(), editedFactory.getModificationDate(), String.valueOf(factory.isActive()), String.valueOf(editedFactory.isActive()), "active");
                this.getCurrentSession().save(changeLog);
            }

            factory.setNameInBangla(editedFactory.getNameInBangla());
            factory.setNameInEnglish(editedFactory.getNameInEnglish());
            factory.setAddress(editedFactory.getAddress());
            factory.setRegNo(editedFactory.getRegNo());
            factory.setActive(editedFactory.isActive());
            factory.setModifiedBy(editedFactory.getModifiedBy());
            factory.setModificationDate(editedFactory.getModificationDate());
            factory.setDivision(editedFactory.getDivision());
            factory.setDistrict(editedFactory.getDistrict());
            factory.setUpazila(editedFactory.getUpazila());
        }
        this.sessionFactory.getCurrentSession().update(factory);
    }

    @Override
    public boolean delete(Factory factory)
    {
        try
        {
            Query q = this.sessionFactory.getCurrentSession().createQuery("delete from Factory where id = " + factory.getId());
            q.executeUpdate();
        }
        catch (ConstraintViolationException e)
        {
            e.printStackTrace();
            System.out.println("error = " + e.getMessage());
            return false;
        }
        ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Factory, factory.getId(), factory.getModifiedBy(), factory.getModificationDate(), factory.getNameInBangla(), "deleted");
        this.getCurrentSession().save(changeLog);
        return true;
    }

    @Override
    public List<Factory> getFactoryList(FactoryType factoryType)
    {
        List<Factory> list = null;
        try
        {
            Locale locale = LocaleContextHolder.getLocale();
            String orderBy = locale.toString().equals("bn") ? " order by f.nameInBangla" : " order by f.nameInEnglish";
            if (factoryType == null)
            {
                list = sessionFactory.getCurrentSession().createQuery("from Factory f where f.deleted = 0" + orderBy).list();
            }
            else
            {
                list = sessionFactory.getCurrentSession().createQuery("from Factory f where f.deleted = 0 and f.type=:type" + orderBy).setParameter("type", factoryType).list();
            }
            return list;
        }
        catch (Exception e)
        {
            System.out.println("exc=" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Object> getBGMEASearchListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        @SuppressWarnings("unchecked")
        List<Object> result = new ArrayList<Object>();
        Integer divisionId = (Integer) parameter.get("divisionId");
        Integer districtId = (Integer) parameter.get("districtId");
        Integer upazilaId = (Integer) parameter.get("upazilaId");

        String querySt = "select o"
                //                + "new com.wfp.lmmis.masterdata.model.Factory(o.id, o.nameInBangla, o.nameInEnglish, o.type, o.address, o.regNo, o.active) "
                + " from Factory o where o.type=0";
        if (parameter.get("nameEn") != null)
        {
            querySt += " and o.nameInEnglish like '%" + parameter.get("nameEn") + "%'";
        }
        if (parameter.get("regNo") != null)
        {
            querySt += " and o.regNo = " + parameter.get("regNo");
        }
        if (divisionId != null)
        {
            querySt += " and o.division.id=" + divisionId;
        }
        if (districtId != null)
        {
            querySt += " and o.district.id=" + districtId;
        }
        if (upazilaId != null)
        {
            querySt += " and o.upazila.id=" + upazilaId;
        }
        System.out.println("query " + querySt);
        try
        {
            Query query = sessionFactory.getCurrentSession().
                    createQuery(querySt);
            List<Factory> list = (List<Factory>) query.setFirstResult(beginIndex).setMaxResults(pageSize).list();
            Integer count = sessionFactory.getCurrentSession().
                    createQuery(querySt).list().size();
            result.add(list);
            result.add(count);
            result.add(count);
            return result;
        }
        catch (Exception e)
        {
            System.out.println("ex = " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object> getBKMEASearchListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        @SuppressWarnings("unchecked")
        List<Object> result = new ArrayList<Object>();
        Integer divisionId = (Integer) parameter.get("divisionId");
        Integer districtId = (Integer) parameter.get("districtId");
        Integer upazilaId = (Integer) parameter.get("upazilaId");
        String querySt = "select o"
                //                + "new com.wfp.lmmis.masterdata.model.Factory(o.id, o.nameInBangla, o.nameInEnglish, o.type, o.address, o.regNo, o.active) "
                + " from Factory o where o.type=1";
        if (parameter.get("nameEn") != null)
        {
            querySt += " and o.nameInEnglish like '%" + parameter.get("nameEn") + "%'";
        }
        if (parameter.get("regNo") != null)
        {
            querySt += " and o.regNo = " + parameter.get("regNo");
        }
        if (divisionId != null)
        {
            querySt += " and o.division.id=" + divisionId;
        }
        if (districtId != null)
        {
            querySt += " and o.district.id=" + districtId;
        }
        if (upazilaId != null)
        {
            querySt += " and o.upazila.id=" + upazilaId;
        }
        System.out.println("query " + querySt);
        try
        {
            Query query = sessionFactory.getCurrentSession().
                    createQuery(querySt);
            List<Factory> list = (List<Factory>) query.setFirstResult(beginIndex).setMaxResults(pageSize).list();
            Integer count = sessionFactory.getCurrentSession().
                    createQuery(querySt).list().size();
            result.add(list);
            result.add(count);
            result.add(count);
            return result;
        }
        catch (Exception e)
        {
            System.out.println("ex = " + e.getMessage());
        }
        return null;
    }
}
