/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface DivisionDao
{
    public Division getDivision(Integer id);
    
    /**
     *
     * @param division
     * @return
     */
    public boolean save(Division division);
    
    /**
     *
     * @param division
     */
    public void edit(Division division);
    
    public boolean delete (Division division);
    
    /**
     *
     * @return
     */
    public List<Division> getDivisionList();
    
    /**
     *
     * @return
     */
    public List<ItemObject> getDivisionIoList();
}
