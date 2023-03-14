/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import com.wfp.lmmis.masterdata.model.SchemeAttributeSetupData;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SchemeAttributeSetupDataDaoImpl implements SchemeAttributeSetupDataDao
{

    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public SchemeAttributeSetupData getSchemeAttributeSetupData(Integer id)
    {
        return (SchemeAttributeSetupData) this.sessionFactory.getCurrentSession().get(SchemeAttributeSetupData.class, id);
    }

    @Override
    public void save(SchemeAttributeSetupData schemeAttributeSetupData)
    {
        try
        {
            this.sessionFactory.getCurrentSession().save(schemeAttributeSetupData);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void edit(SchemeAttributeSetupData editedSchemeAttributeSetupData)
    {
        try
        {
            SchemeAttributeSetupData schemeAttributeSetupData = (SchemeAttributeSetupData) sessionFactory.getCurrentSession().get(SchemeAttributeSetupData.class, editedSchemeAttributeSetupData.getId());
            schemeAttributeSetupData.setAttributeValueInEnglish(editedSchemeAttributeSetupData.getAttributeValueInEnglish());
            schemeAttributeSetupData.setAttributeValueInBangla(editedSchemeAttributeSetupData.getAttributeValueInBangla());
            schemeAttributeSetupData.setSchemeAttribute(editedSchemeAttributeSetupData.getSchemeAttribute());
            schemeAttributeSetupData.setModifiedBy(editedSchemeAttributeSetupData.getModifiedBy());
            schemeAttributeSetupData.setModificationDate(editedSchemeAttributeSetupData.getModificationDate());
            this.sessionFactory.getCurrentSession().update(schemeAttributeSetupData);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param schemeAttributeId
     * @return
     */
    @Override
    public List<SchemeAttributeSetupData> getSchemeAttributeSetupDataList(Integer schemeAttributeId)
    {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (schemeAttributeId == null)
        {
            querySt = "from SchemeAttributeSetupData";
        }
        else
        {
            querySt = "from SchemeAttributeSetupData o where o.schemeAttribute.id = " + schemeAttributeId;
        }

        List<SchemeAttributeSetupData> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        return list;
    }

    @Override
    public SchemeAttributeSetupData getSchemeAttributeSetupDataList(Integer schemeAttributeId, String attributeValueEnglish, String attributeValueBangla)
    {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (schemeAttributeId == null)
        {
            querySt = "from SchemeAttributeSetupData";
        }
        else
        {
            querySt = "from SchemeAttributeSetupData o where o.schemeAttribute.id = " + schemeAttributeId;
        }        

        if (attributeValueEnglish != null)
        {
            querySt += " AND o.attributeValueInEnglish = '" + attributeValueEnglish + "'";
        }
        if (attributeValueBangla != null)
        {
            querySt += " AND o.attributeValueInBangla = '" + attributeValueBangla + "'";
        }

        List<SchemeAttributeSetupData> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        if (list != null && !list.isEmpty())
        {
            return list.get(0);
        }
        return null;
    }

    /**
     *
     * @param id
     */
    @Override
    public void delete(Integer id)
    {
        try
        {
            SchemeAttributeSetupData schemeAttributeSetupData = (SchemeAttributeSetupData) sessionFactory.getCurrentSession().get(SchemeAttributeSetupData.class, id);
            if (schemeAttributeSetupData != null)
            {
                SchemeAttribute schemeAttribute = schemeAttributeSetupData.getSchemeAttribute();
                schemeAttribute.getSchemeAttributeDataList().remove(schemeAttributeSetupData);
                sessionFactory.getCurrentSession().update(schemeAttribute);
                sessionFactory.getCurrentSession().delete(schemeAttributeSetupData);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
