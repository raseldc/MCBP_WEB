/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.SchemeDao;
import com.wfp.lmmis.masterdata.model.Scheme;
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
public class SchemeServiceImpl implements SchemeService
{

    @Autowired
    SchemeDao schemeDao;

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Scheme getScheme(Integer id)
    {
        return this.schemeDao.getScheme(id);
    }

    /**
     *
     * @param scheme
     */
    @Override
    public void save(Scheme scheme)
    {
        this.schemeDao.save(scheme);
    }

    @Override
    public void edit(Scheme scheme)
    {
        this.schemeDao.edit(scheme);
    }

    @Override
    public void delete(Scheme scheme)
    {
        this.schemeDao.delete(scheme);
    }

    @Override
    public List<Scheme> getSchemeList(Integer ministryId, boolean activeOnly)
    {
        return this.schemeDao.getSchemeList(ministryId, activeOnly);
    }

    @Override
    public List<ItemObject> getSchemeIoList(Integer ministryId)
    {
        return this.schemeDao.getSchemeIoList(ministryId);
    }

}
