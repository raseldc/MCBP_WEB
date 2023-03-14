/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.BeneficiaryChildDao;
import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import com.wfp.lmmis.utility.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Service
@Transactional
public class BeneficiaryChildServiceImpl implements BeneficiaryChildService {

    @Autowired
    BeneficiaryChildDao beneficiaryChildDao;

    @Override
    public JsonResult addChildInfo(BeneficiaryChildInformation beneficiaryChildInformation) {
        return beneficiaryChildDao.addChildInfo(beneficiaryChildInformation);
    }

    @Override
    public JsonResult updateChildInfo(BeneficiaryChildInformation beneficiaryChildInformation) {
        return beneficiaryChildDao.updateChildInfo(beneficiaryChildInformation);
    }

    @Override
    public BeneficiaryChildInformation getBeneficiaryChildInformationById(int id) {
        return beneficiaryChildDao.getBeneficiaryChildInformationById(id);
    }
}
