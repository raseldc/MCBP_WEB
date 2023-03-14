/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.monitoring.service;

import com.wfp.lmmis.monitoring.model.Purpose;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface PurposeService
{

    public Purpose getPurpose(Integer id);

    public void save(Purpose purpose);

    public void edit(Purpose purpose);

    /**
     *
     * @param activeOnly
     * @return
     */
    public List<Purpose> getPurposeList(boolean activeOnly);
    
    public void delete(Purpose purpose);
}
