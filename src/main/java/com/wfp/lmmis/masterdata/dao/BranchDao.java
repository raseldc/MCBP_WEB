/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.model.Branch;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;
import java.util.Map;


/**
 *
 * @author user
 */
public interface BranchDao
{
    public Branch getBranch(Integer id);
    
    /**
     *
     * @param branch
     * @return
     */
    public boolean save(Branch branch);
    
    public void edit(Branch branch);
    
    public void delete (Branch branch);
    
    public List<Branch> getBranchList(Integer bankId);
    
    public List<ItemObject> getBranchIoList(Integer bankId, Integer districtId);
        
    public List<Object> getBranchSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize);
}
