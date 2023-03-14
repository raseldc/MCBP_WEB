/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.model.AccountType;
import java.util.List;

/**
 *
 * @author user
 */
public interface AccountTypeService
{
    public AccountType getAccountType(Integer id);
    
    public boolean save(AccountType accountType);
    
    public void edit(AccountType accountType);
    
    public void delete (AccountType accountType);
    
    public List<AccountType> getAccountTypeList();
}
