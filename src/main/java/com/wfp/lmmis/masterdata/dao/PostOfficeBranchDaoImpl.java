/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.PostOfficeBranch;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.utility.ItemObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class PostOfficeBranchDaoImpl implements PostOfficeBranchDao
{

    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public PostOfficeBranch getPostOfficeBranch(Integer id)
    {
        PostOfficeBranch postOfficeBranch = (PostOfficeBranch) this.sessionFactory.getCurrentSession().get(PostOfficeBranch.class, id);
        return postOfficeBranch;
    }

    @Override
    public boolean save(PostOfficeBranch postOfficeBranch)
    {
        String duplicateCheckQuery = "select count(id) from PostOfficeBranch where (nameInEnglish = '" + postOfficeBranch.getNameInEnglish() + "' or nameInBangla = '" + postOfficeBranch.getNameInBangla() + "' or code = '" + postOfficeBranch.getCode() + "')";
        System.out.println("query = " + duplicateCheckQuery);
        long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
        System.out.println("duplicate count = " + duplicateCount);
        if (duplicateCount == 0)
        {
            Integer postOfficeBranchId = (Integer) this.sessionFactory.getCurrentSession().save(postOfficeBranch);
            System.out.println("postOfficeBranch is "+postOfficeBranchId);
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.PostOfficeBranch, postOfficeBranchId, postOfficeBranch.getCreatedBy(), postOfficeBranch.getCreationDate(), null, postOfficeBranch.getNameInBangla() + " status:" + postOfficeBranch.isActive() + " code:" + postOfficeBranch.getCode());
            this.getCurrentSession().save(changeLog);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void edit(PostOfficeBranch editedPostOfficeBranch)
    {
        PostOfficeBranch postOfficeBranch = (PostOfficeBranch) this.sessionFactory.getCurrentSession().get(PostOfficeBranch.class, editedPostOfficeBranch.getId());
        if (postOfficeBranch != null)
        {
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.PostOfficeBranch, postOfficeBranch.getId(), editedPostOfficeBranch.getModifiedBy(), editedPostOfficeBranch.getModificationDate(), postOfficeBranch.getNameInBangla() + " status:" + postOfficeBranch.isActive() + " code:" + postOfficeBranch.getCode(), editedPostOfficeBranch.getNameInBangla() + " status:" + editedPostOfficeBranch.isActive() + " code:" + editedPostOfficeBranch.getCode());
            this.getCurrentSession().save(changeLog);

            postOfficeBranch.setNameInBangla(editedPostOfficeBranch.getNameInBangla());
            postOfficeBranch.setNameInEnglish(editedPostOfficeBranch.getNameInEnglish());
            postOfficeBranch.setCode(editedPostOfficeBranch.getCode());
            postOfficeBranch.setRoutingNumber(editedPostOfficeBranch.getRoutingNumber());
            postOfficeBranch.setDistrict(editedPostOfficeBranch.getDistrict());
            postOfficeBranch.setAddress(editedPostOfficeBranch.getAddress());
            postOfficeBranch.setActive(editedPostOfficeBranch.isActive());
            postOfficeBranch.setModifiedBy(editedPostOfficeBranch.getModifiedBy());
            postOfficeBranch.setModificationDate(editedPostOfficeBranch.getModificationDate());
        }
        this.sessionFactory.getCurrentSession().update(postOfficeBranch);
    }

    /**
     *
     * @param postOfficeBranch
     */
    @Override
    public void delete(PostOfficeBranch postOfficeBranch)
    {
        if (postOfficeBranch != null)
        {
            System.out.println("PostOfficeBranch not null id is = " + postOfficeBranch.getId() + " name = " + postOfficeBranch.getNameInEnglish());
            Query q = this.sessionFactory.getCurrentSession().createQuery("delete from PostOfficeBranch p where p.id = " + postOfficeBranch.getId());
            q.executeUpdate();

            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.PostOfficeBranch, postOfficeBranch.getId(), postOfficeBranch.getModifiedBy(), postOfficeBranch.getModificationDate(), postOfficeBranch.getNameInBangla(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
    }

    @Override
    public List<PostOfficeBranch> getPostOfficeBranchList(Integer districtId)
    {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (districtId == null)
        {
            querySt = "from PostOfficeBranch o where o.deleted=0";
        }
        else
        {
            querySt = "from PostOfficeBranch o where o.district.id = " + districtId + " and o.deleted=0";
        }

        List<PostOfficeBranch> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        return list;
    }

    @Override
    public List<ItemObject> getPostOfficeBranchIoList(Integer districtId)
    {
        @SuppressWarnings("unchecked")
        String querySt = null;

        querySt = "select NEW com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish)  from PostOfficeBranch o";

        querySt += " WHERE 0 = 0 and o.active=1 and o.deleted=0";

        if (districtId != null)
        {
            querySt += " AND o.district.id = " + districtId;
        }
        System.out.println("querySt = " + querySt);

        try
        {
            List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return itemObjects;
        }
        catch (Exception e)
        {
            System.out.println("ex = " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param parameter
     * @param beginIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<Object> getPostOfficeBranchSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        @SuppressWarnings("unchecked")
        List<Object> result = new ArrayList<Object>();
//        Integer bankId = (Integer) parameter.get("bankId");
//        Integer districtId = (Integer) parameter.get("districtId");

        String querySt = "from PostOfficeBranch o join fetch o.district d where 0=0";
        if (parameter.get("districtId") != null)
        {
            querySt += " and o.district.id = " + parameter.get("districtId");
        }
        System.out.println("query st = " + querySt);
        try
        {
            List<PostOfficeBranch> list = (List<PostOfficeBranch>) sessionFactory.getCurrentSession().
                    createQuery(querySt).setFirstResult(beginIndex).setMaxResults(pageSize).list();
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
