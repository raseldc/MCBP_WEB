/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.BranchDao;
import com.wfp.lmmis.masterdata.model.Branch;
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
public class BranchServiceImpl implements BranchService
{

    @Autowired
    BranchDao branchDao;

    @Override
    public Branch getBranch(Integer id)
    {
        return this.branchDao.getBranch(id);
    }

    @Override
    public boolean save(Branch branch)
    {
        return this.branchDao.save(branch);
    }

    @Override
    public void edit(Branch branch)
    {
        this.branchDao.edit(branch);
    }

    @Override
    public void delete(Branch branch)
    {
        this.branchDao.delete(branch);
    }

    @Override
    public List<Branch> getBranchList(Integer bankId)
    {
        return this.branchDao.getBranchList(bankId);
    }

    /**
     *
     * @param bankId
     * @param districtId
     * @return
     */
    @Override
    public List<ItemObject> getBranchIoList(Integer bankId, Integer districtId)
    {
        return this.branchDao.getBranchIoList(bankId, districtId);
    }

    @Override
    public List<Object> getBranchSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        return this.branchDao.getBranchSearchListBySearchParameter(parameter, beginIndex, pageSize);
    }
}
