/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.payroll.dao.FiscalYearDao;
import com.wfp.lmmis.payroll.model.FiscalYear;
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
public class FiscalYearServiceImpl implements FiscalYearService
{

    @Autowired
    FiscalYearDao fiscalYearDao;

    @Override
    public FiscalYear getFiscalYear(Integer id)
    {
        return this.fiscalYearDao.getFiscalYear(id);
    }

    @Override
    public boolean save(FiscalYear fiscalYear)
    {
        return this.fiscalYearDao.save(fiscalYear);
    }

    @Override
    public void edit(FiscalYear fiscalYear) throws ExceptionWrapper
    {
        this.fiscalYearDao.edit(fiscalYear);
    }

    @Override
    public void delete(FiscalYear fiscalYear) throws ExceptionWrapper
    {
        this.fiscalYearDao.delete(fiscalYear);
    }

    @Override
    public List<FiscalYear> getFiscalYearList(boolean isSearchByActive, boolean isActive)
    {
        return this.fiscalYearDao.getFiscalYearList(isSearchByActive, isActive);
    }

    @Override
    public List<ItemObject> getFiscalYearIoList()
    {
        return this.fiscalYearDao.getFiscalYearIoList();
    }

    @Override
    public FiscalYear getActiveFiscalYear()
    {
        return this.fiscalYearDao.getActiveFiscalYear();
    }

    /**
     *
     * @param fiscalYearId
     * @param nameInEnglish
     * @return
     */
    @Override
    public boolean checkUniqueFiscalYear(Integer fiscalYearId, String nameInEnglish)
    {
        return this.fiscalYearDao.checkUniqueFiscalYear(fiscalYearId, nameInEnglish);
    }

}
