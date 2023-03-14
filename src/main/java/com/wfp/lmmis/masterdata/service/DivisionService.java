/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface DivisionService
{
    public Division getDivision(Integer id);
    
    public boolean save(Division division);
    
    public void edit(Division division);
    
    /**
     *
     * @param division
     * @return
     */
    public boolean delete (Division division);
    
    public List<Division> getDivisionList();
    
    public List<ItemObject> getDivisionIoList();
}
