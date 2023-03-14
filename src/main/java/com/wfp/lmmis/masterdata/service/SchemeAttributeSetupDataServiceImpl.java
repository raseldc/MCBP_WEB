/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.SchemeAttributeSetupDataDao;
import com.wfp.lmmis.masterdata.model.SchemeAttributeSetupData;
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
public class SchemeAttributeSetupDataServiceImpl implements SchemeAttributeSetupDataService{
    @Autowired
    SchemeAttributeSetupDataDao schemeAttributeSetupDataDao;
    
    @Override
    public SchemeAttributeSetupData getSchemeAttributeSetupData(Integer id)
    {
        return this.schemeAttributeSetupDataDao.getSchemeAttributeSetupData(id);
    }
    
    @Override
    public void save(SchemeAttributeSetupData schemeAttributeSetupData)
    {
       this.schemeAttributeSetupDataDao.save(schemeAttributeSetupData);
    }
    
    @Override
    public void edit(SchemeAttributeSetupData schemeAttributeSetupData)
    {
        this.schemeAttributeSetupDataDao.edit(schemeAttributeSetupData);
    }
    
    @Override
    public void delete(Integer id)
    {
        this.schemeAttributeSetupDataDao.delete(id);
    }
    
    @Override
    public List<SchemeAttributeSetupData> getSchemeAttributeSetupDataList(Integer schemeAttributeId)
    {
        return this.schemeAttributeSetupDataDao.getSchemeAttributeSetupDataList(schemeAttributeId);
    }
    @Override
    public SchemeAttributeSetupData getSchemeAttributeSetupDataList(Integer schemeAttributeId, String attributeValueEnglish, String attributeValueBangla)
    {
        return this.schemeAttributeSetupDataDao.getSchemeAttributeSetupDataList(schemeAttributeId, attributeValueEnglish, attributeValueBangla);
    }
}
