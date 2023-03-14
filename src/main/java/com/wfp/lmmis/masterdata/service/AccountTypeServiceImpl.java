/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.AccountTypeDao;
import com.wfp.lmmis.masterdata.model.AccountType;
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
public class AccountTypeServiceImpl implements AccountTypeService
{
    @Autowired
    AccountTypeDao accountTypeDao;
    
    @Override
    public AccountType getAccountType(Integer id)
    {
        return this.accountTypeDao.getAccountType(id);
    }
    
    @Override
    public boolean save(AccountType accountType)
    {
       return this.accountTypeDao.save(accountType);
    }
    
    @Override
    public void edit(AccountType accountType)
    {
        this.accountTypeDao.edit(accountType);
    }
    
    /**
     *
     * @param accountType
     */
    @Override
    public void delete (AccountType accountType){
        this.accountTypeDao.delete(accountType);
    }
    
    @Override
    public List<AccountType> getAccountTypeList()
    {
        return this.accountTypeDao.getAccountTypeList();
    }
}
