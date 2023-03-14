/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author user
 */
public interface UpazillaService
{

    /**
     *
     * @param id
     * @return
     */
    public Upazilla getUpazilla(Integer id);
    
    public boolean save(Upazilla upazilla);
    
    public void edit(Upazilla upazilla);
    
    public boolean delete (Upazilla upazilla);
    
    public List<Upazilla> getUpazillaList(Integer districtId, String schemeName);
    
    public List<ItemObject> getUpazillaIoList(Integer districtId, String schemeName);
    
    public List<Object> getUpazilaSearchList(String divisionId, boolean isSearchByDivisionId,
            String districtId, boolean isSearchByDistrictId, int offset, int numofRecords);
}
