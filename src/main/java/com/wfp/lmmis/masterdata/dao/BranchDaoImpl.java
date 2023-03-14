/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.Branch;
import com.wfp.lmmis.masterdata.model.BranchInfo;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ItemObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public class BranchDaoImpl implements BranchDao {

    @Autowired
    SessionFactory sessionFactory;

    /**
     *
     * @return
     */
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Branch getBranch(Integer id) {
        Branch branch = (Branch) this.sessionFactory.getCurrentSession().get(Branch.class, id);
        Hibernate.initialize(branch.getDistrict());
        Hibernate.initialize(branch.getBank());
        return branch;
    }

    @Override
    public boolean save(Branch branch) {
        String duplicateCheckQuery = "select count(id) from Branch where bank.id = '" + branch.getBank().getId() + "' and district.id = '" + branch.getDistrict().getId() + "' and (nameInEnglish = '" + branch.getNameInEnglish() + "' or nameInBangla = '" + branch.getNameInBangla() + "' or code = '" + branch.getCode() + "')";
        System.out.println("query = " + duplicateCheckQuery);
        long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
        System.out.println("duplicate count = " + duplicateCount);
        if (duplicateCount == 0) {
            Integer branchId = (Integer) this.sessionFactory.getCurrentSession().save(branch);
            System.out.println("branch is " + branchId);
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.Branch, branchId, branch.getCreatedBy(), branch.getCreationDate(), null, branch.getNameInBangla() + " status:" + branch.isActive() + " code:" + branch.getCode());
            this.getCurrentSession().save(changeLog);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void edit(Branch editedBranch) {
        Branch branch = (Branch) this.sessionFactory.getCurrentSession().get(Branch.class, editedBranch.getId());
        if (branch != null) {
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Branch, branch.getId(), editedBranch.getModifiedBy(), editedBranch.getModificationDate(), branch.getNameInBangla() + " status:" + branch.isActive() + " code:" + branch.getCode(), editedBranch.getNameInBangla() + " status:" + editedBranch.isActive() + " code:" + editedBranch.getCode());
            this.getCurrentSession().save(changeLog);

            branch.setNameInBangla(editedBranch.getNameInBangla());
            branch.setNameInEnglish(editedBranch.getNameInEnglish());
            branch.setCode(editedBranch.getCode());
            branch.setDistrict(editedBranch.getDistrict());
            branch.setAddress(editedBranch.getAddress());
            branch.setActive(editedBranch.isActive());
            branch.setBank(editedBranch.getBank());
            branch.setRoutingNumber(editedBranch.getRoutingNumber());
            branch.setModifiedBy(editedBranch.getModifiedBy());
            branch.setModificationDate(editedBranch.getModificationDate());
            branch.setAccountNoLength(editedBranch.getAccountNoLength());
        }
        this.sessionFactory.getCurrentSession().update(branch);
    }

    @Override
    public void delete(Branch branch) {
        if (branch != null) {
            System.out.println("Branch not null id is = " + branch.getId() + " name = " + branch.getNameInEnglish());
            Query q = this.sessionFactory.getCurrentSession().createQuery("update Branch set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id = " + branch.getId());
            q.setParameter("modifiedBy", branch.getModifiedBy());
            q.setParameter("modificationDate", branch.getModificationDate());
            q.executeUpdate();

            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Branch, branch.getId(), branch.getModifiedBy(), branch.getModificationDate(), branch.getNameInBangla(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
    }

    @Override
    public List<Branch> getBranchList(Integer bankId) {
        try {
            @SuppressWarnings("unchecked")
            String querySt = null;
            if (bankId == null) {
                querySt = "from Branch o where o.deleted=0";
            } else {
                querySt = "from Branch o where o.bank.id = " + bankId + " and o.deleted=0";
            }

            List<Branch> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ItemObject> getBranchIoList(Integer bankId, Integer districtId) {
        @SuppressWarnings("unchecked")
        String querySt = null;

        querySt = "select NEW com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish,o.accountNoLength)  from Branch o";

        querySt += " WHERE 0 = 0 and o.active=1 and o.deleted=0";

        if (bankId != null) {
            querySt += " AND o.bank.id = " + bankId;
        }

        if (districtId != null) {
            querySt += " AND o.district.id = " + districtId;
        }

//        if (bankId == null)
//        {
//            querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish)  from Branch o join Applicant a on o.id = a.permanentDistrict.id where o.active=1 and o.deleted=0";
//        }
//        else
//        {
//            querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish)  from Branch o join Applicant a on o.id = a.permanentDistrict.id where o.bank.id = " + bankId + " and o.active=1 and o.deleted=0";
//        }
        System.out.println("querySt = " + querySt);

        try {
            List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return itemObjects;
        } catch (Exception e) {
            System.out.println("ex = " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object> getBranchSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize) {
        @SuppressWarnings("unchecked")
        List<Object> result = new ArrayList<Object>();

        String querySt = "select "
                + "new com.wfp.lmmis.masterdata.model.BranchInfo(o.id, o.nameInBangla, o.nameInEnglish, o.code, o.address, o.routingNumber, o.bank.nameInBangla, o.bank.nameInEnglish, o.district.nameInBangla, o.district.nameInEnglish, o.active) "
                + "from Branch o where 0=0";
        if (parameter.get("bankId") != null) {
            querySt += " and o.bank.id = " + parameter.get("bankId");
        }
        if (parameter.get("districtId") != null) {
            querySt += " and o.district.id = " + parameter.get("districtId");
        }
        if (parameter.get("routingNumber") != null) {
            querySt += " and o.routingNumber = " + CommonUtility.getNumberInEnglish(parameter.get("routingNumber").toString());
        }
        try {
            List<BranchInfo> list = (List<BranchInfo>) sessionFactory.getCurrentSession().
                    createQuery(querySt).setFirstResult(beginIndex).setMaxResults(pageSize).list();
            Integer count = sessionFactory.getCurrentSession().
                    createQuery(querySt).list().size();
            result.add(list);
            result.add(count);
            result.add(count);
            return result;
        } catch (Exception e) {
            System.out.println("ex = " + e.getMessage());
        }
        return null;
    }
//    @Override
//    public List<Object> getBranchSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize)
//    {
//        @SuppressWarnings("unchecked")
//        List<Object> result = new ArrayList<Object>();
//        System.out.println("bank = " + parameter.get("bankId"));
////        Integer bankId = (Integer) parameter.get("bankId");
////        Integer districtId = (Integer) parameter.get("districtId");
//
//        String querySt = "select new com.wfp.lmmis.masterdata.model.BranchInfo(o. ) from Branch o where 0=0";
//        if (parameter.get("bankId") != null)
//        {
//            querySt += " and o.bank.id = " + parameter.get("bankId");
//        }
//        if (parameter.get("districtId") != null)
//        {
//            querySt += " and o.district.id = " + parameter.get("districtId");
//        }
//        if (parameter.get("routingNumber") != null)
//        {
//            querySt += " and o.routingNumber = " + CommonUtility.getNumberInEnglish(parameter.get("routingNumber").toString());
//        }
//        System.out.println("query st = " + querySt);
//        try
//        {
//            List<Branch> list = (List<Branch>) sessionFactory.getCurrentSession().
//                    createQuery(querySt).setFirstResult(beginIndex).setMaxResults(pageSize).list();
//            Integer count = sessionFactory.getCurrentSession().
//                    createQuery(querySt).list().size();
//            result.add(list);
//            result.add(count);
//            result.add(count);
//            return result;
//        }
//        catch (Exception e)
//        {
//            System.out.println("ex = " + e.getMessage());
//        }
//        return null;
//    }

}
