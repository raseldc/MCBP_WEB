/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author sarwar
 */
public interface SchemeService
{
    public Scheme getScheme(Integer id);
    
    /**
     *
     * @param scheme
     */
    public void save(Scheme scheme);
    
    public void edit(Scheme scheme);
    
    public void delete(Scheme scheme);
    
    public List<Scheme> getSchemeList(Integer ministryId, boolean activeOnly);
    
    /**
     *
     * @param ministryId
     * @return
     */
    public List<ItemObject> getSchemeIoList(Integer ministryId);
}
