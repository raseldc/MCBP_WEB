/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.UnionDao;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.types.CoverageArea;
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
public class UnionServiceImpl implements UnionService {

    @Autowired
    UnionDao unionDao;

    @Override
    public Union getUnion(Integer id) {
        return this.unionDao.getUnion(id);
    }

    @Override
    public boolean save(Union union) {
        return this.unionDao.save(union);
    }

    @Override
    public void edit(Union union) {
        this.unionDao.edit(union);
    }

    @Override
    public List<Union> getUnionList(Integer upazillaId) {
        return this.unionDao.getUnionList(upazillaId);
    }

    @Override
    public List<ItemObject> getUnionIoList(Integer upazillaId) {
        return this.unionDao.getUnionIoList(upazillaId);
    }

    @Override
    public List<ItemObject> getMunicipalOrCityCorporation(Integer upazillaId, CoverageArea coverageArea) {
        return this.unionDao.getMunicipalOrCityCorporation(upazillaId, coverageArea);
    }

    @Override
    public List<ItemObject> getUnionAndMunicipal(Integer upazillaId) {
        return this.unionDao.getUnionAndMunicipal(upazillaId);
    }

    @Override
    public List<Object> getUnionSearchList(String divisionId, boolean isSearchByDivisionId,
            String districtId, boolean isSearchByDistrictId, String upazilaId, boolean isSearchByUpazilaId, String unionName, boolean isSearchByName, int offset, int numofRecords) {
        return this.unionDao.getUnionSearchList(divisionId, isSearchByDivisionId, districtId, isSearchByDistrictId, upazilaId, isSearchByUpazilaId, unionName, isSearchByName, offset, numofRecords);
    }

    @Override
    public List<Object> getMunicipalOrCityCorporationSearchList(String divisionId, boolean isSearchByDivisionId,
            String districtId, boolean isSearchByDistrictId, String upazilaId, boolean isSearchByUpazilaId, CoverageArea coverageArea, int offset, int numofRecords) {
        return this.unionDao.getMunicipalOrCityCorporationSearchList(divisionId, isSearchByDivisionId, districtId, isSearchByDistrictId, upazilaId, isSearchByUpazilaId, coverageArea, offset, numofRecords);
    }

    /**
     *
     * @param union
     * @return
     */
    @Override
    public boolean delete(Union union) {
        return this.unionDao.delete(union);
    }
}
