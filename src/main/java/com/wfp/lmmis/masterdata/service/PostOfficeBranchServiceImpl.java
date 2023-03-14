/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.PostOfficeBranchDao;
import com.wfp.lmmis.masterdata.model.PostOfficeBranch;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service
@Transactional
public class PostOfficeBranchServiceImpl implements PostOfficeBranchService
{

    @Autowired
    PostOfficeBranchDao postOfficeBranchDao;

    /**
     *
     * @param id
     * @return
     */
    @Override
    public PostOfficeBranch getPostOfficeBranch(Integer id)
    {
        return this.postOfficeBranchDao.getPostOfficeBranch(id);
    }

    @Override
    public boolean save(PostOfficeBranch postOfficeBranch)
    {
        return this.postOfficeBranchDao.save(postOfficeBranch);
    }

    @Override
    public void edit(PostOfficeBranch postOfficeBranch)
    {
        this.postOfficeBranchDao.edit(postOfficeBranch);
    }

    /**
     *
     * @param postOfficeBranch
     */
    @Override
    public void delete(PostOfficeBranch postOfficeBranch)
    {
        this.postOfficeBranchDao.delete(postOfficeBranch);
    }

    /**
     *
     * @param districtId
     * @return
     */
    @Override
    public List<PostOfficeBranch> getPostOfficeBranchList(Integer districtId)
    {
        return this.postOfficeBranchDao.getPostOfficeBranchList(districtId);
    }

    @Override
    public List<ItemObject> getPostOfficeBranchIoList(Integer districtId)
    {
        return this.postOfficeBranchDao.getPostOfficeBranchIoList(districtId);
    }

    @Override
    public List<Object> getPostOfficeBranchSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        return this.postOfficeBranchDao.getPostOfficeBranchSearchListBySearchParameter(parameter, beginIndex, pageSize);
    }
}
