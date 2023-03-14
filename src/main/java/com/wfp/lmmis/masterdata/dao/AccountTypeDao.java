/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.model.AccountType;
import java.util.List;

/**
 *
 * @author user
 */
public interface AccountTypeDao
{

    /**
     *
     * @param id
     * @return
     */
    public AccountType getAccountType(Integer id);
    
    /**
     *
     * @param bank
     * @return
     */
    public boolean save(AccountType bank);
    
    public void edit(AccountType bank);
    
    public void delete (AccountType accountType);
    
    public List<AccountType> getAccountTypeList();
}
