/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.payroll.model.Batch;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface BatchDao
{

    public Batch getBatch(Integer id);

    /**
     *
     * @param batch
     */
    public void save(Batch batch);

    public void edit(Batch batch) throws ExceptionWrapper;

    /**
     *
     * @param batch
     * @return
     * @throws ExceptionWrapper
     */
    public boolean delete(Batch batch) throws ExceptionWrapper;

    public List<Batch> getBatchList();
    
    /**
     *
     * @param schemeId
     * @return
     */
    public List<Batch> getBatchListByScheme(Integer schemeId);

    public List<ItemObject> getBatchIoList(Integer schemeId,
            boolean isSearchByActive, boolean isActive);

    public boolean checkUniqueBatch(Integer batchId, String nameInEnglish, Integer schemeId);
}
