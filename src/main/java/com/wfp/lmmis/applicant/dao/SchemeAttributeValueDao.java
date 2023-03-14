/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.applicant.model.SchemeAttributeValue;
import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import java.util.List;

/**
 *
 * @author sarwar
 */
public interface SchemeAttributeValueDao
{
//    public SchemeAttribute getSchemeAttribute(Integer id);
//    
//    public void save(SchemeAttribute schemeAttribute);
//    
//    public void edit(SchemeAttribute schemeAttribute);
//    
//    public void delete(Integer id);

    /**
     *
     * @param applicantId
     * @return
     */
    
    public List<SchemeAttributeValue> getSchemeAttributeValueList(Integer applicantId);
    
//    public List<SchemeAttribute> getSchemeAttributeDDList();
}
