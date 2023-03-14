/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.payroll.model.Batch;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface BatchService
{

    public Batch getBatch(Integer id);

    public void save(Batch paymentCycle);

    public void edit(Batch paymentCycle) throws ExceptionWrapper;

    public boolean delete(Batch paymentCycle) throws ExceptionWrapper;

    public List<Batch> getBatchList();
    
    public List<Batch> getBatchListByScheme(Integer schemeId);

    public List<ItemObject> getBatchIoList(Integer schemeId,
            boolean isSearchByActive, boolean isActive);

//    public List<ItemObject> getBatchIoList();
    
    public boolean checkUniqueBatch(Integer paymentCycleId, String nameInEnglish, Integer schemeId);
}
