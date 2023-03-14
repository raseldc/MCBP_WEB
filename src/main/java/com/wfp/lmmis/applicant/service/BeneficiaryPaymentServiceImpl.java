/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.BeneficiaryPaymentDao;
import com.wfp.lmmis.payroll.model.PaymentInformationHistory;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Service
@Transactional
public class BeneficiaryPaymentServiceImpl implements BeneficiaryPaymentService {

    @Autowired
    BeneficiaryPaymentDao beneficiaryPaymentDao;

    @Override
    public List<PaymentInformationView> getPaymentInformation(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo) {
        return beneficiaryPaymentDao.getPaymentInformation(skip, take, paymnetInformationViewSearchInfo);
    }

    @Override
    public List<PaymentInformationView> getBeneficiaryPaymentHistoryList(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo) {
        return beneficiaryPaymentDao.getBeneficiaryPaymentHistoryList(skip, take, paymnetInformationViewSearchInfo);
    }

    @Override
    public JsonResult updatePaymentInfo(PaymentInformationView paymentInformationView, User loginedUser) {
        return beneficiaryPaymentDao.updatePaymentInfo(paymentInformationView, loginedUser);
    }

    /**
     *
     * @param paymentHistoryId
     * @return
     */
    @Override
    public PaymentInformationHistory getBeneficiaryPaymentHistoryById(int paymentHistoryId) {
        return beneficiaryPaymentDao.getBeneficiaryPaymentHistoryById(paymentHistoryId);
    }
}
