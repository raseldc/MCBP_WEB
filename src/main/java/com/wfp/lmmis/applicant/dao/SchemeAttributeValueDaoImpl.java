/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.applicant.model.SchemeAttributeValue;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SchemeAttributeValueDaoImpl implements SchemeAttributeValueDao
{

    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

//    @Override
//    public SchemeAttribute getSchemeAttribute(Integer id)
//    {
//        return (SchemeAttribute) this.sessionFactory.getCurrentSession().get(SchemeAttribute.class, id);
//    }
//
//    @Override
//    public void save(SchemeAttribute schemeAttribute)
//    {
//        try
//        {
//            this.sessionFactory.getCurrentSession().save(schemeAttribute);
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
//
//    @Override
//    public void edit(SchemeAttribute editedSchemeAttribute)
//    {
//        try
//        {
//            SchemeAttribute schemeAttribute = (SchemeAttribute) sessionFactory.getCurrentSession().get(SchemeAttribute.class, editedSchemeAttribute.getId());
//            schemeAttribute.setNameInBangla(editedSchemeAttribute.getNameInBangla());
//            schemeAttribute.setNameInEnglish(editedSchemeAttribute.getNameInEnglish());
//            schemeAttribute.setAttributeType(editedSchemeAttribute.getAttributeType());
//            schemeAttribute.setIsMandatoryForSelectionCriteria(editedSchemeAttribute.isIsMandatoryForSelectionCriteria());
//            schemeAttribute.setViewOrder(editedSchemeAttribute.getViewOrder());
//            schemeAttribute.setSelectionCriteriaPriority(editedSchemeAttribute.getSelectionCriteriaPriority());
//            schemeAttribute.setComparisonType(editedSchemeAttribute.getComparisonType());
//            schemeAttribute.setComparedValue(editedSchemeAttribute.getComparedValue());
//            schemeAttribute.setScheme(editedSchemeAttribute.getScheme());
//            schemeAttribute.setModifiedBy(editedSchemeAttribute.getModifiedBy());
//            schemeAttribute.setModificationDate(editedSchemeAttribute.getModificationDate());
//            this.sessionFactory.getCurrentSession().update(schemeAttribute);
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }

    /**
     *
     * @param applicantId
     * @return
     */
    @Override
    public List<SchemeAttributeValue> getSchemeAttributeValueList(Integer applicantId)
    {
        String querySt;
        querySt = "from SchemeAttributeValue o where o.applicant.id = " + applicantId;

        List<SchemeAttributeValue> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        return list;
    }

//    @Override
//    public List<SchemeAttribute> getSchemeAttributeDDList()
//    {
//        try
//        {
//            String querySt = "from SchemeAttribute o where o.attributeType not in :attributeType";
//            List<SchemeAttribute> list = sessionFactory.getCurrentSession().createQuery(querySt).setParameter("attributeType", AttributeType.TEXT).list();
//            return list;
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//        return null;
//
//    }
//
//
//    @Override
//    public void delete(Integer id)
//    {
//        SchemeAttribute schemeAttribute = (SchemeAttribute) sessionFactory.getCurrentSession().get(SchemeAttribute.class, id);
//        if (schemeAttribute != null)
//        {
//            sessionFactory.getCurrentSession().delete(schemeAttribute);
//        }
//    }
}
