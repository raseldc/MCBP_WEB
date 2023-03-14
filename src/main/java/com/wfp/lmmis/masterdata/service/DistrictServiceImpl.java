/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.DistrictDao;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.DistrictInfo;
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
public class DistrictServiceImpl implements DistrictService
{

    @Autowired
    DistrictDao districtDao;

    @Override
    public District getDistrict(Integer id)
    {
        return this.districtDao.getDistrict(id);
    }

    @Override
    public boolean save(District district)
    {
        return this.districtDao.save(district);
    }

    /**
     *
     * @param district
     */
    @Override
    public void edit(District district)
    {
        this.districtDao.edit(district);
    }

    /**
     *
     * @param district
     * @return
     */
    @Override
    public boolean delete(District district)
    {
        return this.districtDao.delete(district);
    }

    @Override
    public List<DistrictInfo> getDistrictList(Integer divisionId)
    {
        return this.districtDao.getDistrictList(divisionId);
    }

    @Override
    public List<ItemObject> getDistrictIoList(Integer divisionId)
    {
        return this.districtDao.getDistrictIoList(divisionId);
    }
}
