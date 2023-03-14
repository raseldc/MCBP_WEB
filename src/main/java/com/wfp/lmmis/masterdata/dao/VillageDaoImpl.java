/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.Village;
import com.wfp.lmmis.masterdata.model.VillageInfo;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.utility.ItemObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
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
public class VillageDaoImpl implements VillageDao {

    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Village getVillage(Integer id) {
        Village village = (Village) this.sessionFactory.getCurrentSession().get(Village.class, id);
        Hibernate.initialize(village.getUnion());
        Hibernate.initialize(village.getUpazila());
        Hibernate.initialize(village.getUpazila().getDistrict());
        Hibernate.initialize(village.getUpazila().getDistrict().getDivision());

        village.setDivision(village.getUpazila().getDistrict().getDivision());
        village.setDistrict(village.getUpazila().getDistrict());

        return village;
    }

    @Override
    public boolean save(Village village) {
        try {
            String duplicateCheckQuery = "select count(o.id) from Village o where o.union.id = " + village.getUnion().getId() + " and (o.nameInEnglish = '" + village.getNameInEnglish() + "' or o.nameInBangla = '" + village.getNameInBangla() + "' or o.code = '" + village.getCode() + "')";
            long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
            if (duplicateCount == 0) {
                Integer villageId = (Integer) this.sessionFactory.getCurrentSession().save(village);
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.Village, villageId, village.getCreatedBy(), village.getCreationDate(), null, village.getNameInBangla() + " status:" + village.isActive() + " code:" + village.getCode());
                this.getCurrentSession().save(changeLog);
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            return false;
        }
    }

    @Override
    public boolean edit(Village editedVillage) {
        Village village = (Village) this.sessionFactory.getCurrentSession().get(Village.class, editedVillage.getId());

        if (village != null) {

            String duplicateCheckQuery = "select count(o.id) from Village o where o.union.id = "
                    + village.getUnion().getId() + " and o.id != " + village.getId() + " and o.wardNo = " + editedVillage.getWardNo()
                    + " and (o.nameInEnglish = '" + editedVillage.getNameInEnglish()
                    + "' or o.nameInBangla = '" + editedVillage.getNameInBangla()
                    + "' or o.code = '" + editedVillage.getCode() + "')";
            long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
            if (duplicateCount > 0) {
                return false;
            }

            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Village, village.getId(), editedVillage.getModifiedBy(), editedVillage.getModificationDate(), village.getNameInBangla() + " status:" + village.isActive() + " code:" + village.getCode(), editedVillage.getNameInBangla() + " status:" + editedVillage.isActive() + " code:" + editedVillage.getCode());
            this.getCurrentSession().save(changeLog);

            village.setNameInBangla(editedVillage.getNameInBangla());
            village.setNameInEnglish(editedVillage.getNameInEnglish());
            village.setCode(editedVillage.getCode());
            if (editedVillage.getUpazila() != null) {
                village.setUpazila(editedVillage.getUpazila());
            }
            if (editedVillage.getUnion() != null) {
                village.setUnion(editedVillage.getUnion());
            }

            village.setActive(editedVillage.isActive());
            if (editedVillage.getWardNo() != null) {
                village.setWardNo(editedVillage.getWardNo());
            }

            village.setModifiedBy(editedVillage.getModifiedBy());
            village.setModificationDate(editedVillage.getModificationDate());
        }
        this.sessionFactory.getCurrentSession().update(village);
        return true;
    }

    @Override
    public void delete(Village village) {
        if (village != null) {
            System.out.println("Village not null id is = " + village.getId() + " name = " + village.getNameInEnglish());
            Query q = this.sessionFactory.getCurrentSession().createQuery("update Village set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id = " + village.getId());
            q.setParameter("modifiedBy", village.getModifiedBy());
            q.setParameter("modificationDate", village.getModificationDate());
            q.executeUpdate();

            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Village, village.getId(), village.getModifiedBy(), village.getModificationDate(), village.getNameInBangla(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
    }

    @Override
    public List<Village> getVillageList(Integer bankId) {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (bankId == null) {
            querySt = "from Village o where o.deleted=0";
        } else {
            querySt = "from Village o where o.bank.id = " + bankId + " and o.deleted=0";
        }

        List<Village> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        return list;
    }

    @Override
    public List<ItemObject> getVillageIoList(Integer wardNo, Integer unionId) {
        @SuppressWarnings("unchecked")
        String querySt = null;

        querySt = "select NEW com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish)  from Village o";

        querySt += " WHERE o.active=1";

        if (wardNo != null) {
            querySt += " AND o.wardNo = " + wardNo;
        }

        if (unionId != null) {
            querySt += " AND o.union.id = " + unionId;
        }
        try {
            List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return itemObjects;
        } catch (Exception e) {
            System.out.println("ex = " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object> getVillageSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize) {
        @SuppressWarnings("unchecked")
        List<Object> result = new ArrayList<Object>();
        String querySt;
        Locale locale = LocaleContextHolder.getLocale();
        if ("bn".equals(locale.getLanguage())) {
            querySt = "select "
                    + "new com.wfp.lmmis.masterdata.model.VillageInfo(o.id, o.nameInBangla, o.nameInEnglish, o.code, o.wardNo, o.union.nameInBangla, o.upazila.nameInBangla, o.upazila.district.nameInBangla, o.active) "
                    + "from Village o where 0=0";
        } else {
            querySt = "select "
                    + "new com.wfp.lmmis.masterdata.model.VillageInfo(o.id, o.nameInBangla, o.nameInEnglish, o.code, o.wardNo, o.union.nameInEnglish, o.upazila.nameInEnglish, o.upazila.district.nameInEnglish, o.active) "
                    + "from Village o where 0=0";
        }

        if (parameter.get("divisionId") != null && !parameter.get("divisionId").equals("")) {
            querySt += " and o.upazila.district.division.id = " + parameter.get("divisionId");
        }
        if (parameter.get("districtId") != null && !parameter.get("districtId").equals("")) {
            querySt += " and o.upazila.district.id = " + parameter.get("districtId");
        }
        if (parameter.get("unionId") != null && !parameter.get("unionId").equals("")) {
            querySt += " and o.union.id = " + parameter.get("unionId");
        }
        if (parameter.get("upazilaId") != null && !parameter.get("upazilaId").equals("")) {
            querySt += " and o.upazila.id = " + parameter.get("upazilaId");
        }

        if (parameter.get("villageName") != null && !parameter.get("villageName").equals("")) {
            querySt += " and (o.nameInBangla LIKE '%" + parameter.get("villageName") + "%' or o.nameInEnglish LIKE '%" + parameter.get("villageName") + "%' )";
        }

        try {
            List<VillageInfo> list = (List<VillageInfo>) sessionFactory.getCurrentSession().createQuery(querySt).setFirstResult(beginIndex).setMaxResults(pageSize).list();
            Integer count = sessionFactory.getCurrentSession().createQuery(querySt).list().size();
            System.out.println("count = " + count);
            result.add(list);
            result.add(count);
            result.add(count);
            return result;
        } catch (Exception e) {
            System.out.println("ex = " + e.getMessage());
        }
        return null;
    }

}
