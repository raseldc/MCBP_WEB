/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.utility.JsonResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shamiul Islam-AnunadSolution This class create after beneficiary service due to large and
 * new requirement
 */
public interface BeneficiaryInfoService {

    public JsonResult beneficiaryInfoEdit(Beneficiary beneficiary);
}
