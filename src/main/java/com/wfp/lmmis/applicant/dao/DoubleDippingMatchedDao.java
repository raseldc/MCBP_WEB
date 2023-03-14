/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.applicant.model.DoubleDippingMatchedStatus;

/**
 *
 * @author user
 */
public interface DoubleDippingMatchedDao
{
    public DoubleDippingMatchedStatus getDoubleDippingMatchedStatus(Integer id);

    public void save(DoubleDippingMatchedStatus doubleDippingMatchedStatus);

    /**
     *
     * @param doubleDippingMatchedStatus
     */
    public void edit(DoubleDippingMatchedStatus doubleDippingMatchedStatus);
}
