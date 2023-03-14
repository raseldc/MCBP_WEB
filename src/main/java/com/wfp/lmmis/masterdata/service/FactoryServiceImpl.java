/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.FactoryDao;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.types.FactoryType;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service
@Transactional
public class FactoryServiceImpl implements FactoryService
{

    @Autowired
    FactoryDao factoryDao;

    @Override
    public Factory getFactory(Integer id)
    {
        return this.factoryDao.getFactory(id);
    }

    @Override
    public boolean save(Factory factory)
    {
        return this.factoryDao.save(factory);
    }

    @Override
    public void edit(Factory factory)
    {
        this.factoryDao.edit(factory);
    }

    @Override
    public boolean delete(Factory factory)
    {
        return this.factoryDao.delete(factory);
    }

    @Override
    public List<Factory> getFactoryList(FactoryType factoryType)
    {
        return this.factoryDao.getFactoryList(factoryType);
    }
     
    @Override
    public List<Object> getBGMEASearchListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        return this.factoryDao.getBGMEASearchListBySearchParameter(parameter, beginIndex, pageSize);
    }
    
    @Override
    public List<Object> getBKMEASearchListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        return this.factoryDao.getBKMEASearchListBySearchParameter(parameter, beginIndex, pageSize);
    }
}
