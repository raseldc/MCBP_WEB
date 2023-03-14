/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.SchemeAttributeValueDao;
import com.wfp.lmmis.applicant.model.SchemeAttributeValue;
import com.wfp.lmmis.masterdata.service.*;
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
public class SchemeAttributeValueServiceImpl implements SchemeAttributeValueService
{

    @Autowired
    SchemeAttributeValueDao schemeAttributeValueDao;

//    @Override
//    public SchemeAttribute getSchemeAttribute(Integer id)
//    {
//        return this.schemeAttributeDao.getSchemeAttribute(id);
//    }
//    
//    @Override
//    public void save(SchemeAttribute schemeAttribute)
//    {
//       this.schemeAttributeDao.save(schemeAttribute);
//    }
//    
//    @Override
//    public void edit(SchemeAttribute schemeAttribute)
//    {
//        this.schemeAttributeDao.edit(schemeAttribute);
//    }
//    
//    @Override
//    public void delete(Integer id)
//    {
//        this.schemeAttributeDao.delete(id);
//    }
    @Override
    public List<SchemeAttributeValue> getSchemeAttributeValueList(Integer applicantId)
    {
        return this.schemeAttributeValueDao.getSchemeAttributeValueList(applicantId);
    }

//    @Override
//    public List<SchemeAttribute> getSchemeAttributeDDList()
//    {
//        return this.schemeAttributeDao.getSchemeAttributeDDList();
//    }
}
