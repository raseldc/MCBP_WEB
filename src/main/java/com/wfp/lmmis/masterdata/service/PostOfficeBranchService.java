/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.model.PostOfficeBranch;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public interface PostOfficeBranchService
{
    public PostOfficeBranch getPostOfficeBranch(Integer id);
    
    public boolean save(PostOfficeBranch postOfficeBranch);
    
    public void edit(PostOfficeBranch postOfficeBranch);
    
    public void delete (PostOfficeBranch postOfficeBranch);
    
    public List<PostOfficeBranch> getPostOfficeBranchList(Integer districtId);
    
    public List<ItemObject> getPostOfficeBranchIoList(Integer districtId);
        
    public List<Object> getPostOfficeBranchSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize);
}
