/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import java.util.List;

/**
 *
 * @author sarwar
 */
public interface SchemeAttributeDao
{
    public SchemeAttribute getSchemeAttribute(Integer id);
    
    public void save(SchemeAttribute schemeAttribute);
    
    public void edit(SchemeAttribute schemeAttribute);
    
    public void delete(SchemeAttribute schemeAttribute);
    
    /**
     *
     * @param schemeId
     * @return
     */
    public List<SchemeAttribute> getSchemeAttributeList(Integer schemeId);
    
    public List<SchemeAttribute> getSchemeAttributeListByCode(Integer schemeCode);
    
    public List<SchemeAttribute> getSchemeAttributeDDList();
}
