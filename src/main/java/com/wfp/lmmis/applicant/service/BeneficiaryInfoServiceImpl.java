/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.BeneficiaryInfoDao;
import com.wfp.lmmis.beneficiary.model.Beneficiary;
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
public class BeneficiaryInfoServiceImpl implements BeneficiaryInfoService {

    @Autowired
    BeneficiaryInfoDao beneficiaryInfoDao;

    @Override
    public JsonResult beneficiaryInfoEdit(Beneficiary beneficiary) {
        return beneficiaryInfoDao.beneficiaryInfoEdit(beneficiary);
    }

}
