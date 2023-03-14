/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.model.MobileBankingProvider;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface MobileBankingProviderDao
{
    public MobileBankingProvider getMobileBankingProvider(Integer id);
    
    /**
     *
     * @param mobileBankingProvider
     */
    public void save(MobileBankingProvider mobileBankingProvider);
    
    public void edit(MobileBankingProvider mobileBankingProvider);
    
    public void delete (MobileBankingProvider mobileBankingProvider) throws ExceptionWrapper;
    
    /**
     *
     * @param activeOnly
     * @return
     */
    public List<MobileBankingProvider> getMobileBankingProviderList(boolean activeOnly);
    
    public List<ItemObject> getMobileBankingProviderIoList(boolean activeOnly);
}
