/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.payroll.dao.BatchDao;
import com.wfp.lmmis.payroll.model.Batch;
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
public class BatchServiceImpl implements BatchService
{

    @Autowired
    BatchDao batchDao;

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Batch getBatch(Integer id)
    {
        return this.batchDao.getBatch(id);
    }

    /**
     *
     * @param batch
     */
    @Override
    public void save(Batch batch)
    {
        this.batchDao.save(batch);
    }

    @Override
    public void edit(Batch batch) throws ExceptionWrapper
    {
        this.batchDao.edit(batch);
    }

    /**
     *
     * @param batch
     * @return
     * @throws ExceptionWrapper
     */
    @Override
    public boolean delete(Batch batch) throws ExceptionWrapper
    {
        return this.batchDao.delete(batch);
    }

    @Override
    public List<Batch> getBatchList()
    {
        return this.batchDao.getBatchList();
    }

    @Override
    public List<Batch> getBatchListByScheme(Integer schemeId)
    {
        return this.batchDao.getBatchListByScheme(schemeId);
    }

    @Override
    public List<ItemObject> getBatchIoList(Integer schemeId,
            boolean isSearchByActive, boolean isActive)
    {
        return this.batchDao.getBatchIoList(schemeId,
                isSearchByActive, isActive);
    }

//    @Override
//    public List<ItemObject> getBatchIoList()
//    {
//        return this.batchDao.getBatchIoList();
//    }
    @Override
    public boolean checkUniqueBatch(Integer batchId, String nameInEnglish, Integer schemeId)
    {
        return this.batchDao.checkUniqueBatch(batchId, nameInEnglish, schemeId);
    }

}
