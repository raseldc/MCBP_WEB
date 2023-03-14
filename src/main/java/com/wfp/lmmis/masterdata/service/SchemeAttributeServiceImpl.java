/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.SchemeAttributeDao;
import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service
@Transactional
public class SchemeAttributeServiceImpl implements SchemeAttributeService{
    @Autowired
    SchemeAttributeDao schemeAttributeDao;
    
    /**
     *
     * @param id
     * @return
     */
    @Override
    public SchemeAttribute getSchemeAttribute(Integer id)
    {
        return this.schemeAttributeDao.getSchemeAttribute(id);
    }
    
    @Override
    public void save(SchemeAttribute schemeAttribute)
    {
       this.schemeAttributeDao.save(schemeAttribute);
    }
    
    @Override
    public void edit(SchemeAttribute schemeAttribute)
    {
        this.schemeAttributeDao.edit(schemeAttribute);
    }
    
    @Override
    public void delete(SchemeAttribute schemeAttribute)
    {
        this.schemeAttributeDao.delete(schemeAttribute);
    }
    
    @Override
    public List<SchemeAttribute> getSchemeAttributeList(Integer schemeId)
    {
        return this.schemeAttributeDao.getSchemeAttributeList(schemeId);
    }

    @Override
    public List<SchemeAttribute> getSchemeAttributeDDList()
    {
        return this.schemeAttributeDao.getSchemeAttributeDDList();
    }

    /**
     *
     * @param schemeCode
     * @return
     */
    @Override
    public List<SchemeAttribute> getSchemeAttributeListByCode(Integer schemeCode)
    {
        return this.schemeAttributeDao.getSchemeAttributeListByCode(schemeCode);
    }
    
}
