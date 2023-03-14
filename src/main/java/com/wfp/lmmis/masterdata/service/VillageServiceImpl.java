/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.VillageDao;
import com.wfp.lmmis.masterdata.model.Village;
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
public class VillageServiceImpl implements VillageService {

    @Autowired
    VillageDao villageDao;

    @Override
    public Village getVillage(Integer id) {
        return this.villageDao.getVillage(id);
    }

    /**
     *
     * @param village
     * @return
     */
    @Override
    public boolean save(Village village) {
        return this.villageDao.save(village);
    }

    /**
     *
     * @param village
     * @return
     */
    @Override
    public boolean edit(Village village) {
        return this.villageDao.edit(village);
    }

    @Override
    public void delete(Village village) {
        this.villageDao.delete(village);
    }

    @Override
    public List<Village> getVillageList(Integer unionId) {
        return this.villageDao.getVillageList(unionId);
    }

    /**
     *
     * @param wardNo
     * @param unionId
     * @return
     */
    @Override
    public List<ItemObject> getVillageIoList(Integer wardNo, Integer unionId) {
        return this.villageDao.getVillageIoList(wardNo, unionId);
    }

    @Override
    public List<Object> getVillageSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize) {
        return this.villageDao.getVillageSearchListBySearchParameter(parameter, beginIndex, pageSize);
    }
}
