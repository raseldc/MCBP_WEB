/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.types.CoverageArea;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author user
 */
public interface UnionDao
{

    public Union getUnion(Integer id);

    public boolean save(Union union);

    /**
     *
     * @param union
     */
    public void edit(Union union);

    public boolean delete(Union union);

    public List<Union> getUnionList(Integer upazillaId);

    public List<ItemObject> getUnionIoList(Integer upazillaId);

    public List<ItemObject> getMunicipalOrCityCorporation(Integer upazillaId, CoverageArea coverageArea);
    
    public List<ItemObject> getUnionAndMunicipal(Integer upazillaId);

    public List<Object> getUnionSearchList(String divisionId, boolean isSearchByDivisionId,
            String districtId, boolean isSearchByDistrictId, String upazilaId, boolean isSearchByUpazilaId,String unionName, boolean isSearchByName,  int offset, int numofRecords);

    public List<Object> getMunicipalOrCityCorporationSearchList(String divisionId, boolean isSearchByDivisionId,
            String districtId, boolean isSearchByDistrictId, String upazilaId, boolean isSearchByUpazilaId, CoverageArea coverageArea, int offset, int numofRecords);

}
