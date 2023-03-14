/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.DivisionDao;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service
@Transactional
public class DivisionServiceImpl implements DivisionService{
    @Autowired
    DivisionDao divisionDao;
    
    @Override
    public Division getDivision(Integer id)
    {
        return this.divisionDao.getDivision(id);
    }
    
    @Override
    public boolean save(Division division)
    {
       return this.divisionDao.save(division);
    }
    
    /**
     *
     * @param division
     */
    @Override
    public void edit(Division division)
    {
        this.divisionDao.edit(division);
    }
    
    /**
     *
     * @param division
     * @return
     */
    @Override
    public boolean delete (Division division){
        return this.divisionDao.delete(division);
    }
    
    @Override
    public List<Division> getDivisionList()
    {
        return this.divisionDao.getDivisionList();
    }
    @Override
    public List<ItemObject> getDivisionIoList()
    {
        return this.divisionDao.getDivisionIoList();
    }
}
