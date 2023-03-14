/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.payroll.dao.PaymentCycleDao;
import com.wfp.lmmis.payroll.model.PaymentCycle;
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
public class PaymentCycleServiceImpl implements PaymentCycleService
{

    @Autowired
    PaymentCycleDao paymentCycleDao;

    @Override
    public PaymentCycle getPaymentCycle(Integer id)
    {
        return this.paymentCycleDao.getPaymentCycle(id);
    }

    @Override
    public void save(PaymentCycle paymentCycle)
    {
        this.paymentCycleDao.save(paymentCycle);
    }

    @Override
    public void edit(PaymentCycle paymentCycle) throws ExceptionWrapper
    {
        this.paymentCycleDao.edit(paymentCycle);
    }

    /**
     *
     * @param paymentCycle
     * @return
     * @throws ExceptionWrapper
     */
    @Override
    public boolean delete(PaymentCycle paymentCycle) throws ExceptionWrapper
    {
        return this.paymentCycleDao.delete(paymentCycle);
    }

    @Override
    public List<PaymentCycle> getPaymentCycleList(Integer schemeId)
    {
        return this.paymentCycleDao.getPaymentCycleList(schemeId);
    }

    @Override
    public List<PaymentCycle> getParentPaymentCycleList(Integer schemeId)
    {
        return this.paymentCycleDao.getParentPaymentCycleList(schemeId);
    }

    @Override
    public List<ItemObject> getPaymentCycleIoList(boolean isSearchByFiscalYear, Integer fiscalYearId,
            boolean isSearchByActive, boolean isActive, Integer schemeId)
    {
        return this.paymentCycleDao.getPaymentCycleIoList(isSearchByFiscalYear, fiscalYearId,
                isSearchByActive, isActive, schemeId);
    }

    @Override
    public List<ItemObject> getParentPaymentCycleIoList(Integer fiscalYearId, Integer schemeId)
    {
        return this.paymentCycleDao.getParentPaymentCycleIoList(fiscalYearId, schemeId);
    }

    /**
     *
     * @param schemeId
     * @return
     */
    @Override
    public List<ItemObject> getParentPaymentCycleIoListByScheme(Integer schemeId)
    {
        return this.paymentCycleDao.getParentPaymentCycleIoListByScheme(schemeId);
    }

    @Override
    public List<ItemObject> getChildPaymentCycleIoList(Integer fiscalYearId, Integer schemeId)
    {
        return this.paymentCycleDao.getChildPaymentCycleIoList(fiscalYearId, schemeId);
    }

//    @Override
//    public List<ItemObject> getPaymentCycleIoList()
//    {
//        return this.paymentCycleDao.getPaymentCycleIoList();
//    }
    @Override
    public boolean checkUniquePaymentCycle(Integer schemeId, Integer paymentCycleId, String nameInEnglish)
    {
        return this.paymentCycleDao.checkUniquePaymentCycle(schemeId, paymentCycleId, nameInEnglish);
    }

}
