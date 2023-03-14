/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.payroll.model.PaymentInformationHistory;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import java.util.List;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public interface BeneficiaryPaymentService {

    public List<PaymentInformationView> getPaymentInformation(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo);

    /**
     *
     * @param startPageIndex
     * @param recordsPerPage
     * @param paymnetInformationViewSearchInfo
     * @return
     */
    public List<PaymentInformationView> getBeneficiaryPaymentHistoryList(int startPageIndex, int recordsPerPage, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo);

    /**
     *
     * @param paymentInformationView
     * @param loginedUser
     * @return
     */
    public JsonResult updatePaymentInfo(PaymentInformationView paymentInformationView, User loginedUser);

    public PaymentInformationHistory getBeneficiaryPaymentHistoryById(int paymentHistoryId);

}
