/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.types.UpazilaType;
import com.wfp.lmmis.utility.ItemObject;
import java.util.ArrayList;
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
public class UpazillaDaoImpl implements UpazillaDao {

    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Upazilla getUpazilla(Integer id) {
        Upazilla upazilla = (Upazilla) this.sessionFactory.getCurrentSession().get(Upazilla.class, id);
        Hibernate.initialize(upazilla.getDistrict());
        Hibernate.initialize(upazilla.getDistrict().getDivision());
        upazilla.setDivision(upazilla.getDistrict().getDivision());
        return upazilla;
    }

    @Override
    public boolean save(Upazilla upazilla) {
        String duplicateCheckQuery = "select count(id) from Upazilla where district.id = " + upazilla.getDistrict().getId() + " and (nameInEnglish = '" + upazilla.getNameInEnglish() + "' or nameInBangla = '" + upazilla.getNameInBangla() + "' or code = '" + upazilla.getCode() + "')";
        System.out.println("query = " + duplicateCheckQuery);
        long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
        System.out.println("duplicate count = " + duplicateCount);
        if (duplicateCount == 0) {
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.Upazilla, upazilla.getId(), upazilla.getCreatedBy(), upazilla.getCreationDate(), null, upazilla.getNameInBangla() + " status:" + upazilla.isActive() + " code:" + upazilla.getCode());
            this.getCurrentSession().save(changeLog);
            this.sessionFactory.getCurrentSession().save(upazilla);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void edit(Upazilla editedUpazilla) {
        Upazilla upazilla = (Upazilla) this.sessionFactory.getCurrentSession().get(Upazilla.class, editedUpazilla.getId());
        if (upazilla != null) {
            if (!upazilla.getNameInBangla().equals(editedUpazilla.getNameInBangla())) {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Upazilla, upazilla.getId(), editedUpazilla.getModifiedBy(), editedUpazilla.getModificationDate(), upazilla.getNameInBangla(), editedUpazilla.getNameInBangla(), "name_in_bangla");
                this.getCurrentSession().save(changeLog);
            }
            if (!upazilla.getNameInEnglish().equals(editedUpazilla.getNameInEnglish())) {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Upazilla, upazilla.getId(), editedUpazilla.getModifiedBy(), editedUpazilla.getModificationDate(), upazilla.getNameInEnglish(), editedUpazilla.getNameInEnglish(), "name_in_english");
                this.getCurrentSession().save(changeLog);
            }
            if (!upazilla.getCode().equals(editedUpazilla.getCode())) {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Upazilla, upazilla.getId(), editedUpazilla.getModifiedBy(), editedUpazilla.getModificationDate(), upazilla.getCode(), editedUpazilla.getCode(), "code");
                this.getCurrentSession().save(changeLog);
            }
            if (upazilla.isActive() != editedUpazilla.isActive()) {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Upazilla, upazilla.getId(), editedUpazilla.getModifiedBy(), editedUpazilla.getModificationDate(), String.valueOf(upazilla.isActive()), String.valueOf(editedUpazilla.isActive()), "active");
                this.getCurrentSession().save(changeLog);
            }

            upazilla.setNameInBangla(editedUpazilla.getNameInBangla());
            upazilla.setNameInEnglish(editedUpazilla.getNameInEnglish());
            upazilla.setCode(editedUpazilla.getCode());
            upazilla.setActive(editedUpazilla.isActive());
            if (editedUpazilla.getDistrict() != null) {
                upazilla.setDistrict(editedUpazilla.getDistrict());
            }
            upazilla.setModifiedBy(editedUpazilla.getModifiedBy());
            upazilla.setModificationDate(editedUpazilla.getModificationDate());
        }
        this.sessionFactory.getCurrentSession().update(upazilla);
    }

    @Override
    public boolean delete(Upazilla upazilla) {
        if (upazilla != null) {
            String querySt = "select u.id from Union u where u.upazilla.id = " + upazilla.getId();
            System.out.println("query = " + querySt);
            Integer unionRefCount = this.sessionFactory.getCurrentSession().createQuery(querySt).list().size();
            System.out.println("Num of union refers to upazila " + unionRefCount);
            if (unionRefCount == 0) {
//                Query q = this.sessionFactory.getCurrentSession().createQuery("update Upazilla set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id = " + upazilla.getId());
//                q.setParameter("modifiedBy", upazilla.getModifiedBy());
//                q.setParameter("modificationDate", upazilla.getModificationDate());
//                q.executeUpdate();
                Query q = this.sessionFactory.getCurrentSession().createQuery("delete from Upazilla where id = " + upazilla.getId());
                q.executeUpdate();

                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Upazilla, upazilla.getId(), upazilla.getModifiedBy(), upazilla.getModificationDate(), upazilla.getNameInBangla(), "deleted");
                this.getCurrentSession().save(changeLog);
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public List<Upazilla> getUpazillaList(Integer districtId, String schemeName) {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (schemeName.equals("MA")) {
            if (districtId == null) {
                querySt = "from Upazilla u where u.deleted = 0 and u.upazilaType=0";
            } else {
                querySt = "from Upazilla u where u.district.id = " + districtId + " and u.deleted = 0 and u.upazilaType=0";
            }
        } else {
            if (districtId == null) {
                querySt = "from Upazilla u where u.deleted = 0";
            } else {
                querySt = "from Upazilla u where u.district.id = " + districtId + " and u.deleted = 0";
            }
        }
        System.out.println("query stss = " + querySt);
        List<Upazilla> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        System.out.println("up list size = " + list.size());

        return list;
    }

    @Override
    public List<ItemObject> getUpazillaIoList(Integer districtId, String upazilaType) {
        @SuppressWarnings("unchecked")
        String querySt = null;
        Locale locale = LocaleContextHolder.getLocale();
        if (upazilaType.equals("MA")) {
            if (districtId == null) {
                querySt = "select new com.wfp.lmmis.utility.ItemObject(u.id, u.nameInBangla, u.nameInEnglish)  from Upazilla u where u.active = 1 and u.deleted = 0 and u.upazilaType=0";
            } else {
                querySt = "select new com.wfp.lmmis.utility.ItemObject(u.id, u.nameInBangla, u.nameInEnglish)  from Upazilla u where u.district.id = " + districtId + " and u.active = 1 and u.deleted = 0 and u.upazilaType=0";
            }
        } else if (upazilaType.equals("LMA")) {
            if (districtId == null) {
                querySt = "select new com.wfp.lmmis.utility.ItemObject(u.id, u.nameInBangla, u.nameInEnglish)  from Upazilla u where u.active = 1 and u.deleted = 0";
            } else {
                querySt = "select new com.wfp.lmmis.utility.ItemObject(u.id, u.nameInBangla, u.nameInEnglish)  from Upazilla u where u.district.id = " + districtId + " and u.active = 1 and u.deleted = 0";
            }
        } else if (upazilaType.equals("CITY_CORP_DIST")) {
            if (districtId != null) {
                querySt = "select new com.wfp.lmmis.utility.ItemObject(u.id, u.nameInBangla, u.nameInEnglish)  from Upazilla u where u.district.id = " + districtId + " and u.upazilaType = 1 and u.active = 1 and u.deleted = 0";
            }
        }
        String orderBy = locale.toString().equals("bn") ? " order by u.nameInBangla" : " order by u.nameInEnglish";
        querySt += orderBy;
        try {
            List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).list();
            System.out.println("itemObjects.size() = " + itemObjects.size());
            return itemObjects;
        } catch (Exception e) {
            System.out.println("ex = " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object> getUpazilaSearchList(String divisionId, boolean isSearchByDivisionId,
            String districtId, boolean isSearchByDistrictId, int offset, int numofRecords) {
        try {
            List<Object> result = new ArrayList<Object>();
            String querySt = "";
            String mainQuerySt = "select new com.wfp.lmmis.masterdata.model.UpazilaInfo(o.id, o.nameInBangla, o.nameInEnglish, o.code, o.district.nameInBangla, o.district.nameInEnglish, o.active) from Upazilla o where 0=0 and o.deleted=0 and o.upazilaType=0";
            String countQuerySt = "select count(distinct(o.Id)) from Upazilla o where 0=0";

            if (isSearchByDivisionId) {
                querySt += " and o.district.division.id = " + divisionId;
            }
            if (isSearchByDistrictId) {
                querySt += " and o.district.id = " + districtId;
            }
            // List<Upazilla> up = sessionFactory.getCurrentSession().createQuery("SELECT a FROM Upazilla a WHERE a.upazilaType = 0 AND a.district.id = 33").list();
            List<Upazilla> list = (List<Upazilla>) sessionFactory.getCurrentSession().
                    createQuery(mainQuerySt + querySt).
                    setFirstResult(offset).setMaxResults(numofRecords).list();
            Integer count = sessionFactory.getCurrentSession().
                    createQuery(mainQuerySt + querySt).list().size();
            result.add(list);
            result.add(count);
            result.add(count);// have to alter this
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
