/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.UpazillaDao;
import com.wfp.lmmis.masterdata.model.Upazilla;
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
public class UpazillaServiceImpl implements UpazillaService
{

    @Autowired
    UpazillaDao upazillaDao;

    @Override
    public Upazilla getUpazilla(Integer id)
    {
        return this.upazillaDao.getUpazilla(id);
    }

    /**
     *
     * @param upazilla
     * @return
     */
    @Override
    public boolean save(Upazilla upazilla)
    {
        return this.upazillaDao.save(upazilla);
    }

    @Override
    public void edit(Upazilla upazilla)
    {
        this.upazillaDao.edit(upazilla);
    }

    @Override
    public boolean delete (Upazilla upazilla)
    {
        return this.upazillaDao.delete(upazilla);
    }
    @Override
    public List<Upazilla> getUpazillaList(Integer districtId, String schemeName)
    {
        return this.upazillaDao.getUpazillaList(districtId, schemeName);
    }
    
    @Override
    public List<ItemObject> getUpazillaIoList(Integer districtId, String schemeName)
    {
        return this.upazillaDao.getUpazillaIoList(districtId, schemeName);
    }
    
    @Override
    public List<Object> getUpazilaSearchList(String divisionId, boolean isSearchByDivisionId,
            String districtId, boolean isSearchByDistrictId, int offset, int numofRecords)
    {
        return this.upazillaDao.getUpazilaSearchList(divisionId, isSearchByDivisionId, districtId, isSearchByDistrictId, offset, numofRecords);
    }
}
