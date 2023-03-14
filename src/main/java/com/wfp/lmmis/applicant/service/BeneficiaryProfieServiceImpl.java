/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.BeneficiaryDao;
import com.wfp.lmmis.applicant.dao.BeneficiaryProfieDao;
import com.wfp.lmmis.utility.BeneficiaryProfileView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Service
@Transactional
public class BeneficiaryProfieServiceImpl implements BeneficiaryProfieService {

    @Autowired
    BeneficiaryProfieDao beneficiaryProfieDao;

    /**
     *
     * @param beneficiaryID
     * @return
     */
    @Override
    public BeneficiaryProfileView getBenefificaryInfoByID(int beneficiaryID) {
        return beneficiaryProfieDao.getBenefificaryInfoByID(beneficiaryID);
    }
}
