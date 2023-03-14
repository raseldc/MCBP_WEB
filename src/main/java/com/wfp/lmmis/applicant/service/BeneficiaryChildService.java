/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import com.wfp.lmmis.utility.JsonResult;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public interface BeneficiaryChildService {

    public JsonResult addChildInfo(BeneficiaryChildInformation beneficiaryChildInformation);

    public JsonResult updateChildInfo(BeneficiaryChildInformation beneficiaryChildInformation);

    public BeneficiaryChildInformation getBeneficiaryChildInformationById(int id);
}
