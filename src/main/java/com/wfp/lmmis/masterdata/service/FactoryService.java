/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.types.FactoryType;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public interface FactoryService
{
    public Factory getFactory(Integer id);
    
    public boolean save(Factory factory);
    
    public void edit(Factory factory);
    
    /**
     *
     * @param factory
     * @return
     */
    public boolean delete (Factory factory);
    
    /**
     *
     * @param factoryType
     * @return
     */
    public List<Factory> getFactoryList(FactoryType factoryType);
    
    public List<Object> getBGMEASearchListBySearchParameter(Map parameter, int beginIndex, int pageSize);
    
    public List<Object> getBKMEASearchListBySearchParameter(Map parameter, int beginIndex, int pageSize);
}
