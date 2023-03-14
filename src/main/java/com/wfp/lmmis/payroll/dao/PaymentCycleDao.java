/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.payroll.model.PaymentCycle;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface PaymentCycleDao
{

    public PaymentCycle getPaymentCycle(Integer id);

    public void save(PaymentCycle paymentCycle);

    public void edit(PaymentCycle paymentCycle) throws ExceptionWrapper;

    /**
     *
     * @param paymentCycle
     * @return
     * @throws ExceptionWrapper
     */
    public boolean delete(PaymentCycle paymentCycle) throws ExceptionWrapper;

    public List<PaymentCycle> getPaymentCycleList(Integer schemeId);

    /**
     *
     * @param schemeId
     * @return
     */
    public List<PaymentCycle> getParentPaymentCycleList(Integer schemeId);

    public List<ItemObject> getPaymentCycleIoList(boolean isSearchByFiscalYear, Integer fiscalYearId,
            boolean isSearchByActive, boolean isActive, Integer schemeId);

    public List<ItemObject> getParentPaymentCycleIoList(Integer fiscalYearId, Integer schemeId);
    
    public List<ItemObject> getParentPaymentCycleIoListByScheme(Integer schemeId);

    public List<ItemObject> getChildPaymentCycleIoList(Integer fiscalYearId, Integer schemeId);

    public boolean checkUniquePaymentCycle(Integer schemeId, Integer paymentCycleId, String nameInEnglish);
}
