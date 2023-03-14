/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.model.SchemeAttributeSetupData;
import java.util.List;

/**
 *
 * @author sarwar
 */
public interface SchemeAttributeSetupDataDao
{
    public SchemeAttributeSetupData getSchemeAttributeSetupData(Integer id);
    
    public void save(SchemeAttributeSetupData schemeAttributeSetupData);
    
    public void edit(SchemeAttributeSetupData schemeAttributeSetupData);
    
    public void delete(Integer id);
    
    public List<SchemeAttributeSetupData> getSchemeAttributeSetupDataList(Integer schemeAttributeId);
    
    public SchemeAttributeSetupData getSchemeAttributeSetupDataList(Integer schemeAttributeId, String attributeValueEnglish, String attributeValueBangla);
}
