/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.model.Village;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public interface VillageDao {

    /**
     *
     * @param id
     * @return
     */
    public Village getVillage(Integer id);

    /**
     *
     * @param village
     * @return
     */
    public boolean save(Village village);

    public boolean edit(Village village);

    public void delete(Village village);

    public List<Village> getVillageList(Integer unionId);

    public List<ItemObject> getVillageIoList(Integer wardNo, Integer unionId);

    /**
     *
     * @param parameter
     * @param beginIndex
     * @param pageSize
     * @return
     */
    public List<Object> getVillageSearchListBySearchParameter(Map parameter, int beginIndex, int pageSize);
}
