/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.model.MobileBankingProvider;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface MobileBankingProviderService
{
    public MobileBankingProvider getMobileBankingProvider(Integer id);
    
    public void save(MobileBankingProvider mobileBankingProvider);
    
    public void edit(MobileBankingProvider mobileBankingProvider);
    
    public void delete (MobileBankingProvider mobileBankingProvider) throws ExceptionWrapper;
    
    public List<MobileBankingProvider> getMobileBankingProviderList(boolean activeOnly);
    
    /**
     *
     * @param activeOnly
     * @return
     */
    public List<ItemObject> getMobileBankingProviderIoList(boolean activeOnly);
}
