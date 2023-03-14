/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.DoubleDippingMatchedDao;
import com.wfp.lmmis.applicant.model.DoubleDippingMatchedStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service
@Transactional
public class DoubleDippingMatchedServiceImpl implements DoubleDippingMatchedService
{
    @Autowired
    DoubleDippingMatchedDao doubleDippingMatchedDao;
    
    @Override
    public DoubleDippingMatchedStatus getDoubleDippingMatchedStatus(Integer id)
    {
        return this.doubleDippingMatchedDao.getDoubleDippingMatchedStatus(id);
    }

    @Override
    public void save(DoubleDippingMatchedStatus doubleDippingMatchedStatus)
    {
        this.doubleDippingMatchedDao.save(doubleDippingMatchedStatus);
    }

    @Override
    public void edit(DoubleDippingMatchedStatus doubleDippingMatchedStatus)
    {
        this.doubleDippingMatchedDao.edit(doubleDippingMatchedStatus);
    }
}
