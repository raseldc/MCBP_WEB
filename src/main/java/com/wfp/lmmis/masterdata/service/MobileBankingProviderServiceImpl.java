/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.dao.MobileBankingProviderDao;
import com.wfp.lmmis.masterdata.model.MobileBankingProvider;
import com.wfp.lmmis.utility.ItemObject;
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
public class MobileBankingProviderServiceImpl implements MobileBankingProviderService{
    @Autowired
    MobileBankingProviderDao mobileBankingProviderDao;
    
    @Override
    public MobileBankingProvider getMobileBankingProvider(Integer id)
    {
        return this.mobileBankingProviderDao.getMobileBankingProvider(id);
    }
    
    /**
     *
     * @param mobileBankingProvider
     */
    @Override
    public void save(MobileBankingProvider mobileBankingProvider)
    {
       this.mobileBankingProviderDao.save(mobileBankingProvider);
    }
    
    @Override
    public void edit(MobileBankingProvider mobileBankingProvider)
    {
        this.mobileBankingProviderDao.edit(mobileBankingProvider);
    }
    
    @Override
    public void delete (MobileBankingProvider mobileBankingProvider) throws ExceptionWrapper
    {
        this.mobileBankingProviderDao.delete(mobileBankingProvider);
    }
    
    @Override
    public List<MobileBankingProvider> getMobileBankingProviderList(boolean activeOnly)
    {
        return this.mobileBankingProviderDao.getMobileBankingProviderList(activeOnly);
    }
    @Override
    public List<ItemObject> getMobileBankingProviderIoList(boolean activeOnly)
    {
        return this.mobileBankingProviderDao.getMobileBankingProviderIoList(activeOnly);
    }
}
