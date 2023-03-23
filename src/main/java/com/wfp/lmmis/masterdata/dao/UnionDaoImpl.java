/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.UnionInfo;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.types.CoverageArea;
import com.wfp.lmmis.utility.ItemObject;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public class UnionDaoImpl implements UnionDao {

    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Union getUnion(Integer id) {
        Union union = (Union) this.sessionFactory.getCurrentSession().get(Union.class, id);
        Hibernate.initialize(union.getUpazilla());
        Hibernate.initialize(union.getUpazilla().getDistrict());
        Hibernate.initialize(union.getUpazilla().getDistrict().getDivision());

        //  District district = getDistrictByUpazila(union.getUpazilla().getId());
        union.setDistrict(union.getUpazilla().getDistrict());
        union.setDivision(union.getUpazilla().getDistrict().getDivision());
        return union;
    }

    @Override
    public boolean save(Union union) {

        ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.Union, union.getId(), union.getCreatedBy(), union.getCreationDate(), null, union.getNameInBangla() + " status:" + union.isActive() + " code:" + union.getCode());
        this.getCurrentSession().save(changeLog);
        this.sessionFactory.getCurrentSession().save(union);
        return true;

    }

    @Override
    public void edit(Union editedUnion) {
        Union union = (Union) this.sessionFactory.getCurrentSession().get(Union.class, editedUnion.getId());
        if (union != null) {
            if (!union.getNameInBangla().equals(editedUnion.getNameInBangla())) {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Union, union.getId(), editedUnion.getModifiedBy(), editedUnion.getModificationDate(), union.getNameInBangla(), editedUnion.getNameInBangla(), "name_in_bangla");
                this.getCurrentSession().save(changeLog);
            }
            if (!union.getNameInEnglish().equals(editedUnion.getNameInEnglish())) {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Union, union.getId(), editedUnion.getModifiedBy(), editedUnion.getModificationDate(), union.getNameInEnglish(), editedUnion.getNameInEnglish(), "name_in_english");
                this.getCurrentSession().save(changeLog);
            }
            if (!union.getCode().equals(editedUnion.getCode())) {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Union, union.getId(), editedUnion.getModifiedBy(), editedUnion.getModificationDate(), union.getCode(), editedUnion.getCode(), "code");
                this.getCurrentSession().save(changeLog);
            }
            if (union.isActive() != editedUnion.isActive()) {
                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Union, union.getId(), editedUnion.getModifiedBy(), editedUnion.getModificationDate(), String.valueOf(union.isActive()), String.valueOf(editedUnion.isActive()), "active");
                this.getCurrentSession().save(changeLog);
            }

            if (editedUnion.getUpazilla() != null) {
                union.setUpazilla(editedUnion.getUpazilla());
            }
            union.setNameInBangla(editedUnion.getNameInBangla());
            union.setNameInEnglish(editedUnion.getNameInEnglish());

            union.setCode(editedUnion.getCode());
            union.setActive(editedUnion.isActive());
            union.setParentType(editedUnion.getParentType());
            union.setCoverageArea(editedUnion.getCoverageArea());
            union.setCoverageAreaClass(editedUnion.getCoverageAreaClass());
            union.setModifiedBy(editedUnion.getModifiedBy());
            union.setModificationDate(editedUnion.getModificationDate());
        }
        this.sessionFactory.getCurrentSession().update(union);
    }

    @Override
    public List<Union> getUnionList(Integer upazillaId) {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (upazillaId == null) {
            querySt = "from Union u where u.deleted = 0";
        } else {
            querySt = "from Union u where u.upazilla.id = " + upazillaId + " and u.deleted = 0" + " order by u.upazilla.id, u.nameInEnglish";
        }
        List<Union> list = null;
        try {
            list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<ItemObject> getUnionIoList(Integer upazillaId) {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (upazillaId == null) {
            querySt = "select new com.wfp.lmmis.utility.ItemObject(u.id, u.nameInBangla, u.nameInEnglish)  from Union u where u.active = 1 and u.deleted = 0 and u.coverageArea=:ca";
        } else {
            querySt = "select new com.wfp.lmmis.utility.ItemObject(u.id, u.nameInBangla, u.nameInEnglish)  from Union u where u.upazilla.id = " + upazillaId + " and u.active = 1 and u.deleted = 0 and u.coverageArea=:ca" + " order by u.nameInEnglish";
        }

        List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).setParameter("ca", CoverageArea.UNION).list();
        return itemObjects;

    }

    @Override
    public List<ItemObject> getMunicipalOrCityCorporation(Integer upazillaId, CoverageArea coverageArea) {
        @SuppressWarnings("unchecked")
        String querySt = "select new com.wfp.lmmis.utility.ItemObject(u.id, u.nameInBangla, u.nameInEnglish) from Union u where 0=0 and u.active = true ";

        if (upazillaId != null) {
            querySt += " and u.upazilla.id = " + upazillaId;
        }
        if (coverageArea != null) {
            querySt += " and u.coverageArea = " + coverageArea.ordinal();
        }

        querySt += " and u.deleted = 0 order by u.nameInEnglish";
        List<ItemObject> list = null;
        try {
//            list = sessionFactory.getCurrentSession().createQuery(querySt).setParameter("coverageArea", coverageArea).list();
            list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<ItemObject> getUnionAndMunicipal(Integer upazillaId) {
        @SuppressWarnings("unchecked")
        String querySt = "select new com.wfp.lmmis.utility.ItemObject(u.id, u.nameInBangla, u.nameInEnglish) from Union u where 0=0";

        if (upazillaId != null) {
            querySt += " and u.upazilla.id = " + upazillaId;
        }
        querySt += " and u.coverageArea in (0,1)";
        querySt += " and u.deleted = 0 order by u.nameInEnglish";
        List<ItemObject> list = null;
        try {
//            list = sessionFactory.getCurrentSession().createQuery(querySt).setParameter("coverageArea", coverageArea).list();
            list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Object> getUnionSearchList(String divisionId, boolean isSearchByDivisionId,
            String districtId, boolean isSearchByDistrictId, String upazilaId, boolean isSearchByUpazilaId, String unionName, boolean isSearchByName, int offset, int numofRecords) {
        try {
            List<Object> result = new ArrayList<Object>();
            String querySt = "";
            String mainQuerySt = "select new com.wfp.lmmis.masterdata.model.UnionInfo(o.id, o.nameInBangla, o.nameInEnglish, o.code, o.upazilla.nameInBangla, o.upazilla.nameInEnglish, o.active) from Union o where 0=0 and o.deleted=0";
            if (isSearchByDivisionId) {
                querySt += " and o.upazilla.district.division.id = " + divisionId;
            }
            if (isSearchByDistrictId) {
                querySt += " and o.upazilla.district.id = " + districtId;
            }
            if (isSearchByUpazilaId) {
                querySt += " and o.upazilla.id = " + upazilaId;
            }
            if (isSearchByUpazilaId) {
                querySt += " and o.upazilla.id = " + upazilaId;
            }
            if (isSearchByName) {
                querySt += " and (o.nameInBangla LIKE '%" + unionName + "%' or o.nameInEnglish LIKE '%" + unionName + "%' )";

            }
            querySt += " and o.coverageArea = :coverageArea";
            List<UnionInfo> list = (List<UnionInfo>) sessionFactory.getCurrentSession().
                    createQuery(mainQuerySt + querySt).setParameter("coverageArea", CoverageArea.UNION).
                    setFirstResult(offset).setMaxResults(numofRecords).list();
            Integer count = sessionFactory.getCurrentSession().
                    createQuery(mainQuerySt + querySt).setParameter("coverageArea", CoverageArea.UNION).list().size();
            result.add(list);
            result.add(count);
            result.add(count);// have to alter this
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Union union) {
        if (union != null) {
            try {
                Query q = this.sessionFactory.getCurrentSession().createQuery("delete from Union where id = " + union.getId());
                q.executeUpdate();
            } catch (ConstraintViolationException e) {
                e.printStackTrace();
                System.out.println("error = " + e.getMessage());
                return false;
            }

            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Union, union.getId(), union.getModifiedBy(), union.getModificationDate(), union.getNameInBangla(), "deleted");
            this.getCurrentSession().save(changeLog);
            return true;

        }

        return false;
    }

    /**
     *
     * @param upazilaId
     * @return
     */
    public District getDistrictByUpazila(Integer upazilaId) {
        String querySt = "select o.district from Upazilla o where o.id = " + upazilaId;
        District district = (District) sessionFactory.getCurrentSession().createQuery(querySt).uniqueResult();
        System.out.println("district from upazila is = " + district.getNameInEnglish());
        return district;
    }

    @Override
    public List<Object> getMunicipalOrCityCorporationSearchList(String divisionId, boolean isSearchByDivisionId,
            String districtId, boolean isSearchByDistrictId, String upazilaId, boolean isSearchByUpazilaId, CoverageArea coverageArea, int offset, int numofRecords) {
        try {
            System.out.println("Division id= " + divisionId + " district id = " + districtId + " upazila id = " + upazilaId + " coverage aree = " + coverageArea);
            List<Object> result = new ArrayList<Object>();
            String querySt = "";
            String mainQuerySt = "select new com.wfp.lmmis.masterdata.model.UnionInfo(o.id, o.nameInBangla, o.nameInEnglish, o.code, o.upazilla.nameInBangla, o.upazilla.nameInEnglish, o.active) from Union o where 0=0 and o.deleted=0";
            String countQuerySt = "select count(distinct(o.Id)) from Union o where 0=0";
            if (isSearchByDivisionId) {
                querySt += " and o.upazilla.district.division.id = " + divisionId;
            }
            if (isSearchByDistrictId) {
                querySt += " and o.upazilla.district.id = " + districtId;
            }
            if (isSearchByUpazilaId) {
                querySt += " and o.upazilla.id = " + upazilaId;
            }

            List<Union> list = null;
            Integer count = 0;
            if (coverageArea != null) {
                querySt += " and o.coverageArea = :coverageArea";
                list = (List<Union>) sessionFactory.getCurrentSession().
                        createQuery(mainQuerySt + querySt).setParameter("coverageArea", coverageArea).
                        setFirstResult(offset).setMaxResults(numofRecords).list();
                count = sessionFactory.getCurrentSession().
                        createQuery(mainQuerySt + querySt).setParameter("coverageArea", coverageArea).list().size();
            } else {
                querySt += " and o.coverageArea != :unionCoverageArea";
                list = (List<Union>) sessionFactory.getCurrentSession().
                        createQuery(mainQuerySt + querySt).setParameter("unionCoverageArea", CoverageArea.UNION).
                        setFirstResult(offset).setMaxResults(numofRecords).list();
                count = sessionFactory.getCurrentSession().
                        createQuery(mainQuerySt + querySt).setParameter("unionCoverageArea", CoverageArea.UNION).list().size();
            }
            System.out.println("query is " + querySt + " count is " + count);
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
