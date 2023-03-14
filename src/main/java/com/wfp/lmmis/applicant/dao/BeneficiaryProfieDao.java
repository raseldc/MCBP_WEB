/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.utility.BeneficiaryProfileView;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public interface BeneficiaryProfieDao {

    public BeneficiaryProfileView getBenefificaryInfoByID(int beneficiaryID);
}
