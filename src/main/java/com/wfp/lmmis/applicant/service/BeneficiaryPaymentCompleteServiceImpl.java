/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.BeneficiaryPaymentCompleteDao;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Component
@Service
@Transactional
public class BeneficiaryPaymentCompleteServiceImpl implements BeneficiaryPaymentCompleteService {

    @Autowired(required = true)
    BeneficiaryPaymentCompleteDao beneficiaryPaymentCompleteDao;

    /**
     *
     * @param skip
     * @param take
     * @param paymnetInformationViewSearchInfo
     * @return
     */
    public List<PaymentInformationView> getPaymentCompleteBeneficiaryList(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo) {
        return beneficiaryPaymentCompleteDao.getPaymentCompleteBeneficiaryList(skip, take, paymnetInformationViewSearchInfo);
    }
}
