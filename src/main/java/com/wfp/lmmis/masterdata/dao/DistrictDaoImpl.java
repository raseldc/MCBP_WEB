/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.DistrictInfo;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;
import java.util.Locale;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public class DistrictDaoImpl implements DistrictDao
{

    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public District getDistrict(Integer id)
    {
        District district = (District) this.sessionFactory.getCurrentSession().get(District.class, id);
        Hibernate.initialize(district.getDivision());
        return district;
    }

    @Override
    public boolean save(District district)
    {
        String duplicateCheckQuery = "select count(*) from District where nameInEnglish = '" + district.getNameInEnglish() + "' or nameInBangla = '" + district.getNameInBangla() + "' or code = '" + district.getCode() + "'";
        System.out.println("query = " + duplicateCheckQuery);
        long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
        System.out.println("duplicate count = " + duplicateCount);

        if (duplicateCount == 0)
        {
            System.out.println("duplicate not found");
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.District, district.getId(), district.getCreatedBy(), district.getCreationDate(), null, district.getNameInBangla() + " status:" + district.isActive() + " code:" + district.getCode());
            this.getCurrentSession().save(changeLog);
            this.sessionFactory.getCurrentSession().save(district);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void edit(District editedDistrict)
    {
        District district = (District) this.sessionFactory.getCurrentSession().get(District.class, editedDistrict.getId());
        if (district != null)
        {
            if (!district.getNameInBangla().equals(editedDistrict.getNameInBangla()))
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.District, district.getId(), editedDistrict.getModifiedBy(), editedDistrict.getModificationDate(), district.getNameInBangla(), editedDistrict.getNameInBangla(), "name_in_bangla");
                this.getCurrentSession().save(changeLog);
            }
            if (!district.getNameInEnglish().equals(editedDistrict.getNameInEnglish()))
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.District, district.getId(), editedDistrict.getModifiedBy(), editedDistrict.getModificationDate(), district.getNameInEnglish(), editedDistrict.getNameInEnglish(), "name_in_english");
                this.getCurrentSession().save(changeLog);
            }
            if (!district.getCode().equals(editedDistrict.getCode()))
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.District, district.getId(), editedDistrict.getModifiedBy(), editedDistrict.getModificationDate(), district.getCode(), editedDistrict.getCode(), "code");
                this.getCurrentSession().save(changeLog);
            }
            if (district.isActive() != editedDistrict.isActive())
            {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.District, district.getId(), editedDistrict.getModifiedBy(), editedDistrict.getModificationDate(), String.valueOf(district.isActive()), String.valueOf(editedDistrict.isActive()), "active");
                this.getCurrentSession().save(changeLog);
            }

            district.setNameInBangla(editedDistrict.getNameInBangla());
            district.setNameInEnglish(editedDistrict.getNameInEnglish());
            district.setCode(editedDistrict.getCode());
            district.setActive(editedDistrict.isActive());
            district.setDivision(editedDistrict.getDivision());
            district.setModifiedBy(editedDistrict.getModifiedBy());
            district.setModificationDate(editedDistrict.getModificationDate());
        }
        this.sessionFactory.getCurrentSession().update(district);
    }

    @Override
    public boolean delete(District district)
    {
        if (district != null)
        {
            String querySt = "select u.id from Upazilla u where u.district.id = " + district.getId();
            System.out.println("query = " + querySt);
            Integer upazilaRefCount = this.sessionFactory.getCurrentSession().createQuery(querySt).list().size();
            if (upazilaRefCount == 0)
            {
//                Query q = this.sessionFactory.getCurrentSession().createQuery("update District set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id = " + district.getId());
//                q.setParameter("modifiedBy", district.getModifiedBy());
//                q.setParameter("modificationDate", district.getModificationDate());
//                q.executeUpdate();
                Query q = this.sessionFactory.getCurrentSession().createQuery("delete from District where id = " + district.getId());
                q.executeUpdate();

                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.District, district.getId(), district.getModifiedBy(), district.getModificationDate(), district.getNameInBangla(), "deleted");
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
    public List<DistrictInfo> getDistrictList(Integer divisionId)
    {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (divisionId == null)
        {
            querySt = "select new com.wfp.lmmis.masterdata.model.DistrictInfo(d.id, d.nameInBangla, d.nameInEnglish, d.code, d.division.nameInBangla, d.division.nameInEnglish, d.active)from District d where d.deleted = 0";
        }
        else
        {
            querySt = "from District d where d.division.id = " + divisionId + " and d.deleted = 0";
        }
        Locale locale = LocaleContextHolder.getLocale();
        String orderBy = locale.toString().equals("bn")?" order by d.nameInBangla":" order by d.nameInEnglish";
        querySt +=orderBy;
        try
        {
            List<DistrictInfo> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return list;
        }
        catch (Exception e)
        {
            System.out.println("ex1 = " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param divisionId
     * @return
     */
    @Override
    public List<ItemObject> getDistrictIoList(Integer divisionId)
    {
        @SuppressWarnings("unchecked")
        String querySt = null;
        Locale locale = LocaleContextHolder.getLocale();
        if (divisionId == null)
        {            
            querySt = "select new com.wfp.lmmis.utility.ItemObject(d.id, d.nameInBangla, d.nameInEnglish)  from District d where d.active = 1 and d.deleted = 0";
        }
        else
        {
            querySt = "select new com.wfp.lmmis.utility.ItemObject(d.id, d.nameInBangla, d.nameInEnglish)  from District d where d.division.id = " + divisionId + " and d.active = 1 and d.deleted = 0 ";
        }
        String orderBy = locale.toString().equals("bn")?" order by d.nameInBangla":" order by d.nameInEnglish";
        querySt +=orderBy;    
        List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).list();
        return itemObjects;

    }
}
