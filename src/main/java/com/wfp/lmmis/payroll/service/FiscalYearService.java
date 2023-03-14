/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface FiscalYearService
{
    public FiscalYear getFiscalYear(Integer id);
    
    public boolean save(FiscalYear fiscalYear);
    
    public void edit(FiscalYear fiscalYear) throws ExceptionWrapper;
    
    public void delete (FiscalYear fiscalYear) throws ExceptionWrapper;
    
    public List<FiscalYear> getFiscalYearList(boolean isSearchByActive, boolean isActive);
    
    public List<ItemObject> getFiscalYearIoList();
    
    public FiscalYear getActiveFiscalYear();
    
    /**
     *
     * @param fiscalYearId
     * @param nameInEnglish
     * @return
     */
    public boolean checkUniqueFiscalYear(Integer fiscalYearId, String nameInEnglish);
}
