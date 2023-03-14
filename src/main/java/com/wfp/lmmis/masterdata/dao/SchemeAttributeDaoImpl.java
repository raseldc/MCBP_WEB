/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.AttributeType;
import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import com.wfp.lmmis.model.ChangeLog;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SchemeAttributeDaoImpl implements SchemeAttributeDao
{

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
    public SchemeAttribute getSchemeAttribute(Integer id)
    {
        return (SchemeAttribute) this.sessionFactory.getCurrentSession().get(SchemeAttribute.class, id);
    }

    @Override
    public void save(SchemeAttribute schemeAttribute)
    {
        try
        {
            this.sessionFactory.getCurrentSession().save(schemeAttribute);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void edit(SchemeAttribute editedSchemeAttribute)
    {
        try
        {
            SchemeAttribute schemeAttribute = (SchemeAttribute) sessionFactory.getCurrentSession().get(SchemeAttribute.class, editedSchemeAttribute.getId());
            schemeAttribute.setNameInBangla(editedSchemeAttribute.getNameInBangla());
            schemeAttribute.setNameInEnglish(editedSchemeAttribute.getNameInEnglish());
            schemeAttribute.setAttributeType(editedSchemeAttribute.getAttributeType());
            schemeAttribute.setIsMandatoryForSelectionCriteria(editedSchemeAttribute.isIsMandatoryForSelectionCriteria());
            schemeAttribute.setViewOrder(editedSchemeAttribute.getViewOrder());
            schemeAttribute.setOrderingType(editedSchemeAttribute.getOrderingType());
            schemeAttribute.setSelectionCriteriaPriority(editedSchemeAttribute.getSelectionCriteriaPriority());
            schemeAttribute.setComparisonType(editedSchemeAttribute.getComparisonType());
            schemeAttribute.setComparedValue(editedSchemeAttribute.getComparedValue());
            schemeAttribute.setScheme(editedSchemeAttribute.getScheme());
            schemeAttribute.setModifiedBy(editedSchemeAttribute.getModifiedBy());
            schemeAttribute.setModificationDate(editedSchemeAttribute.getModificationDate());
            this.sessionFactory.getCurrentSession().update(schemeAttribute);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public List<SchemeAttribute> getSchemeAttributeList(Integer schemeId)
    {
        String querySt;
        if (null != schemeId)
        {
//            querySt = "from SchemeAttribute o join fetch o.schemeAttributeDataList where o.deleted=0 and o.scheme.id = " + schemeId + " order by o.selectionCriteriaPriority";
            querySt = "from SchemeAttribute o where o.deleted=0 and o.scheme.id = " + schemeId + " order by o.selectionCriteriaPriority";
        }
        else
        {
            querySt = "from SchemeAttribute o where o.deleted=0 order by o.selectionCriteriaPriority";
        }
        System.out.println("query is: " + querySt);

        List<SchemeAttribute> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        System.out.println("query end .");
        System.out.println("list.size() = " + list.size());
        return list;
    }

    /**
     *
     * @param schemeCode
     * @return
     */
    @Override
    public List<SchemeAttribute> getSchemeAttributeListByCode(Integer schemeCode)
    {
        try
        {
            String querySt;
            if (null != schemeCode)
            {
                querySt = "from SchemeAttribute o where o.deleted=0 and o.scheme.code = " + schemeCode + " order by o.selectionCriteriaPriority";
            }
            else
            {
                querySt = "from SchemeAttribute o where o.deleted=0 order by o.selectionCriteriaPriority";
            }
            System.out.println("query is " + querySt);

            List<SchemeAttribute> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return list;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SchemeAttribute> getSchemeAttributeDDList()
    {
        try
        {
            String querySt = "from SchemeAttribute o where o.deleted=0 and o.attributeType not in :attributeType";
            List<SchemeAttribute> list = sessionFactory.getCurrentSession().createQuery(querySt).setParameter("attributeType", AttributeType.TEXT).list();
            return list;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;

    }

    @Override
    public void delete(SchemeAttribute schemeAttribute)
    {
        try
        {
            Query q = this.getCurrentSession().createQuery("update SchemeAttribute set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + schemeAttribute.getId());
            q.setParameter("modifiedBy", schemeAttribute.getModifiedBy());
            q.setParameter("modificationDate", schemeAttribute.getModificationDate());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Scheme_Attribute, schemeAttribute.getId(), schemeAttribute.getModifiedBy(), schemeAttribute.getModificationDate(), schemeAttribute.getNameInEnglish(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
