/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.BankDao;
import com.wfp.lmmis.masterdata.model.Bank;
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
public class BankServiceImpl implements BankService
{

    @Autowired
    BankDao bankDao;

    @Override
    public Bank getBank(Integer id)
    {
        return this.bankDao.getBank(id);
    }

    @Override
    public boolean save(Bank bank)
    {
        return this.bankDao.save(bank);
    }

    /**
     *
     * @param bank
     */
    @Override
    public void edit(Bank bank)
    {
        this.bankDao.edit(bank);
    }

    @Override
    public boolean delete(Bank bank)
    {
        return this.bankDao.delete(bank);
    }

    @Override
    public List<Bank> getBankList()
    {
        return this.bankDao.getBankList();
    }

    /**
     *
     * @return
     */
    @Override
    public List<ItemObject> getBankIoList()
    {
        return this.bankDao.getBankIoList();
    }
}
