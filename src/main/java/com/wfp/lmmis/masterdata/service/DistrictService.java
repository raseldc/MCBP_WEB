/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.DistrictInfo;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author user
 */
public interface DistrictService
{

    public District getDistrict(Integer id);

    public boolean save(District district);

    public void edit(District district);

    public boolean delete(District district);

    public List<DistrictInfo> getDistrictList(Integer divisionId);

    public List<ItemObject> getDistrictIoList(Integer divisionId);
}
