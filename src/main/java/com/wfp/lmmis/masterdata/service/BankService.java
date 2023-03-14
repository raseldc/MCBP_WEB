/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.model.Bank;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author user
 */
public interface BankService
{
    public Bank getBank(Integer id);
    
    public boolean save(Bank bank);
    
    public void edit(Bank bank);
    
    public boolean delete (Bank bank);
    
    /**
     *
     * @return
     */
    public List<Bank> getBankList();
    
    /**
     *
     * @return
     */
    public List<ItemObject> getBankIoList();
}
